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
package me.eccentric_nz.TARDIS.mapping;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class TARDISServerListener implements Listener {
    private final String plugin;
    private final TARDISMapper mapper;

    public TARDISServerListener(String plugin, TARDISMapper mapper) {
        this.plugin = plugin;
        this.mapper = mapper;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        Plugin p = event.getPlugin();
        String name = p.getDescription().getName();
        if (name.equals(plugin)) {
            mapper.activate();
        }
    }
}
