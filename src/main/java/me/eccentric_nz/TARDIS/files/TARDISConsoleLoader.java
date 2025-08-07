/*
 * Copyright (C) 2025 eccentric_nz
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

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
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

    //    new Schematic(String seed, String permission, String description, ConsoleSize size, boolean beacon, TardisLight light, boolean custom, int preview)
    public void addSchematics() {
        Consoles.getBY_NAMES().put("ANCIENT", new Schematic("SCULK", "ancient", "Ancient Console", ConsoleSize.SMALL, false, TardisLight.LAMP, false, -10));
        // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics designed by Lord_Rahl and killeratnight at mcnovus.net
        Consoles.getBY_NAMES().put("ARS", new Schematic("QUARTZ_BLOCK", "ars", "ARS Console", ConsoleSize.SMALL, true, TardisLight.TENTH, false, -11));
        Consoles.getBY_NAMES().put("BIGGER", new Schematic("GOLD_BLOCK", "bigger", "A Bigger Console", ConsoleSize.MEDIUM, true, TardisLight.TENTH, false, -12));
        // BONE loosely based on a console by DT10 - https://www.youtube.com/watch?v=Ux4qt0qYm80
        Consoles.getBY_NAMES().put("BONE", new Schematic("WAXED_OXIDIZED_CUT_COPPER", "bone", "An Early Style Console", ConsoleSize.SMALL, true, TardisLight.CLASSIC, false, -13));
        Consoles.getBY_NAMES().put("BUDGET", new Schematic("IRON_BLOCK", "budget", "Default Console", ConsoleSize.SMALL, true, TardisLight.TENTH, false, -14));
        Consoles.getBY_NAMES().put("CAVE", new Schematic("DRIPSTONE_BLOCK", "cave", "Cave Console", ConsoleSize.SMALL, false, TardisLight.LAMP, false, -15));
        // COPPER & CORAL schematics designed by vistaero
        Consoles.getBY_NAMES().put("COPPER", new Schematic("WARPED_PLANKS", "copper", "11th Doctor's Copper Console", ConsoleSize.MASSIVE, true, TardisLight.ELEVENTH, 65, false, -16));
        Consoles.getBY_NAMES().put("CORAL", new Schematic("NETHER_WART_BLOCK", "coral", "10th Doctor's Console", ConsoleSize.TALL, true, TardisLight.TENTH, false, -17));
        // CURSED schematic designed by airomis (player at thatsnotacreeper.com)
        Consoles.getBY_NAMES().put("CURSED", new Schematic("BLACK_CONCRETE", "cursed", "Cursed Console", ConsoleSize.MASSIVE, true, TardisLight.TENTH, 62, false, -18));
        Consoles.getBY_NAMES().put("DELTA", new Schematic("CRYING_OBSIDIAN", "delta", "Nether Delta Console", ConsoleSize.MEDIUM, false, TardisLight.LAMP, false, -19));
        Consoles.getBY_NAMES().put("DELUXE", new Schematic("DIAMOND_BLOCK", "deluxe", "Supersized Deluxe Console", ConsoleSize.TALL, true, TardisLight.TENTH, false, -20));
        Consoles.getBY_NAMES().put("DIVISION", new Schematic("PINK_GLAZED_TERRACOTTA", "division", "The Division Interuniverse Console", ConsoleSize.MEDIUM, false, TardisLight.LANTERN, false, -21));
        Consoles.getBY_NAMES().put("ELEVENTH", new Schematic("EMERALD_BLOCK", "eleventh", "11th Doctor's Console", ConsoleSize.TALL, true, TardisLight.TWELFTH, false, -22));
        // ENDER schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
        Consoles.getBY_NAMES().put("ENDER", new Schematic("PURPUR_BLOCK", "ender", "Ender Console", ConsoleSize.SMALL, true, TardisLight.LANTERN, false, -23));
        // FACTORY designed by Razihel
        Consoles.getBY_NAMES().put("FACTORY", new Schematic("YELLOW_CONCRETE_POWDER", "factory", "Factory Console (1st Doctor)", ConsoleSize.MEDIUM, false, TardisLight.CLASSIC, 65, false, -24));
        // FIFTEENTH schematic designed by airomis (player at thatsnotacreeper.com)
        Consoles.getBY_NAMES().put("FIFTEENTH", new Schematic("OCHRE_FROGLIGHT", "fifteenth", "15th Doctor's Console", ConsoleSize.MASSIVE, true, TardisLight.THIRTEENTH, false, -25));
        // FUGITIVE based on Ruth TARDIS designed by DT10 - https://www.youtube.com/watch?v=aykwXVemSs8
        Consoles.getBY_NAMES().put("FUGITIVE", new Schematic("POLISHED_DEEPSLATE", "fugitive", "Ruth (The Fugitive Doctor) Clayton's Console", ConsoleSize.MEDIUM, false, TardisLight.CLASSIC, false, -26));
        Consoles.getBY_NAMES().put("HOSPITAL", new Schematic("WHITE_CONCRETE", "hospital", "St John's Hospital Console", ConsoleSize.SMALL, false, TardisLight.CLASSIC, false, -27));
        // MASTER's schematic designed by ShadowAssociate
        Consoles.getBY_NAMES().put("MASTER", new Schematic("NETHER_BRICKS", "master", "The Master's Console", ConsoleSize.TALL, true, TardisLight.LAMP, false, -28));
        // MECHANICAL adapted from design by Plastic Straw https://www.planetminecraft.com/data-pack/new-tardis-mod-mechanical-interior-datapack/
        Consoles.getBY_NAMES().put("MECHANICAL", new Schematic("POLISHED_ANDESITE", "mechanical", "Mechanical Console", ConsoleSize.MEDIUM, false, TardisLight.LANTERN, 62, false, -29));
        Consoles.getBY_NAMES().put("ORIGINAL", new Schematic("PACKED_MUD", "original", "The original TARDIS plugin Console", ConsoleSize.SMALL, false, TardisLight.TENTH, false, -30));
        Consoles.getBY_NAMES().put("PLANK", new Schematic("BOOKSHELF", "plank", "Wood Console", ConsoleSize.SMALL, false, TardisLight.LAMP, false, -31));
        // PYRAMID schematic designed by airomis (player at thatsnotacreeper.com)
        Consoles.getBY_NAMES().put("PYRAMID", new Schematic("SANDSTONE_STAIRS", "pyramid", "A Sandstone Pyramid Console", ConsoleSize.SMALL, true, TardisLight.LAMP, false, -32));
        Consoles.getBY_NAMES().put("REDSTONE", new Schematic("REDSTONE_BLOCK", "redstone", "Redstone Console", ConsoleSize.MEDIUM, true, TardisLight.TENTH, 65, false, -33));
        Consoles.getBY_NAMES().put("ROTOR", new Schematic("HONEYCOMB_BLOCK", "rotor", "Time Rotor Console", ConsoleSize.SMALL, false, TardisLight.TENTH, false, -34));
        Consoles.getBY_NAMES().put("RUSTIC", new Schematic("COPPER_BULB", "rustic", "Rustic Console", ConsoleSize.MEDIUM, false, TardisLight.BULB, false, -35));
        Consoles.getBY_NAMES().put("STEAMPUNK", new Schematic("COAL_BLOCK", "steampunk", "Steampunk Console", ConsoleSize.SMALL, true, TardisLight.LAMP, false, -36));
        // THIRTEENTH designed by Razihel
        Consoles.getBY_NAMES().put("THIRTEENTH", new Schematic("HORN_CORAL_BLOCK", "thirteenth", "13th Doctor's Console", ConsoleSize.MEDIUM, false, TardisLight.THIRTEENTH, 65, false, -37));
        Consoles.getBY_NAMES().put("TOM", new Schematic("LAPIS_BLOCK", "tom", "4th Doctor's Console", ConsoleSize.SMALL, false, TardisLight.CLASSIC, false, -38));
        Consoles.getBY_NAMES().put("TWELFTH", new Schematic("PRISMARINE", "twelfth", "12th Doctor's Console", ConsoleSize.MEDIUM, true, TardisLight.TWELFTH, false, -39));
        Consoles.getBY_NAMES().put("WAR", new Schematic("WHITE_TERRACOTTA", "war", "War Doctor's Console", ConsoleSize.SMALL, true, TardisLight.CLASSIC, false, -40));
        Consoles.getBY_NAMES().put("WEATHERED", new Schematic("WEATHERED_COPPER", "weathered", "Weathered Copper Console", ConsoleSize.SMALL, true, TardisLight.LANTERN, false, -41));
        // cobblestone templates
        Consoles.getBY_NAMES().put("SMALL", new Schematic("COBBLESTONE", "small", "16x16x16 cobblestone template", ConsoleSize.SMALL, false, TardisLight.LAMP, false, 0));
        Consoles.getBY_NAMES().put("MEDIUM", new Schematic("COBBLESTONE", "medium", "32x16x32 cobblestone template", ConsoleSize.MEDIUM, false, TardisLight.LAMP, false, 0));
        Consoles.getBY_NAMES().put("TALL", new Schematic("COBBLESTONE", "tall", "32x32x32 cobblestone template", ConsoleSize.TALL, false, TardisLight.LAMP, false, 0));
        // legacy consoles
        if (plugin.getConfig().getBoolean("creation.enable_legacy")) {
            Consoles.getBY_NAMES().put("LEGACY_BIGGER", new Schematic("ORANGE_GLAZED_TERRACOTTA", "legacy_bigger", "Legacy Bigger Console", ConsoleSize.MEDIUM, true, TardisLight.TENTH, false, -42));
            Consoles.getBY_NAMES().put("LEGACY_DELUXE", new Schematic("LIME_GLAZED_TERRACOTTA", "legacy_deluxe", "Legacy Deluxe Console", ConsoleSize.TALL, true, TardisLight.TENTH, false, -43));
            Consoles.getBY_NAMES().put("LEGACY_ELEVENTH", new Schematic("CYAN_GLAZED_TERRACOTTA", "legacy_eleventh", "Legacy 11th Doctor's Console", ConsoleSize.TALL, true, TardisLight.TWELFTH, false, -44));
            Consoles.getBY_NAMES().put("LEGACY_REDSTONE", new Schematic("RED_GLAZED_TERRACOTTA", "legacy_redstone", "Legacy Redstone Console", ConsoleSize.TALL, true, TardisLight.TENTH, false, -45));
        }
        int p = -46;
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
                String permission = console.toLowerCase(Locale.ROOT);
                if (plugin.getArtronConfig().get("upgrades." + permission) == null) {
                    plugin.debug("Could not find a corresponding config entry in artron.yml for " + permission + "!");
                    continue;
                }
                // check seed material
                String seed = plugin.getCustomConsolesConfig().getString(console + ".seed").toUpperCase(Locale.ROOT);
                try {
                    Material.valueOf(seed);
                } catch (IllegalArgumentException e) {
                    plugin.debug("Invalid custom seed block material for " + console + "!");
                    continue;
                }
                plugin.debug("Adding custom console schematic: " + console);
                // get JSON
                JsonObject obj = TARDISSchematicGZip.unzip(path);
                // get dimensions
                JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                int h = dimensions.get("height").getAsInt();
                int w = dimensions.get("width").getAsInt();
                String description = plugin.getCustomConsolesConfig().getString(console + ".description");
                ConsoleSize consoleSize = ConsoleSize.getByWidthAndHeight(w, h);
                boolean beacon = plugin.getCustomConsolesConfig().getBoolean(console + ".has_beacon");
                TardisLight light = (plugin.getCustomConsolesConfig().getBoolean(console + ".has_lanterns")) ? TardisLight.LANTERN : TardisLight.LAMP;
                if (plugin.getCustomConsolesConfig().contains(console + ".lights")) {
                    light = TardisLight.valueOf(plugin.getCustomConsolesConfig().getString(console + ".lights"));
                } else {
                    plugin.getCustomConsolesConfig().set(console + ".lights", light.toString());
                    save = true;
                }
                int sy = 64;
                if (plugin.getCustomConsolesConfig().contains(console + ".start_y")) {
                    sy = plugin.getCustomConsolesConfig().getInt(console + ".start_y");
                } else {
                    plugin.getCustomConsolesConfig().set(console + ".start_y", 64);
                    save = true;
                }
                // add the schematic
                Consoles.getBY_NAMES().put(console.toUpperCase(Locale.ROOT), new Schematic(seed, permission, description, consoleSize, beacon, light, sy, true, p));
                p--;
            }
        }
        // reload lookup maps
        Consoles.loadLookups();
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
