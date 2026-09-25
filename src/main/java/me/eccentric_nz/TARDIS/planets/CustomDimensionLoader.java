package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class CustomDimensionLoader {

    public static World loadWorld(TARDIS plugin, String name) {
        WorldCreator creator = new TARDISDimension().getWorldCreator(plugin, name);
        return Bukkit.getServer().createWorld(creator);
    }
}
