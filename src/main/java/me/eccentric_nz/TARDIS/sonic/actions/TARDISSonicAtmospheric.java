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
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.TARDISAtmosphericExcitation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISSonicAtmospheric {

    public static void makeItSnow(TARDIS plugin, Player player, Block b) {
        // check the text on the sign
        Sign sign = (Sign) b.getState();
        SignSide front = sign.getSide(Side.FRONT);
        String line0 = ComponentUtils.stripColour(front.line(0));
        String line1 = ComponentUtils.stripColour(front.line(1));
        String line2 = ComponentUtils.stripColour(front.line(2));
        if (isPresetSign(plugin, line0, line1, line2)) {
            // get TARDIS id
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (rs.fromUUID(player.getUniqueId().toString())) {
                int tid = rs.getTardisId();
                Block blockbehind = null;
                Directional directional = (Directional) b.getBlockData();
                if (directional.getFacing().equals(BlockFace.WEST)) {
                    blockbehind = b.getRelative(BlockFace.EAST, 2);
                }
                if (directional.getFacing().equals(BlockFace.EAST)) {
                    blockbehind = b.getRelative(BlockFace.WEST, 2);
                }
                if (directional.getFacing().equals(BlockFace.SOUTH)) {
                    blockbehind = b.getRelative(BlockFace.NORTH, 2);
                }
                if (directional.getFacing().equals(BlockFace.NORTH)) {
                    blockbehind = b.getRelative(BlockFace.SOUTH, 2);
                }
                if (blockbehind != null) {
                    Block blockDown = blockbehind.getRelative(BlockFace.DOWN, 2);
                    Location bd_loc = blockDown.getLocation();
                    HashMap<String, Object> wherecl = new HashMap<>();
                    wherecl.put("tardis_id", tid);
                    wherecl.put("world", bd_loc.getWorld().getName());
                    wherecl.put("x", bd_loc.getBlockX());
                    wherecl.put("y", bd_loc.getBlockY());
                    wherecl.put("z", bd_loc.getBlockZ());
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (rsc.resultSet() && !plugin.getTrackerKeeper().getExcitation().contains(player.getUniqueId())) {
                        new TARDISAtmosphericExcitation(plugin).excite(tid, player);
                        plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
                    }
                }
            }
        }
    }

    private static boolean isPresetSign(TARDIS plugin, String l0, String l1, String l2) {
        if (l0.equalsIgnoreCase("WEEPING") || l0.equalsIgnoreCase("$50,000")) {
            return (plugin.getGeneralKeeper().getSign_lookup().containsKey(l0) && l1.equals(plugin.getGeneralKeeper().getSign_lookup().get(l0)));
        } else {
            return (plugin.getGeneralKeeper().getSign_lookup().containsKey(l1) && l2.equals(plugin.getGeneralKeeper().getSign_lookup().get(l1)));
        }
    }
}
