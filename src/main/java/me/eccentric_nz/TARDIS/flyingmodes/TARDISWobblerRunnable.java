/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.flyingmodes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author eccentric_nz
 */
public class TARDISWobblerRunnable extends TARDISWobblerSlot implements Runnable {

    private final Inventory inv;
    private int slot;
    private int taskId;
    private final List<Integer> directions = Arrays.asList(new Integer[]{0, 1, 2, 3});

    public TARDISWobblerRunnable(Inventory inv) {
        this.inv = inv;
        this.slot = 20;
    }

    @Override
    public void run() {
        // clear slot
        inv.setItem(getSlot(), vortex);
        Collections.shuffle(directions);
        found:
        for (int i = 0; i < 4; i++) {
            for (int d = 0; d < 4; d++) {
                if (directions.get(d) == 0) {
                    int s0 = upSlot(getSlot());
                    if (bounds.contains(s0)) {
                        setSlot(s0);
                        break found;
                    }
                }
                if (directions.get(d) == 1) {
                    int s1 = leftSlot(getSlot());
                    if (bounds.contains(s1)) {
                        setSlot(s1);
                        break found;
                    }
                }
                if (directions.get(d) == 2) {
                    int s2 = rightSlot(getSlot());
                    if (bounds.contains(s2)) {
                        setSlot(s2);
                        break found;
                    }
                }
                if (directions.get(d) == 3) {
                    int s3 = downSlot(getSlot());
                    if (bounds.contains(s3)) {
                        setSlot(s3);
                        break found;
                    }
                }
            }
        }
        // set the slot
        inv.setItem(getSlot(), box);
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
