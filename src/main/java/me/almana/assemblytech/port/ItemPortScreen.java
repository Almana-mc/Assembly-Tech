package me.almana.assemblytech.port;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ItemPortScreen extends AbstractContainerScreen<ItemPortMenu> {

    private static final int WIDTH = 176;
    private static final int HEIGHT = 184;

    private static final int COLOR_PANEL = 0xFFC6C6C6;
    private static final int COLOR_BEVEL_LIGHT = 0xFFFFFFFF;
    private static final int COLOR_BEVEL_DARK = 0xFF555555;
    private static final int COLOR_SLOT_FILL = 0xFF8B8B8B;
    private static final int COLOR_SLOT_SHADOW = 0xFF373737;
    private static final int COLOR_TEXT = 0xFF404040;

    public ItemPortScreen(ItemPortMenu menu, Inventory inv, Component title) {
        super(menu, inv, title, WIDTH, HEIGHT);
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = HEIGHT - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x0 = leftPos;
        int y0 = topPos;
        int x1 = leftPos + imageWidth;
        int y1 = topPos + imageHeight;

        graphics.fill(x0, y0, x1, y1, COLOR_PANEL);
        graphics.fill(x0, y0, x1, y0 + 1, COLOR_BEVEL_LIGHT);
        graphics.fill(x0, y0, x0 + 1, y1, COLOR_BEVEL_LIGHT);
        graphics.fill(x0, y1 - 1, x1, y1, COLOR_BEVEL_DARK);
        graphics.fill(x1 - 1, y0, x1, y1, COLOR_BEVEL_DARK);

        for (int row = 0; row < ItemPortMenu.ROWS; row++) {
            for (int col = 0; col < ItemPortMenu.COLS; col++) {
                drawSlot(graphics, x0 + 8 + col * 18, y0 + 18 + row * 18);
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlot(graphics, x0 + 8 + col * 18, y0 + 102 + row * 18);
            }
        }
        for (int col = 0; col < 9; col++) {
            drawSlot(graphics, x0 + 8 + col * 18, y0 + 160);
        }
    }

    private void drawSlot(GuiGraphicsExtractor graphics, int slotX, int slotY) {
        int wx0 = slotX - 1;
        int wy0 = slotY - 1;
        int wx1 = slotX + 17;
        int wy1 = slotY + 17;

        graphics.fill(wx0, wy0, wx1, wy1, COLOR_SLOT_FILL);
        graphics.fill(wx0, wy0, wx1, wy0 + 1, COLOR_SLOT_SHADOW);
        graphics.fill(wx0, wy0, wx0 + 1, wy1, COLOR_SLOT_SHADOW);
        graphics.fill(wx0, wy1 - 1, wx1, wy1, COLOR_BEVEL_LIGHT);
        graphics.fill(wx1 - 1, wy0, wx1, wy1, COLOR_BEVEL_LIGHT);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        graphics.text(font, this.title, leftPos + titleLabelX, topPos + titleLabelY, COLOR_TEXT, false);
        graphics.text(font, this.playerInventoryTitle, leftPos + inventoryLabelX, topPos + inventoryLabelY, COLOR_TEXT, false);
    }
}
