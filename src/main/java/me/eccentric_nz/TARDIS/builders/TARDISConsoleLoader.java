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
package me.eccentric_nz.tardis.builders;

import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.enumeration.ConsoleSize;
import me.eccentric_nz.tardis.enumeration.Consoles;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.schematic.TARDISSchematicGZip;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TARDISConsoleLoader {

	private final TARDISPlugin plugin;
	private boolean save = false;

	public TARDISConsoleLoader(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	//    new Schematic(String seed, String permission, String description, ConsoleSize size, boolean beacon, boolean lanterns, boolean custom)
	public void addSchematics() {
		// DELUXE, ELEVENTH, TWELFTH, ars & REDSTONE schematics designed by Lord_Rahl and killeratnight at mcnovus.net
		Consoles.getBY_NAMES().put("ars", new Schematic("QUARTZ_BLOCK", "ars", "ars Console", ConsoleSize.SMALL, true, false, false));
		Consoles.getBY_NAMES().put("BIGGER", new Schematic("GOLD_BLOCK", "bigger", "A Bigger Console", ConsoleSize.MEDIUM, true, false, false));
		Consoles.getBY_NAMES().put("BUDGET", new Schematic("IRON_BLOCK", "budget", "Default Console", ConsoleSize.SMALL, true, false, false));
		// COPPER & CORAL schematics designed by vistaero
		Consoles.getBY_NAMES().put("COPPER", new Schematic("WARPED_PLANKS", "copper", "11th Doctor's Copper Console", ConsoleSize.MASSIVE, true, false, false));
		Consoles.getBY_NAMES().put("CORAL", new Schematic("NETHER_WART_BLOCK", "coral", "10th Doctor's Console", ConsoleSize.TALL, true, false, false));
		Consoles.getBY_NAMES().put("DELTA", new Schematic("CRYING_OBSIDIAN", "delta", "Nether Delta Console", ConsoleSize.MEDIUM, false, false, false));
		Consoles.getBY_NAMES().put("DELUXE", new Schematic("DIAMOND_BLOCK", "deluxe", "Supersized Deluxe Console", ConsoleSize.TALL, true, false, false));
		Consoles.getBY_NAMES().put("ELEVENTH", new Schematic("EMERALD_BLOCK", "eleventh", "11th Doctor's Console", ConsoleSize.TALL, true, true, false));
		// ENDER schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
		Consoles.getBY_NAMES().put("ENDER", new Schematic("PURPUR_BLOCK", "ender", "Ender Console", ConsoleSize.SMALL, true, true, false));
		// FACTORY designed by Razihel
		Consoles.getBY_NAMES().put("FACTORY", new Schematic("YELLOW_CONCRETE_POWDER", "factory", "Factory Console (1st Doctor)", ConsoleSize.MEDIUM, false, true, false));
		// MASTER's schematic designed by ShadowAssociate
		Consoles.getBY_NAMES().put("MASTER", new Schematic("NETHER_BRICKS", "master", "The Master's Console", ConsoleSize.TALL, true, false, false));
		Consoles.getBY_NAMES().put("PLANK", new Schematic("BOOKSHELF", "plank", "Wood Console", ConsoleSize.SMALL, false, false, false));
		// PYRAMID schematic designed by airomis (player at thatsnotacreeper.com)
		Consoles.getBY_NAMES().put("PYRAMID", new Schematic("SANDSTONE_STAIRS", "pyramid", "A Sandstone Pyramid Console", ConsoleSize.SMALL, true, false, false));
		Consoles.getBY_NAMES().put("REDSTONE", new Schematic("REDSTONE_BLOCK", "redstone", "Redstone Console", ConsoleSize.MEDIUM, true, false, false));
		Consoles.getBY_NAMES().put("ROTOR", new Schematic("HONEYCOMB_BLOCK", "rotor", "Time Rotor Console", ConsoleSize.SMALL, false, false, false));
		Consoles.getBY_NAMES().put("STEAMPUNK", new Schematic("COAL_BLOCK", "steampunk", "Steampunk Console", ConsoleSize.SMALL, true, false, false));
		// THIRTEENTH designed by Razihel
		Consoles.getBY_NAMES().put("THIRTEENTH", new Schematic("ORANGE_CONCRETE", "thirteenth", "13th Doctor's Console", ConsoleSize.MEDIUM, false, false, false));
		Consoles.getBY_NAMES().put("TOM", new Schematic("LAPIS_BLOCK", "tom", "4th Doctor's Console", ConsoleSize.SMALL, false, false, false));
		Consoles.getBY_NAMES().put("TWELFTH", new Schematic("PRISMARINE", "twelfth", "12th Doctor's Console", ConsoleSize.MEDIUM, true, true, false));
		Consoles.getBY_NAMES().put("WAR", new Schematic("WHITE_TERRACOTTA", "war", "War Doctor's Console", ConsoleSize.SMALL, true, false, false));
		// cobblestone templates
		Consoles.getBY_NAMES().put("SMALL", new Schematic("COBBLESTONE", "small", "16x16x16 cobblestone template", ConsoleSize.SMALL, false, true, false));
		Consoles.getBY_NAMES().put("MEDIUM", new Schematic("COBBLESTONE", "medium", "32x16x32 cobblestone template", ConsoleSize.MEDIUM, false, true, false));
		Consoles.getBY_NAMES().put("TALL", new Schematic("COBBLESTONE", "tall", "32x32x32 cobblestone template", ConsoleSize.TALL, false, true, false));
		// legacy consoles
		if (plugin.getConfig().getBoolean("creation.enable_legacy")) {
			Consoles.getBY_NAMES().put("LEGACY_BIGGER", new Schematic("ORANGE_GLAZED_TERRACOTTA", "legacy_bigger", "The original Bigger Console", ConsoleSize.MEDIUM, true, false, false));
			Consoles.getBY_NAMES().put("LEGACY_BUDGET", new Schematic("LIGHT_GRAY_GLAZED_TERRACOTTA", "legacy_budget", "The original Default Console", ConsoleSize.SMALL, true, false, false));
			Consoles.getBY_NAMES().put("LEGACY_DELUXE", new Schematic("LIME_GLAZED_TERRACOTTA", "legacy_deluxe", "The original Deluxe Console", ConsoleSize.TALL, true, false, false));
			Consoles.getBY_NAMES().put("LEGACY_ELEVENTH", new Schematic("CYAN_GLAZED_TERRACOTTA", "legacy_eleventh", "The original 11th Doctor's Console", ConsoleSize.TALL, true, true, false));
			Consoles.getBY_NAMES().put("LEGACY_REDSTONE", new Schematic("RED_GLAZED_TERRACOTTA", "legacy_redstone", "The original Redstone Console", ConsoleSize.TALL, true, false, false));
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
				String seed = Objects.requireNonNull(plugin.getCustomConsolesConfig().getString(
						console + ".seed")).toUpperCase(Locale.ENGLISH);
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
				assert obj != null;
				JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
				int h = dimensions.get("height").getAsInt();
				int w = dimensions.get("width").getAsInt();
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
				Consoles.getBY_NAMES().put(console.toUpperCase(Locale.ENGLISH), new Schematic(seed, permission, description, consoleSize, beacon, lanterns, true));
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
