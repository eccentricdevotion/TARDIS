package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.hads.TARDISCloisterBell;

public class CloisterBellAction {

    private final TARDIS plugin;

    public CloisterBellAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void ring(int id, Tardis tardis) {
        if (plugin.getTrackerKeeper().getCloisterBells().containsKey(id)) {
            plugin.getServer().getScheduler().cancelTask(plugin.getTrackerKeeper().getCloisterBells().get(id));
            plugin.getTrackerKeeper().getCloisterBells().remove(id);
        } else {
            TARDISCloisterBell bell = new TARDISCloisterBell(plugin, Integer.MAX_VALUE, id, plugin.getServer().getPlayer(tardis.getUuid()));
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
            bell.setTask(taskID);
            plugin.getTrackerKeeper().getCloisterBells().put(id, taskID);
        }
    }
}
