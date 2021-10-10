package me.gopro336.zenith.event;

import me.gopro336.zenith.Client;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventManager {
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().player == null) {
            return;
        }
        Client.SendMessage("OnUpdate Fired");
        Client.moduleManager.onUpdate();
    }
}
