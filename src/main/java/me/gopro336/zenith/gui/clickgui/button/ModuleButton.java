package me.gopro336.zenith.gui.clickgui.button;

import java.awt.Color;
import java.util.ArrayList;
import me.gopro336.zenith.Client;
import me.gopro336.zenith.gui.clickgui.button.SettingButton;
import me.gopro336.zenith.gui.clickgui.button.settings.BindButton;
import me.gopro336.zenith.gui.clickgui.button.settings.BoolButton;
import me.gopro336.zenith.gui.clickgui.button.settings.EnumButton;
import me.gopro336.zenith.gui.clickgui.button.settings.SliderButton;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import me.gopro336.zenith.setting.SettingManager;
import me.gopro336.zenith.setting.SettingType;
import me.gopro336.zenith.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ModuleButton {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Module module;
    private final ArrayList<SettingButton> buttons = new ArrayList();
    private final int W;
    private final int H;
    private int X;
    private int Y;
    private boolean open;
    private int showingModuleCount;
    private boolean opening;
    private boolean closing;

    public ModuleButton(Module module, int x, int y, int w, int h) {
        this.module = module;
        this.X = x;
        this.Y = y;
        this.W = w;
        this.H = h;
        int n = 0;
        for (Setting setting : Client.settingManager.getSettings(module)) {
            SettingButton settingButton = null;
            if (setting.getType().equals((Object)SettingType.BOOLEAN)) {
                settingButton = new BoolButton(module, setting, this.X, this.Y + this.H + n, this.W, this.H);
            } else if (setting.getType().equals((Object)SettingType.INTEGER)) {
                settingButton = new SliderButton.IntSlider(module, setting, this.X, this.Y + this.H + n, this.W, this.H);
            } else if (setting.getType().equals((Object)SettingType.ENUM)) {
                settingButton = new EnumButton(module, setting, this.X, this.Y + this.H + n, this.W, this.H);
            }
            if (settingButton == null) continue;
            this.buttons.add(settingButton);
            n += this.H;
        }
        this.buttons.add(new BindButton(module, this.X, this.Y + this.H + n, this.W, this.H));
    }

    public void render(int mX, int mY) {
        Client.clickGUI.drawGradient(this.X, this.Y, this.X + this.W, this.Y + this.H, new Color(20, 20, 20, SettingManager.getSetting("ClickGUI", "backround alpha").getIntegerValue()).getRGB(), new Color(20, 20, 20, SettingManager.getSetting("ClickGUI", "backround alpha").getIntegerValue()).getRGB());
        if (this.module.isEnabled()) {
            if (this.isHover(this.X, this.Y, this.W, this.H - 1, mX, mY)) {
                Client.clickGUI.drawGradient(this.X + 1, (double)this.Y + 0.5, this.X + this.W - 1, (double)(this.Y + this.H) - 0.5, new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB(), new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB());
                Client.clickGUI.drawGradient(this.X + 1, (double)this.Y + 0.5, this.X + this.W - 1, (double)(this.Y + this.H) - 0.5, new Color(16, 16, 16, 0).getRGB(), new Color(0, 0, 0, 90).getRGB());
            } else {
                Client.clickGUI.drawGradient(this.X + 1, (double)this.Y + 0.5, this.X + this.W - 1, (double)(this.Y + this.H) - 0.5, new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB(), new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB());
                Client.clickGUI.drawGradient(this.X + 1, (double)this.Y + 0.5, this.X + this.W - 1, (double)(this.Y + this.H) - 0.5, new Color(16, 16, 16, 0).getRGB(), new Color(0, 0, 0, 90).getRGB());
            }
            FontUtil.drawStringWithShadow(this.module.getName(), this.X + 5, this.Y + 4, new Color(255, 255, 255, 232).getRGB());
            FontUtil.drawStringWithShadow("...", this.X + this.W - 3 - FontUtil.getStringWidth("..."), this.Y + 4, new Color(255, 255, 255, 255).getRGB());
        } else {
            if (this.isHover(this.X, this.Y, this.W, this.H - 1, mX, mY)) {
                Client.clickGUI.drawGradient(this.X + 1, (double)this.Y + 0.5, this.X + this.W - 1, (double)(this.Y + this.H) - 0.5, new Color(16, 16, 16, SettingManager.getSetting("ClickGUI", "disabled alpha").getIntegerValue()).getRGB(), new Color(16, 16, 16, SettingManager.getSetting("ClickGUI", "disabled alpha").getIntegerValue()).getRGB());
                Client.clickGUI.drawGradient(this.X + 1, (double)this.Y + 0.5, this.X + this.W - 1, (double)(this.Y + this.H) - 0.5, new Color(16, 16, 16, 0).getRGB(), new Color(0, 0, 0, 90).getRGB());
            } else {
                Client.clickGUI.drawGradient(this.X + 1, (double)this.Y + 0.5, this.X + this.W - 1, (double)(this.Y + this.H) - 0.5, new Color(16, 16, 16, SettingManager.getSetting("ClickGUI", "disabled alpha").getIntegerValue()).getRGB(), new Color(16, 16, 16, SettingManager.getSetting("ClickGUI", "disabled alpha").getIntegerValue()).getRGB());
                Client.clickGUI.drawGradient(this.X + 1, (double)this.Y + 0.5, this.X + this.W - 1, (double)(this.Y + this.H) - 0.5, new Color(16, 16, 16, 0).getRGB(), new Color(0, 0, 0, 90).getRGB());
            }
            FontUtil.drawString(this.module.getName(), this.X + 5, this.Y + 4, new Color(255, 255, 255, 232).getRGB());
            FontUtil.drawStringWithShadow("...", this.X + this.W - 3 - FontUtil.getStringWidth("..."), this.Y + 4, new Color(255, 255, 255, 255).getRGB());
        }
        if (this.opening) {
            ++this.showingModuleCount;
            if (this.showingModuleCount == this.buttons.size()) {
                this.opening = false;
                this.open = true;
            }
        }
        if (this.closing) {
            --this.showingModuleCount;
            if (this.showingModuleCount == 0) {
                this.closing = false;
                this.open = false;
            }
        }
        if (this.isHover(this.X, this.Y, this.W, this.H - 1, mX, mY) && this.module.getDescription() != null && !this.module.getDescription().equals("")) {
            FontUtil.drawStringWithShadow(this.module.getDescription(), 2.0, new ScaledResolution(this.mc).getScaledHeight() - FontUtil.getFontHeight() - 2, new Color(-221985596, true).getRGB());
        }
    }

    public void mouseDown(int mX, int mY, int mB) {
        if (this.isHover(this.X, this.Y, this.W, this.H - 1, mX, mY)) {
            if (mB == 0) {
                this.module.toggle();
                if (this.module.getName().equals("ClickGUI")) {
                    this.mc.displayGuiScreen(null);
                }
            } else if (mB == 1) {
                this.processRightClick();
            }
        }
        if (this.open) {
            for (SettingButton settingButton : this.buttons) {
                settingButton.mouseDown(mX, mY, mB);
            }
        }
    }

    public void mouseUp(int mX, int mY) {
        for (SettingButton settingButton : this.buttons) {
            settingButton.mouseUp(mX, mY);
        }
    }

    public void keyPress(int key) {
        for (SettingButton settingButton : this.buttons) {
            settingButton.keyPress(key);
        }
    }

    public void close() {
        for (SettingButton button : this.buttons) {
            button.close();
        }
    }

    private boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }

    public void setX(int x) {
        this.X = x;
    }

    public void setY(int y) {
        this.Y = y;
    }

    public boolean isOpen() {
        return this.open;
    }

    public Module getModule() {
        return this.module;
    }

    public ArrayList<SettingButton> getButtons() {
        return this.buttons;
    }

    public int getShowingModuleCount() {
        return this.showingModuleCount;
    }

    public boolean isOpening() {
        return this.opening;
    }

    public boolean isClosing() {
        return this.closing;
    }

    public void processRightClick() {
        if (!this.open) {
            this.showingModuleCount = 0;
            this.opening = true;
        } else {
            this.showingModuleCount = this.buttons.size();
            this.closing = true;
        }
    }
}
