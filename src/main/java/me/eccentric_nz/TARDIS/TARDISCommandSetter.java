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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.artron.TARDISArtronStorageCommand;
import me.eccentric_nz.TARDIS.artron.TARDISArtronTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISAreaCommands;
import me.eccentric_nz.TARDIS.commands.TARDISAreaTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISBindCommands;
import me.eccentric_nz.TARDIS.commands.TARDISBindTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISBookCommands;
import me.eccentric_nz.TARDIS.commands.TARDISGravityCommands;
import me.eccentric_nz.TARDIS.commands.TARDISGravityTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISNetherPortalCommand;
import me.eccentric_nz.TARDIS.commands.TARDISQuestionMarkCommand;
import me.eccentric_nz.TARDIS.commands.TARDISQuestionTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISRecipeCommands;
import me.eccentric_nz.TARDIS.commands.TARDISRecipeTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISRoomCommands;
import me.eccentric_nz.TARDIS.commands.TARDISRoomTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISTextureCommands;
import me.eccentric_nz.TARDIS.commands.TARDISTextureTabComplete;
import me.eccentric_nz.TARDIS.commands.TARDISTravelCommands;
import me.eccentric_nz.TARDIS.commands.TARDISTravelTabComplete;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminCommands;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminTabComplete;
import me.eccentric_nz.TARDIS.commands.admin.TARDISGiveCommand;
import me.eccentric_nz.TARDIS.commands.admin.TARDISGiveTabComplete;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsCommands;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsTabComplete;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteCommands;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISCommands;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISTabComplete;
import me.eccentric_nz.TARDIS.junk.TARDISJunkCommands;
import me.eccentric_nz.TARDIS.junk.TARDISJunkTabComplete;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicCommand;
import me.eccentric_nz.TARDIS.universaltranslator.TARDISSayCommand;
import me.eccentric_nz.TARDIS.universaltranslator.TARDISSayTabComplete;

/**
 * Loads all TARDIS command executors and tab completers.
 *
 * @author eccentric_nz
 */
public class TARDISCommandSetter {

    private final TARDIS plugin;

    public TARDISCommandSetter(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads all the commands that the TARDIS uses.
     */
    public void loadCommands() {
        plugin.getCommand("tardis").setExecutor(new TARDISCommands(plugin));
        plugin.getCommand("tardis").setTabCompleter(new TARDISTabComplete(plugin));
        TARDISAdminCommands tardisAdminCommand = new TARDISAdminCommands(plugin);
        plugin.getCommand("tardisadmin").setExecutor(tardisAdminCommand);
        plugin.getGeneralKeeper().setTardisAdminCommand(tardisAdminCommand);
        plugin.getCommand("tardisadmin").setTabCompleter(new TARDISAdminTabComplete(plugin));
        plugin.getCommand("tardisarea").setExecutor(new TARDISAreaCommands(plugin));
        plugin.getCommand("tardisarea").setTabCompleter(new TARDISAreaTabComplete());
        plugin.getCommand("tardisartron").setExecutor(new TARDISArtronStorageCommand(plugin));
        plugin.getCommand("tardisartron").setTabCompleter(new TARDISArtronTabComplete());
        plugin.getCommand("tardisbind").setExecutor(new TARDISBindCommands(plugin));
        plugin.getCommand("tardisbind").setTabCompleter(new TARDISBindTabComplete());
        plugin.getCommand("tardisbook").setExecutor(new TARDISBookCommands(plugin));
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
        plugin.getCommand("tardistexture").setExecutor(new TARDISTextureCommands(plugin));
        plugin.getCommand("tardistexture").setTabCompleter(new TARDISTextureTabComplete());
        TARDISTravelCommands tardisTravelCommand = new TARDISTravelCommands(plugin);
        plugin.getCommand("tardistravel").setExecutor(tardisTravelCommand);
        plugin.getGeneralKeeper().setTardisTravelCommand(tardisTravelCommand);
        plugin.getCommand("tardistravel").setTabCompleter(new TARDISTravelTabComplete(plugin));
        plugin.getCommand("tardissay").setExecutor(new TARDISSayCommand(plugin));
        plugin.getCommand("tardissay").setTabCompleter(new TARDISSayTabComplete());
        plugin.getCommand("tardisremote").setExecutor(new TARDISRemoteCommands(plugin));
        plugin.getCommand("tardisnetherportal").setExecutor(new TARDISNetherPortalCommand(plugin));
        plugin.getCommand("tardis?").setExecutor(new TARDISQuestionMarkCommand(plugin));
        plugin.getCommand("tardis?").setTabCompleter(new TARDISQuestionTabComplete(plugin));
    }
}
