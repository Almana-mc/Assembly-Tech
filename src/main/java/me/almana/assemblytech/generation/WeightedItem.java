package me.almana.assemblytech.generation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public record WeightedItem(Holder<Item> item, int weight, int min, int max, int minTier) {

    public static final Codec<WeightedItem> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(WeightedItem::item),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").forGetter(WeightedItem::weight),
            Codec.intRange(1, 64).optionalFieldOf("min", 1).forGetter(WeightedItem::min),
            Codec.intRange(1, 64).optionalFieldOf("max", 1).forGetter(WeightedItem::max),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("min_tier", 1).forGetter(WeightedItem::minTier)
    ).apply(inst, WeightedItem::new));
}
