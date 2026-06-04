package me.almana.assemblytech.voidminer.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record VoidMiningEntry(Optional<Holder<Item>> item, Optional<TagKey<Item>> tag, int weight, int min, int max) {

    public static final Codec<VoidMiningEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BuiltInRegistries.ITEM.holderByNameCodec().optionalFieldOf("item").forGetter(VoidMiningEntry::item),
            TagKey.codec(Registries.ITEM).optionalFieldOf("tag").forGetter(VoidMiningEntry::tag),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").forGetter(VoidMiningEntry::weight),
            Codec.intRange(1, 64).optionalFieldOf("min", 1).forGetter(VoidMiningEntry::min),
            Codec.intRange(1, 64).optionalFieldOf("max", 1).forGetter(VoidMiningEntry::max)
    ).apply(inst, VoidMiningEntry::new));

    public VoidMiningEntry {
        if (item.isPresent() == tag.isPresent()) throw new IllegalArgumentException("entry needs item or tag");
        if (min > max) throw new IllegalArgumentException("min > max");
    }

    public static VoidMiningEntry item(Holder<Item> item, int weight, int min, int max) {
        return new VoidMiningEntry(Optional.of(item), Optional.empty(), weight, min, max);
    }

    public static VoidMiningEntry tag(TagKey<Item> tag, int weight, int min, int max) {
        return new VoidMiningEntry(Optional.empty(), Optional.of(tag), weight, min, max);
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

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(item.isPresent());
        if (item.isPresent()) {
            buf.writeIdentifier(BuiltInRegistries.ITEM.getKey(item.get().value()));
        } else {
            buf.writeIdentifier(tag.get().location());
        }
        buf.writeVarInt(weight);
        buf.writeVarInt(min);
        buf.writeVarInt(max);
    }

    public static VoidMiningEntry read(FriendlyByteBuf buf) {
        boolean hasItem = buf.readBoolean();
        Identifier id = buf.readIdentifier();
        int weight = buf.readVarInt();
        int min = buf.readVarInt();
        int max = buf.readVarInt();
        if (hasItem) {
            return item(BuiltInRegistries.ITEM.wrapAsHolder(BuiltInRegistries.ITEM.getValue(id)), weight, min, max);
        }
        return tag(TagKey.create(Registries.ITEM, id), weight, min, max);
    }
}
