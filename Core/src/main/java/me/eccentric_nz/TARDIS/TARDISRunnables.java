package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.artron.ArtronPoweredRunnable;
import me.eccentric_nz.TARDIS.artron.TARDISArtronFurnaceParticle;
import me.eccentric_nz.TARDIS.artron.TARDISStandbyMode;
import me.eccentric_nz.TARDIS.console.ControlMonitor;
import me.eccentric_nz.TARDIS.control.TARDISControlRunnable;
import me.eccentric_nz.TARDIS.desktop.TARDISDesktopPreview;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.forcefield.TARDISForceField;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesRunnable;
import me.eccentric_nz.TARDIS.junk.TARDISJunkReturnRunnable;
import me.eccentric_nz.TARDIS.move.TARDISMonsterRunnable;
import me.eccentric_nz.TARDIS.move.TARDISSpectaclesRunnable;
import me.eccentric_nz.TARDIS.rooms.TARDISZeroRoomRunnable;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeRunnable;
import me.eccentric_nz.TARDIS.utility.TARDISHumSounds;
import me.eccentric_nz.TARDIS.utility.TARDISSpigotChecker;
import me.eccentric_nz.TARDIS.utility.TARDISUpdateChecker;
import me.eccentric_nz.TARDIS.utility.TARDISVaultChecker;

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
            plugin.setStandbyTask(plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new TARDISStandbyMode(plugin), 6000, repeat));
        }
    }

    public void start() {
        // start artron furnace particles
        if (plugin.getArtronConfig().getBoolean("artron_furnace.particles")) {
            new TARDISArtronFurnaceParticle(plugin).addParticles();
        }
        // starts a repeating task that powers artron furnaces inside the TARDIS
        if (plugin.getArtronConfig().getBoolean("artron_furnace.tardis_powered")) {
            long cycle = plugin.getArtronConfig().getLong("artron_furnace.power_cycle");
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new ArtronPoweredRunnable(plugin), cycle, cycle);
        }
        // starts a repeating task that plays TARDIS sound effects to players while they are inside the TARDIS.
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> new TARDISHumSounds().playTARDISHum(), 60, 1500);
        // starts a repeating task that schedules reminders added to a players Handles cyberhead companion.
        if (plugin.getHandlesConfig().getBoolean("reminders.enabled")) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISHandlesRunnable(plugin), 120, plugin.getHandlesConfig().getLong("reminders.schedule"));
        }
        // starts a repeating task that removes Artron Energy from the TARDIS while it is in Siege Mode.
        // Only runs if `siege_ticks` in artron.yml is greater than 0 (the default is 1500 or every 1 minute 15 seconds).
        if (plugin.getConfig().getBoolean("siege.enabled")) {
            long ticks = plugin.getArtronConfig().getLong("siege_ticks");
            if (ticks <= 0) {
                return;
            }
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISSiegeRunnable(plugin), 1500, ticks);
        }
        // starts a repeating task that heals players 1/2 a heart per cycle when they are in the Zero room.
        if (plugin.getConfig().getBoolean("allow.zero_room")) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISZeroRoomRunnable(plugin), 20, plugin.getConfig().getLong("preferences.heal_speed"));
        }
        // removes unused drop chest database records from the vaults table.
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISVaultChecker(plugin), 2400);
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISMonsterRunnable(plugin), 2400, 2400);
        }
        if (plugin.getConfig().getBoolean("allow.3d_doors")) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISSpectaclesRunnable(plugin), 120, 100);
        }
        if (plugin.getConfig().getInt("allow.force_field") > 0) {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISForceField(plugin), 20, 5);
        }
        if (plugin.getConfig().getBoolean("junk.enabled") && plugin.getConfig().getLong("junk.return") > 0) {
            plugin.getGeneralKeeper().setJunkTime(System.currentTimeMillis());
            long delay = plugin.getConfig().getLong("junk.return") * 20;
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISJunkReturnRunnable(plugin), delay, delay);
        }
        // update control menu signs
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISControlRunnable(plugin), 200, 200);
        // update modelled console screens
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new ControlMonitor(plugin), 300, 200);
        // check TARDIS advancements
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (!TARDISAchievementFactory.checkAdvancement("tardis")) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, plugin.getLanguage().getString("ADVANCEMENT_RELOAD"));
            }
        }, 199);
        // check TARDIS build
        if (plugin.getConfig().getBoolean("preferences.update.notify")) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new TARDISUpdateChecker(plugin, null));
        }
        // check Spigot build
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new TARDISSpigotChecker(plugin));
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
            new TARDISDesktopPreview(plugin).create();
        }
    }
}
