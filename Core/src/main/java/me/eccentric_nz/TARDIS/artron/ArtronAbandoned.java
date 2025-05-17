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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISClaimEvent;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISAbandonCommand;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.doors.inner.Inner;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.inner.InnerDoor;
import me.eccentric_nz.TARDIS.doors.inner.InnerMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDoor;
import me.eccentric_nz.TARDIS.doors.outer.OuterMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArtronAbandoned {

    private final TARDIS plugin;

    public ArtronAbandoned(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean claim(Player player, int id, Location location, Tardis tardis) {
            // transfer ownership to the player who clicked
            boolean pu = plugin.getQueryFactory().claimTARDIS(player, id);
            // make sure player is added as owner of interior WorldGuard region
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                plugin.getWorldGuardUtils().updateRegionForClaim(location, player.getUniqueId());
            }
            ResultSetCurrentFromId rscl = new ResultSetCurrentFromId(plugin, id);
            if (rscl.resultSet()) {
                Location current = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                if (pu) {
                    ResultSetTardisPreset rsp = new ResultSetTardisPreset(plugin);
                    if (rsp.fromID(id)) {
                        boolean outerDisplayDoor = rsp.getPreset().usesArmourStand();
                        Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                        UUID playerUUID = player.getUniqueId();
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
                            new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, playerUUID);
                        }
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDON_CLAIMED");
                    plugin.getPM().callEvent(new TARDISClaimEvent(player, tardis, current));
                }
                if (plugin.getConfig().getBoolean("police_box.name_tardis")) {
                    ChameleonPreset preset = tardis.getPreset();
                    Sign sign = TARDISAbandonCommand.getSign(current, rscl.getDirection(), preset);
                    if (sign != null) {
                        SignSide front = sign.getSide(Side.FRONT);
                        String player_name = TARDISStaticUtils.getNick(player);
                        String owner;
                        if (preset.equals(ChameleonPreset.GRAVESTONE) || preset.equals(ChameleonPreset.PUNKED) || preset.equals(ChameleonPreset.ROBOT)) {
                            owner = (player_name.length() > 14) ? player_name.substring(0, 14) : player_name;
                        } else {
                            owner = (player_name.length() > 14) ? player_name.substring(0, 12) + "'s" : player_name + "'s";
                        }
                        switch (preset) {
                            case GRAVESTONE -> front.setLine(3, owner);
                            case ANGEL, JAIL -> front.setLine(2, owner);
                            default -> front.setLine(0, owner);
                        }
                        sign.update();
                    }
                }
            }
            return pu;
    }
}
