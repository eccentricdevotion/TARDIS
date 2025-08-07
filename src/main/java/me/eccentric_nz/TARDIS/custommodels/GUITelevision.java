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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.custommodels;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.Button;
import me.eccentric_nz.TARDIS.custommodels.keys.CybermanVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import org.bukkit.Material;

import java.util.List;

public record GUITelevision() {

    // TARDIS Areas
    public static final GUIData DOCTORS = new GUIData(Button.DOCTOR.getKey(), 0, Material.BROWN_STAINED_GLASS_PANE, "Doctors");
    public static final GUIData COMPANIONS = new GUIData(Button.COMPANION.getKey(), 2, Material.BROWN_STAINED_GLASS_PANE, "Companions");
    public static final GUIData CHARACTERS = new GUIData(Button.CHARACTER.getKey(), 4, Material.BROWN_STAINED_GLASS_PANE, "Characters");
    public static final GUIData MONSTERS = new GUIData(Button.MONSTER.getKey(), 6, Material.BROWN_STAINED_GLASS_PANE, "Monsters");
    public static final GUIData CYBER = new GUIData(CybermanVariant.BUTTON_CYBERMAN.getKey(), 8, Material.BROWN_STAINED_GLASS_PANE, "Cybermen");
    public static final GUIData DOWNLOAD = new GUIData(SwitchVariant.DOWNLOAD_OFF.getKey(), 29, Material.REPEATER);
    public static final GUIData REMOVE = new GUIData(Button.REMOVE.getKey(), 31, Material.BUCKET, "Remove skin");
    public static final GUIData BACK = new GUIData(GuiVariant.BACK.getKey(), 33, Material.BOWL, "Back");
    public static final GUIData CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 35, Material.BOWL, TARDIS.plugin.getLanguage().getString("BUTTON_CLOSE"));

    public static List<GUIData> values() {
        return List.of(DOCTORS, COMPANIONS, CHARACTERS, MONSTERS, CYBER, REMOVE, CLOSE);
    }
}
