package me.almana.assemblytech;

import com.mojang.logging.LogUtils;
import me.almana.assemblytech.command.PlaceStructureCommand;
import me.almana.assemblytech.generation.MinerLootRegistries;
import me.almana.assemblytech.generation.MinerTierConfigRegistries;
import me.almana.assemblytech.multiblock.validation.IntegrityMonitor;
import me.almana.assemblytech.registry.ModBlockEntities;
import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.registry.ModItems;
import me.almana.assemblytech.registry.ModMenus;
import me.almana.assemblytech.network.FluidPortSyncPayload;
import me.almana.assemblytech.port.EnergyPortScreen;
import me.almana.assemblytech.port.FluidPortScreen;
import me.almana.assemblytech.port.ItemPortScreen;
import me.almana.assemblytech.voidminer.screen.MinerLootScreen;
import me.almana.assemblytech.voidminer.screen.VoidMinerStatusScreen;
import me.almana.assemblytech.voidminer.VoidMinerStructures;
import me.almana.assemblytech.worldgen.ModFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(Assemblytech.MODID)
public class Assemblytech {

    public static final String MODID = "assemblytech";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.assemblytech"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.VOID_MINER_CONTROLLER_1.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.STRUCTURE_FRAME_1.get());
                        output.accept(ModItems.STRUCTURE_FRAME_2.get());
                        output.accept(ModItems.STRUCTURE_FRAME_3.get());
                        output.accept(ModItems.STRUCTURE_FRAME_4.get());
                        output.accept(ModItems.STRUCTURE_FRAME_5.get());
                        output.accept(ModItems.STRUCTURE_FRAME_6.get());
                        output.accept(ModItems.STRUCTURE_FRAME_7.get());
                        output.accept(ModItems.STRUCTURE_PANEL_1.get());
                        output.accept(ModItems.STRUCTURE_PANEL_2.get());
                        output.accept(ModItems.STRUCTURE_PANEL_3.get());
                        output.accept(ModItems.STRUCTURE_PANEL_4.get());
                        output.accept(ModItems.STRUCTURE_PANEL_5.get());
                        output.accept(ModItems.STRUCTURE_PANEL_6.get());
                        output.accept(ModItems.STRUCTURE_PANEL_7.get());
                        output.accept(ModItems.DRILL_CORE.get());
                        output.accept(ModItems.DRILL_BLOCK.get());
                        output.accept(ModItems.VOID_BLOCK.get());
                        output.accept(ModItems.VOID_MINER_CONTROLLER_1.get());
                        output.accept(ModItems.VOID_MINER_CONTROLLER_2.get());
                        output.accept(ModItems.VOID_MINER_CONTROLLER_3.get());
                        output.accept(ModItems.VOID_MINER_CONTROLLER_4.get());
                        output.accept(ModItems.VOID_MINER_CONTROLLER_5.get());
                        output.accept(ModItems.VOID_MINER_CONTROLLER_6.get());
                        output.accept(ModItems.VOID_MINER_CONTROLLER_7.get());
                        output.accept(ModItems.ARCANITE_CRYSTAL.get());
                        output.accept(ModItems.SOLUNITE_CRYSTAL.get());
                        output.accept(ModItems.CELESTIUM_CRYSTAL.get());
                        output.accept(ModItems.XENORITE_CRYSTAL.get());
                        output.accept(ModItems.PRISMARA_CRYSTAL.get());
                        output.accept(ModItems.ECLIPSIUM_CRYSTAL.get());
                        output.accept(ModItems.COSMYRITE_CRYSTAL.get());
                        output.accept(ModItems.ASSEMBLER.get());
                        output.accept(ModItems.ITEM_PORT.get());
                        output.accept(ModItems.ENERGY_PORT.get());
                        output.accept(ModItems.FLUID_PORT.get());
                        output.accept(ModItems.UPGRADE_SPEED.get());
                        output.accept(ModItems.UPGRADE_EFFICIENCY.get());
                        output.accept(ModItems.UPGRADE_FORTUNE_1.get());
                        output.accept(ModItems.UPGRADE_FORTUNE_2.get());
                        output.accept(ModItems.UPGRADE_FORTUNE_3.get());
                        output.accept(ModItems.UPGRADE_PARALLEL_1.get());
                        output.accept(ModItems.UPGRADE_PARALLEL_2.get());
                        output.accept(ModItems.UPGRADE_PARALLEL_3.get());
                        output.accept(ModItems.ROUGH_GEOTHERMAL_VENT.get());
                        output.accept(ModItems.GEOTHERMAL_VENT.get());
                        output.accept(ModItems.PRISTINE_GEOTHERMAL_VENT.get());
                        output.accept(ModItems.ROUGH_GEOTHERMAL_VENT_WALL.get());
                        output.accept(ModItems.GEOTHERMAL_VENT_WALL.get());
                        output.accept(ModItems.PRISTINE_GEOTHERMAL_VENT_WALL.get());
                    })
                    .build()
    );

    public Assemblytech(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.init();
        ModBlockEntities.init();
        ModItems.init();
        ModMenus.init();
        ModFeatures.init();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(MinerLootRegistries::register);
        modEventBus.addListener(MinerTierConfigRegistries::register);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerPayloads);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(IntegrityMonitor.class);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(VoidMinerStructures::init);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                ModBlockEntities.VOID_MINER_CONTROLLER.get(),
                (be, side) -> be.getOutputHandler());
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                ModBlockEntities.ITEM_PORT.get(),
                (be, side) -> be.getInventory());
        event.registerBlockEntity(
                Capabilities.Energy.BLOCK,
                ModBlockEntities.ENERGY_PORT.get(),
                (be, side) -> be.getEnergy());
        event.registerBlockEntity(
                Capabilities.Fluid.BLOCK,
                ModBlockEntities.FLUID_PORT.get(),
                (be, side) -> be.getTank());
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToClient(FluidPortSyncPayload.TYPE, FluidPortSyncPayload.STREAM_CODEC, FluidPortSyncPayload::handle);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        PlaceStructureCommand.register(event.getDispatcher());
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenus.MINER_LOOT.get(), MinerLootScreen::new);
            event.register(ModMenus.VOID_MINER.get(), VoidMinerStatusScreen::new);
            event.register(ModMenus.ITEM_PORT.get(), ItemPortScreen::new);
            event.register(ModMenus.ENERGY_PORT.get(), EnergyPortScreen::new);
            event.register(ModMenus.FLUID_PORT.get(), FluidPortScreen::new);
        }
    }
}
