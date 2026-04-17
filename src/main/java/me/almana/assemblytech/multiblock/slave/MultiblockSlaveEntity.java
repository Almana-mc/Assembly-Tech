package me.almana.assemblytech.multiblock.slave;

import me.almana.assemblytech.multiblock.controller.MultiblockControllerEntity;
import me.almana.assemblytech.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class MultiblockSlaveEntity extends BlockEntity {

    private static final String TAG_CONTROLLER_X = "CtrlX";
    private static final String TAG_CONTROLLER_Y = "CtrlY";
    private static final String TAG_CONTROLLER_Z = "CtrlZ";
    private static final String TAG_HAS_CONTROLLER = "HasCtrl";

    @Nullable
    private BlockPos controllerPos;

    public MultiblockSlaveEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.SLAVE.get(), pos, state);
    }

    public MultiblockSlaveEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void bindTo(BlockPos controller) {
        this.controllerPos = controller;
        setChanged();
    }

    public void unbind() {
        this.controllerPos = null;
        setChanged();
    }

    @Nullable
    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public boolean isBound() {
        return controllerPos != null;
    }

    public void notifyControllerOfBreak() {
        if (controllerPos == null || level == null) return;

        BlockEntity be = level.getBlockEntity(controllerPos);
        if (be instanceof MultiblockControllerEntity controller) {
            controller.flagRevalidation();
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (controllerPos != null) {
            output.putBoolean(TAG_HAS_CONTROLLER, true);
            output.putInt(TAG_CONTROLLER_X, controllerPos.getX());
            output.putInt(TAG_CONTROLLER_Y, controllerPos.getY());
            output.putInt(TAG_CONTROLLER_Z, controllerPos.getZ());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        if (input.getBooleanOr(TAG_HAS_CONTROLLER, false)) {
            controllerPos = new BlockPos(
                    input.getIntOr(TAG_CONTROLLER_X, 0),
                    input.getIntOr(TAG_CONTROLLER_Y, 0),
                    input.getIntOr(TAG_CONTROLLER_Z, 0)
            );
        } else {
            controllerPos = null;
        }
    }
}
