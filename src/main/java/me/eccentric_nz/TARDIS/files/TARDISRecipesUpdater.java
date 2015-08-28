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
    private final HashMap<String, Integer> damage = new HashMap<String, Integer>();

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
        this.damage.put("shaped.TARDIS ARS Circuit.lore", 20);
        this.damage.put("shaped.TARDIS Chameleon Circuit.lore", 25);
        this.damage.put("shaped.TARDIS Input Circuit.lore", 50);
        this.damage.put("shaped.TARDIS Materialisation Circuit.lore", 50);
        this.damage.put("shaped.TARDIS Memory Circuit.lore", 20);
        this.damage.put("shaped.TARDIS Randomiser Circuit.lore", 50);
        this.damage.put("shaped.TARDIS Scanner Circuit.lore", 20);
        this.damage.put("shaped.TARDIS Temporal Circuit.lore", 20);
    }

    public void addRecipes() {
        int i = 0;
        // fix lore
        recipes_config.set("shaped.Stattenheim Remote.lore", "Right-click block~to call TARDIS");
        recipes_config.set("shaped.Artron Storage Cell.lore", "Charge Level~0");
        //
        if (!recipes_config.contains("shaped.TARDIS Remote Key")) {
            recipes_config.set("shaped.TARDIS Remote Key.easy_shape", "RCR,-K-,-T-");
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.C", "REDSTONE_COMPARATOR");
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.K", "GOLD_NUGGET");
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.T", "REDSTONE_TORCH_ON");
            recipes_config.set("shaped.TARDIS Remote Key.hard_shape", "RCR,-K-,-T-");
            recipes_config.set("shaped.TARDIS Remote Key.hard_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.TARDIS Remote Key.hard_ingredients.C", "REDSTONE_COMPARATOR");
            recipes_config.set("shaped.TARDIS Remote Key.hard_ingredients.K", "GOLD_NUGGET");
            recipes_config.set("shaped.TARDIS Remote Key.hard_ingredients.T", "MAP:1964");
            recipes_config.set("shaped.TARDIS Remote Key.result", "GOLD_NUGGET");
            recipes_config.set("shaped.TARDIS Remote Key.amount", 1);
            recipes_config.set("shaped.TARDIS Remote Key.lore", "Deadlock & unlock~Hide & rebuild");
        } else if (recipes_config.getString("shaped.TARDIS Remote Key.easy_ingredients.T").equals("REDSTONE_TORCH")) {
            recipes_config.set("shaped.TARDIS Remote Key.easy_ingredients.T", "REDSTONE_TORCH_ON");
        }
        if (!recipes_config.contains("shaped.White Bow Tie")) {
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
        if (!recipes_config.contains("shaped.Fob Watch")) {
            recipes_config.set("shaped.Fob Watch.easy_shape", "-C-,-W-,R-R");
            recipes_config.set("shaped.Fob Watch.easy_ingredients.C", "MAP:1966");
            recipes_config.set("shaped.Fob Watch.easy_ingredients.W", "WATCH");
            recipes_config.set("shaped.Fob Watch.easy_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.Fob Watch.hard_shape", "-C-,IWI,R-R");
            recipes_config.set("shaped.Fob Watch.hard_ingredients.C", "MAP:1966");
            recipes_config.set("shaped.Fob Watch.hard_ingredients.W", "WATCH");
            recipes_config.set("shaped.Fob Watch.hard_ingredients.I", "IRON_INGOT");
            recipes_config.set("shaped.Fob Watch.hard_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.Fob Watch.result", "WATCH");
            recipes_config.set("shaped.Fob Watch.amount", 1);
            recipes_config.set("shaped.Fob Watch.lore", "");
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
        if (!recipes_config.contains("shaped.TARDIS Randomiser Circuit")) {
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_shape", "-D-,NCE,-W-");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.D", "DIRT");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.N", "NETHERRACK");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.C", "COMPASS");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.E", "ENDER_STONE");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.easy_ingredients.W", "WATER_BUCKET");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_shape", "-D-,NCE,-W-");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.D", "DIRT");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.N", "NETHERRACK");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.C", "COMPASS");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.E", "ENDER_STONE");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.hard_ingredients.W", "WATER_BUCKET");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.result", "MAP:1980");
            recipes_config.set("shaped.TARDIS Randomiser Circuit.amount", 1);
            recipes_config.set("shaped.TARDIS Randomiser Circuit.lore", "Uses left~50");
            i++;
        }
        if (!recipes_config.contains("shaped.TARDIS Invisibility Circuit")) {
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_shape", "-D-,P-E,-W-");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_ingredients.D", "DIAMOND");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_ingredients.P", "MAP:1978");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_ingredients.E", "EMERALD");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.easy_ingredients.W", "POTION:8206");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_shape", "-D-,P-E,-W-");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_ingredients.D", "DIAMOND");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_ingredients.P", "MAP:1978");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_ingredients.E", "EMERALD");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.hard_ingredients.W", "POTION:8270");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.result", "MAP:1981");
            recipes_config.set("shaped.TARDIS Invisibility Circuit.amount", 1);
            recipes_config.set("shaped.TARDIS Invisibility Circuit.lore", "Uses left~5");
            i++;
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
        if (!recipes_config.contains("shaped.Ignite Circuit")) {
            recipes_config.set("shaped.Ignite Circuit.easy_shape", "-N-,NFN,-N-");
            recipes_config.set("shaped.Ignite Circuit.easy_ingredients.N", "NETHERRACK");
            recipes_config.set("shaped.Ignite Circuit.easy_ingredients.F", "FLINT_AND_STEEL");
            recipes_config.set("shaped.Ignite Circuit.hard_shape", "LN-,NFN,-NL");
            recipes_config.set("shaped.Ignite Circuit.hard_ingredients.N", "NETHERRACK");
            recipes_config.set("shaped.Ignite Circuit.hard_ingredients.F", "FLINT_AND_STEEL");
            recipes_config.set("shaped.Ignite Circuit.hard_ingredients.L", "LAVA_BUCKET");
            recipes_config.set("shaped.Ignite Circuit.result", "MAP:1982");
            recipes_config.set("shaped.Ignite Circuit.amount", 1);
            recipes_config.set("shaped.Ignite Circuit.lore", "");
            i++;
        }
        if (!recipes_config.contains("shapeless.Ignite Upgrade")) {
            recipes_config.set("shapeless.Ignite Upgrade.recipe", "BLAZE_ROD,MAP:1982");
            recipes_config.set("shapeless.Ignite Upgrade.result", "BLAZE_ROD");
            recipes_config.set("shapeless.Ignite Upgrade.amount", 1);
            recipes_config.set("shapeless.Ignite Upgrade.lore", "");
            i++;
        }
        if (!recipes_config.contains("shaped.TARDIS Artron Furnace")) {
            recipes_config.set("shaped.TARDIS Artron Furnace.easy_shape", "---,OFO,RRR");
            recipes_config.set("shaped.TARDIS Artron Furnace.easy_ingredients.O", "OBSIDIAN");
            recipes_config.set("shaped.TARDIS Artron Furnace.easy_ingredients.F", "FURNACE");
            recipes_config.set("shaped.TARDIS Artron Furnace.easy_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.TARDIS Artron Furnace.hard_shape", "---,OFO,RRR");
            recipes_config.set("shaped.TARDIS Artron Furnace.hard_ingredients.O", "OBSIDIAN");
            recipes_config.set("shaped.TARDIS Artron Furnace.hard_ingredients.F", "FURNACE");
            recipes_config.set("shaped.TARDIS Artron Furnace.hard_ingredients.R", "REDSTONE");
            recipes_config.set("shaped.TARDIS Artron Furnace.result", "FURNACE");
            recipes_config.set("shaped.TARDIS Artron Furnace.amount", 1);
            recipes_config.set("shaped.TARDIS Artron Furnace.lore", "");
            i++;
        }
        for (Map.Entry<String, Integer> uses : damage.entrySet()) {
            if (recipes_config.getString(uses.getKey()).isEmpty()) {
                recipes_config.set(uses.getKey(), "Uses left~" + uses.getValue());
            }
        }
        try {
            recipes_config.save(new File(plugin.getDataFolder(), "recipes.yml"));
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to recipes.yml");
            }
            String key = recipes_config.getString("shaped.TARDIS Key.result");
            if (!key.equals(plugin.getConfig().getString("preferences.key"))) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "The TARDIS Key recipe result (recipes.yml) does not match the configured key preference (config.yml)");
            }
            String r_key_5 = recipes_config.getString("shaped.TARDIS Remote Key.easy_ingredients.K");
            if (r_key_5 != null && !key.equals(r_key_5)) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "The TARDIS Key ingredient (" + r_key_5 + ") in the 'TARDIS Remote Key' recipe does not match the crafting result of the 'TARDIS Key' recipe (" + key + ") - they should be the same!");
            }
        } catch (IOException io) {
            plugin.debug("Could not save recipes.yml, " + io);
        }
    }
}
