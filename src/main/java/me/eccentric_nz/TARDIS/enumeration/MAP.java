/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.enumeration;

/**
 * @author eccentric_nz
 */
public enum MAP {
    
    ADMIN("Server Admin Circuit"),
    ARS("TARDIS ARS Circuit"),
    BIO("Bio-scanner Circuit"),
    CHAMELEON("TARDIS Chameleon Circuit"),
    DIAMOND("Diamond Disruptor Circuit"),
    EMERALD("Emerald Environment Circuit"),
    INPUT("TARDIS Input Circuit"),
    INVISIBILITY("TARDIS Invisibility Circuit"),
    LOCATOR("TARDIS Locator Circuit"),
    MATERIALISATION("TARDIS Materialisation Circuit"),
    MEMORY("TARDIS Memory Circuit"),
    PAINTER("Painter Circuit"),
    PERCEPTION("Perception Circuit"),
    RANDOM("TARDIS Randomiser Circuit"),
    REDSTONE("Redstone Activator Circuit"),
    SCANNER("TARDIS Scanner Circuit"),
    SONIC("Sonic Oscillator"),
    STATTENHEIM("TARDIS Stattenheim Circuit"),
    TEMPORAL("TARDIS Temporal Circuit");

    String displayName;

    MAP(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
