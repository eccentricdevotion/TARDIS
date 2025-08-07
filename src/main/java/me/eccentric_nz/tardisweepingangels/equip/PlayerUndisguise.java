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
package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerUndisguise implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onManualUndisguise(InventoryClickEvent event) {
        if (event.getSlotType().equals(SlotType.ARMOR)) {
            int slot = event.getRawSlot();
            if (slot > 4 && slot < 9) {
                ItemStack is = event.getCurrentItem();
                if (is != null) {
                    if (is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (!im.getPersistentDataContainer().has(TARDISWeepingAngels.MONSTER_HEAD, PersistentDataType.INTEGER)) {
                            if (im.hasDisplayName() && (
                                    ComponentUtils.startsWith(im.displayName(), "Weeping Angel")
                                    || ComponentUtils.startsWith(im.displayName(), "Angel Of Liberty")
                                    || ComponentUtils.startsWith(im.displayName(), "Clockwork Droid")
                                    || ComponentUtils.startsWith(im.displayName(), "Cyberman")
                                    || ComponentUtils.startsWith(im.displayName(), "Cybershade")
                                    || ComponentUtils.startsWith(im.displayName(), "Dalek Sec")
                                    || ComponentUtils.startsWith(im.displayName(), "Dalek")
                                    || ComponentUtils.startsWith(im.displayName(), "Davros")
                                    || ComponentUtils.startsWith(im.displayName(), "Empty Child")
                                    || ComponentUtils.startsWith(im.displayName(), "Hath")
                                    || ComponentUtils.startsWith(im.displayName(), "Headless Monk")
                                    || ComponentUtils.startsWith(im.displayName(), "Ice Warrior")
                                    || ComponentUtils.startsWith(im.displayName(), "Judoon")
                                    || ComponentUtils.startsWith(im.displayName(), "K9")
                                    || ComponentUtils.startsWith(im.displayName(), "Mire")
                                    || ComponentUtils.startsWith(im.displayName(), "Omega")
                                    || ComponentUtils.startsWith(im.displayName(), "Ood")
                                    || ComponentUtils.startsWith(im.displayName(), "Ossified")
                                    || ComponentUtils.startsWith(im.displayName(), "Racnoss")
                                    || ComponentUtils.startsWith(im.displayName(), "Scarecrow")
                                    || ComponentUtils.startsWith(im.displayName(), "Sea Devil")
                                    || ComponentUtils.startsWith(im.displayName(), "Silent")
                                    || ComponentUtils.startsWith(im.displayName(), "Silurian")
                                    || ComponentUtils.startsWith(im.displayName(), "Slitheen")
                                    || ComponentUtils.startsWith(im.displayName(), "Smiler")
                                    || ComponentUtils.startsWith(im.displayName(), "Sontaran")
                                    || ComponentUtils.startsWith(im.displayName(), "Strax")
                                    || ComponentUtils.startsWith(im.displayName(), "Sutekh")
                                    || ComponentUtils.startsWith(im.displayName(), "Sycorax")
                                    || ComponentUtils.startsWith(im.displayName(), "The Beast")
                                    || ComponentUtils.startsWith(im.displayName(), "Toclafane")
                                    || ComponentUtils.startsWith(im.displayName(), "Vampire")
                                    || ComponentUtils.startsWith(im.displayName(), "Vashta")
                                    || ComponentUtils.startsWith(im.displayName(), "Zygon")
                            )) {
                                event.setCancelled(true);
                                TARDIS.plugin.getMessenger().send(event.getWhoClicked(), TardisModule.MONSTERS, "WA_OFF");
                            }
                        }
                    }
                }
            }
        }
    }
}
