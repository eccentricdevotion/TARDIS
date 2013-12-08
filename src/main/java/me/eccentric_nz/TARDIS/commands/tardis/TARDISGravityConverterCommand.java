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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISGravityConverterCommand {

    private final TARDIS plugin;

    public TARDISGravityConverterCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean convertGravity(CommandSender sender, Player player, String[] args) {
        if (player == null) {
            sender.sendMessage(plugin.pluginName + "Must be a player");
            return false;
        }
        // get the players TARDIS id
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", player.getName());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            sender.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
            return false;
        }
        int id = rs.getTardis_id();
        try {
            TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
            Connection connection = service.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM gravity WHERE tardis_id = " + id;
            ResultSet rsg = statement.executeQuery(query);
            if (rsg.isBeforeFirst()) {
                String up = "Location{world=" + rsg.getString("world") + ",x=" + rsg.getFloat("upx") + ",y=10.0,z=" + rsg.getFloat("upz") + ",pitch=0.0,yaw=0.0}";
                Double[] values = new Double[3];
                values[0] = 1D;
                values[1] = 11D;
                values[2] = 0.5D;
                plugin.gravityUpList.put(up, values);
                String down = "Location{world=" + rsg.getString("world") + ",x=" + rsg.getFloat("downx") + ",y=10.0,z=" + rsg.getFloat("downz") + ",pitch=0.0,yaw=0.0}";
                plugin.gravityDownList.add(down);
                HashMap<String, Object> setu = new HashMap<String, Object>();
                setu.put("tardis_id", id);
                setu.put("location", up);
                setu.put("direction", 1);
                setu.put("distance", 11);
                setu.put("velocity", 0.5);
                HashMap<String, Object> setd = new HashMap<String, Object>();
                setd.put("tardis_id", id);
                setd.put("location", down);
                setd.put("direction", 0);
                setd.put("distance", 0);
                setd.put("velocity", 0);
                QueryFactory qf = new QueryFactory(plugin);
                qf.doInsert("gravity_well", setu);
                qf.doInsert("gravity_well", setd);
                player.sendMessage(plugin.pluginName + "Gravity well converted successfully.");
                return true;
            }
        } catch (SQLException e) {
            plugin.debug("Gravity conversion error: " + e.getMessage());
            return false;
        }
        return false;
    }
}
