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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.chemistry.lab;

import me.eccentric_nz.tardis.TARDISPlugin;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

public class BleachRecipe {

	private final TARDISPlugin plugin;
	private final NamespacedKey bedKey;
	private final NamespacedKey woolKey;
	private final NamespacedKey carpetKey;
	private final NamespacedKey bannerKey;
	private final NamespacedKey powderKey;
	private final NamespacedKey helmetKey;
	private final NamespacedKey chestplateKey;
	private final NamespacedKey leggingsKey;
	private final NamespacedKey bootsKey;

	public BleachRecipe(TARDISPlugin plugin) {
		this.plugin = plugin;
		bedKey = new NamespacedKey(this.plugin, "bleached_bed");
		woolKey = new NamespacedKey(this.plugin, "bleached_wool");
		carpetKey = new NamespacedKey(this.plugin, "bleached_carpet");
		bannerKey = new NamespacedKey(this.plugin, "bleached_banner");
		powderKey = new NamespacedKey(this.plugin, "bleached_powder");
		helmetKey = new NamespacedKey(this.plugin, "bleached_helmet");
		chestplateKey = new NamespacedKey(this.plugin, "bleached_chestplate");
		leggingsKey = new NamespacedKey(this.plugin, "bleached_leggings");
		bootsKey = new NamespacedKey(this.plugin, "bleached_boots");
	}

