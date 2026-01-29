package me.eccentric_nz.TARDIS.schematic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SchematicPosition {

    public boolean teleport(TARDIS plugin, Player player) {
        UUID uuid = player.getUniqueId();
        double x;
        double y;
        double z;
        if (plugin.getTrackerKeeper().getStartLocation().containsKey(uuid) && plugin.getTrackerKeeper().getEndLocation().containsKey(uuid)) {
            // get the raw coords
            x = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockX() + 0.5d;
            y = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockY() + 0.5d;
            z = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockZ() + 0.5d;
        } else {
            Chunk chunk = player.getChunk();
            int cx = chunk.getX() * 16;
            int cz = chunk.getZ() * 16;
            x = cx + 15.5d;
            y = chunk.getWorld().getHighestBlockYAt(cx, cz) + 4.5d;
            z = cz + 0.5d;
        }
        Location location = new Location(player.getWorld(), x, y, z, player.getYaw(), 0);
        player.teleport(location);
        return true;
    }
}
