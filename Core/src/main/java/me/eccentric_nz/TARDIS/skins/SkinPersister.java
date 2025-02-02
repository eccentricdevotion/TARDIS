/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.skins;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class SkinPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public SkinPersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void load() {
        try {
            ps = connection.prepareStatement("SELECT * FROM " + prefix + "skins");
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    Skin skin = SkinUtils.deserializeSkin(rs.getString("skin"));
                    SkinUtils.SKINNED.put(uuid, skin);
                    count++;
                }
            }
            if (count > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Loaded " + count + " skins.");
            }
            // clear the skins table, so we don't get any duplicates when saving them
            ps = connection.prepareStatement("DELETE FROM " + prefix + "skins");
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for skins table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing skins statement or resultset: " + ex.getMessage());
            }
        }
    }

    public void save() {
        // save the skins
        try {
            ps = connection.prepareStatement("INSERT INTO " + prefix + "skins (uuid, skin) VALUES (?,?)");
            for (Map.Entry<UUID, Skin> map : SkinUtils.SKINNED.entrySet()) {
                String skin = SkinUtils.serializeSkin(map.getValue());
                ps.setString(1, map.getKey().toString());
                ps.setString(2, skin);
                count += ps.executeUpdate();
            }
            if (count > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Saved " + count + " skins.");
            }
        } catch (SQLException ex) {
            plugin.debug("Insert error for skins table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing skins statement: " + ex.getMessage());
            }
        }
    }
}
