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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.custommodels.keys.DalekVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.EmptyChildVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.K9Variant;
import me.eccentric_nz.TARDIS.custommodels.keys.VampireOfVeniceVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.persistence.PersistentDataType;

public class HeadBuilder {

    public static ItemStack getItemStack(Monster monster) {
        if (monster == Monster.K9 || monster == Monster.TOCLAFANE) {
            return null;
        }
        Material material = monster.getMaterial();
        NamespacedKey model = monster.getHeadModel();
        // special case for Saturnynian
        if (monster == Monster.SATURNYNIAN) {
            model = VampireOfVeniceVariant.SATURNYNIAN_HEAD.getKey();
        }
        if (material == null || model == null) {
            return null;
        }
        ItemStack is = ItemStack.of(material, 1);
        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER, 99);
        String head = switch (monster) {
            case HEADLESS_MONK -> "Headless Monk Hood";
            case MIRE -> "Mire Helmet";
            default -> monster.getName() + " Head";
        };
        im.displayName(ComponentUtils.toWhite(head));
        im.setItemModel(model);
        EquippableComponent component = im.getEquippable();
        component.setSlot(EquipmentSlot.HEAD);
        // add overlays
        if (monster.equals(Monster.DALEK)) {
            component.setCameraOverlay(DalekVariant.DALEK_OVERLAY.getKey());
        }
        if (monster.equals(Monster.EMPTY_CHILD)) {
            component.setCameraOverlay(EmptyChildVariant.EMPTY_CHILD_OVERLAY.getKey());
        }
        im.setEquippable(component);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getK9() {
        ItemStack is = ItemStack.of(Material.BONE);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("K9"));
        im.setItemModel(K9Variant.K9.getKey());
        is.setItemMeta(im);
        return is;
    }
}
