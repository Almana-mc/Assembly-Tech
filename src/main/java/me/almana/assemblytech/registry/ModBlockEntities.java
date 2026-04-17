package me.almana.assemblytech.registry;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.multiblock.slave.MultiblockSlaveEntity;
import me.almana.assemblytech.port.EnergyPortBlockEntity;
import me.almana.assemblytech.port.FluidPortBlockEntity;
import me.almana.assemblytech.port.ItemPortBlockEntity;
import me.almana.assemblytech.upgrade.UpgradeEntity;
import me.almana.assemblytech.voidminer.VoidMinerControllerEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public final class ModBlockEntities {

    private ModBlockEntities() {}

    public static final Supplier<BlockEntityType<MultiblockSlaveEntity>> SLAVE =
            Assemblytech.BLOCK_ENTITY_TYPES.register("slave",
                    () -> new BlockEntityType<>(
                            MultiblockSlaveEntity::new,
                            false,
                            ModBlocks.STRUCTURE_FRAME_1.get(),
                            ModBlocks.STRUCTURE_PANEL.get(),
                            ModBlocks.DRILL_CORE.get(),
                            ModBlocks.DRILL_BLOCK.get(),
                            ModBlocks.VOID_BLOCK.get()
                    )
            );

    public static final Supplier<BlockEntityType<VoidMinerControllerEntity>> VOID_MINER_CONTROLLER =
            Assemblytech.BLOCK_ENTITY_TYPES.register("void_miner_controller",
                    () -> new BlockEntityType<>(
                            VoidMinerControllerEntity::new,
                            false,
                            ModBlocks.VOID_MINER_CONTROLLER_1.get()
                    )
            );

    public static final Supplier<BlockEntityType<ItemPortBlockEntity>> ITEM_PORT =
            Assemblytech.BLOCK_ENTITY_TYPES.register("item_port",
                    () -> new BlockEntityType<>(
                            ItemPortBlockEntity::new,
                            false,
                            ModBlocks.ITEM_PORT.get()
                    )
            );

    public static final Supplier<BlockEntityType<EnergyPortBlockEntity>> ENERGY_PORT =
            Assemblytech.BLOCK_ENTITY_TYPES.register("energy_port",
                    () -> new BlockEntityType<>(
                            EnergyPortBlockEntity::new,
                            false,
                            ModBlocks.ENERGY_PORT.get()
                    )
            );

    public static final Supplier<BlockEntityType<FluidPortBlockEntity>> FLUID_PORT =
            Assemblytech.BLOCK_ENTITY_TYPES.register("fluid_port",
                    () -> new BlockEntityType<>(
                            FluidPortBlockEntity::new,
                            false,
                            ModBlocks.FLUID_PORT.get()
                    )
            );

    public static final Supplier<BlockEntityType<UpgradeEntity>> UPGRADE =
            Assemblytech.BLOCK_ENTITY_TYPES.register("upgrade",
                    () -> new BlockEntityType<>(
                            UpgradeEntity::new,
                            false,
                            ModBlocks.UPGRADE_SPEED.get(),
                            ModBlocks.UPGRADE_EFFICIENCY.get(),
                            ModBlocks.UPGRADE_FORTUNE_1.get(),
                            ModBlocks.UPGRADE_FORTUNE_2.get(),
                            ModBlocks.UPGRADE_FORTUNE_3.get(),
                            ModBlocks.UPGRADE_PARALLEL_1.get(),
                            ModBlocks.UPGRADE_PARALLEL_2.get(),
                            ModBlocks.UPGRADE_PARALLEL_3.get()
                    )
            );

    public static void init() {}
}
