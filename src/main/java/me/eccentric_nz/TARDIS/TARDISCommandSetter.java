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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis;

import me.eccentric_nz.tardis.artron.TARDISArtronStorageCommand;
import me.eccentric_nz.tardis.artron.TARDISArtronTabComplete;
import me.eccentric_nz.tardis.commands.*;
import me.eccentric_nz.tardis.commands.admin.TARDISAdminCommands;
import me.eccentric_nz.tardis.commands.admin.TARDISAdminTabComplete;
import me.eccentric_nz.tardis.commands.admin.TARDISGiveCommand;
import me.eccentric_nz.tardis.commands.admin.TARDISGiveTabComplete;
import me.eccentric_nz.tardis.commands.bind.TARDISBindCommands;
import me.eccentric_nz.tardis.commands.bind.TARDISBindTabComplete;
import me.eccentric_nz.tardis.commands.handles.TARDISHandlesCommand;
import me.eccentric_nz.tardis.commands.handles.TARDISHandlesTabComplete;
import me.eccentric_nz.tardis.commands.preferences.TARDISPrefsCommands;
import me.eccentric_nz.tardis.commands.preferences.TARDISPrefsTabComplete;
import me.eccentric_nz.tardis.commands.remote.TARDISRemoteCommands;
import me.eccentric_nz.tardis.commands.sudo.TARDISSudoCommand;
import me.eccentric_nz.tardis.commands.tardis.TARDISCommands;
import me.eccentric_nz.tardis.commands.tardis.TARDISTabComplete;
import me.eccentric_nz.tardis.commands.utils.*;
import me.eccentric_nz.tardis.display.TARDISDisplayCommand;
import me.eccentric_nz.tardis.info.TARDISInformationSystemListener;
import me.eccentric_nz.tardis.junk.TARDISJunkCommands;
import me.eccentric_nz.tardis.junk.TARDISJunkTabComplete;
import me.eccentric_nz.tardis.schematic.TARDISSchematicCommand;
import me.eccentric_nz.tardis.schematic.TARDISSchematicTabComplete;
import me.eccentric_nz.tardis.universaltranslator.TARDISSayCommand;
import me.eccentric_nz.tardis.universaltranslator.TARDISSayTabComplete;

import java.io.File;
import java.util.Objects;

/**
 * Loads all tardis command executors and tab completers.
 *
 * @author eccentric_nz
 */
class TARDISCommandSetter {

	private final TARDISPlugin plugin;
	private final TARDISInformationSystemListener info;

	TARDISCommandSetter(TARDISPlugin plugin, TARDISInformationSystemListener info) {
		this.plugin = plugin;
		this.info = info;
	}

