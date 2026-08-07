package me.eccentric_nz.TARDIS.rooms.kitchen;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class EdibleLookup {

    public static final HashMap<Material, Integer> EDIBLE = new HashMap<>(Map.ofEntries(
            Map.entry(Material.RED_MUSHROOM, 1),
            Map.entry(Material.BREAD, 1),
            Map.entry(Material.CARROT, 1),
            Map.entry(Material.BAKED_POTATO, 1),
            Map.entry(Material.POTATO, 1),
            Map.entry(Material.POISONOUS_POTATO, 1),
            Map.entry(Material.GOLDEN_CARROT, 1),
            Map.entry(Material.PUMPKIN_PIE, 1),
            Map.entry(Material.COOKIE, 1),
            Map.entry(Material.MELON, 1),
            Map.entry(Material.MUSHROOM_STEW, 1),
            Map.entry(Material.CHICKEN, 1),
            Map.entry(Material.COOKED_CHICKEN, 1),
            Map.entry(Material.BEEF, 1),
            Map.entry(Material.COOKED_BEEF, 1),
            Map.entry(Material.COD, 1),
            Map.entry(Material.COOKED_COD, 1),
            Map.entry(Material.PORKCHOP, 1),
            Map.entry(Material.COOKED_PORKCHOP, 1),
            Map.entry(Material.MUTTON, 1),
            Map.entry(Material.COOKED_MUTTON, 1),
            Map.entry(Material.APPLE, 1),
            Map.entry(Material.GOLDEN_APPLE, 1),
            Map.entry(Material.ROTTEN_FLESH, 1),
            Map.entry(Material.SPIDER_EYE, 1)
    ));

    public static void print() {
        for (Material m : Material.values()) {
            if (m.isEdible()) {
                TARDIS.plugin.debug("Map.entry(Material." + m + ", 1),");
            }
        }
    }
}
