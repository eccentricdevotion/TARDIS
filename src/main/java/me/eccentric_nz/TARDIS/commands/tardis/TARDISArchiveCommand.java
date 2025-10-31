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
package me.eccentric_nz.TARDIS.commands.tardis;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.interior.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicBuilder;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicBuilder.ArchiveData;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveByName;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveCount;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveName;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveUse;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISArchiveCommand {

    private final TARDIS plugin;
    private final List<String> subs = List.of("add", "description", "remove", "rename", "scan", "update", "y");

    TARDISArchiveCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean zip(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "tardis.archive")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return true;
        }
        if (args.length < 2) {
            return false;
        }
        String sub = args[1].toLowerCase(Locale.ROOT);
        if (!subs.contains(sub)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_NOT_VALID");
            return true;
        }
        String uuid = player.getUniqueId().toString();
        String name = (args.length > 2) ? args[2].toUpperCase(Locale.ROOT) : "ARCHIVE";
        if (sub.equals("add") || sub.equals("description") || sub.equals("remove") || sub.equals("update") || sub.equals("y")) {
            if (args.length < 3) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SCHM_NAME");
                return true;
            } else {
                // check name
                ResultSetArchiveName rsa = new ResultSetArchiveName(plugin, name);
                boolean exists = rsa.exists();
                if (sub.equals("add") && exists) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_EXIST", name);
                    return true;
                }
                if ((sub.equals("description") || sub.equals("remove") || sub.equals("update") || sub.equals("y")) && !exists) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_NOT_EXIST", name);
                    return true;
                }
                if (sub.equals("add")) {
                    // check have not reached limit
                    ResultSetArchiveCount rsc = new ResultSetArchiveCount(plugin, uuid);
                    if (rsc.count() >= plugin.getConfig().getInt("archive.limit")) {
                        plugin.getMessenger().sendColouredCommand(player, "ARCHIVE_LIMIT", "/tardis archive update", plugin);
                        return true;
                    }
                }
                if (sub.equals("remove")) {
                    // check archive is not active
                    ResultSetArchiveUse rsu = new ResultSetArchiveUse(plugin, uuid, name);
                    if (rsu.isActive()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_IN_USE");
                        return true;
                    }
                }
            }
        }
        if (sub.equals("y")) {
            // update y
            if (args.length > 3 && (args[3].equals("64") || args[3].equals("65"))) {
                HashMap<String, Object> set = new HashMap<>();
                set.put("y", TARDISNumberParsers.parseInt(args[3]));
                HashMap<String, Object> wherey = new HashMap<>();
                wherey.put("uuid", uuid);
                wherey.put("name", name);
                plugin.getQueryFactory().doUpdate("archive", set, wherey);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_UPDATE", name);
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_Y");
            }
            return true;
        }
        if (sub.equals("scan") || sub.equals("add") || sub.equals("update")) {
            // get TARDIS player is in
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", uuid);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                // must be the owner of the TARDIS
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                where.put("uuid", uuid);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    Tardis tardis = rs.getTardis();
                    Schematic current = tardis.getSchematic();
                    // get the schematic start location, width, length and height
                    JsonObject obj = null;
                    if (current.getPermission().equals("archive")) {
                        ResultSetArchiveByName rsa = new ResultSetArchiveByName(plugin, uuid, name);
                        if (rsa.resultSet()) {
                            Archive archive = rsa.getArchive();
                            obj = archive.getJSON();
                        }
                    } else {
                        // get JSON
                        obj = TARDISSchematicGZip.getObject(plugin, "consoles", current.getPermission(), current.isCustom());
                    }
                    if (obj != null) {
                        // get dimensions
                        JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                        int h = dimensions.get("height").getAsInt() - 1;
                        int w = dimensions.get("width").getAsInt() - 1;
                        int c = dimensions.get("length").getAsInt() - 1;
                        // get console size
                        ConsoleSize console_size = ConsoleSize.getByWidthAndHeight(w, h);
                        if (((args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("update")) && args.length == 4) || (args[1].equalsIgnoreCase("scan") && args.length == 3)) {
                            // size from args[2/3]
                            try {
                                String size = (args[1].equalsIgnoreCase("scan")) ? args[2] : args[3];
                                console_size = ConsoleSize.valueOf(size.toUpperCase(Locale.ROOT));
                                switch (console_size) {
                                    case MASSIVE -> {
                                        h = 31;
                                        w = 47;
                                        c = 47;
                                    }
                                    case WIDE -> {
                                        h = 15;
                                        w = 47;
                                        c = 47;
                                    }
                                    case TALL -> {
                                        h = 31;
                                        w = 31;
                                        c = 31;
                                    }
                                    case MEDIUM -> {
                                        h = 15;
                                        w = 31;
                                        c = 31;
                                    }
                                    default -> {
                                        h = 15;
                                        w = 15;
                                        c = 15;
                                    }
                                }
                            } catch (IllegalArgumentException e) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_SIZE");
                                return true;
                            }
                        }
                        // calculate startx, starty, startz
                        int slot = tardis.getTIPS();
                        id = tardis.getTardisId();
                        int sx, sz;
                        if (slot != -1000001) { // default world - use TIPS
                            TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                            TARDISTIPSData pos = tintpos.getTIPSData(slot);
                            sx = pos.getCentreX();
                            sz = pos.getCentreZ();
                        } else {
                            int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardisId());
                            sx = gsl[0];
                            sz = gsl[2];
                        }
                        int sy = current.getStartY();
                        ArchiveData ad = new TARDISSchematicBuilder(plugin, id, player.getLocation().getWorld(), sx, sx + w, sy, sy + h, sz, sz + c).build();
                        if (sub.equals("scan")) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_SCAN");
                            return true;
                        }
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("data", ad.getJSON().toString());
                        set.put("console_size", console_size.toString());
                        set.put("beacon", ad.getBeacon());
                        // get lights preference
                        String lights = "TENTH";
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                        if (rsp.resultSet()) {
                            lights = rsp.getLights().toString();
                        }
                        set.put("lanterns", lights);
                        if (sub.equals("add")) {
                            // save json to database
                            set.put("uuid", uuid);
                            set.put("name", name);
                            set.put("y", sy);
                            plugin.getQueryFactory().doInsert("archive", set);
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_ADD", name);
                        }else  {
                            // update json in database
                            HashMap<String, Object> whereu = new HashMap<>();
                            whereu.put("uuid", uuid);
                            whereu.put("name", name);
                            plugin.getQueryFactory().doUpdate("archive", set, whereu);
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_UPDATE", name);
                        }
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_NO_JSON");
                    }
                    return true;
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_ONLY_TL");
                    return true;
                }
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_IN_WORLD");
                return true;
            }
        }
        if (sub.equals("description")) {
            // get description
            StringBuilder sb = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                if (i != 3) {
                    sb.append(" ").append(args[i]);
                } else {
                    sb.append(args[i]);
                }
            }
            // update description
            HashMap<String, Object> set = new HashMap<>();
            set.put("description", sb.toString());
            HashMap<String, Object> whereu = new HashMap<>();
            whereu.put("uuid", uuid);
            whereu.put("name", name);
            plugin.getQueryFactory().doUpdate("archive", set, whereu);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_DESC", name);
            return true;
        }
        if (sub.equals("remove")) {
            // delete the archive
            HashMap<String, Object> whereu = new HashMap<>();
            whereu.put("uuid", uuid);
            whereu.put("name", name);
            plugin.getQueryFactory().doDelete("archive", whereu);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_REMOVE", name);
        }
        if (sub.equals("rename")) {
            // get name
            String rename = args[3];
            // update name
            HashMap<String, Object> set = new HashMap<>();
            set.put("name", rename);
            HashMap<String, Object> whereu = new HashMap<>();
            whereu.put("uuid", uuid);
            whereu.put("name", name);
            plugin.getQueryFactory().doUpdate("archive", set, whereu);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_RENAME", rename);
            return true;
        }
        return true;
    }
}
