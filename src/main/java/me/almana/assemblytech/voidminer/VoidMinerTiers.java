package me.almana.assemblytech.voidminer;

public final class VoidMinerTiers {

    private VoidMinerTiers() {}

    public static int oreMinerEnergyCost(int tier) {
        return switch (tier) {
            case 1 -> 264_000;
            case 2 -> 184_000;
            case 3 -> 120_000;
            case 4 -> 72_000;
            case 5 -> 40_000;
            case 6 -> 16_000;
            case 7 -> 8_000;
            default -> 264_000;
        };
    }

    public static int oreMinerProcessTicks(int tier) {
        return switch (tier) {
            case 1 -> 400;
            case 2 -> 320;
            case 3 -> 160;
            case 4 -> 80;
            case 5 -> 40;
            case 6 -> 20;
            case 7 -> 10;
            default -> 400;
        };
    }

    public static int oreMinerEnergyPerTick(int tier) {
        int ticks = oreMinerProcessTicks(tier);
        return ticks <= 0 ? oreMinerEnergyCost(tier) : oreMinerEnergyCost(tier) / ticks;
    }

    public static int oreMinerUpgradeSlots(int tier) {
        return Math.max(0, (tier - 1) * 2);
    }
}
