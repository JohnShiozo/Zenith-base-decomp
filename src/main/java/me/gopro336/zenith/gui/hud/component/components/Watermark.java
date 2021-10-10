package me.gopro336.zenith.gui.hud.component.components;

import java.awt.Color;
import java.awt.Font;
import me.gopro336.zenith.gui.hud.Component;
import me.gopro336.zenith.util.font.FontUtil;
import me.gopro336.zenith.util.fontrenderer.GlyphPage;
import me.gopro336.zenith.util.fontrenderer.GlyphPageFontRenderer;

public class Watermark
extends Component {
    private GlyphPageFontRenderer renderer;

    public Watermark(String name) {
        super(name);
        char[] chars = new char[256];
        for (int i = 0; i < chars.length; ++i) {
            chars[i] = (char)i;
        }
        GlyphPage glyphPage = new GlyphPage(new Font("Comfortaa", 0, 40), true, true);
        this.renderer = new GlyphPageFontRenderer(glyphPage, glyphPage, glyphPage, glyphPage);
        this.setW(this.mc.fontRenderer.getStringWidth("Zenith 1.1-beta"));
        this.setH(this.mc.fontRenderer.FONT_HEIGHT);
        glyphPage.generateGlyphPage(chars);
        glyphPage.setupTexture();
    }

    @Override
    public void render() {
        this.renderer.drawString("Zenith 1.1-beta", this.getX(), this.getY(), -1, true);
        FontUtil.drawStringWithShadow("Zenith 1.1-beta", this.getX(), this.getY() + 140, new Color(255, 255, 255).getRGB());
    }
}
