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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.enumeration;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public enum GlowstoneCircuit {

    ADMIN("Server Admin Circuit", 10001968),
    ARS("TARDIS ARS Circuit", 10001973),
    BIO("Bio-scanner Circuit", 10001969),
    CHAMELEON("TARDIS Chameleon Circuit", 10001966),
    CIRCUITS("Circuits", 10001985),
    DIAMOND("Diamond Disruptor Circuit", 10001971),
    EMERALD("Emerald Environment Circuit", 10001972),
    IGNITE("Ignite Circuit", 10001982),
    INPUT("TARDIS Input Circuit", 10001976),
    INVISIBILITY("TARDIS Invisibility Circuit", 10001981),
    KNOCKBACK("Knockback Circuit", 10001986),
    LOCATOR("TARDIS Locator Circuit", 10001965),
    MATERIALISATION("TARDIS Materialisation Circuit", 10001964),
    MEMORY("TARDIS Memory Circuit", 10001975),
    PAINTER("Painter Circuit", 10001979),
    PERCEPTION("Perception Circuit", 10001978),
    PICKUP("Pickup Arrows Circuit", 10001984),
    RANDOM("TARDIS Randomiser Circuit", 10001980),
    REDSTONE("Redstone Activator Circuit", 10001970),
    RIFT("Rift Circuit", 10001983),
    SCANNER("TARDIS Scanner Circuit", 10001977),
    SONIC("Sonic Oscillator", 10001967),
    STATTENHEIM("TARDIS Stattenheim Circuit", 10001963),
    TEMPORAL("TARDIS Temporal Circuit", 10001974);

    private static final HashMap<String, GlowstoneCircuit> BY_NAME = new HashMap<>();

    static {
        for (GlowstoneCircuit glowstone : values()) {
            BY_NAME.put(glowstone.displayName, glowstone);
        }
    }

    String displayName;
    int customModelData;
    int damaged;

    GlowstoneCircuit(String displayName, int customModelData) {
        this.displayName = displayName;
        this.customModelData = customModelData;
        damaged = this.customModelData + 10000000;
    }

    public static HashMap<String, GlowstoneCircuit> getByName() {
        return BY_NAME;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public int getDamaged() {
        return damaged;
    }
}
