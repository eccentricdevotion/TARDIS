package me.eccentric_nz.TARDIS.rooms.kitchen;

import com.mojang.datafixers.util.Pair;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.FoodProperties;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

public class EdibleLookup {

    public static Pair<Integer, Float> getFoodValues(Material material) {
        FoodProperties food = material.asItemType().getDefaultData(DataComponentTypes.FOOD);
        return food != null ? new Pair<>(food.nutrition(), food.saturation()) : new Pair<>(0, 0.0f);
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
