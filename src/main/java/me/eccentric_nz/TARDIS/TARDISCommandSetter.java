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

import me.eccentric_nz.tardis.artron.TardisArtronStorageCommand;
import me.eccentric_nz.tardis.artron.TardisArtronTabComplete;
import me.eccentric_nz.tardis.commands.*;
import me.eccentric_nz.tardis.commands.admin.TardisAdminCommands;
import me.eccentric_nz.tardis.commands.admin.TardisAdminTabComplete;
import me.eccentric_nz.tardis.commands.bind.TardisBindCommands;
import me.eccentric_nz.tardis.commands.bind.TardisBindTabComplete;
import me.eccentric_nz.tardis.commands.config.TardisConfigCommand;
import me.eccentric_nz.tardis.commands.config.TardisConfigTabComplete;
import me.eccentric_nz.tardis.commands.dev.TardisDevCommand;
import me.eccentric_nz.tardis.commands.dev.TardisDevTabComplete;
import me.eccentric_nz.tardis.commands.give.TardisGiveCommand;
import me.eccentric_nz.tardis.commands.give.TardisGiveTabComplete;
import me.eccentric_nz.tardis.commands.handles.TardisHandlesCommand;
import me.eccentric_nz.tardis.commands.handles.TardisHandlesTabComplete;
import me.eccentric_nz.tardis.commands.preferences.TardisPrefsCommands;
import me.eccentric_nz.tardis.commands.preferences.TardisPrefsTabComplete;
import me.eccentric_nz.tardis.commands.remote.TardisRemoteCommands;
import me.eccentric_nz.tardis.commands.sudo.TardisSudoCommand;
import me.eccentric_nz.tardis.commands.tardis.TardisCommands;
import me.eccentric_nz.tardis.commands.tardis.TardisTabComplete;
import me.eccentric_nz.tardis.commands.utils.*;
import me.eccentric_nz.tardis.display.TardisDisplayCommand;
import me.eccentric_nz.tardis.info.TardisInformationSystemListener;
import me.eccentric_nz.tardis.junk.TardisJunkCommands;
import me.eccentric_nz.tardis.junk.TardisJunkTabComplete;
import me.eccentric_nz.tardis.schematic.TardisSchematicCommand;
import me.eccentric_nz.tardis.schematic.TardisSchematicTabComplete;
import me.eccentric_nz.tardis.universaltranslator.TardisSayCommand;
import me.eccentric_nz.tardis.universaltranslator.TardisSayTabComplete;

import java.io.File;
import java.util.Objects;

/**
 * Loads all tardis command executors and tab completers.
 *
 * @author eccentric_nz
 */
class TardisCommandSetter {

    private final TardisPlugin plugin;
    private final TardisInformationSystemListener info;

    TardisCommandSetter(TardisPlugin plugin, TardisInformationSystemListener info) {
        this.plugin = plugin;
        this.info = info;
    }

