package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.List;

public class StructureChunkListener implements Listener {

    private final TARDIS plugin;
    private final StructureManager manager;
    private final List<String> excludedWorlds;
    private final int spacing;
    private final int separation;

    public StructureChunkListener(TARDIS plugin, StructureManager manager) {
        this.plugin = plugin;
        this.manager = manager;
        excludedWorlds = plugin.getConfig().getStringList("creation.discovery.excluded_worlds");
        spacing = plugin.getConfig().getInt("creation.discovery.spacing");
        separation = plugin.getConfig().getInt("creation.discovery.separation");
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        String worldKey = event.getWorld().getKey().value();
        if (excludedWorlds.contains(worldKey)) {
            return;
        }
        if (!event.isNewChunk() && !plugin.getConfig().getBoolean("creation.discovery.existing_chunks")) {
            return;
        }
        Chunk chunk = event.getChunk();
        if (!isFeatureChunk(chunk.getWorld().getSeed(), chunk.getX(), chunk.getZ())) {
            return;
        }
        manager.tryGenerate(chunk, "Temple", 64, 64);
    }

    private boolean isFeatureChunk(long seed, int x, int z) {
        RandomSpreadStructurePlacement spread = new RandomSpreadStructurePlacement(spacing, separation, RandomSpreadType.TRIANGULAR, 987654321);
        ChunkPos chunkPos = spread.getPotentialStructureChunk(seed, x, z);
        return chunkPos.x() == x && chunkPos.z() == z;
    }
}