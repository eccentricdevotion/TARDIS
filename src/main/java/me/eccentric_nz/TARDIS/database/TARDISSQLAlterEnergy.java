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
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSQLAlterEnergy implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final String table;
    private final int amount;
    private final HashMap<String, Object> where;
    private final Player p;

    /**
     * Adds or removes Artron Energy from an SQLite database table. This method
     * builds an SQL query string from the parameters supplied and then executes
     * the query.
     *
     * @param plugin an instance of the main plugin class
     * @param table the database table name to insert the data into.
     * @param amount the amount of energy to add or remove (use a negative
     * value)
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to alter.
     * @param p the player who receives the success message.
     */
    public TARDISSQLAlterEnergy(TARDIS plugin, String table, int amount, HashMap<String, Object> where, Player p) {
        this.plugin = plugin;
        this.table = table;
        this.amount = amount;
        this.where = where;
        this.p = p;
    }

    @Override
    public void run() {
        Statement statement = null;
        String wheres;
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("' AND ");
            } else {
                sbw.append(entry.getValue()).append(" AND ");
            }
        }
        where.clear();
        wheres = sbw.toString().substring(0, sbw.length() - 5);
        String query = "UPDATE " + table + " SET artron_level = artron_level + " + amount + " WHERE " + wheres;
        if (amount < 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.sendMessage(plugin.pluginName + "You used " + Math.abs(amount) + " Artron Energy.");
                }
            }.runTask(plugin);
        }
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.debug("Artron Energy update error for " + table + "! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Artron Energy error closing " + table + "! " + e.getMessage());
            }
        }
    }
}
