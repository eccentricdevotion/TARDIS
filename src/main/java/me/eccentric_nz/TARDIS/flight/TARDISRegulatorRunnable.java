/*
 * Copyright (C) 2023 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.flight;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.inventory.InventoryView;

/**
 * The helmic regulator also appeared in the Tenth Doctor's TARDIS in the form of a bicycle pump. It is one of the last
 * things he needed to do before taking off, along with closing down the gravitic anomaliser, and turning off the
 * handbrake.
 *
 * @author eccentric_nz
 */
public class TARDISRegulatorRunnable extends TARDISRegulatorSlot implements Runnable {

    private final InventoryView view;
    private final List<Integer> directions = Arrays.asList(0, 1, 2, 3);
    private int slot;
    private int taskId;

    TARDISRegulatorRunnable(InventoryView view) {
        this.view = view;
        slot = 20;
    }

    @Override
    public void run() {
        // clear slot
        view.setItem(getSlot(), vortex);
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
        view.setItem(getSlot(), box);
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    int getTaskId() {
        return taskId;
    }

    void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
