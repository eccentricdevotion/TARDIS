/*
 * Copyright (C) 2022 eccentric_nz
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
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TARDISCraftListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<Material, String> t = new HashMap<>();
    private final List<UUID> crafters = new ArrayList<>();

    public TARDISCraftListener(TARDIS plugin) {
        this.plugin = plugin;
        // DELUXE, ELEVENTH, TWELFTH, ARS & REDSTONE schematics designed by Lord_Rahl and killeratnight at mcnovus.net
        t.put(Material.BOOKSHELF, "PLANK"); // plank
        t.put(Material.COAL_BLOCK, "STEAMPUNK"); // steampunk
        t.put(Material.DIAMOND_BLOCK, "DELUXE"); // deluxe
        t.put(Material.EMERALD_BLOCK, "ELEVENTH"); // eleventh
        t.put(Material.GOLD_BLOCK, "BIGGER"); // bigger
        t.put(Material.IRON_BLOCK, "BUDGET"); // budget
        t.put(Material.LAPIS_BLOCK, "TOM"); // tom baker
        t.put(Material.NETHER_BRICKS, "MASTER"); // master schematic designed by ShadowAssociate
        t.put(Material.NETHER_WART_BLOCK, "CORAL"); // coral schematic designed by vistaero
        t.put(Material.PRISMARINE, "TWELFTH"); // twelfth
        t.put(Material.ORANGE_CONCRETE, "THIRTEENTH"); // thirteenth designed by Razihel
        t.put(Material.YELLOW_CONCRETE_POWDER, "FACTORY"); // factory designed by Razihel
        t.put(Material.PURPUR_BLOCK, "ENDER"); // ender schematic designed by ToppanaFIN (player at thatsnotacreeper.com)
        t.put(Material.QUARTZ_BLOCK, "ARS"); // ARS
        t.put(Material.REDSTONE_BLOCK, "REDSTONE"); // redstone
        t.put(Material.CRYING_OBSIDIAN, "DELTA"); // delta
        t.put(Material.HONEYCOMB_BLOCK, "ROTOR"); // rotor
        t.put(Material.WARPED_PLANKS, "COPPER"); // copper schematic designed by vistaero
        t.put(Material.SANDSTONE_STAIRS, "PYRAMID"); // pyramid schematic designed by airomis (player at thatsnotacreeper.com)
        t.put(Material.SCULK, "ANCIENT"); // ancient city
        t.put(Material.WHITE_TERRACOTTA, "WAR"); // war doctor
        t.put(Material.DRIPSTONE_BLOCK, "CAVE"); // dripstone cave
        t.put(Material.WEATHERED_COPPER, "WEATHERED"); // weathered copper
        t.put(Material.PACKED_MUD, "ORIGINAL"); // original
        t.put(Material.CYAN_GLAZED_TERRACOTTA, "LEGACY_ELEVENTH"); // legacy_eleventh
        t.put(Material.LIME_GLAZED_TERRACOTTA, "LEGACY_DELUXE"); // legacy_deluxe
        t.put(Material.ORANGE_GLAZED_TERRACOTTA, "LEGACY_BIGGER"); // legacy_bigger
        t.put(Material.RED_GLAZED_TERRACOTTA, "LEGACY_REDSTONE"); // legacy_redstone
        t.put(Material.LIGHT_GRAY_GLAZED_TERRACOTTA, "LEGACY_BUDGET"); // legacy_budget
        // custom seeds
        plugin.getCustomConsolesConfig().getKeys(false).forEach((console) -> {
            if (plugin.getCustomConsolesConfig().getBoolean(console + ".enabled")) {
                if (plugin.getArtronConfig().contains("upgrades." + console.toLowerCase(Locale.ENGLISH))) {
                    Material cmat = Material.valueOf(plugin.getCustomConsolesConfig().getString(console + ".seed"));
                    t.put(cmat, console.toUpperCase(Locale.ENGLISH));
                } else {
                    plugin.getLogger().log(Level.WARNING, "The custom console {0} does not have a corresponding upgrade value in artron.yml", console);
                }
            }
        });
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();
        UUID uuid = p.getUniqueId();
        Inventory inv = event.getInventory();
        if (crafters.contains(uuid) && inv.getType().equals(InventoryType.WORKBENCH)) {
            // remove dropped items around workbench
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> p.getNearbyEntities(6, 6, 6).forEach((e) -> {
                if (e instanceof Item) {
                    e.remove();
                }
            }), 1L);
            crafters.remove(uuid);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftTARDISItem(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (recipe != null) {
            ItemStack is = recipe.getResult();
            CraftingInventory ci = event.getInventory();
            if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                String dn = is.getItemMeta().getDisplayName();
                if ((is.getType().equals(Material.RED_MUSHROOM_BLOCK) || is.getType().equals(Material.MUSHROOM_STEM)) && dn.equals(ChatColor.GOLD + "TARDIS Seed Block")) {
                    ItemMeta im = is.getItemMeta();
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
                        String which = split[1].toLowerCase(Locale.ENGLISH);
                        // set the second line of lore
                        ItemMeta im = is.getItemMeta();
                        List<String> lore;
                        String uses = (plugin.getConfig().getString("circuits.uses." + which).equals("0") || !plugin.getConfig().getBoolean("circuits.damage")) ? ChatColor.YELLOW + "unlimited" : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses." + which);
                        if (im.hasLore()) {
                            lore = im.getLore();
                            lore.set(1, uses);
                        } else {
                            lore = Arrays.asList("Uses left", uses);
                        }
                        im.setLore(lore);
                        is.setItemMeta(im);
                        ci.setResult(is);
                    }
                } else if (is.getType().equals(Material.IRON_SWORD) && dn.equals("Rust Plague Sword")) {
                    // enchant the result
                    is.addEnchantment(Enchantment.DAMAGE_UNDEAD, 2);
                    ci.setResult(is);
                } else if (is.getType().equals(Material.LEATHER_HELMET) && dn.equals("3-D Glasses") || dn.equals("TARDIS Communicator")) {
                    LeatherArmorMeta lam = (LeatherArmorMeta) is.getItemMeta();
                    lam.setColor(Color.WHITE);
                    is.setItemMeta(lam);
                    ci.setResult(is);
                } else if (dn.contains("Key") || dn.contains("Authorised Control")) {
                    HumanEntity human = event.getView().getPlayer();
                    if (human instanceof Player) {
                        ItemMeta im = is.getItemMeta();
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
                }
            }
        }
    }
}
