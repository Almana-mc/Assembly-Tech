package me.almana.assemblytech.voidminer;

import me.almana.assemblytech.generation.LootRollSnapshot;
import me.almana.assemblytech.generation.MinerLootRegistries;
import me.almana.assemblytech.generation.MinerLootRoll;
import me.almana.assemblytech.generation.MinerLootTable;
import me.almana.assemblytech.generation.MinerTierConfig;
import me.almana.assemblytech.generation.MinerTierConfigRegistries;
import me.almana.assemblytech.multiblock.api.MultiblockType;
import me.almana.assemblytech.multiblock.controller.MultiblockControllerEntity;
import me.almana.assemblytech.multiblock.modifier.ModifierData;
import me.almana.assemblytech.registry.ModBlockEntities;
import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.voidminer.menu.VoidMinerStatusMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class VoidMinerControllerEntity extends MultiblockControllerEntity implements MenuProvider, Container {

    private static final int OUTPUT_SLOTS = 27;
    private static final String TAG_OUTPUT = "Output";
    private static final String TAG_COUNTER = "ProcessCounter";
    private static final String TAG_BLOCKED_NO_SPACE = "BlockedNoSpace";
    private static final String TAG_ENERGY = "Energy";
    private static final String TAG_FLUID = "Fluid";

    private final ItemStacksResourceHandler outputHandler = new ItemStacksResourceHandler(OUTPUT_SLOTS) {
        @Override
        protected void onContentsChanged(int index, ItemStack previous) {
            setChanged();
        }

        @Override
        public void deserialize(ValueInput input) {
            super.deserialize(input);
            if (size() == OUTPUT_SLOTS) return;
            NonNullList<ItemStack> existing = copyToList();
            NonNullList<ItemStack> resized = NonNullList.withSize(OUTPUT_SLOTS, ItemStack.EMPTY);
            for (int i = 0; i < Math.min(existing.size(), OUTPUT_SLOTS); i++)
                resized.set(i, existing.get(i));
            setStacks(resized);
        }
    };

    private final SimpleEnergyHandler energyStorage = new SimpleEnergyHandler(20_000_000, Integer.MAX_VALUE, 0) {
        @Override
        public long getCapacityAsLong() {
            return VoidMinerTiers.oreMinerEnergyCapacity(getMultiblockType().tier());
        }

        @Override
        public int insert(int amount, TransactionContext transaction) {
            this.capacity = VoidMinerTiers.oreMinerEnergyCapacity(getMultiblockType().tier());
            return super.insert(amount, transaction);
        }

        @Override
        protected void onEnergyChanged(int previousAmount) {
            setChanged();
        }
    };

    private final FluidStacksResourceHandler fluidHandler = new FluidStacksResourceHandler(1, 1_000_000) {
        @Override
        protected void onContentsChanged(int index, FluidStack previous) {
            setChanged();
        }
    };

    private int processCounter;
    private boolean blockedNoSpace;
    private boolean pendingRoll;
    @Nullable
    private MinerTierConfig cachedTierConfig;

    public VoidMinerControllerEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.VOID_MINER_CONTROLLER.get(), pos, state);
    }

    public VoidMinerControllerEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public MultiblockType getMultiblockType() {
        int tier = ModBlocks.controllerTier(getBlockState().getBlock());
        return VoidMinerStructures.get(tier);
    }

    public ItemStacksResourceHandler getOutputHandler() {
        return outputHandler;
    }

    public SimpleEnergyHandler getEnergyHandler() {
        return energyStorage;
    }

    public FluidStacksResourceHandler getFluidHandler() {
        return fluidHandler;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.assemblytech.void_miner");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player) {
        return new VoidMinerStatusMenu(containerId, inv, this);
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
        buf.writeInt(getAggregateEnergyStored());
        buf.writeInt(getAggregateEnergyCapacity());
        buf.writeInt(getFluidStored());
        buf.writeInt(getFluidCapacity());
        buf.writeInt(isWorking() ? 1 : 0);
        buf.writeInt(getProgressCurrent());
        buf.writeInt(getProgressMax());
        buf.writeInt(getEnergyPerTick());
        buf.writeInt(getPlacedUpgradeCount());
        buf.writeInt(getMaxUpgrades());
        buf.writeInt(getMultiblockType().tier());
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, getItem(i));
        }
    }

    @Nullable
    private MinerLootTable lookupLootTable() {
        if (level == null) return null;
        Registry<MinerLootTable> reg = level.registryAccess()
                .lookup(MinerLootRegistries.MINER_LOOT)
                .orElse(null);
        return reg == null ? null : reg.getValue(MinerLootRegistries.VOID_MINER_ORE);
    }

    @Override
    protected void onFormed() {
    }

    @Override
    protected void onBroken() {
        processCounter = 0;
        blockedNoSpace = false;
        cachedTierConfig = null;
    }

    private MinerTierConfig tierConfig() {
        if (cachedTierConfig != null) return cachedTierConfig;
        int tier = getMultiblockType().tier();
        if (level != null) {
            Registry<MinerTierConfig> reg = level.registryAccess()
                    .lookup(MinerTierConfigRegistries.MINER_TIER_CONFIG)
                    .orElse(null);
            if (reg != null) {
                MinerTierConfig cfg = reg.getValue(MinerTierConfigRegistries.oreMinerKey(tier));
                if (cfg != null) {
                    cachedTierConfig = cfg;
                    return cfg;
                }
            }
        }
        cachedTierConfig = new MinerTierConfig(
                tier,
                VoidMinerTiers.oreMinerEnergyPerTick(tier),
                VoidMinerTiers.oreMinerProcessTicks(tier),
                VoidMinerTiers.oreMinerUpgradeSlots(tier)
        );
        return cachedTierConfig;
    }

    @Override
    protected int getMaxUpgradeSlots() {
        return tierConfig().upgradeSlots();
    }

    private ModifierData effectiveModifiers() {
        ModifierData md = getModifierData();
        return md != null ? md : ModifierData.empty();
    }

    private int effectiveProcessTicks(MinerTierConfig cfg) {
        return Math.max(1, (int) Math.ceil(cfg.processTicks() * effectiveModifiers().getSpeedMultiplier()));
    }

    private int effectiveEnergyPerTick(MinerTierConfig cfg) {
        return Math.max(0, (int) Math.ceil(
                VoidMinerTiers.oreMinerEnergyPerTick(getMultiblockType().tier())
                * effectiveModifiers().getEfficiencyMultiplier()
        ));
    }

    @Override
    protected void tickFormed(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;
        if (pendingRoll) return;
        if (isOverUpgradeCap()) return;

        if (!anyOutputHasFreeSlot()) {
            setBlocked(true);
            return;
        }

        MinerTierConfig cfg = tierConfig();
        setBlocked(false);

        int cost = effectiveEnergyPerTick(cfg);
        if (energyStorage.getAmountAsLong() < cost) return;
        energyStorage.set((int) (energyStorage.getAmountAsLong() - cost));

        processCounter++;
        if (processCounter < effectiveProcessTicks(cfg)) return;
        processCounter = 0;

        MinerLootTable table = lookupLootTable();
        if (table == null) return;

        long seed = level.getRandom().nextLong();
        LootRollSnapshot snap = MinerLootRoll.prepareSnapshot(table, cfg.tier(), getModifierData(), seed);
        if (snap.isEmpty()) return;

        MinecraftServer server = ((ServerLevel) level).getServer();
        BlockPos self = worldPosition;
        pendingRoll = true;

        ForkJoinPool.commonPool().execute(() -> {
            List<ItemStack> drops = MinerLootRoll.rollSnapshot(snap);
            server.execute(() -> applyDrops(self, drops));
        });
    }

    private boolean anyOutputHasFreeSlot() {
        int n = outputHandler.size();
        for (int i = 0; i < n; i++) {
            ItemResource res = outputHandler.getResource(i);
            if (res.isEmpty()) return true;
            if (outputHandler.getAmountAsLong(i) < outputHandler.getCapacityAsLong(i, res)) return true;
        }
        return false;
    }

    private void applyDrops(BlockPos self, List<ItemStack> drops) {
        pendingRoll = false;
        if (isRemoved() || !formed || !worldPosition.equals(self)) return;
        if (level == null) return;
        if (drops.isEmpty()) return;

        try (Transaction tx = Transaction.open(null)) {
            for (ItemStack drop : drops) {
                ItemStack leftover = ItemUtil.insertItemReturnRemaining(outputHandler, drop, false, tx);
                if (!leftover.isEmpty()) {
                    setBlocked(true);
                    return;
                }
            }
            tx.commit();
        }
        setChanged();
    }

    private void setBlocked(boolean noSpace) {
        if (noSpace != blockedNoSpace) {
            blockedNoSpace = noSpace;
            setChanged();
        }
    }

    public boolean isWorking() {
        if (!formed || blockedNoSpace || pendingRoll || isOverUpgradeCap()) return false;
        if (!anyOutputHasFreeSlot()) return false;
        return energyStorage.getAmountAsLong() >= effectiveEnergyPerTick(tierConfig());
    }

    public int getMaxUpgrades() {
        return tierConfig().upgradeSlots();
    }

    public int getAggregateEnergyStored() {
        return (int) energyStorage.getAmountAsLong();
    }

    public int getAggregateEnergyCapacity() {
        return VoidMinerTiers.oreMinerEnergyCapacity(getMultiblockType().tier());
    }

    public int getFluidStored() {
        return fluidHandler.getAmountAsInt(0);
    }

    public int getFluidCapacity() {
        return fluidHandler.getCapacityAsInt(0, fluidHandler.getResource(0));
    }

    public int getProgressCurrent() {
        return processCounter;
    }

    public int getProgressMax() {
        return effectiveProcessTicks(tierConfig());
    }

    public int getEnergyPerTick() {
        return effectiveEnergyPerTick(tierConfig());
    }

    @Override
    public int getContainerSize() { return OUTPUT_SLOTS; }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < OUTPUT_SLOTS; i++)
            if (!outputHandler.getResource(i).isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        ItemResource res = outputHandler.getResource(slot);
        if (res.isEmpty()) return ItemStack.EMPTY;
        return res.toStack((int) outputHandler.getAmountAsLong(slot));
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemResource res = outputHandler.getResource(slot);
        if (res.isEmpty()) return ItemStack.EMPTY;
        try (Transaction tx = Transaction.open(null)) {
            int taken = outputHandler.extract(slot, res, count, tx);
            if (taken == 0) return ItemStack.EMPTY;
            tx.commit();
            return res.toStack(taken);
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemResource res = outputHandler.getResource(slot);
        if (res.isEmpty()) return ItemStack.EMPTY;
        try (Transaction tx = Transaction.open(null)) {
            int taken = outputHandler.extract(slot, res, Integer.MAX_VALUE, tx);
            if (taken == 0) return ItemStack.EMPTY;
            tx.commit();
            return res.toStack(taken);
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        try (Transaction tx = Transaction.open(null)) {
            ItemResource cur = outputHandler.getResource(slot);
            if (!cur.isEmpty()) outputHandler.extract(slot, cur, Integer.MAX_VALUE, tx);
            if (!stack.isEmpty()) outputHandler.insert(slot, ItemResource.of(stack), stack.getCount(), tx);
            tx.commit();
        }
        setChanged();
    }

    @Override
    public void clearContent() {
        try (Transaction tx = Transaction.open(null)) {
            for (int i = 0; i < OUTPUT_SLOTS; i++) {
                ItemResource res = outputHandler.getResource(i);
                if (!res.isEmpty()) outputHandler.extract(i, res, Integer.MAX_VALUE, tx);
            }
            tx.commit();
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) { return true; }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt(TAG_COUNTER, processCounter);
        output.putBoolean(TAG_BLOCKED_NO_SPACE, blockedNoSpace);
        output.putInt(TAG_ENERGY, (int) energyStorage.getAmountAsLong());
        outputHandler.serialize(output.child(TAG_OUTPUT));
        fluidHandler.serialize(output.child(TAG_FLUID));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        processCounter = input.getIntOr(TAG_COUNTER, 0);
        blockedNoSpace = input.getBooleanOr(TAG_BLOCKED_NO_SPACE, false);
        energyStorage.set(input.getIntOr(TAG_ENERGY, 0));
        input.child(TAG_OUTPUT).ifPresent(outputHandler::deserialize);
        input.child(TAG_FLUID).ifPresent(fluidHandler::deserialize);
    }
}
