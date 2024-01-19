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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.enumeration;

import org.bukkit.Material;

/**
 * The time rotor, sometimes called the time column is a component in the central column of the TARDIS console. While
 * the TARDIS is in flight, the rotor rises and falls, stopping when the TARDIS handbrake is engaged. It is associated
 * with the 'whooshing' noise heard when the TARDIS is in flight.
 *
 * @author eccentric_nz
 */
public enum Rotor {

    EARLY("early", 10000002, Material.BLACK_DYE, new int[]{0, 1, 2, 3, 4, 5, 6, 7}, 6),
    TENNANT("rotor", 10000003, Material.ORANGE_DYE, new int[]{0, 0, 1, 2, 3, 4, 3, 2, 1}, 3),
    ELEVENTH("copper", 10000004, Material.BROWN_DYE, new int[]{0, 0, 1, 2, 3, 4, 3, 2, 1}, 2),
    TWELFTH("round", 10000005, Material.GRAY_DYE, new int[]{0}, 1),
    DELTA("delta", 10000006, Material.CYAN_DYE, new int[]{0, 1, 2, 3, 4, 5}, 4),
    ENGINE("engine", 10000007, Material.LIGHT_BLUE_DYE, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, 2),
    ENGINE_ROTOR("engine_rotor", 10000008, Material.BLUE_DYE, new int[]{0, 1, 2, 3, 4, 5, 4, 3, 2, 1}, 3),
    HOSPITAL("hospital", 10000009, Material.WHITE_DYE, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, 2);

    private final String name;
    private final int offModelData;
    private final Material material;
    private final int[] frames;
    private final long frameTick;

    Rotor(String name, int offModelData, Material material, int[] frames, long frameTick) {
        this.name = name;
        this.offModelData = offModelData;
        this.material = material;
        this.frames = frames;
        this.frameTick = frameTick;
    }

    public static Rotor getByModelData(int cmd) {
        for (Rotor r : values()) {
            if (r.offModelData == cmd) {
                return r;
            }
        }
        return Rotor.EARLY;
    }

    public static Rotor getByMaterial(Material material) {
        for (Rotor r : values()) {
            if (r.material == material) {
                return r;
            }
        }
        return Rotor.TENNANT;
    }

    public String getName() {
        return name;
    }

    public int getOffModelData() {
        return offModelData;
    }

    public Material getMaterial() {
        return material;
    }

    public int[] getFrames() {
        return frames;
    }

    public long getFrameTick() {
        return frameTick;
    }
}
