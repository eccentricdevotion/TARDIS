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
package me.eccentric_nz.tardisweepingangels.monsters.judoon;

import me.eccentric_nz.TARDIS.custommodels.keys.JudoonVariant;
import me.eccentric_nz.tardisweepingangels.equip.ArmourEquipper;
import me.eccentric_nz.tardisweepingangels.equip.FollowerEquipper;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JudoonEquipment {

    public static void set(OfflinePlayer player, LivingEntity entity, boolean disguise) {
        new ArmourEquipper().dress(entity, Monster.JUDOON);
        if (!disguise) {
            // weapon
            ItemStack hand = ItemStack.of(Material.END_ROD);
            ItemMeta tim = hand.getItemMeta();
            tim.setItemModel(JudoonVariant.JUDOON_WEAPON_RESTING.getKey());
            hand.setItemMeta(tim);
            entity.getEquipment().setItemInMainHand(hand);
            new FollowerEquipper().setOptionsAndInvisibilty(player, entity, Monster.JUDOON);
            // set entity scale
            entity.getAttribute(Attribute.SCALE).setBaseValue(1.25d);
        } else {
            PotionEffect potionEffect = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false);
            entity.addPotionEffect(potionEffect);
        }
    }
}
