/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * While phoning Amy and Rory about his adventures without them, the Eleventh
 * Doctor mentioned how he should be returning to them "any day", but the
 * TARDIS' helmic regulator was playing up.
 *
 * @author eccentric_nz
 */
public class TARDISRegulatorStarter implements Runnable {

    private final TARDIS plugin;
    private final Player player;

    public TARDISRegulatorStarter(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        TARDISRegulatorInventory reg = new TARDISRegulatorInventory();
        ItemStack[] items = reg.getRegulator();
        Inventory inv = plugin.getServer().createInventory(player, 54, "Helmic Regulator");
        inv.setContents(items);
        player.openInventory(inv);
        // play inflight sound
        TARDISSounds.playTARDISSound(player.getLocation(), "interior_flight");
    }
}
