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
import java.io.IOException;
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
    boolean lanterns;
    boolean save = false;

    public TARDISConsoleLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

//    new SCHEMATIC(String seed, String permission, String description, boolean small, boolean tall, boolean beacon, boolean lanterns, boolean custom)
    public void addSchematics() {
        // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics designed by Lord_Rahl and killeratnight at mcnovus.net
        CONSOLES.getBY_NAMES().put("ARS", new SCHEMATIC("QUARTZ_BLOCK", "ars", "ARS Console", true, false, true, false, false));
        CONSOLES.getBY_NAMES().put("BIGGER", new SCHEMATIC("GOLD_BLOCK", "bigger", "A Bigger Console", false, false, true, false, false));
        CONSOLES.getBY_NAMES().put("BUDGET", new SCHEMATIC("IRON_BLOCK", "budget", "Default Console", true, false, true, false, false));
        // CORAL schematic designed by vistaero
        CONSOLES.getBY_NAMES().put("CORAL", new SCHEMATIC("NETHER_WART_BLOCK", "coral", "10th Doctor's Console", false, true, true, false, false));
        CONSOLES.getBY_NAMES().put("DELUXE", new SCHEMATIC("DIAMOND_BLOCK", "deluxe", "Supersized Deluxe Console", false, true, true, false, false));
        CONSOLES.getBY_NAMES().put("ELEVENTH", new SCHEMATIC("EMERALD_BLOCK", "eleventh", "11th Doctor's Console", false, true, true, true, false));
        // ENDER schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
        CONSOLES.getBY_NAMES().put("ENDER", new SCHEMATIC("PURPUR_BLOCK", "ender", "Ender Console", true, false, true, true, false));
        CONSOLES.getBY_NAMES().put("PLANK", new SCHEMATIC("BOOKSHELF", "plank", "Wood Console", true, false, false, false, false));
        CONSOLES.getBY_NAMES().put("REDSTONE", new SCHEMATIC("REDSTONE_BLOCK", "redstone", "Redstone Console", false, false, true, false, false));
        CONSOLES.getBY_NAMES().put("STEAMPUNK", new SCHEMATIC("COAL_BLOCK", "steampunk", "Steampunk Console", true, false, true, false, false));
        CONSOLES.getBY_NAMES().put("TOM", new SCHEMATIC("LAPIS_BLOCK", "tom", "4th Doctor's Console", true, false, false, false, false));
        CONSOLES.getBY_NAMES().put("TWELFTH", new SCHEMATIC("PRISMARINE", "twelfth", "12th Doctor's Console", false, false, true, true, false));
        CONSOLES.getBY_NAMES().put("WAR", new SCHEMATIC("STAINED_CLAY", "war", "War Doctor's Console", true, false, true, false, false));
        // PYRAMID schematic designed by airomis (player at thatsnotacreeper.com)
        CONSOLES.getBY_NAMES().put("PYRAMID", new SCHEMATIC("SANDSTONE_STAIRS", "pyramid", "A Sandstone Pyramid Console", true, false, true, false, false));
        // MASTER's schematic designed by shadowhawk14269 (while playing at pvpcraft.ca)
        CONSOLES.getBY_NAMES().put("MASTER", new SCHEMATIC("NETHER_BRICK", "master", "The Master's Console", false, true, true, false, false));
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
                if (plugin.getCustomConsolesConfig().contains(console + ".has_lanterns")) {
                    lanterns = plugin.getCustomConsolesConfig().getBoolean(console + ".has_lanterns");
                } else {
                    lanterns = false;
                    plugin.getCustomConsolesConfig().set(console + ".has_lanterns", false);
                    save = true;
                }
                // add the schematic
                CONSOLES.getBY_NAMES().put(console.toUpperCase(), new SCHEMATIC(seed, permission, description, small, tall, beacon, lanterns, true));
            }
        }
        // reload lookup maps
        CONSOLES.loadLookups();
        if (save) {
            // save custom consoles config
            try {
                plugin.getCustomConsolesConfig().save(new File(plugin.getDataFolder(), "custom_consoles.yml"));
            } catch (IOException io) {
                plugin.debug("Could not save custom_consoles.yml, " + io);
            }
        }
    }
}
