package me.almana.assemblytech.voidminer.recipe;

import me.almana.assemblytech.Assemblytech;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.LinkedHashMap;
import java.util.Map;

public record DesignatorTexturesPayload(Map<Identifier, String> textures) implements CustomPacketPayload {
    public static final Type<DesignatorTexturesPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Assemblytech.MODID, "designator_textures"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DesignatorTexturesPayload> STREAM_CODEC =
            StreamCodec.of(DesignatorTexturesPayload::write, DesignatorTexturesPayload::read);

    public static DesignatorTexturesPayload create(RecipeManager recipeManager) {
        Map<Identifier, String> textures = new LinkedHashMap<>();
        for (var holder : recipeManager.getRecipes()) {
            if (holder.value() instanceof VoidMiningRecipe recipe) {
                textures.put(BuiltInRegistries.ITEM.getKey(recipe.designator().value()), recipe.texture());
            }
        }
        return new DesignatorTexturesPayload(textures);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static DesignatorTexturesPayload read(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<Identifier, String> textures = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) {
            textures.put(buf.readIdentifier(), buf.readUtf());
        }
        return new DesignatorTexturesPayload(textures);
    }

    private static void write(RegistryFriendlyByteBuf buf, DesignatorTexturesPayload payload) {
        buf.writeVarInt(payload.textures.size());
        payload.textures.forEach((item, texture) -> {
            buf.writeIdentifier(item);
            buf.writeUtf(texture);
        });
    }
}
