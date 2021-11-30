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
package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TARDISSignsUpdater {

    private final TARDIS plugin;
    private final FileConfiguration signs_config;
    private final HashMap<String, List<String>> strings = new HashMap<>();

    public TARDISSignsUpdater(TARDIS plugin, FileConfiguration signs_config) {
        this.plugin = plugin;
        this.signs_config = signs_config;
        strings.put("chameleon", Arrays.asList("Chameleon", "Circuit"));
        strings.put("info", Arrays.asList("Information", "System"));
        strings.put("ars", Arrays.asList("Architectural", "Reconfiguration", "System"));
        strings.put("temporal", Arrays.asList("Temporal", "Locator"));
        strings.put("terminal", Arrays.asList("Destination", "Terminal"));
        strings.put("saves", Arrays.asList("Saved", "Locations"));
        strings.put("control", Arrays.asList("Control", "Centre"));
        strings.put("keyboard", Collections.singletonList("Keyboard"));
        strings.put("junk", Collections.singletonList("Destination"));
    }

    public void checkSignsConfig() {
        int i = 0;
        for (Map.Entry<String, List<String>> entry : strings.entrySet()) {
            if (!signs_config.contains(entry.getKey())) {
                signs_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        try {
            String signPath = plugin.getDataFolder() + File.separator + "language" + File.separator + "signs.yml";
            signs_config.save(new File(signPath));
            if (i > 0) {
                plugin.getLogger().log(Level.INFO, "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to signs.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save signs.yml, " + io.getMessage());
        }
    }
}