	/**
	 * Loads all the commands that the TARDIS uses.
	 */
	void loadCommands() {
		Objects.requireNonNull(plugin.getCommand("tardis")).setExecutor(new TARDISCommands(plugin));
		Objects.requireNonNull(plugin.getCommand("tardis")).setTabCompleter(new TARDISTabComplete(plugin));
		TARDISAdminCommands tardisAdminCommand = new TARDISAdminCommands(plugin);
		Objects.requireNonNull(plugin.getCommand("tardisadmin")).setExecutor(tardisAdminCommand);
		plugin.getGeneralKeeper().setTardisAdminCommand(tardisAdminCommand);
		Objects.requireNonNull(plugin.getCommand("tardisadmin")).setTabCompleter(new TARDISAdminTabComplete(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisarea")).setExecutor(new TARDISAreaCommands(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisarea")).setTabCompleter(new TARDISAreaTabComplete());
		Objects.requireNonNull(plugin.getCommand("tardisartron")).setExecutor(new TARDISArtronStorageCommand(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisartron")).setTabCompleter(new TARDISArtronTabComplete());
		Objects.requireNonNull(plugin.getCommand("tardisbind")).setExecutor(new TARDISBindCommands(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisbind")).setTabCompleter(new TARDISBindTabComplete());
		Objects.requireNonNull(plugin.getCommand("tardisbiome")).setExecutor(new TARDISBiomeCommand(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisbook")).setExecutor(new TARDISBookCommands(plugin));
		TARDISDisplayCommand tardisDisplayCommand = new TARDISDisplayCommand(plugin);
		Objects.requireNonNull(plugin.getCommand("tardisdisplay")).setExecutor(tardisDisplayCommand);
		Objects.requireNonNull(plugin.getCommand("tardisdisplay")).setTabCompleter(tardisDisplayCommand);
		TARDISGameModeCommand tardisGM = new TARDISGameModeCommand(plugin);
		Objects.requireNonNull(plugin.getCommand("tardisgamemode")).setExecutor(tardisGM);
		Objects.requireNonNull(plugin.getCommand("tardisgamemode")).setTabCompleter(tardisGM);
		Objects.requireNonNull(plugin.getCommand("tardisgive")).setExecutor(new TARDISGiveCommand(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisgive")).setTabCompleter(new TARDISGiveTabComplete(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisgravity")).setExecutor(new TARDISGravityCommands(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisgravity")).setTabCompleter(new TARDISGravityTabComplete());
		Objects.requireNonNull(plugin.getCommand("tardisjunk")).setExecutor(new TARDISJunkCommands(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisjunk")).setTabCompleter(new TARDISJunkTabComplete());
		Objects.requireNonNull(plugin.getCommand("tardisprefs")).setExecutor(new TARDISPrefsCommands(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisprefs")).setTabCompleter(new TARDISPrefsTabComplete(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisrecipe")).setExecutor(new TARDISRecipeCommands(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisrecipe")).setTabCompleter(new TARDISRecipeTabComplete());
		Objects.requireNonNull(plugin.getCommand("tardisroom")).setExecutor(new TARDISRoomCommands(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisroom")).setTabCompleter(new TARDISRoomTabComplete(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisschematic")).setExecutor(new TARDISSchematicCommand(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisschematic")).setTabCompleter(new TARDISSchematicTabComplete(new File(plugin.getDataFolder() + File.separator + "user_schematics")));
		TARDISTeleportCommand tardisTP = new TARDISTeleportCommand(plugin);
		Objects.requireNonNull(plugin.getCommand("tardisteleport")).setExecutor(tardisTP);
		Objects.requireNonNull(plugin.getCommand("tardisteleport")).setTabCompleter(tardisTP);
		TARDISWorldCommand tardisWorldCommand = new TARDISWorldCommand(plugin);
		Objects.requireNonNull(plugin.getCommand("tardisworld")).setExecutor(tardisWorldCommand);
		Objects.requireNonNull(plugin.getCommand("tardisworld")).setTabCompleter(tardisWorldCommand);
		Objects.requireNonNull(plugin.getCommand("tardistexture")).setExecutor(new TARDISTextureCommands(plugin));
		Objects.requireNonNull(plugin.getCommand("tardistexture")).setTabCompleter(new TARDISTextureTabComplete());
		TARDISTravelCommands tardisTravelCommand = new TARDISTravelCommands(plugin);
		Objects.requireNonNull(plugin.getCommand("tardistravel")).setExecutor(tardisTravelCommand);
		plugin.getGeneralKeeper().setTardisTravelCommand(tardisTravelCommand);
		Objects.requireNonNull(plugin.getCommand("tardistravel")).setTabCompleter(new TARDISTravelTabComplete(plugin));
		Objects.requireNonNull(plugin.getCommand("tardissay")).setExecutor(new TARDISSayCommand(plugin));
		Objects.requireNonNull(plugin.getCommand("tardissay")).setTabCompleter(new TARDISSayTabComplete());
		TARDISSudoCommand tardisSudoCommand = new TARDISSudoCommand(plugin);
		Objects.requireNonNull(plugin.getCommand("tardissudo")).setExecutor(tardisSudoCommand);
		Objects.requireNonNull(plugin.getCommand("tardissudo")).setTabCompleter(tardisSudoCommand);
		TARDISRemoteCommands tardisRemoteCommands = new TARDISRemoteCommands(plugin);
		Objects.requireNonNull(plugin.getCommand("tardisremote")).setExecutor(tardisRemoteCommands);
		Objects.requireNonNull(plugin.getCommand("tardisremote")).setTabCompleter(tardisRemoteCommands);
		TARDISNetherPortalCommand tardisNetherPortalCommand = new TARDISNetherPortalCommand(plugin);
		Objects.requireNonNull(plugin.getCommand("tardisnetherportal")).setExecutor(tardisNetherPortalCommand);
		Objects.requireNonNull(plugin.getCommand("tardisnetherportal")).setTabCompleter(tardisNetherPortalCommand);
		Objects.requireNonNull(plugin.getCommand("tardis?")).setExecutor(new TARDISQuestionMarkCommand(plugin));
		Objects.requireNonNull(plugin.getCommand("tardis?")).setTabCompleter(new TARDISQuestionTabComplete(plugin));
		Objects.requireNonNull(plugin.getCommand("tardisinfo")).setExecutor(info);
		Objects.requireNonNull(plugin.getCommand("handles")).setExecutor(new TARDISHandlesCommand(plugin));
		Objects.requireNonNull(plugin.getCommand("handles")).setTabCompleter(new TARDISHandlesTabComplete());
		if (plugin.getConfig().getBoolean("allow.chemistry")) {
			Objects.requireNonNull(plugin.getCommand("tardischemistry")).setExecutor(new TARDISChemistryCommand(plugin));
			Objects.requireNonNull(plugin.getCommand("tardischemistry")).setTabCompleter(new TARDISChemistryTabComplete());
		}
		if (plugin.getConfig().getBoolean("allow.weather_set")) {
			TARDISWeatherCommand tardisWeatherCommand = new TARDISWeatherCommand(plugin);
			Objects.requireNonNull(plugin.getCommand("tardisweather")).setExecutor(tardisWeatherCommand);
			Objects.requireNonNull(plugin.getCommand("tardisweather")).setTabCompleter(tardisWeatherCommand);
		}
		TARDISTimeCommand tardisTimeCommand = new TARDISTimeCommand(plugin);
		Objects.requireNonNull(plugin.getCommand("tardistime")).setExecutor(tardisTimeCommand);
		Objects.requireNonNull(plugin.getCommand("tardistime")).setTabCompleter(tardisTimeCommand);
	}
}
