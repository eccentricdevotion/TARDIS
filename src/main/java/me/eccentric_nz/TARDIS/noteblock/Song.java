/*
 * Copyright (C) 2018  michidk && xxmicloxx
 * http://dev.bukkit.org/bukkit-plugins/noteblockapi/
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
package me.eccentric_nz.TARDIS.noteblock;

import java.util.HashMap;

public class Song {

    private final HashMap<Integer, Layer> layerHashMap;
    private final short length;
    private final float delay;

    Song(float speed, HashMap<Integer, Layer> layerHashMap, short length) {
        delay = 20 / speed;
        this.layerHashMap = layerHashMap;
        this.length = length;
    }

    HashMap<Integer, Layer> getLayerHashMap() {
        return layerHashMap;
    }

    public short getLength() {
        return length;
    }

    public float getDelay() {
        return delay;
    }
}
