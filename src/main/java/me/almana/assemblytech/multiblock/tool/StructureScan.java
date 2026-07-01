package me.almana.assemblytech.multiblock.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import me.almana.assemblytech.multiblock.controller.MultiblockControllerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class StructureScan {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private StructureScan() {}

    public static Path export(ServerLevel level, BlockPos a, BlockPos b) throws IOException {
        // inner region, marker corners excluded
        int minX = Math.min(a.getX(), b.getX()) + 1;
        int minY = Math.min(a.getY(), b.getY()) + 1;
        int minZ = Math.min(a.getZ(), b.getZ()) + 1;
        int maxX = Math.max(a.getX(), b.getX()) - 1;
        int maxY = Math.max(a.getY(), b.getY()) - 1;
        int maxZ = Math.max(a.getZ(), b.getZ()) - 1;

        BlockPos controller = findController(level, minX, minY, minZ, maxX, maxY, maxZ);
        BlockPos origin = controller != null ? controller : new BlockPos(minX, minY, minZ);

        JsonArray blocks = new JsonArray();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    cursor.set(x, y, z);
                    BlockState state = level.getBlockState(cursor);
                    if (state.isAir()) continue;

                    JsonArray pos = new JsonArray();
                    pos.add(x - origin.getX());
                    pos.add(y - origin.getY());
                    pos.add(z - origin.getZ());

                    JsonObject entry = new JsonObject();
                    entry.add("pos", pos);
                    entry.add("state", BlockState.CODEC.encodeStart(JsonOps.INSTANCE, state).getOrThrow());
                    blocks.add(entry);
                }
            }
        }

        JsonArray size = new JsonArray();
        size.add(maxX - minX + 1);
        size.add(maxY - minY + 1);
        size.add(maxZ - minZ + 1);

        JsonObject root = new JsonObject();
        root.add("size", size);
        root.addProperty("origin", controller != null ? "controller" : "min_corner");
        root.add("blocks", blocks);

        Path dir = FMLPaths.GAMEDIR.get().resolve("assemblytech_structures");
        Files.createDirectories(dir);
        Path file = dir.resolve("structure_" + System.currentTimeMillis() + ".json");
        Files.writeString(file, GSON.toJson(root));
        return file;
    }

    private static BlockPos findController(ServerLevel level, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    cursor.set(x, y, z);
                    if (level.getBlockEntity(cursor) instanceof MultiblockControllerEntity) {
                        return cursor.immutable();
                    }
                }
            }
        }
        return null;
    }
}
