package me.almana.assemblytech.datagen;

import me.almana.assemblytech.Assemblytech;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Recipe;
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
        emit(output, ModItems.TARGET_DESIGNATOR.get(), "target_designator", baseEntries());
        emit(output, ModItems.RESOURCE_DESIGNATOR.get(), "resource_designator", resourceEntries());
    }

    private static void emit(RecipeOutput output, ItemLike designatorItem, String path, List<VoidMiningEntry> entries) {
        var designator = BuiltInRegistries.ITEM.wrapAsHolder(designatorItem.asItem());
        ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE,
                Identifier.fromNamespaceAndPath(Assemblytech.MODID, "void_mining/" + path));
        output.accept(key, new VoidMiningRecipe(designator, entries), null);
    }

    // base designator: all basic ores; weights are the balance knob
    private static List<VoidMiningEntry> baseEntries() {
        List<VoidMiningEntry> entries = new ArrayList<>();
        addTag(entries, "c:ores/coal", 90);
        addTag(entries, "c:ores/iron", 80);
        addTag(entries, "c:ores/copper", 75);
        addTag(entries, "c:ores/aluminum", 70);
        addTag(entries, "c:ores/tin", 70);
        addTag(entries, "c:gems/quartz", 60);
        addTag(entries, "c:gems/apatite", 55);
        addTag(entries, "c:ores/sulfur", 35);
        addTag(entries, "c:gems/niter", 35);
        addTag(entries, "c:gems/salt", 30);
        addTag(entries, "c:ores/lead", 60);
        addTag(entries, "c:ores/nickel", 60);
        addTag(entries, "c:ores/silver", 60);
        addTag(entries, "c:ores/zinc", 60);
        addTag(entries, "c:ores/redstone", 55);
        addTag(entries, "c:gems/fluorite", 55);
        addTag(entries, "c:ores/osmium", 50);
        addTag(entries, "c:gems/lapis", 45);
        addTag(entries, "c:gems/cinnabar", 45);
        addTag(entries, "c:ores/gold", 40);
        addTag(entries, "c:ores/uranium", 35);
        addTag(entries, "c:ores/uraninite", 35);
        addTag(entries, "c:ores/bauxite", 35);
        addTag(entries, "c:ores/gallium", 30);
        addTag(entries, "c:gems/ruby", 25);
        addTag(entries, "c:gems/sapphire", 25);
        addTag(entries, "c:gems/peridot", 25);
        addTag(entries, "c:gems/amber", 18);
        addTag(entries, "c:ores/cobalt", 18);
        addTag(entries, "c:ores/iridium", 24);
        addTag(entries, "c:ores/antimony", 24);
        addTag(entries, "c:gems/diamond", 20);
        addTag(entries, "c:gems/emerald", 18);
        addTag(entries, "c:ores/platinum", 18);
        addTag(entries, "c:ores/monazite", 18);
        addTag(entries, "c:ores/titanium", 18);
        addTag(entries, "c:ores/tungsten", 14);
        addTag(entries, "c:ores/ardite", 14);
        addTag(entries, "c:gems/onyx", 12);
        addTag(entries, "c:gems/obsidian", 12);
        addTag(entries, "c:gems/dimensional_shard", 10);
        addTag(entries, "c:gems/resonating_ore", 10);
        addTag(entries, "c:gems/necroticarite", 8);
        addItem(entries, ModItems.ARCANITE_CRYSTAL.get(), 10);
        return entries;
    }

    // resource designator: bulk stone/earth blocks
    private static List<VoidMiningEntry> resourceEntries() {
        List<VoidMiningEntry> entries = new ArrayList<>();
        addId(entries, "minecraft:cobblestone", 100);
        addId(entries, "minecraft:stone", 95);
        addId(entries, "minecraft:dirt", 90);
        addId(entries, "minecraft:gravel", 85);
        addId(entries, "minecraft:sand", 80);
        addId(entries, "minecraft:cobbled_deepslate", 70);
        addId(entries, "minecraft:deepslate", 65);
        addId(entries, "minecraft:andesite", 55);
        addId(entries, "minecraft:diorite", 55);
        addId(entries, "minecraft:granite", 55);
        addId(entries, "minecraft:tuff", 50);
        addId(entries, "minecraft:netherrack", 45);
        addId(entries, "minecraft:red_sand", 40);
        addId(entries, "minecraft:sandstone", 40);
        addId(entries, "minecraft:clay", 35);
        addId(entries, "minecraft:calcite", 30);
        addId(entries, "minecraft:basalt", 28);
        addId(entries, "minecraft:blackstone", 26);
        addId(entries, "minecraft:dripstone_block", 24);
        addId(entries, "minecraft:ice", 22);
        addId(entries, "minecraft:packed_ice", 14);
        addId(entries, "minecraft:obsidian", 8);
        return entries;
    }

    private static void addItem(List<VoidMiningEntry> entries, ItemLike item, int weight) {
        entries.add(VoidMiningEntry.item(BuiltInRegistries.ITEM.wrapAsHolder(item.asItem()), weight, 1, 1));
    }

    private static void addId(List<VoidMiningEntry> entries, String id, int weight) {
        var item = BuiltInRegistries.ITEM.getValue(Identifier.parse(id));
        entries.add(VoidMiningEntry.item(BuiltInRegistries.ITEM.wrapAsHolder(item), weight, 1, 1));
    }

    private static void addTag(List<VoidMiningEntry> entries, String tag, int weight) {
        entries.add(VoidMiningEntry.tag(TagKey.create(Registries.ITEM, Identifier.parse(tag)), weight, 1, 1));
    }
}
