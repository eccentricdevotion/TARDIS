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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class SecondaryCommand {

    private final TARDIS plugin;

    public SecondaryCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void startSecondary(Player player, String arg) {
        if (TARDISPermission.hasPermission(player, "tardis.update")) {
            UUID uuid = player.getUniqueId();
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(uuid.toString())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return;
            }
            if (arg.equalsIgnoreCase("remove")) {
                plugin.getTrackerKeeper().getSecondaryRemovers().put(player.getUniqueId(), rs.getTardisId());
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SEC_REMOVE_CLICK_BLOCK");
                return;
            }
            Updateable updateable;
            try {
                updateable = Updateable.valueOf(TARDISStringUtils.toScoredUppercase(arg));
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_NOT_VALID");
                return;
            }
            if (!updateable.isSecondary()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_NOT_VALID");
                return;
            }
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
                return;
            }
            plugin.getTrackerKeeper().getSecondary().put(uuid, updateable);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_CLICK", updateable.getName());
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
        }
    }
}
