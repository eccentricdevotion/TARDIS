/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.MusicDisc;
import me.eccentric_nz.TARDIS.enumeration.BiomeLookup;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISDiskCraftListener implements Listener {

    private final TARDIS plugin;
    private final List<InventoryAction> actions = new ArrayList<>();

    public TARDISDiskCraftListener(TARDIS plugin) {
        this.plugin = plugin;
        actions.add(InventoryAction.PLACE_ALL);
        actions.add(InventoryAction.PLACE_ONE);
        actions.add(InventoryAction.PLACE_SOME);
        actions.add(InventoryAction.SWAP_WITH_CURSOR);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftBiomePresetDisk(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();
        if (!inv.getType().equals(InventoryType.WORKBENCH) || slot > 9 || !actions.contains(event.getAction())) {
            return;
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (!checkSlots(inv)) {
                return;
            }
            // get the other ingredients
            List<ItemStack> items = getOtherItems(inv);
            ItemStack disk;
            if (inv.contains(Material.MUSIC_DISC_CAT)) {
                // check it is a Biome Storage Disk
                ItemStack is = inv.getItem(inv.first(Material.MUSIC_DISC_CAT));
                if (is == null || !is.hasItemMeta()) {
                    return;
                }
                ItemMeta im = is.getItemMeta();
                if (!im.hasDisplayName() || !im.getDisplayName().endsWith("Biome Storage Disk") || !im.hasLore()) {
                    return;
                }
                List<String> lore = im.getLore();
                if (!lore.getFirst().equals("Blank")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_BLANK_BIOME");
                    return;
                }
                List<String> disk_lore = new ArrayList<>();
                // biome disk
                Material lookup = items.getFirst().getType();
                Biome biome = BiomeLookup.MATERIALS.get(lookup);
                if (biome != null) {
                    disk_lore.add(biome.toString());
                }
                if (disk_lore.isEmpty()) {
                    return;
                }
                disk = new ItemStack(Material.MUSIC_DISC_CAT, 1);
                ItemMeta dim = disk.getItemMeta();
                dim.setDisplayName("Biome Storage Disk");
                dim.setLore(disk_lore);
                dim.setItemModel(MusicDisc.BIOME_DISK.getKey());
                disk.setItemMeta(dim);
                inv.setItem(0, disk);
                player.updateInventory();
            } else {
                // check if it is a Preset Storage Disk
                ItemStack is = inv.getItem(inv.first(Material.MUSIC_DISC_MALL));
                if (is == null || !is.hasItemMeta()) {
                    return;
                }
                ItemMeta im = is.getItemMeta();
                if (!im.hasDisplayName() || !im.getDisplayName().endsWith("Preset Storage Disk") || !im.hasLore()) {
                    return;
                }
                List<String> lore = im.getLore();
                if (!lore.getFirst().equals("Blank")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_BLANK_PRESET");
                    return;
                }
                // preset disk
                if (items.isEmpty()) {
                    return;
                }
                Material m = items.getFirst().getType();
                String preset = "";
                if (ChameleonPreset.getPreset(m) != null) {
                    preset = ChameleonPreset.getPreset(m).toString();
                }
                if (preset.isEmpty()) {
                    return;
                }
                List<String> disk_lore = List.of(preset);
                disk = new ItemStack(Material.MUSIC_DISC_MALL, 1);
                ItemMeta dim = disk.getItemMeta();
                dim.setDisplayName("Preset Storage Disk");
                dim.setLore(disk_lore);
                dim.setItemModel(MusicDisc.PRESET_DISK.getKey());
                disk.setItemMeta(dim);
                inv.setItem(0, disk);
                player.updateInventory();
            }
        }, 3L);
    }

    private boolean checkSlots(Inventory inv) {
        boolean check = false;
        int count = 0;
        for (int i = 1; i < 10; i++) {
            if (inv.getItem(i) != null && !inv.getItem(i).getType().isAir()) {
                count++;
            }
        }
        if ((inv.contains(Material.MUSIC_DISC_CAT) || inv.contains(Material.MUSIC_DISC_MALL)) && count > 1) {
            check = true;
        }
        return check;
    }

    private List<ItemStack> getOtherItems(Inventory inv) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack is : inv.getContents()) {
            if (is != null) {
                Material m = is.getType();
                if (!m.equals(Material.MUSIC_DISC_CAT) && !m.equals(Material.MUSIC_DISC_MALL) && !m.isAir()) {
                    items.add(is);
                }
            }
        }
        return items;
    }
}
