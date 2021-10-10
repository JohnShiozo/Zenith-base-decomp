// Decompiled with: CFR 0.151
// Class Version: 8
package me.gopro336.zenith.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class HoleUtil
extends Vec3i {
    private final BlockPos blockPos;
    private final HoleTypes holeTypes;
    private boolean tall;

    public HoleUtil(int x, int y, int z, BlockPos pos, HoleTypes type) {
        super(x, y, z);
        this.blockPos = pos;
        this.holeTypes = type;
    }

    public HoleUtil(int x, int y, int z, BlockPos pos, HoleTypes type, boolean tall) {
        super(x, y, z);
        this.blockPos = pos;
        this.tall = tall;
        this.holeTypes = type;
    }

    public boolean isTall() {
        return this.tall;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public HoleTypes getHoleTypes() {
        return this.holeTypes;
    }

    public enum HoleTypes {
        None, Normal, Obsidian, Bedrock,
    }
}
