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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Comparator;

/**
 * The relativity differentiator is a component inside the TARDIS that allows it to travel in space.
 *
 * @author eccentric_nz
 */
public class DifferentiatorAction {

    private final TARDIS plugin;
    public DifferentiatorAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void bleep(Block block) {
        Comparator comparator = (Comparator) block.getBlockData();
        String sound =  comparator.getMode().equals(Comparator.Mode.SUBTRACT) ? "differentiator_off" : "differentiator_on";
        TARDISSounds.playTARDISSound(block.getLocation(), sound);
    }
}
