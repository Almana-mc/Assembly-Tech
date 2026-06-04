package me.almana.assemblytech.registry;

import me.almana.assemblytech.Assemblytech;
import me.almana.assemblytech.voidminer.menu.VoidMinerStatusMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.function.Supplier;

public final class ModMenus {

    private ModMenus() {}

    public static final Supplier<MenuType<VoidMinerStatusMenu>> VOID_MINER =
            Assemblytech.MENU_TYPES.register("void_miner",
                    () -> IMenuTypeExtension.create(VoidMinerStatusMenu::new));

    public static void init() {}
}
