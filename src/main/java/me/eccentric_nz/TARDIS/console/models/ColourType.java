package me.eccentric_nz.TARDIS.console.models;

import org.bukkit.Material;

import java.util.HashMap;

public class ColourType {

    public static final HashMap<Material, Integer> LOOKUP = new HashMap<>();
    public static final HashMap<Integer, String> COLOURS = new HashMap<>();

    static {
        LOOKUP.put(Material.LIGHT_GRAY_CONCRETE_POWDER, 1);
        LOOKUP.put(Material.GRAY_CONCRETE_POWDER, 2);
        LOOKUP.put(Material.BLACK_CONCRETE_POWDER, 3);
        LOOKUP.put(Material.WHITE_CONCRETE_POWDER, 4);
        LOOKUP.put(Material.RED_CONCRETE_POWDER, 5);
        LOOKUP.put(Material.ORANGE_CONCRETE_POWDER, 6);
        LOOKUP.put(Material.YELLOW_CONCRETE_POWDER, 7);
        LOOKUP.put(Material.LIME_CONCRETE_POWDER, 8);
        LOOKUP.put(Material.GREEN_CONCRETE_POWDER, 9);
        LOOKUP.put(Material.CYAN_CONCRETE_POWDER, 10);
        LOOKUP.put(Material.LIGHT_BLUE_CONCRETE_POWDER, 11);
        LOOKUP.put(Material.BLUE_CONCRETE_POWDER, 12);
        LOOKUP.put(Material.PURPLE_CONCRETE_POWDER, 13);
        LOOKUP.put(Material.MAGENTA_CONCRETE_POWDER, 14);
        LOOKUP.put(Material.PINK_CONCRETE_POWDER, 15);
        LOOKUP.put(Material.BROWN_CONCRETE_POWDER, 16);
        COLOURS.put(1, "LIGHT_GRAY");
        COLOURS.put(2, "GRAY");
        COLOURS.put(3, "BLACK");
        COLOURS.put(4, "WHITE");
        COLOURS.put(5, "RED");
        COLOURS.put(6, "ORANGE");
        COLOURS.put(7, "YELLOW");
        COLOURS.put(8, "LIME");
        COLOURS.put(9, "GREEN");
        COLOURS.put(10, "CYAN");
        COLOURS.put(11, "LIGHT_BLUE");
        COLOURS.put(12, "BLUE");
        COLOURS.put(13, "PURPLE");
        COLOURS.put(14, "MAGENTA");
        COLOURS.put(15, "PINK");
        COLOURS.put(16, "BROWN");
    }
}
