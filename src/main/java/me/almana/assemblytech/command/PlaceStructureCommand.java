package me.almana.assemblytech.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.almana.assemblytech.multiblock.api.ComponentType;
import me.almana.assemblytech.multiblock.api.StructureComponent;
import me.almana.assemblytech.multiblock.api.StructureDefinition;
import me.almana.assemblytech.multiblock.controller.MultiblockControllerEntity;
import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.voidminer.VoidMinerStructures;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.EnumMap;
import java.util.Map;

public final class PlaceStructureCommand {

    private PlaceStructureCommand() {}

    private static Map<ComponentType, Block> blockMap(int tier) {
        Map<ComponentType, Block> map = new EnumMap<>(ComponentType.class);
        map.put(ComponentType.FRAME, ModBlocks.frame(tier).get());
        map.put(ComponentType.PANEL, ModBlocks.panel(tier).get());
        map.put(ComponentType.DRILL_CORE, ModBlocks.DRILL_CORE.get());
        map.put(ComponentType.DRILL_BLOCK, ModBlocks.DRILL_BLOCK.get());
        map.put(ComponentType.VOID_BLOCK, ModBlocks.VOID_BLOCK.get());
        return map;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("assemblytech")
                        .then(Commands.literal("place")
                                .then(Commands.literal("void_miner")
                                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                                        .then(Commands.argument("tier", IntegerArgumentType.integer(1, ModBlocks.MINER_TIERS))
                                                .executes(PlaceStructureCommand::placeVoidMiner)
                                        )
                                )
                        )
        );
    }

    private static int placeVoidMiner(CommandContext<CommandSourceStack> ctx) {
        int tier = IntegerArgumentType.getInteger(ctx, "tier");
        CommandSourceStack source = ctx.getSource();
        Level level = source.getLevel();
        BlockPos center = BlockPos.containing(source.getPosition());
        Map<ComponentType, Block> map = blockMap(tier);

        level.setBlock(center, ModBlocks.controller(tier).get().defaultBlockState(), Block.UPDATE_ALL);

        StructureDefinition structure = VoidMinerStructures.get(tier).structure();
        int placed = 0;
        for (StructureComponent comp : structure.components()) {
            BlockPos worldPos = center.offset(comp.offset());
            Block block = map.get(comp.type());
            if (block != null) {
                level.setBlock(worldPos, block.defaultBlockState(), Block.UPDATE_ALL);
                placed++;
            }
        }

        boolean formed = false;
        BlockEntity be = level.getBlockEntity(center);
        if (be instanceof MultiblockControllerEntity controller) {
            formed = controller.tryForm().isValid();
        }

        int placedCount = placed;
        String formationStatus = formed ? "formed" : "not formed";
        int finalTier = tier;
        source.sendSuccess(
                () -> Component.literal(
                        "Placed Void Miner T" + finalTier + " at "
                                + center.toShortString()
                                + " ("
                                + placedCount
                                + " blocks, "
                                + formationStatus
                                + ")"
                ),
                true
        );
        return placed;
    }
}
