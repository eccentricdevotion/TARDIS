/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Security Protocol 712 is a security feature aboard the Doctor's TARDIS which causes the ship to dematerialise and
 * re-appear at a predetermined point in space and time without its pilot when activated. It can be programmed to be
 * triggered by different events or objects such as Sally Sparrow's DVDs or the activation of the echelon circuit. A
 * holographic recreation of the incumbent Doctor will be projected to inform anyone within the TARDIS of the protocol
 * being activated.
 *
 * @author eccentric_nz
 */
public class TARDISAuthorisedControlDisk {

    private final TARDIS plugin;

    public TARDISAuthorisedControlDisk(TARDIS plugin) {
        this.plugin = plugin;
    }
}
