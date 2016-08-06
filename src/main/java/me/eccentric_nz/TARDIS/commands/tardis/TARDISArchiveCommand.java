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

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchiveCount;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchiveName;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicBuilder;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicBuilder.ArchiveData;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISArchiveCommand {

    private final TARDIS plugin;
    private final List<String> subs = Arrays.asList("add", "description", "remove", "scan", "update");

    public TARDISArchiveCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean zip(Player player, String[] args) {
        if (!player.hasPermission("tardis.archive")) {
            TARDISMessage.send(player, "NO_PERMS");
            return true;
        }
        if (args.length < 2) {
            return false;
        }
        String sub = args[1].toLowerCase(Locale.ENGLISH);
        if (!subs.contains(sub)) {
            TARDISMessage.send(player, "ARG_NOT_VALID");
            return true;
        }
        String uuid = player.getUniqueId().toString();
        String name = (args.length > 2) ? args[2].toUpperCase(Locale.ENGLISH) : "ARCHIVE";
        if (sub.equals("add") || sub.equals("description") || sub.equals("remove") || sub.equals("update")) {
            if (args.length < 3) {
                TARDISMessage.send(player, "SCHM_NAME");
                return true;
            } else {
                // check name
                ResultSetArchiveName rsa = new ResultSetArchiveName(plugin, name);
                boolean exists = rsa.exists();
                if (sub.equals("add") && exists) {
                    TARDISMessage.send(player, "ARCHIVE_EXIST", name);
                    return true;
                }
                if ((sub.equals("description") || sub.equals("remove") || sub.equals("update")) && !exists) {
                    TARDISMessage.send(player, "ARCHIVE_NOT_EXIST", name);
                    return true;
                }
                if (sub.equals("add")) {
                    // check have not reached limit
                    ResultSetArchiveCount rsc = new ResultSetArchiveCount(plugin, uuid);
                    if (rsc.count() >= plugin.getConfig().getInt("archive.limit")) {
                        TARDISMessage.send(player, "ARCHIVE_LIMIT");
                        return true;
                    }
                }
            }
        }
        QueryFactory qf = new QueryFactory(plugin);
        if (sub.equals("scan") || sub.equals("add") || sub.equals("update")) {
            // get TARDIS player is in
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("uuid", uuid);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                // must be the owner of the TARDIS
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", id);
                where.put("uuid", uuid);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
                if (rs.resultSet()) {
                    Tardis tardis = rs.getTardis();
                    SCHEMATIC current = tardis.getSchematic();
                    // get the schematic start location, width, length and height
                    String directory = (current.isCustom()) ? "user_schematics" : "schematics";
                    String path = plugin.getDataFolder() + File.separator + directory + File.separator + current.getPermission() + ".tschm";
                    File file = new File(path);
                    if (!file.exists()) {
                        plugin.debug(plugin.getPluginName() + "Could not find a schematic with that name!");
                        return true;
                    }
                    // get JSON
                    JSONObject obj = TARDISSchematicGZip.unzip(path);
                    // get dimensions
                    JSONObject dimensions = (JSONObject) obj.get("dimensions");
                    int h = dimensions.getInt("height") - 1;
                    int w = dimensions.getInt("width") - 1;
                    int c = dimensions.getInt("length") - 1;
                    // calculate startx, starty, startz
                    int slot = tardis.getTIPS();
                    id = tardis.getTardis_id();
                    int sx, sz;
                    if (slot != -1) { // default world - use TIPS
                        TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                        TARDISTIPSData pos = tintpos.getTIPSData(slot);
                        sx = pos.getCentreX();
                        sz = pos.getCentreZ();
                    } else {
                        int gsl[] = plugin.getLocationUtils().getStartLocation(tardis.getTardis_id());
                        sx = gsl[0];
                        sz = gsl[2];
                    }
                    int sy = (current.getPermission().equals("redstone")) ? 65 : 64;
                    ArchiveData ad = new TARDISSchematicBuilder(plugin, id, player.getLocation().getWorld(), sx, sx + w, sy, sy + h, sz, sz + c).build();
//                    if (ad == null) {
//                        TARDISMessage.send(player, "ARCHIVE_FAIL");
//                        return true;
//                    }
                    if (sub.equals("scan")) {
                        TARDISMessage.send(player, "ARCHIVE_SCAN");
                        return true;
                    }
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("data", ad.getJSON().toString());
                    // get console size
                    ConsoleSize console_size = ConsoleSize.getByWidthAndHeight(w, h);
                    set.put("console_size", console_size.toString());
                    set.put("beacon", ad.getBeacon());
                    // get lanterns preference
                    int lanterns = 0;
                    HashMap<String, Object> wherep = new HashMap<String, Object>();
                    wherep.put("uuid", uuid);
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
                    if (rsp.resultSet() && rsp.isLanternsOn()) {
                        lanterns = 1;
                    }
                    set.put("lanterns", lanterns);
                    if (sub.equals("add")) {
                        // save json to database
                        set.put("uuid", uuid);
                        set.put("name", name);
                        qf.doInsert("archive", set);
                        TARDISMessage.send(player, "ARCHIVE_ADD", name);
                        return true;
                    }
                    if (sub.equals("update")) {
                        // update json in database
                        HashMap<String, Object> whereu = new HashMap<String, Object>();
                        whereu.put("uuid", uuid);
                        whereu.put("name", name);
                        qf.doUpdate("archive", set, whereu);
                        TARDISMessage.send(player, "ARCHIVE_UPDATE", name);
                        return true;
                    }
                } else {
                    TARDISMessage.send(player, "CMD_ONLY_TL");
                    return true;
                }
            } else {
                TARDISMessage.send(player, "CMD_IN_WORLD");
                return true;
            }
        }
        if (sub.equals("description")) {
            // get description
            StringBuilder sb = new StringBuilder();
            for (int i = 4; i < args.length; i++) {
                if (i != 4) {
                    sb.append(args[i]);
                } else {
                    sb.append(" ").append(args[i]);
                }
            }
            // update description
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("description", sb.toString());
            HashMap<String, Object> whereu = new HashMap<String, Object>();
            whereu.put("uuid", uuid);
            whereu.put("name", name);
            qf.doUpdate("archive", set, whereu);
            TARDISMessage.send(player, "ARCHIVE_DESC", name);
            return true;
        }
        if (sub.equals("remove")) {
            // delete the archive
            HashMap<String, Object> whereu = new HashMap<String, Object>();
            whereu.put("uuid", uuid);
            whereu.put("name", name);
            qf.doDelete("archive", whereu);
            TARDISMessage.send(player, "ARCHIVE_REMOVE", name);
        }
        return true;
    }
}
