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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.VortexManipulatorCommandNode;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMMessageGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMSavesGUIListener;
import me.eccentric_nz.tardisvortexmanipulator.listeners.*;

import java.util.List;

public class TARDISVortexManipulator {

    private final TARDIS plugin;

    public TARDISVortexManipulator(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        plugin.setTvmSettings(new TVMSettings());
        // update the config
        new VortexManipulatorConfig(plugin).checkConfig();
        // register listeners
        registerListeners();
        // register command
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
                commands.registrar().register(new VortexManipulatorCommandNode(plugin).build(), List.of("vortexmainpulator", "tvm")));
        // add recipe
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

    private void startRecharger() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TVMTachyonRunnable(plugin), 1200L, plugin.getVortexConfig().getLong("tachyon_use.recharge_interval"));
    }
}
