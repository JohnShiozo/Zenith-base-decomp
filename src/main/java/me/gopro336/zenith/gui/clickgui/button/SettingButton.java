package me.gopro336.zenith.gui.clickgui.button;

import java.awt.Color;
import me.gopro336.zenith.Client;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.SettingManager;
import net.minecraft.client.Minecraft;

public class SettingButton {
    public final Minecraft mc = Minecraft.getMinecraft();
    private final int H;
    private Module module;
    private int X;
    private int Y;
    private int W;

    public SettingButton(Module module, int x, int y, int w, int h) {
        this.module = module;
        this.X = x;
        this.Y = y;
        this.W = w;
        this.H = h;
    }

    public void render(int mX, int mY) {
    }

    public void mouseDown(int mX, int mY, int mB) {
    }

    public void mouseUp(int mX, int mY) {
    }

    public void keyPress(int key) {
    }

    public void close() {
    }

    public void drawButton(int mX, int mY) {
        Client.clickGUI.drawGradient(this.X, this.Y, this.X + this.W, this.Y + this.H, new Color(20, 20, 20, SettingManager.getSetting("ClickGUI", "backround alpha").getIntegerValue()).getRGB(), new Color(20, 20, 20, SettingManager.getSetting("ClickGUI", "backround alpha").getIntegerValue()).getRGB());
        if (this.isHover(this.getX(), this.getY(), this.getW(), this.getH() - 1, mX, mY)) {
            Client.clickGUI.drawGradient(this.X + 1 + 1, (double)this.Y + 0.5, this.X + this.W - 1, (double)(this.Y + this.H) - 0.5, new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB(), new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB());
        } else {
            Client.clickGUI.drawGradient(this.X + 1 + 1, (double)this.Y + 0.5, this.X + this.W - 1, (double)(this.Y + this.H) - 0.5, new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB(), new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB());
        }
    }

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public int getX() {
        return this.X;
    }

    public void setX(int x) {
        this.X = x;
    }

    public int getY() {
        return this.Y;
    }

    public void setY(int y) {
        this.Y = y;
    }

    public int getW() {
        return this.W;
    }

    public int getH() {
        return this.H;
    }

    public boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
}
