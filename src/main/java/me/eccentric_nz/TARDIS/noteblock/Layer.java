/*
 * Copyright (C) 2018 michidk && xxmicloxx
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

class Layer {

    private final HashMap<Integer, Note> hashMap = new HashMap<>();
    private byte volume = 100;
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Note getNote(int tick) {
        return hashMap.get(tick);
    }

    void setNote(int tick, Note note) {
        hashMap.put(tick, note);
    }

    byte getVolume() {
        return volume;
    }

    void setVolume(byte volume) {
        this.volume = volume;
    }
}
