/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.flight;

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * While phoning Amy and Rory about his adventures without them, the Eleventh Doctor mentioned how he should be
 * returning to them "any day", but the tardis' helmic regulator was playing up.
 *
 * @author eccentric_nz
 */
class TardisRegulatorStarter implements Runnable {

    private final TardisPlugin plugin;
    private final Player player;
    private final int id;

    TardisRegulatorStarter(TardisPlugin plugin, Player player, int id) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
    }

    @Override
    public void run() {
        TardisRegulatorInventory reg = new TardisRegulatorInventory();
        ItemStack[] items = reg.getRegulator();
        Inventory inv = plugin.getServer().createInventory(player, 54, "Helmic Regulator");
        inv.setContents(items);
        player.openInventory(inv);
        // play inflight sound
        if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            plugin.getServer().getScheduler().runTask(plugin, new TardisLoopingFlightSound(plugin, player.getLocation(), id));
        }
    }
}
