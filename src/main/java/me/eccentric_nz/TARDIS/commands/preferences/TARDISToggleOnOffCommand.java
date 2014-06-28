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
package me.eccentric_nz.TARDIS.commands.preferences;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISToggleOnOffCommand {

    private final TARDIS plugin;
    private final List<String> was;

    public TARDISToggleOnOffCommand(TARDIS plugin) {
        this.plugin = plugin;
        this.was = Arrays.asList("auto", "beacon", "ctm", "dnd", "eps", "hads", "minecart", "plain", "renderer", "submarine", "travelbar");
    }

    public boolean toggle(Player player, String[] args, QueryFactory qf) {
        String pref = args[0];
        if (pref.equals("auto") && !plugin.getConfig().getBoolean("allow.autonomous")) {
            TARDISMessage.send(player, "AUTO_DISABLED");
            return true;
        }
        if (pref.equals("eps") && !plugin.getConfig().getBoolean("allow.emergency_npc")) {
            TARDISMessage.send(player, "EP1_DISABLED");
            return true;
        }
        if (pref.equals("hads") && !plugin.getConfig().getBoolean("allow.hads")) {
            TARDISMessage.send(player, "HADS_DISBALED");
            return true;
        }
        if (pref.equals("travelbar") && !plugin.isBarAPIOnServer()) {
            TARDISMessage.send(player, "BAR_DISBALED");
            return true;
        }
        HashMap<String, Object> setp = new HashMap<String, Object>();
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("uuid", player.getUniqueId().toString());
        if (args[1].equalsIgnoreCase("on")) {
            setp.put(pref + "_on", 1);
            if (pref.equals("beacon")) {
                new TARDISBeaconToggler(plugin).flickSwitch(player.getUniqueId(), true);
            }
            String grammar = (was.contains(pref)) ? "PREF_WAS_ON" : "PREF_WERE_ON";
            TARDISMessage.send(player, grammar, pref);
        }
        if (args[1].equalsIgnoreCase("off")) {
            setp.put(pref + "_on", 0);
            if (pref.equals("beacon")) {
                new TARDISBeaconToggler(plugin).flickSwitch(player.getUniqueId(), false);
            }
            String grammar = (was.contains(pref)) ? "PREF_WAS_OFF" : "PREF_WERE_OFF";
            TARDISMessage.send(player, grammar, pref);
        }
        qf.doUpdate("player_prefs", setp, wherep);
        return true;
    }
}
