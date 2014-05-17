/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.flyingmodes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISManualFlightRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<String> target = Arrays.asList("one", "two", "three", "four");
    private int taskID;
    private final int loops = 10;
    private int i = 0;
    private final Random random = new Random();
    private final Player player;

    public TARDISManualFlightRunnable(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        if (i < loops) {
            String r = target.get(random.nextInt(4));
            player.sendMessage("Click repeater " + r);
            plugin.getTrackerKeeper().getFlight().put(player.getUniqueId(), r);
            i++;
        } else {
            player.sendMessage("You hit the correct repeater " + plugin.getTrackerKeeper().getCount().get(player.getUniqueId()) + " times out of 10!");
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            plugin.getTrackerKeeper().getCount().remove(player.getUniqueId());
        }
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
