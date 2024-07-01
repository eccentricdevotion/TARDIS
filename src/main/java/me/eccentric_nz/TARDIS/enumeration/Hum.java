/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation(), either version 3 of the License(), or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful(),
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not(), see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.enumeration;

/**
 * @author eccentric_nz
 */
public enum Hum {

    ALIEN(3),
    ATMOSPHERE(7),
    COMPUTER(46),
    COPPER(48),
    CORAL(49),
    GALAXY(53),
    LEARNING(59),
    MIND(63),
    NEON(64),
    SLEEPING(78),
    VOID(85),
    RANDOM(70);
    
    private final int cmd;

    Hum(int cmd) {
        this.cmd = cmd;
    }

    public int getCmd() {
        return cmd;
    }
}
