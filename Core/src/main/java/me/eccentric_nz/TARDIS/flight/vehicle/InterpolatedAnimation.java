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
