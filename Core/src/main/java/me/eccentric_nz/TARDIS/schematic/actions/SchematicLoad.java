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
package me.eccentric_nz.TARDIS.schematic.actions;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Load;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

public class SchematicLoad {

    public boolean act(TARDIS plugin, Player player, String[] args) {
        if (args.length < 3) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_NAME");
            return true;
        }
        Load load;
        try {
            load = Load.valueOf(args[1].toLowerCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            load = Load.user;
        }
        if (!load.isFromJar()) {
            String instr = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + args[2] + ".tschm";
            File file = new File(instr);
            if (!file.exists()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_NOT_VALID");
                return true;
            }
        }
        JsonObject sch = TARDISSchematicGZip.getObject(plugin, load.getPath(), args[2], !load.isFromJar());
        UUID uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getPastes().put(uuid, sch);
        plugin.getMessenger().sendColouredCommand(player, "SCHM_LOADED", "/ts paste", plugin);
        return true;
    }
}
