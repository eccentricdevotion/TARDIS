package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FindWithJunkAction {

    private final TARDIS plugin;
    BlockFace face;

    public FindWithJunkAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void getNearbyChunkLocation(int id, Player player) {
        // set destination to a surrounding chunk where the player's TARDIS is located
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet() && rsc.getWorld() != null) {
            Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            Chunk chunk = getRandomSurroundingChunk(location.getChunk());
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
        }
    }

    private Chunk getRandomSurroundingChunk(Chunk chunk) {
        // get a random block face
        BlockFace face = plugin.getGeneralKeeper().getSurrounding().get(TARDISConstants.RANDOM.nextInt(plugin.getGeneralKeeper().getSurrounding().size()));
        Vector vector = face.getDirection();
        plugin.debug(chunk.getX() + " + " + vector.getBlockX());
        plugin.debug(chunk.getZ() + " + " + vector.getBlockZ());
        int x = chunk.getX() + vector.getBlockX();
        int z = chunk.getZ() + vector.getBlockZ();
        return chunk.getWorld().getChunkAt(x, z);
    }
}
