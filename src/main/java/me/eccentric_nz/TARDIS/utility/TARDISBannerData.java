/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import com.google.gson.JsonObject;
import org.bukkit.block.data.BlockData;

/**
 * @author eccentric_nz
 */
public class TARDISBannerData {

    private final BlockData data;
    private final JsonObject state;

    public TARDISBannerData(BlockData data, JsonObject state) {
        this.data = data;
        this.state = state;
    }

    public BlockData getData() {
        return data;
    }

    public JsonObject getState() {
        return state;
    }
}
