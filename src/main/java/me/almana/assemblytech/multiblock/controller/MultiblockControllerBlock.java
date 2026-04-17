package me.almana.assemblytech.multiblock.controller;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class MultiblockControllerBlock extends Block implements EntityBlock {

    private final Supplier<BlockEntityType<? extends MultiblockControllerEntity>> entityType;

    public MultiblockControllerBlock(Properties properties, Supplier<BlockEntityType<? extends MultiblockControllerEntity>> entityType) {
        super(properties);
        this.entityType = entityType;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return entityType.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTicker(type);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <T extends BlockEntity> BlockEntityTicker<T> createTicker(BlockEntityType<T> type) {
        if (type == entityType.get()) {
            return (BlockEntityTicker<T>) (BlockEntityTicker<? extends MultiblockControllerEntity>) MultiblockControllerEntity::tick;
        }
        return null;
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        if (level.isClientSide()) return;
        if (level.getBlockEntity(pos) instanceof MultiblockControllerEntity controller) {
            controller.flagRevalidation();
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof MultiblockControllerEntity controller) {
            controller.deform();
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && player instanceof ServerPlayer sp) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MultiblockControllerEntity controller) {
                if (controller.isFormed() && controller instanceof MenuProvider provider) {
                    sp.openMenu(provider);
                    return InteractionResult.SUCCESS;
                }
                if (!controller.isFormed()) {
                    controller.tryForm();
                }
                sp.sendSystemMessage(
                        Component.translatable(
                                controller.isFormed()
                                        ? "assemblytech.multiblock.status_formed"
                                        : "assemblytech.multiblock.status_not_formed"
                        ),
                        true
                );
            }
        }

        return InteractionResult.SUCCESS;
    }
}
