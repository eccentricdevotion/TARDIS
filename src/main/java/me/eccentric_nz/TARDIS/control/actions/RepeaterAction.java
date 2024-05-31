package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUISystemTree;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.RepeaterControl;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.entity.Player;

public class RepeaterAction {

    private final TARDIS plugin;

    public RepeaterAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void announce(Player player, Block block, int type) {
        Repeater repeater = (Repeater) block.getBlockData();
        RepeaterControl rc = RepeaterControl.getControl(type);
        int delay = repeater.getDelay();
        boolean hasnt = false;
        // check if player/tardis has system upgrade
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
            if (rc == RepeaterControl.WORLD) {
                if ((delay == 2 || delay == 3) && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), GUISystemTree.INTER_DIMENSIONAL_TRAVEL)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Inter Dimensional Travel");
                    hasnt = true;
                }
            }
            if (rc == RepeaterControl.MULTIPLIER) {
                if (delay == 1 && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), GUISystemTree.DISTANCE_1)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Distance 1");
                    hasnt = true;
                }
                if (delay == 2 && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), GUISystemTree.DISTANCE_2)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Distance 2");
                    hasnt = true;
                }
                if (delay == 3 && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), GUISystemTree.DISTANCE_3)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Distance 3");
                    hasnt = true;
                }
            }
            if (hasnt) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    repeater.setDelay(1);
                    block.setBlockData(repeater);
                }, 2L);
            }
        }
        // message setting when clicked
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
        if (rsp.resultSet() && rsp.isAnnounceRepeatersOn()) {
            if (delay == 4 || hasnt) {
                delay = 0;
            }
            plugin.getMessenger().announceRepeater(player, rc.getDescriptions().get(delay));
        }
    }
}
