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

import java.io.File;
import java.util.HashMap;

public class Song {

    private final HashMap<Integer, Layer> layerHashMap;
    private final short songHeight;
    private final short length;
    private final String title;
    private final File path;
    private final String author;
    private final String description;
    private final float speed;
    private final float delay;

    public Song(Song other) {
        speed = other.getSpeed();
        delay = 20 / speed;
        layerHashMap = other.getLayerHashMap();
        songHeight = other.getSongHeight();
        length = other.getLength();
        title = other.getTitle();
        author = other.getAuthor();
        description = other.getDescription();
        path = other.getPath();
    }

    public Song(float speed, HashMap<Integer, Layer> layerHashMap, short songHeight, short length, String title, String author, String description, File path) {
        this.speed = speed;
        delay = 20 / speed;
        this.layerHashMap = layerHashMap;
        this.songHeight = songHeight;
        this.length = length;
        this.title = title;
        this.author = author;
        this.description = description;
        this.path = path;
    }

    public HashMap<Integer, Layer> getLayerHashMap() {
        return layerHashMap;
    }

    private short getSongHeight() {
        return songHeight;
    }

    public short getLength() {
        return length;
    }

    private String getTitle() {
        return title;
    }

    private String getAuthor() {
        return author;
    }

    private File getPath() {
        return path;
    }

    private String getDescription() {
        return description;
    }

    private float getSpeed() {
        return speed;
    }

    public float getDelay() {
        return delay;
    }
}
