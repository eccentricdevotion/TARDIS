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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISZeroRoomEnterEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISZeroRoomExitEvent;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.TARDISExteriorRenderer;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ZeroRoomAction {

    private final TARDIS plugin;

    public ZeroRoomAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void doEntry(Player player, Tardis tardis, int id) {
        // enter zero room
        int zero_amount = plugin.getArtronConfig().getInt("zero");
        if (tardis.getArtronLevel() < zero_amount) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ZERO_ENERGY");
            return;
        }
        Location zero = TARDISStaticLocationGetters.getLocationFromDB(tardis.getZero());
        if (zero != null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ZERO_READY");
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                new TARDISExteriorRenderer(plugin).transmat(player, COMPASS.SOUTH, zero);
                plugin.getPM().callEvent(new TARDISZeroRoomEnterEvent(player, id));
            }, 20L);
            plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
            HashMap<String, Object> wherez = new HashMap<>();
            wherez.put("tardis_id", id);
            plugin.getQueryFactory().alterEnergyLevel("tardis", -zero_amount, wherez, player);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_ZERO");
        }
    }

    public void doExit(Player player, int id) {
        // exit zero room
        plugin.getTrackerKeeper().getZeroRoomOccupants().remove(player.getUniqueId());
        plugin.getGeneralKeeper().getRendererListener().transmat(player);
        plugin.getPM().callEvent(new TARDISZeroRoomExitEvent(player, id));
    }
}
