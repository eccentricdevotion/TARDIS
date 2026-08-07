package me.eccentric_nz.TARDIS.rooms.kitchen;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.FoodProperties;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EdibleLookup {

    public static final HashMap<Material, Integer> EDIBLE = new HashMap<>(Map.ofEntries(
            Map.entry(Material.APPLE, 4),
            Map.entry(Material.BAKED_POTATO, 5),
            Map.entry(Material.BEEF, 3),
            Map.entry(Material.BEETROOT, 1),
            Map.entry(Material.BEETROOT_SOUP, 6),
            Map.entry(Material.BREAD, 5),
            Map.entry(Material.CARROT, 3),
            Map.entry(Material.CHICKEN, 2),
            Map.entry(Material.CHORUS_FRUIT, 4),
            Map.entry(Material.COD, 2),
            Map.entry(Material.COOKED_BEEF, 8),
            Map.entry(Material.COOKED_CHICKEN, 6),
            Map.entry(Material.COOKED_COD, 5),
            Map.entry(Material.COOKED_MUTTON, 6),
            Map.entry(Material.COOKED_PORKCHOP, 8),
            Map.entry(Material.COOKED_RABBIT, 5),
            Map.entry(Material.COOKED_SALMON, 6),
            Map.entry(Material.COOKIE, 2),
            Map.entry(Material.DRIED_KELP, 1),
            Map.entry(Material.ENCHANTED_GOLDEN_APPLE, 4),
            Map.entry(Material.GLOW_BERRIES, 2),
            Map.entry(Material.GOLDEN_APPLE, 4),
            Map.entry(Material.GOLDEN_CARROT, 6),
            Map.entry(Material.HONEY_BOTTLE, 6),
            Map.entry(Material.MELON_SLICE, 2),
            Map.entry(Material.MUSHROOM_STEW, 6),
            Map.entry(Material.MUTTON, 2),
            Map.entry(Material.POISONOUS_POTATO, 2),
            Map.entry(Material.PORKCHOP, 3),
            Map.entry(Material.POTATO, 1),
            Map.entry(Material.PUFFERFISH, 1),
            Map.entry(Material.PUMPKIN_PIE, 8),
            Map.entry(Material.RABBIT, 3),
            Map.entry(Material.RABBIT_STEW, 10),
            Map.entry(Material.ROTTEN_FLESH, 4),
            Map.entry(Material.SALMON, 2),
            Map.entry(Material.SPIDER_EYE, 2),
            Map.entry(Material.SUSPICIOUS_STEW, 6),
            Map.entry(Material.SWEET_BERRIES, 2),
            Map.entry(Material.TROPICAL_FISH, 1)
    ));

    public static void print() {
        for (Material m : Material.values()) {
            if (m.isEdible()) {
                FoodProperties food = m.asItemType().getDefaultData(DataComponentTypes.FOOD);
                TARDIS.plugin.debug("Map.entry(Material." + m + ", " + food.nutrition() + "),");
            }
        }
    }
}
