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
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.database.WorldRemover;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISSpace;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class TARDISWorldConfig {

    private final TARDIS plugin;

    public TARDISWorldConfig(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void check() {
        // check chunk generator
        if (plugin.getConfig().getBoolean("creation.create_worlds")) {
            if (plugin.getConfig().getBoolean("abandon.enabled")) {
                plugin.getConfig().set("abandon.enabled", false);
                plugin.saveConfig();
                plugin.getLogger().log(Level.SEVERE, "Abandoned TARDISes were disabled as create_worlds is true!");
            }
            if (plugin.getConfig().getBoolean("creation.default_world")) {
                plugin.getConfig().set("creation.default_world", false);
                plugin.saveConfig();
                plugin.getLogger().log(Level.SEVERE, "default_world was disabled as create_worlds is true!");
            }
            // disable TARDIS_TimeVortex world
            plugin.getPlanetsConfig().set("planets.TARDIS_TimeVortex.enabled", false);
            try {
                plugin.getPlanetsConfig().save(new File(plugin.getDataFolder(), "planets.yml"));
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Couldn't save planets.yml!");
            }
        }
        if (plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && plugin.getConfig().getBoolean("abandon.enabled")) {
            plugin.getConfig().set("abandon.enabled", false);
            plugin.saveConfig();
            plugin.getLogger().log(Level.SEVERE, "Abandoned TARDISes were disabled as create_worlds_with_perms is true!");
        }
        // clean up worlds
        plugin.getCleanUpWorlds().forEach((w) -> new WorldRemover(plugin).cleanWorld(w));
        // check default world
        if (!plugin.getConfig().getBoolean("creation.default_world")) {
            return;
        }
        String defWorld = plugin.getConfig().getString("creation.default_world_name", "TARDIS_TimeVortex");
        if (plugin.getServer().getWorld(defWorld) == null) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Default world specified, but it doesn't exist! Trying to create it now...");
            new TARDISSpace(plugin).createDefaultWorld(defWorld);
        }
    }
}
