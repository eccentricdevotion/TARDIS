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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSecondaryCommand {

    private final TARDIS plugin;

    public TARDISSecondaryCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean startSecondary(Player player, String[] args) {
        if (player.hasPermission("tardis.update")) {
            String[] validBlockNames = {"button", "world-repeater", "x-repeater", "z-repeater", "y-repeater", "artron", "handbrake", "door", "back"};
            if (args.length < 2) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.TOO_FEW_ARGS.getText());
                return false;
            }
            String tardis_block = args[1].toLowerCase(Locale.ENGLISH);
            if (!Arrays.asList(validBlockNames).contains(tardis_block)) {
                TARDISMessage.send(player, plugin.getPluginName() + "That is not a valid TARDIS block name! Try one of : button|world-repeater|x-repeater|z-repeater|y-repeater|artron|handbrake|door|back");
                return false;
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NOT_A_TIMELORD.getText());
                return false;
            }
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NOT_IN_TARDIS.getText());
                return false;
            }
            plugin.getTrackerKeeper().getSecondary().put(player.getUniqueId(), tardis_block);
            TARDISMessage.send(player, plugin.getPluginName() + "Click the TARDIS " + tardis_block + " to update its position.");
            return true;
        } else {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
