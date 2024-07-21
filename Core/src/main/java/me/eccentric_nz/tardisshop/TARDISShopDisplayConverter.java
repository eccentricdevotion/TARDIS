/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.tardisshop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisshop.database.ResultSetUpdateShop;
import org.bukkit.Chunk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

/**
 *
 * @author eccentric_nz
 */
public class TARDISShopDisplayConverter implements Runnable {

    private final TARDIS plugin;
    private int count = 0;

    public TARDISShopDisplayConverter(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (findAndReplace()) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.SHOP, "Converted " + count + " shop display items");
        } else {
            String message = (count > 0) ? "Converting shop display items failed!" : "There were no shop display items to convert";
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.SHOP, message);
        }
    }

    public boolean findAndReplace() {
        ResultSetUpdateShop rs = new ResultSetUpdateShop(plugin);
        if (rs.getAll()) {
            for (TARDISShopItem si : rs.getShopItems()) {
                Chunk chunk = si.getLocation().getChunk();
                while (!chunk.isLoaded()) {
                    chunk.load();
                }
                // remove the current dropped item and armour stand
                for (Entity e : si.getLocation().getWorld().getNearbyEntities(si.getLocation(), 1.0d, 2.0d, 1.0d)) {
                    if (e instanceof Item || e instanceof ArmorStand) {
                        e.remove();
                    }
                }
                // add new display item
                new TARDISShopItemSpawner(plugin).setItem(si.getLocation(), si);
                count++;
            }
            return true;
        }
        return false;
    }
}
