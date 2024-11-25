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

import me.eccentric_nz.TARDIS.custommodeldata.keys.GlowstoneDust;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public enum GlowstoneCircuit {

    ADMIN("Server Admin Circuit", GlowstoneDust.ADMIN.getKey()),
    ARS("TARDIS ARS Circuit", GlowstoneDust.ARS.getKey()),
    BIO("Bio-scanner Circuit", GlowstoneDust.BIO.getKey()),
    BRUSH("Brush Circuit", GlowstoneDust.BRUSH.getKey()),
    CHAMELEON("TARDIS Chameleon Circuit", GlowstoneDust.CHAMELEON.getKey()),
    CIRCUITS("Circuits", GlowstoneDust.GALLIFREY.getKey()),
    CONVERSION("Conversion Circuit", GlowstoneDust.CONVERSION.getKey()),
    DIAMOND("Diamond Disruptor Circuit", GlowstoneDust.DIAMOND.getKey()),
    EMERALD("Emerald Environment Circuit", GlowstoneDust.EMERALD.getKey()),
    IGNITE("Ignite Circuit", GlowstoneDust.IGNITE.getKey()),
    INPUT("TARDIS Input Circuit", GlowstoneDust.INPUT.getKey()),
    INVISIBILITY("TARDIS Invisibility Circuit", GlowstoneDust.INVISIBILITY.getKey()),
    KNOCKBACK("Knockback Circuit", GlowstoneDust.KNOCKBACK.getKey()),
    LOCATOR("TARDIS Locator Circuit", GlowstoneDust.LOCATOR.getKey()),
    MATERIALISATION("TARDIS Materialisation Circuit", GlowstoneDust.MATERIALISATION.getKey()),
    MEMORY("TARDIS Memory Circuit", GlowstoneDust.MEMORY.getKey()),
    PAINTER("Painter Circuit", GlowstoneDust.PAINTER.getKey()),
    PERCEPTION("Perception Circuit", GlowstoneDust.PERCEPTION.getKey()),
    PICKUP("Pickup Arrows Circuit", GlowstoneDust.PICKUP.getKey()),
    RANDOM("TARDIS Randomiser Circuit", GlowstoneDust.RANDOM.getKey()),
    REDSTONE("Redstone Activator Circuit", GlowstoneDust.REDSTONE.getKey()),
    RIFT("Rift Circuit", GlowstoneDust.RIFT.getKey()),
    SCANNER("TARDIS Scanner Circuit", GlowstoneDust.SCANNER.getKey()),
    SONIC("Sonic Oscillator", GlowstoneDust.SONIC.getKey()),
    STATTENHEIM("TARDIS Stattenheim Circuit", GlowstoneDust.STATTENHEIM.getKey()),
    TELEPATHIC("TARDIS Telepathic Circuit", GlowstoneDust.TELEPATHIC.getKey()),
    TEMPORAL("TARDIS Temporal Circuit", GlowstoneDust.TEMPORAL.getKey());

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
