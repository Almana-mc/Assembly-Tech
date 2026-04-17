package me.almana.assemblytech.network;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.port.FluidPortMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FluidPortSyncPayload(int containerId, FluidStack fluid) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<FluidPortSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Assemblytech.MODID, "fluid_port_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidPortSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, FluidPortSyncPayload::containerId,
                    FluidStack.OPTIONAL_STREAM_CODEC, FluidPortSyncPayload::fluid,
                    FluidPortSyncPayload::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(FluidPortSyncPayload payload, IPayloadContext ctx) {
        Player player = ctx.player();
        if (player.containerMenu instanceof FluidPortMenu menu && menu.containerId == payload.containerId) {
            menu.setFluidClient(payload.fluid);
        }
    }
}
