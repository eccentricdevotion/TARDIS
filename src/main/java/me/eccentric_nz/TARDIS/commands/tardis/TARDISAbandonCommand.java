/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAbandonLister;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.ResultSetTardisAbandoned;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.TARDISAbandonUpdate;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.move.TARDISDoorCloser;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAbandonCommand {

    private final TARDIS plugin;

    public TARDISAbandonCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doAbandon(CommandSender sender, boolean list) {
        if (sender.hasPermission("tardis.abandon") && plugin.getConfig().getBoolean("abandon.enabled")) {
            if (list) {
                // list abandoned TARDISes
                if (sender.hasPermission("tardis.admin")) {
                    new TARDISAbandonLister(plugin).list(sender);
                    return true;
                } else {
                    TARDISMessage.send(sender, "NO_PERMS");
                }
            } else {
                // must be a Player
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (player == null) {
                    TARDISMessage.send(sender, "CMD_NO_CONSOLE");
                    return true;
                }
                if (!plugin.getConfig().getBoolean("allow.power_down")) {
                    TARDISMessage.send(sender, "ABANDON_POWER_DOWN");
                    return true;
                }
                // abandon TARDIS
                ResultSetTardisAbandoned rs = new ResultSetTardisAbandoned(plugin);
                if (!rs.fromUUID(player.getUniqueId().toString())) {
                    TARDISMessage.send(player, "NO_TARDIS");
                    return true;
                } else {
                    // need to be in tardis
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                    if (!rst.resultSet()) {
                        TARDISMessage.send(player, "NOT_IN_TARDIS");
                        return true;
                    }
                    if (rs.getPreset().equals(PRESET.JUNK_MODE)) {
                        TARDISMessage.send(player, "ABANDONED_NOT_JUNK");
                        return true;
                    }
                    int id = rs.getTardis_id();
                    if (rst.getTardis_id() != id) {
                        TARDISMessage.send(player, "ABANDONED_OWN");
                        return true;
                    }
                    if (!rs.isTardis_init()) {
                        TARDISMessage.send(player, "ENERGY_NO_INIT");
                        return true;
                    }
                    if (!rs.isHandbrake_on()) {
                        TARDISMessage.send(player, "HANDBRAKE_ENGAGE");
                        return true;
                    }
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        TARDISMessage.send(player, "NOT_IN_VORTEX");
                        return true;
                    }
                    new TARDISAbandonUpdate(plugin, id, player.getUniqueId().toString()).run();
                    if (rs.isPowered_on()) {
                        // power down TARDIS
                        new TARDISPowerButton(plugin, id, player, rs.getPreset(), rs.isPowered_on(), rs.isHidden(), rs.isLights_on(), player.getLocation(), rs.getArtron_level(), rs.getSchematic().hasLanterns()).clickButton();
                    }
                    // close the door
                    new TARDISDoorCloser(plugin, player.getUniqueId(), id).closeDoors();
                    TARDISMessage.send(player, "ABANDONED_SUCCESS");
                }
            }
        } else {
            TARDISMessage.send(sender, "NO_PERMS_ABANDON");
        }
        return true;
    }
}
