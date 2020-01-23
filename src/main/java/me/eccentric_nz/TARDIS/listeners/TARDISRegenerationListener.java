package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import net.pekkit.projectrassilon.events.RegenEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class TARDISRegenerationListener implements Listener {

    private final TARDIS plugin;

    public TARDISRegenerationListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void onTimeLordRegeneraion(RegenEvent event) {
        Player player = event.getPlayer();
        // is player in the TARDIS?
        if (plugin.getUtils().inTARDISWorld(player.getLocation())) {
            String uuid = player.getUniqueId().toString();
            // does the player have a TARDIS?
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            if (rs.resultSet()) {
                int id = rs.getTardis().getTardis_id();
                // is the TARDIS in siege mode?
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    int min = (plugin.getArtronConfig().getInt("full_charge") / 100) * plugin.getArtronConfig().getInt("siege_transfer");
                    // is the TARDIS low on Artron energy?
                    if (rs.getTardis().getArtron_level() < min) {
                        // attempt to transfer Time Lord energy to the TARDIS
                        // check player has a prefs record
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                        if (!rsp.resultSet()) {
                            return;
                        }
                        // check player has enough Time Lord energy - default 10% of full_charge
                        int level = rsp.getArtronLevel();
                        if (min > level) {
                            TARDISMessage.send(player, "SIEGE_MIN", String.format("%s", min));
                            return;
                        }
                        // transfer min
                        HashMap<String, Object> wheretl = new HashMap<>();
                        wheretl.put("uuid", uuid);
                        HashMap<String, Object> wherea = new HashMap<>();
                        wherea.put("tardis_id", id);
                        plugin.getQueryFactory().alterEnergyLevel("player_prefs", -min, wheretl, player);
                        plugin.getQueryFactory().alterEnergyLevel("tardis", min, wherea, player);
                        TARDISMessage.send(player, "SIEGE_TRANSFER", String.format("%s", min));
                    }
                }
            }
        }
    }
}
