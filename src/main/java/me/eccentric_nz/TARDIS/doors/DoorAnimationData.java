package me.eccentric_nz.TARDIS.doors;

public class DoorAnimationData {

    private final long ticks;
    private final String sound;

    public DoorAnimationData(long ticks, String sound) {
        this.ticks = ticks;
        this.sound = sound;
    }

    public long getTicks() {
        return ticks;
    }

    public String getSound() {
        return sound;
    }
}
