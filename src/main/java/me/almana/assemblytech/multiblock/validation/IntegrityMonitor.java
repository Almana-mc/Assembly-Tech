package me.almana.assemblytech.multiblock.validation;

import me.almana.assemblytech.multiblock.controller.MultiblockControllerEntity;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class IntegrityMonitor {

    private static final Map<ChunkPos, List<WeakReference<MultiblockControllerEntity>>> ACTIVE = new ConcurrentHashMap<>();

    private IntegrityMonitor() {}

    public static void register(MultiblockControllerEntity controller, AABB worldBounds) {
        int minCX = SectionPos.blockToSectionCoord((int) worldBounds.minX);
        int minCZ = SectionPos.blockToSectionCoord((int) worldBounds.minZ);
        int maxCX = SectionPos.blockToSectionCoord((int) worldBounds.maxX);
        int maxCZ = SectionPos.blockToSectionCoord((int) worldBounds.maxZ);

        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cz = minCZ; cz <= maxCZ; cz++) {
                ChunkPos cp = new ChunkPos(cx, cz);
                ACTIVE.computeIfAbsent(cp, k -> new ArrayList<>()).add(new WeakReference<>(controller));
            }
        }
    }

    public static void unregister(MultiblockControllerEntity controller) {
        for (List<WeakReference<MultiblockControllerEntity>> refs : ACTIVE.values()) {
            refs.removeIf(ref -> {
                MultiblockControllerEntity c = ref.get();
                return c == null || c == controller;
            });
        }
        ACTIVE.values().removeIf(List::isEmpty);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        checkPosition(event.getPos());
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        checkPosition(event.getPos());
    }

    private static void checkPosition(net.minecraft.core.BlockPos pos) {
        int cx = SectionPos.blockToSectionCoord(pos.getX());
        int cz = SectionPos.blockToSectionCoord(pos.getZ());

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                ChunkPos cp = new ChunkPos(cx + dx, cz + dz);
                List<WeakReference<MultiblockControllerEntity>> refs = ACTIVE.get(cp);
                if (refs == null) continue;

                Iterator<WeakReference<MultiblockControllerEntity>> it = refs.iterator();
                while (it.hasNext()) {
                    MultiblockControllerEntity ctrl = it.next().get();
                    if (ctrl == null) {
                        it.remove();
                        continue;
                    }
                    if (ctrl.getWorldBounds() != null && ctrl.getWorldBounds().contains(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)) {
                        ctrl.flagRevalidation();
                    }
                }
            }
        }
    }
}
