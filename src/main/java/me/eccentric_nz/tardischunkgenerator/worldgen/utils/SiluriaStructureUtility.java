package me.eccentric_nz.tardischunkgenerator.worldgen.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bukkit.util.BlockVector;

public class SiluriaStructureUtility {

    public static HashMap<BlockVector, String> vectorZero = new HashMap<>();
    public static HashMap<BlockVector, String> vectorOne = new HashMap<>();
    public static HashMap<BlockVector, String> vectorTwo = new HashMap<>();
    public static HashMap<BlockVector, String> vectorThree = new HashMap<>();
    public static HashMap<BlockVector, String> vectorFour = new HashMap<>();
    public static HashMap<BlockVector, String> vectorFive = new HashMap<>();
    public static HashMap<BlockVector, HashMap<BlockVector, String>> centres = new HashMap<>();
    public static List<String> structures = Arrays.asList("cross", "east_west", "farm", "large", "lift", "north_south", "small", "temple");

    static {
        // zero
        vectorZero.put(new BlockVector(0, 0, 0), "lift");
        vectorZero.put(new BlockVector(0, 0, -16), "north_south");
        vectorZero.put(new BlockVector(0, 0, 16), "north_south");
        vectorZero.put(new BlockVector(-16, 0, 0), "east_west");
        vectorZero.put(new BlockVector(16, 0, 0), "east_west");
        // one
        vectorOne.put(new BlockVector(0, 0, -16), "large");
        vectorOne.put(new BlockVector(0, 0, 16), "north_south");
        vectorOne.put(new BlockVector(0, 0, 0), "cross");
        vectorOne.put(new BlockVector(-16, 0, 0), "east_west");
        vectorOne.put(new BlockVector(16, 0, 0), "east_west");
        // two
        vectorTwo.put(new BlockVector(0, 0, 0), "temple");
        // three
        vectorThree.put(new BlockVector(0, 0, 0), "north_south");
        vectorThree.put(new BlockVector(0, 0, -16), "cross");
        vectorThree.put(new BlockVector(16, 0, -16), "large");
        vectorThree.put(new BlockVector(0, 0, 16), "small");
        // four
        vectorFour.put(new BlockVector(0, 0, 0), "cross");
        vectorFour.put(new BlockVector(-16, 0, 0), "east_west");
        vectorFour.put(new BlockVector(16, 0, 0), "east_west");
        // five
        vectorFive.put(new BlockVector(0, 0, 0), "farm");
        // centres
        centres.put(new BlockVector(3, 0, 0), vectorOne); // one
        centres.put(new BlockVector(5, 0, 0), vectorTwo); // two
        centres.put(new BlockVector(0, 0, 3), vectorThree); // three
        centres.put(new BlockVector(3, 0, 2), vectorFour); // four
        centres.put(new BlockVector(5, 0, 2), vectorFive); // five
    }
}
