package me.eccentric_nz.TARDIS.recipes;

public class TARDISSeedCheck {

    private boolean valid;
    private int lampCount;
    private int lapisCount;
    private int typeCount;
    private int wallCount;
    private int floorCount;
    private int amount;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getLampCount() {
        return lampCount;
    }

    public void setLampCount(int lampCount) {
        this.lampCount = lampCount;
    }

    public int getLapisCount() {
        return lapisCount;
    }

    public void setLapisCount(int lapisCount) {
        this.lapisCount = lapisCount;
    }

    public int getTypeCount() {
        return typeCount;
    }

    public void setTypeCount(int typeCount) {
        this.typeCount = typeCount;
    }

    public int getWallCount() {
        return wallCount;
    }

    public void setWallCount(int wallCount) {
        this.wallCount = wallCount;
    }

    public int getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(int floorCount) {
        this.floorCount = floorCount;
    }

    public int getAmount() {
        return amount;
    }

    public void calculateAmount() {
        int min = lampCount;
        if (lapisCount < min) {
            min = lapisCount;
        }
        if (typeCount < min) {
            min = typeCount;
        }
        if (wallCount < min) {
            min = wallCount;
        }
        if (floorCount < min) {
            min = floorCount;
        }
        amount = min;
    }
}
