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

import java.util.HashMap;

/**
 * The time rotor, sometimes called the time column is a component in the central column of the TARDIS console. While
 * the TARDIS is in flight, the rotor rises and falls, stopping when the TARDIS handbrake is engaged. It is associated
 * with the 'whooshing' noise heard when the TARDIS is in flight.
 *
 * @author eccentric_nz
 */
public class Rotor {

    public static final HashMap<Material, Rotor> byMaterial = new HashMap<>();
    public static final HashMap<Integer, Rotor> byCustomModelData = new HashMap<>();
    private final String name;
    private final int offModelData;
    private final Material material;
    private final int[] frames;
    private final long frameTick;
    private final boolean custom;

    public Rotor(String name, int offModelData, Material material, int[] frames, long frameTick, boolean custom) {
        this.name = name;
        this.offModelData = offModelData;
        this.material = material;
        this.frames = frames;
        this.frameTick = frameTick;
        this.custom = custom;
    }

    public static Rotor getByModelData(int cmd) {
        return byCustomModelData.get(cmd);
    }

    public static Rotor getByMaterial(Material material) {
        return byMaterial.get(material);
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

    public boolean isCustom() {
        return custom;
    }
}
