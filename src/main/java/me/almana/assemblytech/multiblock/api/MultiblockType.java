package me.almana.assemblytech.multiblock.api;

import net.minecraft.resources.Identifier;

public record MultiblockType(
        Identifier id,
        int tier,
        StructureDefinition structure
) {}
