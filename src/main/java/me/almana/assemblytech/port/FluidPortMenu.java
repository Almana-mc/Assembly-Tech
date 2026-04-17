package me.almana.assemblytech.port;

import me.almana.assemblytech.network.FluidPortSyncPayload;
import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.registry.ModMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class FluidPortMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    @Nullable
    private final FluidPortBlockEntity server;
    @Nullable
    private final ServerPlayer viewer;
    private FluidStack lastSent = FluidStack.EMPTY;
    private FluidStack clientFluid = FluidStack.EMPTY;

    public FluidPortMenu(int containerId, Inventory playerInv, FluidPortBlockEntity be) {
        super(ModMenus.FLUID_PORT.get(), containerId);
        this.server = be;
        this.access = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
        this.viewer = playerInv.player instanceof ServerPlayer sp ? sp : null;
    }

    public FluidPortMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        super(ModMenus.FLUID_PORT.get(), containerId);
        this.server = null;
        this.access = ContainerLevelAccess.NULL;
        this.viewer = null;
        this.clientFluid = FluidStack.OPTIONAL_STREAM_CODEC.decode(buf);
    }

    public FluidStack getFluid() {
        return server != null ? server.getFluidStack() : clientFluid;
    }

    public int getCapacity() {
        return FluidPortBlockEntity.CAPACITY;
    }

    public void setFluidClient(FluidStack stack) {
        this.clientFluid = stack;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (server == null || viewer == null) return;
        FluidStack current = server.getFluidStack();
        if (!FluidStack.matches(current, lastSent)) {
            lastSent = current.copy();
            PacketDistributor.sendToPlayer(viewer, new FluidPortSyncPayload(containerId, current.copy()));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return access.evaluate((level, pos) -> {
            if (level.getBlockState(pos).getBlock() != ModBlocks.FLUID_PORT.get()) return false;
            return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
        }, true);
    }
}
