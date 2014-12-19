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
public class TARDISConsoleLoader {

    private final TARDIS plugin;
    String seed;
    String permission;
    String description;
    boolean small;
    boolean tall;
    boolean beacon;

    public TARDISConsoleLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addSchematics() {
        CONSOLES.getByNames().put("ARS", new SCHEMATIC("QUARTZ_BLOCK", "ars", "ARS Console", true, false, true, false));
        CONSOLES.getByNames().put("BIGGER", new SCHEMATIC("GOLD_BLOCK", "bigger", "A Bigger Console", false, false, true, false));
        CONSOLES.getByNames().put("BUDGET", new SCHEMATIC("IRON_BLOCK", "budget", "Default Console", true, false, true, false));
        CONSOLES.getByNames().put("DELUXE", new SCHEMATIC("DIAMOND_BLOCK", "deluxe", "Supersized Deluxe Console", false, true, true, false));
        CONSOLES.getByNames().put("ELEVENTH", new SCHEMATIC("EMERALD_BLOCK", "eleventh", "11th Doctor's Console", false, true, true, false));
        CONSOLES.getByNames().put("PLANK", new SCHEMATIC("BOOKSHELF", "plank", "Wood Console", true, false, false, false));
        CONSOLES.getByNames().put("REDSTONE", new SCHEMATIC("REDSTONE_BLOCK", "redstone", "Redstone Console", false, false, true, false));
        CONSOLES.getByNames().put("STEAMPUNK", new SCHEMATIC("COAL_BLOCK", "steampunk", "Steampunk Console", true, false, true, false));
        CONSOLES.getByNames().put("TOM", new SCHEMATIC("LAPIS_BLOCK", "tom", "4th Doctor's Console", true, false, false, false));
        CONSOLES.getByNames().put("TWELFTH", new SCHEMATIC("PRISMARINE", "twelfth", "12th Doctor's Console", false, false, true, false));
        CONSOLES.getByNames().put("WAR", new SCHEMATIC("STAINED_CLAY", "war", "War Doctor's Console", true, false, true, false));
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
                // add the schematic
                CONSOLES.getByNames().put(console.toUpperCase(), new SCHEMATIC(seed, permission, description, small, tall, beacon, true));
            }
        }
        // reload lookup maps
        CONSOLES.loadLookups();
    }
}
