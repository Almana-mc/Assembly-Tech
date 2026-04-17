package me.almana.assemblytech.generation;

import me.almana.assemblytech.multiblock.modifier.ModifierData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MinerLootRoll {

    private MinerLootRoll() {}

    public static List<ItemStack> roll(MinerLootTable table, int tier, @Nullable ModifierData modifierData, RandomSource random) {
        LootRollSnapshot snap = prepareSnapshot(table, tier, modifierData, random.nextLong());
        return rollSnapshot(snap);
    }

    public static LootRollSnapshot prepareSnapshot(MinerLootTable table, int tier, @Nullable ModifierData modifierData, long seed) {
        List<WeightedItem> eligible = new ArrayList<>();
        for (WeightedItem entry : table.entries()) {
            if (entry.minTier() <= tier) {
                eligible.add(entry);
            }
        }

        int fortune = modifierData != null ? modifierData.getFortuneLevel() : 0;
        Set<WeightedItem> boosted = Set.of();
        if (fortune > 0 && !eligible.isEmpty()) {
            List<WeightedItem> sorted = new ArrayList<>(eligible);
            sorted.sort(Comparator.comparingInt(WeightedItem::weight));
            int boostCount = Math.max(1, sorted.size() / 10);
            boosted = new HashSet<>(sorted.subList(0, boostCount));
        }

        int extraDrops = modifierData != null ? modifierData.getExtraDrops() : 0;
        int parallelBonus = modifierData != null ? modifierData.getParallelBonus() : 0;
        int picks = 1 + extraDrops + parallelBonus;

        int n = eligible.size();
        Item[] items = new Item[n];
        int[] cumulative = new int[n];
        int[] mins = new int[n];
        int[] maxs = new int[n];
        int cursor = 0;
        for (int i = 0; i < n; i++) {
            WeightedItem e = eligible.get(i);
            items[i] = e.item().value();
            int w = e.weight();
            if (boosted.contains(e)) w += fortune;
            cursor += w;
            cumulative[i] = cursor;
            mins[i] = e.min();
            maxs[i] = e.max();
        }

        return new LootRollSnapshot(items, cumulative, mins, maxs, cursor, picks, seed);
    }

    public static List<ItemStack> rollSnapshot(LootRollSnapshot snap) {
        if (snap.isEmpty()) return List.of();

        RandomSource random = RandomSource.create(snap.seed());
        Item[] items = snap.items();
        int[] cumulative = snap.cumulativeWeights();
        int[] mins = snap.mins();
        int[] maxs = snap.maxs();
        int totalWeight = snap.totalWeight();
        int picks = snap.picks();

        List<ItemStack> out = new ArrayList<>(picks);
        for (int p = 0; p < picks; p++) {
            int roll = random.nextInt(totalWeight);
            for (int i = 0; i < cumulative.length; i++) {
                if (roll < cumulative[i]) {
                    int range = maxs[i] - mins[i] + 1;
                    int count = mins[i] + (range > 1 ? random.nextInt(range) : 0);
                    out.add(new ItemStack(items[i], count));
                    break;
                }
            }
        }
        return out;
    }
}
