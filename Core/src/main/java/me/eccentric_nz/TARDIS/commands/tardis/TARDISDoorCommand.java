/*
 * Copyright (C) 2025 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.doors.inner.*;
import me.eccentric_nz.TARDIS.doors.outer.OuterDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDoor;
import me.eccentric_nz.TARDIS.doors.outer.OuterMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISDoorCommand {

    private final TARDIS plugin;

    public TARDISDoorCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggleDoors(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "tardis.use")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return true;
        }
        // must have a TARDIS
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
            return false;
        }
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return false;
        }
        int id = rs.getTardisId();
        boolean open = (args[1].equalsIgnoreCase("close"));
        UUID playerUUID = player.getUniqueId();
        ResultSetTardisPreset rsp = new ResultSetTardisPreset(plugin);
        if (rsp.fromID(id)) {
            boolean outerDisplayDoor = rsp.getPreset().usesArmourStand();
            Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
            // toggle the doors
            if (open) {
                // close inner
                if (innerDisplayDoor.display()) {
                    new InnerDisplayDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID, true);
                } else {
                    new InnerMinecraftDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID);
                }
                // close outer
                if (outerDisplayDoor) {
                    new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
                } else if (rsp.getPreset().hasDoor()) {
                    new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(rsp.getPreset()), id, playerUUID);
                }
            } else {
                // open inner
                if (innerDisplayDoor.display()) {
                    new InnerDisplayDoorOpener(plugin).open(innerDisplayDoor.block(), id, playerUUID, true);
                } else {
                    new InnerMinecraftDoorOpener(plugin).open(innerDisplayDoor.block(), id, playerUUID);
                }
                // open outer
                if (outerDisplayDoor) {
                    new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
                } else if (rsp.getPreset().hasDoor()) {
                    new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(rsp.getPreset()), id, playerUUID);
                }
            }
        }
        return true;
    }
}
