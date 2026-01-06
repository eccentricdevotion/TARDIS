/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChunks;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eccentric_nz
 */
public class MonstersCommand {

    private final TARDIS plugin;

    public MonstersCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean reset(Player player, int id, String[] args) {
        if (args[1].equalsIgnoreCase("reset")) {
            Location l = null;
            for (Map.Entry<Location, TARDISTeleportLocation> map : plugin.getTrackerKeeper().getPortals().entrySet()) {
                // only portals in police box worlds
                if (map.getKey().getWorld().getName().contains("TARDIS")) {
                    continue;
                }
                if (map.getValue().getTardisId() == id) {
                    l = map.getKey();
                    break;
                }
            }
            if (l != null) {
                plugin.getTrackerKeeper().getPortals().remove(l);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "PORTAL_RESET");
                return true;
            }
        } else if (args[1].equalsIgnoreCase("kill")) {
            // get TARDIS console chunks
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetChunks rsc = new ResultSetChunks(plugin, where, true);
            if (rsc.resultSet()) {
                World world = plugin.getServer().getWorld(rsc.getWorld());
                for (HashMap<String, String> map : rsc.getData()) {
                    int x = TARDISNumberParsers.parseInt(map.get("x"));
                    int z = TARDISNumberParsers.parseInt(map.get("z"));
                    Chunk chunk = world.getChunkAt(x, z);
                    while (!chunk.isLoaded()) {
                        chunk.load();
                    }
                    for (Entity entity : chunk.getEntities()) {
                        if (entity instanceof Enemy) {
                            entity.remove();
                        }
                    }
                }
                plugin.getMessenger().send(player, TardisModule.TARDIS, "MONSTERS_RESET");
                return true;
            }
        }
        return false;
    }
}
