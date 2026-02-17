package me.eccentric_nz.TARDIS.commands.bind;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class BindPreset {

    public void click(TARDIS plugin, Player player, String which) {
        int id = BindUtility.checkForId(plugin, player);
        if (id != -1) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            if (which.equalsIgnoreCase("OFF") || which.equalsIgnoreCase("ADAPT")) {
                set.put("name", which.toUpperCase(Locale.ROOT));
            } else {
                // check valid preset
                ChameleonPreset preset;
                if (plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false).contains(which)) {
                    set.put("name", "ITEM:" + which);
                } else {
                    try {
                        preset = ChameleonPreset.valueOf(which.toUpperCase(Locale.ROOT));
                    } catch (IllegalArgumentException e) {
                        // abort
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_PRESET");
                        return;
                    }
                    set.put("name", preset.toString());
                }
            }
            set.put("type", 5);
            int bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
            plugin.getTrackerKeeper().getBinder().put(player.getUniqueId(), bind_id);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "BIND_CLICK");
        }
    }
}
