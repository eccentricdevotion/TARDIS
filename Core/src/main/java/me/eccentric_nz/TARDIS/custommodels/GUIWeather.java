/*
 * Copyright (C) 2025 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.custommodels;

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.Weather;
import org.bukkit.Material;

public record GUIWeather() {

    // TARDIS Weather Menu
    public static GUIData CLEAR = new GUIData(Weather.CLEAR.getKey(), 0, Material.SUNFLOWER);
    public static GUIData RAIN = new GUIData(Weather.RAIN.getKey(), 1, Material.WATER_BUCKET);
    public static GUIData THUNDER = new GUIData(Weather.THUNDER.getKey(), 2, Material.GUNPOWDER);
    public static GUIData EXCITE = new GUIData(Weather.EXCITE.getKey(), 5, Material.FIREWORK_ROCKET);
    public static GUIData CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 8, Material.BOWL);
}
