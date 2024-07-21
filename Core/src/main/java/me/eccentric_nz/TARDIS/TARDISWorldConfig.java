package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.database.TARDISWorldRemover;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISSpace;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class TARDISWorldConfig {

    private final TARDIS plugin;

    public TARDISWorldConfig(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void check() {
        // check chunk generator
        if (plugin.getConfig().getBoolean("creation.create_worlds")) {
            if (plugin.getConfig().getBoolean("abandon.enabled")) {
                plugin.getConfig().set("abandon.enabled", false);
                plugin.saveConfig();
                plugin.getLogger().log(Level.SEVERE, "Abandoned TARDISes were disabled as create_worlds is true!");
            }
            if (plugin.getConfig().getBoolean("creation.default_world")) {
                plugin.getConfig().set("creation.default_world", false);
                plugin.saveConfig();
                plugin.getLogger().log(Level.SEVERE, "default_world was disabled as create_worlds is true!");
            }
            // disable TARDIS_TimeVortex world
            plugin.getPlanetsConfig().set("planets.TARDIS_TimeVortex.enabled", false);
            try {
                plugin.getPlanetsConfig().save(new File(plugin.getDataFolder(), "planets.yml"));
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Couldn't save planets.yml!");
            }
        }
        if (plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && plugin.getConfig().getBoolean("abandon.enabled")) {
            plugin.getConfig().set("abandon.enabled", false);
            plugin.saveConfig();
            plugin.getLogger().log(Level.SEVERE, "Abandoned TARDISes were disabled as create_worlds_with_perms is true!");
        }
        // clean up worlds
        plugin.getCleanUpWorlds().forEach((w) -> new TARDISWorldRemover(plugin).cleanWorld(w));
        // check default world
        if (!plugin.getConfig().getBoolean("creation.default_world")) {
            return;
        }
        String defWorld = plugin.getConfig().getString("creation.default_world_name", "TARDIS_TimeVortex");
        if (plugin.getServer().getWorld(defWorld) == null) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Default world specified, but it doesn't exist! Trying to create it now...");
            new TARDISSpace(plugin).createDefaultWorld(defWorld);
        }
    }
}
