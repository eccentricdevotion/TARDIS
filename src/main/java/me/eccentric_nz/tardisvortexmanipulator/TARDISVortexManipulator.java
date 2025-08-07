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
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisvortexmanipulator.command.TVMCommand;
import me.eccentric_nz.tardisvortexmanipulator.command.TVMTabComplete;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMMessageGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMSavesGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.*;

public class TARDISVortexManipulator {

    private final TARDIS plugin;

    public TARDISVortexManipulator(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        plugin.setTvmSettings(new TVMSettings());
        new VortexManipulatorConfig(plugin).checkConfig();
        registerListeners();
        registerCommand();
        new TVMRecipe(plugin).addRecipe();
        startRecharger();
    }

    private void registerListeners() {
        plugin.getPM().registerEvents(new TVMBlockListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMCraftListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMDeathListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMEquipListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMMessageGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMMoveListener(plugin), plugin);
        plugin.getPM().registerEvents(new TVMSavesGUIListener(plugin), plugin);
    }

    private void registerCommand() {
        plugin.getCommand("vm").setExecutor(new TVMCommand(plugin));
        plugin.getCommand("vm").setTabCompleter(new TVMTabComplete());
    }

    private void startRecharger() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TVMTachyonRunnable(plugin), 1200L, plugin.getVortexConfig().getLong("tachyon_use.recharge_interval"));
    }
}
