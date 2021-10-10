package me.gopro336.zenith.gui.hud;

import java.awt.Color;
import java.util.ArrayList;
import me.gopro336.zenith.Client;
import me.gopro336.zenith.gui.clickgui.Window;
import me.gopro336.zenith.gui.hud.Component;
import me.gopro336.zenith.gui.hud.ComponentManager;
import me.gopro336.zenith.module.Category;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class HUDEditor
extends GuiScreen {
    private final ArrayList<Window> windows = new ArrayList();
    private boolean dragging;
    private int dragX;
    private int dragY;
    private Component dragComponent;

    public HUDEditor() {
        int xOffset = 3;
        Window window = new Window(Category.HUD, xOffset, 3, 105, 15);
        this.windows.add(window);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.doScroll();
        for (Window window : this.windows) {
            window.render(mouseX, mouseY);
        }
        for (Component component : ComponentManager.INSTANCE.getComponents()) {
            if (this.dragging && this.dragComponent.equals(component)) {
                component.setX(mouseX - this.dragX);
                component.setY(mouseY - this.dragY);
            }
            if (!Client.moduleManager.getModule(component.getName()).isEnabled()) continue;
            Gui.drawRect(component.getX() - 2, component.getY() - 2, component.getX() + component.getW() + 2, component.getY() + component.getH() + 2, this.isHover(component.getX(), component.getY(), component.getW(), component.getH(), mouseX, mouseY) ? new Color(0x72000000, true).getRGB() : new Color(0x4F000000, true).getRGB());
            component.render();
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Window window : this.windows) {
            window.mouseDown(mouseX, mouseY, mouseButton);
        }
        for (Component component : ComponentManager.INSTANCE.getComponents()) {
            if (!Client.moduleManager.getModule(component.getName()).isEnabled() || !this.isHover(component.getX() - 2, component.getY() - 2, component.getW() + 2, component.getH() + 2, mouseX, mouseY)) continue;
            this.dragComponent = component;
            this.dragging = true;
            this.dragX = mouseX - component.getX();
            this.dragY = mouseY - component.getY();
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Window window : this.windows) {
            window.mouseUp(mouseX, mouseY);
        }
        this.dragging = false;
        this.dragComponent = null;
    }

    public void onGuiClosed() {
        this.dragComponent = null;
        this.dragging = false;
        Client.moduleManager.getModule("HUDEditor").disable();
    }

    private void doScroll() {
        block3: {
            int w;
            block2: {
                w = Mouse.getDWheel();
                if (w >= 0) break block2;
                for (Window window : this.windows) {
                    window.setY(window.getY() - 8);
                }
                break block3;
            }
            if (w <= 0) break block3;
            for (Window window : this.windows) {
                window.setY(window.getY() + 8);
            }
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    private boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
}
