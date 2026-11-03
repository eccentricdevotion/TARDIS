package me.eccentric_nz.TARDIS.rooms.kitchen;

import com.mojang.datafixers.util.Pair;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.FoodProperties;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemType;

public class EdibleLookup {

    public static Pair<Integer, Float> getFoodValues(Material material) {
        ItemType itemType = material.asItemType();
        FoodProperties food = itemType.getDefaultData(DataComponentTypes.FOOD);
        // if it is cake, make values multiplied by number of slices, as we remove the whole item stack
        int multiplier = (itemType == ItemType.CAKE) ? 7 : 1;
        return food != null ? new Pair<>(food.nutrition() * multiplier, food.saturation() * multiplier) : new Pair<>(0, 0.0f);
    }

    public static void print() {
        for (Material m : Material.values()) {
            if (m.isEdible()) {
                FoodProperties food = m.asItemType().getDefaultData(DataComponentTypes.FOOD);
                TARDIS.plugin.debug("Map.entry(Material." + m + ", " + food.nutrition() + "),");
            }
        }
    }
}
