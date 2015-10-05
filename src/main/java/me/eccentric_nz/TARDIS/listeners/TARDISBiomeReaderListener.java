/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBiomeReaderListener implements Listener {

    private final TARDIS plugin;

    public TARDISBiomeReaderListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (plugin.getGeneralKeeper().getInteractables().contains(event.getClickedBlock().getType())) {
            return;
        }
        final Player player = event.getPlayer();
        ItemStack is = player.getItemInHand();
        if (is.getType().equals(Material.CLAY_BRICK) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName() && im.getDisplayName().equals("TARDIS Biome Reader")) {
                Biome biome = event.getClickedBlock().getBiome();
                if (biome.equals(Biome.SKY) || biome.equals(Biome.HELL)) {
                    TARDISMessage.send(player, "BIOME_READER_NOT_VALID");
                    return;
                }
                UUID uuid = player.getUniqueId();
                // check if they have this biome disk yet
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("uuid", uuid.toString());
                ResultSetDiskStorage rs = new ResultSetDiskStorage(plugin, where);
                if (rs.resultSet()) {
                    try {
                        ItemStack[] disks1 = TARDISSerializeInventory.itemStacksFromString(rs.getBiomesOne());
                        if (!hasBiomeDisk(disks1, biome.toString())) {
                            ItemStack[] disks2 = TARDISSerializeInventory.itemStacksFromString(rs.getBiomesTwo());
                            if (!hasBiomeDisk(disks2, biome.toString())) {
                                ItemStack bd = new ItemStack(Material.GREEN_RECORD, 1);
                                ItemMeta dim = bd.getItemMeta();
                                dim.setDisplayName("Biome Storage Disk");
                                List<String> disk_lore = new ArrayList<String>();
                                disk_lore.add(biome.toString());
                                dim.setLore(disk_lore);
                                bd.setItemMeta(dim);
                                int slot = getNextFreeSlot(disks1);
                                if (slot != -1) {
                                    disks1[slot] = bd;
                                    String serialized = TARDISSerializeInventory.itemStacksToString(disks1);
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    set.put("biomes_one", serialized);
                                    HashMap<String, Object> whereu = new HashMap<String, Object>();
                                    whereu.put("uuid", uuid.toString());
                                    new QueryFactory(plugin).doUpdate("storage", set, whereu);
                                    TARDISMessage.send(player, "BIOME_READER_ADDED", biome.toString(), "1");
                                } else {
                                    slot = getNextFreeSlot(disks2);
                                    if (slot != -1) {
                                        disks2[slot] = bd;
                                        String serialized = TARDISSerializeInventory.itemStacksToString(disks2);
                                        HashMap<String, Object> set = new HashMap<String, Object>();
                                        set.put("biomes_two", serialized);
                                        HashMap<String, Object> whereu = new HashMap<String, Object>();
                                        whereu.put("uuid", uuid.toString());
                                        new QueryFactory(plugin).doUpdate("storage", set, whereu);
                                        TARDISMessage.send(player, "BIOME_READER_ADDED", biome.toString(), "2");
                                    } else {
                                        TARDISMessage.send(player, "BIOME_READER_FULL");
                                    }
                                }
                            } else {
                                TARDISMessage.send(player, "BIOME_READER_FOUND", biome.toString(), "2");
                            }
                        } else {
                            TARDISMessage.send(player, "BIOME_READER_FOUND", biome.toString(), "1");
                        }
                    } catch (IOException ex) {
                        plugin.debug("Could not get biome disks: " + ex);
                    }
                }
            }
        }
    }

    public static boolean hasBiomeDisk(ItemStack[] stack, String biome) {
        boolean found = false;
        for (int s = 27; s < stack.length; s++) {
            ItemStack disk = stack[s];
            if (disk != null && disk.hasItemMeta()) {
                ItemMeta diskim = disk.getItemMeta();
                if (diskim.hasLore()) {
                    List<String> lore = diskim.getLore();
                    if (lore.contains(biome)) {
                        found = true;
                        break;
                    }
                }
            }
        }
        return found;
    }

    private int getNextFreeSlot(ItemStack[] stack) {
        int slot = -1;
        for (int s = 27; s < stack.length; s++) {
            ItemStack disk = stack[s];
            if (disk == null) {
                slot = s;
                break;
            }
        }
        return slot;
    }
}
