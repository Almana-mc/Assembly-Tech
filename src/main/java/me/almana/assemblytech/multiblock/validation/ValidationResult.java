package me.almana.assemblytech.multiblock.validation;

import me.almana.assemblytech.multiblock.api.StructureComponent;
import net.minecraft.core.BlockPos;

import java.util.List;

public final class ValidationResult {

    private final boolean valid;
    private final List<BlockPos> slavePositions;
    private final List<StructureComponent> missingComponents;

    private ValidationResult(boolean valid, List<BlockPos> slavePositions, List<StructureComponent> missingComponents) {
        this.valid = valid;
        this.slavePositions = slavePositions;
        this.missingComponents = missingComponents;
    }

    public static ValidationResult success(List<BlockPos> slavePositions) {
        return new ValidationResult(true, List.copyOf(slavePositions), List.of());
    }

    public static ValidationResult failure(List<StructureComponent> missing) {
        return new ValidationResult(false, List.of(), List.copyOf(missing));
    }

    public boolean isValid() {
        return valid;
    }

    public List<BlockPos> slavePositions() {
        return slavePositions;
    }

    public List<StructureComponent> missingComponents() {
        return missingComponents;
    }
}
