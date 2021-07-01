/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.database;

import java.util.concurrent.LinkedBlockingQueue;

public class TARDISRecordingQueue {

    private static final LinkedBlockingQueue<String> QUEUE = new LinkedBlockingQueue<>();

    /**
     * @return the size of the queue
     */
    public static int getQueueSize() {
        return QUEUE.size();
    }

    /**
     * @param data The data string to add to the queue
     */
    public static void addToQueue(String data) {
        if (data == null) {
            return;
        }
        QUEUE.add(data);
    }

    /**
     * @return the queue
     */
    public static LinkedBlockingQueue<String> getQUEUE() {
        return QUEUE;
    }
}
