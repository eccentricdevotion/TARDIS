package me.eccentric_nz.TARDIS.database;

import java.util.concurrent.LinkedBlockingQueue;

public class TARDISRecordingQueue {

    private static final LinkedBlockingQueue<String> QUEUE = new LinkedBlockingQueue<String>();

    /**
     *
     * @return the size of the queue
     */
    public static int getQueueSize() {
        return QUEUE.size();
    }

    /**
     *
     * @param data
     */
    public static void addToQueue(final String data) {
        if (data == null) {
            return;
        }
        QUEUE.add(data);
    }

    /**
     *
     * @return the queue
     */
    public static LinkedBlockingQueue<String> getQUEUE() {
        return QUEUE;
    }
}
