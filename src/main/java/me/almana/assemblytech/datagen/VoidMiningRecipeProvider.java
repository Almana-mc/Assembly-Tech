package me.almana.assemblytech.datagen;

import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.registry.ModItems;
import me.almana.assemblytech.voidminer.recipe.VoidMiningEntry;
import me.almana.assemblytech.voidminer.recipe.VoidMiningRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class VoidMiningRecipeProvider extends RecipeProvider.Runner {

    public VoidMiningRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public String getName() {
        return "Void Mining Recipes";
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            protected void buildRecipes() {
                buildVoidMiningRecipes(output);
            }
        };
    }

    private static void buildVoidMiningRecipes(RecipeOutput output) {
        List<EntryDef> entries = entries();
        for (int tier = 1; tier <= ModBlocks.MINER_TIERS; tier++) {
            int currentTier = tier;
            List<VoidMiningEntry> tierEntries = entries.stream()
                    .filter(entry -> entry.minTier() <= currentTier)
                    .map(EntryDef::entry)
                    .toList();
            output.accept(VoidMiningRecipe.recipeKey(tier), new VoidMiningRecipe(tier, tierEntries), null);
        }
    }

    private static List<EntryDef> entries() {
        List<EntryDef> entries = new ArrayList<>();
        addTag(entries, "c:ores/coal", 90, 1);
        addTag(entries, "c:ores/iron", 80, 1);
        addTag(entries, "c:ores/copper", 75, 1);
        addTag(entries, "c:ores/aluminum", 70, 1);
        addTag(entries, "c:ores/tin", 70, 1);
        addTag(entries, "c:gems/quartz", 60, 1);
        addTag(entries, "c:gems/apatite", 55, 1);
        addTag(entries, "c:ores/sulfur", 35, 1);
        addTag(entries, "c:gems/niter", 35, 1);
        addTag(entries, "c:gems/salt", 30, 1);
        addTag(entries, "c:ores/lead", 60, 2);
        addTag(entries, "c:ores/nickel", 60, 2);
        addTag(entries, "c:ores/silver", 60, 2);
        addTag(entries, "c:ores/zinc", 60, 2);
        addTag(entries, "c:ores/redstone", 55, 2);
        addTag(entries, "c:gems/fluorite", 55, 2);
        addTag(entries, "c:ores/osmium", 50, 2);
        addTag(entries, "c:gems/lapis", 45, 2);
        addTag(entries, "c:gems/cinnabar", 45, 2);
        addTag(entries, "c:ores/gold", 40, 2);
        addTag(entries, "c:ores/uranium", 35, 3);
        addTag(entries, "c:ores/uraninite", 35, 3);
        addTag(entries, "c:ores/bauxite", 35, 3);
        addTag(entries, "c:ores/gallium", 30, 3);
        addTag(entries, "c:gems/ruby", 25, 3);
        addTag(entries, "c:gems/sapphire", 25, 3);
        addTag(entries, "c:gems/peridot", 25, 3);
        addTag(entries, "c:gems/amber", 18, 3);
        addTag(entries, "c:ores/cobalt", 18, 3);
        addTag(entries, "c:ores/iridium", 24, 4);
        addTag(entries, "c:ores/antimony", 24, 4);
        addTag(entries, "c:gems/diamond", 20, 4);
        addTag(entries, "c:gems/emerald", 18, 4);
        addTag(entries, "c:ores/platinum", 18, 4);
        addTag(entries, "c:ores/monazite", 18, 4);
        addTag(entries, "c:ores/titanium", 18, 4);
        addTag(entries, "c:ores/tungsten", 14, 5);
        addTag(entries, "c:ores/ardite", 14, 5);
        addTag(entries, "c:gems/onyx", 12, 5);
        addTag(entries, "c:gems/obsidian", 12, 5);
        addTag(entries, "c:gems/dimensional_shard", 10, 5);
        addTag(entries, "c:gems/resonating_ore", 10, 5);
        addTag(entries, "c:gems/necroticarite", 8, 5);
        addItem(entries, ModItems.ARCANITE_CRYSTAL.get(), 10, 1);
        return entries;
    }

    private static void addItem(List<EntryDef> entries, ItemLike item, int weight, int minTier) {
        entries.add(new EntryDef(VoidMiningEntry.item(BuiltInRegistries.ITEM.wrapAsHolder(item.asItem()), weight, 1, 1), minTier));
    }

    private static void addTag(List<EntryDef> entries, String tag, int weight, int minTier) {
        entries.add(new EntryDef(VoidMiningEntry.tag(TagKey.create(Registries.ITEM, Identifier.parse(tag)), weight, 1, 1), minTier));
    }

    private record EntryDef(VoidMiningEntry entry, int minTier) {}
}
