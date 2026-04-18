package me.almana.assemblytech.worldgen;

import com.mojang.serialization.Codec;
import me.almana.assemblytech.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GeothermalVentFeature extends Feature<NoneFeatureConfiguration> {

    private final int tier;

    public GeothermalVentFeature(int tier, Codec<NoneFeatureConfiguration> codec) {
        super(codec);
        this.tier = tier;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();
        int x0 = origin.getX();
        int z0 = origin.getZ();

        int[] dx = {0, 1, 0, 1};
        int[] dz = {0, 0, 1, 1};

        int minY = level.getMinY();
        int bedrockScanLimit = minY + 6;

        int bedrockTop = Integer.MIN_VALUE;
        int[] columnBedrockTop = new int[4];
        int[] surfaceY = new int[4];

        BlockPos.MutableBlockPos scan = new BlockPos.MutableBlockPos();
        for (int i = 0; i < 4; i++) {
            int x = x0 + dx[i];
            int z = z0 + dz[i];

            int highestBedrock = Integer.MIN_VALUE;
            for (int y = minY; y <= bedrockScanLimit; y++) {
                scan.set(x, y, z);
                if (level.getBlockState(scan).is(Blocks.BEDROCK)) {
                    highestBedrock = y;
                }
            }
            columnBedrockTop[i] = highestBedrock;
            if (highestBedrock > bedrockTop) {
                bedrockTop = highestBedrock;
            }

            surfaceY[i] = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);
        }

        if (bedrockTop == Integer.MIN_VALUE) {
            return false;
        }

        int floorY = bedrockTop + 1;
        Block ventBlock = ModBlocks.vent(tier).get();
        Block wallBlock = ModBlocks.ventWall(tier).get();
        BlockState ventState = ventBlock.defaultBlockState();
        BlockState wallState = wallBlock.defaultBlockState();
        BlockState bedrockState = Blocks.BEDROCK.defaultBlockState();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < 4; i++) {
            int x = x0 + dx[i];
            int z = z0 + dz[i];
            for (int y = columnBedrockTop[i] + 1; y <= bedrockTop; y++) {
                pos.set(x, y, z);
                level.setBlock(pos, bedrockState, Block.UPDATE_CLIENTS);
            }
        }

        for (int i = 0; i < 4; i++) {
            pos.set(x0 + dx[i], floorY, z0 + dz[i]);
            if (!level.getBlockState(pos).is(Blocks.BEDROCK)) {
                level.setBlock(pos, ventState, Block.UPDATE_CLIENTS);
            }
        }

        int maxSurface = Math.max(Math.max(surfaceY[0], surfaceY[1]), Math.max(surfaceY[2], surfaceY[3]));
        for (int y = floorY + 1; y <= maxSurface; y++) {
            for (int i = 0; i < 4; i++) {
                if (y > surfaceY[i]) continue;
                pos.set(x0 + dx[i], y, z0 + dz[i]);
                BlockState existing = level.getBlockState(pos);
                if (existing.is(Blocks.BEDROCK)) continue;
                level.setBlock(pos, wallState, Block.UPDATE_CLIENTS);
            }
        }

        return true;
    }
}
