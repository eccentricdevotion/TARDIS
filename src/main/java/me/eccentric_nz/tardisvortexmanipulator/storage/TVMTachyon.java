/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.storage;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TVMTachyon {

    private final UUID uuid;
    private final int level;

    public TVMTachyon(UUID uuid, int level) {
        this.uuid = uuid;
        this.level = level;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getLevel() {
        return level;
    }
}
