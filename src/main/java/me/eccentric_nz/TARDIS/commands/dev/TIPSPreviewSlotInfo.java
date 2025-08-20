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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;

public class TIPSPreviewSlotInfo {

    private final TARDIS plugin;

    public TIPSPreviewSlotInfo(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean display() {
        for (Schematic schematic : Consoles.getBY_NAMES().values()) {
            if (schematic.getPreview() < 0) {
                int slot = schematic.getPreview();
                int row = slot / 20;
                int col = slot % 20;
                int centreX = row * 1024 + 496;
                int centreZ = col * 1024 + 496;
                plugin.debug("slot = " + slot + ", row = " + row + ", col = " + col + ", X = " + centreX + ", Z = " + centreZ);
            }
        }
        return true;
    }
}
