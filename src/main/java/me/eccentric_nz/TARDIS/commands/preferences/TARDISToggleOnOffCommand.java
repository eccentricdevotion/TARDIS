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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISToggleOnOffCommand {

    private final TARDIS plugin;
    private final List<String> was;

    TARDISToggleOnOffCommand(TARDIS plugin) {
        this.plugin = plugin;
        was = Arrays.asList("announce_repeaters", "auto", "auto_powerup", "auto_siege", "beacon", "build", "dnd", "eps", "farm", "hads", "info", "lock_containers", "minecart", "renderer", "submarine", "travelbar", "telepathy");
    }

    public boolean toggle(Player player, String[] args) {
        String pref = args[0];
        if (pref.equals("auto") && !plugin.getConfig().getBoolean("allow.autonomous")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "AUTO_DISABLED");
            return true;
        }
        if (pref.equals("auto_powerup") && !plugin.getConfig().getBoolean("allow.power_down")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN_DISABLED");
            return true;
        }
        if (pref.equals("eps") && !plugin.getConfig().getBoolean("allow.emergency_npc")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "EP1_DISABLED");
            return true;
        }
        if (pref.equals("hads") && !plugin.getConfig().getBoolean("allow.hads")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "HADS_DISBALED");
            return true;
        }
        if (pref.equals("lock_containers") && !plugin.isWorldGuardOnServer()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "WG_DISABLED");
            return true;
        }
        if (pref.equals("lock_containers") && !plugin.getUtils().inTARDISWorld(player)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_IN_WORLD");
            return true;
        }
        HashMap<String, Object> setp = new HashMap<>();
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", player.getUniqueId().toString());
        if (args[1].equalsIgnoreCase("on")) {
            if (args[0].equalsIgnoreCase("lock_containers")) {
                plugin.getWorldGuardUtils().lockContainers(player.getWorld(), player.getName());
            } else {
                setp.put(pref + "_on", 1);
                if (pref.equals("beacon")) {
                    UUID uuid = player.getUniqueId();
                    // get tardis id
                    ResultSetTardisID rsi = new ResultSetTardisID(plugin);
                    if (rsi.fromUUID(uuid.toString())) {
                        new TARDISBeaconToggler(plugin).flickSwitch(uuid, rsi.getTardis_id(), true);
                    }
                }
            }
            String grammar = (was.contains(pref)) ? "PREF_WAS_ON" : "PREF_WERE_ON";
            plugin.getMessenger().send(player, TardisModule.TARDIS, grammar, pref);
        }
        if (args[1].equalsIgnoreCase("off")) {
            if (args[0].equalsIgnoreCase("lock_containers")) {
                plugin.getWorldGuardUtils().unlockContainers(player.getWorld(), player.getName());
            } else {
                setp.put(pref + "_on", 0);
                if (pref.equals("beacon")) {
                    UUID uuid = player.getUniqueId();
                    // get tardis id
                    ResultSetTardisID rsi = new ResultSetTardisID(plugin);
                    if (rsi.fromUUID(uuid.toString())) {
                        new TARDISBeaconToggler(plugin).flickSwitch(uuid, rsi.getTardis_id(), false);
                    }
                }
            }
            String grammar = (was.contains(pref)) ? "PREF_WAS_OFF" : "PREF_WERE_OFF";
            plugin.getMessenger().send(player, TardisModule.TARDIS, grammar, pref);
        }
        if (!setp.isEmpty()) {
            plugin.getQueryFactory().doUpdate("player_prefs", setp, wherep);
        }
        return true;
    }
}
