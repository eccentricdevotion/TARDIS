/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.api.event.TARDISAbandonEvent;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAbandonLister;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardisAbandoned;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.TARDISAbandonUpdate;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.move.TARDISDoorCloser;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISAbandonCommand {

    private final TARDIS plugin;

    TARDISAbandonCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doAbandon(CommandSender sender, boolean list) {
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
                    PRESET preset = rs.getPreset();
                    // need to be in tardis
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                    if (!rst.resultSet()) {
                        TARDISMessage.send(player, "NOT_IN_TARDIS");
                        return true;
                    }
                    if (preset.equals(PRESET.JUNK_MODE)) {
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
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                    if (rsc.resultSet()) {
                        Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                        plugin.getPM().callEvent(new TARDISAbandonEvent(player, id, current));
                        // clear sign
                        if (plugin.getConfig().getBoolean("police_box.name_tardis")) {
                            Sign sign = getSign(current, rsc.getDirection(), preset);
                            if (sign != null) {
                                switch (preset) {
                                    case GRAVESTONE:
                                        sign.setLine(3, "");
                                        break;
                                    case ANGEL:
                                    case JAIL:
                                        sign.setLine(2, "");
                                        break;
                                    default:
                                        sign.setLine(0, "");
                                        break;
                                }
                                sign.update();
                            }
                        }
                    }
                }
            }
        } else {
            TARDISMessage.send(sender, "NO_PERMS_ABANDON");
        }
        return true;
    }

    public static Sign getSign(Location l, COMPASS d, PRESET p) {
        Sign sign = null;
        World w = l.getWorld();
        int signx, signz, signy;
        switch (p) {
            case JUNK_MODE:
                switch (d) {
                    case EAST:
                        signx = 0;
                        signz = 1;
                        break;
                    case WEST:
                        signx = 0;
                        signz = -1;
                        break;
                    default:
                        signx = 1;
                        signz = 0;
                        break;
                }
                break;
            case GRAVESTONE:
                signx = 0;
                signz = 0;
                break;
            case TORCH:
                switch (d) {
                    case EAST:
                        signx = -1;
                        signz = 0;
                        break;
                    case SOUTH:
                        signx = 0;
                        signz = -1;
                        break;
                    case WEST:
                        signx = 1;
                        signz = 0;
                        break;
                    default:
                        signx = 0;
                        signz = 1;
                        break;
                }
                break;
            case TOILET:
                switch (d) {
                    case EAST:
                        signx = 1;
                        signz = -1;
                        break;
                    case SOUTH:
                        signx = 1;
                        signz = 1;
                        break;
                    case WEST:
                        signx = -1;
                        signz = 1;
                        break;
                    default:
                        signx = -1;
                        signz = -1;
                        break;
                }
                break;
            case APPERTURE:
                switch (d) {
                    case EAST:
                        signx = 1;
                        signz = 0;
                        break;
                    case SOUTH:
                        signx = 0;
                        signz = 1;
                        break;
                    case WEST:
                        signx = -1;
                        signz = 0;
                        break;
                    default:
                        signx = 0;
                        signz = -1;
                        break;
                }
                break;
            default:
                switch (d) {
                    case EAST:
                        signx = -2;
                        signz = 0;
                        break;
                    case SOUTH:
                        signx = 0;
                        signz = -2;
                        break;
                    case WEST:
                        signx = 2;
                        signz = 0;
                        break;
                    default:
                        signx = 0;
                        signz = 2;
                        break;
                }
                break;
        }
        switch (p) {
            case GAZEBO:
            case JAIL:
            case SHROOM:
            case SWAMP:
                signy = 3;
                break;
            case TOPSYTURVEY:
            case TOILET:
            case TORCH:
                signy = 1;
                break;
            case ANGEL:
            case APPERTURE:
            case LAMP:
                signy = 0;
                break;
            default:
                signy = 2;
                break;
        }
        Block b = new Location(w, l.getBlockX() + signx, l.getBlockY() + signy, l.getBlockZ() + signz).getBlock();
        if (b.getType().equals(Material.OAK_WALL_SIGN)) {
            sign = (Sign) b.getState();
        }
        return sign;
    }
}
