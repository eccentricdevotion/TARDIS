/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import java.io.File;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Material;

/**
 *
 * @author eccentric_nz
 */
public class TARDISCustomLoader {

    private final TARDIS plugin;
    String seed;
    String permission;
    String description;
    boolean small;
    boolean tall;
    boolean beacon;

    public TARDISCustomLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addSchematics() {
        for (final String console : plugin.getCustomConsolesConfig().getKeys(false)) {
            if (plugin.getCustomConsolesConfig().getBoolean(console + ".enabled")) {
                // check that the .tschm file exists
                final String filename = plugin.getCustomConsolesConfig().getString(console + ".schematic") + ".tschm";
                String path = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + filename;
                File file = new File(path);
                if (!file.exists()) {
                    plugin.debug(plugin.getPluginName() + "Could not find a custom schematic with the name" + filename + "!");
                    continue;
                }
                // check there is an Artron value
                permission = console.toLowerCase();
                if (plugin.getArtronConfig().get("upgrades." + permission) == null) {
                    plugin.debug(plugin.getPluginName() + "Could not find a corresponding config entry in artron.yml for " + permission + "!");
                    continue;
                }
                // check seed material
                seed = plugin.getCustomConsolesConfig().getString(console + ".seed");
                try {
                    Material.valueOf(seed);
                } catch (IllegalArgumentException e) {
                    plugin.debug(plugin.getPluginName() + "Invalid custom seed block material for " + console + "!");
                    continue;
                }
                plugin.debug("Adding custom console schematic: " + console);
                // get JSON
                JSONObject obj = TARDISSchematicGZip.unzip(path);
                // get dimensions
                JSONObject dimensions = (JSONObject) obj.get("dimensions");
                final int h = dimensions.getInt("height");
                final int w = dimensions.getInt("width");
                description = plugin.getCustomConsolesConfig().getString(console + ".description");
                small = (w == 16);
                tall = (h > 16);
                beacon = plugin.getCustomConsolesConfig().getBoolean(console + ".has_beacon");
                // addSchematics the schematic
                CONSOLES.getByNames().put(console.toUpperCase(), new SCHEMATIC(seed, permission, description, small, tall, beacon, true));
            }
        }
        // reload lookup maps
        CONSOLES.loadLookups();
        for (String key : CONSOLES.getByNames().keySet()) {
            plugin.debug(key);
        }
    }
}
