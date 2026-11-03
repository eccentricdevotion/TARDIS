package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Parts {

    private final TARDIS plugin;
    private final Location location;
    private Material[][][] southWest; // not rotated
    private Material[][][] southEast; // 90 CCW
    private Material[][][] northEast; // 180 CCW
    private Material[][][] northWest; // 270 CCW
    private final BlockPos swPos = new BlockPos(-32, 0, 0);
    private final BlockPos sePos = new BlockPos(0, 0, 0);
    private final BlockPos nePos = new BlockPos(0, 0, -32);
    private final BlockPos nwPos = new BlockPos(-32, 0, -32);

    public Parts(TARDIS plugin, Location location) {
        this.plugin = plugin;
        this.location = location;
    }

    public void getCorners() {
        Material[][][] corner = new Corner().load(plugin);
        southWest = setBaseBlocks(corner);
        Rotator rotator = new Rotator();
        southEast = rotator.counterClockwise90(southWest);
        northEast = rotator.counterClockwise90(southEast);
        northWest = rotator.counterClockwise90(northEast);
    }

    public Material[][][] getSouthWest() {
        return southWest;
    }

    public Material[][][] getSouthEast() {
        return southEast;
    }

    public Material[][][] getNorthEast() {
        return northEast;
    }

    public Material[][][] getNorthWest() {
        return northWest;
    }

    public BlockPos getSwPos() {
        return swPos;
    }

    public BlockPos getNwPos() {
        return nwPos;
    }

    public BlockPos getNePos() {
        return nePos;
    }

    public BlockPos getSePos() {
        return sePos;
    }

    public Material[][][] setBaseBlocks(Material[][][] array) {
        World.Environment environment = location.getWorld().getEnvironment();
        if (environment == World.Environment.NORMAL && location.getBlockY() >= -10) {
            return array;
        }
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    Material material = array[i][j][k];
                    if (material == Material.AIR || material == Material.CLAY) {
                        continue;
                    }
                    if (material == Material.TUFF) {
                        switch (environment) {
                            case NETHER -> array[i][j][k] = Material.DEAD_HORN_CORAL_BLOCK;
                            case THE_END -> array[i][j][k] = Material.END_STONE;
                            default -> array[i][j][k] = Material.COBBLESTONE;
                        }
                    }
                    if (material == Material.TUFF_WALL) {
                        switch (environment) {
                            case NETHER -> array[i][j][k] = Material.MUD_BRICK_WALL;
                            case THE_END -> array[i][j][k] = Material.END_STONE_BRICK_WALL;
                            default -> array[i][j][k] = Material.COBBLESTONE_WALL;
                        }
                    }
                    if (material == Material.MUDDY_MANGROVE_ROOTS) {
                        switch (environment) {
                            case NETHER -> array[i][j][k] = Material.CRIMSON_HYPHAE;
                            case THE_END -> array[i][j][k] = Material.CRYING_OBSIDIAN;
                            default -> array[i][j][k] = Material.WARPED_HYPHAE;
                        }
                    }
                }
            }
        }
        return array;
    }
}
