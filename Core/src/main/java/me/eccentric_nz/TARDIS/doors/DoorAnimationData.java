package me.eccentric_nz.TARDIS.doors;

public class DoorAnimationData {

    private final long ticks;
    private final String sound;
    private final int lastFrame;

    public DoorAnimationData(long ticks, String sound, int lastFrame) {
        this.ticks = ticks;
        this.sound = sound;
        this.lastFrame = lastFrame;
    }

    public long getTicks() {
        return ticks;
    }

    public String getSound() {
        return sound;
    }

    public int getLastFrame() {
        return lastFrame;
    }
}
