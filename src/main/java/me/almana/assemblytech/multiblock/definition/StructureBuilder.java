package me.almana.assemblytech.multiblock.definition;

import me.almana.assemblytech.multiblock.api.ComponentType;
import me.almana.assemblytech.multiblock.api.StructureComponent;
import me.almana.assemblytech.multiblock.api.StructureDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class StructureBuilder {

    private final int tier;
    private final Map<BlockPos, StructureComponent> components = new HashMap<>();
    private final EnumMap<ComponentType, Predicate<BlockState>> validators = new EnumMap<>(ComponentType.class);

    public StructureBuilder(int tier) {
        this.tier = tier;
    }

    public StructureBuilder withValidator(ComponentType type, Predicate<BlockState> validator) {
        validators.put(type, validator);
        return this;
    }

    public StructureBuilder add(ComponentType type, int x, int y, int z, Predicate<BlockState> validator) {
        BlockPos offset = new BlockPos(x, y, z);
        components.put(offset, new StructureComponent(offset, type, tier, validator));
        return this;
    }

    public StructureBuilder add(ComponentType type, int x, int y, int z) {
        return add(type, x, y, z, validatorFor(type));
    }

    public StructureBuilder addSymmetrical(ComponentType type, int x, int y, int z, Predicate<BlockState> validator) {
        for (BlockPos pos : Symmetry.expandQuad(x, y, z)) {
            components.put(pos, new StructureComponent(pos, type, tier, validator));
        }
        return this;
    }

    public StructureBuilder addSymmetrical(ComponentType type, int x, int y, int z) {
        return addSymmetrical(type, x, y, z, validatorFor(type));
    }

    public StructureBuilder addRing(ComponentType type, int halfSize, int y) {
        return addRing(type, halfSize, y, validatorFor(type));
    }

    public StructureBuilder addRing(ComponentType type, int halfSize, int y, Predicate<BlockState> validator) {
        for (int i = -halfSize; i <= halfSize; i++) {
            addAt(type, i, y, -halfSize, validator);
            addAt(type, i, y, halfSize, validator);
            if (i != -halfSize && i != halfSize) {
                addAt(type, -halfSize, y, i, validator);
                addAt(type, halfSize, y, i, validator);
            }
        }
        return this;
    }

    public StructureBuilder addFilledLayer(ComponentType fill, int halfSize, int y, ComponentType border) {
        return addFilledLayer(fill, halfSize, y, border, validatorFor(fill), validatorFor(border));
    }

    public StructureBuilder addFilledLayer(ComponentType fill, int halfSize, int y, ComponentType border,
                                           Predicate<BlockState> fillValidator, Predicate<BlockState> borderValidator) {
        for (int x = -halfSize; x <= halfSize; x++) {
            for (int z = -halfSize; z <= halfSize; z++) {
                boolean isBorder = Math.abs(x) == halfSize || Math.abs(z) == halfSize;
                if (isBorder) {
                    addAt(border, x, y, z, borderValidator);
                } else {
                    addAt(fill, x, y, z, fillValidator);
                }
            }
        }
        return this;
    }

    public StructureDefinition build() {
        List<StructureComponent> componentList = new ArrayList<>(components.values());
        Map<BlockPos, StructureComponent> offsetMap = new HashMap<>(components);

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;

        for (BlockPos pos : components.keySet()) {
            minX = Math.min(minX, pos.getX());
            minY = Math.min(minY, pos.getY());
            minZ = Math.min(minZ, pos.getZ());
            maxX = Math.max(maxX, pos.getX() + 1);
            maxY = Math.max(maxY, pos.getY() + 1);
            maxZ = Math.max(maxZ, pos.getZ() + 1);
        }

        AABB bounds = new AABB(minX, minY, minZ, maxX, maxY, maxZ);
        return new StructureDefinition(componentList, offsetMap, bounds);
    }

    private void addAt(ComponentType type, int x, int y, int z, Predicate<BlockState> validator) {
        BlockPos offset = new BlockPos(x, y, z);
        if (!components.containsKey(offset)) {
            components.put(offset, new StructureComponent(offset, type, tier, validator));
        }
    }

    private Predicate<BlockState> validatorFor(ComponentType type) {
        Predicate<BlockState> configured = validators.get(type);
        if (configured != null) return configured;
        return state -> !state.isAir();
    }
}
