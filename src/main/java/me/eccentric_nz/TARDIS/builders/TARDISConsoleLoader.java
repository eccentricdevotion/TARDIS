/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISConsoleLoader {

    private final TARDIS plugin;
    private boolean save = false;

    public TARDISConsoleLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    //    new SCHEMATIC(String seed, String permission, String description, ConsoleSize size, boolean beacon, boolean lanterns, boolean custom)
    public void addSchematics() {
        // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics designed by Lord_Rahl and killeratnight at mcnovus.net
        CONSOLES.getBY_NAMES().put("ARS", new SCHEMATIC("QUARTZ_BLOCK", "ars", "ARS Console", ConsoleSize.SMALL, true, false, false));
        CONSOLES.getBY_NAMES().put("BIGGER", new SCHEMATIC("GOLD_BLOCK", "bigger", "A Bigger Console", ConsoleSize.MEDIUM, true, false, false));
        CONSOLES.getBY_NAMES().put("BUDGET", new SCHEMATIC("IRON_BLOCK", "budget", "Default Console", ConsoleSize.SMALL, true, false, false));
        // CORAL schematic designed by vistaero
        CONSOLES.getBY_NAMES().put("CORAL", new SCHEMATIC("NETHER_WART_BLOCK", "coral", "10th Doctor's Console", ConsoleSize.TALL, true, false, false));
        CONSOLES.getBY_NAMES().put("DELUXE", new SCHEMATIC("DIAMOND_BLOCK", "deluxe", "Supersized Deluxe Console", ConsoleSize.TALL, true, false, false));
        CONSOLES.getBY_NAMES().put("ELEVENTH", new SCHEMATIC("EMERALD_BLOCK", "eleventh", "11th Doctor's Console", ConsoleSize.TALL, true, true, false));
        // ENDER schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
        CONSOLES.getBY_NAMES().put("ENDER", new SCHEMATIC("PURPUR_BLOCK", "ender", "Ender Console", ConsoleSize.SMALL, true, true, false));
        // MASTER's schematic designed by ShadowAssociate
        CONSOLES.getBY_NAMES().put("MASTER", new SCHEMATIC("NETHER_BRICKS", "master", "The Master's Console", ConsoleSize.TALL, true, false, false));
        CONSOLES.getBY_NAMES().put("PLANK", new SCHEMATIC("BOOKSHELF", "plank", "Wood Console", ConsoleSize.SMALL, false, false, false));
        // PYRAMID schematic designed by airomis (player at thatsnotacreeper.com)
        CONSOLES.getBY_NAMES().put("PYRAMID", new SCHEMATIC("SANDSTONE_STAIRS", "pyramid", "A Sandstone Pyramid Console", ConsoleSize.SMALL, true, false, false));
        CONSOLES.getBY_NAMES().put("REDSTONE", new SCHEMATIC("REDSTONE_BLOCK", "redstone", "Redstone Console", ConsoleSize.MEDIUM, true, false, false));
        CONSOLES.getBY_NAMES().put("STEAMPUNK", new SCHEMATIC("COAL_BLOCK", "steampunk", "Steampunk Console", ConsoleSize.SMALL, true, false, false));
        // THIRTEENTH designed by Razihel
        CONSOLES.getBY_NAMES().put("THIRTEENTH", new SCHEMATIC("ORANGE_CONCRETE", "thirteenth", "13th Doctor's Console", ConsoleSize.MEDIUM, false, false, false));
        // FACTORY designed by Razihel
        CONSOLES.getBY_NAMES().put("FACTORY", new SCHEMATIC("YELLOW_CONCRETE_POWDER", "factory", "Factory Console (1st Doctor)", ConsoleSize.MEDIUM, false, true, false));
        CONSOLES.getBY_NAMES().put("TOM", new SCHEMATIC("LAPIS_BLOCK", "tom", "4th Doctor's Console", ConsoleSize.SMALL, false, false, false));
        CONSOLES.getBY_NAMES().put("TWELFTH", new SCHEMATIC("PRISMARINE", "twelfth", "12th Doctor's Console", ConsoleSize.MEDIUM, true, true, false));
        CONSOLES.getBY_NAMES().put("WAR", new SCHEMATIC("WHITE_TERRACOTTA", "war", "War Doctor's Console", ConsoleSize.SMALL, true, false, false));
        // cobblestone templates
        CONSOLES.getBY_NAMES().put("SMALL", new SCHEMATIC("COBBLESTONE", "small", "16x16x16 cobblestone template", ConsoleSize.SMALL, false, true, false));
        CONSOLES.getBY_NAMES().put("MEDIUM", new SCHEMATIC("COBBLESTONE", "medium", "32x16x32 cobblestone template", ConsoleSize.MEDIUM, false, true, false));
        CONSOLES.getBY_NAMES().put("TALL", new SCHEMATIC("COBBLESTONE", "tall", "32x32x32 cobblestone template", ConsoleSize.TALL, false, true, false));
        // legacy consoles
        if (plugin.getConfig().getBoolean("creation.enable_legacy")) {
            CONSOLES.getBY_NAMES().put("LEGACY_BIGGER", new SCHEMATIC("ORANGE_GLAZED_TERRACOTTA", "legacy_bigger", "The original Bigger Console", ConsoleSize.MEDIUM, true, false, false));
            CONSOLES.getBY_NAMES().put("LEGACY_BUDGET", new SCHEMATIC("LIGHT_GRAY_GLAZED_TERRACOTTA", "legacy_budget", "The original Default Console", ConsoleSize.SMALL, true, false, false));
            CONSOLES.getBY_NAMES().put("LEGACY_DELUXE", new SCHEMATIC("LIME_GLAZED_TERRACOTTA", "legacy_deluxe", "The original Deluxe Console", ConsoleSize.TALL, true, false, false));
            CONSOLES.getBY_NAMES().put("LEGACY_ELEVENTH", new SCHEMATIC("CYAN_GLAZED_TERRACOTTA", "legacy_eleventh", "The original 11th Doctor's Console", ConsoleSize.TALL, true, true, false));
            CONSOLES.getBY_NAMES().put("LEGACY_REDSTONE", new SCHEMATIC("RED_GLAZED_TERRACOTTA", "legacy_redstone", "The original Redstone Console", ConsoleSize.TALL, true, false, false));
        }
        for (String console : plugin.getCustomConsolesConfig().getKeys(false)) {
            if (plugin.getCustomConsolesConfig().getBoolean(console + ".enabled")) {
                // check that the .tschm file exists
                String filename = plugin.getCustomConsolesConfig().getString(console + ".schematic") + ".tschm";
                String path = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + filename;
                File file = new File(path);
                if (!file.exists()) {
                    plugin.debug("Could not find a custom schematic with the name" + filename + "!");
                    continue;
                }
                // check there is an Artron value
                String permission = console.toLowerCase(Locale.ENGLISH);
                if (plugin.getArtronConfig().get("upgrades." + permission) == null) {
                    plugin.debug("Could not find a corresponding config entry in artron.yml for " + permission + "!");
                    continue;
                }
                // check seed material
                String seed = plugin.getCustomConsolesConfig().getString(console + ".seed").toUpperCase(Locale.ENGLISH);
                try {
                    Material.valueOf(seed);
                } catch (IllegalArgumentException e) {
                    plugin.debug("Invalid custom seed block material for " + console + "!");
                    continue;
                }
                plugin.debug("Adding custom console schematic: " + console);
                // get JSON
                JSONObject obj = TARDISSchematicGZip.unzip(path);
                // get dimensions
                JSONObject dimensions = (JSONObject) obj.get("dimensions");
                int h = dimensions.getInt("height");
                int w = dimensions.getInt("width");
                String description = plugin.getCustomConsolesConfig().getString(console + ".description");
                ConsoleSize consoleSize = ConsoleSize.getByWidthAndHeight(w, h);
                boolean beacon = plugin.getCustomConsolesConfig().getBoolean(console + ".has_beacon");
                boolean lanterns;
                if (plugin.getCustomConsolesConfig().contains(console + ".has_lanterns")) {
                    lanterns = plugin.getCustomConsolesConfig().getBoolean(console + ".has_lanterns");
                } else {
                    lanterns = false;
                    plugin.getCustomConsolesConfig().set(console + ".has_lanterns", false);
                    save = true;
                }
                // add the schematic
                CONSOLES.getBY_NAMES().put(console.toUpperCase(Locale.ENGLISH), new SCHEMATIC(seed, permission, description, consoleSize, beacon, lanterns, true));
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
