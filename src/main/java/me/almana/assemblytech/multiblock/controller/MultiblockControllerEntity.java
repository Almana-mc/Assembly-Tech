package me.almana.assemblytech.multiblock.controller;

import com.mojang.serialization.Codec;
import me.almana.assemblytech.multiblock.api.MultiblockType;
import me.almana.assemblytech.multiblock.api.StructureDefinition;
import me.almana.assemblytech.multiblock.modifier.ModifierData;
import me.almana.assemblytech.multiblock.slave.MultiblockSlaveEntity;
import me.almana.assemblytech.multiblock.validation.IntegrityMonitor;
import me.almana.assemblytech.multiblock.validation.StructureValidator;
import me.almana.assemblytech.multiblock.validation.ValidationResult;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiblockControllerEntity extends BlockEntity {

    private static final String TAG_FORMED = "Formed";
    private static final String TAG_SLAVES = "Slaves";
    private static final String TAG_MODIFIERS = "Modifiers";

    private static final int REVALIDATION_DELAY = 4;

    protected boolean formed;
    protected List<BlockPos> slavePositions = List.of();
    @Nullable
    protected ModifierData modifierData;
    protected int placedUpgradeCount;

    boolean needsRevalidation;
    int revalidationCooldown;
    boolean loadedFormed;

    public MultiblockControllerEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract MultiblockType getMultiblockType();

    protected abstract void onFormed();

    protected abstract void onBroken();

    protected abstract void tickFormed(Level level, BlockPos pos, BlockState state);

    protected int getMaxUpgradeSlots() {
        return Integer.MAX_VALUE;
    }

    public static <T extends MultiblockControllerEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
        if (entity.loadedFormed) {
            entity.loadedFormed = false;
            IntegrityMonitor.register(entity, entity.getMultiblockType().structure().getWorldBounds(pos));
            entity.needsRevalidation = true;
            entity.revalidationCooldown = 1;
        }

        if (!entity.formed) return;

        if (entity.needsRevalidation) {
            entity.revalidationCooldown--;
            if (entity.revalidationCooldown <= 0) {
                entity.revalidate();
            }
            return;
        }

        entity.tickFormed(level, pos, state);
    }

    public ValidationResult tryForm() {
        if (level == null) return ValidationResult.failure(List.of());
        if (formed) return ValidationResult.success(slavePositions);

        StructureDefinition structure = getMultiblockType().structure();
        ValidationResult result = StructureValidator.validate(level, worldPosition, structure);

        if (result.isValid()) {
            formed = true;
            slavePositions = result.slavePositions();
            bindSlaves();
            IntegrityMonitor.register(this, structure.getWorldBounds(worldPosition));
            placedUpgradeCount = ModifierData.countUpgrades(level, slavePositions);
            modifierData = ModifierData.collect(level, slavePositions, getMaxUpgradeSlots());
            onFormed();
            setChanged();
        }

        return result;
    }

    public void deform() {
        if (!formed) return;

        unbindSlaves();
        IntegrityMonitor.unregister(this);
        formed = false;
        slavePositions = List.of();
        modifierData = null;
        placedUpgradeCount = 0;
        needsRevalidation = false;
        onBroken();
        setChanged();
    }

    public void flagRevalidation() {
        if (!formed) return;
        needsRevalidation = true;
        revalidationCooldown = REVALIDATION_DELAY;
    }

    public boolean isFormed() {
        return formed;
    }

    public List<BlockPos> getSlavePositions() {
        return slavePositions;
    }

    @Nullable
    public ModifierData getModifierData() {
        return modifierData;
    }

    public int getPlacedUpgradeCount() {
        return placedUpgradeCount;
    }

    public boolean isOverUpgradeCap() {
        return placedUpgradeCount > getMaxUpgradeSlots();
    }

    @Nullable
    public AABB getWorldBounds() {
        if (!formed) return null;
        return getMultiblockType().structure().getWorldBounds(worldPosition);
    }

    void revalidate() {
        needsRevalidation = false;
        if (level == null) return;

        StructureDefinition structure = getMultiblockType().structure();
        ValidationResult result = StructureValidator.validate(level, worldPosition, structure);

        if (!result.isValid()) {
            deform();
        } else {
            placedUpgradeCount = ModifierData.countUpgrades(level, slavePositions);
            modifierData = ModifierData.collect(level, slavePositions, getMaxUpgradeSlots());
        }
    }

    private void bindSlaves() {
        if (level == null) return;
        for (BlockPos pos : slavePositions) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MultiblockSlaveEntity slave) {
                slave.bindTo(worldPosition);
            }
        }
    }

    private void unbindSlaves() {
        if (level == null) return;
        for (BlockPos pos : slavePositions) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MultiblockSlaveEntity slave) {
                slave.unbind();
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean(TAG_FORMED, formed);

        if (formed && !slavePositions.isEmpty()) {
            ValueOutput.TypedOutputList<Long> packed = output.list(TAG_SLAVES, Codec.LONG);
            for (BlockPos slavePos : slavePositions) {
                packed.add(slavePos.asLong());
            }
        }

        if (modifierData != null) {
            output.store(TAG_MODIFIERS, CompoundTag.CODEC, modifierData.saveToNbt());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        formed = input.getBooleanOr(TAG_FORMED, false);
        slavePositions = List.of();
        modifierData = null;
        loadedFormed = false;

        if (formed) {
            input.list(TAG_SLAVES, Codec.LONG).ifPresent(slaveList -> {
                List<BlockPos> positions = new ArrayList<>();
                for (long l : slaveList) {
                    positions.add(BlockPos.of(l));
                }
                slavePositions = List.copyOf(positions);
            });

            input.read(TAG_MODIFIERS, CompoundTag.CODEC).ifPresent(tag -> {
                modifierData = ModifierData.loadFromNbt(tag);
            });

            loadedFormed = true;
        }
    }
}
