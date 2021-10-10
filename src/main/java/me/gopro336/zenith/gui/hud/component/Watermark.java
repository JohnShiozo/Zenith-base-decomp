package me.gopro336.zenith.gui.hud.component;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;

public class Watermark
extends Module {
    public final Setting X_ = new Setting("X", this, 100, 0, 8000);
    public final Setting Y_ = new Setting("Y", this, 100, 0, 8000);

    public Watermark(String name, Category category) {
        super(name, category);
        this.addSetting(this.X_);
        this.addSetting(this.Y_);
    }
}
