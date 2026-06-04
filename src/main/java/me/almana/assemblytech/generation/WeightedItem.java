package me.almana.assemblytech.generation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record WeightedItem(Optional<Holder<Item>> item, Optional<TagKey<Item>> tag, int weight, int min, int max, int minTier) {

    public static final Codec<WeightedItem> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BuiltInRegistries.ITEM.holderByNameCodec().optionalFieldOf("item").forGetter(WeightedItem::item),
            TagKey.codec(Registries.ITEM).optionalFieldOf("tag").forGetter(WeightedItem::tag),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").forGetter(WeightedItem::weight),
            Codec.intRange(1, 64).optionalFieldOf("min", 1).forGetter(WeightedItem::min),
            Codec.intRange(1, 64).optionalFieldOf("max", 1).forGetter(WeightedItem::max),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("min_tier", 1).forGetter(WeightedItem::minTier)
    ).apply(inst, WeightedItem::new));

    public WeightedItem {
        if (item.isPresent() == tag.isPresent()) throw new IllegalArgumentException("entry needs item or tag");
        if (min > max) throw new IllegalArgumentException("min > max");
    }

    public static WeightedItem item(Holder<Item> item, int weight, int min, int max, int minTier) {
        return new WeightedItem(Optional.of(item), Optional.empty(), weight, min, max, minTier);
    }

    public static WeightedItem tag(TagKey<Item> tag, int weight, int min, int max, int minTier) {
        return new WeightedItem(Optional.empty(), Optional.of(tag), weight, min, max, minTier);
    }

    public List<Item> resolveItems() {
        if (item.isPresent()) return List.of(item.get().value());
        List<Item> items = new ArrayList<>();
        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag.get())) {
            items.add(holder.value());
        }
        return items;
    }

    public Optional<Item> representativeItem() {
        if (item.isPresent()) return Optional.of(item.get().value());
        for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag.get())) {
            return Optional.of(holder.value());
        }
        return Optional.empty();
    }
}
