package me.gopro336.zenith.util.fontrenderer;

import java.awt.Color;

public interface IRenderer {
    public void drawRect(double var1, double var3, double var5, double var7, Color var9);

    public void drawOutline(double var1, double var3, double var5, double var7, float var9, Color var10);

    public void setColor(Color var1);

    public void drawString(int var1, int var2, String var3, Color var4);

    public int getStringWidth(String var1);

    public int getStringHeight(String var1);

    public void drawTriangle(double var1, double var3, double var5, double var7, double var9, double var11, Color var13);

    public void initMask();

    public void useMask();

    public void disableMask();
}
