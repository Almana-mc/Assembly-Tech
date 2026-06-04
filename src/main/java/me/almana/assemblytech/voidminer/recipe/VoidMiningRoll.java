package me.almana.assemblytech.voidminer.recipe;

import me.almana.assemblytech.generation.LootRollSnapshot;
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

public final class VoidMiningRoll {

    private VoidMiningRoll() {}

    public static LootRollSnapshot prepareSnapshot(List<VoidMiningEntry> entries, @Nullable ModifierData modifierData, long seed) {
        int extraDrops = modifierData != null ? modifierData.getExtraDrops() : 0;
        int parallelBonus = modifierData != null ? modifierData.getParallelBonus() : 0;
        int picks = 1 + extraDrops + parallelBonus;

        List<VoidMiningEntry> resolvedEntries = new ArrayList<>();
        List<Item[]> resolvedItems = new ArrayList<>();
        for (VoidMiningEntry e : entries) {
            List<Item> resolved = e.resolveItems();
            if (resolved.isEmpty()) continue;

            resolvedEntries.add(e);
            resolvedItems.add(resolved.toArray(Item[]::new));
        }

        int fortune = modifierData != null ? modifierData.getFortuneLevel() : 0;
        Set<VoidMiningEntry> boosted = Set.of();
        if (fortune > 0 && !resolvedEntries.isEmpty()) {
            List<VoidMiningEntry> sorted = new ArrayList<>(resolvedEntries);
            sorted.sort(Comparator.comparingInt(VoidMiningEntry::weight));
            int boostCount = Math.max(1, sorted.size() / 10);
            boosted = new HashSet<>(sorted.subList(0, boostCount));
        }

        int n = resolvedEntries.size();
        Item[][] items = resolvedItems.toArray(Item[][]::new);
        int[] cumulative = new int[n];
        int[] mins = new int[n];
        int[] maxs = new int[n];
        int cursor = 0;
        for (int i = 0; i < n; i++) {
            VoidMiningEntry e = resolvedEntries.get(i);
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
        Item[][] items = snap.items();
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
                    Item[] options = items[i];
                    out.add(new ItemStack(options[random.nextInt(options.length)], count));
                    break;
                }
            }
        }
        return out;
    }
}
