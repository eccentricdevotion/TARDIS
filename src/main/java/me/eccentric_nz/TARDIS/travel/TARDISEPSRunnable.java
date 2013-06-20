/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.travel;

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * Emergency Program One was a feature of the Doctor's TARDIS designed to return
 * a companion to a designated place in case of extreme emergency.
 *
 * @author eccentric_nz
 */
public class TARDISEPSRunnable implements Runnable {

    private TARDIS plugin;
    private String message;
    private Player tl;
    private List<String> players;
    private int id;
    private String eps;

    public TARDISEPSRunnable(TARDIS plugin, String message, Player tl, List<String> players, int id, String eps) {
        this.plugin = plugin;
        this.message = message;
        this.tl = tl;
        this.players = players;
        this.id = id;
        this.eps = eps;
    }

    @Override
    public void run() {
        Location l = getSpawnLocation(id, eps);
        if (l != null) {
            try {
                plugin.myspawn = true;
                l.setX(l.getX() + 0.5F);
                l.setZ(l.getZ() + 1.5F);
                // set yaw if TARDIS world
                if (plugin.getConfig().getBoolean("create_worlds")) {
                }
                // create NPC
                NPCRegistry registry = CitizensAPI.getNPCRegistry();
                NPC npc = registry.createNPC(EntityType.PLAYER, tl.getName());
                npc.spawn(l);
                int npcid = npc.getId();
                plugin.npcIDs.add(npcid);
                for (String p : players) {
                    Player pp = plugin.getServer().getPlayer(p);
                    if (pp != null) {
                        pp.sendMessage(ChatColor.RED + "[Emergency Program One] " + ChatColor.RESET + message);
                        pp.sendMessage(ChatColor.RED + "[Emergency Program One] " + ChatColor.RESET + "Right-click me to make me go away.");
                    }
                }
            } catch (Exception e) {
                plugin.debug(e);
            }
        }
    }

    private Location getSpawnLocation(int id, String eps) {
        if (!eps.isEmpty()) {
            String[] npc = eps.split(":");
            World w = plugin.getServer().getWorld(npc[0]);
            int x = plugin.utils.parseNum(npc[1]);
            int y = plugin.utils.parseNum(npc[2]);
            int z = plugin.utils.parseNum(npc[3]);
            return new Location(w, x, y, z);
        } else {
            if (plugin.getConfig().getBoolean("create_worlds")) {
                // get world spawn location
                return plugin.getServer().getWorld("TARDIS_WORLD_" + tl.getName()).getSpawnLocation();
            } else {
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", id);
                where.put("door_type", 1);
                ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                if (rsd.resultSet()) {
                    String[] door = rsd.getDoor_location().split(":");
                    World w = plugin.getServer().getWorld(door[0]);
                    float x = Float.parseFloat(door[1]);
                    float y = Float.parseFloat(door[2]);
                    float z = Float.parseFloat(door[3]);
                    switch (rsd.getDoor_direction()) {
                        case NORTH:
                            z -= 2F;
                            break;
                        case EAST:
                            x += 1F;
                            z -= 1F;
                            break;
                        case WEST:
                            x -= 1F;
                            z -= 1F;
                            break;
                        default:
                            break;
                    }
                    return new Location(w, x, y, z);
                } else {
                    return null;
                }
            }
        }
    }
}
