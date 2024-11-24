package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.custommodeldata.keys.MagmaBlock;
import org.bukkit.NamespacedKey;

public enum Capacitor {

    NORMAL(0.75, 6, MagmaBlock.SPHERE_0.getKey()),
    MEDIUM(1.0, 12, MagmaBlock.SPHERE_1.getKey()),
    LARGE(1.33, 18, MagmaBlock.SPHERE_2.getKey()),
    ENORMOUS(1.66, 24, MagmaBlock.SPHERE_3.getKey()),
    SUPERMASSIVE(2, 30, MagmaBlock.SPHERE_4.getKey());

    private final double radius;
    private final double rings;
    private final NamespacedKey model;

    Capacitor(double radius, double rings, NamespacedKey model) {
        // larger radii are needed for bigger spheres
        this.radius = radius;
        // more rings are needed for bigger spheres
        this.rings = rings;
        this.model = model;
    }

    public double getRadius() {
        return radius;
    }

    public double getRings() {
        return rings;
    }

    public NamespacedKey getModel() {
        return model;
    }
}
