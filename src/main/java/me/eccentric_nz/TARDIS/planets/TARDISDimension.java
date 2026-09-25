package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class TARDISDimension {

    public WorldCreator getWorldCreator(TARDIS plugin, String world) {
        String e = plugin.getPlanetsConfig().getString("planets." + world + ".environment");
        World.Environment environment = World.Environment.valueOf(e);
        WorldCreator worldCreator = WorldCreator.name(world).environment(environment);
        try {
            WorldType worldType = WorldType.valueOf(plugin.getPlanetsConfig().getString("planets." + world + ".world_type"));
            worldCreator.type(worldType);
            worldCreator.seed(TARDISConstants.RANDOM.nextLong());
        } catch (IllegalArgumentException iae) {
            plugin.getMessenger().sendWithColour(plugin.getConsole(), TardisModule.DEBUG, "Invalid World Type specified for '" + world + "'! " + iae.getMessage(), "#FF5555");
        }
        String g = plugin.getPlanetsConfig().getString("planets." + world + ".generator");
        if (g != null && !g.equalsIgnoreCase("DEFAULT")) {
            worldCreator.generator(g);
        }
        boolean structures = true; // true if not specified
        if (plugin.getPlanetsConfig().contains("planets." + world + ".generate_structures")) {
            structures = plugin.getPlanetsConfig().getBoolean("planets." + world + ".generate_structures");
        }
        worldCreator.generateStructures(structures);
        boolean hardcore = plugin.getPlanetsConfig().getBoolean("planets." + world + ".hardcore");
        if (hardcore) {
            worldCreator.hardcore(true);
        }
        return worldCreator;
    }
}
