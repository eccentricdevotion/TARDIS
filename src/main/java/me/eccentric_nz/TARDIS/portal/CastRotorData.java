package me.eccentric_nz.TARDIS.portal;

import org.bukkit.entity.ItemFrame;
import org.bukkit.util.Vector;

public class CastRotorData {

    final ItemFrame frame;
    final Vector offset;

    public CastRotorData(ItemFrame frame, Vector offset) {
        this.frame = frame;
        this.offset = offset;
    }

    public ItemFrame getFrame() {
        return frame;
    }

    public Vector getOffset() {
        return offset;
    }
}
