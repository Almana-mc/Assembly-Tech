package me.almana.assemblytech.generation;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.registry.ModItems;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

public final class MinerLootBootstrap {

    private MinerLootBootstrap() {}

    public static void bootstrap(BootstrapContext<MinerLootTable> ctx) {
        ctx.register(MinerLootRegistries.VOID_MINER_ORE, MinerLootBuilder
                .create(Identifier.fromNamespaceAndPath(Assemblytech.MODID, "void_miner/tier1_ores"))
                .add(Items.COAL, 40)
                .add(Items.IRON_ORE, 25)
                .add(Items.DIAMOND, 5)
                .addTiered(ModItems.TIER_1_CRYSTAL.get(), 10, 1)
                .build());
    }
}
