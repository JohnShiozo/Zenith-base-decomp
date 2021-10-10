package me.gopro336.zenith.mixin.mixins.mixin;

import io.netty.channel.ChannelHandlerContext;
import me.gopro336.zenith.event.PacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NetworkManager.class}, priority=634756347)
public class NetworkManagerMixin {
    @Inject(method={"sendPacket(Lnet/minecraft/network/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void preSendPacket(Packet<?> packetIn, CallbackInfo ci) {
        PacketEvent.Send packetEvent = new PacketEvent.Send(packetIn);
        MinecraftForge.EVENT_BUS.post(packetEvent);
        if (packetEvent.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method={"channelRead0"}, at={@At(value="HEAD")}, cancellable=true)
    private void preChannelRead(ChannelHandlerContext handlerContext, Packet<?> packetIn, CallbackInfo ci) {
        PacketEvent.Receive packetEvent = new PacketEvent.Receive(packetIn);
        MinecraftForge.EVENT_BUS.post(packetEvent);
        if (packetEvent.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method={"sendPacket(Lnet/minecraft/network/Packet;)V"}, at={@At(value="TAIL")}, cancellable=true)
    private void postSendPacket(Packet<?> packetIn, CallbackInfo ci) {
        PacketEvent.PostSend packetEvent = new PacketEvent.PostSend(packetIn);
        MinecraftForge.EVENT_BUS.post(packetEvent);
        if (packetEvent.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method={"channelRead0"}, at={@At(value="TAIL")}, cancellable=true)
    private void postChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent.PostReceive packetEvent = new PacketEvent.PostReceive(packet);
        MinecraftForge.EVENT_BUS.post(packetEvent);
        if (packetEvent.isCanceled()) {
            callbackInfo.cancel();
        }
    }
}
