package me.almana.assemblytech.worldgen;

import me.almana.assemblytech.Assemblytech;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class GeothermalVentBiomeModifiers {

    private GeothermalVentBiomeModifiers() {}

    public static final ResourceKey<BiomeModifier> ROUGH_GEOTHERMAL_VENT = key("rough_geothermal_vent");
    public static final ResourceKey<BiomeModifier> GEOTHERMAL_VENT = key("geothermal_vent");
    public static final ResourceKey<BiomeModifier> PRISTINE_GEOTHERMAL_VENT = key("pristine_geothermal_vent");

    private static ResourceKey<BiomeModifier> key(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                Identifier.fromNamespaceAndPath(Assemblytech.MODID, name));
    }

    public static void bootstrap(BootstrapContext<BiomeModifier> ctx) {
        HolderGetter<Biome> biomes = ctx.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placed = ctx.lookup(Registries.PLACED_FEATURE);

        HolderSet<Biome> overworld = biomes.getOrThrow(BiomeTags.IS_OVERWORLD);

        ctx.register(ROUGH_GEOTHERMAL_VENT, new BiomeModifiers.AddFeaturesBiomeModifier(
                overworld,
                HolderSet.direct(placed.getOrThrow(GeothermalVentPlacedFeatures.ROUGH_GEOTHERMAL_VENT)),
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES));

        ctx.register(GEOTHERMAL_VENT, new BiomeModifiers.AddFeaturesBiomeModifier(
                overworld,
                HolderSet.direct(placed.getOrThrow(GeothermalVentPlacedFeatures.GEOTHERMAL_VENT)),
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES));

        ctx.register(PRISTINE_GEOTHERMAL_VENT, new BiomeModifiers.AddFeaturesBiomeModifier(
                overworld,
                HolderSet.direct(placed.getOrThrow(GeothermalVentPlacedFeatures.PRISTINE_GEOTHERMAL_VENT)),
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES));
    }
}
