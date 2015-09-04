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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetCreeper;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Distronic explosives are powerful but unstable weapons, used on many worlds
 * as components of explosive warheads attached to missiles.
 *
 * @author eccentric_nz
 */
public class TARDISExplosionListener implements Listener {

    private final TARDIS plugin;

    public TARDISExplosionListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for explosions around the TARDIS Police Box. If the explosion
     * affects any of the Police Box blocks, then those blocks are removed from
     * the effect of the explosion, there by protecting the Police box from
     * damage.
     *
     * @param e an entity exploding
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityExplode(EntityExplodeEvent e) {
        Location explode = e.getLocation();
        // check if the explosion is in a TARDIS world
        if ((explode.getWorld().getName().contains("TARDIS") || explode.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) && e.getEntity() instanceof Creeper) {
            e.setCancelled(true);
            // check it is not the Artron creeper
            String loc_chk = explode.getWorld().getName() + ":" + (explode.getBlockX() + 0.5f) + ":" + (explode.getBlockY() - 1) + ":" + (explode.getBlockZ() + 0.5f);
            if (new ResultSetCreeper(plugin, loc_chk).resultSet() == false) {
                // create a new explosion that doesn't destroy blocks or set fire
                explode.getWorld().createExplosion(explode.getX(), explode.getY(), explode.getZ(), 4.0f, false, false);
            }
        } else {
            int idchk = 0;
            // get list of police box blocks from DB
            HashMap<String, Object> whereb = new HashMap<String, Object>();
            whereb.put("police_box", 1);
            ResultSetBlocks rs = new ResultSetBlocks(plugin, whereb, true);
            if (rs.resultSet()) {
                ArrayList<HashMap<String, String>> data = rs.getData();
                for (HashMap<String, String> map : data) {
                    int id = TARDISNumberParsers.parseInt(map.get("tardis_id"));
                    Location loc = plugin.getLocationUtils().getLocationFromBukkitString(map.get("location"));
                    if (loc != null) {
                        Block block = loc.getBlock();
                        // if the block is a TARDIS block then remove it
                        if (e.blockList().contains(block)) {
                            e.blockList().remove(block);
                        }
                        if (id != idchk) {
                            HashMap<String, Object> where = new HashMap<String, Object>();
                            where.put("tardis_id", id);
                            ResultSetDoors rsd = new ResultSetDoors(plugin, where, true);
                            if (rsd.resultSet()) {
                                String doorLoc[] = rsd.getDoor_location().split(":");
                                COMPASS d = rsd.getDoor_direction();
                                World w = plugin.getServer().getWorld(doorLoc[0]);
                                int dx = TARDISNumberParsers.parseInt(doorLoc[1]);
                                int dy = TARDISNumberParsers.parseInt(doorLoc[2]);
                                int dz = TARDISNumberParsers.parseInt(doorLoc[3]);
                                Block door_bottom = w.getBlockAt(dx, dy, dz);
                                Block door_under = door_bottom.getRelative(BlockFace.DOWN);
                                Block door_top = door_bottom.getRelative(BlockFace.UP);
                                BlockFace bf;
                                switch (d) {
                                    case NORTH:
                                        bf = BlockFace.WEST;
                                        break;
                                    case WEST:
                                        bf = BlockFace.SOUTH;
                                        break;
                                    case SOUTH:
                                        bf = BlockFace.EAST;
                                        break;
                                    default:
                                        bf = BlockFace.NORTH;
                                        break;
                                }
                                Block sign = door_top.getRelative(BlockFace.UP).getRelative(bf);
                                if (e.blockList().contains(sign)) {
                                    e.blockList().remove(sign);
                                }
                                if (e.blockList().contains(door_under)) {
                                    e.blockList().remove(door_under);
                                }
                                if (e.blockList().contains(door_bottom)) {
                                    e.blockList().remove(door_bottom);
                                }
                                if (e.blockList().contains(door_top)) {
                                    e.blockList().remove(door_top);
                                }
                            }
                            idchk = id;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getCause() != DamageCause.ENTITY_EXPLOSION) {
            return;
        }
        String l = e.getDamager().getLocation().getWorld().getName();
        if (l.contains("TARDIS") || l.equals(plugin.getConfig().getString("creation.default_world_name"))) {
            e.setCancelled(true);
        }
    }
}
