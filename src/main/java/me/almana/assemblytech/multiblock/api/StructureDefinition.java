package me.almana.assemblytech.multiblock.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StructureDefinition {

    private final List<StructureComponent> components;
    private final Map<BlockPos, StructureComponent> componentsByOffset;
    private final AABB relativeBounds;

    public StructureDefinition(List<StructureComponent> components, Map<BlockPos, StructureComponent> componentsByOffset, AABB relativeBounds) {
        this.components = Collections.unmodifiableList(components);
        this.componentsByOffset = Collections.unmodifiableMap(componentsByOffset);
        this.relativeBounds = relativeBounds;
    }

    public List<StructureComponent> components() {
        return components;
    }

    public StructureComponent componentAt(BlockPos offset) {
        return componentsByOffset.get(offset);
    }

    public AABB relativeBounds() {
        return relativeBounds;
    }

    public AABB getWorldBounds(BlockPos controllerPos) {
        return relativeBounds.move(controllerPos.getX(), controllerPos.getY(), controllerPos.getZ());
    }

    public int size() {
        return components.size();
    }
}
