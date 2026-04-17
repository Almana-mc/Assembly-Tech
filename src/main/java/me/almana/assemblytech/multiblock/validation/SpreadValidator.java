package me.almana.assemblytech.multiblock.validation;

import me.almana.assemblytech.multiblock.api.StructureComponent;
import me.almana.assemblytech.multiblock.api.StructureDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpreadValidator {

    private final Iterator<StructureComponent> remaining;
    private final int blocksPerTick;
    private final List<BlockPos> validSlaves = new ArrayList<>();
    private boolean done;

    public SpreadValidator(StructureDefinition structure, int blocksPerTick) {
        this.remaining = structure.components().iterator();
        this.blocksPerTick = blocksPerTick;
    }

    @Nullable
    public ValidationResult tickValidate(Level level, BlockPos controllerPos) {
        if (done) return null;

        int checked = 0;
        while (remaining.hasNext() && checked < blocksPerTick) {
            StructureComponent comp = remaining.next();
            BlockPos worldPos = controllerPos.offset(comp.offset());

            if (!comp.test(level, worldPos)) {
                done = true;
                return ValidationResult.failure(List.of(comp));
            }
            validSlaves.add(worldPos);
            checked++;
        }

        if (!remaining.hasNext()) {
            done = true;
            return ValidationResult.success(validSlaves);
        }
        return null;
    }

    public boolean isDone() {
        return done;
    }
}
