package me.gopro336.zenith.util;

import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PlayerUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static double vanillaSpeed() {
        double baseSpeed = 0.272;
        if (Minecraft.getMinecraft().player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(Minecraft.getMinecraft().player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)amplifier;
        }
        return baseSpeed;
    }

    public static boolean isMoving() {
        return (double)Minecraft.getMinecraft().player.moveForward != 0.0 || (double)Minecraft.getMinecraft().player.moveStrafing != 0.0;
    }

    public static int getSlot(Item item) {
        for (int i = 0; i < 9; ++i) {
            Item item1 = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
            if (!item.equals(item1)) continue;
            return i;
        }
        return -1;
    }

    public static int getSlot(Block block) {
        for (int i = 0; i < 9; ++i) {
            Item item = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
            if (!(item instanceof ItemBlock) || !((ItemBlock)((Object)item)).getBlock().equals(block)) continue;
            return i;
        }
        return -1;
    }

    public static void placeBlock(BlockPos pos) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            if (PlayerUtil.mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) || PlayerUtil.isIntercepted(pos)) continue;
            Vec3d vec = new Vec3d((double)pos.getX() + 0.5 + (double)enumFacing.getXOffset() * 0.5, (double)pos.getY() + 0.5 + (double)enumFacing.getYOffset() * 0.5, (double)pos.getZ() + 0.5 + (double)enumFacing.getZOffset() * 0.5);
            float[] old = new float[]{PlayerUtil.mc.player.rotationYaw, PlayerUtil.mc.player.rotationPitch};
            PlayerUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation((float)Math.toDegrees(Math.atan2(vec.z - PlayerUtil.mc.player.posZ, vec.x - PlayerUtil.mc.player.posX)) - 90.0f, (float)(-Math.toDegrees(Math.atan2(vec.y - (PlayerUtil.mc.player.posY + (double)PlayerUtil.mc.player.getEyeHeight()), Math.sqrt((vec.x - PlayerUtil.mc.player.posX) * (vec.x - PlayerUtil.mc.player.posX) + (vec.z - PlayerUtil.mc.player.posZ) * (vec.z - PlayerUtil.mc.player.posZ))))), PlayerUtil.mc.player.onGround));
            PlayerUtil.mc.player.connection.sendPacket(new CPacketEntityAction(PlayerUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            PlayerUtil.mc.playerController.processRightClickBlock(PlayerUtil.mc.player, PlayerUtil.mc.world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
            PlayerUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
            PlayerUtil.mc.player.connection.sendPacket(new CPacketEntityAction(PlayerUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            PlayerUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(old[0], old[1], PlayerUtil.mc.player.onGround));
            return;
        }
    }

    public static boolean isIntercepted(BlockPos pos) {
        for (Entity entity : PlayerUtil.mc.world.loadedEntityList) {
            if (!new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static double getDirection() {
        float rotationYaw = PlayerUtil.mc.player.rotationYaw;
        if (PlayerUtil.mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (PlayerUtil.mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (PlayerUtil.mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (PlayerUtil.mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (PlayerUtil.mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
}
