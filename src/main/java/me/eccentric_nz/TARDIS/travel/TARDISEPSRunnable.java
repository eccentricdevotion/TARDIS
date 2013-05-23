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
    private Player p;
    private int id;

    public TARDISEPSRunnable(TARDIS plugin, String message, Player p, int id) {
        this.plugin = plugin;
        this.message = message;
        this.p = p;
        this.id = id;
    }

    @Override
    public void run() {
        Location l = getSpawnLocation(id);
        if (l != null) {
            l.setX(l.getX() + 0.5F);
            l.setZ(l.getZ() + 1.5F);
            // create NPC
            NPCRegistry registry = CitizensAPI.getNPCRegistry();
            NPC npc2 = registry.createNPC(EntityType.PLAYER, p.getName());
            npc2.spawn(l);
            int npcid = npc2.getId();
            plugin.npcIDs.add(npcid);
            p.sendMessage(ChatColor.RED + "[Emergency Program One] " + ChatColor.RESET + message);
            p.sendMessage(ChatColor.RED + "[Emergency Program One] " + ChatColor.RESET + "Right-click me to make me go away.");
            try {
                plugin.myspawn = true;
                // set some behaviours
            } catch (Exception e) {
                plugin.debug(e);
            }
        }
    }

    private Location getSpawnLocation(int id) {
        if (plugin.getConfig().getBoolean("create_worlds")) {
            // get world spawn location
            return plugin.getServer().getWorld("TARDIS_WORLD_" + p).getSpawnLocation();
        } else {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            where.put("door_type", 1);
            ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
            if (rsd.resultSet()) {
                String[] door = rsd.getDoor_location().split(":");
                World w = plugin.getServer().getWorld(door[0]);
                int x = plugin.utils.parseNum(door[1]);
                int y = plugin.utils.parseNum(door[2]);
                int z = plugin.utils.parseNum(door[3]);
                return new Location(w, x, y, z);
            } else {
                return null;
            }
        }
    }
}
