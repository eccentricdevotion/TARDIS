package me.eccentric_nz.TARDIS.lights;

import org.bukkit.Material;

import java.util.List;

public class Sequences {

    public static List<List<Material>> PRESETS = List.of(
            List.of(Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.PINK_WOOL, Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.PINK_WOOL, Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.PINK_WOOL),
            List.of(Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL),
            List.of(Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.GREEN_WOOL, Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.PINK_WOOL, Material.BLACK_WOOL, Material.BLACK_WOOL)
    );

    public static List<List<Integer>> DELAYS = List.of(
            List.of(20,20,20,20,20,20,20,20,20),
            List.of(40,40,40,40,40,40,40,40,40),
            List.of(30,20,30,20,30,20,30,20,30)
    );

    public static List<List<Integer>> LEVELS = List.of(
            List.of(5,5,5,15,15,15,10,10,10),
            List.of(15,15,15,15,15,15,15,15,15),
            List.of(15,15,15,15,15,15,15,15,15)
    );
}