    /**
     * Loads all the commands that the TARDIS uses.
     */
    void loadCommands() {
        Objects.requireNonNull(plugin.getCommand("tardis")).setExecutor(new TardisCommands(plugin));
        Objects.requireNonNull(plugin.getCommand("tardis")).setTabCompleter(new TardisTabComplete(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisadmin")).setExecutor(new TardisAdminCommands(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisadmin")).setTabCompleter(new TardisAdminTabComplete(plugin));
        TardisConfigCommand tardisConfigCommand = new TardisConfigCommand(plugin);
        plugin.getGeneralKeeper().setTardisConfigCommand(tardisConfigCommand);
        Objects.requireNonNull(plugin.getCommand("tardisconfig")).setExecutor(tardisConfigCommand);
        Objects.requireNonNull(plugin.getCommand("tardisconfig")).setTabCompleter(new TardisConfigTabComplete(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisdev")).setExecutor(new TardisDevCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisdev")).setTabCompleter(new TardisDevTabComplete());
        Objects.requireNonNull(plugin.getCommand("tardisarea")).setExecutor(new TardisAreaCommands(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisarea")).setTabCompleter(new TardisAreaTabComplete());
        Objects.requireNonNull(plugin.getCommand("tardisartron")).setExecutor(new TardisArtronStorageCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisartron")).setTabCompleter(new TardisArtronTabComplete());
        Objects.requireNonNull(plugin.getCommand("tardisbind")).setExecutor(new TardisBindCommands(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisbind")).setTabCompleter(new TardisBindTabComplete());
        Objects.requireNonNull(plugin.getCommand("tardisbiome")).setExecutor(new TardisBiomeCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisbook")).setExecutor(new TardisBookCommands(plugin));
        TardisDisplayCommand tardisDisplayCommand = new TardisDisplayCommand(plugin);
        Objects.requireNonNull(plugin.getCommand("tardisdisplay")).setExecutor(tardisDisplayCommand);
        Objects.requireNonNull(plugin.getCommand("tardisdisplay")).setTabCompleter(tardisDisplayCommand);
        TardisGameModeCommand tardisGM = new TardisGameModeCommand(plugin);
        Objects.requireNonNull(plugin.getCommand("tardisgamemode")).setExecutor(tardisGM);
        Objects.requireNonNull(plugin.getCommand("tardisgamemode")).setTabCompleter(tardisGM);
        Objects.requireNonNull(plugin.getCommand("tardisgive")).setExecutor(new TardisGiveCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisgive")).setTabCompleter(new TardisGiveTabComplete(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisgravity")).setExecutor(new TardisGravityCommands(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisgravity")).setTabCompleter(new TardisGravityTabComplete());
        Objects.requireNonNull(plugin.getCommand("tardisjunk")).setExecutor(new TardisJunkCommands(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisjunk")).setTabCompleter(new TardisJunkTabComplete());
        Objects.requireNonNull(plugin.getCommand("tardisprefs")).setExecutor(new TardisPrefsCommands(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisprefs")).setTabCompleter(new TardisPrefsTabComplete(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisrecipe")).setExecutor(new TardisRecipeCommands(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisrecipe")).setTabCompleter(new TardisRecipeTabComplete());
        Objects.requireNonNull(plugin.getCommand("tardisroom")).setExecutor(new TardisRoomCommands(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisroom")).setTabCompleter(new TardisRoomTabComplete(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisschematic")).setExecutor(new TardisSchematicCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisschematic")).setTabCompleter(new TardisSchematicTabComplete(new File(plugin.getDataFolder() + File.separator + "user_schematics")));
        TardisTeleportCommand tardisTP = new TardisTeleportCommand(plugin);
        Objects.requireNonNull(plugin.getCommand("tardisteleport")).setExecutor(tardisTP);
        Objects.requireNonNull(plugin.getCommand("tardisteleport")).setTabCompleter(tardisTP);
        TardisWorldCommand tardisWorldCommand = new TardisWorldCommand(plugin);
        Objects.requireNonNull(plugin.getCommand("tardisworld")).setExecutor(tardisWorldCommand);
        Objects.requireNonNull(plugin.getCommand("tardisworld")).setTabCompleter(tardisWorldCommand);
        Objects.requireNonNull(plugin.getCommand("tardistexture")).setExecutor(new TardisTextureCommands(plugin));
        Objects.requireNonNull(plugin.getCommand("tardistexture")).setTabCompleter(new TardisTextureTabComplete());
        TardisTravelCommands tardisTravelCommand = new TardisTravelCommands(plugin);
        Objects.requireNonNull(plugin.getCommand("tardistravel")).setExecutor(tardisTravelCommand);
        plugin.getGeneralKeeper().setTardisTravelCommand(tardisTravelCommand);
        Objects.requireNonNull(plugin.getCommand("tardistravel")).setTabCompleter(new TardisTravelTabComplete(plugin));
        Objects.requireNonNull(plugin.getCommand("tardissay")).setExecutor(new TardisSayCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("tardissay")).setTabCompleter(new TardisSayTabComplete());
        TardisSudoCommand tardisSudoCommand = new TardisSudoCommand(plugin);
        Objects.requireNonNull(plugin.getCommand("tardissudo")).setExecutor(tardisSudoCommand);
        Objects.requireNonNull(plugin.getCommand("tardissudo")).setTabCompleter(tardisSudoCommand);
        TardisRemoteCommands tardisRemoteCommands = new TardisRemoteCommands(plugin);
        Objects.requireNonNull(plugin.getCommand("tardisremote")).setExecutor(tardisRemoteCommands);
        Objects.requireNonNull(plugin.getCommand("tardisremote")).setTabCompleter(tardisRemoteCommands);
        TardisNetherPortalCommand tardisNetherPortalCommand = new TardisNetherPortalCommand(plugin);
        Objects.requireNonNull(plugin.getCommand("tardisnetherportal")).setExecutor(tardisNetherPortalCommand);
        Objects.requireNonNull(plugin.getCommand("tardisnetherportal")).setTabCompleter(tardisNetherPortalCommand);
        Objects.requireNonNull(plugin.getCommand("tardis?")).setExecutor(new TardisQuestionMarkCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("tardis?")).setTabCompleter(new TardisQuestionTabComplete(plugin));
        Objects.requireNonNull(plugin.getCommand("tardisinfo")).setExecutor(info);
        Objects.requireNonNull(plugin.getCommand("handles")).setExecutor(new TardisHandlesCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("handles")).setTabCompleter(new TardisHandlesTabComplete());
        if (plugin.getConfig().getBoolean("allow.chemistry")) {
            Objects.requireNonNull(plugin.getCommand("tardischemistry")).setExecutor(new TardisChemistryCommand(plugin));
            Objects.requireNonNull(plugin.getCommand("tardischemistry")).setTabCompleter(new TardisChemistryTabComplete());
        }
        if (plugin.getConfig().getBoolean("allow.weather_set")) {
            TardisWeatherCommand tardisWeatherCommand = new TardisWeatherCommand(plugin);
            Objects.requireNonNull(plugin.getCommand("tardisweather")).setExecutor(tardisWeatherCommand);
            Objects.requireNonNull(plugin.getCommand("tardisweather")).setTabCompleter(tardisWeatherCommand);
        }
        TardisTimeCommand tardisTimeCommand = new TardisTimeCommand(plugin);
        Objects.requireNonNull(plugin.getCommand("tardistime")).setExecutor(tardisTimeCommand);
        Objects.requireNonNull(plugin.getCommand("tardistime")).setTabCompleter(tardisTimeCommand);
    }
}
