/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.display.TARDISDisplayCommand;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.info.TARDISInformationSystemCommand;
import me.eccentric_nz.TARDIS.junk.TARDISJunkCommands;
import me.eccentric_nz.TARDIS.junk.TARDISJunkTabComplete;
import me.eccentric_nz.TARDIS.universaltranslator.TARDISSayCommand;
import me.eccentric_nz.TARDIS.universaltranslator.TARDISSayTabComplete;
import me.eccentric_nz.tardischemistry.TARDISChemistryCommand;
import me.eccentric_nz.tardischemistry.TARDISChemistryTabComplete;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Loads all TARDIS command executors and tab completers.
 *
 * @author eccentric_nz
 */
class TARDISCommandSetter {

    private final TARDIS plugin;

    TARDISCommandSetter(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads all the commands that the TARDIS uses.
     */
    void loadCommands() {
        TARDISDisplayCommand tardisDisplayCommand = new TARDISDisplayCommand(plugin);
        plugin.getCommand("tardisdisplay").setExecutor(tardisDisplayCommand);
        plugin.getCommand("tardisdisplay").setTabCompleter(tardisDisplayCommand);
        plugin.getCommand("tardisjunk").setExecutor(new TARDISJunkCommands(plugin));
        plugin.getCommand("tardisjunk").setTabCompleter(new TARDISJunkTabComplete());
        plugin.getCommand("tardissay").setExecutor(new TARDISSayCommand(plugin));
        plugin.getCommand("tardissay").setTabCompleter(new TARDISSayTabComplete());
        plugin.getCommand("tardisinfo").setExecutor(new TARDISInformationSystemCommand(plugin));
        if (plugin.getConfig().getBoolean("modules.chemistry")) {
            plugin.getCommand("tardischemistry").setExecutor(new TARDISChemistryCommand(plugin));
            plugin.getCommand("tardischemistry").setTabCompleter(new TARDISChemistryTabComplete());
        } else {
            plugin.getCommand("tardischemistry").setExecutor(new FallbackCommandHandler("Chemistry", TardisModule.CHEMISTRY));
        }
    }

    public class FallbackCommandHandler implements CommandExecutor {
        private final String moduleName;
        private final TardisModule module;

        public FallbackCommandHandler(String moduleName, TardisModule module) {
            this.moduleName = moduleName;
            this.module = module;
        }

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            plugin.getMessenger().send(commandSender, module, "MODULE_NOT_ENABLED", moduleName);
            return true;
        }
    }
}
