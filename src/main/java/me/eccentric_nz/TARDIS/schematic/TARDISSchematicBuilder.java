/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic;

import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISSchematicBuilder {

    private final TARDIS plugin;
    private final World w;
    private final int id, sx, ex, sy, ey, sz, ez;
    private final int[] controls = {0, 2, 3, 4, 5};
    private Location h;

    public TARDISSchematicBuilder(TARDIS plugin, int id, World w, int sx, int ex, int sy, int ey, int sz, int ez) {
        this.plugin = plugin;
        this.id = id;
        this.w = w;
        this.sx = sx;
        this.ex = ex;
        this.sy = sy;
        this.ey = ey;
        this.sz = sz;
        this.ez = ez;
    }

    public ArchiveData build() {
        boolean ars = true;
        // get locations of controls first and compare their coords...
        HashMap<Integer, Location> map = new HashMap<>();
        for (int c : controls) {
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", id);
            whereh.put("type", c);
            ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
            if (rsc.resultSet()) {
                Location location = TARDISStaticLocationGetters.getLocationFromDB(rsc.getLocation());
                switch (c) {
                    case 2:
                        // world repeater
                        map.put(c, location);
                        break;
                    case 3:
                        // x repeater
                        map.put(c, location);
                        break;
                    case 4:
                        // z repeater
                        map.put(c, location);
                        break;
                    case 5:
                        // distance multiplier
                        map.put(c, location);
                        break;
                    default:
                        // handbrake
                        h = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
                        break;
                }
            }
        }
        // also find the beacon location...
        HashMap<String, Object> whereb = new HashMap<>();
        whereb.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, whereb, "", false, 2);
        int bx = 0, by = 0, bz = 0, cx = 0, cy = 0, cz = 0;
        if (rs.resultSet()) {
            String[] split = rs.getTardis().getBeacon().split(":");
            bx = TARDISNumberParsers.parseInt(split[1]);
            by = TARDISNumberParsers.parseInt(split[2]);
            bz = TARDISNumberParsers.parseInt(split[3]);
            // and the creeper location...
            String[] csplit = rs.getTardis().getCreeper().split(":");
            cx = TARDISNumberParsers.parseInt(csplit[1].substring(0, csplit[1].length() - 2));
            cy = TARDISNumberParsers.parseInt(csplit[2]);
            cz = TARDISNumberParsers.parseInt(csplit[3].substring(0, csplit[3].length() - 2));
        }

        // get the min & max coords
        int minx = (sx < ex) ? sx : ex;
        int maxx = (sx < ex) ? ex : sx;
        int miny = (sy < ey) ? sy : ey;
        int maxy = (sy < ey) ? ey : sy;
        int minz = (sz < ez) ? sz : ez;
        int maxz = (sz < ez) ? ez : sz;
        // create a JSON object for relative position
        JSONObject relative = new JSONObject();
        relative.put("x", maxx);
        relative.put("y", miny);
        relative.put("z", minz - 1);
        // create a JSON object for dimensions
        JSONObject dimensions = new JSONObject();
        int width = (maxx - minx) + 1;
        int height = (maxy - miny) + 1;
        int length = (maxz - minz) + 1;
        dimensions.put("width", width);
        dimensions.put("height", height);
        dimensions.put("length", length);
        // create JSON arrays for block data
        JSONArray levels = new JSONArray();
        int f = 2;
        int beacon = 0;
        // loop through the blocks inside this cube
        for (int l = miny; l <= maxy; l++) {
            JSONArray rows = new JSONArray();
            for (int r = minx; r <= maxx; r++) {
                JSONArray columns = new JSONArray();
                for (int c = minz; c <= maxz; c++) {
                    JSONObject obj = new JSONObject();
                    Block b = w.getBlockAt(r, l, c);
                    BlockData data = b.getBlockData();
                    Material m = data.getMaterial();
                    // set ARS block
                    if (ars && m.isAir()) {
                        data = Material.INFESTED_COBBLESTONE.createBlockData();
                        ars = false;
                    }
                    switch (m) {
                        case REPEATER:
                            // random location blocks
                            if (isControlBlock(map.get(f), w, r, l, c)) {
                                MultipleFacing mushroom = (MultipleFacing) Material.MUSHROOM_STEM.createBlockData();
                                mushroom.setFace(BlockFace.DOWN, true);
                                mushroom.setFace(BlockFace.EAST, true);
                                mushroom.setFace(BlockFace.NORTH, true);
                                mushroom.setFace(BlockFace.SOUTH, true);
                                mushroom.setFace(BlockFace.UP, true);
                                mushroom.setFace(BlockFace.WEST, true);
                                data = mushroom;
                                f++;
                            }
                            break;
                        case LEVER:
                            // handbrake
                            if (isControlBlock(h, w, r, l, c)) {
                                data = Material.CAKE.createBlockData();
                            }
                            break;
                        default:
                            break;
                    }
                    if (l == by && r == bx && c == bz) {
                        data = Material.BEDROCK.createBlockData();
                    }
                    if (l == cy && r == cx && c == cz) {
                        data = (m.equals(Material.BEACON)) ? Material.BEACON.createBlockData() : Material.COMMAND_BLOCK.createBlockData();
                        beacon = (m.equals(Material.BEACON)) ? 1 : 0;
                    }
                    obj.put("data", data.getAsString());
                    // banners
                    if (TARDISStaticUtils.isBanner(m)) {
                        JSONObject state = new JSONObject();
                        Banner banner = (Banner) b.getState();
                        state.put("colour", banner.getBaseColor().toString());
                        JSONArray patterns = new JSONArray();
                        if (banner.numberOfPatterns() > 0) {
                            banner.getPatterns().forEach((p) -> {
                                JSONObject pattern = new JSONObject();
                                pattern.put("pattern", p.getPattern().toString());
                                pattern.put("pattern_colour", p.getColor().toString());
                                patterns.put(pattern);
                            });
                        }
                        state.put("patterns", patterns);
                        obj.put("banner", state);
                    }
                    columns.put(obj);
                }
                rows.put(columns);
            }
            levels.put(rows);
        }
        JSONObject schematic = new JSONObject();
        schematic.put("relative", relative);
        schematic.put("dimensions", dimensions);
        schematic.put("input", levels);
        return new ArchiveData(schematic, beacon);
    }

    private boolean isControlBlock(Location l, World w, int x, int y, int z) {
        Location n = new Location(w, x, y, z);
        return (n.equals(l));
    }

    public static class ArchiveData {

        private final JSONObject JSON;
        private final int beacon;

        ArchiveData(JSONObject JSON, int beacon) {
            this.JSON = JSON;
            this.beacon = beacon;
        }

        public JSONObject getJSON() {
            return JSON;
        }

        public int getBeacon() {
            return beacon;
        }
    }
}
