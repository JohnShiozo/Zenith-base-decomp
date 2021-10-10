package me.gopro336.zenith.gui.clickgui.button.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.text.DecimalFormat;
import me.gopro336.zenith.Client;
import me.gopro336.zenith.gui.clickgui.button.SettingButton;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import me.gopro336.zenith.setting.SettingManager;
import me.gopro336.zenith.util.font.FontUtil;

public class SliderButton
extends SettingButton {
    private final Setting setting;
    protected boolean dragging = false;
    protected int sliderWidth = 0;

    SliderButton(Module module, Setting setting, int X, int Y, int W, int H) {
        super(module, X, Y, W, H);
        this.setting = setting;
    }

    protected void updateSlider(int mouseX) {
    }

    @Override
    public void render(int mX, int mY) {
        this.updateSlider(mX);
        if (this.isHover(this.getX(), this.getY(), this.getW(), this.getH() - 1, mX, mY)) {
            Client.clickGUI.drawGradient(this.getX(), this.getY(), this.getX() + this.getW(), this.getY() + this.getH(), new Color(20, 20, 20, SettingManager.getSetting("ClickGUI", "backround alpha").getIntegerValue()).getRGB(), new Color(20, 20, 20, SettingManager.getSetting("ClickGUI", "backround alpha").getIntegerValue()).getRGB());
            Client.clickGUI.drawGradient(this.getX() + 1 + 1, (double)this.getY() + 0.5, (double)(this.getX() + (this.sliderWidth + 6)) - 0.5, this.getY() + this.getH() - 1, new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB(), new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB());
        } else {
            Client.clickGUI.drawGradient(this.getX(), this.getY(), this.getX() + this.getW(), this.getY() + this.getH(), new Color(20, 20, 20, SettingManager.getSetting("ClickGUI", "backround alpha").getIntegerValue()).getRGB(), new Color(20, 20, 20, SettingManager.getSetting("ClickGUI", "backround alpha").getIntegerValue()).getRGB());
            Client.clickGUI.drawGradient(this.getX() + 1 + 1, (double)this.getY() + 0.5, (double)(this.getX() + (this.sliderWidth + 6)) - 0.5, this.getY() + this.getH() - 1, new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB(), new Color(SettingManager.getSetting("ClickGUI", "button red").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button green").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button blue").getIntegerValue(), SettingManager.getSetting("ClickGUI", "button alpha").getIntegerValue()).getRGB());
        }
        FontUtil.drawStringWithShadow(this.setting.getName(), this.getX() + 6, this.getY() + 4, new Color(255, 255, 255, 255).getRGB());
        FontUtil.drawStringWithShadow(ChatFormatting.GRAY + String.valueOf(this.setting.getIntegerValue()), this.getX() + this.getW() - 6 - FontUtil.getStringWidth(String.valueOf(this.setting.getIntegerValue())), this.getY() + 4, new Color(255, 255, 255, 255).getRGB());
    }

    @Override
    public void mouseDown(int mX, int mY, int mB) {
        if (this.isHover(this.getX(), this.getY(), this.getW(), this.getH() - 1, mX, mY)) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseUp(int mouseX, int mouseY) {
        this.dragging = false;
    }

    @Override
    public void close() {
        this.dragging = false;
    }

    public static class IntSlider
    extends SliderButton {
        private final Setting intSetting;

        public IntSlider(Module module, Setting setting, int X, int Y, int W, int H) {
            super(module, setting, X, Y, W, H);
            this.intSetting = setting;
        }

        @Override
        protected void updateSlider(int mouseX) {
            double diff = Math.min(this.getW(), Math.max(0, mouseX - this.getX()));
            double min = this.intSetting.getMinIntegerValue();
            double max = this.intSetting.getMaxIntegerValue();
            this.sliderWidth = (int)((double)(this.getW() - 6) * ((double)this.intSetting.getIntegerValue() - min) / (max - min));
            if (this.dragging) {
                if (diff == 0.0) {
                    this.intSetting.setIntegerValue(this.intSetting.getIntegerValue());
                } else {
                    DecimalFormat format = new DecimalFormat("##");
                    String newValue = format.format(diff / (double)this.getW() * (max - min) + min);
                    this.intSetting.setIntegerValue(Integer.parseInt(newValue));
                }
            }
        }
    }
}
