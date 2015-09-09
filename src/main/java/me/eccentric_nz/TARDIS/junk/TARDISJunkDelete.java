/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.junk;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.commands.admin.TARDISDeleteCommand;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkDelete {

    private final TARDIS plugin;

    public TARDISJunkDelete(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean delete(final CommandSender sender) {
        if (!sender.hasPermission("tardis.admin")) {
            TARDISMessage.send(sender, "CMD_ADMIN");
            return true;
        }
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            final int id = rs.getTardis_id();
            final SCHEMATIC junk = new SCHEMATIC("AIR", "junk", "Junk TARDIS", true, false, false, false, false);
            String chunkLoc = rs.getChunk();
            String[] cdata = chunkLoc.split(":");
            final String name = cdata[0];
            final World cw = plugin.getServer().getWorld(name);
            // get the current location
            Location bb_loc = null;
            Biome biome = null;
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (rsc.resultSet()) {
                bb_loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                biome = rsc.getBiome();
            }
            if (bb_loc == null) {
                TARDISMessage.send(sender, "CURRENT_NOT_FOUND");
                return true;
            }
            // destroy junk TARDIS
            final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
            pdd.setChameleon(false);
            pdd.setDirection(COMPASS.SOUTH);
            pdd.setLocation(bb_loc);
            pdd.setDematerialise(true);
            pdd.setHide(false);
            pdd.setOutside(false);
            pdd.setSubmarine(rsc.isSubmarine());
            pdd.setTardisID(id);
            pdd.setBiome(biome);
            plugin.getPresetDestroyer().destroyPreset(pdd);
            // destroy the vortex TARDIS
            // give the TARDIS time to remove itself as it's not hidden
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getInteriorDestroyer().destroyInner(junk, id, cw, 0, "junk", -999);
                    TARDISDeleteCommand.cleanDatabase(id);
                    TARDISMessage.send(sender, "JUNK_DELETED");
                }
            }, 20L);
        }
        return true;
    }
}
