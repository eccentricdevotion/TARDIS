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
package me.eccentric_nz.TARDIS.chameleon.shell;

import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.ArrayList;
import java.util.List;

public class ShellLoaderProblemBlocks {

    public static final List<Material> DO_FIRST = new ArrayList<>();

    static {
        DO_FIRST.add(Material.LEVER);
        DO_FIRST.add(Material.REDSTONE_TORCH);
        DO_FIRST.add(Material.REDSTONE_WALL_TORCH);
        DO_FIRST.add(Material.TORCH);
        DO_FIRST.add(Material.WALL_TORCH);
        DO_FIRST.addAll(Tag.BUTTONS.getValues());
        DO_FIRST.addAll(Tag.DOORS.getValues());
        DO_FIRST.addAll(Tag.FLOWERS.getValues());
        DO_FIRST.addAll(Tag.TRAPDOORS.getValues());
        DO_FIRST.addAll(Tag.WALL_SIGNS.getValues());
    }
}
