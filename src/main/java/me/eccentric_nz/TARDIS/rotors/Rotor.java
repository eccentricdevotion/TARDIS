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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.rotors;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

/**
 * The time rotor, sometimes called the time column is a component in the central column of the TARDIS console. While
 * the TARDIS is in flight, the rotor rises and falls, stopping when the TARDIS handbrake is engaged. It is associated
 * with the 'whooshing' noise heard when the TARDIS is in flight.
 *
 * @author eccentric_nz
 */
public record Rotor(String name, NamespacedKey offModel, Material material, int[] frames, long frameTick,
                    boolean custom) {

    public static final HashMap<Material, Rotor> byMaterial = new HashMap<>();
    public static final HashMap<NamespacedKey, Rotor> byCustomModel = new HashMap<>();
    public static final HashMap<String, Rotor> byName = new HashMap<>();

    public static Rotor getByModel(NamespacedKey key) {
        return byCustomModel.get(key);
    }

    public static Rotor getByMaterial(Material material) {
        return byMaterial.get(material);
    }
}
