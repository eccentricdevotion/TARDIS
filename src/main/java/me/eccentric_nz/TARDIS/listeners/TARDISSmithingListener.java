/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TARDISSmithingListener implements Listener {

    private final HashMap<String, String> upgrades = new HashMap<>();
    private final HashMap<Integer, String> customModelData = new HashMap<>();

    public TARDISSmithingListener() {
        upgrades.put("Admin Upgrade", "admin");
        upgrades.put("Bio-scanner Upgrade", "bio");
        upgrades.put("Redstone Upgrade", "redstone");
        upgrades.put("Diamond Upgrade", "diamond");
        upgrades.put("Emerald Upgrade", "emerald");
        upgrades.put("Painter Upgrade", "paint");
        upgrades.put("Ignite Upgrade", "ignite");
        upgrades.put("Pickup Arrows Upgrade", "arrow");
        upgrades.put("Knockback Upgrade", "knockback");
        customModelData.put(10001968, "Admin Upgrade");
        customModelData.put(10001969, "Bio-scanner Upgrade");
        customModelData.put(10001970, "Redstone Upgrade");
        customModelData.put(10001971, "Diamond Upgrade");
        customModelData.put(10001972, "Emerald Upgrade");
        customModelData.put(10001979, "Painter Upgrade");
        customModelData.put(10001982, "Ignite Upgrade");
        customModelData.put(10001984, "Pickup Arrows Upgrade");
        customModelData.put(10001986, "Knockback Upgrade");
    }

    /**
     * This event will check the smithing result to see if it is a sonic upgrade. If it is, then the current sonic
     * screwdriver is queried to see if it has the desired upgrade. If it hasn't (and the player has permission) then
     * the upgrade is added.
     *
     * @param event A player preparing to perform a sonic upgrade
     */
    @EventHandler
    public void onSmithUpgrade(PrepareSmithingEvent event) {
        ItemStack is = event.getResult();
        if (is != null && !is.getType().isAir()) {
            SmithingInventory inventory = event.getInventory();
            // get the current sonic
            ItemStack sonic = inventory.getItem(0);
            if (isSonic(sonic)) {
                ItemMeta im = sonic.getItemMeta();
                // get the upgrade
                boolean found = false;
                String upgrade = "";
                ItemStack glowstone = inventory.getItem(1);
                if (glowstone != null && glowstone.getType().equals(Material.GLOWSTONE_DUST) && glowstone.hasItemMeta()) {
                    ItemMeta rm = glowstone.getItemMeta();
                    assert rm != null;
                    upgrade = customModelData.get(rm.getCustomModelData());
                    found = true;
                }
                // is it a valid upgrade?
                if (!found || !upgrades.containsKey(upgrade)) {
                    event.setResult(null);
                    return;
                }
                // get the player
                HumanEntity human = event.getView().getPlayer();
                Player p = null;
                if (human instanceof Player) {
                    p = (Player) human;
                }
                // make sure the player has permission
                if (p == null || !TARDISPermission.hasPermission(p, "tardis.sonic." + upgrades.get(upgrade))) {
                    event.setResult(null);
                    return;
                }
                ItemMeta sim = sonic.getItemMeta();
                int cmd = 10000011;
                assert sim != null;
                if (sim.hasCustomModelData()) {
                    cmd = sim.getCustomModelData();
                }
                String dn = sim.getDisplayName();
                List<String> lore;
                if (sim.hasLore()) {
                    // get the current sonic's upgrades
                    lore = sim.getLore();
                } else {
                    // otherwise this is the first upgrade
                    lore = new ArrayList<>();
                    lore.add("Upgrades:");
                }
                // if they don't already have the upgrade
                assert lore != null;
                if (!lore.contains(upgrade)) {
                    assert im != null;
                    im.setDisplayName(dn);
                    im.setCustomModelData(cmd);
                    lore.add(upgrade);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    // change the crafting result
                    event.setResult(is);
                } else {
                    event.setResult(null);
                }
            }
        }
    }

    private boolean isSonic(ItemStack is) {
        if (is != null) {
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                assert im != null;
                if (im.hasDisplayName()) {
                    return (ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver"));
                }
            }
        }
        return false;
    }
}
