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
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.artron.ArtronFurnaceParticle;
import me.eccentric_nz.TARDIS.artron.ArtronPoweredRunnable;
import me.eccentric_nz.TARDIS.artron.StandbyMode;
import me.eccentric_nz.TARDIS.console.ControlMonitor;
import me.eccentric_nz.TARDIS.control.ControlRunnable;
import me.eccentric_nz.TARDIS.desktop.DesktopPreview;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.forcefield.ForceField;
import me.eccentric_nz.TARDIS.handles.HandlesRunnable;
import me.eccentric_nz.TARDIS.junk.JunkReturnRunnable;
import me.eccentric_nz.TARDIS.move.TARDISMonsterRunnable;
import me.eccentric_nz.TARDIS.move.SpectaclesRunnable;
import me.eccentric_nz.TARDIS.rooms.ZeroRoomRunnable;
import me.eccentric_nz.TARDIS.siegemode.SiegeRunnable;
import me.eccentric_nz.TARDIS.utility.HumSounds;
import me.eccentric_nz.TARDIS.utility.VaultChecker;

public class TARDISRunnables {

    private final TARDIS plugin;

    public TARDISRunnables(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Starts a repeating task that removes Artron Energy from the TARDIS while it is in standby mode (ie not
     * travelling). Only runs if `standby_time` in artron.yml is greater than 0 (the default is 6000 or every 5
     * minutes).
     */
    public static void startStandBy(TARDIS plugin) {
        if (plugin.getConfig().getBoolean("allow.power_down")) {
            long repeat = plugin.getArtronConfig().getLong("standby_time");
            if (repeat <= 0) {
                return;
            }
            plugin.setStandbyTask(plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new StandbyMode(plugin), 6000, repeat));
        }
    }

    public void start() {
        // start artron furnace particles
        if (plugin.getArtronConfig().getBoolean("artron_furnace.particles")) {
            new ArtronFurnaceParticle(plugin).addParticles();
        }
        // starts a repeating task that powers artron furnaces inside the TARDIS
        if (plugin.getArtronConfig().getBoolean("artron_furnace.tardis_powered")) {
            long cycle = plugin.getArtronConfig().getLong("artron_furnace.power_cycle");
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new ArtronPoweredRunnable(plugin), cycle, cycle);
        }
        // starts a repeating task that plays TARDIS sound effects to players while they are inside the TARDIS.
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> new HumSounds().playTARDISHum(), 60, 1500);
        // starts a repeating task that schedules reminders added to a players Handles cyberhead companion.
        if (plugin.getHandlesConfig().getBoolean("reminders.enabled")) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HandlesRunnable(plugin), 120, plugin.getHandlesConfig().getLong("reminders.schedule"));
        }
        // starts a repeating task that removes Artron Energy from the TARDIS while it is in Siege Mode.
        // Only runs if `siege_ticks` in artron.yml is greater than 0 (the default is 1500 or every 1 minute 15 seconds).
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            long ticks = plugin.getArtronConfig().getLong("siege_ticks");
            if (ticks <= 0) {
                return;
            }
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new SiegeRunnable(plugin), 1500, ticks);
        }
        // starts a repeating task that heals players 1/2 a heart per cycle when they are in the Zero room.
        if (plugin.getConfig().getBoolean("allow.zero_room")) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new ZeroRoomRunnable(plugin), 20, plugin.getConfig().getLong("preferences.heal_speed"));
        }
        // removes unused drop chest database records from the vaults table.
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new VaultChecker(plugin), 2400);
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISMonsterRunnable(plugin), 2400, 2400);
        if (plugin.getConfig().getBoolean("allow.3d_doors")) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new SpectaclesRunnable(plugin), 120, 100);
        }
        if (plugin.getConfig().getInt("allow.force_field") > 0) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new ForceField(plugin), 20, 5);
        }
        if (plugin.getConfig().getBoolean("junk.enabled") && plugin.getConfig().getLong("junk.return") > 0) {
            long delay = plugin.getConfig().getLong("junk.return") * 20;
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new JunkReturnRunnable(plugin), delay, delay);
        }
        // update control menu signs
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new ControlRunnable(plugin), 200, 200);
        // update modelled console screens
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new ControlMonitor(plugin), 300, 200);
        // check TARDIS advancements
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (!TARDISAchievementFactory.checkAdvancement("tardis")) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, plugin.getLanguage().getString("ADVANCEMENT_RELOAD"));
            }
        }, 199);
        /*
         * Starts a repeating task that removes Artron Energy from the TARDIS while it is in standby mode (ie not
         * travelling). Only runs if `standby_time` in artron.yml is greater than 0 (the default is 6000 or every 5
         * minutes).
         */
        if (plugin.getConfig().getBoolean("allow.power_down")) {
            long repeat = plugin.getArtronConfig().getLong("standby_time");
            if (repeat <= 0) {
                return;
            }
            startStandBy(plugin);
        }
        // add desktop previews
        if (plugin.getConfig().getBoolean("desktop.previews") && plugin.getConfig().getBoolean("creation.default_world")) {
            new DesktopPreview(plugin).create();
        }
    }
}
