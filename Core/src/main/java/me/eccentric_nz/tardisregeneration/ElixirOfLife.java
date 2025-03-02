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
package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.UseCooldownComponent;

import java.util.List;

/**
 * With special preparation, a variant of the Elixir of Life can trigger a Time Lord's regeneration, if they are injured
 * too severely to regenerate normally. This variant can also influence both the physical and personality traits of the
 * new incarnation. After the Eighth Doctor died in a spaceship crash on Karn during the Time War, the Sisterhood
 * revived him long enough for him to consume one of a number of prepared Elixir that would enable him to select his
 * personality, appearance, and gender of his next incarnation; he ultimately selected one that would return to him to
 * life as a warrior.
 */
public class ElixirOfLife {

    public static ItemStack create() {
        ItemStack goblet = new ItemStack(Material.GOLD_INGOT);
        ItemMeta im = goblet.getItemMeta();
        im.setUseRemainder(null);
        UseCooldownComponent cooldown = im.getUseCooldown();
        cooldown.setCooldownSeconds(1.0f);
        im.setUseCooldown(cooldown);
        FoodComponent foodComponent = im.getFood();
        foodComponent.setCanAlwaysEat(true);
        foodComponent.setNutrition(4);
        foodComponent.setSaturation(1.0f);
        im.setFood(foodComponent);
        im.setItemModel(Whoniverse.ELIXIR_OF_LIFE.getKey());
        im.setDisplayName(ChatColor.WHITE + "Elixir of Life");
        im.setLore(List.of("Use to trigger a", "Time Lord regeneration"));
        goblet.setItemMeta(im);
        return TARDIS.plugin.getComponentSetter().setConsumable(goblet);
    }

    public static boolean is(ItemStack is) {
        if (is == null || is.getType() != Material.GOLD_INGOT || !is.hasItemMeta()) {
            return false;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.hasItemModel()) {
            return false;
        }
        return im.getDisplayName().endsWith("Elixir of Life");
    }
}
