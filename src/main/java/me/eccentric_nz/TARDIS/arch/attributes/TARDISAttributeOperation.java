/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.arch.attributes;

/**
 *
 * @author eccentric_nz
 */
public enum TARDISAttributeOperation {

    ADD_NUMBER(0),
    MULTIPLY_PERCENTAGE(1),
    ADD_PERCENTAGE(2);
    private final int id;

    private TARDISAttributeOperation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TARDISAttributeOperation fromId(int id) {
        // Linear scan is very fast for small N
        for (TARDISAttributeOperation op : values()) {
            if (op.getId() == id) {
                return op;
            }
        }
        throw new IllegalArgumentException("Corrupt operation ID " + id + " detected.");
    }
}
