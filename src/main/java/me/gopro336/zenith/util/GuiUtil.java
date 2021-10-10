package me.gopro336.zenith.util;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public final class GuiUtil {
    public static void drawRect(int x, int y, int w, int h, int c) {
        Gui.drawRect(x, y, x + w, y + h, c);
    }

    public static void drawRect(int x, int y, int w, int h, int c, boolean outline, int outlineC) {
        if (outline) {
            GuiUtil.drawRect(x - 1, y - 1, w + 2, h + 2, outlineC);
        }
        GuiUtil.drawRect(x, y, w, h, c);
    }

    public static void drawSmoothRect(int x, int y, int w, int h, int smoothness, int c) {
        Color color = new Color(c);
        GuiUtil.drawRegularPolygon(x + smoothness, y + smoothness, smoothness, smoothness * 5, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GuiUtil.drawRegularPolygon(x + w - smoothness, y + smoothness, smoothness, smoothness * 5, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GuiUtil.drawRegularPolygon(x + smoothness, y + h - smoothness, smoothness, smoothness * 5, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GuiUtil.drawRegularPolygon(x + w - smoothness, y + h - smoothness, smoothness, smoothness * 5, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GuiUtil.drawRect(x + smoothness, y, w - smoothness * 2, h, c);
        GuiUtil.drawRect(x, y + smoothness, w, h - smoothness * 2, c);
    }

    public static void drawSmoothRect(int x, int y, int w, int h, int smoothness, int c, boolean outline, int outlineC) {
        if (outline) {
            GuiUtil.drawSmoothRect(x - 1, y - 1, w + 2, h + 2, smoothness, outlineC);
        }
        GuiUtil.drawSmoothRect(x, y, w, h, smoothness, c);
    }

    public static void drawRegularPolygon(double x, double y, int radius, int sides, int r, int g, int b, int a) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GlStateManager.color((float)r / 255.0f, (float)g / 255.0f, (float)b / 255.0f, (float)a / 255.0f);
        Tessellator.getInstance().getBuffer().begin(6, DefaultVertexFormats.POSITION);
        Tessellator.getInstance().getBuffer().pos(x, y, 0.0).endVertex();
        for (int i = 0; i <= sides; ++i) {
            double angle = Math.PI * 2 * (double)i / (double)sides + Math.toRadians(180.0);
            Tessellator.getInstance().getBuffer().pos(x + Math.sin(angle) * (double)radius, y + Math.cos(angle) * (double)radius, 0.0).endVertex();
        }
        Tessellator.getInstance().draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
}
