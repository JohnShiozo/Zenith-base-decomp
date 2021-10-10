package me.gopro336.zenith.gui.hud;

import net.minecraft.client.Minecraft;

public class Component {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private final String name;
    private int x;
    private int y;
    private int w;
    private int h;
    private boolean showing;

    public Component(String name) {
        this.name = name;
    }

    public void render() {
    }

    public String getName() {
        return this.name;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return this.w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return this.h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public boolean isShowing() {
        return this.showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }
}
