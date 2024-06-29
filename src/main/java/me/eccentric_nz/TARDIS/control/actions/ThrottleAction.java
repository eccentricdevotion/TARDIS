package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ThrottleAction {

    private final TARDIS plugin;

    public ThrottleAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setSpaceTime(Block block, Player player) {
        Repeater repeater = (Repeater) block.getBlockData();
        // get delay
        int delay = repeater.getDelay() + 1;
        if (delay > 4) {
            delay = 1;
        }
        if (delay != 4 && plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
            String uuid = player.getUniqueId().toString();
            switch (delay) {
                case 3 -> {
                    if (!new SystemUpgradeChecker(plugin).has(uuid, SystemTree.FASTER)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Faster");
                        delay = 4;
                        repeater.setDelay(4);
                        block.setBlockData(repeater);
                    }
                }
                case 2 -> {
                    if (!new SystemUpgradeChecker(plugin).has(uuid, SystemTree.RAPID)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Rapid");
                        delay = 3;
                        repeater.setDelay(3);
                        block.setBlockData(repeater);
                    }
                }
                case 1 -> {
                    if (!new SystemUpgradeChecker(plugin).has(uuid, SystemTree.WARP)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Warp");
                        delay = 2;
                        repeater.setDelay(2);
                        block.setBlockData(repeater);
                    }
                }
                default -> { }
            }
        }
        // update player prefs
        HashMap<String, Object> wherer = new HashMap<>();
        wherer.put("uuid", player.getUniqueId().toString());
        HashMap<String, Object> setr = new HashMap<>();
        setr.put("throttle", delay);
        plugin.getQueryFactory().doUpdate("player_prefs", setr, wherer);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "THROTTLE", SpaceTimeThrottle.getByDelay().get(delay).toString());
    }
}
