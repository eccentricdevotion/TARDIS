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
package me.eccentric_nz.TARDIS.commands.give;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemRegistry;
import me.eccentric_nz.TARDIS.custommodels.keys.BoneDoorVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ClassicDoorVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SidratDoorVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.TardisDoorVariant;
import me.eccentric_nz.TARDIS.doors.Door;
import me.eccentric_nz.TARDIS.rotors.Rotor;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class TARDISDisplayBlockCommand {

    private final TARDIS plugin;

    public TARDISDisplayBlockCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public ItemStack getStack(String arg) {
        String display = TARDISStringUtils.toEnumUppercase(arg);
        if (display.startsWith("DOOR") || display.endsWith("_DOOR")) {
            Door door = Door.byName.get(display);
            ItemStack is = ItemStack.of(door.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(ComponentUtils.toWhite("Door " + TARDISStringUtils.capitalise(door.getName())));
            NamespacedKey key = switch (door.getMaterial()) {
                case IRON_DOOR -> TardisDoorVariant.TARDIS_DOOR_CLOSED.getKey();
                case BIRCH_DOOR -> BoneDoorVariant.BONE_DOOR_CLOSED.getKey();
                case CHERRY_DOOR -> ClassicDoorVariant.CLASSIC_DOOR_CLOSED.getKey();
                case PALE_OAK_DOOR -> SidratDoorVariant.SIDRAT_DOOR_CLOSED.getKey();
                default -> Door.getClosedModel(door.getMaterial());
            };
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, key.getKey());
            im.setItemModel(key);
            is.setItemMeta(im);
            return is;
        } else if (display.startsWith("TIME_")) {
            Rotor rotor = Rotor.byName.get(display);
            ItemStack is = ItemStack.of(Material.LIGHT_GRAY_DYE, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(ComponentUtils.toWhite("Time Rotor " + rotor.name()));
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, rotor.offModel().getKey());
            im.setItemModel(rotor.offModel());
            is.setItemMeta(im);
            return is;
        } else {
            try {
                TARDISDisplayItem tdi = TARDISDisplayItemRegistry.valueOf(display);
                ItemStack is = ItemStack.of(tdi.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(ComponentUtils.toWhite(tdi.getDisplayName()));
                if (tdi.getCustomModel() != null) {
                    im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
                }
                is.setItemMeta(im);
                return is;
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
