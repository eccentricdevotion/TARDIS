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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.rooms;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.Location;

/**
 *
 * @author eccentric_nz
 */
public class TARDISPassage {

    private final TARDIS plugin;
    private Location l;
    private int middle_id;
    private byte middle_data;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISPassage(TARDIS plugin, Location l, int middle_id, byte middle_data) {
        this.plugin = plugin;
        this.l = l;
        this.middle_id = middle_id;
        this.middle_data = middle_data;
    }

    public void passage() {
        String[][][] s;
        short h, w, l;
        s = plugin.passageschematic;
        h = plugin.passagedimensions[0];
        w = plugin.passagedimensions[1];
        l = plugin.passagedimensions[2];
        int level, row, col, id, x, y, z, startx, starty = 15, startz, resetx, resetz;
        String tmp;
        byte data;
        for (level = 0; level < h; level++) {
            for (row = 0; row < w; row++) {
                for (col = 0; col < l; col++) {
                    tmp = s[level][row][col];
                    if (!tmp.equals("0:0")) {
                        if (tmp.contains(":")) {
                            String[] iddata = tmp.split(":");
                            id = plugin.utils.parseNum(iddata[0]);
                            data = Byte.parseByte(iddata[1]);
                            if (id == 35 && data == 1) {
                                switch (middle_id) {
                                    case 22:
                                        break;
                                    default:
                                        id = middle_id;
                                        data = middle_data;
                                }
                            }
                        } else {
                            id = plugin.utils.parseNum(tmp);
                            data = 0;
                        }

                    }
                    startx += x;
                }
                startx = resetx;
                startz += z;
            }
            startz = resetz;
            starty += 1;
        }
    }
}