package me.almana.assemblytech.upgrade;

import me.almana.assemblytech.multiblock.modifier.ModifierEntity;
import me.almana.assemblytech.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class UpgradeEntity extends ModifierEntity {

    public UpgradeEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.UPGRADE.get(), pos, state);
    }

    public UpgradeEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, UpgradeBlocks.getAttributes(state.getBlock()));
    }
}
