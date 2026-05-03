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
package me.eccentric_nz.TARDIS.sonic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class SonicUpgradeListener implements Listener {

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
            if (is != null && is.getType().equals(Material.BLAZE_ROD)) {
                // get the upgrade
                boolean found = false;
                String upgrade = ComponentUtils.stripColour(is.getData(DataComponentTypes.CUSTOM_NAME));
                for (ItemStack glowstone : ci.getContents()) {
                    if (glowstone != null && glowstone.getType().equals(Material.GLOWSTONE_DUST) && glowstone.hasData(DataComponentTypes.CUSTOM_NAME)) {
                        String customName = ComponentUtils.stripColour(glowstone.getData(DataComponentTypes.CUSTOM_NAME));
                        upgrade = SonicUpgradeData.displayNames.get(customName);
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
                    if (item != null && item.getType().equals(Material.BLAZE_ROD) && item.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
                        sonic = item;
                        break;
                    }
                }
                if (sonic == null) {
                    ci.setResult(null);
                } else {
                    List<Float> floats = SonicVariant.ELEVENTH.getFloats();
                    if (sonic.hasData(DataComponentTypes.CUSTOM_MODEL_DATA)) {
                        floats = sonic.getData(DataComponentTypes.CUSTOM_MODEL_DATA).floats();
                    }
                    String dn = ComponentUtils.stripColour(sonic.getData(DataComponentTypes.CUSTOM_NAME));
                    List<Component> lore;
                    if (sonic.hasData(DataComponentTypes.LORE)) {
                        // get the current sonic's upgrades
                        lore = new ArrayList<>(sonic.getData(DataComponentTypes.LORE).lines());
                    } else {
                        // otherwise this is the first upgrade
                        lore = new ArrayList<>();
                        lore.add(Component.text("Upgrades:"));
                    }
                    // if they don't already have the upgrade
                    if (!lore.contains(Component.text(upgrade))) {
                        ItemStack upgraded = SonicLore.addUpgrade(lore, dn, floats, is, upgrade);
                        // change the crafting result
                        ci.setResult(upgraded);
                    } else {
                        ci.setResult(null);
                    }
                }
            }
        } else if (recipe instanceof ShapedRecipe) {
            if (is == null || !ComponentUtils.isNamed(is, "TARDIS Remote Key")) {
                return;
            }
            ItemStack key = ci.getItem(5);
            if (!ComponentUtils.isNamed(key, "TARDIS Key")) {
                ci.setResult(null);
                TARDIS.plugin.getMessenger().send(event.getView().getPlayer(), TardisModule.TARDIS, "REMOTE_KEY");
            }
        }
    }
}
