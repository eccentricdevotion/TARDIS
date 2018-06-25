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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.schematic.ArchiveReset;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISThemeProcessor {

    private final TARDIS plugin;
    private final UUID uuid;
    private final QueryFactory qf;
    private Archive archive_next;
    private Archive archive_prev;

    public TARDISThemeProcessor(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        qf = new QueryFactory(this.plugin);
    }

    public void changeDesktop() {
        // get upgrade data
        TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
        // get Archive if nescessary
        if (tud.getSchematic().getPermission().equals("archive")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            where.put("use", 1);
            ResultSetArchive rs = new ResultSetArchive(plugin, where);
            if (rs.resultSet()) {
                archive_next = rs.getArchive();
            } else {
                // abort
                Player cp = plugin.getServer().getPlayer(uuid);
                TARDISMessage.send(cp, "ARCHIVE_NOT_FOUND");
                return;
            }
        }
        if (tud.getPrevious().getPermission().equals("archive")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            where.put("use", 2);
            ResultSetArchive rs = new ResultSetArchive(plugin, where);
            if (rs.resultSet()) {
                archive_prev = rs.getArchive();
            } else {
                // abort
                Player cp = plugin.getServer().getPlayer(uuid);
                TARDISMessage.send(cp, "ARCHIVE_NOT_FOUND");
                return;
            }
        }
        // if configured check whether there are still any blocks left
        if (plugin.getConfig().getBoolean("desktop.check_blocks_before_upgrade")) {
            TARDISUpgradeBlockScanner scanner = new TARDISUpgradeBlockScanner(plugin, tud, uuid);
            TARDISBlockScannerData check = scanner.check();
            if (check == null) {
                return;
            } else if (!check.allow()) {
                Player cp = plugin.getServer().getPlayer(uuid);
                TARDISMessage.send(cp, "UPGRADE_PERCENT_BLOCKS", plugin.getConfig().getInt("desktop.block_change_percent") + "");
                TARDISMessage.send(cp, "UPGRADE_PERCENT_EXPLAIN", check.getCount() + "", check.getVolume() + "", check.getChanged() + "");
                TARDISMessage.send(cp, "UPGRADE_PERCENT_REASON");
                if (tud.getPrevious().getPermission().equals("archive")) {
                    // reset archive use back to 1
                    new ArchiveReset(plugin, uuid.toString(), 1).resetUse();
                }
                return;
            }
        }
        // check if there are any rooms that need to be jettisoned
        if (compare(tud.getPrevious(), tud.getSchematic())) {
            // we need more space!
            if (checkARSGrid(tud.getPrevious(), tud.getSchematic(), uuid)) {
                TARDISMessage.send(plugin.getServer().getPlayer(uuid), "UPGRADE_ABORT_SPACE");
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
        setp.put("lanterns_on", (tud.getSchematic().hasLanterns()) ? 1 : 0);
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid.toString());
        qf.doUpdate("player_prefs", setp, wherep);
        // update TARDIS
        HashMap<String, Object> sett = new HashMap<>();
        sett.put("size", tud.getSchematic().getPermission().toUpperCase(Locale.ENGLISH));
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        qf.doUpdate("tardis", sett, wheret);
        // take the Artron Energy
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("uuid", uuid.toString());
        String config_path = (archive_next != null) ? "upgrades.archive." + archive_next.getConsoleSize().getConfigPath() : "upgrades." + tud.getSchematic().getPermission().toLowerCase(Locale.ENGLISH);
        int amount = plugin.getArtronConfig().getInt(config_path);
        TARDISThemeRunnable ttr;
        boolean master = tud.getPrevious().getPermission().equals("master");
        if (tud.getPrevious().equals(tud.getSchematic()) && archive_next == null) {
            // reduce the cost of the theme change
            amount = Math.round((plugin.getArtronConfig().getInt("just_wall_floor") / 100F) * amount);
            ttr = new TARDISWallFloorRunnable(plugin, uuid, tud);
        } else {
            // check for master
            if (master) {
                // remove lava and water
                new TARDISDelavafier(plugin, uuid).swap();
            }
            ttr = new TARDISFullThemeRunnable(plugin, uuid, tud);
        }
        qf.alterEnergyLevel("tardis", -amount, wherea, plugin.getServer().getPlayer(uuid));
        // start the rebuild
        long initial_delay = (master) ? 45L : 5L;
        long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ttr, initial_delay, delay);
        ttr.setTaskID(task);
    }

    private boolean compare(SCHEMATIC prev, SCHEMATIC next) {
        // special case for archives
        if (archive_next != null && archive_prev == null) {
            return (!prev.getPermission().equals(archive_next.getName()) && ((prev.getConsoleSize().equals(ConsoleSize.SMALL) && !archive_next.getConsoleSize().equals(ConsoleSize.SMALL)) || (!prev.getConsoleSize().equals(ConsoleSize.TALL) && archive_next.getConsoleSize().equals(ConsoleSize.TALL))));
        } else if (archive_next == null && archive_prev != null) {
            return (!archive_prev.getName().equals(next.getPermission()) && ((archive_prev.getConsoleSize().equals(ConsoleSize.SMALL) && !next.getConsoleSize().equals(ConsoleSize.SMALL)) || (!archive_prev.getConsoleSize().equals(ConsoleSize.TALL) && next.getConsoleSize().equals(ConsoleSize.TALL))));
        } else if (archive_next != null && archive_prev != null) {
            return (!archive_prev.getName().equals(archive_next.getName()) && ((archive_prev.getConsoleSize().equals(ConsoleSize.SMALL) && !archive_next.getConsoleSize().equals(ConsoleSize.SMALL)) || (!archive_prev.getConsoleSize().equals(ConsoleSize.TALL) && archive_next.getConsoleSize().equals(ConsoleSize.TALL))));
        } else {
            return (!prev.equals(next) && ((prev.getConsoleSize().equals(ConsoleSize.SMALL) && !next.getConsoleSize().equals(ConsoleSize.SMALL)) || (!prev.getConsoleSize().equals(ConsoleSize.TALL) && next.getConsoleSize().equals(ConsoleSize.TALL))));
        }
    }

    private boolean checkARSGrid(SCHEMATIC prev, SCHEMATIC next, UUID uuid) {
        // get ARS
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetARS rs = new ResultSetARS(plugin, where);
        if (rs.resultSet()) {
            String json = rs.getJson();
            String[][][] grid = TARDISARSMethods.getGridFromJSON(json);
            if (prev.getConsoleSize().equals(ConsoleSize.SMALL) || (archive_prev != null && archive_prev.getConsoleSize().equals(ConsoleSize.SMALL))) {
                if (next.getConsoleSize().equals(ConsoleSize.MEDIUM) || (archive_next != null && archive_next.getConsoleSize().equals(ConsoleSize.MEDIUM))) {
                    return (!grid[1][4][5].equals("STONE") || !grid[1][5][4].equals("STONE") || !grid[1][5][5].equals("STONE"));
                } else if (next.getConsoleSize().equals(ConsoleSize.TALL) || (archive_next != null && archive_next.getConsoleSize().equals(ConsoleSize.TALL))) {
                    return (!grid[1][4][5].equals("STONE") || !grid[1][5][4].equals("STONE") || !grid[1][5][5].equals("STONE") || !grid[2][4][4].equals("STONE") || !grid[2][4][5].equals("STONE") || !grid[2][5][4].equals("STONE") || !grid[2][5][5].equals("STONE"));
                } else {
                    return false;
                }
            } else if (prev.getConsoleSize().equals(ConsoleSize.MEDIUM) || (archive_prev != null && archive_next.getConsoleSize().equals(ConsoleSize.MEDIUM))) {
                if (next.getConsoleSize().equals(ConsoleSize.TALL) || (archive_next != null && archive_next.getConsoleSize().equals(ConsoleSize.TALL))) {
                    return (!grid[2][4][4].equals("STONE") || !grid[2][4][5].equals("STONE") || !grid[2][5][4].equals("STONE") || !grid[2][5][5].equals("STONE"));
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }
}
