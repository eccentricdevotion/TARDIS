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
package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eccentric_nz
 */
public class TARDISAdaptiveUpdater {

    private final TARDIS plugin;
    private final FileConfiguration adaptive_config;
    private final HashMap<String, String> options = new HashMap<>();

    public TARDISAdaptiveUpdater(TARDIS plugin) {
        this.plugin = plugin;
        adaptive_config = plugin.getAdaptiveConfig();
        // biomes
        options.put("PALE_GARDEN", "FOREST");
        options.put("CHERRY_GROVE", "FOREST");
    }

    public void checkBiomesConfig() {
        int i = 0;
        // int values
        for (Map.Entry<String, String> entry : options.entrySet()) {
            if (!adaptive_config.contains(entry.getKey())) {
                adaptive_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        try {
            if (i > 0) {
                adaptive_config.save(new File(plugin.getDataFolder(), "adaptive.yml"));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + i + " new items to adaptive.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save adaptive.yml, " + io.getMessage());
        }
    }
}
