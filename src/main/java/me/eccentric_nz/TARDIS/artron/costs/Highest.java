package me.eccentric_nz.TARDIS.artron.costs;

public class Highest {

    private int small;
    private int medium;
    private int tall;
    private int wide;
    private int massive;

    public Highest(int small, int medium, int tall, int wide, int massive) {
        this.small = small;
        this.medium = medium;
        this.tall = tall;
        this.wide = wide;
        this.massive = massive;
    }

    public int getSmall() {
        return small;
    }

    public void setSmall(int small) {
        this.small = small;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getTall() {
        return tall;
    }

    public void setTall(int tall) {
        this.tall = tall;
    }

    public int getWide() {
        return wide;
    }

    public void setWide(int wide) {
        this.wide = wide;
    }

    public int getMassive() {
        return massive;
    }

    public void setMassive(int massive) {
        this.massive = massive;
    }
}
