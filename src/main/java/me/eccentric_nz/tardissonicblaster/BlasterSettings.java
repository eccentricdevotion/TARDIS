/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardissonicblaster;

import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author eccentric_nz
 */
public class BlasterSettings {

    private final double maxUsableDistance;
    private final long cooldown;
    private final HashMap<UUID, Long> isBlasting = new HashMap<>();

    public BlasterSettings(double maxUsableDistance, long cooldown) {
        this.maxUsableDistance = maxUsableDistance;
        this.cooldown = cooldown;
    }

    public double getMaxUsableDistance() {
        return maxUsableDistance;
    }

    public HashMap<UUID, Long> getIsBlasting() {
        return isBlasting;
    }

    public long getCooldown() {
        return cooldown;
    }
}
