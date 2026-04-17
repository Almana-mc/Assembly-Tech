package me.almana.assemblytech.generation;

import net.minecraft.data.worldgen.BootstrapContext;

public final class MinerTierConfigBootstrap {

    private MinerTierConfigBootstrap() {}

    public static void bootstrap(BootstrapContext<MinerTierConfig> ctx) {
        ctx.register(MinerTierConfigRegistries.oreMinerKey(1), new MinerTierConfig(1, 660, 400, 0));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(2), new MinerTierConfig(2, 575, 320, 2));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(3), new MinerTierConfig(3, 750, 160, 4));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(4), new MinerTierConfig(4, 900, 80, 6));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(5), new MinerTierConfig(5, 1000, 40, 8));
        ctx.register(MinerTierConfigRegistries.oreMinerKey(6), new MinerTierConfig(6, 800, 20, 10));
    }
}
