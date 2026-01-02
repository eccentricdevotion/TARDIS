/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

import java.util.ArrayList;
import java.util.List;

/**
 * A TARDIS isn't just a vehicle for travelling in space and time. As a TARDIS has no real constraints on the amount of
 * space it can use, most TARDISes contain extensive areas which can be used as living quarters or storage space.
 *
 * @author eccentric_nz
 */
public class TARDISWalls {

    public static final List<Material> BLOCKS = new ArrayList<>();
    public static RecipeChoice.MaterialChoice CHOICES;

    static {
        for (String m : TARDIS.plugin.getBlocksConfig().getStringList("tardis_blocks")) {
            try {
                BLOCKS.add(Material.valueOf(m));
            } catch (IllegalArgumentException e) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Invalid material '" + m + "' in tardis_blocks list! " + e.getMessage());
            }
        }
        CHOICES = new RecipeChoice.MaterialChoice(TARDISWalls.BLOCKS);
    }
}
