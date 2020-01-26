package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.TARDISAtmosphericExcitation;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISSonicAtmospheric {

    public static void makeItSnow(TARDIS plugin, Player player, Block b) {
        // check the text on the sign
        Sign sign = (Sign) b.getState();
        String line0 = ChatColor.stripColor(sign.getLine(0));
        String line1 = ChatColor.stripColor(sign.getLine(1));
        String line2 = ChatColor.stripColor(sign.getLine(2));
        if (isPresetSign(plugin, line0, line1, line2)) {
            // get TARDIS id
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (rs.fromUUID(player.getUniqueId().toString())) {
                int tid = rs.getTardis_id();
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
                    if (rsc.resultSet()) {
                        new TARDISAtmosphericExcitation(plugin).excite(tid, player);
                        plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
                        return;
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
