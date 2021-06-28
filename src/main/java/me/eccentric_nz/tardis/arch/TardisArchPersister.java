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
package me.eccentric_nz.tardis.arch;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisArchPersister {

    private final TardisPlugin plugin;
    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public TardisArchPersister(TardisPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void saveAll() {
        try {
            // save the arched players
            ps = connection.prepareStatement("INSERT INTO " + prefix + "arched (uuid, arch_name, arch_time) VALUES (?, ?, ?)");
            for (Map.Entry<UUID, TardisWatchData> map : plugin.getTrackerKeeper().getJohnSmith().entrySet()) {
                ps = connection.prepareStatement("SELECT uuid FROM " + prefix + "arched WHERE uuid = ?");
                ps.setString(1, map.getKey().toString());
                rs = ps.executeQuery();
                TardisWatchData twd = map.getValue();
                long time = 0;
                long now = System.currentTimeMillis();
                if (now < twd.getTime()) {
                    // get time remaining
                    time = twd.getTime() - now;
                }
                if (rs.next()) {
                    // update the existing record
                    ps = connection.prepareStatement("UPDATE " + prefix + "arched SET arch_name = ?, arch_time = ? WHERE uuid = ?");
                    ps.setString(1, twd.getName());
                    ps.setLong(2, time);
                    ps.setString(3, map.getKey().toString());
                } else {
                    // save the arched player
                    ps = connection.prepareStatement("INSERT INTO " + prefix + "arched (uuid, arch_name, arch_time) VALUES (?, ?, ?)");
                    ps.setString(1, map.getKey().toString());
                    ps.setString(2, twd.getName());
                    ps.setLong(3, time);
                }
                count += ps.executeUpdate();
            }
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + count + " 'arched' players.");
        } catch (SQLException sqlException) {
            plugin.debug("Insert error for arched table: " + sqlException.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException sqlException) {
                plugin.debug("Error closing arched statement: " + sqlException.getMessage());
            }
        }
    }

    public void save(UUID uuid) {
        try {
            ps = connection.prepareStatement("SELECT uuid FROM " + prefix + "arched WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            TardisWatchData twd = plugin.getTrackerKeeper().getJohnSmith().get(uuid);
            long time = 0;
            long now = System.currentTimeMillis();
            if (now < twd.getTime()) {
                // get time remaining
                time = twd.getTime() - now;
            }
            if (rs.next()) {
                // update the existing record
                ps = connection.prepareStatement("UPDATE " + prefix + "arched SET arch_name = ?, arch_time = ? WHERE uuid = ?");
                ps.setString(1, twd.getName());
                ps.setLong(2, time);
                ps.setString(3, uuid.toString());
            } else {
                // save the arched player
                ps = connection.prepareStatement("INSERT INTO " + prefix + "arched (uuid, arch_name, arch_time) VALUES (?, ?, ?)");
                ps.setString(1, uuid.toString());
                ps.setString(2, twd.getName());
                ps.setLong(3, time);
            }
            count += ps.executeUpdate();
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + count + " 'arched' player.");
        } catch (SQLException sqlException) {
            plugin.debug("Insert error for arched table: " + sqlException.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException sqlException) {
                plugin.debug("Error closing arched statement: " + sqlException.getMessage());
            }
        }
    }

    public void reArch(UUID uuid) {
        try {
            ps = connection.prepareStatement("SELECT * FROM " + prefix + "arched WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                Player player = plugin.getServer().getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    // disguise the player
                    String name = rs.getString("arch_name");
                    long time = System.currentTimeMillis() + rs.getLong("arch_time");
                    TardisWatchData twd = new TardisWatchData(name, time);
                    plugin.getTrackerKeeper().getJohnSmith().put(uuid, twd);
                    if (plugin.isDisguisesOnServer()) {
                        TardisArchLibsDisguise.undisguise(player);
                    } else {
                        TardisArchDisguise.undisguise(player);
                    }
                    if (plugin.isDisguisesOnServer()) {
                        TardisArchLibsDisguise.disguise(player, name);
                    } else {
                        TardisArchDisguise.disguise(player, name);
                    }
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        player.setDisplayName(name);
                        player.setPlayerListName(name);
                    }, 5L);
                }
            }
        } catch (SQLException sqlException) {
            plugin.debug("ResultSet error for arched table: " + sqlException.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException sqlException) {
                plugin.debug("Error closing arched statement or resultset: " + sqlException.getMessage());
            }
        }
    }

    public void checkAll() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            try {
                ps = connection.prepareStatement("SELECT * FROM " + prefix + "arched");
                rs = ps.executeQuery();
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        Player player = plugin.getServer().getPlayer(UUID.fromString(rs.getString("uuid")));
                        if (player != null && player.isOnline()) {
                            // disguise the player
                            String name = rs.getString("arch_name");
                            long time = System.currentTimeMillis() + rs.getLong("arch_time");
                            TardisWatchData twd = new TardisWatchData(name, time);
                            plugin.getTrackerKeeper().getJohnSmith().put(player.getUniqueId(), twd);
                            if (plugin.isDisguisesOnServer()) {
                                TardisArchLibsDisguise.undisguise(player);
                            } else {
                                TardisArchDisguise.undisguise(player);
                            }
                            if (plugin.isDisguisesOnServer()) {
                                TardisArchLibsDisguise.disguise(player, name);
                            } else {
                                TardisArchDisguise.disguise(player, name);
                            }
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                player.setDisplayName(name);
                                player.setPlayerListName(name);
                            }, 5L);
                        }
                    }
                }
            } catch (SQLException sqlException) {
                plugin.debug("ResultSet error for arched table: " + sqlException.getMessage());
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException sqlException) {
                    plugin.debug("Error closing arched statement or resultset: " + sqlException.getMessage());
                }
            }
        }, 30L);
    }

    void removeArch(UUID uuid) {
        try {
            ps = connection.prepareStatement("DELETE FROM " + prefix + "arched WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            plugin.debug("ResultSet error for arched table: " + sqlException.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqlException) {
                plugin.debug("Error closing arched statement or resultset: " + sqlException.getMessage());
            }
        }
    }
}
