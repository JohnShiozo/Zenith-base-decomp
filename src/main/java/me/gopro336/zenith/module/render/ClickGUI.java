package me.gopro336.zenith.module.render;

import java.util.Arrays;
import me.gopro336.zenith.Client;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;

public class ClickGUI
extends Module {
    public final Setting red = new Setting("red", this, 16, 0, 255);
    public final Setting green = new Setting("green", this, 21, 0, 255);
    public final Setting blue = new Setting("blue", this, 30, 0, 255);
    public final Setting alpha = new Setting("alpha", this, 255, 0, 255);
    public final Setting bred = new Setting("button red", this, 104, 0, 255);
    public final Setting bgreen = new Setting("button green", this, 213, 0, 255);
    public final Setting bblue = new Setting("button blue", this, 223, 0, 255);
    public final Setting balpha = new Setting("button alpha", this, 255, 0, 255);
    public final Setting ored = new Setting("outline red", this, 32, 0, 255);
    public final Setting ogreen = new Setting("outline green", this, 32, 0, 255);
    public final Setting oblue = new Setting("outline blue", this, 32, 0, 255);
    public final Setting oalpha = new Setting("outline alpha", this, 255, 0, 255);
    private final Setting thin = new Setting("Thin Outline", (Module)this, false);
    public final Setting backalpha = new Setting("backround alpha", this, 190, 0, 255);
    public final Setting disbuttonalpha = new Setting("disabled alpha", this, 137, 0, 255);

    public ClickGUI(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.red);
        this.addSetting(this.green);
        this.addSetting(this.blue);
        this.addSetting(this.alpha);
        this.addSetting(this.bred);
        this.addSetting(this.bgreen);
        this.addSetting(this.bblue);
        this.addSetting(this.balpha);
        this.addSetting(this.ored);
        this.addSetting(this.ogreen);
        this.addSetting(this.oblue);
        this.addSetting(this.oalpha);
        this.addSetting(this.thin);
        this.addSetting(this.backalpha);
        this.addSetting(this.disbuttonalpha);
        this.setBind(54);
        this.addSetting(new Setting("Text Color", (Module)this, Arrays.asList("White", "Black")));
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Client.clickGUI);
    }
}
