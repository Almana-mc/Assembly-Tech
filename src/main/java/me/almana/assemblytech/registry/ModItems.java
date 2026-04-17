package me.almana.assemblytech.registry;

import me.almana.assemblytech.Assemblytech;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public final class ModItems {

    private ModItems() {}

    public static final DeferredItem<BlockItem> STRUCTURE_FRAME_1 =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_frame_1", ModBlocks.STRUCTURE_FRAME_1);

    public static final DeferredItem<BlockItem> STRUCTURE_PANEL =
            Assemblytech.ITEMS.registerSimpleBlockItem("structure_panel", ModBlocks.STRUCTURE_PANEL);

    public static final DeferredItem<BlockItem> DRILL_CORE =
            Assemblytech.ITEMS.registerSimpleBlockItem("drill_core", ModBlocks.DRILL_CORE);

    public static final DeferredItem<BlockItem> DRILL_BLOCK =
            Assemblytech.ITEMS.registerSimpleBlockItem("drill_block", ModBlocks.DRILL_BLOCK);

    public static final DeferredItem<BlockItem> VOID_BLOCK =
            Assemblytech.ITEMS.registerSimpleBlockItem("void_block", ModBlocks.VOID_BLOCK);

    public static final DeferredItem<BlockItem> VOID_MINER_CONTROLLER_1 =
            Assemblytech.ITEMS.registerSimpleBlockItem("void_miner_controller_1", ModBlocks.VOID_MINER_CONTROLLER_1);

    public static final DeferredItem<Item> ASSEMBLER =
            Assemblytech.ITEMS.registerItem("assembler",
                    props -> new me.almana.assemblytech.multiblock.tool.AssemblerItem(props));

    public static final DeferredItem<Item> TIER_1_CRYSTAL =
            Assemblytech.ITEMS.registerSimpleItem("tier_1_crystal");

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

    public static void init() {}
}
