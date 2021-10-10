package me.gopro336.zenith.module.render;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;

public class CustomFont
extends Module {
    public final Setting fontsise = new Setting("Font Sise", this, 20, 14, 30);
    public final Setting yoffset = new Setting("Y-Offset", this, 0, -8, 8);

    public CustomFont(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.fontsise);
        this.addSetting(this.yoffset);
    }

    @Override
    public void onUpdate() {
    }
}
