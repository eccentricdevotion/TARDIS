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
package me.eccentric_nz.TARDIS.schematic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eccentric_nz
 */
public class ResultSetArchiveButtons {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String uuid;
    private final ItemStack[] buttons;
    private final String prefix;
    private final Material[] terracotta = {Material.WHITE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.MAGENTA_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA, Material.YELLOW_TERRACOTTA, Material.LIME_TERRACOTTA, Material.PINK_TERRACOTTA, Material.GRAY_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA, Material.BLACK_TERRACOTTA};

    public ResultSetArchiveButtons(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
        buttons = new ItemStack[this.plugin.getConfig().getInt("archive.limit")];
    }

    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "archive WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            int i = 0;
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    ItemStack is = new ItemStack(terracotta[i], 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(rs.getString("name"));
                    List<String> lore = new ArrayList<>();
                    if (!rs.getString("description").isEmpty()) {
                        Pattern p = Pattern.compile("\\G\\s*(.{1,25})(?=\\s|$)", Pattern.DOTALL);
                        Matcher m = p.matcher(rs.getString("description"));
                        while (m.find()) {
                            lore.add(m.group(1));
                        }
                    }
                    // add cost
                    int cost = plugin.getArtronConfig().getInt("upgrades.archive." + rs.getString("console_size").toLowerCase(Locale.ENGLISH));
                    lore.add("Cost: " + cost);
                    // add current
                    if (rs.getInt("use") == 1) {
                        lore.add(ChatColor.GREEN + plugin.getLanguage().getString("CURRENT_CONSOLE"));
                    }
                    im.setLore(lore);
                    is.setItemMeta(im);
                    buttons[i] = is;
                    i++;
                    if (i > 15) {
                        break;
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for archive buttons! " + e.getMessage());
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
                plugin.debug("Error closing archive buttons! " + e.getMessage());
            }
        }
        return true;
    }

    public ItemStack[] getButtons() {
        return buttons;
    }
}
