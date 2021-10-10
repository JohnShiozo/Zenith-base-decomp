package me.gopro336.zenith.module.render;

import me.gopro336.zenith.Client;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;

public class HUDEditor
extends Module {
    public HUDEditor(String name, Category category) {
        super(name, category);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Client.hudEditor);
    }
}
