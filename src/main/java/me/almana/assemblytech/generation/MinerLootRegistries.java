package me.almana.assemblytech.generation;

import me.almana.assemblytech.Assemblytech;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public final class MinerLootRegistries {

    private MinerLootRegistries() {}

    public static final ResourceKey<Registry<MinerLootTable>> MINER_LOOT =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Assemblytech.MODID, "miner_loot"));

    public static final ResourceKey<MinerLootTable> VOID_MINER_ORE =
            ResourceKey.create(MINER_LOOT, Identifier.fromNamespaceAndPath(Assemblytech.MODID, "void_miner/tier1_ores"));

    public static void register(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(MINER_LOOT, MinerLootTable.CODEC, MinerLootTable.CODEC);
    }
}
