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
package me.eccentric_nz.TARDIS.travel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

/**
 * The control or console room of the Doctor's TARDIS is the space in which the
 * operation of the craft is usually effected. It is dominated by a large,
 * hexagonal console, typically in or near the middle of the room.
 *
 * @author eccentric_nz
 */
public class TARDISArea {

    private final TARDIS plugin;

    public TARDISArea(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks if a location is contained within any TARDIS area.
     *
     * @param l a location object to check.
     * @return true or false depending on whether the location is within an
     * existing TARDIS area
     */
    public boolean areaCheckInExisting(Location l) {
        boolean chk = true;
        String w = l.getWorld().getName();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("world", w);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, true);
        if (rsa.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsa.getData();
            for (HashMap<String, String> map : data) {
                int minx = TARDISNumberParsers.parseInt(map.get("minx"));
                int minz = TARDISNumberParsers.parseInt(map.get("minz"));
                int maxx = TARDISNumberParsers.parseInt(map.get("maxx"));
                int maxz = TARDISNumberParsers.parseInt(map.get("maxz"));
                // is clicked block within a defined TARDIS area?
                if (l.getX() <= maxx && l.getZ() <= maxz && l.getX() >= minx && l.getZ() >= minz) {
                    chk = false;
                    break;
                }
            }
        }
        return chk;
    }

    /**
     * Checks if a location is contained within a specific TARDIS area.
     *
     * @param a the TARDIS area to check in.
     * @param l a location object to check.
     * @return true or false depending on whether the location is in the
     * specified TARDIS area
     */
    public boolean areaCheckInExile(String a, Location l) {
        boolean chk = true;
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("area_name", a);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, false);
        if (rsa.resultSet()) {
            String w = rsa.getWorld();
            String lw = l.getWorld().getName();
            int minx = rsa.getMinX();
            int minz = rsa.getMinZ();
            int maxx = rsa.getMaxX();
            int maxz = rsa.getMaxZ();
            // is clicked block within a defined TARDIS area?
            if (w.equals(lw) && (l.getX() <= maxx && l.getZ() <= maxz && l.getX() >= minx && l.getZ() >= minz)) {
                chk = false;
            }
        }
        return chk;
    }

    /**
     * Checks if a player has permission to travel to a TARDIS area.
     *
     * @param p a player to check.
     * @param l a location object to check.
     * @return true or false depending on whether the player has permission
     */
    public boolean areaCheckLocPlayer(Player p, Location l) {
        boolean chk = false;
        String w = l.getWorld().getName();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("world", w);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, true);
        if (rsa.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsa.getData();
            for (HashMap<String, String> map : data) {
                String n = map.get("area_name");
                int minx = TARDISNumberParsers.parseInt(map.get("minx"));
                int minz = TARDISNumberParsers.parseInt(map.get("minz"));
                int maxx = TARDISNumberParsers.parseInt(map.get("maxx"));
                int maxz = TARDISNumberParsers.parseInt(map.get("maxz"));
                // is time travel destination within a defined TARDIS area?
                if (l.getX() <= maxx && l.getZ() <= maxz && l.getX() >= minx && l.getZ() >= minz) {
                    // does the player have permmission to travel here
                    if (!p.hasPermission("tardis.area." + n) || !p.isPermissionSet("tardis.area." + n)) {
                        plugin.getTrackerKeeper().getPerm().put(p.getUniqueId(), "tardis.area." + n);
                        chk = true;
                        break;
                    }
                }
            }
        }
        return chk;
    }

    /**
     * Gets the next available parking spot in a specified TARDIS area.
     *
     * @param a the TARDIS area to look in.
     * @return the next free Location in an area
     */
    public Location getNextSpot(String a) {
        Location location = null;
        // find the next available slot in this area
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("area_name", a);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, false);
        if (rsa.resultSet()) {
            int park = rsa.getParkingDistance() + 3;
            int xx, zz = 0;
            int minx = rsa.getMinX();
            int x = minx + 2;
            int minz = rsa.getMinZ();
            int z = minz + 2;
            int maxx = rsa.getMaxX();
            int maxz = rsa.getMaxZ();
            String wStr = rsa.getWorld();
            boolean chk = false;
            // only loop for the size of the TARDIS area
            outerloop:
            for (xx = x; xx <= maxx; xx += park) {
                for (zz = z; zz <= maxz; zz += park) {
                    HashMap<String, Object> wherec = new HashMap<String, Object>();
                    wherec.put("world", wStr);
                    wherec.put("x", xx);
                    wherec.put("z", zz);
                    ResultSetCurrentLocation rs = new ResultSetCurrentLocation(plugin, wherec);
                    if (!rs.resultSet()) {
                        chk = true;
                        break outerloop;
                    }
                }
            }
            if (chk == true) {
                World w = plugin.getServer().getWorld(wStr);
                int y = rsa.getY();
                if (y == 0) {
                    y = w.getHighestBlockYAt(xx, zz);
                }
                location = w.getBlockAt(xx, y, zz).getLocation();
            }
        }
        return location;
    }

    /**
     * Gets the TARDIS area a player is exiled to.
     *
     * @param p a player to check.
     * @return the area the player has been exiled to
     */
    public String getExileArea(Player p) {
        Set<PermissionAttachmentInfo> perms = p.getEffectivePermissions();
        String area = "";
        for (PermissionAttachmentInfo pai : perms) {
            if (pai.getPermission().contains("tardis.area")) {
                int len = pai.getPermission().length();
                area = pai.getPermission().substring(12, len);
            }
        }
        return area;
    }
}
