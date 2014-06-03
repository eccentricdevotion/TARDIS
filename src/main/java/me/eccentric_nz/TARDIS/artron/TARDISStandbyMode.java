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
package me.eccentric_nz.TARDIS.artron;

import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetStandby;
import me.eccentric_nz.TARDIS.database.ResultSetStandby.StandbyData;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author eccentric_nz
 */
public class TARDISStandbyMode implements Runnable {

    private final TARDIS plugin;
    private final int amount;

    public TARDISStandbyMode(TARDIS plugin) {
        this.plugin = plugin;
        this.amount = this.plugin.getArtronConfig().getInt("standby");
    }

    @Override
    public void run() {
        // get TARDISes that are powered on
        HashMap<Integer, StandbyData> ids = new ResultSetStandby(plugin).onStandby();
        QueryFactory qf = new QueryFactory(plugin);
        for (final Map.Entry<Integer, StandbyData> map : ids.entrySet()) {
            final int id = map.getKey();
            // not while travelling and only until they hit zero
            if (!isTravelling(id)) {
                // remove some energy
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", id);
                qf.alterEnergyLevel("tardis", -amount, where, null);
            } else if (map.getValue().getLevel() <= amount) {
                // power down!
                HashMap<String, Object> wherep = new HashMap<String, Object>();
                wherep.put("tardis_id", id);
                HashMap<String, Object> setp = new HashMap<String, Object>();
                setp.put("powered_on", 0);
                OfflinePlayer player = plugin.getServer().getOfflinePlayer(map.getValue().getUuid());
                if (player.isOnline()) {
                    plugin.getUtils().playTARDISSound(player.getPlayer().getLocation(), player.getPlayer(), "power_down");
                    TARDISMessage.send(player.getPlayer(), "POWER_OFF_AUTO");
                }
                long delay = 0;
                // if hidden, rebuild
                if (map.getValue().isHidden()) {
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisremote " + player.getName() + " rebuild");
                    if (player.isOnline()) {
                        TARDISMessage.send(player.getPlayer(), "POWER_FAIL");
                    }
                    delay = 20L;
                }
                // police box lamp, delay it incase the TARDIS needs rebuilding
                if (map.getValue().getPreset().equals(PRESET.NEW) || map.getValue().getPreset().equals(PRESET.OLD)) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            new TARDISPoliceBoxLampToggler(plugin).toggleLamp(id, false);
                        }
                    }, delay);
                }
                // if lights are on, turn them off
                if (map.getValue().isLights()) {
                    new TARDISLampToggler(plugin).flickSwitch(id, map.getValue().getUuid(), true);
                }
                // if beacon is on turn it off
                new TARDISBeaconToggler(plugin).flickSwitch(map.getValue().getUuid().toString(), false);
                // update database
                qf.doUpdate("tardis", setp, wherep);
            }
        }
    }

    private boolean isTravelling(int id) {
        return (plugin.getTrackerKeeper().getDematerialising().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getInVortex().contains(id));
    }
}
