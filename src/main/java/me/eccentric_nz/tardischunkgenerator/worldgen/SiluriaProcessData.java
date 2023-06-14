package me.eccentric_nz.tardischunkgenerator.worldgen;

import java.util.HashMap;
import org.bukkit.util.BlockVector;

public class SiluriaProcessData {

    public int x;
    public int y;
    public int z;
    public HashMap<BlockVector, String> grid;

    public SiluriaProcessData(int x, int y, int z, HashMap<BlockVector, String> grid) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.grid = grid;
    }
}
