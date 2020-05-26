package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.hads.TARDISCloisterBell;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;

public class TARDISBellCommand {

    private final TARDIS plugin;

    public TARDISBellCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggle(int id, Player player, String[] args) {
        if (args.length < 2) {
            // cloister bell
            if (plugin.getTrackerKeeper().getCloisterBells().containsKey(id)) {
                stopCloisterBell(id);
            } else {
                startCloisterBell(id);
            }
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("off")) {
                if (plugin.getTrackerKeeper().getCloisterBells().containsKey(id)) {
                    stopCloisterBell(id);
                } else {
                    TARDISMessage.send(player, "CLOISTER_BELL_CMD", "off");
                }
                return true;
            } else if (args[1].equalsIgnoreCase("on")) {
                if (!plugin.getTrackerKeeper().getCloisterBells().containsKey(id)) {
                    startCloisterBell(id);
                } else {
                    TARDISMessage.send(player, "CLOISTER_BELL_CMD", "on");
                }
                return true;
            }
        }
        return false;
    }

    private void stopCloisterBell(int id) {
        plugin.getServer().getScheduler().cancelTask(plugin.getTrackerKeeper().getCloisterBells().get(id));
        plugin.getTrackerKeeper().getCloisterBells().remove(id);
    }

    private void startCloisterBell(int id) {
        TARDISCloisterBell bell = new TARDISCloisterBell(plugin, Integer.MAX_VALUE, id);
        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
        bell.setTask(taskID);
        plugin.getTrackerKeeper().getCloisterBells().put(id, taskID);
    }
}
