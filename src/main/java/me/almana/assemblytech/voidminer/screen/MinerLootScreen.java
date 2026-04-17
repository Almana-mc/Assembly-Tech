package me.almana.assemblytech.voidminer.screen;

import me.almana.assemblytech.generation.MinerLootTable;
import me.almana.assemblytech.generation.WeightedItem;
import me.almana.assemblytech.voidminer.menu.MinerLootMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MinerLootScreen extends AbstractContainerScreen<MinerLootMenu> {

    private static final int CELL = 18;
    private static final int COLS = 9;
    private static final int GRID_X = 8;
    private static final int GRID_Y = 22;
    private static final int FOOTER = 8;
    private static final int WIDTH = 176;

    private final List<DisplayEntry> visible;

    public MinerLootScreen(MinerLootMenu menu, Inventory inv, Component title) {
        super(menu, inv, title, WIDTH, computeHeight(menu));
        this.visible = buildVisible(menu);
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelY = this.imageHeight;
    }

    private static int computeHeight(MinerLootMenu menu) {
        int n = countVisible(menu);
        int rows = Math.max(1, (n + COLS - 1) / COLS);
        return GRID_Y + rows * CELL + FOOTER;
    }

    private static int countVisible(MinerLootMenu menu) {
        MinerLootTable t = menu.getTable();
        if (t == null) return 0;
        int count = 0;
        for (WeightedItem e : t.entries()) if (e.minTier() <= menu.getTier()) count++;
        return count;
    }

    private static List<DisplayEntry> buildVisible(MinerLootMenu menu) {
        MinerLootTable t = menu.getTable();
        if (t == null) return List.of();
        List<DisplayEntry> out = new ArrayList<>();
        for (WeightedItem e : t.entries()) {
            if (e.minTier() <= menu.getTier()) {
                out.add(new DisplayEntry(e, new ItemStack(e.item().value(), 1)));
            }
        }
        return out;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xC0101010);
        graphics.fill(leftPos + 1, topPos + 1, leftPos + imageWidth - 1, topPos + imageHeight - 1, 0xFF2B2B2B);
        graphics.fill(leftPos + 4, topPos + GRID_Y - 4, leftPos + imageWidth - 4, topPos + GRID_Y - 3, 0xFF1A1A1A);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        graphics.text(font, this.title, leftPos + titleLabelX, topPos + titleLabelY, 0xFFFFFFFF, false);

        DisplayEntry hovered = null;
        for (int i = 0; i < visible.size(); i++) {
            int col = i % COLS;
            int row = i / COLS;
            int x = leftPos + GRID_X + col * CELL;
            int y = topPos + GRID_Y + row * CELL;
            DisplayEntry entry = visible.get(i);
            graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF1A1A1A);
            graphics.item(entry.stack, x, y);
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                hovered = entry;
            }
        }
        if (hovered != null) {
            List<Component> lines = new ArrayList<>(hovered.stack.getTooltipLines(
                    Item.TooltipContext.of(this.minecraft.level),
                    this.minecraft.player,
                    TooltipFlag.NORMAL));
            lines.add(Component.translatable("assemblytech.miner_loot.weight", hovered.weighted.weight())
                    .withStyle(ChatFormatting.AQUA));
            lines.add(Component.translatable("assemblytech.miner_loot.min_tier", hovered.weighted.minTier())
                    .withStyle(ChatFormatting.DARK_GRAY));
            graphics.setTooltipForNextFrame(font, lines, Optional.empty(), hovered.stack, mouseX, mouseY);
        }
    }

    private record DisplayEntry(WeightedItem weighted, ItemStack stack) {}
}
