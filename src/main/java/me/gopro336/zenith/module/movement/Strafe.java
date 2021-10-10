package me.gopro336.zenith.module.movement;

import java.util.Arrays;
import me.gopro336.zenith.event.MoveEvent;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import me.gopro336.zenith.util.StrafeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Strafe
extends Module {
    private int jump;
    private double speed;
    private boolean jump_state;
    public static final Minecraft mc = Minecraft.getMinecraft();
    private final Setting modes_movement = new Setting("Mode", (Module)this, Arrays.asList("OnGround", "AutoJump"));
    private final Setting strafe_speed = new Setting("Speed", this, 0, 0, 100);
    private final Setting automatically_sprint = new Setting("UseSprint", (Module)this, true);
    private final Setting smooth_jump = new Setting("SmoothJump", (Module)this, false);
    private final Setting speed_potion_effect = new Setting("SpeedPotUtil", (Module)this, true);
    private final Setting jump_potion_effect = new Setting("JumpPotUtil", (Module)this, false);
    private final Setting bypass_speed = new Setting("ExplosionBypass", (Module)this, false);
    private final Setting smart_bypass_update = new Setting("AutoBypass", (Module)this, false);

    public Strafe(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.modes_movement);
        this.addSetting(this.strafe_speed);
        this.addSetting(this.automatically_sprint);
        this.addSetting(this.smooth_jump);
        this.addSetting(this.speed_potion_effect);
        this.addSetting(this.jump_potion_effect);
        this.addSetting(this.smart_bypass_update);
        this.addSetting(this.bypass_speed);
        this.jump = Strafe.mc.gameSettings.keyBindJump.getKeyCode();
    }

    @Override
    public void onUpdate() {
        if (Strafe.mc.player == null) {
            return;
        }
        if (!this.bypass_speed.getBooleanValue()) {
            return;
        }
        if (Strafe.mc.currentScreen instanceof GuiChat || Strafe.mc.currentScreen != null) {
            return;
        }
        if (Keyboard.isKeyDown(this.jump) && Strafe.mc.player.onGround) {
            Strafe.mc.player.jump();
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (Strafe.mc.player == null || Strafe.mc.world == null) {
            return;
        }
        if (Strafe.mc.player.isSneaking() || Strafe.mc.player.isOnLadder() || Strafe.mc.player.isInLava() || Strafe.mc.player.isInWater() || Strafe.mc.player.capabilities.isFlying) {
            return;
        }
        double[] player_movement = StrafeUtil.transformStrafeMovement(Strafe.mc.player);
        if (this.automatically_sprint.getBooleanValue()) {
            Strafe.mc.player.setSprinting(true);
        }
        double d = Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ()) > 0.2873 ? Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ()) + (Math.sqrt(event.getX() * event.getX() + event.getZ() * event.getZ()) >= 0.34 ? (double)this.strafe_speed.getIntegerValue() / 1000.0 : 0.0) : (this.speed = 0.2873);
        if (Strafe.mc.player.isPotionActive(MobEffects.SPEED) && this.speed_potion_effect.getBooleanValue()) {
            int amplifier = Strafe.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            this.speed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (player_movement[2] == 0.0 && player_movement[3] == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (this.modes_movement.getEnumValue().equalsIgnoreCase("OnGround")) {
                if (Strafe.mc.gameSettings.keyBindJump.isKeyDown() && Strafe.mc.player.onGround) {
                    this.jump_state = true;
                }
            } else if (this.modes_movement.getEnumValue().equalsIgnoreCase("AutoJump")) {
                if (this.automatically_sprint.getBooleanValue()) {
                    Strafe.mc.player.setSprinting(true);
                }
                if (Strafe.mc.gameSettings.keyBindJump.isKeyDown()) {
                    this.jump_state = true;
                }
            }
            if (this.jump_state) {
                double jump = 0.40123128;
                if (Strafe.mc.player.onGround) {
                    if (!this.smooth_jump.getBooleanValue()) {
                        this.speed = 0.6174077;
                    }
                    if (Strafe.mc.player.isPotionActive(MobEffects.JUMP_BOOST) && this.jump_potion_effect.getBooleanValue()) {
                        jump += (double)((float)(Strafe.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f);
                    }
                    Strafe.mc.player.motionY = jump;
                    event.setY(Strafe.mc.player.motionY);
                }
                this.jump_state = false;
            }
            event.setX(player_movement[2] * this.speed * Math.cos(Math.toRadians(player_movement[0] + 90.0)) + player_movement[3] * this.speed * Math.sin(Math.toRadians(player_movement[0] + 90.0)));
            event.setZ(player_movement[2] * this.speed * Math.sin(Math.toRadians(player_movement[0] + 90.0)) - player_movement[3] * this.speed * Math.cos(Math.toRadians(player_movement[0] + 90.0)));
        }
    }
}
