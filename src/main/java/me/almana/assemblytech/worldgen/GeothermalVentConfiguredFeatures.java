package me.almana.assemblytech.worldgen;

import me.almana.assemblytech.Assemblytech;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class GeothermalVentConfiguredFeatures {

    private GeothermalVentConfiguredFeatures() {}

    public static final ResourceKey<ConfiguredFeature<?, ?>> ROUGH_GEOTHERMAL_VENT = key("rough_geothermal_vent");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GEOTHERMAL_VENT = key("geothermal_vent");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PRISTINE_GEOTHERMAL_VENT = key("pristine_geothermal_vent");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                Identifier.fromNamespaceAndPath(Assemblytech.MODID, name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
        ctx.register(ROUGH_GEOTHERMAL_VENT,
                new ConfiguredFeature<>(ModFeatures.ROUGH_GEOTHERMAL_VENT.get(), NoneFeatureConfiguration.INSTANCE));
        ctx.register(GEOTHERMAL_VENT,
                new ConfiguredFeature<>(ModFeatures.GEOTHERMAL_VENT.get(), NoneFeatureConfiguration.INSTANCE));
        ctx.register(PRISTINE_GEOTHERMAL_VENT,
                new ConfiguredFeature<>(ModFeatures.PRISTINE_GEOTHERMAL_VENT.get(), NoneFeatureConfiguration.INSTANCE));
    }
}
