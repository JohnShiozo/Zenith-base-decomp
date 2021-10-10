package me.gopro336.zenith.gui.hud;

import java.util.ArrayList;
import me.gopro336.zenith.gui.hud.Component;
import me.gopro336.zenith.gui.hud.component.components.Watermark;

public enum ComponentManager {
    INSTANCE;

    private final ArrayList<Component> components = new ArrayList();

    private ComponentManager() {
        this.components.add(new Watermark("Watermark"));
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }
}
