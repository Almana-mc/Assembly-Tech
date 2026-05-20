package me.almana.assemblytech.datagen;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.registry.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
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
            blockModels.createTrivialCube(ModBlocks.panel(tier).get());
            blockModels.createTrivialCube(ModBlocks.controller(tier).get());
        }
        blockModels.createTrivialCube(ModBlocks.DRILL_CORE.get());
        blockModels.createTrivialCube(ModBlocks.DRILL_BLOCK.get());
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
        for (int tier = 1; tier <= ModBlocks.MINER_TIERS; tier++) {
            itemModels.generateFlatItem(ModItems.crystal(tier).get(), ModelTemplates.FLAT_ITEM);
        }
    }
}
