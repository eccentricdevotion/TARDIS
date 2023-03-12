package me.eccentric_nz.tardischunkgenerator.worldgen.utils;

import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkaroStructureUtility {

    public static List<BlockVector> vectorLeft = new ArrayList<>();
    public static List<BlockVector> vectorUp = new ArrayList<>();
    public static List<BlockVector> vectorRight = new ArrayList<>();
    public static List<BlockVector> vectorDown = new ArrayList<>();
    public static List<String> structures = Arrays.asList("small", "farm", "tower", "pen", "water", "small");

    static {
        // left
        vectorLeft.add(new BlockVector(-11, 0, 2)); // connected small
        vectorLeft.add(new BlockVector(2, 0, -14)); // farm
        vectorLeft.add(new BlockVector(24, 0, -15)); // tower
        vectorLeft.add(new BlockVector(21, 0, 14)); // animal pen
        vectorLeft.add(new BlockVector(5, 0, 24)); // water feature
        vectorLeft.add(new BlockVector(-15, 0, 20)); // small
        // up
        vectorUp.add(new BlockVector(2, 0, -11)); // connected small
        vectorUp.add(new BlockVector(23, 0, 2)); // farm
        vectorUp.add(new BlockVector(24, 0, 24)); // tower
        vectorUp.add(new BlockVector(-8, 0, 21)); // animal pen
        vectorUp.add(new BlockVector(-13, 0, 5)); // water feature
        vectorUp.add(new BlockVector(-15, 0, -15)); // small
        // right
        vectorRight.add(new BlockVector(15, 0, 2)); // connected small
        vectorRight.add(new BlockVector(7, 0, 23)); // farm
        vectorRight.add(new BlockVector(-15, 0, 24)); // tower
        vectorRight.add(new BlockVector(-15, 0, -8)); // animal pen
        vectorRight.add(new BlockVector(5, 0, -13)); // water feature
        vectorRight.add(new BlockVector(20, 0, -15)); // small
        // down
        vectorDown.add(new BlockVector(2, 0, 15)); // connected small
        vectorDown.add(new BlockVector(-14, 0, 7)); // farm
        vectorDown.add(new BlockVector(-15, 0, -15)); // tower
        vectorDown.add(new BlockVector(14, 0, -15)); // animal pen
        vectorDown.add(new BlockVector(24, 0, 5)); // water feature
        vectorDown.add(new BlockVector(20, 0, 20)); // small
    }
}
