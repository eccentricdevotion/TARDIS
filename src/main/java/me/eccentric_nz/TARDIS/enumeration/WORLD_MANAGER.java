package me.eccentric_nz.TARDIS.enumeration;

import org.bukkit.Bukkit;

public enum WORLD_MANAGER {

    MULTIVERSE,
    MULTIWORLD,
    MYWORLDS,
    NONE;

    public static WORLD_MANAGER getWorldManager() {
        if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            return MULTIVERSE;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Multiworld")) {
            return MULTIWORLD;
        }
        if (Bukkit.getPluginManager().isPluginEnabled("My_Worlds")) {
            return MYWORLDS;
        }
        return NONE;
    }
}
