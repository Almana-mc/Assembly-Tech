package me.almana.assemblytech.generation;

import net.minecraft.world.item.Item;

public record LootRollSnapshot(
        Item[] items,
        int[] cumulativeWeights,
        int[] mins,
        int[] maxs,
        int totalWeight,
        int picks,
        long seed
) {
    public boolean isEmpty() {
        return items.length == 0 || totalWeight <= 0 || picks <= 0;
    }
}
