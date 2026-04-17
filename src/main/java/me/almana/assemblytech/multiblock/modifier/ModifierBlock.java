package me.almana.assemblytech.multiblock.modifier;

import me.almana.assemblytech.multiblock.slave.MultiblockSlaveBlock;
import me.almana.assemblytech.multiblock.slave.MultiblockSlaveEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModifierBlock extends MultiblockSlaveBlock {

    public ModifierBlock(Properties properties, Supplier<BlockEntityType<? extends MultiblockSlaveEntity>> entityType) {
        super(properties, entityType);
    }
}
