package me.gopro336.zenith.module;

import me.gopro336.zenith.Client;
import me.gopro336.zenith.gui.hud.ComponentManager;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class Module {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private String name;
    private String description;
    private Category category;
    private int bind;
    private boolean enabled;

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void onUpdate() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void enable() {
        this.setEnabled(true);
        this.onEnable();
        MinecraftForge.EVENT_BUS.register(this);
        ComponentManager.INSTANCE.getComponents().stream().filter(c -> c.getName().equals(this.getName())).forEach(c -> c.setShowing(true));
    }

    public void disable() {
        this.setEnabled(false);
        this.onDisable();
        MinecraftForge.EVENT_BUS.unregister(this);
        ComponentManager.INSTANCE.getComponents().stream().filter(c -> c.getName().equals(this.getName())).forEach(c -> c.setShowing(false));
    }

    public void toggle() {
        if (this.isEnabled()) {
            this.disable();
        } else {
            this.enable();
        }
    }

    public boolean nullCheck() {
        return Module.mc.player == null || Module.mc.world == null;
    }

    public void addSetting(Setting setting) {
        Client.settingManager.addSetting(setting);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getBind() {
        return this.bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
