package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class TARDISChunkLoad implements Listener {

    private final TARDIS plugin;

    public TARDISChunkLoad(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        String name = event.getWorld().getName();
        if (!isTARDISCustomBiomeWorld(name)) {
            return;
        }
        Chunk chunk = event.getChunk();
        String biomeKey = plugin.getTardisHelper().getBiomeKey(chunk);
        if (biomeKey.startsWith("minecraft")) {
            String key = name.contains("gallifrey") ? "tardis:gallifrey_badlands" : "tardis:skaro_lakes";
            plugin.getTardisHelper().setCustomBiome(key, chunk);
        } else {
            plugin.getTardisHelper().setCustomBiome(biomeKey, chunk);
        }
    }

    private boolean isTARDISCustomBiomeWorld(String planet) {
        return (planet.endsWith("tardis_gallifrey") || planet.endsWith("tardis_skaro"));
    }
}
