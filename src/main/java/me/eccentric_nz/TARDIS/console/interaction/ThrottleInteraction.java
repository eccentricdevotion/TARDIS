package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class ThrottleInteraction {

    private final TARDIS plugin;

    public ThrottleInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Player player, Interaction interaction, int id) {
        String uuid = player.getUniqueId().toString();
        int unary = interaction.getPersistentDataContainer().getOrDefault(plugin.getUnaryKey(), PersistentDataType.INTEGER, -1);
        // get current throttle setting
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        if (rsp.resultSet()) {
            /*
            states should go up to fastest, then back down to slowest, rather than cycling
            NORMAL => 4
            FASTER => 3
            RAPID => 2
            WARP => 1
             */
            int delay = rsp.getThrottle() + unary;
            if (delay < 1) {
                delay = 2;
                unary = 1;
            }
            if (delay > 4) {
                delay = 3;
                unary = -1;
            }
            // save unary value in the interaction PDC
            interaction.getPersistentDataContainer().set(plugin.getUnaryKey(), PersistentDataType.INTEGER, unary);
            String throttle = SpaceTimeThrottle.getByDelay().get(delay).toString();
            // update player prefs
            HashMap<String, Object> wherer = new HashMap<>();
            wherer.put("uuid", uuid);
            HashMap<String, Object> setr = new HashMap<>();
            setr.put("throttle", delay);
            plugin.getQueryFactory().doUpdate("player_prefs", setr, wherer);
            new InteractionStateSaver(plugin).write("THROTTLE", delay, id);
            plugin.getMessenger().announceRepeater(player, throttle);
            // TODO set custom model data for throttle item display
        }
    }
}
