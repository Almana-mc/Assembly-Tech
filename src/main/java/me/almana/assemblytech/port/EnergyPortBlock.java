package me.almana.assemblytech.port;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class EnergyPortBlock extends PortBlock {

    public EnergyPortBlock(Properties properties, Supplier<BlockEntityType<? extends PortBlockEntity>> entityType) {
        super(properties, entityType);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && player instanceof ServerPlayer sp) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof EnergyPortBlockEntity port) {
                sp.openMenu(port);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
