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
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.artron.TARDISArtronStorageCommand;
import me.eccentric_nz.TARDIS.artron.TARDISArtronTabComplete;
import me.eccentric_nz.TARDIS.commands.*;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminCommands;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminTabComplete;
import me.eccentric_nz.TARDIS.commands.bind.TARDISBindCommands;
import me.eccentric_nz.TARDIS.commands.bind.TARDISBindTabComplete;
import me.eccentric_nz.TARDIS.commands.config.TARDISConfigCommand;
import me.eccentric_nz.TARDIS.commands.config.TARDISConfigTabComplete;
import me.eccentric_nz.TARDIS.commands.dev.TARDISDevCommand;
import me.eccentric_nz.TARDIS.commands.dev.TARDISDevTabComplete;
import me.eccentric_nz.TARDIS.commands.give.TARDISGiveCommand;
import me.eccentric_nz.TARDIS.commands.give.TARDISGiveTabComplete;
import me.eccentric_nz.TARDIS.commands.handles.TARDISHandlesCommand;
import me.eccentric_nz.TARDIS.commands.handles.TARDISHandlesTabComplete;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsCommands;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsTabComplete;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteCommands;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISCommands;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISTabComplete;
import me.eccentric_nz.TARDIS.commands.utils.*;
import me.eccentric_nz.TARDIS.display.TARDISDisplayCommand;
import me.eccentric_nz.TARDIS.info.TARDISInformationSystemListener;
import me.eccentric_nz.TARDIS.junk.TARDISJunkCommands;
import me.eccentric_nz.TARDIS.junk.TARDISJunkTabComplete;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicCommand;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicTabComplete;
import me.eccentric_nz.TARDIS.universaltranslator.TARDISSayCommand;
import me.eccentric_nz.TARDIS.universaltranslator.TARDISSayTabComplete;

import java.io.File;

/**
 * Loads all TARDIS command executors and tab completers.
 *
 * @author eccentric_nz
 */
class TARDISCommandSetter {

    private final TARDIS plugin;
    private final TARDISInformationSystemListener info;

    TARDISCommandSetter(TARDIS plugin, TARDISInformationSystemListener info) {
        this.plugin = plugin;
        this.info = info;
    }

