package me.almana.assemblytech.generation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MinerTierConfig(int tier, int energyPerTick, int processTicks, int upgradeSlots) {

    public static final Codec<MinerTierConfig> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.intRange(1, 64).fieldOf("tier").forGetter(MinerTierConfig::tier),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf("energy_per_tick").forGetter(MinerTierConfig::energyPerTick),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("process_ticks").forGetter(MinerTierConfig::processTicks),
            Codec.intRange(0, 64).optionalFieldOf("upgrade_slots", 0).forGetter(MinerTierConfig::upgradeSlots)
    ).apply(inst, MinerTierConfig::new));
}
