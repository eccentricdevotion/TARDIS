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
package me.eccentric_nz.TARDIS.desktop;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.ArchiveReset;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISThemeProcessor {

    private final TARDIS plugin;
    private final UUID uuid;
    private Archive archive_next;

    public TARDISThemeProcessor(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void changeDesktop() {
        // get upgrade data
        TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
        Player player = plugin.getServer().getPlayer(uuid);
        if (plugin.getHandlesConfig().getBoolean("enabled") && TARDISPermission.hasPermission(player, "tardis.handles")) {
//            HashMap<String, Object> wheret = new HashMap<>();
//            wheret.put("uuid", uuid.toString());
//            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
//            rs.resultSet();
//            Tardis tardis = rs.getTardis();
            Tardis tardis = TARDISCache.BY_UUID.get(uuid);
            if (tardis != null) {
                // check if the player has a Handles placed
                HashMap<String, Object> whereh = new HashMap<>();
                whereh.put("type", 26);
                whereh.put("tardis_id", tardis.getTardisId());
                ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
                if (rsc.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "UPGRADE_REMOVE_HANDLES");
                    return;
                }
            }
        }
        // get Archive if necessary
        ConsoleSize size_next;
        int nextHeight;
        int nextWidth;
        if (tud.getSchematic().getPermission().equals("archive")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            where.put("use", 1);
            ResultSetArchive rs = new ResultSetArchive(plugin, where);
            if (rs.resultSet()) {
                archive_next = rs.getArchive();
                JsonObject dimensions = archive_next.getJSON().get("dimensions").getAsJsonObject();
                nextHeight = dimensions.get("height").getAsInt();
                nextWidth = dimensions.get("width").getAsInt();
                size_next = archive_next.getConsoleSize();
            } else {
                // abort
                Player cp = plugin.getServer().getPlayer(uuid);
                plugin.getMessenger().send(cp, TardisModule.TARDIS, "ARCHIVE_NOT_FOUND");
                return;
            }
        } else {
            // get JSON
            JsonObject obj = TARDISSchematicGZip.getObject(plugin, "consoles", tud.getSchematic().getPermission(), tud.getSchematic().isCustom());
            if (obj == null) {
                return;
            }
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            nextHeight = dimensions.get("height").getAsInt();
            nextWidth = dimensions.get("width").getAsInt();
            size_next = tud.getSchematic().getConsoleSize();
        }
        ConsoleSize size_prev;
        int previousHeight;
        int previousWidth;
        if (tud.getPrevious().getPermission().equals("archive")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            where.put("use", 2);
            ResultSetArchive rs = new ResultSetArchive(plugin, where);
            if (rs.resultSet()) {
                JsonObject dimensions = rs.getArchive().getJSON().get("dimensions").getAsJsonObject();
                previousHeight = dimensions.get("height").getAsInt();
                previousWidth = dimensions.get("width").getAsInt();
                size_prev = rs.getArchive().getConsoleSize();
            } else {
                // abort
                Player cp = plugin.getServer().getPlayer(uuid);
                plugin.getMessenger().send(cp, TardisModule.TARDIS, "ARCHIVE_NOT_FOUND");
                return;
            }
        } else {
            // get JSON
            JsonObject obj = TARDISSchematicGZip.getObject(plugin, "consoles", tud.getPrevious().getPermission(), tud.getPrevious().isCustom());
            if (obj == null) {
                return;
            }
            // get dimensions
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            previousHeight = dimensions.get("height").getAsInt();
            previousWidth = dimensions.get("width").getAsInt();
            size_prev = tud.getPrevious().getConsoleSize();
        }
        // if configured check whether there are still any blocks left
        if (plugin.getConfig().getBoolean("desktop.check_blocks_before_upgrade")) {
            TARDISUpgradeBlockScanner scanner = new TARDISUpgradeBlockScanner(plugin, tud, uuid);
            TARDISBlockScannerData check = scanner.check();
            if (check == null) {
                return;
            } else if (!check.allow()) {
                Player cp = plugin.getServer().getPlayer(uuid);
                plugin.getMessenger().send(cp, TardisModule.TARDIS, "UPGRADE_PERCENT_BLOCKS", plugin.getConfig().getInt("desktop.block_change_percent") + "");
                plugin.getMessenger().send(cp, TardisModule.TARDIS, "UPGRADE_PERCENT_EXPLAIN", check.getCount() + "", check.getVolume() + "", check.getChanged() + "");
                plugin.getMessenger().send(cp, TardisModule.TARDIS, "UPGRADE_PERCENT_REASON");
                if (tud.getPrevious().getPermission().equals("archive")) {
                    // reset archive use back to 1
                    new ArchiveReset(plugin, uuid.toString(), 1).resetUse();
                }
                return;
            }
        }
        // check if there are any rooms that need to be jettisoned
        if (nextWidth > previousWidth || nextHeight > previousHeight) {
            // we need more space!
            if (checkARSGrid(size_prev, size_next, uuid)) {
                plugin.getMessenger().send(plugin.getServer().getPlayer(uuid), TardisModule.TARDIS, "UPGRADE_ABORT_SPACE");
                plugin.getTrackerKeeper().getUpgrades().remove(uuid);
                if (tud.getPrevious().getPermission().equals("archive")) {
                    // reset archive use back to 1
                    new ArchiveReset(plugin, uuid.toString(), 1).resetUse();
                }
                return;
            }
        }
        // update player prefs
        HashMap<String, Object> setp = new HashMap<>();
        setp.put("wall", tud.getWall());
        setp.put("floor", tud.getFloor());
        setp.put("lights", tud.getSchematic().getLights().toString());
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid.toString());
        plugin.getQueryFactory().doUpdate("player_prefs", setp, wherep);
        // update TARDIS
        HashMap<String, Object> sett = new HashMap<>();
        sett.put("size", tud.getSchematic().getPermission().toUpperCase(Locale.ROOT));
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        plugin.getQueryFactory().doUpdate("tardis", sett, wheret);
        TARDISCache.invalidate(uuid);
        // take the Artron Energy
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("uuid", uuid.toString());
        String config_path = (archive_next != null) ? "upgrades.archive." + archive_next.getConsoleSize().getConfigPath() : "upgrades." + tud.getSchematic().getPermission().toLowerCase(Locale.ROOT);
        int amount = plugin.getArtronConfig().getInt(config_path);
        TARDISThemeRunnable ttr;
        boolean hasLava = tud.getPrevious().getPermission().equals("master") || tud.getPrevious().getPermission().equals("delta");
        if (tud.getPrevious().equals(tud.getSchematic()) && archive_next == null) {
            // reduce the cost of the theme change
            amount = Math.round((plugin.getArtronConfig().getInt("just_wall_floor") / 100F) * amount);
            ttr = new TARDISWallFloorRunnable(plugin, uuid, tud);
        } else {
            // check for master
            if (hasLava) {
                // remove lava and water
                new TARDISDelavafier(plugin, uuid).swap();
            }
            ttr = new TARDISFullThemeRunnable(plugin, uuid, tud);
        }
        plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, wherea, plugin.getServer().getPlayer(uuid));
        TARDISCache.invalidate(uuid);
        // start the rebuild
        long initial_delay = (hasLava) ? 45L : 5L;
        long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ttr, initial_delay, delay);
        ttr.setTaskID(task);
    }

    private boolean checkARSGrid(ConsoleSize prev, ConsoleSize next, UUID uuid) {
        // get ARS
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetARS rs = new ResultSetARS(plugin, where);
        if (rs.resultSet()) {
            String json = rs.getJson();
            String[][][] grid = TARDISARSMethods.getGridFromJSON(json);
            switch (prev) {
                case SMALL -> {
                    switch (next) {
                        case MEDIUM -> {
                            return (!grid[1][4][5].equals("STONE")
                                    || !grid[1][5][4].equals("STONE")
                                    || !grid[1][5][5].equals("STONE")
                            );
                        }
                        case TALL -> {
                            return (!grid[1][4][5].equals("STONE")
                                    || !grid[1][5][4].equals("STONE")
                                    || !grid[1][5][5].equals("STONE")
                                    || !grid[2][4][4].equals("STONE")
                                    || !grid[2][4][5].equals("STONE")
                                    || !grid[2][5][4].equals("STONE")
                                    || !grid[2][5][5].equals("STONE")
                            );
                        }
                        case WIDE -> {
                            return (!grid[1][4][5].equals("STONE")
                                    || !grid[1][4][6].equals("STONE")
                                    || !grid[1][5][4].equals("STONE")
                                    || !grid[1][5][5].equals("STONE")
                                    || !grid[1][5][6].equals("STONE")
                                    || !grid[1][6][4].equals("STONE")
                                    || !grid[1][6][5].equals("STONE")
                                    || !grid[1][6][6].equals("STONE")
                            );
                        }
                        case MASSIVE -> {
                            return (!grid[1][4][5].equals("STONE")
                                    || !grid[1][4][6].equals("STONE")
                                    || !grid[1][5][4].equals("STONE")
                                    || !grid[1][5][5].equals("STONE")
                                    || !grid[1][5][6].equals("STONE")
                                    || !grid[1][6][4].equals("STONE")
                                    || !grid[1][6][5].equals("STONE")
                                    || !grid[1][6][6].equals("STONE")
                                    || !grid[2][4][4].equals("STONE")
                                    || !grid[2][4][5].equals("STONE")
                                    || !grid[2][4][6].equals("STONE")
                                    || !grid[2][5][4].equals("STONE")
                                    || !grid[2][5][5].equals("STONE")
                                    || !grid[2][5][6].equals("STONE")
                                    || !grid[2][6][4].equals("STONE")
                                    || !grid[2][6][5].equals("STONE")
                                    || !grid[2][6][6].equals("STONE")
                            );
                        }
                        default -> {
                            // same size do nothing
                            return false;
                        }
                    }
                }
                case MEDIUM -> {
                    switch (next) {
                        case TALL -> {
                            return (!grid[2][4][4].equals("STONE")
                                    || !grid[2][4][5].equals("STONE")
                                    || !grid[2][5][4].equals("STONE")
                                    || !grid[2][5][5].equals("STONE")
                            );
                        }
                        case WIDE -> {
                            return (!grid[1][4][6].equals("STONE")
                                    || !grid[1][5][6].equals("STONE")
                                    || !grid[1][6][4].equals("STONE")
                                    || !grid[1][6][5].equals("STONE")
                                    || !grid[1][6][6].equals("STONE")
                            );
                        }
                        case MASSIVE -> {
                            return (!grid[1][4][6].equals("STONE")
                                    || !grid[1][5][6].equals("STONE")
                                    || !grid[1][6][4].equals("STONE")
                                    || !grid[1][6][5].equals("STONE")
                                    || !grid[1][6][6].equals("STONE")
                                    || !grid[2][4][4].equals("STONE")
                                    || !grid[2][4][5].equals("STONE")
                                    || !grid[2][4][6].equals("STONE")
                                    || !grid[2][5][4].equals("STONE")
                                    || !grid[2][5][5].equals("STONE")
                                    || !grid[2][5][6].equals("STONE")
                                    || !grid[2][6][4].equals("STONE")
                                    || !grid[2][6][5].equals("STONE")
                                    || !grid[2][6][6].equals("STONE")
                            );
                        }
                        default -> {
                            // same or smaller size do nothing
                            return false;
                        }
                    }
                }
                case TALL -> {
                    if (next == ConsoleSize.WIDE) {
                        return (!grid[1][4][6].equals("STONE")
                                || !grid[1][5][6].equals("STONE")
                                || !grid[1][6][4].equals("STONE")
                                || !grid[1][6][5].equals("STONE")
                                || !grid[1][6][6].equals("STONE")
                        );
                    }
                    if (next == ConsoleSize.MASSIVE) {
                        return (!grid[1][4][6].equals("STONE")
                                || !grid[1][5][6].equals("STONE")
                                || !grid[1][6][4].equals("STONE")
                                || !grid[1][6][5].equals("STONE")
                                || !grid[1][6][6].equals("STONE")
                                || !grid[2][4][6].equals("STONE")
                                || !grid[2][5][6].equals("STONE")
                                || !grid[2][6][4].equals("STONE")
                                || !grid[2][6][5].equals("STONE")
                                || !grid[2][6][6].equals("STONE")
                        );
                    }
                }
                default -> {
                    // same or smaller size do nothing
                    return false;
                }
            }
        }
        return false;
    }
}
