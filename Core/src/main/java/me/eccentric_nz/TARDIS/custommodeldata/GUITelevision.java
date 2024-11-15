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
package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.TARDIS.custommodeldata.keys.BrownStainedGlassPane;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bucket;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Repeater;
import org.bukkit.Material;

import java.util.List;

public record GUITelevision() {

    // TARDIS Areas
    public static GUIData DOCTORS = new GUIData(BrownStainedGlassPane.DOCTOR.getKey(), 1, Material.BROWN_STAINED_GLASS_PANE, "Doctors");
    public static GUIData COMPANIONS = new GUIData(BrownStainedGlassPane.COMPANION.getKey(), 3, Material.BROWN_STAINED_GLASS_PANE, "Companions");
    public static GUIData CHARACTERS = new GUIData(BrownStainedGlassPane.CHARACTER.getKey(), 5, Material.BROWN_STAINED_GLASS_PANE, "Characters");
    public static GUIData MONSTERS = new GUIData(BrownStainedGlassPane.MONSTER.getKey(), 7, Material.BROWN_STAINED_GLASS_PANE, "Monsters");
    public static GUIData DOWNLOAD = new GUIData(Repeater.DOWNLOAD_OFF.getKey(), 29, Material.REPEATER);
    public static GUIData REMOVE = new GUIData(Bucket.REMOVE.getKey(), 31, Material.BUCKET);
    public static GUIData BACK = new GUIData(Bowl.BACK, 33, Material.BOWL);
    public static GUIData CLOSE = new GUIData(Bowl.CLOSE, 35, Material.BOWL);

    public static List<GUIData> values() {
        return List.of(DOCTORS, COMPANIONS, CHARACTERS, MONSTERS, REMOVE, CLOSE);
    }
}
