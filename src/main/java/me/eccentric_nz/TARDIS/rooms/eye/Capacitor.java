package me.eccentric_nz.TARDIS.rooms.eye;

public enum Capacitor {

    NORMAL(0.75, 24, 24), // 1000
    MEDIUM(1.0, 21, 21), // 1001
    LARGE(1.25, 18, 18), // 1002
    ENORMOUS(1.5, 15, 15), // 1003
    SUPERMASSIVE(1.75, 12, 12); // 1004

    private final double radius;
    private final double rings;
    private final double density;

    Capacitor(double radius, double rings, double density) {
        this.radius = radius;
        this.rings = rings;
        this.density = density;
    }

    public double getRadius() {
        return radius;
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
