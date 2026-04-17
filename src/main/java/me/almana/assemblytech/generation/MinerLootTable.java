package me.almana.assemblytech.generation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;

public record MinerLootTable(Identifier family, List<WeightedItem> entries) {

    public static final Codec<MinerLootTable> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Identifier.CODEC.fieldOf("family").forGetter(MinerLootTable::family),
            WeightedItem.CODEC.listOf().fieldOf("entries").forGetter(MinerLootTable::entries)
    ).apply(inst, MinerLootTable::new));
}
