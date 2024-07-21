package me.eccentric_nz.TARDIS.commands.give;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Give {

    public static final HashMap<String, String> items = new HashMap<>();
    public static final List<String> custom = new ArrayList<>();

    static {
        items.put("artron", "");
        items.put("blueprint", "");
        items.put("kit", "");
        items.put("recipes", "");
        items.put("seed", "");
        items.put("system-upgrade", "");
        items.put("tachyon", "");
        items.put("acid-bucket", "Acid Bucket");
        items.put("rust-bucket", "Rust Bucket");
        for (RecipeItem recipeItem : RecipeItem.values()) {
            if (recipeItem.getCategory() != RecipeCategory.SONIC_UPGRADES && recipeItem.getCategory() != RecipeCategory.UNUSED && recipeItem.getCategory() != RecipeCategory.UNCRAFTABLE) {
                items.put(recipeItem.toTabCompletionString(), recipeItem.toRecipeString());
            }
            if (recipeItem.getCategory() == RecipeCategory.CUSTOM_BLOCKS) {
                custom.add(recipeItem.toTabCompletionString());
            }
        }
        for (String r : TARDIS.plugin.getCustomDoorsConfig().getKeys(false)) {
            items.put("door-" + r.toLowerCase(), "DOOR_" + r.toUpperCase());
            custom.add("door-" + r.toLowerCase());
        }
        for (String r : TARDIS.plugin.getCustomRotorsConfig().getKeys(false)) {
            items.put("time-rotor-" + r.toLowerCase(), "TIME_ROTOR_" + r.toUpperCase());
            custom.add("time-rotor-" + r.toLowerCase());
        }
    }
}
