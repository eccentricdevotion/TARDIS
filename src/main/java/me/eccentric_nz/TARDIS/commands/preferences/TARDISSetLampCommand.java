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

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetLampCommand {

    private final TARDIS plugin;

    public TARDISSetLampCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setLampPref(Player player, String[] args, QueryFactory qf) {
        if (args.length < 2) {
            TARDISMessage.send(player, "LAMP_NEED");
            return false;
        }
        int lamp;
        try {
            lamp = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            TARDISMessage.send(player, "LAMP_NOT_NUMBER");
            return true;
        }
        // check it's an allowed block
        List<Integer> allowed_ids = plugin.getBlocksConfig().getIntegerList("lamp_blocks");
        if (!allowed_ids.contains(lamp)) {
            TARDISMessage.send(player, "LAMP_NO_SET");
            return true;
        }
        HashMap<String, Object> setl = new HashMap<String, Object>();
        setl.put("lamp", lamp);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", player.getUniqueId().toString());
        qf.doUpdate("player_prefs", setl, where);
        TARDISMessage.send(player, "LAMP_SAVED");
        return true;
    }
}
