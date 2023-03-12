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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class TARDISHandlesUpdater {

    private final TARDIS plugin;
    private final FileConfiguration handlesConfig;
    private final HashMap<String, String> stringOptions = new HashMap<>();

    public TARDISHandlesUpdater(TARDIS plugin, FileConfiguration handlesConfig) {
        this.plugin = plugin;
        this.handlesConfig = handlesConfig;
    }

    public void checkHandles() {
        if (!handlesConfig.contains("core-commands.brake")) {
            handlesConfig.set("core-commands.brake", "\\b(?:(?:hand)*brake|park)\\b");
            try {
                String handlesPath = plugin.getDataFolder() + File.separator + "handles.yml";
                handlesConfig.save(new File(handlesPath));
                plugin.getLogger().log(Level.INFO, "Updated handles.yml");
            } catch (IOException io) {
                plugin.debug("Could not save handles.yml, " + io.getMessage());
            }
        }
    }
}
