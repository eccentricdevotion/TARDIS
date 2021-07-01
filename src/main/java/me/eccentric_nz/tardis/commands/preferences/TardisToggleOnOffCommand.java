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
package me.eccentric_nz.tardis.commands.preferences;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.artron.TardisBeaconToggler;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TardisToggleOnOffCommand {

    private final TardisPlugin plugin;
    private final List<String> was;


    TardisToggleOnOffCommand(TardisPlugin plugin) {
        this.plugin = plugin;
        was = Arrays.asList("auto", "auto_powerup", "auto_siege", "beacon", "build", "difficulty", "dnd", "eps", "farm", "hads", "lock_containers", "minecart", "renderer", "submarine", "travelbar", "telepathy");
    }

    public boolean toggle(Player player, String[] args) {
        String pref = args[0];
        if (pref.equals("auto") && !plugin.getConfig().getBoolean("allow.autonomous")) {
            TardisMessage.send(player, "AUTO_DISABLED");
            return true;
        }
        if (pref.equals("auto_powerup") && !plugin.getConfig().getBoolean("allow.power_down")) {
            TardisMessage.send(player, "POWER_DOWN_DISABLED");
            return true;
        }
        if (pref.equals("eps") && !plugin.getConfig().getBoolean("allow.emergency_npc")) {
            TardisMessage.send(player, "EP1_DISABLED");
            return true;
        }
        if (pref.equals("hads") && !plugin.getConfig().getBoolean("allow.hads")) {
            TardisMessage.send(player, "HADS_DISBALED");
            return true;
        }
        if (pref.equals("lock_containers") && !plugin.isWorldGuardOnServer()) {
            TardisMessage.send(player, "WG_DISABLED");
            return true;
        }
        if (pref.equals("lock_containers") && !plugin.getUtils().inTardisWorld(player)) {
            TardisMessage.send(player, "CMD_IN_WORLD");
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
                    // get TARDIS id
                    ResultSetTardisId rsi = new ResultSetTardisId(plugin);
                    if (rsi.fromUuid(uuid.toString())) {
                        new TardisBeaconToggler(plugin).flickSwitch(uuid, rsi.getTardisId(), true);
                    }
                }
            }
            String grammar = (was.contains(pref)) ? "PREF_WAS_ON" : "PREF_WERE_ON";
            TardisMessage.send(player, grammar, pref);
        }
        if (args[1].equalsIgnoreCase("off")) {
            if (args[0].equalsIgnoreCase("lock_containers")) {
                plugin.getWorldGuardUtils().unlockContainers(player.getWorld(), player.getName());
            } else {
                setp.put(pref + "_on", 0);
                if (pref.equals("beacon")) {
                    UUID uuid = player.getUniqueId();
                    // get TARDIS id
                    ResultSetTardisId rsi = new ResultSetTardisId(plugin);
                    if (rsi.fromUuid(uuid.toString())) {
                        new TardisBeaconToggler(plugin).flickSwitch(uuid, rsi.getTardisId(), false);
                    }
                }
            }
            String grammar = (was.contains(pref)) ? "PREF_WAS_OFF" : "PREF_WERE_OFF";
            TardisMessage.send(player, grammar, pref);
        }
        if (setp.size() > 0) {
            plugin.getQueryFactory().doUpdate("player_prefs", setp, wherep);
        }
        return true;
    }
}
