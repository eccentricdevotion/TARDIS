package me.eccentric_nz.TARDIS.database.data;

public class Hidden {

    private String owner;
    private String status;
    private int x;
    private int y;
    private int z;

    public Hidden(String owner, String status, int x, int y, int z) {
        this.owner = owner;
        this.status = status;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
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
}
