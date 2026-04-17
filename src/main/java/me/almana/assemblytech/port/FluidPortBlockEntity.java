package me.almana.assemblytech.port;

import me.almana.assemblytech.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import org.jetbrains.annotations.Nullable;

public class FluidPortBlockEntity extends PortBlockEntity implements MenuProvider {

    public static final int CAPACITY = 1_000_000;
    private static final String TAG_FLUID = "Fluid";

    private final FluidStacksResourceHandler tank = new FluidStacksResourceHandler(1, CAPACITY) {
        @Override
        protected void onContentsChanged(int index, FluidStack previous) {
            setChanged();
        }
    };

    public FluidPortBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.FLUID_PORT.get(), pos, state);
    }

    public FluidPortBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public FluidStacksResourceHandler getTank() {
        return tank;
    }

    public FluidStack getFluidStack() {
        FluidResource resource = tank.getResource(0);
        int amount = tank.getAmountAsInt(0);
        return resource.isEmpty() || amount <= 0 ? FluidStack.EMPTY : resource.toStack(amount);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.assemblytech.fluid_port");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player) {
        return new FluidPortMenu(containerId, inv, this);
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buf) {
        FluidStack.OPTIONAL_STREAM_CODEC.encode(buf, getFluidStack());
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        tank.serialize(output.child(TAG_FLUID));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.child(TAG_FLUID).ifPresent(tank::deserialize);
    }
}
