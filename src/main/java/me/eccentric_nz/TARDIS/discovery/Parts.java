package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

public class Parts {

    private final TARDIS plugin;
    private Material[][][] southWest; // not rotated
    private Material[][][] northWest; // 90 CW
    private Material[][][] northEast; // 180 CW
    private Material[][][] southEast; // 270 CW
    private final BlockPos swPos = new BlockPos(-32, 0, 0);
    private final BlockPos sePos = new BlockPos(0, 0, 0);
    private final BlockPos nePos = new BlockPos(0, 0, -32);
    private final BlockPos nwPos = new BlockPos(-32, 0, -32);

    public Parts(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void getCorners() {
        southWest = new Corner().load(plugin);
        Rotator rotator = new Rotator();
        northWest = rotator.clockwise90(southWest);
        northEast = rotator.clockwise90(northWest);
        southEast = rotator.clockwise90(northEast);
    }

    public Material[][][] getSouthWest() {
        return southWest;
    }

    public Material[][][] getNorthWest() {
        return northWest;
    }

    public Material[][][] getNorthEast() {
        return northEast;
    }

    public Material[][][] getSouthEast() {
        return southEast;
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
}