	// concrete, concrete powder
	public void setRecipes() {
		// bleach
		ItemStack bleach = new ItemStack(Material.WHITE_DYE, 1);
		ItemMeta bm = bleach.getItemMeta();
		assert bm != null;
		bm.setDisplayName("Bleach");
		bm.setCustomModelData(10000001);
		bm.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1);
		bleach.setItemMeta(bm);
		// beds
		ItemStack bed = new ItemStack(Material.WHITE_BED, 1);
		ShapelessRecipe bedRecipe = new ShapelessRecipe(bedKey, bed);
		bedRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
		RecipeChoice colouredBeds = new RecipeChoice.MaterialChoice(Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED, Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED, Material.PURPLE_BED, Material.RED_BED, Material.YELLOW_BED);
		bedRecipe.addIngredient(colouredBeds);
		plugin.getServer().addRecipe(bedRecipe);
		// wool
		ItemStack wool = new ItemStack(Material.WHITE_WOOL, 1);
		ShapelessRecipe woolRecipe = new ShapelessRecipe(woolKey, wool);
		woolRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
		RecipeChoice colouredWool = new RecipeChoice.MaterialChoice(Material.BLACK_WOOL, Material.BLUE_WOOL, Material.BROWN_WOOL, Material.CYAN_WOOL, Material.GRAY_WOOL, Material.GREEN_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIGHT_GRAY_WOOL, Material.LIME_WOOL, Material.MAGENTA_WOOL, Material.ORANGE_WOOL, Material.PINK_WOOL, Material.PURPLE_WOOL, Material.RED_WOOL, Material.YELLOW_WOOL);
		woolRecipe.addIngredient(colouredWool);
		plugin.getServer().addRecipe(woolRecipe);
		// carpet
		ItemStack carpet = new ItemStack(Material.WHITE_CARPET, 1);
		ShapelessRecipe carpetRecipe = new ShapelessRecipe(carpetKey, carpet);
		carpetRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
		RecipeChoice colouredCarpet = new RecipeChoice.MaterialChoice(Material.BLACK_CARPET, Material.BLUE_CARPET, Material.BROWN_CARPET, Material.CYAN_CARPET, Material.GRAY_CARPET, Material.GREEN_CARPET, Material.LIGHT_BLUE_CARPET, Material.LIGHT_GRAY_CARPET, Material.LIME_CARPET, Material.MAGENTA_CARPET, Material.ORANGE_CARPET, Material.PINK_CARPET, Material.PURPLE_CARPET, Material.RED_CARPET, Material.YELLOW_CARPET);
		carpetRecipe.addIngredient(colouredCarpet);
		plugin.getServer().addRecipe(carpetRecipe);
		// banners
		ItemStack banner = new ItemStack(Material.WHITE_BANNER, 1);
		ShapelessRecipe bannerRecipe = new ShapelessRecipe(bannerKey, banner);
		bannerRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
		RecipeChoice colouredBanners = new RecipeChoice.MaterialChoice(Material.BLACK_BANNER, Material.BLUE_BANNER, Material.BROWN_BANNER, Material.CYAN_BANNER, Material.GRAY_BANNER, Material.GREEN_BANNER, Material.LIGHT_BLUE_BANNER, Material.LIGHT_GRAY_BANNER, Material.LIME_BANNER, Material.MAGENTA_BANNER, Material.ORANGE_BANNER, Material.PINK_BANNER, Material.PURPLE_BANNER, Material.RED_BANNER, Material.YELLOW_BANNER);
		bannerRecipe.addIngredient(colouredBanners);
		plugin.getServer().addRecipe(bannerRecipe);
		// concrete powder
		ItemStack powder = new ItemStack(Material.WHITE_CONCRETE_POWDER, 1);
		ShapelessRecipe powderRecipe = new ShapelessRecipe(powderKey, powder);
		powderRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
		RecipeChoice colouredPowder = new RecipeChoice.MaterialChoice(Material.BLACK_CONCRETE_POWDER, Material.BLUE_CONCRETE_POWDER, Material.BROWN_CONCRETE_POWDER, Material.CYAN_CONCRETE_POWDER, Material.GRAY_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE_POWDER, Material.LIGHT_GRAY_CONCRETE_POWDER, Material.LIME_CONCRETE_POWDER, Material.MAGENTA_CONCRETE_POWDER, Material.ORANGE_CONCRETE_POWDER, Material.PINK_CONCRETE_POWDER, Material.PURPLE_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER, Material.YELLOW_CONCRETE_POWDER);
		powderRecipe.addIngredient(colouredPowder);
		plugin.getServer().addRecipe(powderRecipe);
		// leather helmet
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta helmetItemMeta = (LeatherArmorMeta) helmet.getItemMeta();
		assert helmetItemMeta != null;
		helmetItemMeta.setColor(Color.WHITE);
		helmet.setItemMeta(helmetItemMeta);
		ShapelessRecipe helmetRecipe = new ShapelessRecipe(helmetKey, helmet);
		helmetRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
		RecipeChoice colouredHelmet = new RecipeChoice.MaterialChoice(Material.LEATHER_HELMET);
		helmetRecipe.addIngredient(colouredHelmet);
		plugin.getServer().addRecipe(helmetRecipe);
		// leather chestplate
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta chestplateItemMeta = (LeatherArmorMeta) chestplate.getItemMeta();
		assert chestplateItemMeta != null;
		chestplateItemMeta.setColor(Color.WHITE);
		chestplate.setItemMeta(chestplateItemMeta);
		ShapelessRecipe chestplateRecipe = new ShapelessRecipe(chestplateKey, chestplate);
		chestplateRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
		RecipeChoice colouredChestplate = new RecipeChoice.MaterialChoice(Material.LEATHER_CHESTPLATE);
		chestplateRecipe.addIngredient(colouredChestplate);
		plugin.getServer().addRecipe(chestplateRecipe);
		// leather leggings
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		LeatherArmorMeta leggingsItemMeta = (LeatherArmorMeta) leggings.getItemMeta();
		assert leggingsItemMeta != null;
		leggingsItemMeta.setColor(Color.WHITE);
		leggings.setItemMeta(leggingsItemMeta);
		ShapelessRecipe leggingsRecipe = new ShapelessRecipe(leggingsKey, leggings);
		leggingsRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
		RecipeChoice colouredLeggings = new RecipeChoice.MaterialChoice(Material.LEATHER_LEGGINGS);
		leggingsRecipe.addIngredient(colouredLeggings);
		plugin.getServer().addRecipe(leggingsRecipe);
		// leather boots
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
		LeatherArmorMeta bootsItemMeta = (LeatherArmorMeta) boots.getItemMeta();
		assert bootsItemMeta != null;
		bootsItemMeta.setColor(Color.WHITE);
		boots.setItemMeta(bootsItemMeta);
		ShapelessRecipe bootsRecipe = new ShapelessRecipe(bootsKey, boots);
		bootsRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
		RecipeChoice colouredBoots = new RecipeChoice.MaterialChoice(Material.LEATHER_BOOTS);
		bootsRecipe.addIngredient(colouredBoots);
		plugin.getServer().addRecipe(bootsRecipe);
	}
}
