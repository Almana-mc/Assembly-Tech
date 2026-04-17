package me.almana.assemblytech.generation;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public final class MinerLootBuilder {

    private final Identifier family;
    private final List<WeightedItem> entries = new ArrayList<>();

    private MinerLootBuilder(Identifier family) {
        this.family = family;
    }

    public static MinerLootBuilder create(Identifier family) {
        return new MinerLootBuilder(family);
    }

    public MinerLootBuilder add(ItemLike item, int weight) {
        return addFull(item, weight, 1, 1, 1);
    }

    public MinerLootBuilder addRange(ItemLike item, int weight, int min, int max) {
        return addFull(item, weight, min, max, 1);
    }

    public MinerLootBuilder addTiered(ItemLike item, int weight, int minTier) {
        return addFull(item, weight, 1, 1, minTier);
    }

    public MinerLootBuilder addFull(ItemLike item, int weight, int min, int max, int minTier) {
        if (min > max) throw new IllegalArgumentException("min > max for " + item);
        entries.add(new WeightedItem(BuiltInRegistries.ITEM.wrapAsHolder(item.asItem()), weight, min, max, minTier));
        return this;
    }

    public MinerLootTable build() {
        return new MinerLootTable(family, List.copyOf(entries));
    }
}
