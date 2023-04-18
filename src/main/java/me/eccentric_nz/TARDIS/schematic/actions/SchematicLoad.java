/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic.actions;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class SchematicLoad {

    public boolean act(TARDIS plugin, Player player, String which) {
        UUID uuid = player.getUniqueId();
        String instr = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + which + ".tschm";
        File file = new File(instr);
        if (!file.exists()) {
            TARDISMessage.send(player, "SCHM_NOT_VALID");
            return true;
        }
        JsonObject sch = TARDISSchematicGZip.unzip(instr);
        plugin.getTrackerKeeper().getPastes().put(uuid, sch);
        TARDISMessage.send(player, "SCHM_LOADED", ChatColor.GREEN + "/ts paste" + ChatColor.RESET);
        return true;
    }
}
