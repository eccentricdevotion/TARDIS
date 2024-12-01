/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.custommodels;

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.Bucket;
import org.bukkit.Material;

public record GUITransmat() {

    public static GUIData INFO = new GUIData(GuiVariant.INFO.getKey(), 8, Material.BOWL);
    public static GUIData TRANSMAT = new GUIData(GuiVariant.BUTTON_TRANSMAT.getKey(), 17, Material.BOWL);
    public static GUIData ROOMS = new GUIData(GuiVariant.BUTTON_ROOMS_WORLD.getKey(), 26, Material.BOWL);
    public static GUIData DELETE = new GUIData(Bucket.DELETE.getKey(), 35, Material.BUCKET);
    public static GUIData CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 53, Material.BOWL);
}
