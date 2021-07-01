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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.flight.TARDISHandbrake;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class TARDISHandbrakeCommand {

    private final TARDIS plugin;

    public TARDISHandbrakeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggle(Player player, int id, String[] args, boolean admin) {
        if (args.length < 2) {
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return true;
        }
        String tf = args[1];
        if (!admin && !tf.equalsIgnoreCase("on") && !tf.equalsIgnoreCase("off")) {
            TARDISMessage.send(player, "PREF_ON_OFF", "the handbrake");
            return true;
        }
        // actually toggle the lever block
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("type", 0);
        whereh.put("tardis_id", id);
        ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
        if (rsc.resultSet()) {
            boolean bool = tf.equalsIgnoreCase("on");
            int onoff = (bool) ? 1 : 0;
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            HashMap<String, Object> set = new HashMap<>();
            set.put("handbrake_on", onoff);
            plugin.getQueryFactory().doUpdate("tardis", set, where);
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
            TARDISSounds.playTARDISSound(location, "tardis_handbrake_engage");
            // Changes the lever to on
            TARDISHandbrake.setLevers(location.getBlock(), true, true, location.toString(), id, plugin);
            if (bool) {
                plugin.getTrackerKeeper().getDestinationVortex().remove(id);
                plugin.getTrackerKeeper().getInVortex().remove(id);
                plugin.getTrackerKeeper().getMaterialising().remove(id);
                plugin.getTrackerKeeper().getDidDematToVortex().remove(id);
            }
            if (!admin) {
                TARDISMessage.send(player, "HANDBRAKE_ON_OFF", args[1].toUpperCase(Locale.ENGLISH));
            }
        }
        return true;
    }
}
