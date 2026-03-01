package me.eccentric_nz.TARDIS.commands.bind;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class BindBiome {

    public void click(TARDIS plugin, Player player, Biome biome) {
        int id = BindUtility.checkForId(plugin, player);
        if (id != -1) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            if (biome != null && !biome.equals(Biome.THE_VOID)) {
                set.put("type", 4);
                set.put("name", biome.getKey().getKey().toUpperCase(Locale.ROOT));
                int bind_id = plugin.getQueryFactory().doSyncInsert("bind", set);
                plugin.getTrackerKeeper().getBinder().put(player.getUniqueId(), bind_id);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "BIND_CLICK");
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOME_NOT_VALID");
            }
        }
    }
}
