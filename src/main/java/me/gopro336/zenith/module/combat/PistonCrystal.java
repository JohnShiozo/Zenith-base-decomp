// Decompiled with: CFR 0.151
// Class Version: 8
package me.gopro336.zenith.module.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.gopro336.zenith.Client;
import me.gopro336.zenith.event.PacketEvent;
import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;
import me.gopro336.zenith.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PistonCrystal
extends Module {
    private final Setting breakType = new Setting("Type", this, Arrays.asList("Swing", "Packet"));
    private final Setting rotate = new Setting("Rotate", this, false);
    private final Setting blockPlayer = new Setting("Trap Player", this, true);
    private final Setting confirmBreak = new Setting("No Glitch Blocks", this, true);
    public final Setting enemyRange = new Setting("Range", this, 5, 0, 6);
    public final Setting blocksPerTick = new Setting("Blocks Per Tick", this, 4, 0, 20);
    public final Setting stuckDetector = new Setting("Stuck Check", this, 35, 0, 200);
    public final Setting startDelay = new Setting("Start Delay", this, 4, 0, 20);
    public final Setting supBlocksDelay = new Setting("Surround Delay", this, 4, 0, 20);
    public final Setting pistonDelay = new Setting("Piston Delay", this, 2, 0, 20);
    public final Setting crystalDelay = new Setting("Crystal Delay", this, 2, 0, 20);
    public final Setting hitDelay = new Setting("Hit Delay", this, 2, 0, 20);
    private final Setting chatMsg = new Setting("Chat Msgs", this, true);
    private final Setting antiWeakness = new Setting("Anti Weakness", this, false);
    private boolean isSneaking = false;
    private boolean firstRun = false;
    private boolean noMaterials = false;
    private boolean hasMoved = false;
    private boolean isHole = true;
    private boolean enoughSpace = true;
    private int oldSlot = -1;
    private int[] slot_mat;
    private int[] delayTable;
    private int stage;
    private int delayTimeTicks;
    private double[] enemyCoords;
    private structureTemp toPlace;
    int[][] disp_surblock = new int[][]{{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
    Double[][] sur_block;
    private int stuck = 0;
    boolean broken;
    boolean brokenCrystalBug;
    boolean brokenRedstoneTorch;
    private EntityPlayer closestTarget;
    double[] coordsD;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;

    public PistonCrystal(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.rotate);
        this.addSetting(this.blockPlayer);
        this.addSetting(this.confirmBreak);
        this.addSetting(this.enemyRange);
        this.addSetting(this.blocksPerTick);
        this.addSetting(this.stuckDetector);
        this.addSetting(this.startDelay);
        this.addSetting(this.supBlocksDelay);
        this.addSetting(this.pistonDelay);
        this.addSetting(this.crystalDelay);
        this.addSetting(this.hitDelay);
        this.addSetting(this.chatMsg);
        this.addSetting(this.antiWeakness);
        this.addSetting(this.breakType);
    }

    @Override
    public void onEnable() {
        this.coordsD = new double[3];
        this.delayTable = new int[]{this.startDelay.getIntegerValue(), this.supBlocksDelay.getIntegerValue(), this.pistonDelay.getIntegerValue(), this.crystalDelay.getIntegerValue(), this.hitDelay.getIntegerValue()};
        this.toPlace = new structureTemp(0.0, 0, null);
        this.isHole = true;
        this.brokenRedstoneTorch = false;
        this.brokenCrystalBug = false;
        this.broken = false;
        this.hasMoved = false;
        this.firstRun = true;
        this.slot_mat = new int[]{-1, -1, -1, -1, -1};
        this.stuck = 0;
        this.delayTimeTicks = 0;
        this.stage = 0;
        if (PistonCrystal.mc.player == null) {
            this.disable();
            return;
        }
        if (this.chatMsg.getBooleanValue()) {
            this.printChat("PistonCrystal turned ON!", false);
        }
        this.oldSlot = PistonCrystal.mc.player.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        if (PistonCrystal.mc.player == null) {
            return;
        }
        if (this.chatMsg.getBooleanValue()) {
            if (this.noMaterials) {
                this.printChat("No Materials Detected... PistonCrystal turned OFF!", true);
            } else if (!this.isHole) {
                this.printChat("The enemy is not in a hole... PistonCrystal turned OFF!", true);
            } else if (!this.enoughSpace) {
                this.printChat("Not enough space... PistonCrystal turned OFF!", true);
            } else if (this.hasMoved) {
                this.printChat("He moved away from the hole... PistonCrystal turned OFF!", true);
            } else {
                this.printChat("PystonCrystal turned OFF!", true);
            }
        }
        if (this.isSneaking) {
            PistonCrystal.mc.player.connection.sendPacket(new CPacketEntityAction(PistonCrystal.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        if (this.oldSlot != PistonCrystal.mc.player.inventory.currentItem && this.oldSlot != -1) {
            PistonCrystal.mc.player.inventory.currentItem = this.oldSlot;
            this.oldSlot = -1;
        }
        this.noMaterials = false;
        this.firstRun = true;
    }

    @Override
    public void onUpdate() {
        if (PistonCrystal.mc.player == null) {
            this.disable();
            return;
        }
        if (this.firstRun) {
            this.closestTarget = this.findClosestTarget();
            if (this.closestTarget == null) {
                return;
            }
            this.firstRun = false;
            if (this.getMaterialsSlot()) {
                if (this.is_in_hole()) {
                    this.enemyCoords = new double[]{this.closestTarget.posX, this.closestTarget.posY, this.closestTarget.posZ};
                    this.enoughSpace = this.createStructure();
                } else {
                    this.isHole = false;
                }
            } else {
                this.noMaterials = true;
            }
        } else {
            if (this.delayTimeTicks < this.delayTable[this.stage]) {
                ++this.delayTimeTicks;
                return;
            }
            this.delayTimeTicks = 0;
        }
        if (this.noMaterials || !this.isHole || !this.enoughSpace || this.hasMoved) {
            this.disable();
            return;
        }
        if (this.supportsBlocks()) {
            if (this.stage == 1) {
                BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock));
                BlockPos targetPos = new BlockPos(this.closestTarget.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
                this.placeBlock(targetPos, 1, 1, this.toPlace.offsetX, this.toPlace.offsetZ);
                ++this.stage;
            } else if (this.stage == 2) {
                BlockPos offsetPosPist = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock));
                BlockPos targetPosPist = new BlockPos(this.closestTarget.getPositionVector()).add(offsetPosPist.getX(), offsetPosPist.getY(), offsetPosPist.getZ());
                if (!(this.get_block(targetPosPist.x, targetPosPist.y, targetPosPist.z) instanceof BlockPistonBase)) {
                    --this.stage;
                } else {
                    BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + 1));
                    BlockPos targetPos = new BlockPos(this.closestTarget.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
                    if (this.placeBlock(targetPos, 2, 1, this.toPlace.offsetX, this.toPlace.offsetZ)) {
                        ++this.stage;
                    }
                }
            } else if (this.stage == 3) {
                for (Entity t : PistonCrystal.mc.world.loadedEntityList) {
                    if (!(t instanceof EntityEnderCrystal) || (int)t.posX != (int)this.toPlace.to_place.get(this.toPlace.supportBlock + 1).x || (int)t.posZ != (int)this.toPlace.to_place.get(this.toPlace.supportBlock + 1).z) continue;
                    --this.stage;
                    break;
                }
                if (this.stage == 3) {
                    BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + 2));
                    BlockPos targetPos = new BlockPos(this.closestTarget.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
                    this.placeBlock(targetPos, 3, 1, this.toPlace.offsetX, this.toPlace.offsetZ);
                    ++this.stage;
                }
            } else if (this.stage == 4) {
                Entity crystal = null;
                for (Entity t : PistonCrystal.mc.world.loadedEntityList) {
                    if (!(t instanceof EntityEnderCrystal) || t.posX != (double)((int) t.posX) && t.posZ != (double)((int) t.posZ) && ((int) t.posX != (int)this.closestTarget.posX || (int) t.posZ != (int)this.closestTarget.posZ)) continue;
                    crystal = t;
                }
                if (this.confirmBreak.getBooleanValue() && this.broken && crystal == null) {
                    this.stuck = 0;
                    this.stage = 0;
                    this.broken = false;
                }
                if (crystal != null) {
                    this.breakCrystalPiston(crystal);
                    if (this.confirmBreak.getBooleanValue()) {
                        this.broken = true;
                    } else {
                        this.stuck = 0;
                        this.stage = 0;
                    }
                } else if (++this.stuck >= this.stuckDetector.getIntegerValue()) {
                    boolean found = false;
                    for (Entity t : PistonCrystal.mc.world.loadedEntityList) {
                        if (!(t instanceof EntityEnderCrystal) || (int)t.posX != (int)this.toPlace.to_place.get(this.toPlace.supportBlock + 1).x || (int)t.posZ != (int)this.toPlace.to_place.get(this.toPlace.supportBlock + 1).z) continue;
                        found = true;
                        break;
                    }
                    if (!found) {
                        BlockPos offsetPosPist = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + 2));
                        BlockPos pos = new BlockPos(this.closestTarget.getPositionVector()).add(offsetPosPist.getX(), offsetPosPist.getY(), offsetPosPist.getZ());
                        if (this.confirmBreak.getBooleanValue() && this.brokenRedstoneTorch && this.get_block(pos.x, pos.y, pos.z) instanceof BlockAir) {
                            this.stage = 1;
                            this.brokenRedstoneTorch = false;
                        } else {
                            EnumFacing side = BlockUtil.getPlaceableSide(pos);
                            if (side != null) {
                                if (this.rotate.getBooleanValue()) {
                                    BlockPos neighbour = pos.offset(side);
                                    EnumFacing opposite = side.getOpposite();
                                    Vec3d hitVec = new Vec3d(neighbour).add(0.5, 1.0, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
                                    BlockUtil.faceVectorPacketInstant(hitVec);
                                }
                                PistonCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                                PistonCrystal.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, side));
                                PistonCrystal.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, side));
                                if (this.confirmBreak.getBooleanValue()) {
                                    this.brokenRedstoneTorch = true;
                                } else {
                                    this.stage = 1;
                                }
                                this.printChat("Stuck detected: crystal not placed", true);
                            }
                        }
                    } else {
                        boolean ext = false;
                        for (Entity t : PistonCrystal.mc.world.loadedEntityList) {
                            if (!(t instanceof EntityEnderCrystal) || (int)t.posX != (int)this.toPlace.to_place.get(this.toPlace.supportBlock + 1).x || (int)t.posZ != (int)this.toPlace.to_place.get(this.toPlace.supportBlock + 1).z) continue;
                            ext = true;
                            break;
                        }
                        if (this.confirmBreak.getBooleanValue() && this.brokenCrystalBug && !ext) {
                            this.stuck = 0;
                            this.stage = 0;
                            this.brokenCrystalBug = false;
                        }
                        if (ext) {
                            this.breakCrystalPiston(crystal);
                            if (this.confirmBreak.getBooleanValue()) {
                                this.brokenCrystalBug = true;
                            } else {
                                this.stuck = 0;
                                this.stage = 0;
                            }
                            this.printChat("Stuck detected: crystal is stuck in the moving piston", true);
                        }
                    }
                }
            }
        }
    }

    private void breakCrystalPiston(Entity crystal) {
        if (this.antiWeakness.getBooleanValue()) {
            PistonCrystal.mc.player.inventory.currentItem = this.slot_mat[4];
        }
        if (this.rotate.getBooleanValue()) {
            this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, PistonCrystal.mc.player);
        }
        if (this.breakType.getEnumValue().equals("Swing")) {
            this.breakCrystal(crystal);
        } else if (this.breakType.getEnumValue().equals("Packet")) {
            PistonCrystal.mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
            PistonCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        if (this.rotate.getBooleanValue()) {
            PistonCrystal.resetRotation();
        }
    }

    private boolean supportsBlocks() {
        boolean done = true;
        int i = 0;
        int blockPlaced = 0;
        if (this.toPlace.to_place.size() > 0 && this.toPlace.supportBlock > 0) {
            do {
                BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(i));
                BlockPos targetPos = new BlockPos(this.closestTarget.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
                if (this.placeBlock(targetPos, 0, -1, 0.0, 0.0)) {
                    ++blockPlaced;
                }
                if (blockPlaced != this.blocksPerTick.getIntegerValue()) continue;
                return false;
            } while (++i < this.toPlace.supportBlock);
            this.stage = this.stage == 0 ? 1 : this.stage;
            return true;
        }
        this.stage = this.stage == 0 ? 1 : this.stage;
        return true;
    }

    private boolean placeBlock(BlockPos pos, int step, int direction, double offsetX, double offsetZ) {
        Block block = PistonCrystal.mc.world.getBlockState(pos).getBlock();
        EnumFacing side = BlockUtil.getPlaceableSide(pos);
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        if (side == null) {
            return false;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        if (!BlockUtil.canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d(neighbour).add(0.5 + offsetX, 1.0, 0.5 + offsetZ).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = PistonCrystal.mc.world.getBlockState(neighbour).getBlock();
        if (PistonCrystal.mc.player.inventory.getStackInSlot(this.slot_mat[step]) != ItemStack.EMPTY) {
            if (PistonCrystal.mc.player.inventory.currentItem != this.slot_mat[step]) {
                PistonCrystal.mc.player.inventory.currentItem = step == 11 ? this.slot_mat[4] : this.slot_mat[step];
            }
        } else {
            return false;
        }
        if (!this.isSneaking && BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock)) {
            PistonCrystal.mc.player.connection.sendPacket(new CPacketEntityAction(PistonCrystal.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        boolean stoppedAC = false;
        if (this.rotate.getBooleanValue() || step == 1) {
            BlockUtil.faceVectorPacketInstant(hitVec);
        }
        PistonCrystal.mc.playerController.processRightClickBlock(PistonCrystal.mc.player, PistonCrystal.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        PistonCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
        if (stoppedAC) {
            stoppedAC = false;
        }
        return true;
    }

    private EntityPlayer findClosestTarget() {
        List<EntityPlayer> playerList = PistonCrystal.mc.world.playerEntities;
        EntityPlayer closestTarget_test = null;
        for (EntityPlayer entityPlayer : playerList) {
            if (entityPlayer == PistonCrystal.mc.player || entityPlayer.isDead) continue;
            if (this.closestTarget == null && (double)PistonCrystal.mc.player.getDistance(entityPlayer) <= (double)this.enemyRange.getIntegerValue()) {
                closestTarget_test = entityPlayer;
                continue;
            }
            if (this.closestTarget == null || !((double)PistonCrystal.mc.player.getDistance(entityPlayer) <= (double)this.enemyRange.getIntegerValue()) || !(PistonCrystal.mc.player.getDistance(entityPlayer) < PistonCrystal.mc.player.getDistance(this.closestTarget))) continue;
            closestTarget_test = entityPlayer;
        }
        return closestTarget_test;
    }

    private void printChat(String text, Boolean error) {
        Client.SendMessage("(Piston Crystal)" + text);
    }

    private boolean getMaterialsSlot() {
        if (PistonCrystal.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal) {
            this.slot_mat[2] = 11;
        }
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = PistonCrystal.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (stack.getItem() instanceof ItemEndCrystal) {
                this.slot_mat[2] = i;
                continue;
            }
            if (this.antiWeakness.getBooleanValue() && stack.getItem() instanceof ItemSword) {
                this.slot_mat[4] = i;
                continue;
            }
            if (!(stack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            if (block instanceof BlockObsidian) {
                this.slot_mat[0] = i;
                continue;
            }
            if (block instanceof BlockPistonBase) {
                this.slot_mat[1] = i;
                continue;
            }
            if (!(block instanceof BlockRedstoneTorch) && !block.translationKey.equals("blockRedstone")) continue;
            this.slot_mat[3] = i;
        }
        int count = 0;
        for (int val : this.slot_mat) {
            if (val == -1) continue;
            ++count;
        }
        return count == 4 + (this.antiWeakness.getBooleanValue() ? 1 : 0);
    }

    private boolean is_in_hole() {
        this.sur_block = new Double[][]{{this.closestTarget.posX + 1.0, this.closestTarget.posY, this.closestTarget.posZ}, {this.closestTarget.posX - 1.0, this.closestTarget.posY, this.closestTarget.posZ}, {this.closestTarget.posX, this.closestTarget.posY, this.closestTarget.posZ + 1.0}, {this.closestTarget.posX, this.closestTarget.posY, this.closestTarget.posZ - 1.0}};
        this.enemyCoords = new double[]{this.closestTarget.posX, this.closestTarget.posY, this.closestTarget.posZ};
        return !(this.get_block(this.sur_block[0][0], this.sur_block[0][1], this.sur_block[0][2]) instanceof BlockAir) && !(this.get_block(this.sur_block[1][0], this.sur_block[1][1], this.sur_block[1][2]) instanceof BlockAir) && !(this.get_block(this.sur_block[2][0], this.sur_block[2][1], this.sur_block[2][2]) instanceof BlockAir) && !(this.get_block(this.sur_block[3][0], this.sur_block[3][1], this.sur_block[3][2]) instanceof BlockAir);
    }

    private boolean createStructure() {
        structureTemp addedStructure = new structureTemp(Double.MAX_VALUE, 0, null);
        int i = 0;
        int[] meCord = new int[]{(int)PistonCrystal.mc.player.posX, (int)PistonCrystal.mc.player.posY, (int)PistonCrystal.mc.player.posZ};
        ArrayList ignoreList = new ArrayList();
        for (Double[] cord_b : this.sur_block) {
            double d = 0;
            double[] crystalCords = new double[]{cord_b[0], cord_b[1] + 1.0, cord_b[2]};
            BlockPos positionCrystal = new BlockPos(crystalCords[0], crystalCords[1], crystalCords[2]);
            double distance_now = PistonCrystal.mc.player.getDistance(crystalCords[0], crystalCords[1], crystalCords[2]);
            if (d < addedStructure.distance && (positionCrystal.y != meCord[1] || meCord[0] != positionCrystal.x || Math.abs(meCord[2] - positionCrystal.z) > 3 && meCord[2] != positionCrystal.z || Math.abs(meCord[0] - positionCrystal.x) > 3)) {
                double[] pistonCord;
                Block blockPiston;
                Double[] doubleArray = cord_b;
                Double.valueOf(doubleArray[1] + 1.0);
                if (this.get_block(crystalCords[0], crystalCords[1], crystalCords[2]) instanceof BlockAir && ((blockPiston = this.get_block((pistonCord = new double[]{crystalCords[0] + (double)this.disp_surblock[i][0], crystalCords[1], crystalCords[2] + (double)this.disp_surblock[i][2]})[0], pistonCord[1], pistonCord[2])) instanceof BlockAir || blockPiston instanceof BlockPistonBase) && this.someoneInCoords(pistonCord[0], pistonCord[1], pistonCord[2])) {
                    boolean join = false;
                    boolean bl = !this.rotate.getBooleanValue() || ((int) pistonCord[0] == meCord[0] ? this.closestTarget.posZ > PistonCrystal.mc.player.posZ != this.closestTarget.posZ > pistonCord[2] || Math.abs((int) this.closestTarget.posZ - (int) PistonCrystal.mc.player.posZ) == 1 : (int) pistonCord[2] != meCord[2] || (this.closestTarget.posX > PistonCrystal.mc.player.posX != this.closestTarget.posX > pistonCord[0] || Math.abs((int) this.closestTarget.posX - (int) PistonCrystal.mc.player.posX) == 1) && Math.abs((int) this.closestTarget.posX - (int) PistonCrystal.mc.player.posX) <= 1) || (join = false);
                    if (join) {
                        boolean enter = false;
                        boolean bl2 = !this.rotate.getBooleanValue() || (meCord[0] == (int) this.closestTarget.posX || meCord[2] == (int) this.closestTarget.posZ ? PistonCrystal.mc.player.getDistance(crystalCords[0], crystalCords[1], crystalCords[2]) <= 3.5 || meCord[0] == (int) crystalCords[0] || meCord[2] == (int) crystalCords[2] : meCord[0] != (int) pistonCord[0] || Math.abs((int) this.closestTarget.posZ - (int) PistonCrystal.mc.player.posZ) == 1 || meCord[2] == (int) pistonCord[2] && Math.abs((int) this.closestTarget.posZ - (int) PistonCrystal.mc.player.posZ) != 1) || (enter = false);
                        if (enter) {
                            int[] poss = null;
                            for (int[] possibilites : this.disp_surblock) {
                                double[] coordinatesTemp = new double[]{cord_b[0] + (double)this.disp_surblock[i][0] + (double)possibilites[0], cord_b[1], cord_b[2] + (double)this.disp_surblock[i][2] + (double)possibilites[2]};
                                int[] torchCoords = new int[]{(int)coordinatesTemp[0], (int)coordinatesTemp[1], (int)coordinatesTemp[2]};
                                int[] crystalCoords = new int[]{(int)crystalCords[0], (int)crystalCords[1], (int)crystalCords[2]};
                                if (!(this.get_block(coordinatesTemp[0], coordinatesTemp[1], coordinatesTemp[2]) instanceof BlockAir) || torchCoords[0] == crystalCoords[0] && torchCoords[1] == crystalCoords[1] && crystalCoords[2] == torchCoords[2] || !this.someoneInCoords(coordinatesTemp[0], coordinatesTemp[1], coordinatesTemp[2])) continue;
                                poss = possibilites;
                                break;
                            }
                            if (poss != null) {
                                float offsetZ;
                                float offsetX;
                                ArrayList<Vec3d> toPlaceTemp = new ArrayList<>();
                                int supportBlock = 0;
                                if (this.get_block(cord_b[0] + (double)this.disp_surblock[i][0], cord_b[1] - 1.0, cord_b[2] + (double)this.disp_surblock[i][2]) instanceof BlockAir) {
                                    toPlaceTemp.add(new Vec3d(this.disp_surblock[i][0] * 2, this.disp_surblock[i][1], this.disp_surblock[i][2] * 2));
                                    ++supportBlock;
                                }
                                if (this.get_block(cord_b[0] + (double)this.disp_surblock[i][0] + (double)poss[0], cord_b[1] - 1.0, cord_b[2] + (double)this.disp_surblock[i][2] + (double)poss[2]) instanceof BlockAir) {
                                    toPlaceTemp.add(new Vec3d(this.disp_surblock[i][0] * 2 + poss[0], this.disp_surblock[i][1], this.disp_surblock[i][2] * 2 + poss[2]));
                                    ++supportBlock;
                                }
                                toPlaceTemp.add(new Vec3d(this.disp_surblock[i][0] * 2, this.disp_surblock[i][1] + 1, this.disp_surblock[i][2] * 2));
                                toPlaceTemp.add(new Vec3d(this.disp_surblock[i][0], this.disp_surblock[i][1] + 1, this.disp_surblock[i][2]));
                                toPlaceTemp.add(new Vec3d(this.disp_surblock[i][0] * 2 + poss[0], this.disp_surblock[i][1] + 1, this.disp_surblock[i][2] * 2 + poss[2]));
                                if (this.disp_surblock[i][0] != 0) {
                                    float f = offsetX = this.rotate.getBooleanValue() ? (float)this.disp_surblock[i][0] / 2.0f : (float)(this.disp_surblock[i][0] * 10);
                                    offsetZ = PistonCrystal.mc.player.getDistanceSq(pistonCord[0], pistonCord[1], pistonCord[2] + 0.5) > PistonCrystal.mc.player.getDistanceSq(pistonCord[0], pistonCord[1], pistonCord[2] - 0.5) ? -0.5f : 0.5f;
                                } else {
                                    offsetZ = this.rotate.getBooleanValue() ? (float)this.disp_surblock[i][2] / 2.0f : (float)(this.disp_surblock[i][2] * 10);
                                    offsetX = PistonCrystal.mc.player.getDistanceSq(pistonCord[0] + 0.5, pistonCord[1], pistonCord[2]) > PistonCrystal.mc.player.getDistanceSq(pistonCord[0] - 0.5, pistonCord[1], pistonCord[2]) ? -0.5f : 0.5f;
                                }
                                addedStructure.replaceValues(distance_now, supportBlock, toPlaceTemp, -1, offsetX, offsetZ);
                            }
                        }
                    }
                }
            }
            ++i;
        }
        if (addedStructure.to_place != null) {
            if (this.blockPlayer.getBooleanValue()) {
                Vec3d valuesStart = addedStructure.to_place.get(addedStructure.supportBlock + 1);
                int[] valueBegin = new int[]{(int)(-valuesStart.x), (int)valuesStart.y, (int)(-valuesStart.z)};
                addedStructure.to_place.add(0, new Vec3d(0.0, 2.0, 0.0));
                addedStructure.to_place.add(0, new Vec3d(valueBegin[0], valueBegin[1] + 1, valueBegin[2]));
                addedStructure.to_place.add(0, new Vec3d(valueBegin[0], valueBegin[1], valueBegin[2]));
                addedStructure.supportBlock += 3;
            }
            this.toPlace = addedStructure;
            return true;
        }
        return false;
    }

    private boolean someoneInCoords(double x, double y, double z) {
        int xCheck = (int)x;
        int yCheck = (int)y;
        int zCheck = (int)z;
        List<EntityPlayer> playerList = PistonCrystal.mc.world.playerEntities;
        for (EntityPlayer player : playerList) {
            if ((int)player.posX != xCheck || (int)player.posZ != zCheck || (int)player.posY < yCheck - 1 || (int)player.posY > yCheck + 1) continue;
            return false;
        }
        return true;
    }

    private Block get_block(double x, double y, double z) {
        return PistonCrystal.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = PistonCrystal.calculateLookAt(px, py, pz, me);
        PistonCrystal.setYawAndPitch((float)v[0], (float)v[1]);
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        double yaw = Math.atan2(dirz /= len, dirx /= len);
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;
        return new double[]{yaw += 90.0, pitch};
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private void breakCrystal(Entity crystal) {
        PistonCrystal.mc.playerController.attackEntity(PistonCrystal.mc.player, crystal);
        PistonCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer)packet).yaw = (float)yaw;
            ((CPacketPlayer)packet).pitch = (float)pitch;
        }
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = PistonCrystal.mc.player.rotationYaw;
            pitch = PistonCrystal.mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    static class structureTemp {
        public double distance;
        public int supportBlock;
        public List<Vec3d> to_place;
        public int direction;
        public float offsetX;
        public float offsetZ;

        public structureTemp(double distance, int supportBlock, List<Vec3d> to_place) {
            this.distance = distance;
            this.supportBlock = supportBlock;
            this.to_place = to_place;
            this.direction = -1;
        }

        public void replaceValues(double distance, int supportBlock, List<Vec3d> to_place, int direction, float offsetX, float offsetZ) {
            this.distance = distance;
            this.supportBlock = supportBlock;
            this.to_place = to_place;
            this.direction = direction;
            this.offsetX = offsetX;
            this.offsetZ = offsetZ;
        }
    }
}
