package me.eccentric_nz.TARDIS.rooms.eye;

public enum Capacitor {

    NORMAL(0.75, 0.8, 6, 30), // 1000
    MEDIUM(1.0, 0.9, 12, 24), // 1001
    LARGE(1.33, 1.0,18, 18), // 1002
    ENORMOUS(1.66, 1.15, 24, 12), // 1003
    SUPERMASSIVE(2, 1.3, 30, 6); // 1004

    private final double radius;
    private final double scale;
    private final double rings;
    private final double density;

    Capacitor(double radius, double scale, double rings, double density) {
        // larger radii are needed for bigger spheres
        this.radius = radius;
        // larger scales are needed for bigger spheres
        this.scale = scale;
        // more rings are needed for bigger spheres
        this.rings = rings;
        // less density is needed for bigger spheres
        this.density = density;
    }

    public double getRadius() {
        return radius;
    }

    public double getScale() {
        return scale;
    }

    public double getRings() {
        return rings;
    }

    public double getDensity() {
        return density;
    }

    public int getCustomModelData() {
        return 1000 + this.ordinal();
    }
}
