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
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public enum GlowstoneCircuit {

    ADMIN("Server Admin Circuit", CircuitVariant.ADMIN.getKey()),
    ARS("TARDIS ARS Circuit", CircuitVariant.ARS.getKey()),
    BIO("Bio-scanner Circuit", CircuitVariant.BIO.getKey()),
    BRUSH("Brush Circuit", CircuitVariant.BRUSH.getKey()),
    CHAMELEON("TARDIS Chameleon Circuit", CircuitVariant.CHAMELEON.getKey()),
    CIRCUITS("Circuits", CircuitVariant.GALLIFREY.getKey()),
    CONVERSION("Conversion Circuit", CircuitVariant.CONVERSION.getKey()),
    DIAMOND("Diamond Disruptor Circuit", CircuitVariant.DIAMOND.getKey()),
    EMERALD("Emerald Environment Circuit", CircuitVariant.EMERALD.getKey()),
    IGNITE("Ignite Circuit", CircuitVariant.IGNITE.getKey()),
    INPUT("TARDIS Input Circuit", CircuitVariant.INPUT.getKey()),
    INVISIBILITY("TARDIS Invisibility Circuit", CircuitVariant.INVISIBILITY.getKey()),
    KNOCKBACK("Knockback Circuit", CircuitVariant.KNOCKBACK.getKey()),
    LOCATOR("TARDIS Locator Circuit", CircuitVariant.LOCATOR.getKey()),
    MATERIALISATION("TARDIS Materialisation Circuit", CircuitVariant.MATERIALISATION.getKey()),
    MEMORY("TARDIS Memory Circuit", CircuitVariant.MEMORY.getKey()),
    PAINTER("Painter Circuit", CircuitVariant.PAINTER.getKey()),
    PERCEPTION("Perception Circuit", CircuitVariant.PERCEPTION.getKey()),
    PICKUP("Pickup Arrows Circuit", CircuitVariant.PICKUP.getKey()),
    RANDOM("TARDIS Randomiser Circuit", CircuitVariant.RANDOM.getKey()),
    REDSTONE("Redstone Activator Circuit", CircuitVariant.REDSTONE.getKey()),
    RIFT("Rift Circuit", CircuitVariant.RIFT.getKey()),
    SCANNER("TARDIS Scanner Circuit", CircuitVariant.SCANNER.getKey()),
    SONIC("Sonic Oscillator", CircuitVariant.SONIC.getKey()),
    STATTENHEIM("TARDIS Stattenheim Circuit", CircuitVariant.STATTENHEIM.getKey()),
    TELEPATHIC("TARDIS Telepathic Circuit", CircuitVariant.TELEPATHIC.getKey()),
    TEMPORAL("TARDIS Temporal Circuit", CircuitVariant.TEMPORAL.getKey());

    private static final HashMap<String, GlowstoneCircuit> BY_NAME = new HashMap<>();

    static {
        for (GlowstoneCircuit glowstone : values()) {
            BY_NAME.put(glowstone.displayName, glowstone);
        }
    }

    final String displayName;
    final NamespacedKey model;

    GlowstoneCircuit(String displayName, NamespacedKey model) {
        this.displayName = displayName;
        this.model = model;
    }

    public static HashMap<String, GlowstoneCircuit> getByName() {
        return BY_NAME;
    }

    public String getDisplayName() {
        return displayName;
    }

    public NamespacedKey getModel() {
        return model;
    }
}
