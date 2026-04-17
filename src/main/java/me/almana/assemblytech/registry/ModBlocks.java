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

    public static final int MINER_TIERS = 7;

    private static DeferredBlock<Block> registerFrame(int tier) {
        return Assemblytech.BLOCKS.registerBlock(
                "structure_frame_" + tier,
                props -> new MultiblockSlaveBlock(props, ModBlockEntities.SLAVE::get),
                () -> BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .strength(3.0f + tier * 0.5f, 100.0f)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops()
        );
    }

    private static DeferredBlock<Block> registerPanel(int tier) {
        return Assemblytech.BLOCKS.registerBlock(
                "structure_panel_" + tier,
                props -> new MultiblockSlaveBlock(props, ModBlockEntities.SLAVE::get),
                () -> BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .strength(3.0f + tier * 0.5f, 100.0f)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops()
        );
    }

    private static DeferredBlock<Block> registerController(int tier) {
        return Assemblytech.BLOCKS.registerBlock(
                "void_miner_controller_" + tier,
                props -> new MultiblockControllerBlock(props, ModBlockEntities.VOID_MINER_CONTROLLER::get),
                () -> BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_GREEN)
                        .strength(4.0f + tier * 0.5f, 100.0f)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops()
                        .lightLevel(state -> 7)
        );
    }

    public static final DeferredBlock<Block> STRUCTURE_FRAME_1 = registerFrame(1);
    public static final DeferredBlock<Block> STRUCTURE_FRAME_2 = registerFrame(2);
    public static final DeferredBlock<Block> STRUCTURE_FRAME_3 = registerFrame(3);
    public static final DeferredBlock<Block> STRUCTURE_FRAME_4 = registerFrame(4);
    public static final DeferredBlock<Block> STRUCTURE_FRAME_5 = registerFrame(5);
    public static final DeferredBlock<Block> STRUCTURE_FRAME_6 = registerFrame(6);
    public static final DeferredBlock<Block> STRUCTURE_FRAME_7 = registerFrame(7);

    public static final DeferredBlock<Block> STRUCTURE_PANEL_1 = registerPanel(1);
    public static final DeferredBlock<Block> STRUCTURE_PANEL_2 = registerPanel(2);
    public static final DeferredBlock<Block> STRUCTURE_PANEL_3 = registerPanel(3);
    public static final DeferredBlock<Block> STRUCTURE_PANEL_4 = registerPanel(4);
    public static final DeferredBlock<Block> STRUCTURE_PANEL_5 = registerPanel(5);
    public static final DeferredBlock<Block> STRUCTURE_PANEL_6 = registerPanel(6);
    public static final DeferredBlock<Block> STRUCTURE_PANEL_7 = registerPanel(7);

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

    public static final DeferredBlock<Block> VOID_MINER_CONTROLLER_1 = registerController(1);
    public static final DeferredBlock<Block> VOID_MINER_CONTROLLER_2 = registerController(2);
    public static final DeferredBlock<Block> VOID_MINER_CONTROLLER_3 = registerController(3);
    public static final DeferredBlock<Block> VOID_MINER_CONTROLLER_4 = registerController(4);
    public static final DeferredBlock<Block> VOID_MINER_CONTROLLER_5 = registerController(5);
    public static final DeferredBlock<Block> VOID_MINER_CONTROLLER_6 = registerController(6);
    public static final DeferredBlock<Block> VOID_MINER_CONTROLLER_7 = registerController(7);

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

    public static DeferredBlock<Block> frame(int tier) {
        return switch (tier) {
            case 1 -> STRUCTURE_FRAME_1;
            case 2 -> STRUCTURE_FRAME_2;
            case 3 -> STRUCTURE_FRAME_3;
            case 4 -> STRUCTURE_FRAME_4;
            case 5 -> STRUCTURE_FRAME_5;
            case 6 -> STRUCTURE_FRAME_6;
            case 7 -> STRUCTURE_FRAME_7;
            default -> STRUCTURE_FRAME_1;
        };
    }

    public static DeferredBlock<Block> panel(int tier) {
        return switch (tier) {
            case 1 -> STRUCTURE_PANEL_1;
            case 2 -> STRUCTURE_PANEL_2;
            case 3 -> STRUCTURE_PANEL_3;
            case 4 -> STRUCTURE_PANEL_4;
            case 5 -> STRUCTURE_PANEL_5;
            case 6 -> STRUCTURE_PANEL_6;
            case 7 -> STRUCTURE_PANEL_7;
            default -> STRUCTURE_PANEL_1;
        };
    }

    public static DeferredBlock<Block> controller(int tier) {
        return switch (tier) {
            case 1 -> VOID_MINER_CONTROLLER_1;
            case 2 -> VOID_MINER_CONTROLLER_2;
            case 3 -> VOID_MINER_CONTROLLER_3;
            case 4 -> VOID_MINER_CONTROLLER_4;
            case 5 -> VOID_MINER_CONTROLLER_5;
            case 6 -> VOID_MINER_CONTROLLER_6;
            case 7 -> VOID_MINER_CONTROLLER_7;
            default -> VOID_MINER_CONTROLLER_1;
        };
    }

    public static int controllerTier(Block block) {
        if (block == VOID_MINER_CONTROLLER_1.get()) return 1;
        if (block == VOID_MINER_CONTROLLER_2.get()) return 2;
        if (block == VOID_MINER_CONTROLLER_3.get()) return 3;
        if (block == VOID_MINER_CONTROLLER_4.get()) return 4;
        if (block == VOID_MINER_CONTROLLER_5.get()) return 5;
        if (block == VOID_MINER_CONTROLLER_6.get()) return 6;
        if (block == VOID_MINER_CONTROLLER_7.get()) return 7;
        return 0;
    }

    public static boolean isMinerController(Block block) {
        return controllerTier(block) != 0;
    }

    public static void init() {}
}
