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
package me.eccentric_nz.tardisweepingangels.utils;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Equippable;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.DalekVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.EmptyChildVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.K9Variant;
import me.eccentric_nz.TARDIS.custommodels.keys.VampireOfVeniceVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
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
        is.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
        String head = switch (monster) {
            case HEADLESS_MONK -> "Headless Monk Hood";
            case MIRE -> "Mire Helmet";
            default -> monster.getName() + " Head";
        };
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(head));
        is.setData(DataComponentTypes.ITEM_MODEL, model);
        Equippable.Builder equippable = Equippable.equippable(EquipmentSlot.HEAD);
        // add overlays
        if (monster.equals(Monster.DALEK)) {
            equippable.cameraOverlay(DalekVariant.DALEK_OVERLAY.getKey());
        }
        if (monster.equals(Monster.EMPTY_CHILD)) {
            equippable.cameraOverlay(EmptyChildVariant.EMPTY_CHILD_OVERLAY.getKey());
        }
        is.setData(DataComponentTypes.EQUIPPABLE, equippable.build());
        return is;
    }

    public static ItemStack getK9() {
        ItemStack is = ItemStack.of(Material.BONE);
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("K9"));
        is.setData(DataComponentTypes.ITEM_MODEL, K9Variant.K9.getKey());
        return is;
    }
}
