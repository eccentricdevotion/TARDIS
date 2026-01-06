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
package me.eccentric_nz.TARDIS.commands.dev;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.interior.TIPSData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.desktop.ChunkUtils;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.schematic.SchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ChunksCommand {

    private final TARDIS plugin;

    public ChunksCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean list(CommandSender sender) {
        if (sender instanceof Player player) {
            // get TARDIS player is in
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
            if (rs.resultSet()) {
                int id = rs.getTardis_id();
                // get TARDIS schematic
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("tardis_id", id);
                ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
                if (rst.resultSet()) {
                    Tardis tardis = rst.getTardis();
                    String[] tc = tardis.getChunk().split(":");
                    int cx = TARDISNumberParsers.parseInt(tc[1]);
                    int cz = TARDISNumberParsers.parseInt(tc[2]);
                    World world = player.getLocation().getWorld();
                    Chunk chunk = world.getChunkAt(cx, cz);
                    Schematic schematic = tardis.getSchematic();
                    for (Chunk c : ChunkUtils.getConsoleChunks(chunk, schematic)) {
                        plugin.debug(c);
                    }
                    plugin.debug("-----");
                    JsonObject obj = SchematicGZip.getObject(plugin, "consoles", schematic.getPermission(), schematic.isCustom());
                    if (obj != null) {
                        // get dimensions
                        JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                        int w = dimensions.get("width").getAsInt();
                        int d = dimensions.get("length").getAsInt() - 1;
                        Location location = getLocation(schematic, tardis, world);
                        for (Chunk c : ChunkUtils.getConsoleChunks(world, location.getChunk().getX(), location.getChunk().getZ(), w, d)) {
                            plugin.debug(c);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private Location getLocation(Schematic schematic, Tardis tardis, World world) {
        TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
        TIPSData pos = tintpos.getTIPSData(tardis.getTIPS());
        int startx = pos.getCentreX();
        int starty = schematic.getStartY();
        int startz = pos.getCentreZ();
        return new Location(world, startx, starty, startz);
    }
}
