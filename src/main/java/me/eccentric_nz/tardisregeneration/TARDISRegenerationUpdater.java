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
package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TARDISRegenerationUpdater {

    private final TARDIS plugin;
    private final FileConfiguration regen_config;
    private final HashMap<String, Integer> integerOptions = new HashMap<>();

    public TARDISRegenerationUpdater(TARDIS plugin) {
        this.plugin = plugin;
        this.regen_config = plugin.getRegenerationConfig();
        integerOptions.put("regenerations", 15);
    }

    public void checkConfig() {
        int i = 0;
        // int values
        for (Map.Entry<String, Integer> entry : integerOptions.entrySet()) {
            if (!regen_config.contains(entry.getKey())) {
                regen_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        if (regen_config.contains("regens")) {
            regen_config.set("regenerations", regen_config.getInt("regens"));
            regen_config.set("regens", null);
            regen_config.setComments("regenerations", List.of("the number of times a player can regenerate"));
            i++;
        }
        try {
            if (i > 0) {
                regen_config.save(new File(plugin.getDataFolder(), "regeneration.yml"));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + i + " new items to regeneration.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save regeneration.yml, " + io.getMessage());
        }
    }
}
