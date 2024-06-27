package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.junk.TARDISJunkControlListener;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
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
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (rst.fromUUID(player.getUniqueId().toString())) {
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, rst.getTardisId());
            if (rsc.resultSet() && rsc.getWorld() != null) {
                Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                Chunk chunk = getRandomSurroundingChunk(location.getChunk());
                // get a random location in the chunk
                int x = TARDISConstants.RANDOM.nextInt(16) + (chunk.getX() * 16);
                int z = TARDISConstants.RANDOM.nextInt(16) + (chunk.getZ() * 16);
                Sign sign = TARDISJunkControlListener.getDestinationSign(id);
                sign.getSide(Side.FRONT).setLine(1, chunk.getWorld().getName());
                sign.getSide(Side.FRONT).setLine(2, "" + x);
                sign.getSide(Side.FRONT).setLine(3, "" + z);
                sign.update();
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "EXTERMINATE_NONE");
        }
    }

    private Chunk getRandomSurroundingChunk(Chunk chunk) {
        // get a random block face
        BlockFace face = plugin.getGeneralKeeper().getSurrounding().get(TARDISConstants.RANDOM.nextInt(plugin.getGeneralKeeper().getSurrounding().size()));
        Vector vector = face.getDirection();
        double cx = (vector.getX() < 0) ? Math.floor(vector.getX()) : Math.ceil(vector.getX());
        double cz = (vector.getZ() < 0) ? Math.floor(vector.getZ()) : Math.ceil(vector.getZ());
        int x = (int) (chunk.getX() + cx);
        int z = (int) (chunk.getZ() + cz);
        return chunk.getWorld().getChunkAt(x, z);
    }
}
