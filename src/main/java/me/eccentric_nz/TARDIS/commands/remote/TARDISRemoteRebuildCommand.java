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
package me.eccentric_nz.TARDIS.commands.remote;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISRemoteRebuildCommand {

    private final TARDIS plugin;

    public TARDISRemoteRebuildCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doRemoteRebuild(CommandSender sender, int id, OfflinePlayer player, boolean hidden) {
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (!rsc.resultSet()) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return true;
        }
        ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
        if (rs.fromID(id) && rs.getPreset().equals(ChameleonPreset.INVISIBLE)) {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "INVISIBILITY_ENGAGED");
            return true;
        }
        Current current = rsc.getCurrent();
        BuildData bd = new BuildData(player.getUniqueId().toString());
        bd.setDirection(current.direction());
        bd.setLocation(current.location());
        bd.setMalfunction(false);
        bd.setOutside(false);
        bd.setPlayer(player);
        bd.setRebuild(true);
        bd.setSubmarine(current.submarine());
        bd.setTardisID(id);
        bd.setThrottle(SpaceTimeThrottle.REBUILD);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 10L);
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "TARDIS_REBUILT");
        // set hidden to false
        if (hidden) {
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            HashMap<String, Object> seth = new HashMap<>();
            seth.put("hidden", 0);
            plugin.getQueryFactory().doUpdate("tardis", seth, whereh);
        }
        return true;
    }
}
