package me.almana.assemblytech.registry;

import me.almana.assemblytech.Assemblytech;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public final class ModItems {

    private ModItems() {}

    public static final DeferredItem<BlockItem> STRUCTURE_FRAME_1 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_frame_1", ModBlocks.STRUCTURE_FRAME_1);
    public static final DeferredItem<BlockItem> STRUCTURE_FRAME_2 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_frame_2", ModBlocks.STRUCTURE_FRAME_2);
    public static final DeferredItem<BlockItem> STRUCTURE_FRAME_3 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_frame_3", ModBlocks.STRUCTURE_FRAME_3);
    public static final DeferredItem<BlockItem> STRUCTURE_FRAME_4 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_frame_4", ModBlocks.STRUCTURE_FRAME_4);
    public static final DeferredItem<BlockItem> STRUCTURE_FRAME_5 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_frame_5", ModBlocks.STRUCTURE_FRAME_5);
    public static final DeferredItem<BlockItem> STRUCTURE_FRAME_6 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_frame_6", ModBlocks.STRUCTURE_FRAME_6);
    public static final DeferredItem<BlockItem> STRUCTURE_FRAME_7 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_frame_7", ModBlocks.STRUCTURE_FRAME_7);

    public static final DeferredItem<BlockItem> STRUCTURE_PANEL_1 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_panel_1", ModBlocks.STRUCTURE_PANEL_1);
    public static final DeferredItem<BlockItem> STRUCTURE_PANEL_2 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_panel_2", ModBlocks.STRUCTURE_PANEL_2);
    public static final DeferredItem<BlockItem> STRUCTURE_PANEL_3 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_panel_3", ModBlocks.STRUCTURE_PANEL_3);
    public static final DeferredItem<BlockItem> STRUCTURE_PANEL_4 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_panel_4", ModBlocks.STRUCTURE_PANEL_4);
    public static final DeferredItem<BlockItem> STRUCTURE_PANEL_5 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_panel_5", ModBlocks.STRUCTURE_PANEL_5);
    public static final DeferredItem<BlockItem> STRUCTURE_PANEL_6 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_panel_6", ModBlocks.STRUCTURE_PANEL_6);
    public static final DeferredItem<BlockItem> STRUCTURE_PANEL_7 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_panel_7", ModBlocks.STRUCTURE_PANEL_7);

    public static final DeferredItem<BlockItem> DRILL_CORE =
            Assemblytech.ITEMS.registerSimpleBlockItem("drill_core", ModBlocks.DRILL_CORE);

    public static final DeferredItem<BlockItem> DRILL_BLOCK =
            Assemblytech.ITEMS.registerSimpleBlockItem("drill_block", ModBlocks.DRILL_BLOCK);

    public static final DeferredItem<BlockItem> VOID_BLOCK =
            Assemblytech.ITEMS.registerSimpleBlockItem("void_block", ModBlocks.VOID_BLOCK);

    public static final DeferredItem<BlockItem> VOID_MINER_CONTROLLER_1 =
            Assemblytech.ITEMS.registerSimpleBlockItem("void_miner_controller_1", ModBlocks.VOID_MINER_CONTROLLER_1);
    public static final DeferredItem<BlockItem> VOID_MINER_CONTROLLER_2 =
            Assemblytech.ITEMS.registerSimpleBlockItem("void_miner_controller_2", ModBlocks.VOID_MINER_CONTROLLER_2);
    public static final DeferredItem<BlockItem> VOID_MINER_CONTROLLER_3 =
            Assemblytech.ITEMS.registerSimpleBlockItem("void_miner_controller_3", ModBlocks.VOID_MINER_CONTROLLER_3);
    public static final DeferredItem<BlockItem> VOID_MINER_CONTROLLER_4 =
            Assemblytech.ITEMS.registerSimpleBlockItem("void_miner_controller_4", ModBlocks.VOID_MINER_CONTROLLER_4);
    public static final DeferredItem<BlockItem> VOID_MINER_CONTROLLER_5 =
            Assemblytech.ITEMS.registerSimpleBlockItem("void_miner_controller_5", ModBlocks.VOID_MINER_CONTROLLER_5);
    public static final DeferredItem<BlockItem> VOID_MINER_CONTROLLER_6 =
            Assemblytech.ITEMS.registerSimpleBlockItem("void_miner_controller_6", ModBlocks.VOID_MINER_CONTROLLER_6);
    public static final DeferredItem<BlockItem> VOID_MINER_CONTROLLER_7 =
            Assemblytech.ITEMS.registerSimpleBlockItem("void_miner_controller_7", ModBlocks.VOID_MINER_CONTROLLER_7);

    public static final DeferredItem<Item> ASSEMBLER =
            Assemblytech.ITEMS.registerItem("assembler",
                    props -> new me.almana.assemblytech.multiblock.tool.AssemblerItem(props));

    public static final DeferredItem<Item> ARCANITE_CRYSTAL =
            Assemblytech.ITEMS.registerSimpleItem("arcanite_crystal");
    public static final DeferredItem<Item> SOLUNITE_CRYSTAL =
            Assemblytech.ITEMS.registerSimpleItem("solunite_crystal");
    public static final DeferredItem<Item> CELESTIUM_CRYSTAL =
            Assemblytech.ITEMS.registerSimpleItem("celestium_crystal");
    public static final DeferredItem<Item> XENORITE_CRYSTAL =
            Assemblytech.ITEMS.registerSimpleItem("xenorite_crystal");
    public static final DeferredItem<Item> PRISMARA_CRYSTAL =
            Assemblytech.ITEMS.registerSimpleItem("prismara_crystal");
    public static final DeferredItem<Item> ECLIPSIUM_CRYSTAL =
            Assemblytech.ITEMS.registerSimpleItem("eclipsium_crystal");
    public static final DeferredItem<Item> COSMYRITE_CRYSTAL =
            Assemblytech.ITEMS.registerSimpleItem("cosmyrite_crystal");

    public static final DeferredItem<BlockItem> ITEM_PORT =
            Assemblytech.ITEMS.registerSimpleBlockItem("item_port", ModBlocks.ITEM_PORT);

    public static final DeferredItem<BlockItem> ENERGY_PORT =
            Assemblytech.ITEMS.registerSimpleBlockItem("energy_port", ModBlocks.ENERGY_PORT);

    public static final DeferredItem<BlockItem> FLUID_PORT =
            Assemblytech.ITEMS.registerSimpleBlockItem("fluid_port", ModBlocks.FLUID_PORT);

    public static final DeferredItem<BlockItem> UPGRADE_SPEED =
            Assemblytech.ITEMS.registerSimpleBlockItem("upgrade_speed", ModBlocks.UPGRADE_SPEED);

    public static final DeferredItem<BlockItem> UPGRADE_EFFICIENCY =
            Assemblytech.ITEMS.registerSimpleBlockItem("upgrade_efficiency", ModBlocks.UPGRADE_EFFICIENCY);

    public static final DeferredItem<BlockItem> UPGRADE_FORTUNE_1 =
            Assemblytech.ITEMS.registerSimpleBlockItem("upgrade_fortune_1", ModBlocks.UPGRADE_FORTUNE_1);

    public static final DeferredItem<BlockItem> UPGRADE_FORTUNE_2 =
            Assemblytech.ITEMS.registerSimpleBlockItem("upgrade_fortune_2", ModBlocks.UPGRADE_FORTUNE_2);

    public static final DeferredItem<BlockItem> UPGRADE_FORTUNE_3 =
            Assemblytech.ITEMS.registerSimpleBlockItem("upgrade_fortune_3", ModBlocks.UPGRADE_FORTUNE_3);

    public static final DeferredItem<BlockItem> UPGRADE_PARALLEL_1 =
            Assemblytech.ITEMS.registerSimpleBlockItem("upgrade_parallel_1", ModBlocks.UPGRADE_PARALLEL_1);

    public static final DeferredItem<BlockItem> UPGRADE_PARALLEL_2 =
            Assemblytech.ITEMS.registerSimpleBlockItem("upgrade_parallel_2", ModBlocks.UPGRADE_PARALLEL_2);

    public static final DeferredItem<BlockItem> UPGRADE_PARALLEL_3 =
            Assemblytech.ITEMS.registerSimpleBlockItem("upgrade_parallel_3", ModBlocks.UPGRADE_PARALLEL_3);

    public static final DeferredItem<BlockItem> ROUGH_GEOTHERMAL_VENT =
            Assemblytech.ITEMS.registerSimpleBlockItem("rough_geothermal_vent", ModBlocks.ROUGH_GEOTHERMAL_VENT);
    public static final DeferredItem<BlockItem> GEOTHERMAL_VENT =
            Assemblytech.ITEMS.registerSimpleBlockItem("geothermal_vent", ModBlocks.GEOTHERMAL_VENT);
    public static final DeferredItem<BlockItem> PRISTINE_GEOTHERMAL_VENT =
            Assemblytech.ITEMS.registerSimpleBlockItem("pristine_geothermal_vent", ModBlocks.PRISTINE_GEOTHERMAL_VENT);

    public static final DeferredItem<BlockItem> ROUGH_GEOTHERMAL_VENT_WALL =
            Assemblytech.ITEMS.registerSimpleBlockItem("rough_geothermal_vent_wall", ModBlocks.ROUGH_GEOTHERMAL_VENT_WALL);
    public static final DeferredItem<BlockItem> GEOTHERMAL_VENT_WALL =
            Assemblytech.ITEMS.registerSimpleBlockItem("geothermal_vent_wall", ModBlocks.GEOTHERMAL_VENT_WALL);
    public static final DeferredItem<BlockItem> PRISTINE_GEOTHERMAL_VENT_WALL =
            Assemblytech.ITEMS.registerSimpleBlockItem("pristine_geothermal_vent_wall", ModBlocks.PRISTINE_GEOTHERMAL_VENT_WALL);

    public static DeferredItem<Item> crystal(int tier) {
        return switch (tier) {
            case 1 -> ARCANITE_CRYSTAL;
            case 2 -> SOLUNITE_CRYSTAL;
            case 3 -> CELESTIUM_CRYSTAL;
            case 4 -> XENORITE_CRYSTAL;
            case 5 -> PRISMARA_CRYSTAL;
            case 6 -> ECLIPSIUM_CRYSTAL;
            case 7 -> COSMYRITE_CRYSTAL;
            default -> ARCANITE_CRYSTAL;
        };
    }

    public static void init() {}
}
