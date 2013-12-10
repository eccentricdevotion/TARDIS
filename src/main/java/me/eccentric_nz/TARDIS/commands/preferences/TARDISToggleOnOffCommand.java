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
package me.eccentric_nz.TARDIS.commands.preferences;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISToggleOnOffCommand {

    private final TARDIS plugin;
    private final List<String> was;

    public TARDISToggleOnOffCommand(TARDIS plugin) {
        this.plugin = plugin;
        this.was = Arrays.asList(new String[]{"auto", "beacon", "dnd", "platform", "eps", "hads", "plain", "submarine"});
    }

    public boolean doAbort(Player player, String[] args, QueryFactory qf) {
        String pref = args[0];
        HashMap<String, Object> setp = new HashMap<String, Object>();
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("player", player.getName());
        String grammar = (was.contains(pref)) ? " was" : " were";
        if (args[1].equalsIgnoreCase("on")) {
            setp.put(pref + "_on", 1);
            if (pref.equals("beacon")) {
                toggleBeacon(player.getName(), true);
            }
            player.sendMessage(plugin.pluginName + pref + grammar + " turned ON!");
        }
        if (args[1].equalsIgnoreCase("off")) {
            setp.put(pref + "_on", 0);
            if (pref.equals("beacon")) {
                toggleBeacon(player.getName(), false);
            }
            player.sendMessage(plugin.pluginName + pref + grammar + " turned OFF.");
        }
        qf.doUpdate("player_prefs", setp, wherep);
        return true;
    }

    private void toggleBeacon(String name, boolean on) {
        HashMap<String, Object> whereb = new HashMap<String, Object>();
        whereb.put("owner", name);
        ResultSetTardis rs = new ResultSetTardis(plugin, whereb, "", false);
        if (rs.resultSet()) {
            // toggle beacon
            String beacon = rs.getBeacon();
            String[] beaconData;
            int plusy = 0;
            if (beacon.isEmpty()) {
                // get the location from the TARDIS size and the creeper location
                switch (rs.getSchematic()) {
                    // TODO - Find out how far away the bedrock block is
                    case REDSTONE:
                        plusy = 18;
                        break;
                    case ELEVENTH:
                        plusy = 17;
                        break;
                    case DELUXE:
                        plusy = 16;
                        break;
                    case BIGGER:
                        plusy = 13;
                        break;
                    default: // BUDGET & STEAMPUNK
                        plusy = 12;
                        break;
                }
                String creeper = rs.getCreeper();
                beaconData = creeper.split(":");
            } else {
                beaconData = beacon.split(":");
            }
            World w = plugin.getServer().getWorld(beaconData[0]);
            float bx = 0, by = 0, bz = 0;
            try {
                bx = Float.parseFloat(beaconData[1]);
                by = Float.parseFloat(beaconData[2]) + plusy;
                bz = Float.parseFloat(beaconData[3]);
            } catch (NumberFormatException nfe) {
                plugin.debug("Couldn't convert to a float! " + nfe.getMessage());
            }
            if (beacon.isEmpty()) {
                // update the tardis table so we don't have to do this again
                String beacon_loc = beaconData[0] + ":" + beaconData[1] + ":" + by + ":" + beaconData[3];
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("beacon", beacon_loc);
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", rs.getTardis_id());
                new QueryFactory(plugin).doUpdate("tardis", set, where);
            }
            Location bl = new Location(w, bx, by, bz);
            Block b = bl.getBlock();
            b.setTypeId((on) ? 20 : 7);
        }
    }
}
