package me.almana.assemblytech.datagen;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.generation.MinerLootBootstrap;
import me.almana.assemblytech.generation.MinerLootRegistries;
import me.almana.assemblytech.generation.MinerTierConfigBootstrap;
import me.almana.assemblytech.generation.MinerTierConfigRegistries;
import net.minecraft.core.RegistrySetBuilder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Assemblytech.MODID)
public final class DataGenerators {

    private DataGenerators() {}

    @SubscribeEvent
    public static void onGatherDataServer(GatherDataEvent.Server event) {
        register(event);
    }

    @SubscribeEvent
    public static void onGatherDataClient(GatherDataEvent.Client event) {
        register(event);
        event.createProvider(AssemblytechModelProvider::new);
    }

    private static void register(GatherDataEvent event) {
        RegistrySetBuilder builder = new RegistrySetBuilder()
                .add(MinerLootRegistries.MINER_LOOT, MinerLootBootstrap::bootstrap)
                .add(MinerTierConfigRegistries.MINER_TIER_CONFIG, MinerTierConfigBootstrap::bootstrap);
        event.createDatapackRegistryObjects(builder);
    }
}
