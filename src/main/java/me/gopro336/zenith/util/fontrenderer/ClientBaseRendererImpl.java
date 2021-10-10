package me.gopro336.zenith.util.fontrenderer;

import java.awt.Color;
import me.gopro336.zenith.util.fontrenderer.GLUtil;
import me.gopro336.zenith.util.fontrenderer.GlyphPageFontRenderer;
import me.gopro336.zenith.util.fontrenderer.IRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL41;

public class ClientBaseRendererImpl
implements IRenderer {
    private GlyphPageFontRenderer renderer;

    public ClientBaseRendererImpl(GlyphPageFontRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void drawRect(double x, double y, double w, double h, Color c) {
        GLUtil.drawRect(7, x / 2.0, y / 2.0, x / 2.0 + w / 2.0, y / 2.0 + h / 2.0, GLUtil.toRGBA(c));
    }

    @Override
    public void drawOutline(double x, double y, double w, double h, float lineWidth, Color c) {
        GL11.glLineWidth(lineWidth);
        GLUtil.drawRect(2, x / 2.0, y / 2.0, x / 2.0 + w / 2.0, y / 2.0 + h / 2.0, GLUtil.toRGBA(c));
    }

    @Override
    public void setColor(Color c) {
        GLUtil.setColor(c);
    }

    @Override
    public void drawString(int x, int y, String text, Color color) {
        this.renderer.drawString(text, (float)x / 2.0f, (float)y / 2.0f, GLUtil.toRGBA(color), false);
    }

    @Override
    public int getStringWidth(String str) {
        return this.renderer.getStringWidth(str) * 2;
    }

    @Override
    public int getStringHeight(String str) {
        return this.renderer.getFontHeight() * 2;
    }

    @Override
    public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
    }

    @Override
    public void initMask() {
        GL41.glClearDepthf(1.0f);
        GL11.glClear(256);
        GL11.glColorMask(false, false, false, false);
        GL11.glDepthFunc(513);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
    }

    @Override
    public void useMask() {
        GL11.glColorMask(true, true, true, true);
        GL11.glDepthMask(true);
        GL11.glDepthFunc(514);
    }

    @Override
    public void disableMask() {
        GL41.glClearDepthf(1.0f);
        GL11.glClear(1280);
        GL11.glDisable(2929);
        GL11.glDepthFunc(515);
        GL11.glDepthMask(false);
    }
}
