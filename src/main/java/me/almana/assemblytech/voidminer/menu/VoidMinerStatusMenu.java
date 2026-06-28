package me.almana.assemblytech.voidminer.menu;

import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.registry.ModMenus;
import me.almana.assemblytech.voidminer.VoidMinerControllerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class VoidMinerStatusMenu extends AbstractContainerMenu {

    private static final int OUTPUT_SLOT_COUNT = 27;
    private static final int DESIGNATOR_SLOT = OUTPUT_SLOT_COUNT + 36;
    private static final int SLOT_OUTPUT_X = 110;
    private static final int SLOT_OUTPUT_Y = 57;
    private static final int INV_X = 110;
    private static final int INV_Y = 147;
    private static final int DESIGNATOR_X = 307;
    private static final int DESIGNATOR_Y = 68;

    private static final TagKey<net.minecraft.world.item.Item> DESIGNATORS =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("assemblytech", "target_designators"));

    private static final class OutputSlot extends Slot {
        OutputSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }
        @Override public boolean mayPlace(ItemStack stack) { return false; }
    }

    private static final class DesignatorSlot extends Slot {
        DesignatorSlot(Container container, int x, int y) {
            super(container, 0, x, y);
        }
        @Override public boolean mayPlace(ItemStack stack) { return stack.is(DESIGNATORS); }
        @Override public int getMaxStackSize() { return 1; }
    }

    private final ContainerLevelAccess access;
    private final SimpleContainer clientOutput = new SimpleContainer(OUTPUT_SLOT_COUNT);
    private final SimpleContainer clientDesignator = new SimpleContainer(1);
    @Nullable
    private final VoidMinerControllerEntity miner;
    private final boolean clientSide;

    private int clientEnergy;
    private int clientCapacity;
    private int clientFluid;
    private int clientFluidCapacity;
    private int clientStatus;
    private int clientProgress;
    private int clientProgressMax;
    private int clientEnergyPerTick;
    private int clientUpgradePlaced;
    private int clientUpgradeMax;
    private int clientTier;

    public VoidMinerStatusMenu(int containerId, Inventory playerInv, VoidMinerControllerEntity be) {
        super(ModMenus.VOID_MINER.get(), containerId);
        this.miner = be;
        this.clientSide = false;
        this.access = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
        addOutputSlots(be);
        addPlayerSlots(playerInv);
        addDesignatorSlot(be.getDesignatorContainer());
        addSyncSlots();
    }

    public VoidMinerStatusMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        super(ModMenus.VOID_MINER.get(), containerId);
        BlockPos pos = buf.readBlockPos();
        readInitialData(buf);
        Level level = playerInv.player.level();
        BlockEntity be = level.getBlockEntity(pos);
        this.miner = be instanceof VoidMinerControllerEntity controller ? controller : null;
        this.clientSide = true;
        this.access = ContainerLevelAccess.create(level, pos);
        addOutputSlots(null);
        addPlayerSlots(playerInv);
        addDesignatorSlot(clientDesignator);
        addSyncSlots();
    }

    private void readInitialData(RegistryFriendlyByteBuf buf) {
        if (buf.readableBytes() < 44) return;
        clientEnergy = buf.readInt();
        clientCapacity = buf.readInt();
        clientFluid = buf.readInt();
        clientFluidCapacity = buf.readInt();
        clientStatus = buf.readInt();
        clientProgress = buf.readInt();
        clientProgressMax = buf.readInt();
        clientEnergyPerTick = buf.readInt();
        clientUpgradePlaced = buf.readInt();
        clientUpgradeMax = buf.readInt();
        clientTier = buf.readInt();
        for (int i = 0; i < OUTPUT_SLOT_COUNT && buf.readableBytes() > 0; i++) {
            clientOutput.setItem(i, ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
        }
    }

    private void addOutputSlots(@Nullable VoidMinerControllerEntity be) {
        Container c = be != null ? be : clientOutput;
        for (int i = 0; i < OUTPUT_SLOT_COUNT; i++)
            addSlot(new OutputSlot(c, i, SLOT_OUTPUT_X + i % 9 * 18, SLOT_OUTPUT_Y + i / 9 * 18));
    }

    private void addDesignatorSlot(Container c) {
        addSlot(new DesignatorSlot(c, DESIGNATOR_X, DESIGNATOR_Y));
    }

    private void addPlayerSlots(Inventory inv) {
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(inv, col + row * 9 + 9, INV_X + col * 18, INV_Y + row * 18));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(inv, col, INV_X + col * 18, INV_Y + 58));
    }

    private void addSyncSlots() {
        addDataSlot(new DataSlot() {
            @Override public int get() { return (energy() >>> 16) & 0xFFFF; }
            @Override public void set(int v) { clientEnergy = (clientEnergy & 0xFFFF) | ((v & 0xFFFF) << 16); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return energy() & 0xFFFF; }
            @Override public void set(int v) { clientEnergy = (clientEnergy & 0xFFFF0000) | (v & 0xFFFF); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return (capacity() >>> 16) & 0xFFFF; }
            @Override public void set(int v) { clientCapacity = (clientCapacity & 0xFFFF) | ((v & 0xFFFF) << 16); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return capacity() & 0xFFFF; }
            @Override public void set(int v) { clientCapacity = (clientCapacity & 0xFFFF0000) | (v & 0xFFFF); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return (fluid() >>> 16) & 0xFFFF; }
            @Override public void set(int v) { clientFluid = (clientFluid & 0xFFFF) | ((v & 0xFFFF) << 16); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return fluid() & 0xFFFF; }
            @Override public void set(int v) { clientFluid = (clientFluid & 0xFFFF0000) | (v & 0xFFFF); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return (fluidCapacity() >>> 16) & 0xFFFF; }
            @Override public void set(int v) { clientFluidCapacity = (clientFluidCapacity & 0xFFFF) | ((v & 0xFFFF) << 16); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return fluidCapacity() & 0xFFFF; }
            @Override public void set(int v) { clientFluidCapacity = (clientFluidCapacity & 0xFFFF0000) | (v & 0xFFFF); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return miner != null && !clientSide ? (miner.isWorking() ? 1 : 0) : clientStatus; }
            @Override public void set(int v) { clientStatus = v; }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return progress() & 0xFFFF; }
            @Override public void set(int v) { clientProgress = v & 0xFFFF; }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return progressMax() & 0xFFFF; }
            @Override public void set(int v) { clientProgressMax = v & 0xFFFF; }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return (energyPerTick() >>> 16) & 0xFFFF; }
            @Override public void set(int v) { clientEnergyPerTick = (clientEnergyPerTick & 0xFFFF) | ((v & 0xFFFF) << 16); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return energyPerTick() & 0xFFFF; }
            @Override public void set(int v) { clientEnergyPerTick = (clientEnergyPerTick & 0xFFFF0000) | (v & 0xFFFF); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return upgradePlaced() & 0xFFFF; }
            @Override public void set(int v) { clientUpgradePlaced = v & 0xFFFF; }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return upgradeMax() & 0xFFFF; }
            @Override public void set(int v) { clientUpgradeMax = v & 0xFFFF; }
        });
    }

    private int upgradePlaced() {
        return miner != null && !clientSide ? miner.getPlacedUpgradeCount() : clientUpgradePlaced;
    }

    private int upgradeMax() {
        return miner != null && !clientSide ? miner.getMaxUpgrades() : clientUpgradeMax;
    }

    public int getUpgradePlaced() {
        return upgradePlaced();
    }

    public int getUpgradeMax() {
        return upgradeMax();
    }

    public int getTier() {
        return miner != null && !clientSide ? miner.getMultiblockType().tier() : clientTier;
    }

    public boolean isOverUpgradeCap() {
        return upgradePlaced() > upgradeMax();
    }

    private int energyPerTick() {
        return miner != null && !clientSide ? miner.getEnergyPerTick() : clientEnergyPerTick;
    }

    public int getEnergyPerTick() {
        return energyPerTick();
    }

    private int progress() {
        return miner != null && !clientSide ? miner.getProgressCurrent() : clientProgress;
    }

    private int progressMax() {
        return miner != null && !clientSide ? miner.getProgressMax() : clientProgressMax;
    }

    public int getProgress() {
        return progress();
    }

    public int getProgressMax() {
        return progressMax();
    }

    private int energy() {
        return miner != null && !clientSide ? miner.getAggregateEnergyStored() : clientEnergy;
    }

    private int capacity() {
        return miner != null && !clientSide ? miner.getAggregateEnergyCapacity() : clientCapacity;
    }

    private int fluid() {
        return miner != null && !clientSide ? miner.getFluidStored() : clientFluid;
    }

    private int fluidCapacity() {
        return miner != null && !clientSide ? miner.getFluidCapacity() : clientFluidCapacity;
    }

    public int getEnergy() {
        return energy();
    }

    public int getCapacity() {
        return capacity();
    }

    public int getFluid() {
        return fluid();
    }

    public int getFluidCapacity() {
        return fluidCapacity();
    }

    public boolean isWorking() {
        return miner != null && !clientSide ? miner.isWorking() : clientStatus != 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < OUTPUT_SLOT_COUNT) {
            if (!moveItemStackTo(stack, OUTPUT_SLOT_COUNT, OUTPUT_SLOT_COUNT + 36, true))
                return ItemStack.EMPTY;
        } else if (index == DESIGNATOR_SLOT) {
            if (!moveItemStackTo(stack, OUTPUT_SLOT_COUNT, OUTPUT_SLOT_COUNT + 36, false))
                return ItemStack.EMPTY;
        } else if (stack.is(DESIGNATORS)) {
            if (!moveItemStackTo(stack, DESIGNATOR_SLOT, DESIGNATOR_SLOT + 1, false))
                return ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return access.evaluate((level, pos) -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof VoidMinerControllerEntity)) return false;
            if (!ModBlocks.isMinerController(level.getBlockState(pos).getBlock())) return false;
            return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
        }, true);
    }

}
