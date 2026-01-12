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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ChemistryEquipment;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BleachRecipe {

    private final TARDIS plugin;
    private final NamespacedKey bedKey;
    private final NamespacedKey woolKey;
    private final NamespacedKey carpetKey;
    private final NamespacedKey bannerKey;
    private final NamespacedKey powderKey;
    private final NamespacedKey helmetKey;
    private final NamespacedKey chestplateKey;
    private final NamespacedKey leggingsKey;
    private final NamespacedKey bootsKey;
    private final NamespacedKey horseArmourKey;
    private final NamespacedKey wolfArmourKey;

    public BleachRecipe(TARDIS plugin) {
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
        horseArmourKey = new NamespacedKey(this.plugin, "bleached_horse_armour");
        wolfArmourKey = new NamespacedKey(this.plugin, "bleached_wolf_armour");
    }

    // concrete, concrete powder
    public void setRecipes() {
        // bleach
        ItemStack bleach = ItemStack.of(Material.WHITE_CANDLE, 1);
        ItemMeta bm = bleach.getItemMeta();
        bm.displayName(ComponentUtils.toWhite("Bleach"));
        bm.setItemModel(ChemistryEquipment.BLEACH.getKey());
        bm.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1);
        bleach.setItemMeta(bm);
        // beds
        ItemStack bed = ItemStack.of(Material.WHITE_BED, 1);
        ShapelessRecipe bedRecipe = new ShapelessRecipe(bedKey, bed);
        bedRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredBeds = new RecipeChoice.MaterialChoice(List.copyOf(Tag.BEDS.getValues()));
        bedRecipe.addIngredient(colouredBeds);
        plugin.getServer().addRecipe(bedRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Bed", bedRecipe);
        // wool
        ItemStack wool = ItemStack.of(Material.WHITE_WOOL, 1);
        ShapelessRecipe woolRecipe = new ShapelessRecipe(woolKey, wool);
        woolRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredWool = new RecipeChoice.MaterialChoice(List.copyOf(Tag.ITEMS_WOOL.getValues()));
        woolRecipe.addIngredient(colouredWool);
        plugin.getServer().addRecipe(woolRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Wool", woolRecipe);
        // carpet
        ItemStack carpet = ItemStack.of(Material.WHITE_CARPET, 1);
        ShapelessRecipe carpetRecipe = new ShapelessRecipe(carpetKey, carpet);
        carpetRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredCarpet = new RecipeChoice.MaterialChoice(List.copyOf(Tag.ITEMS_WOOL_CARPETS.getValues()));
        carpetRecipe.addIngredient(colouredCarpet);
        plugin.getServer().addRecipe(carpetRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Carpet", carpetRecipe);
        // TODO fix bleached banners
        // banners
        ItemStack banner = ItemStack.of(Material.WHITE_BANNER, 1);
        ShapelessRecipe bannerRecipe = new ShapelessRecipe(bannerKey, banner);
        bannerRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredBanners = new RecipeChoice.MaterialChoice(List.copyOf(Tag.ITEMS_BANNERS.getValues()));
        bannerRecipe.addIngredient(colouredBanners);
        plugin.getServer().addRecipe(bannerRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Banner", bannerRecipe);
        // TODO fix bleached concrete powder
        // concrete powder
        ItemStack powder = ItemStack.of(Material.WHITE_CONCRETE_POWDER, 1);
        ShapelessRecipe powderRecipe = new ShapelessRecipe(powderKey, powder);
        powderRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredPowder = new RecipeChoice.MaterialChoice(List.copyOf(Tag.CONCRETE_POWDER.getValues()));
        powderRecipe.addIngredient(colouredPowder);
        plugin.getServer().addRecipe(powderRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Concrete Powder", powderRecipe);
        // TODO fix bleached armour - all types just remove the colour altogether rather than making white
        // leather helmet
        ItemStack helmet = ItemStack.of(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta helmetItemMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetItemMeta.setColor(Color.WHITE);
        helmet.setItemMeta(helmetItemMeta);
        ShapelessRecipe helmetRecipe = new ShapelessRecipe(helmetKey, helmet);
        helmetRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredHelmet = new RecipeChoice.MaterialChoice(Material.LEATHER_HELMET);
        helmetRecipe.addIngredient(colouredHelmet);
        plugin.getServer().addRecipe(helmetRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Helmet", helmetRecipe);
        // leather chestplate
        ItemStack chestplate = ItemStack.of(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta chestplateItemMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestplateItemMeta.setColor(Color.WHITE);
        chestplate.setItemMeta(chestplateItemMeta);
        ShapelessRecipe chestplateRecipe = new ShapelessRecipe(chestplateKey, chestplate);
        chestplateRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredChestplate = new RecipeChoice.MaterialChoice(Material.LEATHER_CHESTPLATE);
        chestplateRecipe.addIngredient(colouredChestplate);
        plugin.getServer().addRecipe(chestplateRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Chestplate", chestplateRecipe);
        // leather leggings
        ItemStack leggings = ItemStack.of(Material.LEATHER_LEGGINGS, 1);
        LeatherArmorMeta leggingsItemMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsItemMeta.setColor(Color.WHITE);
        leggings.setItemMeta(leggingsItemMeta);
        ShapelessRecipe leggingsRecipe = new ShapelessRecipe(leggingsKey, leggings);
        leggingsRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredLeggings = new RecipeChoice.MaterialChoice(Material.LEATHER_LEGGINGS);
        leggingsRecipe.addIngredient(colouredLeggings);
        plugin.getServer().addRecipe(leggingsRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Leggings", leggingsRecipe);
        // leather boots
        ItemStack boots = ItemStack.of(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta bootsItemMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsItemMeta.setColor(Color.WHITE);
        boots.setItemMeta(bootsItemMeta);
        ShapelessRecipe bootsRecipe = new ShapelessRecipe(bootsKey, boots);
        bootsRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredBoots = new RecipeChoice.MaterialChoice(Material.LEATHER_BOOTS);
        bootsRecipe.addIngredient(colouredBoots);
        plugin.getServer().addRecipe(bootsRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Boots", bootsRecipe);
        // leather horse armour
        ItemStack horseArmour = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta horseItemMeta = (LeatherArmorMeta) horseArmour.getItemMeta();
        horseItemMeta.setColor(Color.WHITE);
        horseArmour.setItemMeta(horseItemMeta);
        ShapelessRecipe horseRecipe = new ShapelessRecipe(horseArmourKey, horseArmour);
        horseRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredHorseArmour = new RecipeChoice.MaterialChoice(Material.LEATHER_HORSE_ARMOR);
        horseRecipe.addIngredient(colouredHorseArmour);
        plugin.getServer().addRecipe(horseRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Horse Armour", horseRecipe);
        // wolf armour
        ItemStack wolfArmour = ItemStack.of(Material.WOLF_ARMOR, 1);
        LeatherArmorMeta wolfItemMeta = (LeatherArmorMeta) wolfArmour.getItemMeta();
        wolfItemMeta.setColor(Color.WHITE);
        wolfArmour.setItemMeta(wolfItemMeta);
        ShapelessRecipe wolfRecipe = new ShapelessRecipe(wolfArmourKey, wolfArmour);
        wolfRecipe.addIngredient(new RecipeChoice.ExactChoice(bleach));
        RecipeChoice colouredWolfArmour = new RecipeChoice.MaterialChoice(Material.WOLF_ARMOR);
        wolfRecipe.addIngredient(colouredWolfArmour);
        plugin.getServer().addRecipe(wolfRecipe);
        plugin.getIncomposita().getShapelessRecipes().put("Bleached Wolf Armour", wolfRecipe);
    }
}
