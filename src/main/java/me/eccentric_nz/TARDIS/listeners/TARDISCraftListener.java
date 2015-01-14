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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.rooms.TARDISWallsLookup;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISCraftListener implements Listener {

    private final TARDIS plugin;
    private final List<Integer> c = new ArrayList<Integer>();
    private final List<Integer> l = new ArrayList<Integer>();
    private final HashMap<Material, String> t = new HashMap<Material, String>();
    private final List<Material> hasColour = new ArrayList<Material>();
    private final List<InventoryAction> actions = new ArrayList<InventoryAction>();
    private final TARDISWallsLookup twl;

    public TARDISCraftListener(TARDIS plugin) {
        this.plugin = plugin;
        t.put(Material.BOOKSHELF, "PLANK"); // plank
        t.put(Material.COAL_BLOCK, "STEAMPUNK"); // steampunk
        t.put(Material.DIAMOND_BLOCK, "DELUXE"); // deluxe
        t.put(Material.EMERALD_BLOCK, "ELEVENTH"); // eleventh
        t.put(Material.GOLD_BLOCK, "BIGGER"); // bigger
        t.put(Material.IRON_BLOCK, "BUDGET"); // budget
        t.put(Material.LAPIS_BLOCK, "TOM"); // tom baker
        t.put(Material.PRISMARINE, "TWELFTH"); // twelfth
        t.put(Material.QUARTZ_BLOCK, "ARS"); // ARS
        t.put(Material.REDSTONE_BLOCK, "REDSTONE"); // redstone
        t.put(Material.STAINED_CLAY, "WAR"); // war doctor
        // custom seeds
        for (String console : plugin.getCustomConsolesConfig().getKeys(false)) {
            if (plugin.getCustomConsolesConfig().getBoolean(console + ".enabled")) {
                Material cmat = Material.valueOf(plugin.getCustomConsolesConfig().getString(console + ".seed"));
                t.put(cmat, console.toUpperCase());
            }
        }
        for (Integer i : plugin.getBlocksConfig().getIntegerList("chameleon_blocks")) {
            c.add(i);
        }
        for (Integer a : plugin.getBlocksConfig().getIntegerList("lamp_blocks")) {
            l.add(a);
        }
        hasColour.add(Material.WOOL);
        hasColour.add(Material.STAINED_CLAY);
        hasColour.add(Material.STAINED_GLASS);
        hasColour.add(Material.WOOD);
        hasColour.add(Material.LOG);
        hasColour.add(Material.LOG_2);
        hasColour.add(Material.STONE);
        twl = new TARDISWallsLookup(plugin);
    }

    /**
     * Places a configured TARDIS Seed block in the crafting result slot.
     *
     * @param event the player clicking the crafting result slot.
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSeedBlockCraft(final InventoryClickEvent event) {
        final Inventory inv = event.getInventory();
        final int slot = event.getRawSlot();
        if (inv.getType().equals(InventoryType.WORKBENCH) && slot < 10) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (checkSlots(inv)) {
                        // get the materials in crafting slots
                        Material m5 = inv.getItem(5).getType(); // lamp
                        Material m7 = inv.getItem(7).getType(); // tardis type
                        Material m8 = inv.getItem(8).getType(); // chameleon
                        final ItemStack is = new ItemStack(m7, 1);
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName("§6TARDIS Seed Block");
                        List<String> lore = new ArrayList<String>();
                        lore.add(t.get(m7));
                        lore.add("Walls: " + twl.wall_lookup.get(inv.getItem(6).getType().toString() + ":" + inv.getItem(6).getData().getData()));
                        lore.add("Floors: " + twl.wall_lookup.get(inv.getItem(9).getType().toString() + ":" + inv.getItem(9).getData().getData()));
                        // do some funky stuff to get data values for wool/stained glass & clay/wood/log/log_2
                        if (hasColour.contains(m8)) {
                            switch (m8) {
                                case WOOL:
                                case STAINED_CLAY:
                                case STAINED_GLASS:
                                    lore.add("Chameleon block: " + DyeColor.getByWoolData(inv.getItem(8).getData().getData()) + " " + m8.toString());
                                    break;
                                case STONE:
                                    lore.add("Chameleon block: " + plugin.getUtils().getStoneType(inv.getItem(8).getData().getData()));
                                    break;
                                default:
                                    lore.add("Chameleon block: " + plugin.getUtils().getWoodType(m8, inv.getItem(8).getData().getData()) + " " + m8.toString());
                            }
                        } else {
                            lore.add("Chameleon block: " + m8.toString());
                        }
                        lore.add("Lamp: " + m5.toString());
                        im.setLore(lore);
                        is.setItemMeta(im);
                        final Player player = (Player) event.getWhoClicked();
                        if (checkPerms(player, m7)) {
                            TARDISMessage.send(player, "SEED_VALID");
                            inv.setItem(0, is);
                            player.updateInventory();
                            if (slot == 0) {
                                event.setCancelled(true);
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        // clear the other slots
                                        for (int i = 1; i < 10; i++) {
                                            inv.setItem(i, null);
                                        }
                                        if (!event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                                            player.setItemOnCursor(is);
                                        }
                                    }
                                }, 2L);
                            }
                        } else {
                            TARDISMessage.send(player, "NO_PERMS");
                        }
                    }
                }
            }, 2L);
        }
    }

    /**
     * Checks the craft inventory slots contain the correct materials to craft a
     * TARDIS Seed block.
     *
     * @param inv
     * @return whether it is a valid seed block
     */
    @SuppressWarnings("deprecation")
    private boolean checkSlots(Inventory inv) {
        // check first slot
        ItemStack first = inv.getItem(1);
        if (first == null || !first.getType().equals(Material.REDSTONE_TORCH_ON)) {
            return false;
        }
        for (int j = 4; j < 10; j++) {
            ItemStack is = inv.getItem(j);
            if (is == null) {
                return false;
            }
            int id = is.getTypeId();
            Material m = is.getType();
            switch (j) {
                case 4:
                    // must be lapis block
                    if (!m.equals(Material.LAPIS_BLOCK)) {
                        return false;
                    }
                    break;
                case 5:
                    // must be a valid lamp block
                    if (!l.contains(id)) {
                        return false;
                    }
                    break;
                case 7:
                    // must be a TARDIS block
                    if (!t.containsKey(m)) {
                        return false;
                    }
                    break;
                case 8:
                    // must be a valid chameleon block
                    if (!c.contains(id)) {
                        return false;
                    }
                    break;
                default:
                    // must be a valid wall / floor block
                    if (!twl.wall_lookup.containsKey(m.toString() + ":" + is.getData().getData())) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    private boolean checkPerms(Player p, Material m) {
        SCHEMATIC schm = CONSOLES.getByMaterials().get(m.toString());
        if (schm != null) {
            String perm = schm.getPermission();
            return (perm.equals("budget")) ? true : p.hasPermission("tardis." + perm);
        } else {
            return false;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftInvisibilityCircuit(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        ItemStack is = recipe.getResult();
        if (is.getType().equals(Material.MAP) && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
            String dn = is.getItemMeta().getDisplayName();
            if (DISK_CIRCUIT.getCircuitNames().contains(dn)) {
                // which circuit is it?
                String[] split = dn.split(" ");
                String which = split[1].toLowerCase();
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
                event.getInventory().setResult(is);
            }
        }
    }
}
