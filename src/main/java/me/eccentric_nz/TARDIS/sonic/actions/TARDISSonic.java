/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TARDISSonic {

    private static final List<Material> distance = new ArrayList<>();

    static {
        distance.add(Material.ACACIA_BUTTON);
        distance.add(Material.ACACIA_DOOR);
        distance.add(Material.BIRCH_BUTTON);
        distance.add(Material.BIRCH_DOOR);
        distance.add(Material.DARK_OAK_BUTTON);
        distance.add(Material.DARK_OAK_DOOR);
        distance.add(Material.IRON_DOOR);
        distance.add(Material.JUNGLE_BUTTON);
        distance.add(Material.JUNGLE_DOOR);
        distance.add(Material.LEVER);
        distance.add(Material.OAK_BUTTON);
        distance.add(Material.OAK_DOOR);
        distance.add(Material.SPRUCE_BUTTON);
        distance.add(Material.SPRUCE_DOOR);
        distance.add(Material.STONE_BUTTON);
    }

    public static void standardSonic(TARDIS plugin, Player player) {
        Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation().getBlock();
        Material blockType = targetBlock.getType();
        if (distance.contains(blockType)) {
            // not protected doors - WorldGuard / GriefPrevention / Lockette / Towny
            if (!TARDISSonicRespect.checkBlockRespect(plugin, player, targetBlock)) {
                switch (blockType) {
                    case ACACIA_DOOR:
                    case BIRCH_DOOR:
                    case DARK_OAK_DOOR:
                    case IRON_DOOR:
                    case JUNGLE_DOOR:
                    case OAK_DOOR:
                    case SPRUCE_DOOR:
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
                        // not protected doors - WorldGuard / GriefPrevention / Lockette / Towny
                        boolean allow = !TARDISSonicRespect.checkBlockRespect(plugin, player, lowerdoor);
                        if (allow) {
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
                        break;
                    case LEVER:
                        Powerable lever = (Powerable) targetBlock.getBlockData();
                        lever.setPowered(!lever.isPowered());
                        targetBlock.setBlockData(lever, true);
                        break;
                    case ACACIA_BUTTON:
                    case BIRCH_BUTTON:
                    case DARK_OAK_BUTTON:
                    case JUNGLE_BUTTON:
                    case OAK_BUTTON:
                    case SPRUCE_BUTTON:
                    case STONE_BUTTON:
                        Powerable button = (Powerable) targetBlock.getBlockData();
                        button.setPowered(true);
                        targetBlock.setBlockData(button, true);
                        long delay = (blockType.equals(Material.STONE_BUTTON)) ? 20L : 30L;
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            button.setPowered(false);
                            targetBlock.setBlockData(button);
                        }, delay);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
