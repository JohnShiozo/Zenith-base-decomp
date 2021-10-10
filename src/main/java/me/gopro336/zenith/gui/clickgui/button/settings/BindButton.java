package me.gopro336.zenith.gui.clickgui.button.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.gopro336.zenith.gui.clickgui.button.SettingButton;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.util.font.FontUtil;
import org.lwjgl.input.Keyboard;

public class BindButton
extends SettingButton {
    private final Module module;
    private boolean binding;

    public BindButton(Module module, int x, int y, int w, int h) {
        super(module, x, y, w, h);
        this.module = module;
    }

    @Override
    public void render(int mX, int mY) {
        this.drawButton(mX, mY);
        FontUtil.drawStringWithShadow("Bind", this.getX() + 6, this.getY() + 4, new Color(255, 255, 255, 255).getRGB());
        if (this.binding) {
            FontUtil.drawStringWithShadow(ChatFormatting.GRAY + "...", this.getX() + this.getW() - 6 - FontUtil.getStringWidth("..."), this.getY() + 4, new Color(255, 255, 255, 255).getRGB());
        } else {
            try {
                FontUtil.drawStringWithShadow(ChatFormatting.GRAY + Keyboard.getKeyName(this.module.getBind()), this.getX() + this.getW() - 6 - FontUtil.getStringWidth(Keyboard.getKeyName(this.module.getBind())), this.getY() + 4, new Color(255, 255, 255, 255).getRGB());
            }
            catch (Exception e) {
                FontUtil.drawStringWithShadow(ChatFormatting.GRAY + "NONE", this.getX() + this.getW() - 6 - FontUtil.getStringWidth("NONE"), this.getY() + 4, new Color(255, 255, 255, 255).getRGB());
            }
        }
    }

    @Override
    public void mouseDown(int mX, int mY, int mB) {
        if (this.isHover(this.getX(), this.getY(), this.getW(), this.getH() - 1, mX, mY)) {
            this.binding = !this.binding;
        }
    }

    @Override
    public void keyPress(int key) {
        if (this.binding) {
            if (key == 211 || key == 14 || key == 0) {
                this.getModule().setBind(256);
            } else {
                this.getModule().setBind(key);
            }
            this.binding = false;
        }
    }
}
