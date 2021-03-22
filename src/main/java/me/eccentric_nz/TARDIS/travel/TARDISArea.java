/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.HashMap;
import java.util.Set;

/**
 * The control or console room of the Doctor's TARDIS is the space in which the operation of the craft is usually
 * effected. It is dominated by a large, hexagonal console, typically in or near the middle of the room.
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
     * @return true or false depending on whether the location is within an existing TARDIS area
     */
    public boolean areaCheckInExisting(Location l) {
        boolean chk = true;
        String w = l.getWorld().getName();
        HashMap<String, Object> where = new HashMap<>();
        where.put("world", w);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, true, false);
        if (rsa.resultSet()) {
            for (Area a : rsa.getData()) {
                // is clicked block within a defined TARDIS area?
                if (l.getX() <= a.getMaxX() && l.getZ() <= a.getMaxZ() && l.getX() >= a.getMinX() && l.getZ() >= a.getMinZ()) {
                    chk = false;
                    break;
                }
            }
        }
        return chk;
    }

    /**
     * Checks if a location is contained within any TARDIS area.
     *
     * @param l a location object to check.
     * @return a TARDISAreaCheck &lt;Area, Boolean&gt; with values dependent on whether the location is within an
     * existing TARDIS area
     */
    public TARDISAreaCheck areaCheckInExistingArea(Location l) {
        String w = l.getWorld().getName();
        HashMap<String, Object> where = new HashMap<>();
        where.put("world", w);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, true, false);
        if (rsa.resultSet()) {
            for (Area a : rsa.getData()) {
                // is clicked block within a defined TARDIS area?
                if (l.getX() <= a.getMaxX() && l.getZ() <= a.getMaxZ() && l.getX() >= a.getMinX() && l.getZ() >= a.getMinZ()) {
                    return new TARDISAreaCheck(a, true);
                }
            }
        }
        return new TARDISAreaCheck(null, false);
    }

    /**
     * Checks if a location is contained within a specific TARDIS area.
     *
     * @param area the TARDIS area to check in.
     * @param l    a location object to check.
     * @return true or false depending on whether the location is in the specified TARDIS area
     */
    public boolean areaCheckInExile(String area, Location l) {
        boolean chk = true;
        HashMap<String, Object> where = new HashMap<>();
        where.put("area_name", area);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, false, false);
        if (rsa.resultSet()) {
            Area a = rsa.getArea();
            String lw = l.getWorld().getName();
            // is clicked block within a defined TARDIS area?
            if (a.getWorld().equals(lw) && (l.getX() <= a.getMaxX() && l.getZ() <= a.getMaxZ() && l.getX() >= a.getMinX() && l.getZ() >= a.getMinZ())) {
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
        HashMap<String, Object> where = new HashMap<>();
        where.put("world", w);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, true, false);
        if (rsa.resultSet()) {
            for (Area a : rsa.getData()) {
                String n = a.getAreaName();
                // is time travel destination within a defined TARDIS area?
                if (l.getX() <= a.getMaxX() && l.getZ() <= a.getMaxZ() && l.getX() >= a.getMinX() && l.getZ() >= a.getMinZ()) {
                    // does the player have permission to travel here
                    if (!TARDISPermission.hasPermission(p, "tardis.area." + n) || !p.isPermissionSet("tardis.area." + n)) {
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
     * @param area the TARDIS area to look in.
     * @return the next free Location in an area
     */
    public Location getNextSpot(String area) {
        Location location = null;
        // find the next available slot in this area
        HashMap<String, Object> where = new HashMap<>();
        where.put("area_name", area);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, false, false);
        if (rsa.resultSet()) {
            Area a = rsa.getArea();
            int park = a.getParkingDistance() + 3;
            int xx, zz = 0;
            int minx = a.getMinX();
            int x = minx + 2;
            int minz = a.getMinZ();
            int z = minz + 2;
            int maxx = a.getMaxX();
            int maxz = a.getMaxZ();
            String wStr = a.getWorld();
            boolean chk = false;
            // only loop for the size of the TARDIS area
            outerloop:
            for (xx = x; xx <= maxx; xx += park) {
                for (zz = z; zz <= maxz; zz += park) {
                    HashMap<String, Object> wherec = new HashMap<>();
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
            if (chk) {
                World w = plugin.getServer().getWorld(wStr);
                if (w != null) {
                    int y = a.getY();
                    if (y == 0) {
                        y = w.getHighestBlockYAt(xx, zz) + 1;
                    }
                    location = w.getBlockAt(xx, y, zz).getLocation();
                }
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
