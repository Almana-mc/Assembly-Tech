package me.almana.assemblytech.generation;

import net.minecraft.data.worldgen.BootstrapContext;

public final class MinerTierConfigBootstrap {

    private MinerTierConfigBootstrap() {}

    public static void bootstrap(BootstrapContext<MinerTierConfig> ctx) {
        ctx.register(MinerTierConfigRegistries.oreMinerKey(1), new MinerTierConfig(1, 200, 400, 0));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(2), new MinerTierConfig(2, 500, 320, 2));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(3), new MinerTierConfig(3, 1_000, 160, 4));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(4), new MinerTierConfig(4, 2_000, 80, 6));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(5), new MinerTierConfig(5, 4_000, 40, 8));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(6), new MinerTierConfig(6, 8_000, 20, 10));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(7), new MinerTierConfig(7, 16_000, 10, 12));
    }
}
