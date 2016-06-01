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
package me.eccentric_nz.TARDIS.artron;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.desktop.TARDISBlockScannerData;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeBlockScanner;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBeaconToggler {

    private final TARDIS plugin;

    public TARDISBeaconToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void flickSwitch(UUID uuid, boolean on) {
        HashMap<String, Object> whereb = new HashMap<String, Object>();
        whereb.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, whereb, "", false);
        if (rs.resultSet()) {
            SCHEMATIC schm = rs.getSchematic();
            if (CONSOLES.getNO_BEACON().contains(schm)) {
                // doesn't have a beacon!
                return;
            }
            // toggle beacon
            String beacon = rs.getBeacon();
            if (!beacon.isEmpty()) {
                String[] beaconData = beacon.split(":");
                if (beaconData.length > 1) {
                    World w = plugin.getServer().getWorld(beaconData[0]);
                    int bx = TARDISNumberParsers.parseInt(beaconData[1]);
                    int by = TARDISNumberParsers.parseInt(beaconData[2]);
                    int bz = TARDISNumberParsers.parseInt(beaconData[3]);
                    Location bl = new Location(w, bx, by, bz);
                    Block b = bl.getBlock();
                    while (!b.getChunk().isLoaded()) {
                        b.getChunk().load();
                    }
                    b.setType((on) ? Material.GLASS : Material.REDSTONE_BLOCK);
                    if (!plugin.getGeneralKeeper().getProtectBlockMap().containsKey(bl.toString())) {
                        plugin.getGeneralKeeper().getProtectBlockMap().put(bl.toString(), rs.getTardis_id());
                    }
                } else {
                    this.updateBeacon(schm, uuid);
                }
            } else {
                this.updateBeacon(schm, uuid);
            }
        }
    }

    private void updateBeacon(SCHEMATIC schm, UUID uuid) {
        // determine beacon location and update the tardis table so we don't have to do this again
        TARDISUpgradeData tud = new TARDISUpgradeData();
        tud.setSchematic(schm);
        tud.setPrevious(schm);
        tud.setWall("WOOL:1");
        tud.setFloor("WOOL:8");
        TARDISUpgradeBlockScanner scanner = new TARDISUpgradeBlockScanner(plugin, tud, uuid);
        TARDISBlockScannerData check = scanner.check();
        if (!check.getBeacon().isEmpty()) {
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("beacon", check.getBeacon());
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", uuid.toString());
            new QueryFactory(plugin).doUpdate("tardis", set, where);
        }
    }
}
