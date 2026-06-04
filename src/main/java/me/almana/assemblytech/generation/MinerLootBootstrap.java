package me.almana.assemblytech.generation;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.registry.ModItems;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;

public final class MinerLootBootstrap {

    private MinerLootBootstrap() {}

    public static void bootstrap(BootstrapContext<MinerLootTable> ctx) {
        ctx.register(MinerLootRegistries.VOID_MINER_ORE, MinerLootBuilder
                .create(Identifier.fromNamespaceAndPath(Assemblytech.MODID, "void_miner/tier1_ores"))
                .addTag("c:ores/coal", 90)
                .addTag("c:ores/iron", 80)
                .addTag("c:ores/copper", 75)
                .addTag("c:ores/aluminum", 70)
                .addTag("c:ores/tin", 70)
                .addTag("c:gems/quartz", 60)
                .addTag("c:gems/apatite", 55)
                .addTag("c:ores/sulfur", 35)
                .addTag("c:gems/niter", 35)
                .addTag("c:gems/salt", 30)
                .addTagTiered("c:ores/lead", 60, 2)
                .addTagTiered("c:ores/nickel", 60, 2)
                .addTagTiered("c:ores/silver", 60, 2)
                .addTagTiered("c:ores/zinc", 60, 2)
                .addTagTiered("c:ores/redstone", 55, 2)
                .addTagTiered("c:gems/fluorite", 55, 2)
                .addTagTiered("c:ores/osmium", 50, 2)
                .addTagTiered("c:gems/lapis", 45, 2)
                .addTagTiered("c:gems/cinnabar", 45, 2)
                .addTagTiered("c:ores/gold", 40, 2)
                .addTagTiered("c:ores/uranium", 35, 3)
                .addTagTiered("c:ores/uraninite", 35, 3)
                .addTagTiered("c:ores/bauxite", 35, 3)
                .addTagTiered("c:ores/gallium", 30, 3)
                .addTagTiered("c:gems/ruby", 25, 3)
                .addTagTiered("c:gems/sapphire", 25, 3)
                .addTagTiered("c:gems/peridot", 25, 3)
                .addTagTiered("c:gems/amber", 18, 3)
                .addTagTiered("c:ores/cobalt", 18, 3)
                .addTagTiered("c:ores/iridium", 24, 4)
                .addTagTiered("c:ores/antimony", 24, 4)
                .addTagTiered("c:gems/diamond", 20, 4)
                .addTagTiered("c:gems/emerald", 18, 4)
                .addTagTiered("c:ores/platinum", 18, 4)
                .addTagTiered("c:ores/monazite", 18, 4)
                .addTagTiered("c:ores/titanium", 18, 4)
                .addTagTiered("c:ores/tungsten", 14, 5)
                .addTagTiered("c:ores/ardite", 14, 5)
                .addTagTiered("c:gems/onyx", 12, 5)
                .addTagTiered("c:gems/obsidian", 12, 5)
                .addTagTiered("c:gems/dimensional_shard", 10, 5)
                .addTagTiered("c:gems/resonating_ore", 10, 5)
                .addTagTiered("c:gems/necroticarite", 8, 5)
                .addTiered(ModItems.ARCANITE_CRYSTAL.get(), 10, 1)
                .build());
    }
}
