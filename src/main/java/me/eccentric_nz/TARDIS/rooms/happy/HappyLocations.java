package me.eccentric_nz.TARDIS.rooms.happy;

import com.mojang.datafixers.util.Pair;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class HappyLocations {

    public static List<Pair<Vector, BlockFace>> VECTORS = new ArrayList<>(){
        {
            add(new Pair<>(new Vector(-1.5,2.5,-2.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(-1.5,3.5,-2.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(-1.5,4.5,-2.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(2.5,2.5,-2.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(2.5,3.5,-2.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(2.5,4.5,-2.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(-4.5,2.5,-3.5), BlockFace.SOUTH));
            add(new Pair<>(new Vector(-4.5,3.5,-3.5), BlockFace.SOUTH));
            add(new Pair<>(new Vector(-4.5,4.5,-3.5), BlockFace.SOUTH));
            add(new Pair<>(new Vector(5.5,2.5,-3.5), BlockFace.SOUTH));
            add(new Pair<>(new Vector(5.5,3.5,-3.5), BlockFace.SOUTH));
            add(new Pair<>(new Vector(5.5,4.5,-3.5), BlockFace.SOUTH));
            add(new Pair<>(new Vector(-3.5,2.5,-4.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(-3.5,3.5,-4.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(-3.5,4.5,-4.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(4.5,2.5,-4.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(4.5,3.5,-4.5), BlockFace.NORTH));
            add(new Pair<>(new Vector(4.5,4.5,-4.5), BlockFace.NORTH));
        }
    };
}
