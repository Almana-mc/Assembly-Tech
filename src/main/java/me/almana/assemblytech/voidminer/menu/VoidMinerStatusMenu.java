package me.almana.assemblytech.voidminer.menu;

import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.registry.ModMenus;
import me.almana.assemblytech.voidminer.VoidMinerControllerEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class VoidMinerStatusMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    @Nullable
    private final VoidMinerControllerEntity server;

    private int clientEnergy;
    private int clientCapacity;
    private int clientStatus;
    private int clientProgress;
    private int clientProgressMax;
    private int clientEnergyPerTick;
    private int clientUpgradePlaced;
    private int clientUpgradeMax;

    public VoidMinerStatusMenu(int containerId, Inventory playerInv, VoidMinerControllerEntity be) {
        super(ModMenus.VOID_MINER.get(), containerId);
        this.server = be;
        this.access = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
        addSyncSlots();
    }

    public VoidMinerStatusMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        super(ModMenus.VOID_MINER.get(), containerId);
        this.server = null;
        this.access = ContainerLevelAccess.NULL;
        addSyncSlots();
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
            @Override public int get() { return server != null ? (server.isWorking() ? 1 : 0) : clientStatus; }
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
        return server != null ? server.getPlacedUpgradeCount() : clientUpgradePlaced;
    }

    private int upgradeMax() {
        return server != null ? server.getMaxUpgrades() : clientUpgradeMax;
    }

    public int getUpgradePlaced() {
        return upgradePlaced();
    }

    public int getUpgradeMax() {
        return upgradeMax();
    }

    public boolean isOverUpgradeCap() {
        return upgradePlaced() > upgradeMax();
    }

    private int energyPerTick() {
        return server != null ? server.getEnergyPerTick() : clientEnergyPerTick;
    }

    public int getEnergyPerTick() {
        return energyPerTick();
    }

    private int progress() {
        return server != null ? server.getProgressCurrent() : clientProgress;
    }

    private int progressMax() {
        return server != null ? server.getProgressMax() : clientProgressMax;
    }

    public int getProgress() {
        return progress();
    }

    public int getProgressMax() {
        return progressMax();
    }

    private int energy() {
        return server != null ? server.getAggregateEnergyStored() : clientEnergy;
    }

    private int capacity() {
        return server != null ? server.getAggregateEnergyCapacity() : clientCapacity;
    }

    public int getEnergy() {
        return energy();
    }

    public int getCapacity() {
        return capacity();
    }

    public boolean isWorking() {
        return server != null ? server.isWorking() : clientStatus != 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
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
