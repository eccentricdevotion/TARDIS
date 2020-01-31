package me.eccentric_nz.TARDIS.database.data;

public class Transmat {

    private final String name;
    private final String world;
    private final float x;
    private final float y;
    private final float z;
    private final float yaw;

    public Transmat(String name, String world, float x, float y, float z, float yaw) {
        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
    }

    public String getName() {
        return name;
    }

    public String getWorld() {
        return world;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }
}
