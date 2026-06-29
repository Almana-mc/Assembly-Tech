package me.almana.assemblytech.datagen;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.registry.ModItems;
import me.almana.assemblytech.voidminer.client.TargetDesignatorItemRenderer;
import me.almana.assemblytech.voidminer.LaserBlock;
import me.almana.assemblytech.voidminer.client.LaserItemRenderer;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public final class AssemblytechModelProvider extends ModelProvider {

    public AssemblytechModelProvider(PackOutput output) {
        super(output, Assemblytech.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (int tier = 1; tier <= ModBlocks.MINER_TIERS; tier++) {
            Block frame = ModBlocks.frame(tier).get();
            Material baseTexture = new Material(Identifier.fromNamespaceAndPath(Assemblytech.MODID, "block/tier_" + tier + "/structure_frame_" + tier + "_base"));
            blockModels.createTrivialBlock(frame, b -> TexturedModel.createAllSame(baseTexture));
            Block panel = ModBlocks.panel(tier).get();
            Material panelBase = new Material(Identifier.fromNamespaceAndPath(Assemblytech.MODID, "block/tier_" + tier + "/structure_panel_" + tier + "_base"));
            blockModels.createTrivialBlock(panel, b -> TexturedModel.createAllSame(panelBase));
            blockModels.createTrivialCube(ModBlocks.controller(tier).get());
        }
        blockModels.createTrivialCube(ModBlocks.DRILL_CORE.get());
        Identifier drillModel = TexturedModel.CUBE.create(ModBlocks.DRILL_BLOCK.get(), blockModels.modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.DRILL_BLOCK.get())
                .with(PropertyDispatch.initial(LaserBlock.FACING)
                        .generate(facing -> BlockModelGenerators.plainVariant(drillModel))));
        blockModels.createTrivialCube(ModBlocks.VOID_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.UPGRADE_SPEED.get());
        blockModels.createTrivialCube(ModBlocks.UPGRADE_EFFICIENCY.get());
        blockModels.createTrivialCube(ModBlocks.UPGRADE_FORTUNE_1.get());
        blockModels.createTrivialCube(ModBlocks.UPGRADE_FORTUNE_2.get());
        blockModels.createTrivialCube(ModBlocks.UPGRADE_FORTUNE_3.get());
        blockModels.createTrivialCube(ModBlocks.UPGRADE_PARALLEL_1.get());
        blockModels.createTrivialCube(ModBlocks.UPGRADE_PARALLEL_2.get());
        blockModels.createTrivialCube(ModBlocks.UPGRADE_PARALLEL_3.get());
        blockModels.createTrivialCube(ModBlocks.ROUGH_GEOTHERMAL_VENT.get());
        blockModels.createTrivialCube(ModBlocks.GEOTHERMAL_VENT.get());
        blockModels.createTrivialCube(ModBlocks.PRISTINE_GEOTHERMAL_VENT.get());
        blockModels.createTrivialCube(ModBlocks.ROUGH_GEOTHERMAL_VENT_WALL.get());
        blockModels.createTrivialCube(ModBlocks.GEOTHERMAL_VENT_WALL.get());
        blockModels.createTrivialCube(ModBlocks.PRISTINE_GEOTHERMAL_VENT_WALL.get());

        itemModels.generateFlatItem(ModItems.ASSEMBLER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.itemModelOutput.accept(ModItems.TARGET_DESIGNATOR.get(), ItemModelUtils.specialModel(
                Identifier.fromNamespaceAndPath(Assemblytech.MODID, "item/target_designator_base"),
                new TargetDesignatorItemRenderer.Unbaked()
        ));
        itemModels.itemModelOutput.accept(ModItems.RESOURCE_DESIGNATOR.get(), ItemModelUtils.specialModel(
                Identifier.fromNamespaceAndPath(Assemblytech.MODID, "item/target_designator_base"),
                new TargetDesignatorItemRenderer.Unbaked()
        ));
        itemModels.itemModelOutput.accept(ModItems.DRILL_BLOCK.get(), ItemModelUtils.specialModel(
                Identifier.fromNamespaceAndPath(Assemblytech.MODID, "block/drill_block"),
                new LaserItemRenderer.Unbaked()
        ));
        for (int tier = 1; tier <= ModBlocks.MINER_TIERS; tier++) {
            itemModels.generateFlatItem(ModItems.crystal(tier).get(), ModelTemplates.FLAT_ITEM);
        }
    }
}
