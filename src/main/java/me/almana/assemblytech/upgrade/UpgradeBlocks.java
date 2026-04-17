package me.almana.assemblytech.upgrade;

import me.almana.assemblytech.multiblock.modifier.ModifierAttribute;
import me.almana.assemblytech.multiblock.modifier.ModifierData;
import me.almana.assemblytech.registry.ModBlocks;
import net.minecraft.world.level.block.Block;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class UpgradeBlocks {

    private UpgradeBlocks() {}

    private static Map<Block, List<ModifierAttribute>> cache;

    public static Block[] all() {
        return new Block[]{
                ModBlocks.UPGRADE_SPEED.get(),
                ModBlocks.UPGRADE_EFFICIENCY.get(),
                ModBlocks.UPGRADE_FORTUNE_1.get(),
                ModBlocks.UPGRADE_FORTUNE_2.get(),
                ModBlocks.UPGRADE_FORTUNE_3.get(),
                ModBlocks.UPGRADE_PARALLEL_1.get(),
                ModBlocks.UPGRADE_PARALLEL_2.get(),
                ModBlocks.UPGRADE_PARALLEL_3.get()
        };
    }

    public static List<ModifierAttribute> getAttributes(Block block) {
        if (cache == null) {
            Map<Block, List<ModifierAttribute>> m = new IdentityHashMap<>();
            m.put(ModBlocks.UPGRADE_SPEED.get(), List.of(new ModifierAttribute(ModifierData.SPEED, 1.0)));
            m.put(ModBlocks.UPGRADE_EFFICIENCY.get(), List.of(new ModifierAttribute(ModifierData.EFFICIENCY, 1.0)));
            m.put(ModBlocks.UPGRADE_FORTUNE_1.get(), List.of(new ModifierAttribute(ModifierData.FORTUNE, 1.0)));
            m.put(ModBlocks.UPGRADE_FORTUNE_2.get(), List.of(new ModifierAttribute(ModifierData.FORTUNE, 2.0)));
            m.put(ModBlocks.UPGRADE_FORTUNE_3.get(), List.of(new ModifierAttribute(ModifierData.FORTUNE, 3.0)));
            m.put(ModBlocks.UPGRADE_PARALLEL_1.get(), List.of(new ModifierAttribute(ModifierData.PARALLEL_BONUS, 1.0)));
            m.put(ModBlocks.UPGRADE_PARALLEL_2.get(), List.of(new ModifierAttribute(ModifierData.PARALLEL_BONUS, 3.0)));
            m.put(ModBlocks.UPGRADE_PARALLEL_3.get(), List.of(new ModifierAttribute(ModifierData.PARALLEL_BONUS, 5.0)));
            cache = m;
        }
        return cache.getOrDefault(block, List.of());
    }
}
