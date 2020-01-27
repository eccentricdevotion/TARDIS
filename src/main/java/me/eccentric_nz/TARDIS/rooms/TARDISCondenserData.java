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
package me.eccentric_nz.TARDIS.rooms;

import java.util.HashMap;

/**
 * Store required block data for growing rooms. Used to debit the condenser table when the room is grown.
 *
 * @author eccentric_nz
 */
public class TARDISCondenserData {

    private HashMap<String, Integer> blockIDCount = new HashMap<>();
    private int tardis_id;

    public TARDISCondenserData() {
    }

    public HashMap<String, Integer> getBlockIDCount() {
        return blockIDCount;
    }

    public void setBlockIDCount(HashMap<String, Integer> blockIDCount) {
        this.blockIDCount = blockIDCount;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public void setTardis_id(int tardis_id) {
        this.tardis_id = tardis_id;
    }
}
