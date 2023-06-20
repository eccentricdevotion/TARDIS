/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.recipes;

import java.util.*;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

/**
 * @author eccentric_nz
 */
public class TARDISShapedRecipe {

    private final TARDIS plugin;
    private final HashMap<String, ShapedRecipe> shapedRecipes;
    private final HashMap<String, Integer> sonicModelLookup = new HashMap<>();
    private final HashMap<String, Integer> keyModelLookup = new HashMap<>();
    private int keyModel = -1;
    private int sonicModel = -1;

    public TARDISShapedRecipe(TARDIS plugin) {
        this.plugin = plugin;
        shapedRecipes = new HashMap<>();
        sonicModelLookup.put("mark_1", 10000001);
        sonicModelLookup.put("mark_2", 10000002);
        sonicModelLookup.put("mark_3", 10000003);
        sonicModelLookup.put("mark_4", 10000004);
        sonicModelLookup.put("eighth", 10000008);
        sonicModelLookup.put("ninth", 10000009);
        sonicModelLookup.put("ninth_open", 12000009);
        sonicModelLookup.put("tenth", 10000010);
        sonicModelLookup.put("tenth_open", 12000010);
        sonicModelLookup.put("eleventh", 10000011);
        sonicModelLookup.put("eleventh_open", 12000011);
        sonicModelLookup.put("master", 10000032);
        sonicModelLookup.put("sarah_jane", 10000033);
        sonicModelLookup.put("river_song", 10000031);
        sonicModelLookup.put("twelfth", 10000012);
        sonicModelLookup.put("thirteenth", 10000013);
        sonicModelLookup.put("war", 10000085);
        keyModelLookup.put("first", 1);
        keyModelLookup.put("second", 2);
        keyModelLookup.put("third", 3);
        keyModelLookup.put("fifth", 4);
        keyModelLookup.put("seventh", 5);
        keyModelLookup.put("ninth", 6);
        keyModelLookup.put("tenth", 7);
        keyModelLookup.put("eleventh", 8);
        keyModelLookup.put("rose", 9);
        keyModelLookup.put("sally", 10);
        keyModelLookup.put("perception", 11);
        keyModelLookup.put("susan", 12);
        keyModelLookup.put("gold", 13);
    }

    public void addShapedRecipes() {
        keyModel = keyModelLookup.get(plugin.getConfig().getString("preferences.default_key").toLowerCase(Locale.ENGLISH));
        sonicModel = sonicModelLookup.get(plugin.getConfig().getString("preferences.default_sonic").toLowerCase(Locale.ENGLISH));
        Set<String> shaped = plugin.getRecipesConfig().getConfigurationSection("shaped").getKeys(false);
        shaped.forEach((s) -> plugin.getServer().addRecipe(makeRecipe(s)));
    }

