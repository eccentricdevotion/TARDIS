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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * TARDISes prefer the environment of the Space-Time Vortex to the four
 * dimensional world. They have Curiosity Circuits to encourage them to leave
 * the Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISDatabaseUpdater {

    private HashMap<String, String> updates = new HashMap<String, String>();
    private Statement statement;

    public TARDISDatabaseUpdater(Statement statement) {
        this.statement = statement;
        updates.put("areas", "y INTEGER");
        updates.put("blocks", "police_box INTEGER DEFAULT 0");
        updates.put("destinations", "bind TEXT DEFAULT ''");
        updates.put("player_prefs", "artron_level INTEGER DEFAULT 0");
        updates.put("player_prefs", "auto_on INTEGER DEFAULT 0");
        updates.put("player_prefs", "wall TEXT DEFAULT 'ORANGE_WOOL'");
        updates.put("tardis", "artron_button TEXT DEFAULT ''");
        updates.put("tardis", "artron_level INTEGER DEFAULT 0");
        updates.put("tardis", "condenser TEXT DEFAULT ''");
        updates.put("tardis", "creeper TEXT DEFAULT ''");
        updates.put("tardis", "handbrake TEXT DEFAULT ''");
        updates.put("tardis", "handbrake_on INT DEFAULT 1");
        updates.put("tardis", "middle_data INTEGER");
        updates.put("tardis", "middle_id INTEGER");
        updates.put("tardis", "scanner TEXT DEFAULT ''");
        updates.put("tardis", "tardis_init INTEGER DEFAULT 0");
    }

    /**
     * Updates the TARDIS database tables, adding new fields.
     */
    public void updateTables() {
        for (Map.Entry<String, String> a : updates.entrySet()) {
            String query = "SELECT sql FROM sqlite_master WHERE tbl_name = '" + a.getKey() + "' AND sql LIKE '%" + a.getValue() + "%'";
            try {
                ResultSet rsa = statement.executeQuery(query);
                if (!rsa.next()) {
                    String alter = "ALTER TABLE " + a.getKey() + " ADD " + a.getValue();
                    statement.executeUpdate(alter);
                }
            } catch (SQLException e) {
                TARDIS.plugin.debug("Database update error: " + a + " - " + e.getMessage());
            }
        }
        TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + "Adding scanner to tardis!");
    }
}
