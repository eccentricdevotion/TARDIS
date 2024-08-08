package me.eccentric_nz.TARDIS.lazarus.disguise;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class ArmourTrim {

    public void giveCustomArmour(Player player, String[] args) {
        String key = "cyberman";
        if (args.length == 2) {
            key = args[1].toLowerCase();
        }
        // make some custom armour trim
        TrimMaterial material = TrimMaterial.IRON;
        TrimPattern pattern = getCustomTrimPattern("tardis:" + key);
        ArmorTrim trim = new ArmorTrim(material, pattern);
        // make a chestplate
        ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ArmorMeta chestMeta = (ArmorMeta) chestplate.getItemMeta();
        // hide the "upgrade" text on the item
        chestMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        chestMeta.setTrim(trim);
        chestplate.setItemMeta(chestMeta);
        player.getInventory().setChestplate(chestplate);
        // make some leggings
        ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ArmorMeta legMeta = (ArmorMeta) leggings.getItemMeta();
        legMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        legMeta.setTrim(trim);
        leggings.setItemMeta(legMeta);
        player.getInventory().setLeggings(leggings);
        // make a helmet
        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        ArmorMeta headMeta = (ArmorMeta) helmet.getItemMeta();
        headMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        headMeta.setTrim(trim);
        helmet.setItemMeta(headMeta);
        player.getInventory().setHelmet(helmet);
    }

    private TrimPattern getCustomTrimPattern(String key) {
        NamespacedKey nsk = NamespacedKey.fromString(key);
        return Bukkit.getRegistry(TrimPattern.class).get(nsk);
    }
}
