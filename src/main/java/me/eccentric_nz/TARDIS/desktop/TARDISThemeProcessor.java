/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.rooms.TARDISWallsLookup;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISThemeProcessor {

    private final TARDIS plugin;
    private final UUID uuid;
    private final QueryFactory qf;

    public TARDISThemeProcessor(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.qf = new QueryFactory(this.plugin);
    }

    public void changeDesktop() {
        // get upgrade data
        TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
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
                return;
            }
        }
        // check if there are any rooms that need to be jettisoned
        if (compare(tud.getPrevious(), tud.getSchematic())) {
            // we need more space!
            if (checkARSGrid(tud.getPrevious(), tud.getSchematic(), uuid)) {
                TARDISMessage.send(plugin.getServer().getPlayer(uuid), "UPGRADE_ABORT_SPACE");
                plugin.getTrackerKeeper().getUpgrades().remove(uuid);
                return;
            }
        }
        // update player prefs
        String wall_pref = new TARDISWallsLookup(plugin).wall_lookup.get(tud.getWall());
        String floor_pref = new TARDISWallsLookup(plugin).wall_lookup.get(tud.getFloor());
        HashMap<String, Object> setp = new HashMap<String, Object>();
        setp.put("wall", wall_pref);
        setp.put("floor", floor_pref);
        setp.put("lanterns_on", (tud.getSchematic().getPermission().equals("eleventh") || tud.getSchematic().getPermission().equals("twelfth")) ? 1 : 0);
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("uuid", uuid.toString());
        qf.doUpdate("player_prefs", setp, wherep);
        // update TARDIS
        HashMap<String, Object> sett = new HashMap<String, Object>();
        sett.put("size", tud.getSchematic().getPermission().toUpperCase());
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("uuid", uuid.toString());
        qf.doUpdate("tardis", sett, wheret);
        // take the Artron Energy
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("uuid", uuid.toString());
        int amount = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().getPermission().toLowerCase());
        TARDISThemeRunnable ttr;
        if (tud.getPrevious().equals(tud.getSchematic())) {
            // reduce the cost of the theme change
            amount = Math.round((plugin.getArtronConfig().getInt("just_wall_floor") / 100F) * amount);
            ttr = new TARDISWallFloorRunnable(plugin, uuid, tud);
        } else {
            ttr = new TARDISFullThemeRunnable(plugin, uuid, tud);
        }
        qf.alterEnergyLevel("tardis", -amount, wherea, plugin.getServer().getPlayer(uuid));
        // start the rebuild
        long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ttr, 5L, delay);
        ttr.setTaskID(task);
    }

    private boolean compare(SCHEMATIC prev, SCHEMATIC next) {
        return (!prev.equals(next) && ((prev.isSmall() && !next.isSmall()) || (!prev.isTall() && next.isTall())));
    }

    private boolean checkARSGrid(SCHEMATIC prev, SCHEMATIC next, UUID uuid) {
        // get ARS
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetARS rs = new ResultSetARS(plugin, where);
        if (rs.resultSet()) {
            String json = rs.getJson();
            int[][][] grid = TARDISARSMethods.getGridFromJSON(json);
            if (prev.getPermission().equals("ars") || prev.getPermission().equals("budget") || prev.getPermission().equals("plank") || prev.getPermission().equals("steampunk") || prev.getPermission().equals("tom") || prev.getPermission().equals("war") || prev.getPermission().equals("pyramid")) {
                if (next.getPermission().equals("bigger") || next.getPermission().equals("redstone") || next.getPermission().equals("twelfth")) {
                    return (grid[1][4][5] != 1 || grid[1][5][4] != 1 || grid[1][5][5] != 1);
                } else if (next.getPermission().equals("deluxe") || next.getPermission().equals("eleventh") || next.getPermission().equals("master")) {
                    return (grid[1][4][5] != 1 || grid[1][5][4] != 1 || grid[1][5][5] != 1 || grid[2][4][4] != 1 || grid[2][4][5] != 1 || grid[2][5][4] != 1 || grid[2][5][5] != 1);
                } else {
                    return false;
                }
            } else if (prev.getPermission().equals("bigger") || prev.getPermission().equals("redstone") || prev.getPermission().equals("twelfth")) {
                if (next.getPermission().equals("deluxe") || next.getPermission().equals("eleventh") || next.getPermission().equals("master")) {
                    return (grid[2][4][4] != 1 || grid[2][4][5] != 1 || grid[2][5][4] != 1 || grid[2][5][5] != 1);
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
