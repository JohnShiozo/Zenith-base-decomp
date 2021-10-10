package me.gopro336.zenith.gui.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import me.gopro336.zenith.Client;
import me.gopro336.zenith.gui.clickgui.button.ModuleButton;
import me.gopro336.zenith.gui.clickgui.button.SettingButton;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.module.render.ClickGUI;
import me.gopro336.zenith.setting.SettingManager;
import me.gopro336.zenith.util.RenderUtil;
import me.gopro336.zenith.util.font.FontUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

public class Window {
    private int bottom;
    private int place;
    private int scroll;
    private final ArrayList<ModuleButton> buttons = new ArrayList();
    private final Category category;
    private final int W;
    private final int H;
    private final ArrayList<ModuleButton> buttonsBeforeClosing = new ArrayList();
    private int X;
    private int Y;
    private int dragX;
    private int dragY;
    private boolean open = true;
    private boolean dragging;
    private int showingButtonCount;
    private boolean opening;
    private boolean closing;
    private ClickGUI Click;

    public Window(Category category, int x, int y, int w, int h) {
        this.category = category;
        this.X = x;
        this.Y = y;
        this.W = w;
        this.H = h;
        int yOffset = this.Y + this.H;
        for (Module module : Client.moduleManager.getModules(category)) {
            ModuleButton button = new ModuleButton(module, this.X, yOffset, this.W, this.H);
            this.buttons.add(button);
            yOffset += this.H;
        }
        this.showingButtonCount = this.buttons.size();
    }

    public void render(int mX, int mY) {
        if (this.dragging) {
            this.X = this.dragX + mX;
            this.Y = this.dragY + mY;
        }
        Gui.drawRect(this.X, this.Y, this.X + this.W, this.Y + this.H - 1, new Color(SettingManager.getSetting("ClickGUI", "red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "alpha").getIntegerValue()).getRGB());
        Client.clickGUI.drawGradient(this.X, this.Y + this.H - 1, this.X + this.W, this.Y + this.H, new Color(20, 20, 20, SettingManager.getSetting("ClickGUI", "backround alpha").getIntegerValue()).getRGB(), new Color(20, 20, 20, SettingManager.getSetting("ClickGUI", "backround alpha").getIntegerValue()).getRGB());
        if (SettingManager.getSetting("ClickGUI", "Text Color").getEnumValue().equalsIgnoreCase("white")) {
            FontUtil.drawString(this.category.getName(), this.X + 4, this.Y + 4, new Color(255, 255, 255, 255).getRGB());
        } else {
            FontUtil.drawString(this.category.getName(), this.X + 4, this.Y + 4, new Color(29, 29, 29, 232).getRGB());
        }
        if (this.open || this.opening || this.closing) {
            int modY = this.Y + this.H;
            int moduleRenderCount = 0;
            for (ModuleButton moduleButton : this.buttons) {
                if (++moduleRenderCount >= this.showingButtonCount + 1) continue;
                moduleButton.setX(this.X);
                moduleButton.setY(modY);
                moduleButton.render(mX, mY);
                if (!moduleButton.isOpen() && this.opening && this.buttonsBeforeClosing.contains(moduleButton)) {
                    moduleButton.processRightClick();
                }
                modY += this.H;
                if (!moduleButton.isOpen() && !moduleButton.isOpening() && !moduleButton.isClosing()) continue;
                int settingRenderCount = 0;
                for (SettingButton settingButton : moduleButton.getButtons()) {
                    if (++settingRenderCount >= moduleButton.getShowingModuleCount() + 1) continue;
                    settingButton.setX(this.X);
                    settingButton.setY(modY);
                    settingButton.render(mX, mY);
                    modY += this.H;
                }
            }
            if (SettingManager.getSetting("ClickGUI", "Thin Outline").getBooleanValue()) {
                RenderUtil.drawRectOutline(this.X, this.Y, this.X + this.W, modY, 0.5, new Color(SettingManager.getSetting("ClickGUI", "outline red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "outline green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "outline blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "outline alpha").getIntegerValue()).getRGB());
            } else {
                RenderUtil.drawRectOutline(this.X, this.Y, this.X + this.W, modY, 1.0, new Color(SettingManager.getSetting("ClickGUI", "outline red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "outline green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "outline blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "outline alpha").getIntegerValue()).getRGB());
            }
        }
        if (this.opening) {
            ++this.showingButtonCount;
            if (this.showingButtonCount == this.buttons.size()) {
                this.opening = false;
                this.open = true;
                this.buttonsBeforeClosing.clear();
            }
        }
        if (this.closing) {
            --this.showingButtonCount;
            if (this.showingButtonCount == 0 || this.showingButtonCount == 1) {
                this.closing = false;
                this.open = false;
            }
        }
    }

    public void mouseDown(int mX, int mY, int mB) {
        if (this.isHover(this.X, this.Y, this.W, this.H, mX, mY)) {
            if (mB == 0) {
                this.dragging = true;
                this.dragX = this.X - mX;
                this.dragY = this.Y - mY;
            } else if (mB == 1) {
                if (this.open && !this.opening && !this.closing) {
                    this.showingButtonCount = this.buttons.size();
                    this.closing = true;
                    for (ModuleButton button : this.buttons) {
                        if (!button.isOpen()) continue;
                        button.processRightClick();
                        this.buttonsBeforeClosing.add(button);
                    }
                } else if (!(this.open || this.opening || this.closing)) {
                    this.showingButtonCount = 1;
                    this.opening = true;
                }
            }
        }
        if (this.open) {
            for (ModuleButton button : this.buttons) {
                button.mouseDown(mX, mY, mB);
            }
        }
    }

    public void mouseUp(int mX, int mY) {
        this.dragging = false;
        if (this.open) {
            for (ModuleButton button : this.buttons) {
                button.mouseUp(mX, mY);
            }
        }
    }

    public void keyPress(int key) {
        if (this.open) {
            for (ModuleButton button : this.buttons) {
                button.keyPress(key);
            }
        }
    }

    public void close() {
        for (ModuleButton button : this.buttons) {
            button.close();
        }
    }

    private boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }

    public int getY() {
        return this.Y;
    }

    public void setY(int y) {
        this.Y = y;
    }

    private void handleScrolling(int mouseX, int mouseY) {
        this.scroll += Mouse.getDWheel();
    }
}
