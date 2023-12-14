/*
 * Copyright (C) 2023 eccentric_nz
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

import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.sonic.SonicUpgradeData;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
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
import java.util.List;

public class TARDISSmithingListener implements Listener {

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
            ItemStack sonic = inventory.getItem(1);
            if (TARDISStaticUtils.isSonic(sonic)) {
                ItemMeta im = sonic.getItemMeta();
                // get the upgrade
                boolean found = false;
                String upgrade = "";
                ItemStack glowstone = inventory.getItem(2);
                if (glowstone != null && glowstone.getType().equals(Material.GLOWSTONE_DUST) && glowstone.hasItemMeta()) {
                    ItemMeta rm = glowstone.getItemMeta();
                    upgrade = SonicUpgradeData.customModelData.get(rm.getCustomModelData());
                    found = true;
                }
                // is it a valid upgrade?
                if (!found || !SonicUpgradeData.upgrades.containsKey(upgrade)) {
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
                if (p == null || !TARDISPermission.hasPermission(p, "tardis.sonic." + SonicUpgradeData.upgrades.get(upgrade))) {
                    event.setResult(null);
                    return;
                }
                ItemMeta sim = sonic.getItemMeta();
                int cmd = 10000011;
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
                if (!lore.contains(upgrade)) {
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
}
