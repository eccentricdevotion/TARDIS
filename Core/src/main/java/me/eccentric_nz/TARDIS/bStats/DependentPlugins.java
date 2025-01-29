package me.eccentric_nz.TARDIS.bStats;

import me.eccentric_nz.TARDIS.TARDIS;

import java.util.*;

public class DependentPlugins {

    private final TARDIS plugin;
    private final List<String> plugins = List.of("Blocklocker", "BlueMap", "CoreProtect", "dynmap", "Essentials", "Factions", "floodgate", "GriefPrevention", "LibsDisguises", "LWC", "Multiverse-Core", "Multiverse-Inventories", "PlaceholderAPI", "RedProtect", "Towny", "WorldBorder", "WorldGuard", "Vault", "Citizens");

    public DependentPlugins(TARDIS plugin) {
        this.plugin = plugin;
    }

    public HashMap<String, Integer> getMap() {
        HashMap<String, Integer> data = new HashMap<>();
        for (String p : plugins) {
            if (plugin.getServer().getPluginManager().isPluginEnabled(p)) {
                data.put(p, 1);
            }
        }
        return data;
    }
}
