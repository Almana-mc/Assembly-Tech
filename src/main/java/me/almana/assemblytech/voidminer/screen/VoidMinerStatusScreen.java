package me.almana.assemblytech.voidminer.screen;

import me.almana.assemblytech.voidminer.menu.VoidMinerStatusMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.Locale;

public class VoidMinerStatusScreen extends AbstractContainerScreen<VoidMinerStatusMenu> {

    private static final int WIDTH = 176;
    private static final int HEIGHT = 154;

    private static final int BAR_X = 16;
    private static final int BAR_Y = 22;
    private static final int BAR_W = 30;
    private static final int BAR_H = 96;

    private static final int PROGRESS_X = 32;
    private static final int PROGRESS_Y = 138;
    private static final int PROGRESS_W = 136;
    private static final int PROGRESS_H = 8;

    private static final int COLOR_PANEL = 0xFFC6C6C6;
    private static final int COLOR_BEVEL_LIGHT = 0xFFFFFFFF;
    private static final int COLOR_BEVEL_DARK = 0xFF555555;
    private static final int COLOR_WELL_FILL = 0xFF373737;
    private static final int COLOR_WELL_SHADOW = 0xFF1A1A1A;
    private static final int COLOR_ENERGY = 0xFFE53935;
    private static final int COLOR_ENERGY_HL = 0xFFFF8A80;
    private static final int COLOR_TEXT = 0xFF404040;
    private static final int COLOR_WORKING = 0xFF2E7D32;
    private static final int COLOR_NOT_WORKING = 0xFFC62828;
    private static final int COLOR_PROGRESS_BG = 0xFF8B8B8B;
    private static final int COLOR_PROGRESS_FILL = 0xFF43A047;
    private static final int COLOR_PROGRESS_HL = 0xFF81C784;

    private float prevProgress;
    private float currProgress;
    private int lastProgressMax;
    private float smoothedProgress;
    private boolean initialized;

    public VoidMinerStatusScreen(VoidMinerStatusMenu menu, Inventory inv, Component title) {
        super(menu, inv, title, WIDTH, HEIGHT);
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelY = -9999;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        int pmax = menu.getProgressMax();
        int pcur = menu.getProgress();
        if (!initialized || pmax != lastProgressMax || pcur + 1 < currProgress) {
            prevProgress = pcur;
            currProgress = pcur;
            smoothedProgress = pcur;
            lastProgressMax = pmax;
            initialized = true;
            return;
        }
        prevProgress = currProgress;
        currProgress = pcur;
        lastProgressMax = pmax;
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

        int cap = menu.getCapacity();
        int e = menu.getEnergy();
        if (cap > 0 && e > 0) {
            int fillH = Math.min(BAR_H, (int) ((long) e * BAR_H / cap));
            if (fillH > 0) {
                int fx0 = x0 + BAR_X;
                int fy1 = y0 + BAR_Y + BAR_H;
                int fy0 = fy1 - fillH;
                int fx1 = x0 + BAR_X + BAR_W;
                graphics.fill(fx0, fy0, fx1, fy1, COLOR_ENERGY);
                graphics.fill(fx0, fy0, fx0 + 2, fy1, COLOR_ENERGY_HL);
            }
        }

        int px0 = x0 + PROGRESS_X;
        int py0 = y0 + PROGRESS_Y;
        int px1 = px0 + PROGRESS_W;
        int py1 = py0 + PROGRESS_H;
        graphics.fill(px0 - 1, py0 - 1, px1 + 1, py1 + 1, COLOR_WELL_SHADOW);
        graphics.fill(px0, py0, px1, py1, COLOR_PROGRESS_BG);
        float t = Mth.clamp(a, 0f, 1f);
        smoothedProgress = Mth.lerp(t, prevProgress, currProgress);
        int pmax = menu.getProgressMax();
        float pcur = smoothedProgress;
        if (pmax > 0 && pcur > 0f) {
            float pwf = Math.min((float) PROGRESS_W, pcur * PROGRESS_W / pmax);
            int pw = (int) pwf;
            float frac = pwf - pw;
            if (pw > 0) {
                graphics.fill(px0, py0, px0 + pw, py1, COLOR_PROGRESS_FILL);
                graphics.fill(px0, py0, px0 + pw, py0 + 1, COLOR_PROGRESS_HL);
            }
            if (pw < PROGRESS_W && frac > 0f) {
                int edgeFill = blendAlpha(COLOR_PROGRESS_FILL, frac);
                int edgeHl = blendAlpha(COLOR_PROGRESS_HL, frac);
                graphics.fill(px0 + pw, py0, px0 + pw + 1, py1, edgeFill);
                graphics.fill(px0 + pw, py0, px0 + pw + 1, py0 + 1, edgeHl);
            }
        }
    }

    private static int blendAlpha(int argb, float frac) {
        int baseA = (argb >>> 24) & 0xFF;
        int newA = Math.round(baseA * Mth.clamp(frac, 0f, 1f));
        return (newA << 24) | (argb & 0x00FFFFFF);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        int cap = menu.getCapacity();
        int e = menu.getEnergy();
        int infoX = leftPos + BAR_X + BAR_W + 14;
        int infoY = topPos + BAR_Y;

        graphics.text(font, Component.translatable("screen.assemblytech.port.stored"), infoX, infoY, COLOR_TEXT, false);
        graphics.text(font, Component.literal(formatNumber(e) + " RF"), infoX, infoY + 11, COLOR_TEXT, false);
        graphics.text(font, Component.translatable("screen.assemblytech.port.capacity"), infoX, infoY + 28, COLOR_TEXT, false);
        graphics.text(font, Component.literal(formatNumber(cap) + " RF"), infoX, infoY + 39, COLOR_TEXT, false);

        boolean working = menu.isWorking();
        boolean overCap = menu.isOverUpgradeCap();
        int statusColor = working ? COLOR_WORKING : COLOR_NOT_WORKING;
        Component statusLabel = overCap
                ? Component.translatable("screen.assemblytech.void_miner.too_many_upgrades", menu.getUpgradeMax())
                : Component.translatable(
                        working ? "screen.assemblytech.void_miner.working"
                                : "screen.assemblytech.void_miner.not_working");

        graphics.text(font, Component.translatable("screen.assemblytech.void_miner.consumption"), infoX, infoY + 60, COLOR_TEXT, false);
        graphics.text(font, Component.literal(formatNumber(menu.getEnergyPerTick()) + " RF/t"), infoX, infoY + 71, COLOR_TEXT, false);

        graphics.text(font, Component.translatable("screen.assemblytech.void_miner.status"), infoX, infoY + 88, COLOR_TEXT, false);
        if (overCap) {
            int statusX = leftPos + (imageWidth - font.width(statusLabel)) / 2;
            graphics.text(font, statusLabel, statusX, infoY + 99, statusColor, false);
        } else {
            graphics.text(font, statusLabel, infoX, infoY + 99, statusColor, false);
        }

        int pmax = menu.getProgressMax();
        float percent = pmax > 0 ? smoothedProgress * 100f / pmax : 0f;
        if (percent > 100f) percent = 100f;
        if (percent < 0f) percent = 0f;
        String pct = String.format(Locale.ROOT, "%.1f%%", percent);
        int pctWidth = font.width(pct);
        int pctX = leftPos + PROGRESS_X - 4 - pctWidth;
        int pctY = topPos + PROGRESS_Y;
        graphics.text(font, Component.literal(pct), pctX, pctY, COLOR_TEXT, false);
    }

    private static String formatNumber(int n) {
        return String.format(Locale.ROOT, "%,d", n);
    }
}
