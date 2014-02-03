/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISEPSMessageCommand {

    private final TARDIS plugin;

    public TARDISEPSMessageCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setMessage(Player player, String[] args, QueryFactory qf) {
        int count = args.length;
        if (count < 2) {
            player.sendMessage(plugin.getPluginName() + "You need to specify an Emergency Program System message!");
            return false;
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 1; i < count; i++) {
            buf.append(args[i]).append(" ");
        }
        String tmp = buf.toString();
        String message = tmp.substring(0, tmp.length() - 1);
        HashMap<String, Object> sete = new HashMap<String, Object>();
        sete.put("eps_message", message);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", player.getName());
        qf.doUpdate("player_prefs", sete, where);
        player.sendMessage(plugin.getPluginName() + "The Emergency Program System message was set!");
        return true;
    }
}
