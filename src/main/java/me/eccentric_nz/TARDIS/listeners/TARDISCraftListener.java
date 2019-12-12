/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
    private final List<Integer> spaces = Arrays.asList(1, 4, 7, 6, 9);

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
        t.put(Material.SANDSTONE_STAIRS, "PYRAMID"); // pyramid schematic designed by airomis (player at thatsnotacreeper.com)
        t.put(Material.WHITE_TERRACOTTA, "WAR"); // war doctor
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

    private boolean checkPerms(Player p, Material m) {
        SCHEMATIC schm = CONSOLES.getBY_MATERIALS().get(m.toString());
        if (schm != null) {
            String perm = schm.getPermission();
            return (perm.equals("budget")) || p.hasPermission("tardis." + perm);
        } else {
            return false;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftTARDISItem(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (recipe != null) {
            ItemStack is = recipe.getResult();
            CraftingInventory ci = event.getInventory();
            if (is.getType().equals(Material.AIR)) {
                // get first map
                int slot = ci.first(Material.FILLED_MAP);
                if (slot != -1) {
                    ItemStack map = ci.getItem(slot);
                    if (map.hasItemMeta() && map.getItemMeta().hasDisplayName() && TARDISConstants.CIRCUITS.contains(map.getItemMeta().getDisplayName())) {
                        // disallow cloning
                        if (ci.first(Material.FILLED_MAP) != -1) {
                            ci.setResult(null);
                            return;
                        }
                    }
                }
            }
            if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                String dn = is.getItemMeta().getDisplayName();
                if (is.getType().equals(Material.RED_MUSHROOM_BLOCK) && dn.equals(ChatColor.GOLD + "TARDIS Seed Block")) {
                    ItemMeta im = is.getItemMeta();
                    List<String> lore = im.getLore();
                    lore.add("Walls: " + ci.getItem(6).getType().toString());
                    lore.add("Floors: " + ci.getItem(9).getType().toString());
                    im.setLore(lore);
                    is.setItemMeta(im);
                    ci.setResult(is);
                } else if (is.getType().equals(Material.FILLED_MAP)) {
                    if (DISK_CIRCUIT.getCircuitNames().contains(dn)) {
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
                }
            }
        }
    }
}
