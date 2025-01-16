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
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.enumeration.SonicScrewdriver;
import me.eccentric_nz.TARDIS.recipes.shaped.SonicScrewdriverRecipe;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class TARDISSonicSound {

    private static final HashMap<UUID, Long> timeout = new HashMap<>();

    public static void playSonicSound(TARDIS plugin, Player player, long now, long cooldown, String sound) {
        if ((!timeout.containsKey(player.getUniqueId()) || timeout.get(player.getUniqueId()) < now)) {
            ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
            // change model to 'on/open', then after scheduled time change back to 'off/closed' model
            NamespacedKey sonicModel = SonicScrewdriverRecipe.sonicModelLookup.getOrDefault(plugin.getConfig().getString("sonic.default_model").toLowerCase(Locale.ROOT), SonicVariant.ELEVENTH.getKey());
            if (im.hasItemModel()) {
                sonicModel = im.getItemModel();
            }
            SonicScrewdriver screwdriver = SonicScrewdriver.getByKey(sonicModel.getKey());
            im.setItemModel(screwdriver.getActive());
            player.getInventory().getItemInMainHand().setItemMeta(im);
            timeout.put(player.getUniqueId(), now + cooldown);
            TARDISSounds.playTARDISSound(player.getLocation(), sound);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ItemStack is = player.getInventory().getItemInMainHand();
                if (is.hasItemMeta()) {
                    ItemMeta meta = is.getItemMeta();
                    if (meta.hasDisplayName() && meta.getDisplayName().endsWith("Sonic Screwdriver")) {
                        player.getInventory().getItemInMainHand().getEnchantments().keySet().forEach((e) -> player.getInventory().getItemInMainHand().removeEnchantment(e));
                        meta.setItemModel(screwdriver.getModel());
                        is.setItemMeta(meta);
                    } else {
                        // find the screwdriver in the player's inventory
                        revertSonic(player.getInventory(), screwdriver.getModel());
                    }
                } else {
                    // find the screwdriver in the player's inventory
                    revertSonic(player.getInventory(), screwdriver.getModel());
                }
            }, (cooldown / 50L));
        }
    }

    private static void revertSonic(PlayerInventory inv, NamespacedKey model) {
        int first = inv.first(Material.BLAZE_ROD);
        if (first < 0) {
            return;
        }
        ItemStack stack = inv.getItem(first);
        if (stack == null) {
            return;
        }
        ItemMeta meta = stack.getItemMeta();
        if (meta.hasDisplayName() && meta.getDisplayName().endsWith("Sonic Screwdriver")) {
            meta.setItemModel(model);
            stack.setItemMeta(meta);
            if (stack.containsEnchantment(Enchantment.UNBREAKING)) {
                stack.getEnchantments().keySet().forEach(stack::removeEnchantment);
            }
        }
    }
}
