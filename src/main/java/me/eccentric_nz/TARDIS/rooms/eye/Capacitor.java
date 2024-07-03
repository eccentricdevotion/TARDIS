package me.eccentric_nz.TARDIS.rooms.eye;

public enum Capacitor {

    NORMAL(0.75, 6), // 1000
    MEDIUM(1.0, 12), // 1001
    LARGE(1.33, 18), // 1002
    ENORMOUS(1.66, 24), // 1003
    SUPERMASSIVE(2, 30); // 1004

    private final double radius;
    private final double rings;

    Capacitor(double radius, double rings) {
        // larger radii are needed for bigger spheres
        this.radius = radius;
        // more rings are needed for bigger spheres
        this.rings = rings;
    }

    public double getRadius() {
        return radius;
    }

    public double getRings() {
        return rings;
    }

    public int getCustomModelData() {
        return 1000 + this.ordinal();
    }
}
