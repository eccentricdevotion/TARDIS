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
package me.eccentric_nz.TARDIS.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.UUID;

public class TARDISPlaceholderExpansion extends PlaceholderExpansion {

    private final TARDIS plugin;

    public TARDISPlaceholderExpansion(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier() {
        return "tardis";
    }

    @Override
    public String getAuthor() {
        return "eccentric_nz";
    }

    @Override
    public String getVersion() {
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
    public String onRequest(OfflinePlayer player, String identifier) {
        String result = null;
        if (player == null) {
            result = "";
        } else {
            UUID uuid = player.getUniqueId();
            String unique = uuid.toString();
            ResultSetArtronLevel rsl;
//            ResultSetTardis rst;
            HashMap<String, Object> where = new HashMap<>();
            Tardis tardis;
            ResultSetTardisID rsti;
            Current current;
            if (identifier.startsWith("in")) {
                // default to false so if all else fails we get a result
                result = "false";
                where.put("uuid", unique);
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
                        case "any" -> result = "true";
                        case "own" -> {
                            rsti = new ResultSetTardisID(plugin);
                            if (rsti.fromUUID(unique) && rsti.getTardisId() == rsv.getTardis_id()) {
                                result = "true";
                            }
                        }
                        case "whose" -> {
//                            where.put("tardis_id", rsv.getTardis_id());
//                            rst = new ResultSetTardis(plugin, where, "", false, 2);
//                            if (rst.resultSet()) {
                            tardis = TARDISCache.BY_ID.get(rsv.getTardis_id());
                            if (tardis != null) {
                                if (tardis.getTardisId() == rsv.getTardis_id()) {
                                    result = "their own";
                                } else {
                                    result = tardis.getOwner() + "'s";
                                }
                            }
                        }
                        default -> {
                            // user
                            if (split.length > 2) {
                                OfflinePlayer offlinePlayer = plugin.getServer().getPlayer(split[2]);
                                if (offlinePlayer != null) {
                                    rsti = new ResultSetTardisID(plugin);
                                    if (rsti.fromUUID(offlinePlayer.getUniqueId().toString()) && rsti.getTardisId() == rsv.getTardis_id()) {
                                        result = "true";
                                    }
                                }
                            }
                        }
                    }
                } else {
                    result = "Not in any TARDIS";
                }
            } else {
                switch (identifier) {
                    case "ars_status" -> {
                        Integer percent = plugin.getBuildKeeper().getRoomProgress().get(player.getUniqueId());
                        if (percent != null) {
                            result = Integer.toString(percent);
                        } else {
                            result = "ARS not in use";
                        }
                    }
                    case "artron_amount" -> {
                        rsl = new ResultSetArtronLevel(plugin, unique);
                        if (rsl.resultset()) {
                            result = Integer.toString(rsl.getArtronLevel());
                        } else {
                            result = "0";
                        }
                    }
                    case "artron_percent" -> {
                        rsl = new ResultSetArtronLevel(plugin, unique);
                        if (rsl.resultset()) {
                            result = String.format("%s%%", Math.round(rsl.getArtronLevel() * 100.0d / plugin.getArtronConfig().getDouble("full_charge")));
                        } else {
                            result = "0%";
                        }
                    }
                    case "console" -> {
//                        where.put("uuid", uuid);
//                        rst = new ResultSetTardis(plugin, where, "", false, 2);
//                        if (rst.resultSet()) {
                        tardis = TARDISCache.BY_UUID.get(uuid);
                        if (tardis != null) {
                            result = TARDISStringUtils.uppercaseFirst(tardis.getSchematic().getPermission());
                        } else {
                            result = "";
                        }
                    }
                    case "preset" -> {
//                        where.put("uuid", uuid);
//                        rst = new ResultSetTardis(plugin, where, "", false, 2);
//                        if (rst.resultSet()) {
                        tardis = TARDISCache.BY_UUID.get(uuid);
                        if (tardis != null) {
                            result = tardis.getPreset().toString();
                        } else {
                            result = "";
                        }
                    }
                    case "current_location" -> {
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(unique)) {
                            current = TARDISCache.CURRENT.get(rsti.getTardisId());
                            if (current != null) {
                                result = "TARDIS was left at " + current.location().getWorld().getName() + " at " + "x: " + current.location().getBlockX() + " y: " + current.location().getBlockY() + " z: " + current.location().getBlockZ();
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                    }
                    case "current_location_x" -> {
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(unique)) {
                            current = TARDISCache.CURRENT.get(rsti.getTardisId());
                            if (current != null) {
                                result = Integer.toString(current.location().getBlockX());
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                    }
                    case "current_location_y" -> {
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(unique)) {
                            current = TARDISCache.CURRENT.get(rsti.getTardisId());
                            if (current != null) {
                                result = Integer.toString(current.location().getBlockY());
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                    }
                    case "current_location_z" -> {
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(unique)) {
                            current = TARDISCache.CURRENT.get(rsti.getTardisId());
                            if (current != null) {
                                result = Integer.toString(current.location().getBlockZ());
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                    }
                    case "current_location_world" -> {
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(unique)) {
                            current = TARDISCache.CURRENT.get(rsti.getTardisId());
                            if (current != null) {
                                result = current.location().getWorld().getName();
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                    }
                    case "current_location_direction" -> {
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(unique)) {
                            current = TARDISCache.CURRENT.get(rsti.getTardisId());
                            if (current != null) {
                                result = current.direction().toString();
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                    }
                    case "current_location_biome" -> {
                        rsti = new ResultSetTardisID(plugin);
                        if (rsti.fromUUID(unique)) {
                            current = TARDISCache.CURRENT.get(rsti.getTardisId());
                            if (current != null) {
                                // get from current location
                                result = current.location().getBlock().getBiome().toString();
                            } else {
                                result = "";
                            }
                        } else {
                            result = "";
                        }
                    }
                    case "timelord_artron_amount" -> {
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, unique);
                        if (rsp.resultSet()) {
                            result = Integer.toString(rsp.getArtronLevel());
                        } else {
                            result = "0";
                        }
                    }
                    default -> {
                    }
                }
            }
        }
        // return null if an invalid placeholder (e.g. %tardis_unknownplaceholder%) was provided
        return result;
    }
}
