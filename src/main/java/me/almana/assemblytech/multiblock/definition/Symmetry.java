package me.almana.assemblytech.multiblock.definition;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class Symmetry {

    private Symmetry() {}

    public static List<BlockPos> expandQuad(int x, int y, int z) {
        List<BlockPos> positions = new ArrayList<>(4);
        positions.add(new BlockPos(x, y, z));

        if (x != 0) {
            positions.add(new BlockPos(-x, y, z));
        }
        if (z != 0) {
            positions.add(new BlockPos(x, y, -z));
        }
        if (x != 0 && z != 0) {
            positions.add(new BlockPos(-x, y, -z));
        }

        return positions;
    }
}
