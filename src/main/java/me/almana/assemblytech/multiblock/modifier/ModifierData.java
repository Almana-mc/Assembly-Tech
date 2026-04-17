package me.almana.assemblytech.multiblock.modifier;

import me.almana.assemblytech.Assemblytech;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModifierData {

    public static final Identifier SPEED = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "speed_mod");
    public static final Identifier ACCURACY = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "accuracy");
    public static final Identifier ENERGY_MULTIPLIER = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "forge_energy_multiplier");
    public static final Identifier EXTRA_DROPS = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "extra_drops");
    public static final Identifier PIEZO = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "piezo");
    public static final Identifier SILK_TOUCH = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "silk_touch");
    public static final Identifier ENERGY_FIXED = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "energy_fixed");
    public static final Identifier EFFICIENCY = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "efficiency");
    public static final Identifier FORTUNE = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "fortune");
    public static final Identifier PARALLEL_BONUS = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "parallel_bonus");

    private static final Set<Identifier> MAX_MERGE = Set.of(FORTUNE);

    private static final ModifierData EMPTY = new ModifierData(Map.of(), 0);

    private final Map<Identifier, Double> attributes;
    private final int totalEnergyFixedCost;

    private ModifierData(Map<Identifier, Double> attributes, int totalEnergyFixedCost) {
        this.attributes = Map.copyOf(attributes);
        this.totalEnergyFixedCost = totalEnergyFixedCost;
    }

    public static ModifierData empty() {
        return EMPTY;
    }

    public static int countUpgrades(Level level, List<BlockPos> slavePositions) {
        int count = 0;
        for (BlockPos pos : slavePositions) {
            if (level.getBlockEntity(pos) instanceof ModifierEntity) count++;
        }
        return count;
    }

    public static ModifierData collect(Level level, List<BlockPos> slavePositions, int maxUpgradeSlots) {
        Map<Identifier, Double> raw = new HashMap<>();
        int energyCost = 0;
        int counted = 0;

        for (BlockPos pos : slavePositions) {
            if (counted >= maxUpgradeSlots) break;
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ModifierEntity mod) {
                counted++;
                for (ModifierAttribute attr : mod.getAttributes()) {
                    if (MAX_MERGE.contains(attr.id())) {
                        raw.merge(attr.id(), attr.value(), Math::max);
                    } else {
                        raw.merge(attr.id(), attr.value(), Double::sum);
                    }
                    if (attr.id().equals(ENERGY_FIXED)) {
                        energyCost += (int) attr.value();
                    }
                }
            }
        }

        return new ModifierData(raw, energyCost);
    }

    public double get(Identifier id) {
        return attributes.getOrDefault(id, 0.0);
    }

    public double getSpeedMultiplier() {
        return Math.pow(0.7, get(SPEED));
    }

    public double getAccuracyMultiplier() {
        return Math.pow(1.18, get(ACCURACY));
    }

    public double getEnergyMultiplier() {
        return Math.pow(1.12, get(ENERGY_MULTIPLIER));
    }

    public double getEfficiencyMultiplier() {
        return Math.pow(0.9, get(EFFICIENCY));
    }

    public int getFortuneLevel() {
        return (int) get(FORTUNE);
    }

    public int getParallelBonus() {
        return (int) get(PARALLEL_BONUS);
    }

    public int getExtraDrops() {
        return (int) get(EXTRA_DROPS);
    }

    public boolean hasPiezo() {
        return get(PIEZO) > 0;
    }

    public boolean hasSilkTouch() {
        return get(SILK_TOUCH) > 0;
    }

    public int getTotalEnergyFixedCost() {
        return totalEnergyFixedCost;
    }

    public CompoundTag saveToNbt() {
        CompoundTag tag = new CompoundTag();
        CompoundTag attrs = new CompoundTag();
        for (Map.Entry<Identifier, Double> entry : attributes.entrySet()) {
            attrs.putDouble(entry.getKey().toString(), entry.getValue());
        }
        tag.put("Attributes", attrs);
        tag.putInt("EnergyCost", totalEnergyFixedCost);
        return tag;
    }

    public static ModifierData loadFromNbt(CompoundTag tag) {
        Map<Identifier, Double> attrs = new HashMap<>();
        CompoundTag attrTag = tag.getCompoundOrEmpty("Attributes");
        for (String key : attrTag.keySet()) {
            attrs.put(Identifier.parse(key), attrTag.getDoubleOr(key, 0.0));
        }
        int energyCost = tag.getIntOr("EnergyCost", 0);
        return new ModifierData(attrs, energyCost);
    }
}
