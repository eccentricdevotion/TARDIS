/*
 * Copyright (C) 2022 eccentric_nz
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
        distance.add(Material.ACACIA_BUTTON);
        distance.add(Material.ACACIA_DOOR);
        distance.add(Material.ACACIA_FENCE_GATE);
        distance.add(Material.BIRCH_BUTTON);
        distance.add(Material.BIRCH_DOOR);
        distance.add(Material.BIRCH_FENCE_GATE);
        distance.add(Material.CRIMSON_BUTTON);
        distance.add(Material.CRIMSON_DOOR);
        distance.add(Material.CRIMSON_FENCE_GATE);
        distance.add(Material.DARK_OAK_BUTTON);
        distance.add(Material.DARK_OAK_DOOR);
        distance.add(Material.DARK_OAK_FENCE_GATE);
        distance.add(Material.IRON_DOOR);
        distance.add(Material.JUNGLE_BUTTON);
        distance.add(Material.JUNGLE_DOOR);
        distance.add(Material.JUNGLE_FENCE_GATE);
        distance.add(Material.LEVER);
        distance.add(Material.OAK_BUTTON);
        distance.add(Material.OAK_DOOR);
        distance.add(Material.OAK_FENCE_GATE);
        distance.add(Material.OAK_FENCE_GATE);
        distance.add(Material.POLISHED_BLACKSTONE_BUTTON);
        distance.add(Material.SPRUCE_BUTTON);
        distance.add(Material.SPRUCE_DOOR);
        distance.add(Material.SPRUCE_FENCE_GATE);
        distance.add(Material.STONE_BUTTON);
        distance.add(Material.WARPED_BUTTON);
        distance.add(Material.WARPED_DOOR);
        distance.add(Material.WARPED_FENCE_GATE);
    }

    public static void standardSonic(TARDIS plugin, Player player, long now) {
        Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation().getBlock();
        Material blockType = targetBlock.getType();
        if (distance.contains(blockType)) {
            TARDISSonicSound.playSonicSound(plugin, player, now, 600L, "sonic_short");
            // not protected doors - WorldGuard / GriefPrevention / Lockette / Towny
            if (TARDISSonicRespect.checkBlockRespect(plugin, player, targetBlock)) {
                switch (blockType) {
                    case ACACIA_DOOR, BIRCH_DOOR, CRIMSON_DOOR, DARK_OAK_DOOR, IRON_DOOR, JUNGLE_DOOR, OAK_DOOR, SPRUCE_DOOR, WARPED_DOOR -> {
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
                    case LEVER, ACACIA_BUTTON, BIRCH_BUTTON, DARK_OAK_BUTTON, JUNGLE_BUTTON, OAK_BUTTON, SPRUCE_BUTTON, STONE_BUTTON, CRIMSON_BUTTON, POLISHED_BLACKSTONE_BUTTON, WARPED_BUTTON ->
                            powerSurroundingBlock(targetBlock);
                    case ACACIA_FENCE_GATE, BIRCH_FENCE_GATE, DARK_OAK_FENCE_GATE, JUNGLE_FENCE_GATE, OAK_FENCE_GATE, SPRUCE_FENCE_GATE -> {
                        Gate gate = (Gate) targetBlock.getBlockData();
                        gate.setOpen(!gate.isOpen());
                        targetBlock.setBlockData(gate, true);
                    }
                    default -> {
                    }
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
