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
package me.eccentric_nz.TARDIS.utility;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Soon after taking Amy Pond on board for the first time, the TARDIS console provided the Doctor with a new sonic
 * screwdriver, as the previous one had been destroyed.
 *
 * @author eccentric_nz
 */
public class TARDISItemRenamer {

    private final TARDIS plugin;
    private final Player player;
    private final ItemStack itemStack;

    public TARDISItemRenamer(TARDIS plugin, Player player, ItemStack itemStack) {
        this.plugin = plugin;
        this.player = player;
        this.itemStack = itemStack;
    }

    /**
     * Sets the name of the held item to the specified string. Also adds some lore.
     *
     * @param name    the name to give the item
     * @param setlore whether to set lore on the item
     */
    public void setName(String name, boolean setlore) {
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, Component.text(name));
        if (setlore) {
            ItemLore.Builder lore = ItemLore.lore();
            lore.addLine(Component.text("Enter and exit your TARDIS"));
            lore.addLine(Component.text("This key belongs to", NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
            lore.addLine(Component.text(player.getName(), NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
            itemStack.setData(DataComponentTypes.LORE, lore.build());
            itemStack.editPersistentDataContainer(pdc -> pdc.set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId()));
        }
    }
}
