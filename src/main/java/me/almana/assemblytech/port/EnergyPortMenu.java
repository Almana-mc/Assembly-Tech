package me.almana.assemblytech.port;

import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.registry.ModMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EnergyPortMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    @Nullable
    private final EnergyPortBlockEntity server;
    private int clientEnergy;

    public EnergyPortMenu(int containerId, Inventory playerInv, EnergyPortBlockEntity be) {
        super(ModMenus.ENERGY_PORT.get(), containerId);
        this.server = be;
        this.access = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
        addEnergyDataSlots();
    }

    public EnergyPortMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        super(ModMenus.ENERGY_PORT.get(), containerId);
        this.server = null;
        this.access = ContainerLevelAccess.NULL;
        addEnergyDataSlots();
    }

    private void addEnergyDataSlots() {
        addDataSlot(new DataSlot() {
            @Override public int get() { return (currentEnergy() >>> 16) & 0xFFFF; }
            @Override public void set(int v) { clientEnergy = (clientEnergy & 0xFFFF) | ((v & 0xFFFF) << 16); }
        });
        addDataSlot(new DataSlot() {
            @Override public int get() { return currentEnergy() & 0xFFFF; }
            @Override public void set(int v) { clientEnergy = (clientEnergy & 0xFFFF0000) | (v & 0xFFFF); }
        });
    }

    private int currentEnergy() {
        return server != null ? (int) server.getEnergy().getAmountAsLong() : clientEnergy;
    }

    public int getEnergy() {
        return currentEnergy();
    }

    public int getCapacity() {
        return EnergyPortBlockEntity.CAPACITY;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return access.evaluate((level, pos) -> {
            if (level.getBlockState(pos).getBlock() != ModBlocks.ENERGY_PORT.get()) return false;
            return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
        }, true);
    }
}
