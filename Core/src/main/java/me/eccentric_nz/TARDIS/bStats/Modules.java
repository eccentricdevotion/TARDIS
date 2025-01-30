package me.eccentric_nz.TARDIS.bStats;

import me.eccentric_nz.TARDIS.TARDIS;

import java.util.HashMap;

public class Modules {

    private final TARDIS plugin;

    public Modules(TARDIS plugin) {
        this.plugin = plugin;
    }

    public HashMap<String, Integer> getMap() {
        HashMap<String, Integer> data = new HashMap<>();
        for (String m : plugin.getConfig().getConfigurationSection("modules").getKeys(false)) {
            if (plugin.getConfig().getBoolean("modules." + m)) {
                data.put(m, 1);
            }
        }
        return data;
    }
}
