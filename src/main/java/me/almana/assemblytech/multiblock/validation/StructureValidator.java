package me.almana.assemblytech.multiblock.validation;

import me.almana.assemblytech.multiblock.api.StructureComponent;
import me.almana.assemblytech.multiblock.api.StructureDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public final class StructureValidator {

    private StructureValidator() {}

    public static ValidationResult validate(Level level, BlockPos controllerPos, StructureDefinition structure) {
        List<BlockPos> validSlaves = new ArrayList<>();
        List<StructureComponent> missing = new ArrayList<>();

        for (StructureComponent component : structure.components()) {
            BlockPos worldPos = controllerPos.offset(component.offset());

            if (component.test(level, worldPos)) {
                validSlaves.add(worldPos);
            } else {
                missing.add(component);
            }
        }

        if (missing.isEmpty()) {
            return ValidationResult.success(validSlaves);
        }
        return ValidationResult.failure(missing);
    }
}
