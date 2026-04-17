package me.almana.assemblytech.multiblock.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public record StructureComponent(
        BlockPos offset,
        ComponentType type,
        int minimumTier,
        Predicate<BlockState> validator
) {
    public boolean test(Level level, BlockPos worldPos) {
        return validator.test(level.getBlockState(worldPos));
    }
}
