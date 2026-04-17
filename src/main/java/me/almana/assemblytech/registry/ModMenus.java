package me.almana.assemblytech.registry;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.port.EnergyPortMenu;
import me.almana.assemblytech.port.FluidPortMenu;
import me.almana.assemblytech.port.ItemPortMenu;
import me.almana.assemblytech.voidminer.menu.MinerLootMenu;
import me.almana.assemblytech.voidminer.menu.VoidMinerStatusMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.function.Supplier;

public final class ModMenus {

    private ModMenus() {}

    public static final Supplier<MenuType<MinerLootMenu>> MINER_LOOT =
            Assemblytech.MENU_TYPES.register("miner_loot",
                    () -> IMenuTypeExtension.create(MinerLootMenu::new));

    public static final Supplier<MenuType<VoidMinerStatusMenu>> VOID_MINER =
            Assemblytech.MENU_TYPES.register("void_miner",
                    () -> IMenuTypeExtension.create(VoidMinerStatusMenu::new));

    public static final Supplier<MenuType<ItemPortMenu>> ITEM_PORT =
            Assemblytech.MENU_TYPES.register("item_port",
                    () -> IMenuTypeExtension.create(ItemPortMenu::new));

    public static final Supplier<MenuType<EnergyPortMenu>> ENERGY_PORT =
            Assemblytech.MENU_TYPES.register("energy_port",
                    () -> IMenuTypeExtension.create(EnergyPortMenu::new));

    public static final Supplier<MenuType<FluidPortMenu>> FLUID_PORT =
            Assemblytech.MENU_TYPES.register("fluid_port",
                    () -> IMenuTypeExtension.create(FluidPortMenu::new));

    public static void init() {}
}
