package me.gopro336.zenith;

import me.gopro336.zenith.Client;
import me.gopro336.zenith.gui.hud.Component;
import me.gopro336.zenith.gui.hud.ComponentManager;
import me.gopro336.zenith.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class EventHandler {
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().player == null) {
            return;
        }
        Client.moduleManager.onUpdate();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState() || Keyboard.getEventKey() == 0) {
            return;
        }
        for (Module module : Client.moduleManager.getModules()) {
            if (module.getBind() != Keyboard.getEventKey()) continue;
            module.toggle();
        }
    }

    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) {
        if (Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null) {
            return;
        }
        if (event.getMessage().startsWith(Client.commandManager.getPrefix())) {
            event.setCanceled(true);
            Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
            Client.commandManager.runCommand(event.getMessage());
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null || !event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            return;
        }
        ComponentManager.INSTANCE.getComponents().stream().filter(Component::isShowing).forEach(Component::render);
    }
}
