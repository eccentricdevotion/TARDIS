/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.JSON.JSONObject;
import org.bukkit.Material;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBannerData {

    private final Material material;
    private final JSONObject state;

    public TARDISBannerData(Material material, JSONObject state) {
        this.material = material;
        this.state = state;
    }

    public Material getMaterial() {
        return material;
    }

    public JSONObject getState() {
        return state;
    }
}
