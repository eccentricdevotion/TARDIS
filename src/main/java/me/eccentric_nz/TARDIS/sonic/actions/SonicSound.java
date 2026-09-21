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
package me.eccentric_nz.TARDIS.sonic.actions;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.enumeration.SonicScrewdriver;
import me.eccentric_nz.TARDIS.recipes.shaped.SonicScrewdriverRecipe;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class SonicSound {

    private static final HashMap<UUID, Long> timeout = new HashMap<>();

    public static void playSonicSound(TARDIS plugin, Player player, long now, long cooldown, String sound) {
        if ((!timeout.containsKey(player.getUniqueId()) || timeout.get(player.getUniqueId()) < now)) {
            ItemStack sonic = player.getInventory().getItemInMainHand();
            // change model to 'on/open', then after scheduled time change back to 'off/closed' model
            CustomModelData component = sonic.getData(DataComponentTypes.CUSTOM_MODEL_DATA);
            float f;
            try {
                f = component.floats().getFirst();
            } catch (NoSuchElementException e) {
                List<Float> sonicModel = SonicScrewdriverRecipe.sonicModelLookup.getOrDefault(plugin.getConfig().getString("sonic.default_model").toLowerCase(Locale.ROOT), SonicVariant.ELEVENTH.getFloats());
                f = sonicModel.getFirst();
            }
            SonicScrewdriver screwdriver = SonicScrewdriver.getByFloat(f);
            sonic.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                    .addFloats(screwdriver.getActive())
                    .build());
            timeout.put(player.getUniqueId(), now + cooldown);
            TARDISSounds.playTARDISSound(player.getLocation(), sound);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ItemStack is = player.getInventory().getItemInMainHand();
                if (ComponentUtils.isNamed(is, "Sonic Screwdriver")) {
                    player.getInventory().getItemInMainHand().getEnchantments().keySet().forEach((e) -> player.getInventory().getItemInMainHand().removeEnchantment(e));
                    is.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
                    is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                            .addFloats(screwdriver.getModel())
                            .build());
                } else {
                    // find the screwdriver in the player's inventory
                    revertSonic(player.getInventory(), screwdriver.getModel());
                }
            }, (cooldown / 50L));
        }
    }

    private static void revertSonic(PlayerInventory inv, List<Float> model) {
        int first = inv.first(Material.BLAZE_ROD);
        if (first < 0) {
            return;
        }
        ItemStack stack = inv.getItem(first);
        if (stack == null) {
            return;
        }
        if (ComponentUtils.isNamed(stack, "Sonic Screwdriver")) {
            stack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                    .addFloats(model)
                    .build());
            stack.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
            if (stack.containsEnchantment(Enchantment.UNBREAKING)) {
                stack.getEnchantments().keySet().forEach(stack::removeEnchantment);
            }
        }
    }
}
