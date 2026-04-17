package me.almana.assemblytech.multiblock.api;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MultiblockRegistry {

    private static final Map<Identifier, MultiblockType> TYPES = new LinkedHashMap<>();

    private MultiblockRegistry() {}

    public static void register(MultiblockType type) {
        if (TYPES.containsKey(type.id())) {
            throw new IllegalStateException("Duplicate multiblock type: " + type.id());
        }
        TYPES.put(type.id(), type);
    }

    @Nullable
    public static MultiblockType get(Identifier id) {
        return TYPES.get(id);
    }

    public static Collection<MultiblockType> all() {
        return Collections.unmodifiableCollection(TYPES.values());
    }
}
