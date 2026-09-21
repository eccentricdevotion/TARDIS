/*
 * Copyright (C) 2026 eccentric_nz
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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.sonic.SonicUpgradeData;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

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
                // get the upgrade
                boolean found = false;
                String upgrade = "";
                ItemStack glowstone = inventory.getItem(2);
                if (glowstone != null && glowstone.getType().equals(Material.GLOWSTONE_DUST) && glowstone.hasData(DataComponentTypes.CUSTOM_NAME)) {
                    upgrade = SonicUpgradeData.displayNames.get(ComponentUtils.stripColour(glowstone.getData(DataComponentTypes.CUSTOM_NAME)));
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
                Component dn = sonic.getData(DataComponentTypes.CUSTOM_NAME);
                List<Component> lore;
                if (ComponentUtils.hasLore(sonic)) {
                    // get the current sonic's upgrades
                    lore = new ArrayList<>(sonic.getData(DataComponentTypes.LORE).lines());
                } else {
                    // otherwise this is the first upgrade
                    lore = new ArrayList<>();
                    lore.add(Component.text("Upgrades:"));
                }
                // if they don't already have the upgrade
                if (!lore.contains(Component.text(upgrade))) {
                    is.setData(DataComponentTypes.CUSTOM_NAME, dn);
                    is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                            .build());
                    int index = -1;
                    Component charge = null;
                    for (int i = lore.size() - 1; i >= 0; i--) {
                        if (ComponentUtils.stripColour(lore.get(i)).startsWith("Charge: ")) {
                            charge = lore.get(i);
                            index = i;
                            break;
                        }
                    }
                    if (index != -1 && charge != null) {
                        lore.remove(index);
                        lore.add(Component.text(upgrade));
                        lore.add(charge);
                    } else {
                        lore.add(Component.text(upgrade));
                    }
                    is.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
                    // change the crafting result
                    event.setResult(is);
                } else {
                    event.setResult(null);
                }
            } else if (isDamagedCapacitor(sonic)) {
                ItemStack repaired = sonic.clone();
                is.copyDataFrom(sonic, dataComponentType -> true);
                event.setResult(repaired);
            }
        }
    }

    private boolean isDamagedCapacitor(ItemStack is) {
        if (is == null || is.getType() != Material.BUCKET) {
            return false;
        }
        if (!ComponentUtils.isNamed(is, "Artron Capacitor")) {
            return false;
        }
        return !ComponentUtils.isModelled(is) || NamespacedKey.fromString(is.getData(DataComponentTypes.ITEM_MODEL).asString()).equals(Whoniverse.ARTRON_CAPACITOR_DAMAGED.getKey());
    }
}
