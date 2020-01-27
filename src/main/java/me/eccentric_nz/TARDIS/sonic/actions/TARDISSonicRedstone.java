package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlock;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISSonicRedstone {

    public static void togglePoweredState(TARDIS plugin, Player player, Block block) {
        // not protected blocks - WorldGuard / GriefPrevention / Lockette / Towny
        if (TARDISSonicRespect.checkBlockRespect(plugin, player, block)) {
            return;
        }
        TARDISSonicSound.playSonicSound(plugin, player, System.currentTimeMillis(), 600L, "sonic_short");
        Material blockType = block.getType();
        // do redstone activation
        switch (blockType) {
            case DETECTOR_RAIL:
            case POWERED_RAIL:
                RedstoneRail rail = (RedstoneRail) block.getBlockData();
                if (plugin.getGeneralKeeper().getSonicRails().contains(block.getLocation().toString())) {
                    plugin.getGeneralKeeper().getSonicRails().remove(block.getLocation().toString());
                    rail.setPowered(false);
                } else {
                    plugin.getGeneralKeeper().getSonicRails().add(block.getLocation().toString());
                    rail.setPowered(true);
                }
                block.setBlockData(rail, true);
                break;
            case IRON_DOOR:
                // get bottom door block
                Block tmp = block;
                Bisected bisected = (Bisected) block.getBlockData();
                if (bisected.getHalf().equals(Bisected.Half.TOP)) {
                    tmp = block.getRelative(BlockFace.DOWN);
                }
                // not TARDIS doors!
                String doorloc = tmp.getLocation().getWorld().getName() + ":" + tmp.getLocation().getBlockX() + ":" + tmp.getLocation().getBlockY() + ":" + tmp.getLocation().getBlockZ();
                HashMap<String, Object> wheredoor = new HashMap<>();
                wheredoor.put("door_location", doorloc);
                ResultSetDoors rsd = new ResultSetDoors(plugin, wheredoor, false);
                if (rsd.resultSet()) {
                    return;
                }
                if (!plugin.getTrackerKeeper().getSonicDoors().contains(player.getUniqueId())) {
                    plugin.getTrackerKeeper().getSonicDoors().add(player.getUniqueId());
                    Block door_bottom = tmp;
                    Openable openable = (Openable) door_bottom.getBlockData();
                    openable.setOpen(true);
                    door_bottom.setBlockData(openable, true);
                    // return the door to its previous state after 3 seconds
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        openable.setOpen(false);
                        door_bottom.setBlockData(openable, true);
                        plugin.getTrackerKeeper().getSonicDoors().remove(player.getUniqueId());
                    }, 60L);
                }
                break;
            case PISTON:
            case STICKY_PISTON:
                Piston piston = (Piston) block.getBlockData();
                // find the direction the piston is facing
                if (plugin.getGeneralKeeper().getSonicPistons().contains(block.getLocation().toString())) {
                    plugin.getGeneralKeeper().getSonicPistons().remove(block.getLocation().toString());
                    for (BlockFace f : plugin.getGeneralKeeper().getBlockFaces()) {
                        if (block.getRelative(f).getType().equals(Material.AIR)) {
                            // force a block update
                            block.getRelative(f).setBlockData(TARDISConstants.VOID_AIR, true);
                            block.getRelative(f).setBlockData(TARDISConstants.AIR, true);
                            break;
                        }
                    }
                } else if (setExtension(block)) {
                    plugin.getGeneralKeeper().getSonicPistons().add(block.getLocation().toString());
                    piston.setExtended(true);
                    block.setBlockData(piston, true);
                    player.playSound(block.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1.0f, 1.0f);
                }
                break;
            case REDSTONE_LAMP:
                Lightable lightable = (Lightable) block.getBlockData();
                if (!lightable.isLit()) {
                    plugin.getGeneralKeeper().getSonicLamps().add(block.getLocation().toString());
                    for (BlockFace f : plugin.getGeneralKeeper().getBlockFaces()) {
                        if (block.getRelative(f).getType().equals(Material.AIR)) {
                            // force a block update
                            block.getRelative(f).setBlockData(TARDISConstants.POWER, true);
                            lightable.setLit(true);
                            block.setBlockData(lightable, true);
                            block.getRelative(f).setBlockData(TARDISConstants.AIR, true);
                            break;
                        }
                    }
                } else if (plugin.getGeneralKeeper().getSonicLamps().contains(block.getLocation().toString())) {
                    plugin.getGeneralKeeper().getSonicLamps().remove(block.getLocation().toString());
                    lightable.setLit(false);
                    block.setBlockData(lightable, true);
                }
                break;
            case REDSTONE_WIRE:
                RedstoneWire wire = (RedstoneWire) block.getBlockData();
                if (plugin.getGeneralKeeper().getSonicWires().contains(block.getLocation().toString())) {
                    plugin.getGeneralKeeper().getSonicWires().remove(block.getLocation().toString());
                    wire.setPower(0);
                    plugin.getGeneralKeeper().getBlockFaces().forEach((f) -> {
                        if (block.getRelative(f).getType().equals(Material.REDSTONE_WIRE)) {
                            wire.setPower(0);
                        }
                    });
                    block.setBlockData(wire, true);
                } else {
                    plugin.getGeneralKeeper().getSonicWires().add(block.getLocation().toString());
                    wire.setPower(15);
                    plugin.getGeneralKeeper().getBlockFaces().forEach((f) -> {
                        if (block.getRelative(f).getType().equals(Material.REDSTONE_WIRE)) {
                            wire.setPower(13);
                        }
                    });
                    block.setBlockData(wire, true);
                }
                break;
            case MUSHROOM_STEM:
                // check the block is a chemistry lamp block
                MultipleFacing multipleFacing = (MultipleFacing) block.getBlockData();
                if (TARDISMushroomBlock.isChemistryStemOn(multipleFacing)) {
                    multipleFacing = TARDISMushroomBlock.getChemistryStemOff(multipleFacing);
                    // delete light source
                    plugin.getTardisHelper().deleteLight(block.getLocation());
                } else if (TARDISMushroomBlock.isChemistryStemOff(multipleFacing)) {
                    multipleFacing = TARDISMushroomBlock.getChemistryStemOn(multipleFacing);
                    // create light source
                    plugin.getTardisHelper().createLight(block.getLocation());
                }
                block.setBlockData(multipleFacing,true);
                break;
            default:
                break;
        }
    }

    public static boolean setExtension(Block b) {
        BlockFace face = ((Piston) b.getBlockData()).getFacing();
        Block l = b.getRelative(face);
        Material mat = l.getType();
        // check if there is a block there
        if (!mat.equals(Material.PISTON_HEAD)) {
            if (mat.isAir()) {
                extend(b, l);
                return true;
            } else {
                // check the block further on for AIR
                Block two = b.getRelative(face, 2);
                if (two.getType().isAir()) {
                    two.setBlockData(mat.createBlockData());
                    extend(b, l);
                    return true;
                }
            }
        }
        return false;
    }

    private static void extend(Block b, Block l) {
        l.setBlockData(Material.PISTON_HEAD.createBlockData());
        Piston piston = (Piston) b.getBlockData();
        piston.setExtended(true);
        b.setBlockData(piston, true);
    }
}
