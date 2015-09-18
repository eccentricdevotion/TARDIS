/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.desktop;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBlockScannerData {

    private int changed;
    private int count;
    private float volume;
    private boolean allow;

    public int getChanged() {
        return changed;
    }

    public void setChanged(int changed) {
        this.changed = changed;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getVolume() {
        return (int) volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean allow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }
}
