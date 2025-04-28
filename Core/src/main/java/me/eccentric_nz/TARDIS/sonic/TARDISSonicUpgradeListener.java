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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISSonicUpgradeListener implements Listener {

    /**
     * This event will check the crafting recipe to see if it is a sonic
     * upgrade. If it is, then the current sonic screwdriver is queried to see
     * if it has the desired upgrade. If it hasn't (and the player has
     * permission) then the upgrade is added.
     *
     * @param event A player preparing to craft a sonic upgrade
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onSonicUpgrade(PrepareItemCraftEvent event) {
        CraftingInventory ci = event.getInventory();
        Recipe recipe = ci.getRecipe();
        ItemStack is = ci.getResult();
        // upgrades are all shapeless so only check those
        if (recipe instanceof ShapelessRecipe) {
            // if the recipe result is the same type of material as the sonic screwdriver
            if (is != null && is.getType().equals(Material.BLAZE_ROD) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                // get the upgrade
                boolean found = false;
                String upgrade = im.getDisplayName();
                for (ItemStack glowstone : ci.getContents()) {
                    if (glowstone != null && glowstone.getType().equals(Material.GLOWSTONE_DUST) && glowstone.hasItemMeta()) {
                        ItemMeta rm = glowstone.getItemMeta();
                        String displayName = ChatColor.stripColor(rm.getDisplayName());
                        upgrade = SonicUpgradeData.displayNames.get(displayName);
                        found = true;
                    }
                }
                // is it a valid upgrade?
                if (!found || !SonicUpgradeData.upgrades.containsKey(upgrade)) {
                    ci.setResult(null);
                    return;
                }
                // get the player
                HumanEntity human = event.getView().getPlayer();
                Player p = null;
                if (human instanceof Player) {
                    p = (Player) human;
                }
                // make sure the player has permission
                if (p == null || !TARDISPermission.hasPermission(p, "tardis.sonic." + SonicUpgradeData.upgrades.get(upgrade))) {
                    ci.setResult(null);
                    return;
                }
                // get the current sonic
                ItemStack sonic = null;
                for (int i = 1; i <= ci.getSize(); i++) {
                    ItemStack item = ci.getItem(i);
                    if (item != null && item.getType().equals(Material.BLAZE_ROD) && item.hasItemMeta()) {
                        sonic = item;
                        break;
                    }
                }
                if (sonic == null) {
                    ci.setResult(null);
                } else {
                    ItemMeta sim = sonic.getItemMeta();
                    List<Float> floats = SonicVariant.ELEVENTH.getFloats();
                    if (sim.hasCustomModelDataComponent()) {
                        floats = sim.getCustomModelDataComponent().getFloats();
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
                    if (!lore.contains(upgrade)) {
                        ItemStack upgraded = SonicLore.addUpgrade(lore, dn, floats, is, upgrade);
                        // change the crafting result
                        ci.setResult(upgraded);
                    } else {
                        ci.setResult(null);
                    }
                }
            }
        } else if (recipe instanceof ShapedRecipe) {
            if (is == null || !is.hasItemMeta() || !is.getItemMeta().hasDisplayName() || !is.getItemMeta().getDisplayName().endsWith("TARDIS Remote Key")) {
                return;
            }
            ItemStack key = ci.getItem(5);
            if (!key.hasItemMeta() || !key.getItemMeta().hasDisplayName() || !key.getItemMeta().getDisplayName().endsWith("TARDIS Key")) {
                ci.setResult(null);
                TARDIS.plugin.getMessenger().send(event.getView().getPlayer(), TardisModule.TARDIS, "REMOTE_KEY");
            }
        }
    }
}
