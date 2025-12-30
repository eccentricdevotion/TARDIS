/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.monsters.cybermen;

import me.eccentric_nz.TARDIS.custommodels.keys.ArmourVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.CybermanVariant;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class CyberType {

    public static final HashMap<NamespacedKey, NamespacedKey> CYBER_HEADS = new HashMap<>() {
        {
            put(ArmourVariant.CYBERMAN.getKey(), CybermanVariant.CYBERMAN_HEAD.getKey());
            put(ArmourVariant.BLACK_CYBERMAN.getKey(), CybermanVariant.BLACK_CYBERMAN_HEAD.getKey());
            put(ArmourVariant.CYBERMAN_EARTHSHOCK.getKey(), CybermanVariant.CYBERMAN_EARTHSHOCK_HEAD.getKey());
            put(ArmourVariant.CYBERMAN_INVASION.getKey(), CybermanVariant.CYBERMAN_INVASION_HEAD.getKey());
            put(ArmourVariant.CYBERMAN_MOONBASE.getKey(), CybermanVariant.CYBERMAN_MOONBASE_HEAD.getKey());
            put(ArmourVariant.CYBERMAN_RISE.getKey(), CybermanVariant.CYBERMAN_RISE_HEAD.getKey());
            put(ArmourVariant.CYBERMAN_TENTH_PLANET.getKey(), CybermanVariant.CYBERMAN_TENTH_PLANET_HEAD.getKey());
            put(ArmourVariant.CYBER_LORD.getKey(), CybermanVariant.CYBER_LORD_HEAD.getKey());
            put(ArmourVariant.WOOD_CYBERMAN.getKey(), CybermanVariant.WOOD_CYBERMAN_HEAD.getKey());
            put(ArmourVariant.CYBERSHADE.getKey(), CybermanVariant.CYBERSHADE_HEAD.getKey());
        }
    };
}
