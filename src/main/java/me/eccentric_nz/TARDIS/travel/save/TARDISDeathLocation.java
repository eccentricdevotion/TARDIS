package me.eccentric_nz.TARDIS.travel.save;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class TARDISDeathLocation {

    private final TARDIS plugin;

    public TARDISDeathLocation(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void record(Player player, Location location) {
        TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
        Connection connection = service.getConnection();
        String uuid = player.getUniqueId().toString();
        Statement statement = null;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            String select = "SELECT uuid FROM " + plugin.getPrefix() + "deaths WHERE uuid = '" + uuid + "'";
            ResultSet rs = statement.executeQuery(select);
            HashMap<String, Object> set = new HashMap<>();
            if (location == null) {
                set.put("world", "Could not be saved!");
                return;
            } else {
                set.put("world", location.getWorld().getName());
                set.put("x", location.getBlockX());
                set.put("y", location.getBlockY());
                set.put("z", location.getBlockZ());
                COMPASS player_d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                set.put("direction", player_d.toString());
                set.put("submarine", isSub(location) ? 1 : 0);
            }
            if (rs.isBeforeFirst()) {
                // update
                HashMap<String, Object> uid = new HashMap<>();
                uid.put("uuid", uuid);
                plugin.getQueryFactory().doUpdate("deaths", set, uid);
            } else {
                // insert
                set.put("uuid", uuid);
                plugin.getQueryFactory().doInsert("deaths", set);
            }
        } catch (SQLException e) {
            plugin.debug("Upsert deaths error! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing upsert deaths statement! " + e.getMessage());
            }
        }
    }

    private boolean isSub(Location l) {
        return l.getBlock().getType().equals(Material.WATER);
    }

    public Location getLocation(Player player) {
        // check they are not in a TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (rst.resultSet()) {
            return null;
        }
        Location location = player.getLocation();
        if (plugin.getTardisArea().isInExistingArea(location)) {
            return null;
        }
        String world = location.getWorld().getName();
        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && world.equals(plugin.getConfig().getString("creation.default_world_name"))) {
            return null;
        }
        // check the world is not excluded
        if (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".time_travel")) {
            return null;
        }
        if (!plugin.getPluginRespect().getRespect(location, new Parameters(player, Flag.getDefaultFlags()))) {
            return null;
        }
        // check for space
        Block b = location.getBlock();
        boolean unsafe = true;
        while (unsafe) {
            boolean clear = true;
            for (BlockFace f : plugin.getGeneralKeeper().getSurrounding()) {
                if (!TARDISConstants.GOOD_MATERIALS.contains(b.getRelative(f).getType())) {
                    b = b.getRelative(BlockFace.UP);
                    clear = false;
                    break;
                }
            }
            unsafe = !clear;
        }
        // if no space get the highest block
        location.setY(b.getY());
        return location;
    }
}
