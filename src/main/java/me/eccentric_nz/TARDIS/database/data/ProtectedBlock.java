package me.eccentric_nz.TARDIS.database.data;

public class ProtectedBlock {

    private String location;
    private int x;
    private int y;
    private int z;
    private int id;

    public ProtectedBlock(String location, int x, int y, int z, int id) {
        this.location = location;
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getId() {
        return id;
    }
}
