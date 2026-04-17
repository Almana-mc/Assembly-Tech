package me.almana.assemblytech.multiblock.modifier;

import me.almana.assemblytech.multiblock.slave.MultiblockSlaveEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ModifierEntity extends MultiblockSlaveEntity {

    private final List<ModifierAttribute> attributes;

    public ModifierEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, List<ModifierAttribute> attributes) {
        super(type, pos, state);
        this.attributes = List.copyOf(attributes);
    }

    public List<ModifierAttribute> getAttributes() {
        return attributes;
    }
}
