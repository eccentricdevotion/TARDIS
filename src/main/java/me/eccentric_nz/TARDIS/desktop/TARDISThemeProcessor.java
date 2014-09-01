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
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("uuid", uuid.toString());
        qf.doUpdate("player_prefs", setp, wherep);
        // update TARDIS
        HashMap<String, Object> sett = new HashMap<String, Object>();
        sett.put("size", tud.getSchematic().name());
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("uuid", uuid.toString());
        qf.doUpdate("tardis", sett, wheret);
        // take the Artron Energy
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("uuid", uuid.toString());
        int amount = plugin.getArtronConfig().getInt("upgrades." + tud.getSchematic().name().toLowerCase());
        qf.alterEnergyLevel("tardis", -amount, wherea, plugin.getServer().getPlayer(uuid));
        // start the rebuild
        TARDISThemeRunnable ttr = new TARDISThemeRunnable(plugin, uuid, tud);
        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, ttr, 5L, 2L);
        ttr.setTaskID(task);
    }

    private boolean compare(SCHEMATIC prev, SCHEMATIC next) {
        return (!prev.equals(next) && (prev.isSmall() && !next.isSmall() || !prev.isTall() && next.isTall()));
    }

    private boolean checkARSGrid(SCHEMATIC prev, SCHEMATIC next, UUID uuid) {
        // get ARS
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetARS rs = new ResultSetARS(plugin, where);
        if (rs.resultSet()) {
            String json = rs.getJson();
            int[][][] grid = TARDISARSMethods.getGridFromJSON(json);
            switch (prev) {
                case ARS:
                case BUDGET:
                case PLANK:
                case STEAMPUNK:
                case TOM:
                case WAR:
                    switch (next) {
                        case BIGGER:
                        case REDSTONE:
                            return (grid[1][4][5] != 1 || grid[1][5][4] != 1 || grid[1][5][5] != 1);
                        case DELUXE:
                        case ELEVENTH:
                            return (grid[1][4][5] != 1 || grid[1][5][4] != 1 || grid[1][5][5] != 1 || grid[2][4][4] != 1 || grid[2][4][5] != 1 || grid[2][5][4] != 1 || grid[2][5][5] != 1);
                        default:
                            return false;
                    }
                case BIGGER:
                case REDSTONE:
                    switch (next) {
                        case DELUXE:
                        case ELEVENTH:
                            return (grid[2][4][4] != 1 || grid[2][4][5] != 1 || grid[2][5][4] != 1 || grid[2][5][5] != 1);
                        default:
                            return false;
                    }
                default:
                    return false;
            }
        }
        return false;
    }
}
