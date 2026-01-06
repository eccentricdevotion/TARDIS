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
package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TelepathicStructureListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TelepathicStructureListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onStructureMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TelepathicStructure)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                plugin.debug("TelepathicStructureListener");
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        if (slot == 53) {
            close(player);
        } else {
            ItemStack choice = event.getView().getItem(slot);
            if (choice != null) {
                // get TARDIS id
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                if (!rst.fromUUID(player.getUniqueId().toString())) {
                    return;
                }
                // get tardis artron level
                ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
                if (!rs.fromID(rst.getTardisId())) {
                    return;
                }
                int travel = plugin.getArtronConfig().getInt("travel");
                if (rs.getArtronLevel() < travel) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
                    return;
                }
                // get the structure
                ItemMeta im = choice.getItemMeta();
                String enumStr = ComponentUtils.toEnumUppercase(im.displayName());
                player.performCommand("tardistravel structure " + enumStr);
                close(player);
            }
        }
    }
}
