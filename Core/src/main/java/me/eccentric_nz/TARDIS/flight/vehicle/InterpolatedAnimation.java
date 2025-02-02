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
package me.eccentric_nz.TARDIS.flight.vehicle;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class InterpolatedAnimation implements Runnable {

    private final ItemDisplay display;
    private final int period;
    private final float scale = 1.75f;
    private final Vector3f size = new Vector3f(scale, scale, scale);
    private final Vector3f position = new Vector3f(0, -1, 0);
    private final AxisAngle4f axisAngleRotMat = new AxisAngle4f((float) Math.PI, new Vector3f(0, 1, 0));

    public InterpolatedAnimation(ItemDisplay display, int period) {
        this.display = display;
        this.period = period;
    }

    @Override
    public void run() {
        display.setInterpolationDelay(-1);
        Transformation transformation = new Transformation(
                position,
                axisAngleRotMat,
                size,
                axisAngleRotMat
        );
        display.setInterpolationDuration(period);
        display.setTransformation(transformation);
    }
}
