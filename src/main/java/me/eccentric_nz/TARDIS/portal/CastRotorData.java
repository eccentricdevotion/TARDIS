/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.portal;

import org.bukkit.entity.ItemFrame;
import org.bukkit.util.Vector;

public class CastRotorData {

    final ItemFrame frame;
    final Vector offset;

    public CastRotorData(ItemFrame frame, Vector offset) {
        this.frame = frame;
        this.offset = offset;
    }

    public ItemFrame getFrame() {
        return frame;
    }

    public Vector getOffset() {
        return offset;
    }
}