    /**
     * Loads all the commands that the TARDIS uses.
     */
    void loadCommands() {
        plugin.getCommand("tardis").setExecutor(new TARDISCommands(plugin));
        plugin.getCommand("tardis").setTabCompleter(new TARDISTabComplete(plugin));
        plugin.getCommand("tardisadmin").setExecutor(new TARDISAdminCommands(plugin));
        plugin.getCommand("tardisadmin").setTabCompleter(new TARDISAdminTabComplete(plugin));
        TARDISConfigCommand tardisConfigCommand = new TARDISConfigCommand(plugin);
        plugin.getGeneralKeeper().setTardisConfigCommand(tardisConfigCommand);
        plugin.getCommand("tardisconfig").setExecutor(tardisConfigCommand);
        plugin.getCommand("tardisconfig").setTabCompleter(new TARDISConfigTabComplete(plugin));
        plugin.getCommand("tardisdev").setExecutor(new TARDISDevCommand(plugin));
        plugin.getCommand("tardisdev").setTabCompleter(new TARDISDevTabComplete());
        plugin.getCommand("tardisarea").setExecutor(new TARDISAreaCommands(plugin));
        plugin.getCommand("tardisarea").setTabCompleter(new TARDISAreaTabComplete());
        plugin.getCommand("tardisartron").setExecutor(new TARDISArtronStorageCommand(plugin));
        plugin.getCommand("tardisartron").setTabCompleter(new TARDISArtronTabComplete());
        plugin.getCommand("tardisbind").setExecutor(new TARDISBindCommands(plugin));
        plugin.getCommand("tardisbind").setTabCompleter(new TARDISBindTabComplete());
        plugin.getCommand("tardisbiome").setExecutor(new TARDISBiomeCommand(plugin));
        plugin.getCommand("tardisbook").setExecutor(new TARDISBookCommands(plugin));
        TARDISDisplayCommand tardisDisplayCommand = new TARDISDisplayCommand(plugin);
        plugin.getCommand("tardisdisplay").setExecutor(tardisDisplayCommand);
        plugin.getCommand("tardisdisplay").setTabCompleter(tardisDisplayCommand);
        TARDISGameModeCommand tardisGM = new TARDISGameModeCommand(plugin);
        plugin.getCommand("tardisgamemode").setExecutor(tardisGM);
        plugin.getCommand("tardisgamemode").setTabCompleter(tardisGM);
        plugin.getCommand("tardisgive").setExecutor(new TARDISGiveCommand(plugin));
        plugin.getCommand("tardisgive").setTabCompleter(new TARDISGiveTabComplete(plugin));
        plugin.getCommand("tardisgravity").setExecutor(new TARDISGravityCommands(plugin));
        plugin.getCommand("tardisgravity").setTabCompleter(new TARDISGravityTabComplete());
        plugin.getCommand("tardisjunk").setExecutor(new TARDISJunkCommands(plugin));
        plugin.getCommand("tardisjunk").setTabCompleter(new TARDISJunkTabComplete());
        plugin.getCommand("tardisprefs").setExecutor(new TARDISPrefsCommands(plugin));
        plugin.getCommand("tardisprefs").setTabCompleter(new TARDISPrefsTabComplete(plugin));
        plugin.getCommand("tardisrecipe").setExecutor(new TARDISRecipeCommands(plugin));
        plugin.getCommand("tardisrecipe").setTabCompleter(new TARDISRecipeTabComplete());
        plugin.getCommand("tardisroom").setExecutor(new TARDISRoomCommands(plugin));
        plugin.getCommand("tardisroom").setTabCompleter(new TARDISRoomTabComplete(plugin));
        plugin.getCommand("tardisschematic").setExecutor(new TARDISSchematicCommand(plugin));
        plugin.getCommand("tardisschematic").setTabCompleter(new TARDISSchematicTabComplete(new File(plugin.getDataFolder() + File.separator + "user_schematics")));
        TARDISTeleportCommand tardisTP = new TARDISTeleportCommand(plugin);
        plugin.getCommand("tardisteleport").setExecutor(tardisTP);
        plugin.getCommand("tardisteleport").setTabCompleter(tardisTP);
        TARDISWorldCommand tardisWorldCommand = new TARDISWorldCommand(plugin);
        plugin.getCommand("tardisworld").setExecutor(tardisWorldCommand);
        plugin.getCommand("tardisworld").setTabCompleter(tardisWorldCommand);
        plugin.getCommand("tardistexture").setExecutor(new TARDISTextureCommands(plugin));
        plugin.getCommand("tardistexture").setTabCompleter(new TARDISTextureTabComplete());
        TARDISTravelCommands tardisTravelCommand = new TARDISTravelCommands(plugin);
        plugin.getCommand("tardistravel").setExecutor(tardisTravelCommand);
        plugin.getGeneralKeeper().setTardisTravelCommand(tardisTravelCommand);
        plugin.getCommand("tardistravel").setTabCompleter(new TARDISTravelTabComplete(plugin));
        plugin.getCommand("tardissay").setExecutor(new TARDISSayCommand(plugin));
        plugin.getCommand("tardissay").setTabCompleter(new TARDISSayTabComplete());
        TARDISSudoCommand tardisSudoCommand = new TARDISSudoCommand(plugin);
        plugin.getCommand("tardissudo").setExecutor(tardisSudoCommand);
        plugin.getCommand("tardissudo").setTabCompleter(tardisSudoCommand);
        TARDISRemoteCommands tardisRemoteCommands = new TARDISRemoteCommands(plugin);
        plugin.getCommand("tardisremote").setExecutor(tardisRemoteCommands);
        plugin.getCommand("tardisremote").setTabCompleter(tardisRemoteCommands);
        TARDISNetherPortalCommand tardisNetherPortalCommand = new TARDISNetherPortalCommand(plugin);
        plugin.getCommand("tardisnetherportal").setExecutor(tardisNetherPortalCommand);
        plugin.getCommand("tardisnetherportal").setTabCompleter(tardisNetherPortalCommand);
        plugin.getCommand("tardis?").setExecutor(new TARDISQuestionMarkCommand(plugin));
        plugin.getCommand("tardis?").setTabCompleter(new TARDISQuestionTabComplete(plugin));
        plugin.getCommand("tardisinfo").setExecutor(info);
        plugin.getCommand("handles").setExecutor(new TARDISHandlesCommand(plugin));
        plugin.getCommand("handles").setTabCompleter(new TARDISHandlesTabComplete());
        if (plugin.getConfig().getBoolean("allow.chemistry")) {
            plugin.getCommand("tardischemistry").setExecutor(new TARDISChemistryCommand(plugin));
            plugin.getCommand("tardischemistry").setTabCompleter(new TARDISChemistryTabComplete());
        }
        if (plugin.getConfig().getBoolean("allow.weather_set")) {
            TARDISWeatherCommand tardisWeatherCommand = new TARDISWeatherCommand(plugin);
            plugin.getCommand("tardisweather").setExecutor(tardisWeatherCommand);
            plugin.getCommand("tardisweather").setTabCompleter(tardisWeatherCommand);
        }
        TARDISTimeCommand tardisTimeCommand = new TARDISTimeCommand(plugin);
        plugin.getCommand("tardistime").setExecutor(tardisTimeCommand);
        plugin.getCommand("tardistime").setTabCompleter(tardisTimeCommand);
    }
}
