/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class TARDISPlaceholderExpansion extends PlaceholderExpansion {

    private final TARDIS plugin;

    public TARDISPlaceholderExpansion(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public @NotNull String getIdentifier() {
        return "tardis";
    }

    @Override
    public @NotNull String getAuthor() {
        return "eccentric_nz";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * This is the method called when a placeholder with our identifier is found and needs a value. We specify the value
     * identifier in this method.
     *
     * @param player     An org.bukkit.Player
     * @param identifier A String containing the identifier
     * @return a possibly-null String of the requested identifier
     */
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        String result = null;
        if (player == null) {
            result = "";
        } else {
            String uuid = player.getUniqueId().toString();
            ResultSetArtronLevel rsl;
            ResultSetTardis rst;
            HashMap<String, Object> where = new HashMap<>();
            ResultSetTardisID rsti;
            ResultSetCurrentLocation rscl;
            if (identifier.startsWith("in")) {
                // default to false so if all else fails we get a result
                result = "false";
                where.put("uuid", uuid);
                ResultSetTravellers rsv = new ResultSetTravellers(plugin, where, false);
                if (rsv.resultSet()) {
                    /*
                     %tardis_in_any%
                     %tardis_in_own%
                     %tardis_in_whose%
                     %tardis_in_user_<username>%
                     */
                    String[] split = identifier.split("_");
                    switch (split[1]) {
                        case "any":
                            result = "true";
                            break;
                        case "own":
                            rsti = new ResultSetTardisID(plugin);
                            if (rsti.fromUUID(uuid) && rsti.getTardis_id() == rsv.getTardis_id()) {
                                result = "true";
                            }
                            break;
                        case "whose":
                            where.put("tardis_id", rsv.getTardis_id());
                            rst = new ResultSetTardis(plugin, where, "", false, 2);
                            if (rst.resultSet()) {
                                if (rst.getTardis().getTardis_id() == rsv.getTardis_id()) {
                                    result = "their own";
                                } else {
                                    result = rst.getTardis().getOwner() + "'s";
                                }
                            }
                            break;
                        default:
                            // user
                            if (split.length > 2) {
                                OfflinePlayer offlinePlayer = plugin.getServer().getPlayer(split[2]);
                                if (offlinePlayer != null) {
                                    rsti = new ResultSetTardisID(plugin);
                                    if (rsti.fromUUID(offlinePlayer.getUniqueId().toString()) && rsti.getTardis_id() == rsv.getTardis_id()) {
                                        result = "true";
                                    }
                                }
                            }
                            break;
                    }
                } else {
                    result = "Not in any TARDIS";
                }
            } else {
                switch (identifier) {
                    case "ars_status":
                        Integer percent = plugin.getBuildKeeper().getRoomProgress().get(player.getUniqueId());
                        if (percent != null) {
                            result = Integer.toString(percent);
                        } else {
                            result = "ARS not in use";
                        }
                        break;
                    case "artron_amount":
                        rsl = new ResultSetArtronLevel(plugin, uuid);
                        if (rsl.resultset()) {
                            result = Integer.toString(rsl.getArtronLevel());
                        } else {
                            result = "0";
                        }
                        break;
                    case "artron_percent":
                        rsl = new ResultSetArtronLevel(plugin, uuid);
                        if (rsl.resultset()) {
                            result = String.format("%s%%", Math.round(rsl.getArtronLevel() * 100.0d / plugin.getArtronConfig().getDouble("full_charge")));
                        } else {
                            result = "0%";
                        }
                        break;
                    case "console":
                        where.put("uuid", uuid);
                        rst = new ResultSetTardis(plugin, where, "", false, 2);
                        if (rst.resultSet()) {
                            result = TARDISStringUtils.uppercaseFirst(rst.getTardis().getSchematic().getPermission());
                        } else {
                            result = "";
                        }
                        break;
                    case "preset":
                        where.put("uuid", uuid);
                        rst = new ResultSetTardis(plugin, where, "", false, 2);
                        if (rst.resultSet()) {
                            result = rst.getTardis().getPreset().toString();
                        } else {
                            result = "";
                        }
                        break;
                    case "current_location":
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(uuid)) {
                            where.put("tardis_id", rsti.getTardis_id());
                            rscl = new ResultSetCurrentLocation(plugin, where);
                            if (rscl.resultSet()) {
                                result = "TARDIS was left at " + rscl.getWorld().getName() + " at " + "x: " + rscl.getX() + " y: " + rscl.getY() + " z: " + rscl.getZ();
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                        break;
                    case "current_location_x":
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(uuid)) {
                            where.put("tardis_id", rsti.getTardis_id());
                            rscl = new ResultSetCurrentLocation(plugin, where);
                            if (rscl.resultSet()) {
                                result = Integer.toString(rscl.getX());
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                        break;
                    case "current_location_y":
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(uuid)) {
                            where.put("tardis_id", rsti.getTardis_id());
                            rscl = new ResultSetCurrentLocation(plugin, where);
                            if (rscl.resultSet()) {
                                result = Integer.toString(rscl.getY());
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                        break;
                    case "current_location_z":
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(uuid)) {
                            where.put("tardis_id", rsti.getTardis_id());
                            rscl = new ResultSetCurrentLocation(plugin, where);
                            if (rscl.resultSet()) {
                                result = Integer.toString(rscl.getZ());
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                        break;
                    case "current_location_world":
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(uuid)) {
                            where.put("tardis_id", rsti.getTardis_id());
                            rscl = new ResultSetCurrentLocation(plugin, where);
                            if (rscl.resultSet()) {
                                result = rscl.getWorld().getName();
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                        break;
                    case "current_location_direction":
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(uuid)) {
                            where.put("tardis_id", rsti.getTardis_id());
                            rscl = new ResultSetCurrentLocation(plugin, where);
                            if (rscl.resultSet()) {
                                result = rscl.getDirection().toString();
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                        break;
                    case "current_location_biome":
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(uuid)) {
                            where.put("tardis_id", rsti.getTardis_id());
                            rscl = new ResultSetCurrentLocation(plugin, where);
                            if (rscl.resultSet()) {
                                // get from current location
                                Location location = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                                result = TARDISStaticUtils.getBiomeAt(location).name();
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                        break;
                    case "timelord_artron_amount":
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                        if (rsp.resultSet()) {
                            result = Integer.toString(rsp.getArtronLevel());
                        } else {
                            result = "0";
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        // return null if an invalid placeholder (e.g. %tardis_unknownplaceholder%) was provided
        return result;
    }
}