    private ShapedRecipe makeRecipe(String s) {
        /*
         * shape: A-A,BBB,CDC
         * ingredients: A: STONE B: GRASS_BLOCK C: BIRCH_PLANKS D: DIAMOND_BLOCK=Special Name
         * result: DIAMOND_SWORD
         * amount: 1
         * lore: "The vorpal blade~goes snicker-snack!"
         */
        String result = plugin.getRecipesConfig().getString("shaped." + s + ".result");
        Material mat = Material.valueOf(result);
        int amount = plugin.getRecipesConfig().getInt("shaped." + s + ".amount");
        ItemStack is = new ItemStack(mat, amount);
        ItemMeta im = is.getItemMeta();
        if (s.equals("TARDIS Key") && keyModel != -1) {
            im.setCustomModelData(keyModel);
        } else if (s.equals("Sonic Screwdriver") && sonicModel != -1) {
            im.setCustomModelData(sonicModel);
        } else {
            im.setCustomModelData(RecipeItem.getByName(s).getCustomModelData());
        }
        im.setDisplayName(s);
        if (!plugin.getRecipesConfig().getString("shaped." + s + ".lore").equals("")) {
            if (mat.equals(Material.GLOWSTONE_DUST) && DiskCircuit.getCircuitNames().contains(s)) {
                // which circuit is it?
                String[] split = s.split(" ");
                String which = split[1].toLowerCase(Locale.ENGLISH);
                // set the second line of lore
                List<String> lore;
                String uses = (plugin.getConfig().getString("circuits.uses." + which).equals("0") || !plugin.getConfig().getBoolean("circuits.damage")) ? ChatColor.YELLOW + "unlimited" : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses." + which);
                lore = Arrays.asList("Uses left", uses);
                im.setLore(lore);
            } else {
                im.setLore(Arrays.asList(plugin.getRecipesConfig().getString("shaped." + s + ".lore").split("~")));
            }
        }
        if (s.endsWith("Bow Tie") || s.equals("3-D Glasses") || s.equals("TARDIS Communicator")) {
            Damageable damageable = (Damageable) im;
            damageable.setDamage(50);
            im.addItemFlags(ItemFlag.values());
        }
        if (s.endsWith("Disk")) {
            im.addItemFlags(ItemFlag.values());
        }
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, s.replace(" ", "_").toLowerCase(Locale.ENGLISH));
        ShapedRecipe r = new ShapedRecipe(key, is);
        // get shape
        String difficulty = (plugin.getDifficulty().equals(Difficulty.MEDIUM)) ? "easy" : plugin.getConfig().getString("preferences.difficulty").toLowerCase(Locale.ENGLISH);
        try {
            String[] shape_tmp = plugin.getRecipesConfig().getString("shaped." + s + "." + difficulty + "_shape").split(",");
            String[] shape = new String[shape_tmp.length];
            for (int i = 0; i < shape_tmp.length; i++) {
                shape[i] = shape_tmp[i].replaceAll("-", " ");
            }
            if (shape_tmp.length > 2) {
                r.shape(shape[0], shape[1], shape[2]);
            } else {
                r.shape(shape[0], shape[1]);
            }
            Set<String> ingredients = plugin.getRecipesConfig().getConfigurationSection("shaped." + s + "." + difficulty + "_ingredients").getKeys(false);
            ingredients.forEach((g) -> {
                char c = g.charAt(0);
                String i = plugin.getRecipesConfig().getString("shaped." + s + "." + difficulty + "_ingredients." + g);
                if (i.contains("=")) {
                    ItemStack exact;
                    String[] choice = i.split("=");
                    Material m = Material.valueOf(choice[0]);
                    exact = new ItemStack(m, 1);
                    ItemMeta em = exact.getItemMeta();
                    em.setDisplayName(choice[1]);
                    em.setCustomModelData(RecipeItem.getByName(choice[1]).getCustomModelData());
                    if (m.equals(Material.GLOWSTONE_DUST) && DiskCircuit.getCircuitNames().contains(choice[1])) {
                        // which circuit is it?
                        String[] split = choice[1].split(" ");
                        String which = split[1].toLowerCase(Locale.ENGLISH);
                        // set the second line of lore
                        List<String> lore;
                        String uses = (plugin.getConfig().getString("circuits.uses." + which).equals("0") || !plugin.getConfig().getBoolean("circuits.damage")) ? ChatColor.YELLOW + "unlimited" : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses." + which);
                        lore = Arrays.asList("Uses left", uses);
                        em.setLore(lore);
                    }
                    exact.setItemMeta(em);
                    r.setIngredient(c, new RecipeChoice.ExactChoice(exact));
                } else if (i.contains(">")) {
                    ItemStack potion;
                    String[] choice = i.split(">");
                    potion = new ItemStack(Material.POTION, 1);
                    PotionMeta pm = (PotionMeta) potion.getItemMeta();
                    PotionType potionType;
                    try {
                        potionType = PotionType.valueOf(choice[1]);
                    } catch (IllegalArgumentException e) {
                        potionType = PotionType.INVISIBILITY;
                    }
                    PotionData potionData = new PotionData(potionType);
                    pm.setBasePotionData(potionData);
                    potion.setItemMeta(pm);
                    r.setIngredient(c, new RecipeChoice.ExactChoice(potion));
                } else if (i.contains("≈")) {
                    ItemStack book;
                    String[] choice = i.split("≈");
                    book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                    EnchantmentStorageMeta pm = (EnchantmentStorageMeta) book.getItemMeta();
                    Enchantment enchantment;
                    try {
                        enchantment = Enchantment.getByKey(NamespacedKey.minecraft(choice[1].toLowerCase()));
                    } catch (IllegalArgumentException e) {
                        enchantment = Enchantment.KNOCKBACK;
                    }
                    pm.addStoredEnchant(enchantment, 1, false);
                    book.setItemMeta(pm);
                    r.setIngredient(c, new RecipeChoice.ExactChoice(book));
                } else {
                    Material m = Material.valueOf(i);
                    r.setIngredient(c, m);
                }
            });
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, s + " recipe failed! Check the recipe config file!");
        }
        if (s.contains("Bow Tie")) {
            r.setGroup("Bow Ties");
        }
        shapedRecipes.put(s, r);
        return r;
    }

    public HashMap<String, ShapedRecipe> getShapedRecipes() {
        return shapedRecipes;
    }
}
