/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class TARDISInteractionDoorUpdater {

    private final TARDIS plugin;

    public TARDISInteractionDoorUpdater(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean addIds() {
        HashMap<String, Object> where = new HashMap<>();
        where.put("door_type", 1);
        ResultSetDoors rsd = new ResultSetDoors(plugin, where, true);
        if (rsd.resultSet()) {
            for (HashMap<String, String> map : rsd.getData()) {
                String l = map.get("door_location");
                String i = map.get("tardis_id");
                Location location = TARDISStaticLocationGetters.getLocationFromDB(l);
                if (location != null) {
                    while (!location.getChunk().isLoaded()) {
                        location.getChunk().load();
                    }
                    Interaction interaction = TARDISDisplayItemUtils.getInteraction(location);
                    if (interaction != null) {
                        int id = TARDISNumberParsers.parseInt(i);
                        if (id != -1) {
                            interaction.getPersistentDataContainer().set(plugin.getTardisIdKey(), PersistentDataType.INTEGER, id);
                        }
                    }
                }
            }
        }
        return true;
    }
}
