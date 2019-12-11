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
import me.eccentric_nz.TARDIS.custommodeldata.TARDISSeedModel;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.recipes.TARDISSeedCheck;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

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

    /**
     * Places a configured TARDIS Seed block in the crafting result slot.
     *
     * @param event the player clicking the crafting result slot.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSeedBlockCraft(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();
        if (inv.getType().equals(InventoryType.WORKBENCH) && slot < 10) {
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            TARDISSeedCheck tsc = checkSlots(inv);
            if (tsc.isValid()) {
                if (!crafters.contains(uuid)) {
                    crafters.add(uuid);
                }
                if (slot == 0) {
                    event.setCancelled(true);
                }
                // get the materials in crafting slots
                Material m7 = inv.getItem(7).getType(); // tardis type
                ItemStack is = new ItemStack(Material.RED_MUSHROOM_BLOCK, tsc.getAmount());
                ItemMeta im = is.getItemMeta();
                int model = TARDISSeedModel.modelByMaterial(m7);
                im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, model);
                im.setDisplayName(ChatColor.GOLD + "TARDIS Seed Block");
                im.setCustomModelData(10000000 + model);
                List<String> lore = new ArrayList<>();
                lore.add(t.get(m7));
                lore.add("Walls: " + inv.getItem(6).getType().toString());
                lore.add("Floors: " + inv.getItem(9).getType().toString());
                im.setLore(lore);
                is.setItemMeta(im);
                if (checkPerms(player, m7)) {
                    TARDISMessage.send(player, "SEED_VALID");
                    inv.setItem(0, is);
                    player.updateInventory();
                    if (slot == 0) {
                        event.setCancelled(true);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            // clear the other slots
                            int leftover;
                            for (int i = 1; i < 10; i++) {
                                switch (i) {
                                    case 1:
                                        leftover = tsc.getLampCount() - tsc.getAmount();
                                        tsc.setLampCount(leftover);
                                        setSlotContents(inv, 1, leftover);
                                        break;
                                    case 4:
                                        leftover = tsc.getLapisCount() - tsc.getAmount();
                                        tsc.setLapisCount(leftover);
                                        setSlotContents(inv, 4, leftover);
                                        break;
                                    case 6:
                                        leftover = tsc.getWallCount() - tsc.getAmount();
                                        tsc.setWallCount(leftover);
                                        setSlotContents(inv, 6, leftover);
                                        break;
                                    case 7:
                                        leftover = tsc.getTypeCount() - tsc.getAmount();
                                        tsc.setTypeCount(leftover);
                                        setSlotContents(inv, 7, leftover);
                                        break;
                                    case 9:
                                        leftover = tsc.getFloorCount() - tsc.getAmount();
                                        tsc.setFloorCount(leftover);
                                        setSlotContents(inv, 9, leftover);
                                        break;
                                    default:
                                        inv.setItem(i, null);
                                        break;
                                }
                            }
                            if (!event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                                player.setItemOnCursor(is);
                                crafters.remove(uuid);
                            }
                        }, 1L);
                    }
                } else {
                    TARDISMessage.send(player, "NO_PERMS");
                }
            }
        }
    }

    private void setSlotContents(Inventory inv, int slot, int amount) {
        ItemStack current = inv.getItem(slot);
        if (current != null && amount > 0) {
            inv.setItem(slot, new ItemStack(current.getType(), amount));
        } else {
            inv.setItem(slot, null);
        }
    }

    /**
     * Checks the craft inventory slots contain the correct materials to craft a TARDIS Seed block.
     *
     * @param inv the inventory to check
     * @return whether it is a valid seed block
     */
    private TARDISSeedCheck checkSlots(Inventory inv) {
        TARDISSeedCheck tsc = new TARDISSeedCheck();
        for (int s : spaces) {
            ItemStack is = inv.getItem(s);
            if (is == null) {
                tsc.setValid(false);
                return tsc;
            }
            int a = is.getAmount();
            Material m = is.getType();
            switch (s) {
                case 1:
                    if (!m.equals(Material.REDSTONE_TORCH)) {
                        tsc.setValid(false);
                        return tsc;
                    } else {
                        tsc.setLampCount(a);
                    }
                    break;
                case 4:
                    // must be lapis block
                    if (!m.equals(Material.LAPIS_BLOCK)) {
                        tsc.setValid(false);
                        return tsc;
                    } else {
                        tsc.setLapisCount(a);
                    }
                    break;
                case 6:
                    // must be a valid wall block
                    if (!TARDISWalls.BLOCKS.contains(m)) {
                        tsc.setValid(false);
                        return tsc;
                    } else {
                        tsc.setWallCount(a);
                    }
                    break;
                case 7:
                    // must be a TARDIS block
                    if (!t.containsKey(m)) {
                        tsc.setValid(false);
                        return tsc;
                    } else {
                        tsc.setTypeCount(a);
                    }
                    break;
                default: // 9
                    // must be a valid floor block
                    if (!TARDISWalls.BLOCKS.contains(m)) {
                        tsc.setValid(false);
                        return tsc;
                    } else {
                        tsc.setFloorCount(a);
                    }
                    break;
            }
        }
        tsc.calculateAmount();
        tsc.setValid(true);
        return tsc;
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
                if (is.getType().equals(Material.FILLED_MAP)) {
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
