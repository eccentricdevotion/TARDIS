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
package me.eccentric_nz.TARDIS.commands.dev;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISFloodgate;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISAddRegionsCommand {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private WorldGuardPlugin wg;

    TARDISAddRegionsCommand(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
        if (plugin.isWorldGuardOnServer()) {
            wg = (WorldGuardPlugin) plugin.getPM().getPlugin("WorldGuard");
        }
    }

    boolean doCheck(CommandSender sender) {
        if (!plugin.isWorldGuardOnServer()) {
            sender.sendMessage(plugin.getPluginName() + "WorldGuard is not enabled on this server!");
            return true;
        }
        if (!plugin.getConfig().getBoolean("creation.default_world")) {
            sender.sendMessage(plugin.getPluginName() + "This command only works if TARDISes are created in a default world!");
            return true;
        }
        // get default world name
        String dw = plugin.getConfig().getString("creation.default_world_name");
        // get and load the regions.yml file for this world
        String world_folder = "worlds" + File.separator + dw + File.separator;
        File configFile = new File(wg.getDataFolder(), world_folder + "regions.yml");
        if (!configFile.exists()) {
            plugin.debug("Can't find default world regions.yml file!");
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        // get all TARDISes
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT uuid, owner, tips FROM " + prefix + "tardis";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String tl = rs.getString("owner");
                    int t = rs.getInt("tips");
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    if (t >= 0) {
                        // check if region name exists
                        String rn = (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(uuid)) ? "TARDIS_" + TARDISFloodgate.sanitisePlayerName(tl) : "TARDIS_" + tl;
                        if (!config.contains("regions." + rn)) {
                            TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                            TARDISTIPSData td = tintpos.getTIPSData(t);
                            plugin.getWorldGuardUtils().addWGProtection(uuid, tl, td, TARDISAliasResolver.getWorldFromAlias(dw));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing tardis table! " + e.getMessage());
            }
        }
        return true;
    }
}
