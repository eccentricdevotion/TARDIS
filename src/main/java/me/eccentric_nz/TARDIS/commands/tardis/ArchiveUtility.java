package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.interior.TIPSData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.SchematicBuilder;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveCount;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveName;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class ArchiveUtility {

    private static Tardis getTardis(TARDIS plugin, String uuid) {
        Tardis tardis = null;
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
                tardis = rs.getTardis();
            }
        }
        return tardis;
    }

    private static SchematicBuilder.ArchiveData getData(TARDIS plugin, Tardis tardis, String uuid, String name, String size, Player player) {
        Schematic current = tardis.getSchematic();
        // get the schematic start location, width, length and height
        int h;
        int w;
        int c;
        // get console size
        ConsoleSize console_size = ConsoleSize.valueOf(size.toUpperCase(Locale.ROOT));
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
        // calculate startx, starty, startz
        int slot = tardis.getTIPS();
        int id = tardis.getTardisId();
        int sx, sz;
        if (slot != -1000001) { // default world - use TIPS
            TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
            TIPSData pos = tintpos.getTIPSData(slot);
            sx = pos.getCentreX();
            sz = pos.getCentreZ();
        } else {
            int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardisId());
            sx = gsl[0];
            sz = gsl[2];
        }
        int sy = current.getStartY();
        return new SchematicBuilder(plugin, id, player.getLocation().getWorld(), sx, sx + w, sy, sy + h, sz, sz + c, console_size).build();
    }

    public static void add(TARDIS plugin, Player player, String name, String size) {
        // check name
        ResultSetArchiveName rsan = new ResultSetArchiveName(plugin, name);
        if (rsan.exists()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_EXIST", name);
        }
        String uuid = player.getUniqueId().toString();
        // check have not reached limit
        ResultSetArchiveCount rsc = new ResultSetArchiveCount(plugin, uuid);
        if (rsc.count() >= plugin.getConfig().getInt("archive.limit")) {
            plugin.getMessenger().sendColouredCommand(player, "ARCHIVE_LIMIT", "/tardis archive update", plugin);
            return;
        }
        Tardis tardis = getTardis(plugin, uuid);
        if (tardis == null) {
            return;
        }
        SchematicBuilder.ArchiveData ad = ArchiveUtility.getData(plugin, tardis, uuid, name, size, player);
        if (ad == null) {
            return;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("data", ad.getJSON().toString());
        set.put("console_size", ad.getSize().toString());
        set.put("beacon", ad.getBeacon());
        // get lights preference
        String lights = "TENTH";
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        if (rsp.resultSet()) {
            lights = rsp.getLights().toString();
        }
        set.put("lanterns", lights);
        // save json to database
        set.put("uuid", uuid);
        set.put("name", name);
        set.put("y", ad.getStartY());
        plugin.getQueryFactory().doInsert("archive", set);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_ADD", name);
    }

    public static void description(TARDIS plugin, Player player, String name, String description) {
        ResultSetArchiveName rsa = new ResultSetArchiveName(plugin, name);
        if (!rsa.exists()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_NOT_EXIST", name);
            return;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("description", description);
        HashMap<String, Object> whereu = new HashMap<>();
        whereu.put("uuid", player.getUniqueId().toString());
        whereu.put("name", name);
        plugin.getQueryFactory().doUpdate("archive", set, whereu);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_DESC", name);
    }

    public static void remove(TARDIS plugin, Player player, String name) {
        ResultSetArchiveName rsa = new ResultSetArchiveName(plugin, name);
        if (!rsa.exists()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_NOT_EXIST", name);
            return;
        }
        // delete the archive
        HashMap<String, Object> whereu = new HashMap<>();
        whereu.put("uuid", player.getUniqueId().toString());
        whereu.put("name", name);
        plugin.getQueryFactory().doDelete("archive", whereu);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_REMOVE", name);
    }

    public static void rename(TARDIS plugin, Player player, String name, String rename) {
        ResultSetArchiveName rsa = new ResultSetArchiveName(plugin, name);
        if (!rsa.exists()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_NOT_EXIST", name);
            return;
        }
        // update name
        HashMap<String, Object> set = new HashMap<>();
        set.put("name", rename);
        HashMap<String, Object> whereu = new HashMap<>();
        whereu.put("uuid", player.getUniqueId().toString());
        whereu.put("name", name);
        plugin.getQueryFactory().doUpdate("archive", set, whereu);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_RENAME", rename);
    }

    public static void scan(TARDIS plugin, Player player, String size) {
        String uuid = player.getUniqueId().toString();
        Tardis tardis = getTardis(plugin, uuid);
        if (tardis == null) {
            return;
        }
        SchematicBuilder.ArchiveData ad = ArchiveUtility.getData(plugin, tardis, uuid, "", size, player);
        if (ad == null) {
            return;
        }
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_SCAN");
    }

    public static void update(TARDIS plugin, Player player, String name, String size) {
        ResultSetArchiveName rsa = new ResultSetArchiveName(plugin, name);
        if (!rsa.exists()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_NOT_EXIST", name);
            return;
        }
        String uuid = player.getUniqueId().toString();
        Tardis tardis = getTardis(plugin, uuid);
        if (tardis == null) {
            return;
        }
        SchematicBuilder.ArchiveData ad = ArchiveUtility.getData(plugin, tardis, uuid, "", size, player);
        if (ad == null) {
            return;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("data", ad.getJSON().toString());
        set.put("console_size", ad.getSize().toString());
        set.put("beacon", ad.getBeacon());
        // get lights preference
        String lights = "TENTH";
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        if (rsp.resultSet()) {
            lights = rsp.getLights().toString();
        }
        set.put("lanterns", lights);
        // update json in database
        HashMap<String, Object> whereu = new HashMap<>();
        whereu.put("uuid", uuid);
        whereu.put("name", name);
        plugin.getQueryFactory().doUpdate("archive", set, whereu);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_UPDATE", name);
    }

    public static void y(TARDIS plugin, Player player, String name, int y) {
        ResultSetArchiveName rsa = new ResultSetArchiveName(plugin, name);
        if (!rsa.exists()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_NOT_EXIST", name);
            return;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("y", y);
        HashMap<String, Object> wherey = new HashMap<>();
        wherey.put("uuid", player.getUniqueId().toString());
        wherey.put("name", name);
        plugin.getQueryFactory().doUpdate("archive", set, wherey);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_UPDATE", name);
    }
}
