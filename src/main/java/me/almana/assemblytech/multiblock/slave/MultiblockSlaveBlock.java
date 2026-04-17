package me.almana.assemblytech.multiblock.slave;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class MultiblockSlaveBlock extends Block implements EntityBlock {

    private final Supplier<BlockEntityType<? extends MultiblockSlaveEntity>> entityType;

    public MultiblockSlaveBlock(Properties properties, Supplier<BlockEntityType<? extends MultiblockSlaveEntity>> entityType) {
        super(properties);
        this.entityType = entityType;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return entityType.get().create(pos, state);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof MultiblockSlaveEntity slave) {
            slave.notifyControllerOfBreak();
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }
}
