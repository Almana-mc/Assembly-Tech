package me.almana.assemblytech.voidminer.screen;

import me.almana.assemblytech.voidminer.menu.VoidMinerStatusMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class VoidMinerStatusScreen extends AbstractContainerScreen<VoidMinerStatusMenu> {

    private static final int WIDTH = 350;
    private static final int HEIGHT = 252;

    private static final int HEADER_H = 30;

    private static final int CHAMBER_X = 10;
    private static final int CHAMBER_Y = 38;
    private static final int CHAMBER_W = 80;
    private static final int CHAMBER_H = 194;

    private static final int OUTPUT_PANEL_X = 96;
    private static final int OUTPUT_PANEL_Y = 38;
    private static final int OUTPUT_SLOT_X = 110;
    private static final int OUTPUT_SLOT_Y = 57;
    private static final int OUTPUT_COLS = 9;
    private static final int OUTPUT_ROWS = 3;

    private static final int MIDDLE_W = 188;

    private static final int INV_PANEL_X = 96;
    private static final int INV_PANEL_Y = 136;
    private static final int INV_SLOT_X = 110;
    private static final int INV_SLOT_Y = 147;

    private static final int DESIGNATOR_PANEL_X = 290;
    private static final int DESIGNATOR_PANEL_Y = 38;
    private static final int DESIGNATOR_PANEL_W = 51;
    private static final int DESIGNATOR_PANEL_H = 76;
    private static final int DESIGNATOR_SLOT_X = 307;
    private static final int DESIGNATOR_SLOT_Y = 68;

    private static final int POWER_X = 290;
    private static final int POWER_Y = 136;
    private static final int POWER_W = 51;
    private static final int POWER_H = 96;
    private static final int POWER_TUBE_X = 307;
    private static final int POWER_TUBE_Y = 145;
    private static final int POWER_TUBE_W = 16;
    private static final int POWER_TUBE_H = 78;

    private static final int COLOR_VOID = 0xFF05070B;
    private static final int COLOR_SHELL = 0xFF0A0E16;
    private static final int COLOR_HEADER = 0xFF131A25;
    private static final int COLOR_PANEL = 0xFF0D131E;
    private static final int COLOR_PANEL_DARK = 0xFF070A10;
    private static final int COLOR_CHAMBER = 0xFF02040A;
    private static final int COLOR_RIM = 0xFF2A3445;
    private static final int COLOR_RIM_BRIGHT = 0xFF3B4A64;
    private static final int COLOR_SLOT = 0xFF0A0D13;
    private static final int COLOR_SLOT_RIM = 0xFF232C3B;
    private static final int COLOR_TEXT = 0xFFD8E2EE;
    private static final int COLOR_TEXT_DIM = 0xFF7A879B;
    private static final int COLOR_TEXT_MUTE = 0xFF4A5468;
    private static final int COLOR_ACCENT = 0xFF4CF3FF;
    private static final int COLOR_ACCENT_DARK = 0xFF0B2E35;
    private static final int COLOR_ENERGY = 0xFFFFA030;
    private static final int COLOR_DANGER = 0xFFFF4A5C;
    private static final int COLOR_ROCK = 0xFF4A3825;
    private static final int COLOR_ROCK_DARK = 0xFF1B1209;

    public VoidMinerStatusScreen(VoidMinerStatusMenu menu, Inventory inv, Component title) {
        super(menu, inv, title, WIDTH, HEIGHT);
        this.titleLabelX = -9999;
        this.inventoryLabelY = -9999;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x = leftPos;
        int y = topPos;

        graphics.fill(x - 2, y - 2, x + imageWidth + 2, y + imageHeight + 2, 0xD0000000);
        fillFrame(graphics, x, y, imageWidth, imageHeight, COLOR_SHELL, COLOR_RIM);
        graphics.fill(x + 1, y + 1, x + imageWidth - 1, y + HEADER_H, COLOR_HEADER);
        graphics.fill(x + 46, y + HEADER_H - 1, x + imageWidth - 46, y + HEADER_H, withAlpha(COLOR_ACCENT, 0.55f));

        drawCornerBrackets(graphics, x, y, imageWidth, imageHeight, COLOR_RIM_BRIGHT);
        drawHeader(graphics, x, y);
        drawChamber(graphics, x, y, a);
        drawOutputPanel(graphics, x, y);
        drawDesignatorPanel(graphics, x, y);
        drawInventoryPanel(graphics, x, y);
        drawPowerCell(graphics, x, y);
        drawRivets(graphics, x, y);
    }

    private void drawHeader(GuiGraphicsExtractor graphics, int x, int y) {
        int markX = x + 17;
        int markY = y + 8;
        graphics.fill(markX + 4, markY, markX + 10, markY + 2, COLOR_ACCENT);
        graphics.fill(markX + 1, markY + 3, markX + 13, markY + 11, COLOR_ACCENT_DARK);
        graphics.fill(markX + 5, markY + 4, markX + 9, markY + 9, COLOR_ACCENT);
        graphics.fill(markX + 4, markY + 12, markX + 10, markY + 14, COLOR_ACCENT);

        graphics.text(font, Component.literal("XENO-DRILL Mk." + roman(menu.getTier())), x + 36, y + 6, COLOR_TEXT, false);
        graphics.text(font, Component.translatable("screen.assemblytech.void_miner.header_sub"), x + 36, y + 17, COLOR_TEXT_DIM, false);

        drawHeaderProgress(graphics, x, y);

        boolean working = menu.isWorking();
        int bx = x + imageWidth - 10 - 52;
        int by = y + 7;
        fillFrame(graphics, bx, by, 52, 15, working ? COLOR_ACCENT : COLOR_PANEL_DARK, COLOR_ACCENT);
        graphics.fill(bx + 4, by + 5, bx + 8, by + 9, working ? COLOR_SLOT : COLOR_ACCENT);
        graphics.text(font, Component.translatable(working
                ? "screen.assemblytech.void_miner.active"
                : "screen.assemblytech.void_miner.offline"), bx + 10, by + 4, working ? COLOR_SLOT : COLOR_ACCENT, false);
    }

    private void drawHeaderProgress(GuiGraphicsExtractor graphics, int x, int y) {
        int bx = x + 172;
        int bw = 112;
        int by = y + 18;
        int bh = 6;

        int max = menu.getProgressMax();
        float pct = max > 0 ? Mth.clamp((float) menu.getProgress() / max, 0f, 1f) : 0f;
        int fill = Math.round(bw * pct);

        graphics.text(font, Component.translatable("screen.assemblytech.void_miner.mining"), bx, y + 5, COLOR_TEXT_MUTE, false);

        graphics.fill(bx - 1, by - 1, bx + bw + 1, by + bh + 1, COLOR_RIM);
        graphics.fill(bx, by, bx + bw, by + bh, COLOR_CHAMBER);
        if (fill > 0) {
            graphics.fill(bx, by, bx + fill, by + bh, COLOR_ACCENT);
            graphics.fill(bx, by, bx + fill, by + 1, 0xFFFFFFFF);
        }

        String label = Math.round(pct * 100) + "%";
        graphics.text(font, Component.literal(label), bx + bw - font.width(label), y + 5, COLOR_TEXT_DIM, false);
    }

    private void drawChamber(GuiGraphicsExtractor graphics, int x, int y, float a) {
        int cx = x + CHAMBER_X;
        int cy = y + CHAMBER_Y;
        fillFrame(graphics, cx, cy, CHAMBER_W, CHAMBER_H, COLOR_CHAMBER, COLOR_RIM);
        graphics.fill(cx + 1, cy + 1, cx + CHAMBER_W - 1, cy + CHAMBER_H - 1, COLOR_VOID);

        for (int i = 0; i < 34; i++) {
            int sx = cx + 4 + Math.floorMod(i * 37, CHAMBER_W - 9);
            int sy = cy + 5 + Math.floorMod(i * 29, CHAMBER_H - 12);
            int color = i % 6 == 0 ? 0xFFE9FFFF : 0xFFB9CAD8;
            graphics.fill(sx, sy, sx + 1, sy + 1, withAlpha(color, 0.55f));
        }
        for (int gy = cy + 19; gy < cy + CHAMBER_H - 22; gy += 15) {
            graphics.fill(cx + 6, gy, cx + CHAMBER_W - 6, gy + 1, withAlpha(COLOR_RIM, 0.18f));
        }

        int t = (int) (minecraft.level == null ? 0 : minecraft.level.getGameTime());
        int wobX = Math.round(Mth.sin((t + a) * 0.08f) * 2f);
        int wobY = Math.round(Mth.cos((t + a) * 0.1f) * 2f);
        int ax = cx + CHAMBER_W / 2 + wobX;
        int ay = cy + CHAMBER_H / 2 + 25 + wobY;
        drawAsteroid(graphics, ax, ay);
        drawEmitter(graphics, cx, cy, menu.isWorking(), t);

        if (menu.isWorking()) {
            drawLaser(graphics, cx, cy, ax, ay, t);
        }

        int scanY = cy + 6 + Math.floorMod(t, CHAMBER_H - 12);
        graphics.fill(cx + 2, scanY, cx + CHAMBER_W - 2, scanY + 1, withAlpha(COLOR_ACCENT, 0.18f));
        graphics.fill(cx + 2, cy + 2, cx + CHAMBER_W - 2, cy + 3, withAlpha(0xFFFFFFFF, 0.05f));
        drawChamberChrome(graphics, cx, cy);
    }

    private void drawAsteroid(GuiGraphicsExtractor graphics, int ax, int ay) {
        graphics.fill(ax - 20, ay - 9, ax + 20, ay + 13, COLOR_ROCK_DARK);
        graphics.fill(ax - 15, ay - 17, ax + 12, ay + 19, COLOR_ROCK);
        graphics.fill(ax - 24, ay - 4, ax + 24, ay + 8, COLOR_ROCK);
        graphics.fill(ax - 9, ay - 20, ax + 17, ay + 16, 0xFF5A4632);
        graphics.fill(ax - 18, ay - 12, ax - 7, ay - 5, 0xFF2D2116);
        graphics.fill(ax + 7, ay - 8, ax + 17, ay - 2, 0xFF2D2116);
        graphics.fill(ax - 5, ay + 9, ax + 9, ay + 15, 0xFF2D2116);
        graphics.fill(ax - 23, ay + 1, ax - 16, ay + 6, 0xFF3B2A19);
        graphics.fill(ax + 16, ay + 1, ax + 25, ay + 8, 0xFF3B2A19);
        graphics.fill(ax - 13, ay + 4, ax - 10, ay + 7, COLOR_ACCENT);
        graphics.fill(ax + 9, ay + 6, ax + 11, ay + 8, 0xFF80FFE8);
        graphics.fill(ax, ay - 13, ax + 3, ay - 11, 0xFFFF80D8);
        graphics.fill(ax - 2, ay - 1, ax + 2, ay + 2, 0xFF6D5438);
    }

    private void drawEmitter(GuiGraphicsExtractor graphics, int cx, int cy, boolean working, int tick) {
        int ex = cx + CHAMBER_W / 2;
        graphics.fill(ex - 15, cy + 1, ex + 16, cy + 10, COLOR_PANEL);
        graphics.fill(ex - 11, cy + 9, ex + 12, cy + 14, COLOR_SLOT);
        graphics.fill(ex - 3, cy + 13, ex + 4, cy + 18, working ? COLOR_ACCENT : COLOR_RIM);
        graphics.fill(ex - 19, cy + 2, ex - 17, cy + 11, COLOR_RIM_BRIGHT);
        graphics.fill(ex + 17, cy + 2, ex + 19, cy + 11, COLOR_RIM_BRIGHT);
        if (working && tick % 14 < 7) {
            graphics.fill(ex - 1, cy + 15, ex + 2, cy + 17, 0xFFFFFFFF);
        }
    }

    private void drawLaser(GuiGraphicsExtractor graphics, int cx, int cy, int ax, int ay, int tick) {
        int ex = cx + CHAMBER_W / 2;
        int startY = cy + 18;
        int endY = ay - 17;
        float pulse = tick % 12 < 6 ? 1f : 0.72f;
        graphics.fill(ex - 5, startY, ex + 6, endY, withAlpha(COLOR_ACCENT, 0.16f * pulse));
        graphics.fill(ex - 2, startY, ex + 3, endY, withAlpha(COLOR_ACCENT, 0.86f * pulse));
        graphics.fill(ex, startY, ex + 1, endY, 0xFFFFFFFF);
        graphics.fill(ex - 8, endY - 3, ex + 9, endY + 7, withAlpha(COLOR_ACCENT, 0.32f * pulse));
        graphics.fill(ex - 2, endY, ex + 3, endY + 4, 0xFFFFFFFF);
        graphics.fill(ex - 10, endY - 10, ex - 8, endY - 8, COLOR_ACCENT);
        graphics.fill(ex + 11, endY - 3, ex + 13, endY - 1, 0xFFA88A60);
        graphics.fill(ex - 12, endY + 7, ex - 10, endY + 9, 0xFFA88A60);
        graphics.fill(ex + 4, endY + 13, ex + 6, endY + 15, COLOR_ACCENT);
        graphics.fill(ex, endY - 18, ex + 1, endY - 12, COLOR_ACCENT);
        graphics.fill(ex - 14, endY + 1, ex - 13, endY + 7, COLOR_ACCENT);
        graphics.fill(ex + 12, endY + 1, ex + 13, endY + 7, COLOR_ACCENT);
    }

    private void drawChamberChrome(GuiGraphicsExtractor graphics, int cx, int cy) {
        corner(graphics, cx + 1, cy + 1, 10, 10, COLOR_ACCENT, true, true);
        corner(graphics, cx + CHAMBER_W - 11, cy + 1, 10, 10, COLOR_ACCENT, false, true);
        corner(graphics, cx + 1, cy + CHAMBER_H - 11, 10, 10, COLOR_ACCENT, true, false);
        corner(graphics, cx + CHAMBER_W - 11, cy + CHAMBER_H - 11, 10, 10, COLOR_ACCENT, false, false);
    }

    private void drawOutputPanel(GuiGraphicsExtractor graphics, int x, int y) {
        int px = x + OUTPUT_PANEL_X;
        int py = y + OUTPUT_PANEL_Y;
        fillFrame(graphics, px, py, MIDDLE_W, 76, COLOR_PANEL, COLOR_RIM);
        graphics.fill(px + 42, py, px + 146, py + 1, withAlpha(COLOR_ACCENT, 0.55f));
        graphics.text(font, Component.translatable("screen.assemblytech.void_miner.output_buffer"), px + 8, py + 5, COLOR_TEXT, false);
        String count = countFilledOutputSlots() + "/27";
        graphics.text(font, Component.literal(count), px + MIDDLE_W - 8 - font.width(count), py + 5, COLOR_TEXT_DIM, false);

        for (int row = 0; row < OUTPUT_ROWS; row++) {
            for (int col = 0; col < OUTPUT_COLS; col++) {
                drawSlot(graphics, x + OUTPUT_SLOT_X + col * 18, y + OUTPUT_SLOT_Y + row * 18);
            }
        }
    }

    private int countFilledOutputSlots() {
        int count = 0;
        for (int i = 0; i < 27; i++) {
            if (menu.getSlot(i).hasItem()) count++;
        }
        return count;
    }

    private void drawDesignatorPanel(GuiGraphicsExtractor graphics, int x, int y) {
        int px = x + DESIGNATOR_PANEL_X;
        int py = y + DESIGNATOR_PANEL_Y;
        fillFrame(graphics, px, py, DESIGNATOR_PANEL_W, DESIGNATOR_PANEL_H, COLOR_PANEL, COLOR_RIM);
        graphics.text(font, Component.translatable("screen.assemblytech.void_miner.designator"), px + 5, py + 5, COLOR_TEXT_DIM, false);
        drawSlot(graphics, x + DESIGNATOR_SLOT_X, y + DESIGNATOR_SLOT_Y);
    }

    private void drawInventoryPanel(GuiGraphicsExtractor graphics, int x, int y) {
        int px = x + INV_PANEL_X;
        int py = y + INV_PANEL_Y;
        fillFrame(graphics, px, py, MIDDLE_W, 96, COLOR_PANEL_DARK, COLOR_RIM);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlot(graphics, x + INV_SLOT_X + col * 18, y + INV_SLOT_Y + row * 18);
            }
        }
        for (int col = 0; col < 9; col++) {
            drawSlot(graphics, x + INV_SLOT_X + col * 18, y + INV_SLOT_Y + 58);
        }
    }

    private void drawPowerCell(GuiGraphicsExtractor graphics, int x, int y) {
        int px = x + POWER_X;
        int py = y + POWER_Y;
        fillFrame(graphics, px, py, POWER_W, POWER_H, COLOR_PANEL, COLOR_RIM);

        int cap = menu.getCapacity();
        int energy = menu.getEnergy();
        float pct = cap > 0 ? Mth.clamp((float) energy / cap, 0f, 1f) : 0f;
        int fill = Math.round(POWER_TUBE_H * pct);
        int tx = x + POWER_TUBE_X;
        int ty = y + POWER_TUBE_Y;
        graphics.fill(tx, ty, tx + POWER_TUBE_W, ty + POWER_TUBE_H, COLOR_CHAMBER);
        graphics.fill(tx, ty, tx + POWER_TUBE_W, ty + 1, COLOR_RIM);
        graphics.fill(tx, ty + POWER_TUBE_H - fill, tx + POWER_TUBE_W, ty + POWER_TUBE_H, COLOR_ENERGY);
        if (fill > 1) {
            graphics.fill(tx + 1, ty + POWER_TUBE_H - fill, tx + 3, ty + POWER_TUBE_H, 0xFFFFE0A8);
            graphics.fill(tx, ty + POWER_TUBE_H - fill, tx + POWER_TUBE_W, ty + POWER_TUBE_H - fill + 1, 0xFFFFFFFF);
        }

        for (int i = 0; i <= 10; i++) {
            int sy = ty + POWER_TUBE_H - i * POWER_TUBE_H / 10;
            int len = i % 2 == 0 ? 6 : 4;
            graphics.fill(tx, sy, tx + len, sy + 1, COLOR_TEXT_MUTE);
        }

    }

    private void drawRivets(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x + 1, y + imageHeight - 10, x + imageWidth - 1, y + imageHeight - 9, COLOR_RIM);
        for (int i = 0; i < 15; i++) {
            int rx = x + 13 + i * 23;
            int ry = y + imageHeight - 6;
            graphics.fill(rx, ry, rx + 3, ry + 3, COLOR_SLOT_RIM);
            graphics.fill(rx + 1, ry + 1, rx + 2, ry + 2, COLOR_PANEL_DARK);
        }
    }

    private void drawSlot(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x - 1, y - 1, x + 17, y + 17, COLOR_SLOT_RIM);
        graphics.fill(x, y, x + 16, y + 16, COLOR_SLOT);
        graphics.fill(x + 1, y + 1, x + 15, y + 2, 0xFF111A27);
        graphics.fill(x + 1, y + 1, x + 2, y + 15, 0xFF111A27);
    }

    private void fillFrame(GuiGraphicsExtractor graphics, int x, int y, int w, int h, int bg, int rim) {
        graphics.fill(x, y, x + w, y + h, rim);
        graphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, bg);
        graphics.fill(x + 1, y + 1, x + w - 1, y + 2, withAlpha(0xFFFFFFFF, 0.05f));
        graphics.fill(x + 1, y + h - 2, x + w - 1, y + h - 1, 0xFF000000);
    }

    private void drawCornerBrackets(GuiGraphicsExtractor graphics, int x, int y, int w, int h, int color) {
        corner(graphics, x, y, 16, 16, color, true, true);
        corner(graphics, x + w - 16, y, 16, 16, color, false, true);
        corner(graphics, x, y + h - 16, 16, 16, color, true, false);
        corner(graphics, x + w - 16, y + h - 16, 16, 16, color, false, false);
    }

    private void corner(GuiGraphicsExtractor graphics, int x, int y, int w, int h, int color, boolean left, boolean top) {
        int hx0 = left ? x : x + w - 8;
        int hx1 = left ? x + 8 : x + w;
        int hy = top ? y : y + h - 1;
        int vx = left ? x : x + w - 1;
        int vy0 = top ? y : y + h - 8;
        int vy1 = top ? y + 8 : y + h;
        graphics.fill(hx0, hy, hx1, hy + 1, color);
        graphics.fill(vx, vy0, vx + 1, vy1, color);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        int tx = leftPos + POWER_X;
        int ty = topPos + POWER_Y;
        if (mouseX >= tx && mouseX < tx + POWER_W && mouseY >= ty && mouseY < ty + POWER_H) {
            graphics.setTooltipForNextFrame(font, powerTooltip(), Optional.empty(), ItemStack.EMPTY, mouseX, mouseY);
        }
    }

    private List<Component> powerTooltip() {
        int energy = menu.getEnergy();
        int capacity = menu.getCapacity();
        int draw = menu.getEnergyPerTick();
        return List.of(
                Component.translatable("screen.assemblytech.void_miner.power_tooltip").withStyle(ChatFormatting.AQUA),
                Component.translatable("screen.assemblytech.void_miner.power_stored", formatNumber(energy)).withStyle(ChatFormatting.GRAY),
                Component.translatable("screen.assemblytech.void_miner.power_max", formatNumber(capacity)).withStyle(ChatFormatting.GRAY),
                Component.translatable("screen.assemblytech.void_miner.power_consumption", formatNumber(draw)).withStyle(ChatFormatting.GRAY),
                Component.translatable("screen.assemblytech.void_miner.power_runtime", runtimeText(energy, draw)).withStyle(ChatFormatting.DARK_GRAY)
        );
    }

    private static String runtimeText(int energy, int draw) {
        if (draw <= 0) return "idle";
        int seconds = energy / Math.max(1, draw * 20);
        return seconds + "s";
    }

    private static int withAlpha(int argb, float alpha) {
        int a = Math.round(255 * Mth.clamp(alpha, 0f, 1f));
        return (a << 24) | (argb & 0x00FFFFFF);
    }

    private static String formatNumber(int n) {
        return String.format(Locale.ROOT, "%,d", n);
    }

    private static String formatCompact(int n) {
        if (n >= 1_000_000) return String.format(Locale.ROOT, "%.1fM", n / 1_000_000f);
        if (n >= 1_000) return Math.round(n / 1_000f) + "k";
        return Integer.toString(n);
    }

    private static String roman(int n) {
        return switch (n) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            default -> Integer.toString(n);
        };
    }
}
