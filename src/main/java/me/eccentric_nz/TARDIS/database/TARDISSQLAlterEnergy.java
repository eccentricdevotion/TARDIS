/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISArtronEvent;
import me.eccentric_nz.TARDIS.artron.TARDISArtronIndicator;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISSQLAlterEnergy implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String table;
    private final int amount;
    private final HashMap<String, Object> where;
    private final Player p;
    private final String prefix;

    /**
     * Adds or removes Artron Energy from an SQLite database table. This method builds an SQL query string from the
     * parameters supplied and then executes the query.
     *
     * @param plugin an instance of the main plugin class
     * @param table  the database table name to insert the data into.
     * @param amount the amount of energy to add or remove (use a negative value)
     * @param where  a HashMap<String, Object> of table fields and values to select the records to alter.
     * @param p      the player who receives the success message.
     */
    public TARDISSQLAlterEnergy(TARDIS plugin, String table, int amount, HashMap<String, Object> where, Player p) {
        this.plugin = plugin;
        this.table = table;
        this.amount = amount;
        this.where = where;
        this.p = p;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        Statement statement = null;
        String wheres;
        StringBuilder sbw = new StringBuilder();
        where.entrySet().forEach((entry) -> {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class) || entry.getValue().getClass().equals(UUID.class)) {
                sbw.append("'").append(entry.getValue()).append("' AND ");
            } else {
                sbw.append(entry.getValue()).append(" AND ");
            }
        });
        int tmp = 0;
        if (where.containsKey("tardis_id")) {
            tmp = (Integer) where.get("tardis_id");
        }
        int id = tmp;
        where.clear();
        wheres = sbw.toString().substring(0, sbw.length() - 5);
        String query = "UPDATE " + prefix + table + " SET artron_level = artron_level + " + amount + " WHERE " + wheres;
        if (amount < 0 && p != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (id > 0) {
                        new TARDISArtronIndicator(plugin).showArtronLevel(p, id, Math.abs(amount));
                        plugin.getPM().callEvent(new TARDISArtronEvent(p, amount, id));
                    } else {
                        TARDISMessage.send(p, "ENERGY_USED", String.format("%d", Math.abs(amount)));
                    }
                }
            }.runTask(plugin);
        }
        try {
            service.testConnection(connection);
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
