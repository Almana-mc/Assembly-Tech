package me.almana.assemblytech.voidminer.menu;

import me.almana.assemblytech.generation.MinerLootRegistries;
import me.almana.assemblytech.generation.MinerLootTable;
import me.almana.assemblytech.registry.ModMenus;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MinerLootMenu extends AbstractContainerMenu {

    @Nullable
    private final MinerLootTable table;
    private final int tier;

    public MinerLootMenu(int containerId, Inventory inv, @Nullable MinerLootTable table, int tier) {
        super(ModMenus.MINER_LOOT.get(), containerId);
        this.table = table;
        this.tier = tier;
    }

    public MinerLootMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(containerId, inv, readTable(inv, buf), buf.readVarInt());
    }

    @Nullable
    private static MinerLootTable readTable(Inventory inv, RegistryFriendlyByteBuf buf) {
        var key = buf.readResourceKey(MinerLootRegistries.MINER_LOOT);
        Registry<MinerLootTable> reg = inv.player.level().registryAccess()
                .lookup(MinerLootRegistries.MINER_LOOT)
                .orElse(null);
        return reg == null ? null : reg.getValue(key);
    }

    @Nullable
    public MinerLootTable getTable() {
        return table;
    }

    public int getTier() {
        return tier;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
