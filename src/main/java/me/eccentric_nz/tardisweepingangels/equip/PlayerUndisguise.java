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

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerUndisguise implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onManualUndisguise(InventoryClickEvent event) {
        if (event.getSlotType().equals(SlotType.ARMOR)) {
            int slot = event.getRawSlot();
            if (slot > 4 && slot < 9) {
                ItemStack is = event.getCurrentItem();
                if (is != null
                        && !is.getPersistentDataContainer().has(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER)
                        && is.hasData(DataComponentTypes.CUSTOM_NAME)) {
                    Component component = is.getData(DataComponentTypes.CUSTOM_NAME);
                    if (ComponentUtils.startsWith(component, "Weeping Angel")
                            || ComponentUtils.startsWith(component, "Angel Of Liberty")
                            || ComponentUtils.startsWith(component, "Clockwork Droid")
                            || ComponentUtils.startsWith(component, "Cyberman")
                            || ComponentUtils.startsWith(component, "Cybershade")
                            || ComponentUtils.startsWith(component, "Dalek Sec")
                            || ComponentUtils.startsWith(component, "Dalek")
                            || ComponentUtils.startsWith(component, "Davros")
                            || ComponentUtils.startsWith(component, "Empty Child")
                            || ComponentUtils.startsWith(component, "Hath")
                            || ComponentUtils.startsWith(component, "Headless Monk")
                            || ComponentUtils.startsWith(component, "Ice Warrior")
                            || ComponentUtils.startsWith(component, "Judoon")
                            || ComponentUtils.startsWith(component, "K9")
                            || ComponentUtils.startsWith(component, "Mire")
                            || ComponentUtils.startsWith(component, "Omega")
                            || ComponentUtils.startsWith(component, "Ood")
                            || ComponentUtils.startsWith(component, "Ossified")
                            || ComponentUtils.startsWith(component, "Racnoss")
                            || ComponentUtils.startsWith(component, "Scarecrow")
                            || ComponentUtils.startsWith(component, "Sea Devil")
                            || ComponentUtils.startsWith(component, "Silent")
                            || ComponentUtils.startsWith(component, "Silurian")
                            || ComponentUtils.startsWith(component, "Slitheen")
                            || ComponentUtils.startsWith(component, "Smiler")
                            || ComponentUtils.startsWith(component, "Sontaran")
                            || ComponentUtils.startsWith(component, "Strax")
                            || ComponentUtils.startsWith(component, "Sutekh")
                            || ComponentUtils.startsWith(component, "Sycorax")
                            || ComponentUtils.startsWith(component, "The Beast")
                            || ComponentUtils.startsWith(component, "Toclafane")
                            || ComponentUtils.startsWith(component, "Vampire")
                            || ComponentUtils.startsWith(component, "Vashta")
                            || ComponentUtils.startsWith(component, "Zygon")) {
                        event.setCancelled(true);
                        TARDIS.plugin.getMessenger().send(event.getWhoClicked(), TardisModule.MONSTERS, "WA_OFF");
                    }
                }
            }
        }
    }
}

