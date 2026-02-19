package me.eccentric_nz.TARDIS.commands.utils;

import io.papermc.paper.math.BlockPosition;
import me.eccentric_nz.tardischunkgenerator.worldgen.RoomGenerator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TeleportUtility {

    public static void notForPlayers(Player player, World world, BlockPosition pos) {
        Location spawn = new Location(world, pos.blockX(), pos.blockY(), pos.blockZ());
        teleport(player, spawn);
    }

    public static void teleport(Player player, World world) {
        Location spawn;
        if (world.getGenerator() instanceof RoomGenerator) {
            spawn = new Location(world, 8, 68, 8);
        } else {
            spawn = world.getSpawnLocation();
        }
        teleport(player, spawn);
    }

    private static void teleport(Player player, Location spawn) {
        while (!spawn.getWorld().getChunkAt(spawn).isLoaded()) {
            spawn.getWorld().getChunkAt(spawn).load();
        }
        int highest = spawn.getBlockY();
        // check if there is room for a player
        if (!spawn.getBlock().getType().isAir() || !spawn.getBlock().getRelative(BlockFace.UP).getType().isAir()) {
            highest = (spawn.getWorld().getEnvironment() == World.Environment.NETHER) ? spawn.getBlockY() : spawn.getWorld().getHighestBlockYAt(spawn) + 1;
        }
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();
        spawn.setYaw(yaw);
        spawn.setPitch(pitch);
        spawn.setY(highest);
        player.teleport(spawn);
    }
}
