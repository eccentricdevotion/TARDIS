/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.tardis.recipes;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.DiskCircuit;
import me.eccentric_nz.tardis.enumeration.RecipeItem;
import org.bukkit.ChatColor;
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

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISShapedRecipe {

	private final TARDIS plugin;
	private final HashMap<String, ShapedRecipe> shapedRecipes;
	private final HashMap<String, ChatColor> sonic_colour_lookup = new HashMap<>();
	private final HashMap<String, ChatColor> key_colour_lookup = new HashMap<>();
	private ChatColor keyDisplay;
	private ChatColor sonicDisplay;

	public TARDISShapedRecipe(TARDIS plugin) {
		this.plugin = plugin;
		shapedRecipes = new HashMap<>();
		sonic_colour_lookup.put("mark_1", ChatColor.DARK_GRAY);
		sonic_colour_lookup.put("mark_2", ChatColor.YELLOW);
		sonic_colour_lookup.put("mark_3", ChatColor.DARK_PURPLE);
		sonic_colour_lookup.put("mark_4", ChatColor.GRAY);
		sonic_colour_lookup.put("eighth", ChatColor.BLUE);
		sonic_colour_lookup.put("ninth", ChatColor.GREEN);
		sonic_colour_lookup.put("ninth_open", ChatColor.DARK_GREEN);
		sonic_colour_lookup.put("tenth", ChatColor.AQUA);
		sonic_colour_lookup.put("tenth_open", ChatColor.DARK_AQUA);
		sonic_colour_lookup.put("eleventh", null);
		sonic_colour_lookup.put("eleventh_open", ChatColor.LIGHT_PURPLE);
		sonic_colour_lookup.put("master", ChatColor.DARK_BLUE);
		sonic_colour_lookup.put("sarah_jane", ChatColor.RED);
		sonic_colour_lookup.put("river_song", ChatColor.GOLD);
		sonic_colour_lookup.put("twelfth", ChatColor.UNDERLINE);
		sonic_colour_lookup.put("thirteenth", ChatColor.BLACK);
		sonic_colour_lookup.put("war", ChatColor.DARK_RED);
		key_colour_lookup.put("first", ChatColor.AQUA);
		key_colour_lookup.put("second", ChatColor.DARK_BLUE);
		key_colour_lookup.put("third", ChatColor.LIGHT_PURPLE);
		key_colour_lookup.put("fifth", ChatColor.DARK_RED);
		key_colour_lookup.put("seventh", ChatColor.GRAY);
		key_colour_lookup.put("ninth", ChatColor.DARK_PURPLE);
		key_colour_lookup.put("tenth", ChatColor.GREEN);
		key_colour_lookup.put("eleventh", null);
		key_colour_lookup.put("susan", ChatColor.YELLOW);
		key_colour_lookup.put("rose", ChatColor.RED);
		key_colour_lookup.put("sally", ChatColor.DARK_AQUA);
		key_colour_lookup.put("perception", ChatColor.BLUE);
		key_colour_lookup.put("gold", ChatColor.GOLD);
	}

	public void addShapedRecipes() {
		keyDisplay = key_colour_lookup.get(plugin.getConfig().getString("preferences.default_key").toLowerCase(Locale.ENGLISH));
		sonicDisplay = sonic_colour_lookup.get(plugin.getConfig().getString("preferences.default_sonic").toLowerCase(Locale.ENGLISH));
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
		if (s.equals("tardis Key") && keyDisplay != null) {
			im.setDisplayName(keyDisplay + s);
		} else if (s.equals("Sonic Screwdriver") && sonicDisplay != null) {
			im.setDisplayName(sonicDisplay + s);
		} else {
			im.setDisplayName(s);
		}
		im.setCustomModelData(RecipeItem.getByName(s).getCustomModelData());
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
		if (s.endsWith("Bow Tie") || s.equals("3-D Glasses") || s.equals("tardis Communicator")) {
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
			plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + s + " recipe failed! " + ChatColor.RESET + "Check the recipe config file!");
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
