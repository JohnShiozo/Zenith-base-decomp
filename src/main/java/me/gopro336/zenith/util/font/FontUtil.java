package me.gopro336.zenith.util.font;

import java.awt.GraphicsEnvironment;
import me.gopro336.zenith.Client;
import me.gopro336.zenith.setting.SettingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class FontUtil {
    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    public static int getStringWidth(String text) {
        return FontUtil.customFont() ? Client.customFontRenderer.getStringWidth(text) + 3 : fontRenderer.getStringWidth(text);
    }

    public static void drawString(String text, double x, double y, int color) {
        if (FontUtil.customFont()) {
            Client.customFontRenderer.drawString(text, x, y - 1.0 + (double)SettingManager.getSetting("CustomFont", "Y-Offset").getIntegerValue(), color, false);
        } else {
            fontRenderer.drawString(text, (int)x, (int)y, color);
        }
    }

    public static void drawStringWithShadow(String text, double x, double y, int color) {
        if (FontUtil.customFont()) {
            Client.customFontRenderer.drawStringWithShadow(text, x, y - 1.0 + (double)SettingManager.getSetting("CustomFont", "Y-Offset").getIntegerValue(), color);
        } else {
            fontRenderer.drawStringWithShadow(text, (float)x, (float)y, color);
        }
    }

    public static void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        if (FontUtil.customFont()) {
            Client.customFontRenderer.drawCenteredStringWithShadow(text, x, y - 1.0f + (float)SettingManager.getSetting("CustomFont", "Y-Offset").getIntegerValue(), color);
        } else {
            fontRenderer.drawStringWithShadow(text, x - (float)fontRenderer.getStringWidth(text) / 2.0f, y, color);
        }
    }

    public static void drawCenteredString(String text, float x, float y, int color) {
        if (FontUtil.customFont()) {
            Client.customFontRenderer.drawCenteredString(text, x, y - 1.0f + (float)SettingManager.getSetting("CustomFont", "Y-Offset").getIntegerValue(), color);
        } else {
            fontRenderer.drawString(text, (int)(x - (float)(FontUtil.getStringWidth(text) / 2)), (int)y, color);
        }
    }

    public static int getFontHeight() {
        return FontUtil.customFont() ? Client.customFontRenderer.fontHeight / 2 - 1 : FontUtil.fontRenderer.FONT_HEIGHT;
    }

    public static boolean validateFont(String font) {
        for (String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if (!s.equals(font)) continue;
            return true;
        }
        return false;
    }

    private static boolean customFont() {
        return Client.moduleManager.getModule("CustomFont").isEnabled();
    }
}
