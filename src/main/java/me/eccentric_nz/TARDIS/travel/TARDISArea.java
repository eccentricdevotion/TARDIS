/*
 * Copyright (C) 2012 eccentric_nz
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

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetSave;
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

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISArea(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks if a location is contained within a any TARDIS area.
     *
     * @param l a location object to check.
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
                int minx = plugin.utils.parseNum(map.get("minx"));
                int minz = plugin.utils.parseNum(map.get("minz"));
                int maxx = plugin.utils.parseNum(map.get("maxx"));
                int maxz = plugin.utils.parseNum(map.get("maxz"));
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
     */
    public boolean areaCheckInExile(String a, Location l) {
        boolean chk = true;
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("area_name", a);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, false);
        if (rsa.resultSet()) {
            String w = rsa.getWorld();
            String lw = l.getWorld().getName();
            int minx = rsa.getMinx();
            int minz = rsa.getMinz();
            int maxx = rsa.getMaxx();
            int maxz = rsa.getMaxz();
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
     */
    public boolean areaCheckLocPlayer(Player p, Location l) {
        boolean chk = false;
        String w = l.getWorld().getName();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("world", w);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, true);
        int i = 1;
        if (rsa.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsa.getData();
            for (HashMap<String, String> map : data) {
                String n = map.get("area_name");
                int minx = plugin.utils.parseNum(map.get("minx"));
                int minz = plugin.utils.parseNum(map.get("minz"));
                int maxx = plugin.utils.parseNum(map.get("maxx"));
                int maxz = plugin.utils.parseNum(map.get("maxz"));
                // is time travel destination within a defined TARDIS area?
                if (l.getX() <= maxx && l.getZ() <= maxz && l.getX() >= minx && l.getZ() >= minz) {
                    // does the player have permmission to travel here
                    if (!p.hasPermission("tardis.area." + n) || !p.isPermissionSet("tardis.area." + n)) {
                        plugin.trackPerm.put(p.getName(), "tardis.area." + n);
                        chk = true;
                        break;
                    }
                }
                i++;
            }
        }
        return chk;
    }

    /**
     * Gets the next available parking spot in a specified TARDIS area.
     *
     * @param a the TARDIS area to look in.
     */
    public Location getNextSpot(String a) {
        Location location = null;
        // find the next available slot in this area
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("area_name", a);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, false);
        if (rsa.resultSet()) {
            int minx = rsa.getMinx();
            int x = minx + 2;
            int minz = rsa.getMinz();
            int z = minz + 2;
            int maxx = rsa.getMaxx();
            int maxz = rsa.getMaxz();
            String wStr = rsa.getWorld();
            boolean chk = false;
            while (chk == false) {
                String queryLoc = wStr + ":" + x + ":%:" + z;
                ResultSetSave rs = new ResultSetSave(plugin, queryLoc);
                if (rs.resultSet()) {
                    if (x <= maxx) {
                        x += 5;
                    } else {
                        x = minx + 2;
                        if (z <= maxz) {
                            z += 5;
                        } else {
                            z = minz + 2;
                        }
                    }
                } else {
                    chk = true;
                    break;
                }
            }
            if (chk == true) {
                World w = plugin.getServer().getWorld(wStr);
                int y = w.getHighestBlockYAt(x, z);
                location = w.getBlockAt(x, y, z).getLocation();
            }
        }
        return location;
    }

    /**
     * Gets the TARDIS area a player is exiled to.
     *
     * @param p a player to check.
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