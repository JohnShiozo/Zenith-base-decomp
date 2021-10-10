package me.gopro336.zenith.util;

import net.minecraft.client.entity.EntityPlayerSP;

public class StrafeUtil {
    public static double[] transformStrafeMovement(EntityPlayerSP entity) {
        double entity_rotation_yaw = entity.rotationYaw;
        double entity_rotation_pitch = entity.rotationPitch;
        double entity_movement_forward = entity.movementInput.moveForward;
        double entity_movement_strafe = entity.movementInput.moveStrafe;
        if (entity_movement_forward != 0.0 && entity_movement_strafe != 0.0 && entity_movement_forward != 0.0) {
            if (entity_movement_strafe > 0.0) {
                entity_rotation_yaw += (double)(entity_movement_forward > 0.0 ? -45 : 45);
            } else if (entity_movement_strafe < 0.0) {
                entity_rotation_yaw += (double)(entity_movement_forward > 0.0 ? 45 : -45);
            }
            entity_movement_strafe = 0.0;
            if (entity_movement_forward > 0.0) {
                entity_movement_forward = 1.0;
            } else if (entity_movement_forward < 0.0) {
                entity_movement_forward = -1.0;
            }
        }
        return new double[]{entity_rotation_yaw, entity_rotation_pitch, entity_movement_forward, entity_movement_strafe};
    }
}
