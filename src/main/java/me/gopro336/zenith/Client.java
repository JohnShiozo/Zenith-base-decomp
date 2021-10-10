package me.gopro336.zenith;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Font;
import me.gopro336.zenith.EventHandler;
import me.gopro336.zenith.command.CommandManager;
import me.gopro336.zenith.config.Config;
import me.gopro336.zenith.event.EventManager;
import me.gopro336.zenith.gui.clickgui.ClickGUI;
import me.gopro336.zenith.gui.hud.HUDEditor;
import me.gopro336.zenith.module.ModuleManager;
import me.gopro336.zenith.setting.SettingManager;
import me.gopro336.zenith.util.font.CustomFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid="zenith", name="Zenith", version="b1")
public class Client {
    public static final String name = "Zenith";
    public static final String version = "b1";
    public static final String creator = "336";
    public static ModuleManager moduleManager;
    public static SettingManager settingManager;
    public static CustomFontRenderer customFontRenderer;
    public static ClickGUI clickGUI;
    public static CommandManager commandManager;
    public static final EventManager EVENT_MANAGER;
    public static HUDEditor hudEditor;

    public static void SendMessage(String string) {
        if (Minecraft.getMinecraft().ingameGUI != null || Minecraft.getMinecraft().player == null) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("\u00a77" + ChatFormatting.BLUE + "[ZenithCore]\u00a7f " + ChatFormatting.RESET + string));
        }
    }

    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event) {
        commandManager = new CommandManager();
        settingManager = new SettingManager();
        moduleManager = new ModuleManager();
        customFontRenderer = new CustomFontRenderer(new Font("Verdana", 0, SettingManager.getSetting("CustomFont", "Font Sise").getIntegerValue()), true, false);
        clickGUI = new ClickGUI();
        hudEditor = new HUDEditor();
        Config.loadConfig();
        Runtime.getRuntime().addShutdownHook(new Config());
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    static {
        EVENT_MANAGER = new EventManager();
    }
}
