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
package me.eccentric_nz.TARDIS.particles;

public enum ParticleShape {

    BEAM(25L, 0.5d),
    HELIX(60L, 0),
    RANDOM(20L, 1.0d),
    RINGS(10L, 2.0d),
    VACUUM(12L, 0.5d),
    WAVE(32L, 0.5d),
    FALLING(20L, 0);

    private final long period;
    private final double y;

    ParticleShape(long period, double y) {
        this.period = period;
        this.y = y;
    }

    public long getPeriod() {
        return period;
    }

    public double getY() {
        return y;
    }
}
