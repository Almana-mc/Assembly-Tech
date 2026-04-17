package me.almana.assemblytech.voidminer;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.multiblock.api.ComponentType;
import me.almana.assemblytech.multiblock.api.MultiblockRegistry;
import me.almana.assemblytech.multiblock.api.MultiblockType;
import me.almana.assemblytech.multiblock.api.StructureDefinition;
import me.almana.assemblytech.multiblock.definition.StructureBuilder;
import me.almana.assemblytech.port.PortBlock;
import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.upgrade.UpgradeBlocks;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.Set;

public final class VoidMinerStructures {

    private static final MultiblockType[] TIERS = new MultiblockType[ModBlocks.MINER_TIERS + 1];

    public static MultiblockType TIER_1;
    public static MultiblockType TIER_2;
    public static MultiblockType TIER_3;
    public static MultiblockType TIER_4;
    public static MultiblockType TIER_5;
    public static MultiblockType TIER_6;
    public static MultiblockType TIER_7;

    public static void init() {
        for (int tier = 1; tier <= ModBlocks.MINER_TIERS; tier++) {
            TIERS[tier] = buildTier(tier);
            MultiblockRegistry.register(TIERS[tier]);
        }
        TIER_1 = TIERS[1];
        TIER_2 = TIERS[2];
        TIER_3 = TIERS[3];
        TIER_4 = TIERS[4];
        TIER_5 = TIERS[5];
        TIER_6 = TIERS[6];
        TIER_7 = TIERS[7];
    }

    public static MultiblockType get(int tier) {
        if (tier < 1 || tier > ModBlocks.MINER_TIERS) return TIERS[1];
        return TIERS[tier];
    }

    private static MultiblockType buildTier(int tier) {
        Set<Block> upgradeBlocks = Set.copyOf(Arrays.asList(UpgradeBlocks.all()));
        Block frame = ModBlocks.frame(tier).get();
        Block panel = ModBlocks.panel(tier).get();

        StructureDefinition structure = new StructureBuilder(tier)
                .withValidator(ComponentType.FRAME, state -> state.is(frame))
                .withValidator(ComponentType.PANEL, state -> state.is(panel)
                        || state.getBlock() instanceof PortBlock
                        || upgradeBlocks.contains(state.getBlock()))
                .withValidator(ComponentType.MODIFIER, state -> upgradeBlocks.contains(state.getBlock()))
                .withValidator(ComponentType.DRILL_CORE, state -> state.is(ModBlocks.DRILL_CORE.get()))
                .withValidator(ComponentType.DRILL_BLOCK, state -> state.is(ModBlocks.DRILL_BLOCK.get()))
                .withValidator(ComponentType.VOID_BLOCK, state -> state.is(ModBlocks.VOID_BLOCK.get()))

                .addRing(ComponentType.FRAME, 3, 0)

                .addSymmetrical(ComponentType.FRAME, 3, -1, 3)
                .addSymmetrical(ComponentType.FRAME, 3, -1, 0)
                .addSymmetrical(ComponentType.FRAME, 0, -1, 3)

                .addSymmetrical(ComponentType.FRAME, 3, -2, 3)
                .addSymmetrical(ComponentType.PANEL, 3, -2, 2)
                .addSymmetrical(ComponentType.PANEL, 3, -2, 1)
                .addSymmetrical(ComponentType.PANEL, 2, -2, 3)
                .addSymmetrical(ComponentType.PANEL, 1, -2, 3)

                .addSymmetrical(ComponentType.FRAME, 2, -3, 2)
                .addSymmetrical(ComponentType.PANEL, 2, -3, 1)
                .addSymmetrical(ComponentType.PANEL, 1, -3, 2)
                .addSymmetrical(ComponentType.PANEL, 2, -3, 0)
                .addSymmetrical(ComponentType.PANEL, 0, -3, 2)

                .addSymmetrical(ComponentType.FRAME, 1, -4, 1)
                .addSymmetrical(ComponentType.PANEL, 1, -4, 0)
                .addSymmetrical(ComponentType.PANEL, 0, -4, 1)
                .add(ComponentType.DRILL_CORE, 0, -4, 0)

                .add(ComponentType.DRILL_BLOCK, 0, -5, 0)

                .add(ComponentType.VOID_BLOCK, 0, -6, 0)

                .build();

        return new MultiblockType(
                Identifier.fromNamespaceAndPath(Assemblytech.MODID, "void_miner_t" + tier),
                tier,
                structure
        );
    }
}
