/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Gate;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TARDISSonic {

    private static final List<Material> distance = new ArrayList<>();

    static {
        distance.addAll(Tag.BUTTONS.getValues());
        distance.addAll(Tag.DOORS.getValues());
        distance.addAll(Tag.FENCE_GATES.getValues());
        distance.add(Material.LEVER);
    }

    public static void standardSonic(TARDIS plugin, Player player, long now) {
        Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation().getBlock();
        Material blockType = targetBlock.getType();
        if (distance.contains(blockType)) {
            TARDISSonicSound.playSonicSound(plugin, player, now, 600L, "sonic_short");
            // not protected doors - WorldGuard / GriefPrevention  / Towny
            if (TARDISSonicRespect.checkBlockRespect(plugin, player, targetBlock)) {
                if (Tag.DOORS.isTagged(blockType)) {
                    Block lowerdoor;
                    Bisected bisected = (Bisected) targetBlock.getBlockData();
                    if (bisected.getHalf().equals(Bisected.Half.TOP)) {
                        lowerdoor = targetBlock.getRelative(BlockFace.DOWN);
                    } else {
                        lowerdoor = targetBlock;
                    }
                    // is it a TARDIS door?
                    HashMap<String, Object> where = new HashMap<>();
                    String doorloc = lowerdoor.getLocation().getWorld().getName() + ":" + lowerdoor.getLocation().getBlockX() + ":" + lowerdoor.getLocation().getBlockY() + ":" + lowerdoor.getLocation().getBlockZ();
                    where.put("door_location", doorloc);
                    ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
                    if (rs.resultSet()) {
                        return;
                    }
                    if (!plugin.getTrackerKeeper().getSonicDoors().contains(player.getUniqueId())) {
                        Openable openable = (Openable) lowerdoor.getBlockData();
                        boolean open = !openable.isOpen();
                        openable.setOpen(open);
                        lowerdoor.setBlockData(openable, true);
                        if (blockType.equals(Material.IRON_DOOR)) {
                            plugin.getTrackerKeeper().getSonicDoors().add(player.getUniqueId());
                            // return the door to its previous state after 3 seconds
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                openable.setOpen(!open);
                                lowerdoor.setBlockData(openable, true);
                                plugin.getTrackerKeeper().getSonicDoors().remove(player.getUniqueId());
                            }, 60L);
                        }
                    }
                }
                if (Tag.BUTTONS.isTagged(blockType) || blockType == Material.LEVER) {
                    powerSurroundingBlock(targetBlock);
                }
                if (Tag.FENCE_GATES.isTagged(blockType)) {
                    Gate gate = (Gate) targetBlock.getBlockData();
                    gate.setOpen(!gate.isOpen());
                    targetBlock.setBlockData(gate, true);
                }
            }
        } else {
            TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
        }
    }

    private static void powerSurroundingBlock(Block block) {
        TARDIS.plugin.getTardisHelper().setPowerableBlockInteract(block);
    }
}
