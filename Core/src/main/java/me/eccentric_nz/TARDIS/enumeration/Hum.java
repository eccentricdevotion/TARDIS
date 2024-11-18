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

import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import org.bukkit.NamespacedKey;

/**
 * @author eccentric_nz
 */
public enum Hum {

    ALIEN(Bowl.ALIEN.getKey()),
    ATMOSPHERE(Bowl.ATMOSPHERE.getKey()),
    COMPUTER(Bowl.COMPUTER.getKey()),
    COPPER(Bowl.COPPER.getKey()),
    CORAL(Bowl.CORAL.getKey()),
    GALAXY(Bowl.GALAXY.getKey()),
    LEARNING(Bowl.LEARNING.getKey()),
    MIND(Bowl.MIND.getKey()),
    NEON(Bowl.NEON.getKey()),
    SLEEPING(Bowl.SLEEPING.getKey()),
    VOID(Bowl.VOID.getKey()),
    RANDOM(Bowl.RANDOM.getKey());
    
    private final NamespacedKey key;

    Hum(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
