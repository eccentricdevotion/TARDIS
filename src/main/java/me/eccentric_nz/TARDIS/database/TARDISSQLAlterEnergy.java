/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.database;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.event.TardisArtronEvent;
import me.eccentric_nz.tardis.artron.TardisArtronIndicator;
import me.eccentric_nz.tardis.messaging.TardisMessage;
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
class TardisSqlAlterEnergy implements Runnable {

    private final TardisPlugin plugin;
    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String table;
    private final int amount;
    private final HashMap<String, Object> where;
    private final Player player;
    private final String prefix;

    /**
     * Adds or removes Artron Energy from an SQLite database table. This method builds an SQL query string from the
     * parameters supplied and then executes the query.
     *
     * @param plugin an instance of the main plugin class
     * @param table  the database table name to insert the data into.
     * @param amount the amount of energy to add or remove (use a negative value)
     * @param where  a HashMap&lt;String, Object&gt; of table fields and values to select the records to alter.
     * @param player the player who receives the success message.
     */
    TardisSqlAlterEnergy(TardisPlugin plugin, String table, int amount, HashMap<String, Object> where, Player player) {
        this.plugin = plugin;
        this.table = table;
        this.amount = amount;
        this.where = where;
        this.player = player;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        Statement statement = null;
        String wheres;
        StringBuilder sbw = new StringBuilder();
        where.forEach((key, value) -> {
            sbw.append(key).append(" = ");
            if (value instanceof String || value instanceof UUID) {
                sbw.append("'").append(value).append("' AND ");
            } else {
                sbw.append(value).append(" AND ");
            }
        });
        int tmp = 0;
        if (where.containsKey("tardis_id")) {
            tmp = (Integer) where.get("tardis_id");
        }
        int id = tmp;
        where.clear();
        wheres = sbw.substring(0, sbw.length() - 5);
        String query = "UPDATE " + prefix + table + " SET artron_level = artron_level + " + amount + " WHERE " + wheres;
        if (amount < 0 && player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (id > 0) {
                        new TardisArtronIndicator(plugin).showArtronLevel(player, id, Math.abs(amount));
                        plugin.getPM().callEvent(new TardisArtronEvent(player, amount, id));
                    } else {
                        TardisMessage.send(player, "ENERGY_USED", String.format("%d", Math.abs(amount)));
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
