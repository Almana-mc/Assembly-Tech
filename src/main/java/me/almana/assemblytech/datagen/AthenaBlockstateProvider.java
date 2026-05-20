package me.almana.assemblytech.datagen;

import com.google.gson.JsonObject;
import me.almana.assemblytech.registry.ModBlocks;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class AthenaBlockstateProvider implements DataProvider {

    private final PackOutput output;

    public AthenaBlockstateProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (int tier = 1; tier <= ModBlocks.MINER_TIERS; tier++) {
            futures.add(generateFrame(cache, tier));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> generateFrame(CachedOutput cache, int tier) {
        String prefix = "assemblytech:block/tier_" + tier + "/structure_frame_" + tier;

        JsonObject ctmTextures = new JsonObject();
        ctmTextures.addProperty("particle", prefix + "_base");
        ctmTextures.addProperty("center", prefix + "_corner");
        ctmTextures.addProperty("empty", prefix + "_middle");
        ctmTextures.addProperty("horizontal", prefix + "_horizontal");
        ctmTextures.addProperty("vertical", prefix + "_vertical");

        JsonObject root = new JsonObject();
        root.addProperty("athena:loader", "athena:ctm");
        root.add("ctm_textures", ctmTextures);

        Path path = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve("assemblytech/athena/structure_frame_" + tier + ".json");

        return DataProvider.saveStable(cache, root, path);
    }

    @Override
    public String getName() {
        return "AssemblyTech Athena CTM Configs";
    }
}
