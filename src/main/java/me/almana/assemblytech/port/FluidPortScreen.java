package me.almana.assemblytech.port;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Locale;

public class FluidPortScreen extends AbstractContainerScreen<FluidPortMenu> {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 128;

    private static final int BAR_X = 16;
    private static final int BAR_Y = 22;
    private static final int BAR_W = 30;
    private static final int BAR_H = 96;

    private static final int COLOR_PANEL = 0xFFC6C6C6;
    private static final int COLOR_BEVEL_LIGHT = 0xFFFFFFFF;
    private static final int COLOR_BEVEL_DARK = 0xFF555555;
    private static final int COLOR_WELL_FILL = 0xFF373737;
    private static final int COLOR_WELL_SHADOW = 0xFF1A1A1A;
    private static final int COLOR_TEXT = 0xFF404040;

    public FluidPortScreen(FluidPortMenu menu, Inventory inv, Component title) {
        super(menu, inv, title, WIDTH, HEIGHT);
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelY = -9999;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x0 = leftPos;
        int y0 = topPos;
        int x1 = x0 + imageWidth;
        int y1 = y0 + imageHeight;

        graphics.fill(x0, y0, x1, y1, COLOR_PANEL);
        graphics.fill(x0, y0, x1, y0 + 1, COLOR_BEVEL_LIGHT);
        graphics.fill(x0, y0, x0 + 1, y1, COLOR_BEVEL_LIGHT);
        graphics.fill(x0, y1 - 1, x1, y1, COLOR_BEVEL_DARK);
        graphics.fill(x1 - 1, y0, x1, y1, COLOR_BEVEL_DARK);

        int wx0 = x0 + BAR_X - 1;
        int wy0 = y0 + BAR_Y - 1;
        int wx1 = x0 + BAR_X + BAR_W + 1;
        int wy1 = y0 + BAR_Y + BAR_H + 1;
        graphics.fill(wx0, wy0, wx1, wy1, COLOR_WELL_FILL);
        graphics.fill(wx0, wy0, wx1, wy0 + 1, COLOR_WELL_SHADOW);
        graphics.fill(wx0, wy0, wx0 + 1, wy1, COLOR_WELL_SHADOW);
        graphics.fill(wx0, wy1 - 1, wx1, wy1, COLOR_BEVEL_LIGHT);
        graphics.fill(wx1 - 1, wy0, wx1, wy1, COLOR_BEVEL_LIGHT);

        FluidStack stack = menu.getFluid();
        int cap = menu.getCapacity();
        if (cap > 0 && !stack.isEmpty() && stack.getAmount() > 0) {
            int fillH = Math.min(BAR_H, (int) ((long) stack.getAmount() * BAR_H / cap));
            if (fillH > 0) {
                drawFluid(graphics, x0 + BAR_X, y0 + BAR_Y + BAR_H - fillH, BAR_W, fillH, stack);
            }
        }
    }

    private void drawFluid(GuiGraphicsExtractor graphics, int x, int y, int w, int h, FluidStack stack) {
        FluidModel model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(stack.getFluid().defaultFluidState());
        TextureAtlasSprite sprite = model.stillMaterial().sprite();
        int tint = 0xFFFFFFFF;
        if (model.fluidTintSource() != null) {
            int raw = model.fluidTintSource().colorAsStack(stack);
            tint = (raw & 0xFF000000) != 0 ? raw : (0xFF000000 | raw);
        }
        int tileH = 16;
        int drawn = 0;
        while (drawn < h) {
            int segH = Math.min(tileH, h - drawn);
            int segY = y + h - drawn - segH;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, segY, w, segH, tint);
            drawn += segH;
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        FluidStack stack = menu.getFluid();
        int cap = menu.getCapacity();
        int amt = stack.isEmpty() ? 0 : stack.getAmount();
        int infoX = leftPos + BAR_X + BAR_W + 14;
        int infoY = topPos + BAR_Y;

        Component fluidName = stack.isEmpty()
                ? Component.translatable("screen.assemblytech.port.empty")
                : stack.getHoverName();
        graphics.text(font, fluidName, infoX, infoY, COLOR_TEXT, false);
        graphics.text(font, Component.literal(formatNumber(amt) + " mB"), infoX, infoY + 15, COLOR_TEXT, false);
        graphics.text(font, Component.translatable("screen.assemblytech.port.capacity"), infoX, infoY + 32, COLOR_TEXT, false);
        graphics.text(font, Component.literal(formatNumber(cap) + " mB"), infoX, infoY + 43, COLOR_TEXT, false);
        int pct = cap > 0 ? (int) ((long) amt * 100 / cap) : 0;
        graphics.text(font, Component.literal(pct + "%"), infoX, infoY + 64, COLOR_TEXT, false);
    }

    private static String formatNumber(int n) {
        return String.format(Locale.ROOT, "%,d", n);
    }
}
