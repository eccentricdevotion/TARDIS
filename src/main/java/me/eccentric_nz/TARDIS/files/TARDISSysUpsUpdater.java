/*
 * Copyright (C) 2024 eccentric_nz
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
public class TARDISSysUpsUpdater {

    private final TARDIS plugin;
    private final FileConfiguration system_upgrades_config;
    private final HashMap<String, Integer> integerOptions = new HashMap<>();

    public TARDISSysUpsUpdater(TARDIS plugin) {
        this.plugin = plugin;
        system_upgrades_config = plugin.getSystemUpgradesConfig();
        // integer
        integerOptions.put("throttle.faster", 1000);
        integerOptions.put("throttle.rapid", 2000);
        integerOptions.put("throttle.warp", 3000);
    }

    public void checkSystemUpgradesConfig() {
        int i = 0;
        // int values
        for (Map.Entry<String, Integer> entry : integerOptions.entrySet()) {
            if (!system_upgrades_config.contains(entry.getKey())) {
                system_upgrades_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        if (system_upgrades_config.contains("tools.biome_reader")) {
            system_upgrades_config.set("tools.biome_reader", null);
            system_upgrades_config.set("feature.saves", system_upgrades_config.getInt("navigation.saves"));
            system_upgrades_config.set("navigation.saves", null);
            system_upgrades_config.set("feature.monitor", system_upgrades_config.getInt("tools.monitor"));
            system_upgrades_config.set("tools.monitor", null);
            system_upgrades_config.set("feature.force_field", system_upgrades_config.getInt("tools.force_field"));
            system_upgrades_config.set("tools.force_field", null);
            system_upgrades_config.set("tools.telepathic_circuit", system_upgrades_config.getInt("navigation.telepathic_circuit"));
            system_upgrades_config.set("navigation.telepathic_circuit", null);
            system_upgrades_config.set("throttle.exterior_flight", system_upgrades_config.getInt("navigation.exterior_flight"));
            system_upgrades_config.set("navigation.exterior_flight", null);
        }
        try {
            system_upgrades_config.save(new File(plugin.getDataFolder(), "system_upgrades.yml"));
            if (i > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + i + " new items to system_upgrades.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save system_upgrades.yml, " + io.getMessage());
        }
    }
}
