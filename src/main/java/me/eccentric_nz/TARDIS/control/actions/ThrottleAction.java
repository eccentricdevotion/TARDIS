package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
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
        // update player prefs
        HashMap<String, Object> wherer = new HashMap<>();
        wherer.put("uuid", player.getUniqueId().toString());
        HashMap<String, Object> setr = new HashMap<>();
        setr.put("throttle", delay);
        plugin.getQueryFactory().doUpdate("player_prefs", setr, wherer);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "THROTTLE", SpaceTimeThrottle.getByDelay().get(delay).toString());
    }
}
