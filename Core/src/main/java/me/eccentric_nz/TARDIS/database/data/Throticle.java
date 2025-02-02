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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.database.data;

import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;

public class Throticle {

    private final SpaceTimeThrottle  throttle;
    private final boolean particles;

    public Throticle(SpaceTimeThrottle throttle, boolean particles) {
        this.throttle = throttle;
        this.particles = particles;
    }

    public SpaceTimeThrottle getThrottle() {
        return throttle;
    }

    public boolean getParticles() {
        return particles;
    }
}
