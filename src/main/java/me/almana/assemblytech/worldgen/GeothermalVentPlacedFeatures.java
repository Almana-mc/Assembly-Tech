package me.almana.assemblytech.worldgen;

import me.almana.assemblytech.Assemblytech;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

public final class GeothermalVentPlacedFeatures {

    private GeothermalVentPlacedFeatures() {}

    public static final ResourceKey<PlacedFeature> ROUGH_GEOTHERMAL_VENT = key("rough_geothermal_vent");
    public static final ResourceKey<PlacedFeature> GEOTHERMAL_VENT = key("geothermal_vent");
    public static final ResourceKey<PlacedFeature> PRISTINE_GEOTHERMAL_VENT = key("pristine_geothermal_vent");

    private static ResourceKey<PlacedFeature> key(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                Identifier.fromNamespaceAndPath(Assemblytech.MODID, name));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> ctx) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = ctx.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> rough = configured.getOrThrow(GeothermalVentConfiguredFeatures.ROUGH_GEOTHERMAL_VENT);
        Holder<ConfiguredFeature<?, ?>> normal = configured.getOrThrow(GeothermalVentConfiguredFeatures.GEOTHERMAL_VENT);
        Holder<ConfiguredFeature<?, ?>> pristine = configured.getOrThrow(GeothermalVentConfiguredFeatures.PRISTINE_GEOTHERMAL_VENT);

        ctx.register(ROUGH_GEOTHERMAL_VENT, new PlacedFeature(rough, modifiers(80)));
        ctx.register(GEOTHERMAL_VENT, new PlacedFeature(normal, modifiers(160)));
        ctx.register(PRISTINE_GEOTHERMAL_VENT, new PlacedFeature(pristine, modifiers(300)));
    }

    private static List<PlacementModifier> modifiers(int rarity) {
        return List.of(
                RarityFilter.onAverageOnceEvery(rarity),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
        );
    }
}
