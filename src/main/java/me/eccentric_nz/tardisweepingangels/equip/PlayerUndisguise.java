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
package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
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
                        if (!im.getPersistentDataContainer().has(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER)) {
                            if (im.hasCustomName() && (
                                    ComponentUtils.startsWith(im.customName(), "Weeping Angel")
                                    || ComponentUtils.startsWith(im.customName(), "Angel Of Liberty")
                                    || ComponentUtils.startsWith(im.customName(), "Clockwork Droid")
                                    || ComponentUtils.startsWith(im.customName(), "Cyberman")
                                    || ComponentUtils.startsWith(im.customName(), "Cybershade")
                                    || ComponentUtils.startsWith(im.customName(), "Dalek Sec")
                                    || ComponentUtils.startsWith(im.customName(), "Dalek")
                                    || ComponentUtils.startsWith(im.customName(), "Davros")
                                    || ComponentUtils.startsWith(im.customName(), "Empty Child")
                                    || ComponentUtils.startsWith(im.customName(), "Hath")
                                    || ComponentUtils.startsWith(im.customName(), "Headless Monk")
                                    || ComponentUtils.startsWith(im.customName(), "Ice Warrior")
                                    || ComponentUtils.startsWith(im.customName(), "Judoon")
                                    || ComponentUtils.startsWith(im.customName(), "K9")
                                    || ComponentUtils.startsWith(im.customName(), "Mire")
                                    || ComponentUtils.startsWith(im.customName(), "Omega")
                                    || ComponentUtils.startsWith(im.customName(), "Ood")
                                    || ComponentUtils.startsWith(im.customName(), "Ossified")
                                    || ComponentUtils.startsWith(im.customName(), "Racnoss")
                                    || ComponentUtils.startsWith(im.customName(), "Scarecrow")
                                    || ComponentUtils.startsWith(im.customName(), "Sea Devil")
                                    || ComponentUtils.startsWith(im.customName(), "Silent")
                                    || ComponentUtils.startsWith(im.customName(), "Silurian")
                                    || ComponentUtils.startsWith(im.customName(), "Slitheen")
                                    || ComponentUtils.startsWith(im.customName(), "Smiler")
                                    || ComponentUtils.startsWith(im.customName(), "Sontaran")
                                    || ComponentUtils.startsWith(im.customName(), "Strax")
                                    || ComponentUtils.startsWith(im.customName(), "Sutekh")
                                    || ComponentUtils.startsWith(im.customName(), "Sycorax")
                                    || ComponentUtils.startsWith(im.customName(), "The Beast")
                                    || ComponentUtils.startsWith(im.customName(), "Toclafane")
                                    || ComponentUtils.startsWith(im.customName(), "Vampire")
                                    || ComponentUtils.startsWith(im.customName(), "Vashta")
                                    || ComponentUtils.startsWith(im.customName(), "Zygon")
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
