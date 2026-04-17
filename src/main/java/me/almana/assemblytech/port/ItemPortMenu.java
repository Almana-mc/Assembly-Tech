package me.almana.assemblytech.port;

import me.almana.assemblytech.registry.ModBlocks;
import me.almana.assemblytech.registry.ModMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class ItemPortMenu extends AbstractContainerMenu {

    public static final int ROWS = 4;
    public static final int COLS = 9;
    public static final int PORT_SLOT_COUNT = ROWS * COLS;

    private static final int SLOT_PX = 18;
    private static final int ORIGIN_X = 8;
    private static final int PORT_ORIGIN_Y = 18;
    private static final int PLAYER_INV_Y = 102;
    private static final int HOTBAR_Y = 160;

    private final ItemStacksResourceHandler inventory;
    private final ContainerLevelAccess access;

    public ItemPortMenu(int containerId, Inventory playerInv, ItemPortBlockEntity be) {
        this(containerId, playerInv, be.getInventory(),
                ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()));
    }

    public ItemPortMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInv, new ItemStacksResourceHandler(PORT_SLOT_COUNT), ContainerLevelAccess.NULL);
    }

    private ItemPortMenu(int containerId, Inventory playerInv, ItemStacksResourceHandler inventory, ContainerLevelAccess access) {
        super(ModMenus.ITEM_PORT.get(), containerId);
        this.inventory = inventory;
        this.access = access;
        addPortSlots();
        addPlayerSlots(playerInv);
    }

    private void addPortSlots() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int index = col + row * COLS;
                int x = ORIGIN_X + col * SLOT_PX;
                int y = PORT_ORIGIN_Y + row * SLOT_PX;
                addSlot(new ResourceHandlerSlot(inventory, inventory::set, index, x, y));
            }
        }
    }

    private void addPlayerSlots(Inventory inv) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int index = col + row * 9 + 9;
                int x = ORIGIN_X + col * SLOT_PX;
                int y = PLAYER_INV_Y + row * SLOT_PX;
                addSlot(new Slot(inv, index, x, y));
            }
        }
        for (int col = 0; col < 9; col++) {
            int x = ORIGIN_X + col * SLOT_PX;
            addSlot(new Slot(inv, col, x, HOTBAR_Y));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index < PORT_SLOT_COUNT) {
            if (!moveItemStackTo(stack, PORT_SLOT_COUNT, PORT_SLOT_COUNT + 36, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!moveItemStackTo(stack, 0, PORT_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return access.evaluate((level, pos) -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof ItemPortBlockEntity)) return false;
            if (level.getBlockState(pos).getBlock() != ModBlocks.ITEM_PORT.get()) return false;
            return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
        }, true);
    }
}
