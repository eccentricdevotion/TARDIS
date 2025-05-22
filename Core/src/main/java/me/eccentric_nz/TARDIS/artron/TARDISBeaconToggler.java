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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.desktop.TARDISBlockScannerData;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeBlockScanner;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISBeaconToggler {

    private final TARDIS plugin;

    public TARDISBeaconToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void flickSwitch(UUID uuid, int id, boolean on) {
//        HashMap<String, Object> whereb = new HashMap<>();
//        whereb.put("tardis_id", id);
//        ResultSetTardis rs = new ResultSetTardis(plugin, whereb, "", false, 2);
//        if (rs.resultSet()) {
//            Tardis tardis = rs.getTardis();
        Tardis tardis = TARDISCache.BY_ID.get(id);
        if (tardis != null) {
            Schematic schm = tardis.getSchematic();
            if (Consoles.getNO_BEACON().contains(schm)) {
                // doesn't have a beacon!
                return;
            }
            // toggle beacon
            String beacon = tardis.getBeacon();
            if (!beacon.isEmpty()) {
                String[] beaconData = beacon.split(":");
                if (beaconData.length > 1) {
                    Location bl = TARDISStaticLocationGetters.getLocationFromDB(beacon);
                    Block b = bl.getBlock();
                    while (!b.getChunk().isLoaded()) {
                        b.getChunk().load();
                    }
                    b.setBlockData((on) ? TARDISConstants.GLASS : TARDISConstants.POWER);
                    if (!plugin.getGeneralKeeper().getProtectBlockMap().containsKey(bl.toString())) {
                        plugin.getGeneralKeeper().getProtectBlockMap().put(bl.toString(), tardis.getTardisId());
                    }
                } else {
                    updateBeacon(schm, uuid);
                }
            } else {
                updateBeacon(schm, uuid);
            }
        }
    }

    private void updateBeacon(Schematic schm, UUID uuid) {
        // determine beacon location and update the tardis table so we don't have to do this again
        TARDISUpgradeData tud = new TARDISUpgradeData();
        tud.setSchematic(schm);
        tud.setPrevious(schm);
        tud.setWall("ORANGE_WOOL");
        tud.setFloor("LIGHT_GRAY_WOOL");
        TARDISUpgradeBlockScanner scanner = new TARDISUpgradeBlockScanner(plugin, tud, uuid);
        TARDISBlockScannerData check = scanner.check();
        if (!check.getBeacon().isEmpty()) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("beacon", check.getBeacon());
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            plugin.getQueryFactory().doUpdate("tardis", set, where);
        }
    }
}
