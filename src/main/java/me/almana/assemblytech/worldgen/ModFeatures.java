package me.almana.assemblytech.worldgen;

import me.almana.assemblytech.Assemblytech;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModFeatures {

    private ModFeatures() {}

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Assemblytech.MODID);

    public static final DeferredHolder<Feature<?>, GeothermalVentFeature> ROUGH_GEOTHERMAL_VENT =
            FEATURES.register("rough_geothermal_vent", () -> new GeothermalVentFeature(1, NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, GeothermalVentFeature> GEOTHERMAL_VENT =
            FEATURES.register("geothermal_vent", () -> new GeothermalVentFeature(2, NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, GeothermalVentFeature> PRISTINE_GEOTHERMAL_VENT =
            FEATURES.register("pristine_geothermal_vent", () -> new GeothermalVentFeature(3, NoneFeatureConfiguration.CODEC));

    public static void init() {}
}
