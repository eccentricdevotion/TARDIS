package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

public class EyeLoader {

    private final TARDIS plugin;

    public EyeLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        // put into own class with other eye methods
        plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER,"Adding custom biome for Eye of Harmony room...");
        plugin.getTardisHelper().addCustomBiome("eye");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, ()->{
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER,"Firing up Eye of Harmony capture stars...");
            new EyeStarter(plugin).goSuperNova();
        }, 600L);
    }
}
