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
package me.eccentric_nz.tardisregeneration;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.UseCooldown;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import io.papermc.paper.registry.keys.SoundEventKeys;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
        ItemStack goblet = ItemStack.of(Material.GOLD_INGOT);
        // set use components
        goblet.setData(DataComponentTypes.USE_COOLDOWN, UseCooldown.useCooldown(1.0f).build());
        // set item model
        goblet.setData(DataComponentTypes.ITEM_MODEL, Whoniverse.ELIXIR_OF_LIFE.getKey());
        // set text components
        goblet.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Elixir of Life"));
        goblet.setData(DataComponentTypes.LORE, ItemLore.lore()
                .addLine(Component.text("Use to trigger a"))
                .addLine(Component.text("Time Lord regeneration"))
                .build());
        // set food component
        FoodProperties foodComponent = FoodProperties.food()
                .canAlwaysEat(true)
                .nutrition(4)
                .saturation(1.0f)
                .build();
        goblet.setData(DataComponentTypes.FOOD, foodComponent);
        // set consumable component
        Consumable consumable = Consumable.consumable()
                .animation(ItemUseAnimation.DRINK)
                .sound(SoundEventKeys.ENTITY_GENERIC_DRINK)
                .consumeSeconds(1.6f)
                .hasConsumeParticles(false)
                .build();
        goblet.setData(DataComponentTypes.CONSUMABLE, consumable);
        return goblet;
    }

    public static boolean is(ItemStack is) {
        if (is == null || is.getType() != Material.GOLD_INGOT) {
            return false;
        }
        return ComponentUtils.isNamed(is, "Elixir of Life");
    }
}
