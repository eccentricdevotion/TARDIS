/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRecipesUpdater {

    private final TARDIS plugin;
    private FileConfiguration recipes_config = null;
    private final HashMap<String, Integer> flavours = new HashMap<String, Integer>();
    private final HashMap<String, Integer> colours = new HashMap<String, Integer>();

    public TARDISRecipesUpdater(TARDIS plugin) {
        this.plugin = plugin;
        this.recipes_config = plugin.getRecipesConfig();
        this.flavours.put("Licorice", 0);
        this.flavours.put("Raspberry", 1);
        this.flavours.put("Apple", 2);
        this.flavours.put("Cappuccino", 3);
        this.flavours.put("Blueberry", 4);
        this.flavours.put("Grape", 5);
        this.flavours.put("Island Punch", 6);
        this.flavours.put("Vodka", 7);
        this.flavours.put("Earl Grey", 8);
        this.flavours.put("Strawberry", 9);
        this.flavours.put("Lime", 10);
        this.flavours.put("Lemon", 11);
        this.flavours.put("Bubblegum", 12);
        this.flavours.put("Watermelon", 13);
        this.flavours.put("Orange", 14);
        this.flavours.put("Vanilla", 15);
        this.colours.put("White", 0);
        this.colours.put("Orange", 1);
        this.colours.put("Magenta", 2);
        this.colours.put("Light Blue", 3);
        this.colours.put("Yellow", 4);
        this.colours.put("Lime", 5);
        this.colours.put("Pink", 6);
        this.colours.put("Grey", 7);
        this.colours.put("Light Grey", 8);
        this.colours.put("Cyan", 9);
        this.colours.put("Purple", 10);
        this.colours.put("Blue", 11);
        this.colours.put("Brown", 12);
        this.colours.put("Green", 13);
        this.colours.put("Red", 14);
        this.colours.put("Black", 15);
    }

    public void addRecipes() {
        int i = 0;
        if (!recipes_config.contains("shapeless.White Bow Tie")) {
            for (Map.Entry<String, Integer> map : colours.entrySet()) {
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.easy_shape", "---,SWS,---");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.easy_ingredients.S", "STRING");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.easy_ingredients.W", "WOOL:" + map.getValue());
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.hard_shape", "STS,L-L,WWW");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.hard_ingredients.S", "STRING");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.hard_ingredients.T", "TRIPWIRE_HOOK");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.hard_ingredients.L", "LEATHER");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.hard_ingredients.W", "WOOL:" + map.getValue());
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.result", "LEATHER_CHESTPLATE");
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.amount", 1);
                recipes_config.set("shaped." + map.getKey() + " Bow Tie.lore", "Bow ties are cool!");
                i++;
            }
        }
        if (!recipes_config.contains("shaped.3-D Glasses")) {
            recipes_config.set("shaped.3-D Glasses.easy_shape", "---,P-P,CPM");
            recipes_config.set("shaped.3-D Glasses.easy_ingredients.P", "PAPER");
            recipes_config.set("shaped.3-D Glasses.easy_ingredients.C", "STAINED_GLASS_PANE:9");
            recipes_config.set("shaped.3-D Glasses.easy_ingredients.M", "STAINED_GLASS_PANE:2");
            recipes_config.set("shaped.3-D Glasses.hard_shape", "R-T,P-P,CPM");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.R", "REDSTONE_COMPARATOR");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.T", "REDSTONE_TORCH_ON");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.P", "PAPER");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.C", "STAINED_GLASS_PANE:9");
            recipes_config.set("shaped.3-D Glasses.hard_ingredients.M", "STAINED_GLASS_PANE:2");
            recipes_config.set("shaped.3-D Glasses.result", "LEATHER_HELMET");
            recipes_config.set("shaped.3-D Glasses.amount", 1);
            recipes_config.set("shaped.3-D Glasses.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.Jammy Dodger")) {
            recipes_config.set("shaped.Jammy Dodger.easy_shape", "---,WRW,---");
            recipes_config.set("shaped.Jammy Dodger.easy_ingredients.W", "WHEAT");
            recipes_config.set("shaped.Jammy Dodger.easy_ingredients.R", "INK_SACK:1");
            recipes_config.set("shaped.Jammy Dodger.hard_shape", "---,WRW,---");
            recipes_config.set("shaped.Jammy Dodger.hard_ingredients.W", "WHEAT");
            recipes_config.set("shaped.Jammy Dodger.hard_ingredients.R", "INK_SACK:1");
            recipes_config.set("shaped.Jammy Dodger.result", "COOKIE");
            recipes_config.set("shaped.Jammy Dodger.amount", 8);
            recipes_config.set("shaped.Jammy Dodger.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.Fish Finger")) {
            recipes_config.set("shaped.Fish Finger.easy_shape", "-B-,-F-,-B-");
            recipes_config.set("shaped.Fish Finger.easy_ingredients.B", "BREAD");
            recipes_config.set("shaped.Fish Finger.easy_ingredients.F", "RAW_FISH");
            recipes_config.set("shaped.Fish Finger.hard_shape", "-B-,-F-,-B-");
            recipes_config.set("shaped.Fish Finger.hard_ingredients.B", "BREAD");
            recipes_config.set("shaped.Fish Finger.hard_ingredients.F", "RAW_FISH");
            recipes_config.set("shaped.Fish Finger.result", "COOKED_FISH");
            recipes_config.set("shaped.Fish Finger.amount", 3);
            recipes_config.set("shaped.Fish Finger.lore", "Best eaten with custard!");
            i++;
        }
        if (!recipes_config.contains("shapeless.Bowl of Custard")) {
            recipes_config.set("shapeless.Bowl of Custard.recipe", "BOWL,MILK_BUCKET,EGG");
            recipes_config.set("shapeless.Bowl of Custard.result", "MUSHROOM_SOUP");
            recipes_config.set("shapeless.Bowl of Custard.amount", 1);
            recipes_config.set("shapeless.Bowl of Custard.lore", "");
            i++;
        }
        if (!recipes_config.contains("shapeless.Vanilla Jelly Baby")) {
            for (Map.Entry<String, Integer> map : flavours.entrySet()) {
                recipes_config.set("shapeless." + map.getKey() + " Jelly Baby.recipe", "SUGAR,SLIME_BALL,INK_SACK:" + map.getValue());
                recipes_config.set("shapeless." + map.getKey() + " Jelly Baby.result", "MELON");
                recipes_config.set("shapeless." + map.getKey() + " Jelly Baby.amount", 4);
                recipes_config.set("shapeless." + map.getKey() + " Jelly Baby.lore", "");
                i++;
            }
        }
        if (!recipes_config.contains("shaped.Painter Circuit")) {
            recipes_config.set("shaped.Painter Circuit.easy_shape", "-I-,DGD,-I-");
            recipes_config.set("shaped.Painter Circuit.easy_ingredients.I", "INK_SACK:0");
            recipes_config.set("shaped.Painter Circuit.easy_ingredients.D", "INK_SACK:5");
            recipes_config.set("shaped.Painter Circuit.easy_ingredients.G", "GOLD_NUGGET");
            recipes_config.set("shaped.Painter Circuit.hard_shape", "-I-,DGD,-I-");
            recipes_config.set("shaped.Painter Circuit.hard_ingredients.I", "INK_SACK:0");
            recipes_config.set("shaped.Painter Circuit.hard_ingredients.D", "INK_SACK:5");
            recipes_config.set("shaped.Painter Circuit.hard_ingredients.G", "GOLD_BLOCK");
            recipes_config.set("shaped.Painter Circuit.result", "MAP:1979");
            recipes_config.set("shaped.Painter Circuit.amount", 1);
            recipes_config.set("shaped.Painter Circuit.lore", "");
            i++;
        } else {
            // fix the hard recipe if necessary
            if (recipes_config.get("shaped.Painter Circuit.hard_shape").equals("-B-,-F-,-B-")) {
                recipes_config.set("shaped.Painter Circuit.hard_shape", "-I-,DGD,-I-");
            }
        }
        if (!recipes_config.contains("shapeless.Painter Upgrade")) {
            recipes_config.set("shapeless.Painter Upgrade.recipe", "BLAZE_ROD,MAP:1979");
            recipes_config.set("shapeless.Painter Upgrade.result", "BLAZE_ROD");
            recipes_config.set("shapeless.Painter Upgrade.amount", 1);
            recipes_config.set("shapeless.Painter Upgrade.lore", "");
            i++;
        }
        try {
            recipes_config.save(new File(plugin.getDataFolder(), "recipes.yml"));
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to recipes.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save recipes.yml, " + io);
        }
    }
}
