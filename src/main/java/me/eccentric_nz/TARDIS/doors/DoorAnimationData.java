package me.eccentric_nz.TARDIS.doors;

import org.bukkit.NamespacedKey;

public class DoorAnimationData {

    private final long ticks;
    private final String sound;
    private final NamespacedKey[] animation;

    public DoorAnimationData(long ticks, String sound, NamespacedKey[] animation) {
        this.ticks = ticks;
        this.sound = sound;
        this.animation = animation;
    }

    public long getTicks() {
        return ticks;
    }

    public String getSound() {
        return sound;
    }

    public NamespacedKey[] getAnimation() {
        return animation;
    }
}
