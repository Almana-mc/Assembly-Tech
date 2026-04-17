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

    /*
     * T1 Void Miner — 7 wide, 7 tall arching structure
     *
     * Y=0:  7x7 top platform — frame border, controller at center
     * Y=-1: Corner pillars at ±3,±3, edge frames at ±3,0 and 0,±3
     * Y=-2: Corner pillars at ±3,±3, panels filling walls between pillars
     * Y=-3: Arch inward — pillars step to ±2,±2, panels between
     * Y=-4: Arch converges — pillars at ±1,±1, drill core at center
     * Y=-5: Drill block below drill core
     * Y=-6: Void block at bottom
     */
    public static MultiblockType TIER_1;

    public static void init() {
        Set<Block> upgradeBlocks = Set.copyOf(Arrays.asList(UpgradeBlocks.all()));
        StructureDefinition structure = new StructureBuilder(1)
                .withValidator(ComponentType.FRAME, state -> state.is(ModBlocks.STRUCTURE_FRAME_1.get()))
                .withValidator(ComponentType.PANEL, state -> state.is(ModBlocks.STRUCTURE_PANEL.get())
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

        TIER_1 = new MultiblockType(
                Identifier.fromNamespaceAndPath(Assemblytech.MODID, "void_miner_t1"),
                1,
                structure
        );

        MultiblockRegistry.register(TIER_1);
    }
}
