/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.give;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
            items.put("door-" + r.toLowerCase(Locale.ROOT), "DOOR_" + r.toUpperCase(Locale.ROOT));
            custom.add("door-" + r.toLowerCase(Locale.ROOT));
        }
        for (String r : TARDIS.plugin.getCustomRotorsConfig().getKeys(false)) {
            items.put("time-rotor-" + r.toLowerCase(Locale.ROOT), "TIME_ROTOR_" + r.toUpperCase(Locale.ROOT));
            custom.add("time-rotor-" + r.toLowerCase(Locale.ROOT));
        }
    }
}
