/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISCraftListener implements Listener {

    private final TARDIS plugin;

    public TARDISCraftListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftTARDISItem(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (recipe != null) {
            ItemStack is = recipe.getResult();
            CraftingInventory ci = event.getInventory();
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName()) {
                    String dn = im.getDisplayName();
                    if (dn.equals(ChatColor.GOLD + "TARDIS Seed Block")) {
                        List<String> lore = im.getLore();
                        lore.add("Walls: " + ci.getItem(6).getType());
                        lore.add("Floors: " + ci.getItem(9).getType());
                        lore.add("Chameleon: FACTORY");
                        im.setLore(lore);
                        is.setItemMeta(im);
                        ci.setResult(is);
                    } else if (is.getType().equals(Material.GLOWSTONE_DUST)) {
                        if (DiskCircuit.getCircuitNames().contains(dn)) {
                            // which circuit is it?
                            String[] split = dn.split(" ");
                            String which = split[1].toLowerCase(Locale.ROOT);
                            // set the second line of lore
                            List<String> lore;
                            String uses = (plugin.getConfig().getString("circuits.uses." + which).equals("0") || !plugin.getConfig().getBoolean("circuits.damage")) ? ChatColor.YELLOW + "unlimited" : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses." + which);
                            if (im.hasLore()) {
                                lore = im.getLore();
                                lore.set(1, uses);
                            } else {
                                lore = List.of("Uses left", uses);
                            }
                            im.setLore(lore);
                            is.setItemMeta(im);
                            ci.setResult(is);
                        }
                    } else if (is.getType().equals(Material.IRON_SWORD) && dn.endsWith("Rust Plague Sword")) {
                        // enchant the result
                        is.addEnchantment(Enchantment.SMITE, 2);
                        ci.setResult(is);
                    } else if (is.getType().equals(Material.LEATHER_HELMET) && dn.endsWith("3-D Glasses") || dn.endsWith("TARDIS Communicator")) {
                        LeatherArmorMeta lam = (LeatherArmorMeta) im;
                        lam.setColor(Color.WHITE);
                        is.setItemMeta(lam);
                        ci.setResult(is);
                    } else if (dn.contains("Key") || dn.contains("Authorised Control")) {
                        HumanEntity human = event.getView().getPlayer();
                        if (human instanceof Player) {
                            im.getPersistentDataContainer().set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), human.getUniqueId());
                            List<String> lore = im.getLore();
                            if (lore == null) {
                                lore = new ArrayList<>();
                            }
                            String format = ChatColor.AQUA + "" + ChatColor.ITALIC;
                            String what = dn.contains("Key") ? "key" : "disk";
                            lore.add(format + "This " + what + " belongs to");
                            lore.add(format + human.getName());
                            im.setLore(lore);
                            is.setItemMeta(im);
                            ci.setResult(is);
                        }
                    } else if (dn.startsWith("Door ") && im.hasItemModel()) {
                        // add custom block key to PDC
                        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 10000);
                        is.setItemMeta(im);
                        ci.setResult(is);
                    } else if (dn.contains("Stattenheim")) {
                        int uses = plugin.getConfig().getInt("circuits.uses.stattenheim", 15);
                        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, uses > 0 ? uses : 1000);
                        is.setItemMeta(im);
                        ci.setResult(is);
                    } else if (is.getType() == Material.GLASS && dn.endsWith("Variable Light")) {
                        // set the lore to the material in the centre
                        Material variable = ci.getItem(5).getType();
                        im.setLore(List.of(variable.toString()));
                        is.setItemMeta(im);
                        ci.setResult(is);
                    }
                }
            }
        }
    }
}
