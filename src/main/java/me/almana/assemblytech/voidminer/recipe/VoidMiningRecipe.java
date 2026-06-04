package me.almana.assemblytech.voidminer.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.registry.ModItems;
import me.almana.assemblytech.registry.ModRecipes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public record VoidMiningRecipe(int tier, List<VoidMiningEntry> entries) implements Recipe<VoidMiningRecipe.Input> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "void_mining");
    public static final MapCodec<VoidMiningRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            com.mojang.serialization.Codec.intRange(1, 64).fieldOf("tier").forGetter(VoidMiningRecipe::tier),
            VoidMiningEntry.CODEC.listOf().fieldOf("entries").forGetter(VoidMiningRecipe::entries)
    ).apply(inst, VoidMiningRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, VoidMiningRecipe> STREAM_CODEC = StreamCodec.of(
            VoidMiningRecipe::write,
            VoidMiningRecipe::read
    );

    public static Identifier recipeId(int tier) {
        return Identifier.fromNamespaceAndPath(Assemblytech.MODID, "void_mining/tier_" + tier);
    }

    public static ResourceKey<Recipe<?>> recipeKey(int tier) {
        return ResourceKey.create(Registries.RECIPE, recipeId(tier));
    }

    @Override
    public boolean matches(Input input, Level level) {
        return input.tier() == tier;
    }

    @Override
    public ItemStack assemble(Input input) {
        return ModItems.crystal(Math.max(1, Math.min(7, tier))).get().getDefaultInstance();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "void_mining";
    }

    @Override
    public RecipeSerializer<? extends Recipe<Input>> getSerializer() {
        return ModRecipes.VOID_MINING_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<Input>> getType() {
        return ModRecipes.VOID_MINING.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    private static VoidMiningRecipe read(RegistryFriendlyByteBuf buf) {
        int tier = buf.readVarInt();
        List<VoidMiningEntry> entries = buf.readList(VoidMiningEntry::read);
        return new VoidMiningRecipe(tier, entries);
    }

    private static void write(RegistryFriendlyByteBuf buf, VoidMiningRecipe recipe) {
        buf.writeVarInt(recipe.tier());
        buf.writeCollection(recipe.entries(), (entryBuf, entry) -> entry.write(entryBuf));
    }

    public record Input(int tier) implements RecipeInput {

        @Override
        public ItemStack getItem(int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public int size() {
            return 0;
        }
    }
}
