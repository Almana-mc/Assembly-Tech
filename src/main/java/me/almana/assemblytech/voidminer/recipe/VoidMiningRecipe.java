package me.almana.assemblytech.voidminer.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.registry.ModRecipes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
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

public record VoidMiningRecipe(Holder<Item> designator, List<VoidMiningEntry> entries) implements Recipe<VoidMiningRecipe.Input> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "void_mining");
    public static final MapCodec<VoidMiningRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("designator").forGetter(VoidMiningRecipe::designator),
            VoidMiningEntry.CODEC.listOf().fieldOf("entries").forGetter(VoidMiningRecipe::entries)
    ).apply(inst, VoidMiningRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, VoidMiningRecipe> STREAM_CODEC = StreamCodec.of(
            VoidMiningRecipe::write,
            VoidMiningRecipe::read
    );

    @Override
    public boolean matches(Input input, Level level) {
        return input.designator() == designator.value();
    }

    @Override
    public ItemStack assemble(Input input) {
        return ItemStack.EMPTY;
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
        Item designator = BuiltInRegistries.ITEM.getValue(buf.readIdentifier());
        List<VoidMiningEntry> entries = buf.readList(VoidMiningEntry::read);
        return new VoidMiningRecipe(BuiltInRegistries.ITEM.wrapAsHolder(designator), entries);
    }

    private static void write(RegistryFriendlyByteBuf buf, VoidMiningRecipe recipe) {
        buf.writeIdentifier(BuiltInRegistries.ITEM.getKey(recipe.designator().value()));
        buf.writeCollection(recipe.entries(), (entryBuf, entry) -> entry.write(entryBuf));
    }

    public record Input(Item designator) implements RecipeInput {

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
