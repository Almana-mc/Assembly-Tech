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
import me.almana.assemblytech.port.EnergyPortBlockEntity;
import me.almana.assemblytech.port.ItemPortBlockEntity;
import me.almana.assemblytech.registry.ModBlockEntities;
import me.almana.assemblytech.voidminer.menu.VoidMinerStatusMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class VoidMinerControllerEntity extends MultiblockControllerEntity implements MenuProvider {

    private static final int OUTPUT_SLOTS = 4;
    private static final String TAG_OUTPUT = "Output";
    private static final String TAG_COUNTER = "ProcessCounter";
    private static final String TAG_BLOCKED_NO_ENERGY = "BlockedNoEnergy";
    private static final String TAG_BLOCKED_NO_SPACE = "BlockedNoSpace";

    private final ItemStacksResourceHandler outputHandler = new ItemStacksResourceHandler(OUTPUT_SLOTS) {
        @Override
        protected void onContentsChanged(int index, ItemStack previous) {
            setChanged();
        }
    };

    private int processCounter;
    private List<BlockPos> itemPortPositions = List.of();
    private List<BlockPos> energyPortPositions = List.of();
    private boolean portsCached;
    private boolean blockedNoEnergy;
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
        return VoidMinerStructures.TIER_1;
    }

    public ItemStacksResourceHandler getOutputHandler() {
        return outputHandler;
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
        rebuildPortCaches();
    }

    @Override
    protected void onBroken() {
        processCounter = 0;
        itemPortPositions = List.of();
        energyPortPositions = List.of();
        portsCached = false;
        blockedNoEnergy = false;
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
        return Math.max(0, (int) Math.ceil(cfg.energyPerTick() * effectiveModifiers().getEfficiencyMultiplier()));
    }

    private void rebuildPortCaches() {
        if (level == null) return;
        List<BlockPos> items = new ArrayList<>();
        List<BlockPos> energy = new ArrayList<>();
        for (BlockPos slave : getSlavePositions()) {
            BlockEntity be = level.getBlockEntity(slave);
            if (be instanceof ItemPortBlockEntity) {
                items.add(slave.immutable());
            } else if (be instanceof EnergyPortBlockEntity) {
                energy.add(slave.immutable());
            }
        }
        itemPortPositions = List.copyOf(items);
        energyPortPositions = List.copyOf(energy);
        portsCached = true;
    }

    @Override
    protected void tickFormed(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        if (!portsCached) rebuildPortCaches();

        if (pendingRoll) return;

        if (isOverUpgradeCap()) {
            return;
        }

        if (itemPortPositions.isEmpty() || energyPortPositions.isEmpty()) {
            setBlocked(itemPortPositions.isEmpty(), energyPortPositions.isEmpty());
            return;
        }

        if (!anyItemPortHasFreeSlot()) {
            setBlocked(true, false);
            return;
        }

        MinerTierConfig cfg = tierConfig();
        if (!tryDrainPerTick(effectiveEnergyPerTick(cfg))) {
            setBlocked(false, true);
            return;
        }

        setBlocked(false, false);

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

    private boolean tryDrainPerTick(int perTick) {
        if (perTick <= 0) return true;
        try (Transaction tx = Transaction.open(null)) {
            int drained = 0;
            for (BlockPos energyPort : energyPortPositions) {
                if (drained >= perTick) break;
                BlockEntity be = level.getBlockEntity(energyPort);
                if (be instanceof EnergyPortBlockEntity ep) {
                    drained += ep.getEnergy().extract(perTick - drained, tx);
                }
            }
            if (drained < perTick) return false;
            tx.commit();
            return true;
        }
    }

    private boolean anyItemPortHasFreeSlot() {
        for (BlockPos p : itemPortPositions) {
            BlockEntity be = level.getBlockEntity(p);
            if (be instanceof ItemPortBlockEntity ip) {
                var h = ip.getInventory();
                int n = h.size();
                for (int i = 0; i < n; i++) {
                    ItemResource res = h.getResource(i);
                    if (res.isEmpty()) return true;
                    if (h.getAmountAsLong(i) < h.getCapacityAsLong(i, res)) return true;
                }
            }
        }
        return false;
    }

    private void applyDrops(BlockPos self, List<ItemStack> drops) {
        pendingRoll = false;
        if (isRemoved() || !formed || !worldPosition.equals(self)) return;
        if (level == null) return;
        if (drops.isEmpty()) return;
        if (itemPortPositions.isEmpty()) {
            setBlocked(true, false);
            return;
        }

        try (Transaction tx = Transaction.open(null)) {
            for (ItemStack drop : drops) {
                ItemStack leftover = drop;
                for (BlockPos itemPort : itemPortPositions) {
                    if (leftover.isEmpty()) break;
                    BlockEntity be = level.getBlockEntity(itemPort);
                    if (be instanceof ItemPortBlockEntity ip) {
                        leftover = ItemUtil.insertItemReturnRemaining(ip.getInventory(), leftover, false, tx);
                    }
                }
                if (!leftover.isEmpty()) {
                    setBlocked(true, false);
                    return;
                }
            }
            tx.commit();
        }
        setChanged();
    }

    private void setBlocked(boolean noSpace, boolean noEnergy) {
        if (noEnergy != blockedNoEnergy || noSpace != blockedNoSpace) {
            blockedNoEnergy = noEnergy;
            blockedNoSpace = noSpace;
            setChanged();
        }
    }

    public boolean isWorking() {
        return formed && portsCached
                && !itemPortPositions.isEmpty()
                && !energyPortPositions.isEmpty()
                && !blockedNoEnergy
                && !blockedNoSpace
                && !isOverUpgradeCap();
    }

    public int getMaxUpgrades() {
        return tierConfig().upgradeSlots();
    }

    public int getAggregateEnergyStored() {
        if (level == null) return 0;
        long total = 0;
        for (BlockPos p : energyPortPositions) {
            BlockEntity be = level.getBlockEntity(p);
            if (be instanceof EnergyPortBlockEntity ep) {
                total += ep.getEnergy().getAmountAsLong();
            }
        }
        return (int) Math.min(Integer.MAX_VALUE, total);
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

    public int getAggregateEnergyCapacity() {
        if (level == null) return 0;
        long total = 0;
        for (BlockPos p : energyPortPositions) {
            BlockEntity be = level.getBlockEntity(p);
            if (be instanceof EnergyPortBlockEntity ep) {
                total += ep.getEnergy().getCapacityAsLong();
            }
        }
        return (int) Math.min(Integer.MAX_VALUE, total);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt(TAG_COUNTER, processCounter);
        output.putBoolean(TAG_BLOCKED_NO_ENERGY, blockedNoEnergy);
        output.putBoolean(TAG_BLOCKED_NO_SPACE, blockedNoSpace);
        outputHandler.serialize(output.child(TAG_OUTPUT));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        processCounter = input.getIntOr(TAG_COUNTER, 0);
        blockedNoEnergy = input.getBooleanOr(TAG_BLOCKED_NO_ENERGY, false);
        blockedNoSpace = input.getBooleanOr(TAG_BLOCKED_NO_SPACE, false);
        input.child(TAG_OUTPUT).ifPresent(outputHandler::deserialize);
    }
}
