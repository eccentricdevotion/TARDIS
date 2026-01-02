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
package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

public class EyeLoader {

    private final TARDIS plugin;

    public EyeLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        // add custom biome
        plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER,"Adding custom biome for Eye of Harmony room...");
        plugin.getTardisHelper().addCustomBiome("eye");
        // start particles
        if (plugin.getConfig().getBoolean("eye_of_harmony.particles")) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER, "Firing up Eye of Harmony captured stars...");
                new EyeStarter(plugin).goSuperNova();
            }, 600L);
        }
        // start damage
        if (plugin.getConfig().getBoolean("eye_of_harmony.player_damage")) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                    plugin,
                    new EyeDamageRunnable(plugin),
                    180, plugin.getConfig().getLong("eye_of_harmony.damage_period")
            );
        }
    }
}
