package me.almana.assemblytech.registry;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.multiblock.controller.MultiblockControllerBlock;
import me.almana.assemblytech.multiblock.modifier.ModifierBlock;
import me.almana.assemblytech.multiblock.slave.MultiblockSlaveBlock;
import me.almana.assemblytech.port.EnergyPortBlock;
import me.almana.assemblytech.port.FluidPortBlock;
import me.almana.assemblytech.port.ItemPortBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

public final class ModBlocks {

    private ModBlocks() {}

    public static final DeferredBlock<Block> STRUCTURE_FRAME_1 = Assemblytech.BLOCKS.registerBlock(
            "structure_frame_1",
            props -> new MultiblockSlaveBlock(props, ModBlockEntities.SLAVE::get),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0f, 100.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<Block> STRUCTURE_PANEL = Assemblytech.BLOCKS.registerBlock(
            "structure_panel",
            props -> new MultiblockSlaveBlock(props, ModBlockEntities.SLAVE::get),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0f, 100.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<Block> DRILL_CORE = Assemblytech.BLOCKS.registerBlock(
            "drill_core",
            props -> new MultiblockSlaveBlock(props, ModBlockEntities.SLAVE::get),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.0f, 100.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 4)
    );

    public static final DeferredBlock<Block> DRILL_BLOCK = Assemblytech.BLOCKS.registerBlock(
            "drill_block",
            props -> new MultiblockSlaveBlock(props, ModBlockEntities.SLAVE::get),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.0f, 100.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<Block> VOID_BLOCK = Assemblytech.BLOCKS.registerBlock(
            "void_block",
            props -> new MultiblockSlaveBlock(props, ModBlockEntities.SLAVE::get),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(4.0f, 100.0f)
                    .sound(SoundType.AMETHYST)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 8)
    );

    public static final DeferredBlock<Block> VOID_MINER_CONTROLLER_1 = Assemblytech.BLOCKS.registerBlock(
            "void_miner_controller_1",
            props -> new MultiblockControllerBlock(props, ModBlockEntities.VOID_MINER_CONTROLLER::get),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(4.0f, 100.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 7)
    );

    public static final DeferredBlock<Block> ITEM_PORT = Assemblytech.BLOCKS.registerBlock(
            "item_port",
            props -> new ItemPortBlock(props, ModBlockEntities.ITEM_PORT::get),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0f, 6.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<Block> ENERGY_PORT = Assemblytech.BLOCKS.registerBlock(
            "energy_port",
            props -> new EnergyPortBlock(props, ModBlockEntities.ENERGY_PORT::get),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(2.0f, 6.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<Block> FLUID_PORT = Assemblytech.BLOCKS.registerBlock(
            "fluid_port",
            props -> new FluidPortBlock(props, ModBlockEntities.FLUID_PORT::get),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .strength(2.0f, 6.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
    );

    private static DeferredBlock<ModifierBlock> registerUpgrade(String name) {
        return Assemblytech.BLOCKS.registerBlock(
                name,
                props -> new ModifierBlock(props, ModBlockEntities.UPGRADE::get),
                () -> BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .strength(3.0f, 100.0f)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops()
        );
    }

    public static final DeferredBlock<ModifierBlock> UPGRADE_SPEED = registerUpgrade("upgrade_speed");
    public static final DeferredBlock<ModifierBlock> UPGRADE_EFFICIENCY = registerUpgrade("upgrade_efficiency");
    public static final DeferredBlock<ModifierBlock> UPGRADE_FORTUNE_1 = registerUpgrade("upgrade_fortune_1");
    public static final DeferredBlock<ModifierBlock> UPGRADE_FORTUNE_2 = registerUpgrade("upgrade_fortune_2");
    public static final DeferredBlock<ModifierBlock> UPGRADE_FORTUNE_3 = registerUpgrade("upgrade_fortune_3");
    public static final DeferredBlock<ModifierBlock> UPGRADE_PARALLEL_1 = registerUpgrade("upgrade_parallel_1");
    public static final DeferredBlock<ModifierBlock> UPGRADE_PARALLEL_2 = registerUpgrade("upgrade_parallel_2");
    public static final DeferredBlock<ModifierBlock> UPGRADE_PARALLEL_3 = registerUpgrade("upgrade_parallel_3");

    public static void init() {}
}
