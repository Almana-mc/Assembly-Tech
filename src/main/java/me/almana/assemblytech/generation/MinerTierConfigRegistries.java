package me.almana.assemblytech.generation;

import me.almana.assemblytech.Assemblytech;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public final class MinerTierConfigRegistries {

    private MinerTierConfigRegistries() {}

    public static final ResourceKey<Registry<MinerTierConfig>> MINER_TIER_CONFIG =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Assemblytech.MODID, "miner_tier_config"));

    public static ResourceKey<MinerTierConfig> oreMinerKey(int tier) {
        return ResourceKey.create(
                MINER_TIER_CONFIG,
                Identifier.fromNamespaceAndPath(Assemblytech.MODID, "ore_miner/tier_" + tier)
        );
    }

    public static void register(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(MINER_TIER_CONFIG, MinerTierConfig.CODEC, MinerTierConfig.CODEC);
    }
}
