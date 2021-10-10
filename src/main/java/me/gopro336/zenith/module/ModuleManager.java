package me.gopro336.zenith.module;

import java.util.ArrayList;
import java.util.stream.Collectors;
import me.gopro336.zenith.Client;
import me.gopro336.zenith.gui.hud.component.Watermark;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.module.combat.PistonCrystal;
import me.gopro336.zenith.module.exploit.BoatPlaceBypass;
import me.gopro336.zenith.module.exploit.PacketCanceller;
import me.gopro336.zenith.module.movement.Strafe;
import me.gopro336.zenith.module.movement.StrafeBypass;
import me.gopro336.zenith.module.render.ClickGUI;
import me.gopro336.zenith.module.render.CustomFont;
import me.gopro336.zenith.module.render.HUDEditor;

public class ModuleManager {
    private final ArrayList<Module> modules = new ArrayList();

    public ModuleManager() {
        this.modules.add(new ClickGUI("ClickGUI", "Toggle modules by clicking on them", Category.THEME));
        this.modules.add(new CustomFont("CustomFont", "Use a custom font render instead of Minecraft's default", Category.RENDER));
        this.modules.add(new Strafe("Strafe", "speeeeeeeed", Category.MOVEMENT));
        this.modules.add(new StrafeBypass("StrafeBypass", "Bypass toggle for strafe(uses crystal damage)", Category.MOVEMENT));
        this.modules.add(new BoatPlaceBypass("BoatPlaceBypass", "Allows you to place boats outside of water on 2b (dont leave this on)", Category.EXPLOIT));
        this.modules.add(new PacketCanceller("PacketCanceller", "Cancels Packets", Category.EXPLOIT));
        this.modules.add(new PistonCrystal("PistonCrystal", "SIXTIETH's pistonaura", Category.COMBAT));
        this.modules.add(new Watermark("Watermark", Category.HUD));
        this.modules.add(new HUDEditor("HUDEditor", Category.THEME));
    }

    public static void onUpdate() {
        Client.moduleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::onUpdate);
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public Module getModule(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getModules(Category category) {
        ArrayList<Module> mods = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.getCategory().equals((Object)category)) continue;
            mods.add(module);
        }
        return mods;
    }

    public ArrayList<Module> getEnabledModules() {
        return this.modules.stream().filter(Module::isEnabled).collect(Collectors.toCollection(ArrayList::new));
    }
}
