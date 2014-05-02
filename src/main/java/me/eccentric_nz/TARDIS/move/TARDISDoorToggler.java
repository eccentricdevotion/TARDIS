/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDoorToggler {

    private final TARDIS plugin;
    private final Block block;
    private final COMPASS dd;
    private final Player player;
    private final boolean minecart;
    private final boolean playsound;
    private final int doortype;
    private final int id;

    public TARDISDoorToggler(TARDIS plugin, Block block, COMPASS dd, Player player, boolean minecart, boolean playsound, int doortype, int id) {
        this.plugin = plugin;
        this.block = block;
        this.dd = dd;
        this.player = player;
        this.minecart = minecart;
        this.playsound = playsound;
        this.doortype = doortype;
        this.id = id;
    }

    /**
     * Toggle the door open and closed.
     */
    @SuppressWarnings("deprecation")
    public void toggleDoor() {
        if (isTogglable(block)) {
            boolean open = true;
            Block door_bottom;
            Door door = (Door) block.getState().getData();
            door_bottom = (door.isTopHalf()) ? block.getRelative(BlockFace.DOWN) : block;
            byte door_data = door_bottom.getData();
            switch (dd) {
                case NORTH:
                    if (door_data == 3) {
                        door_bottom.setData((byte) 7, false);
                    } else {
                        door_bottom.setData((byte) 3, false);
                        open = false;
                    }
                    break;
                case WEST:
                    if (door_data == 2) {
                        door_bottom.setData((byte) 6, false);
                    } else {
                        door_bottom.setData((byte) 2, false);
                        open = false;
                    }
                    break;
                case SOUTH:
                    if (door_data == 1) {
                        door_bottom.setData((byte) 5, true);
                    } else {
                        door_bottom.setData((byte) 1, false);
                        open = false;
                    }
                    break;
                default:
                    if (door_data == 0) {
                        door_bottom.setData((byte) 4, false);
                    } else {
                        door_bottom.setData((byte) 0, false);
                        open = false;
                    }
                    break;
            }
            if (playsound) {
                // get all companion UUIDs as well
                List<UUID> uuids = new ArrayList<UUID>();
                uuids.add(player.getUniqueId());
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", id);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    String[] companions = rs.getCompanions().split(":");
                    for (String c : companions) {
                        if (!c.isEmpty()) {
                            uuids.add(UUID.fromString(c));
                        }
                    }
                }
                if (open) {
                    if (doortype == 0 || (doortype == 1 && !checkForSpace(door_bottom))) {
                        // only add them if they're not there already!
                        for (UUID uuid : uuids) {
                            if (!plugin.getTrackerKeeper().getTrackMover().contains(uuid)) {
                                plugin.getTrackerKeeper().getTrackMover().add(uuid);
                            }
                        }
                    }
                } else {
                    for (UUID uuid : uuids) {
                        if (plugin.getTrackerKeeper().getTrackMover().contains(uuid)) {
                            plugin.getTrackerKeeper().getTrackMover().remove(uuid);
                        }
                    }
                }
            }
            if (playsound) {
                playDoorSound(player, open, block.getLocation(), minecart);
            }
        }
    }

    /**
     * Plays a door sound when the iron door is clicked.
     *
     * @param p a player to play the sound for
     * @param sound which sound to play, open (true), close (false)
     * @param l a location to play the sound at
     * @param m whether to play the custom sound (false) or the Minecraft one
     * (true)
     */
    private void playDoorSound(Player p, boolean sound, Location l, boolean m) {
        if (sound) {
            if (!m) {
                plugin.getUtils().playTARDISSound(l, p, "tardis_door_open");
            } else {
                p.playSound(p.getLocation(), Sound.DOOR_OPEN, 1.0F, 1.0F);
            }
        } else {
            if (!m) {
                plugin.getUtils().playTARDISSound(l, p, "tardis_door_close");
            } else {
                p.playSound(p.getLocation(), Sound.DOOR_OPEN, 1.0F, 1.0F);
            }
        }
    }

    private boolean isTogglable(Block b) {
        return block.getType().equals(Material.IRON_DOOR_BLOCK) || block.getType().equals(Material.WOODEN_DOOR);
    }

    private boolean checkForSpace(Block b) {
        return (b.getRelative(BlockFace.NORTH).getType().equals(Material.AIR)
                && b.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP).getType().equals(Material.AIR));
    }
}
