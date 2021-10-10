package me.gopro336.zenith.gui.clickgui;

import java.util.ArrayList;
import me.gopro336.zenith.Client;
import me.gopro336.zenith.gui.clickgui.Window;
import me.gopro336.zenith.module.Category;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class ClickGUI
extends GuiScreen {
    private final ArrayList<Window> windows = new ArrayList();

    public ClickGUI() {
        int xOffset = 3;
        for (Category category : Category.values()) {
            Window window = new Window(category, xOffset, 3, 110, 15);
            this.windows.add(window);
            xOffset += 120;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.doScroll();
        for (Window window : this.windows) {
            window.render(mouseX, mouseY);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Window window : this.windows) {
            window.mouseDown(mouseX, mouseY, mouseButton);
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Window window : this.windows) {
            window.mouseUp(mouseX, mouseY);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) {
        for (Window window : this.windows) {
            window.keyPress(keyCode);
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawGradient(double left, double top, double right, double bottom, int startColor, int endColor) {
        this.drawGradientRect((int)left, (int)top, (int)right, (int)bottom, startColor, endColor);
    }

    public void onGuiClosed() {
        for (Window window : this.windows) {
            window.close();
        }
        Client.moduleManager.getModule("ClickGUI").disable();
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
}
