package me.eccentric_nz.TARDIS.flyingmodes;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class TARDISManualFlightListener implements Listener {

    private final TARDIS plugin;

    public TARDISManualFlightListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block b = event.getClickedBlock();
        if (b != null) {
            String loc = b.getLocation().toString();
            if (plugin.getTrackerKeeper().getCommand().containsKey(uuid)) {
                String which = plugin.getTrackerKeeper().getCommand().get(uuid);
                plugin.getConfig().set(which, loc);
                plugin.getTrackerKeeper().getCommand().remove(uuid);
                player.sendMessage(plugin.getPluginName() + which + " set!");
                plugin.saveConfig();
            }
            if (plugin.getTrackerKeeper().getFlight().containsKey(uuid)) {
                String which = plugin.getConfig().getString(plugin.getTrackerKeeper().getFlight().get(uuid));
                if (loc.equals(which)) {
                    if (plugin.getTrackerKeeper().getCount().containsKey(uuid)) {
                        int increment = plugin.getTrackerKeeper().getCount().get(uuid) + 1;
                        plugin.getTrackerKeeper().getCount().put(uuid, increment);
                    } else {
                        plugin.getTrackerKeeper().getCount().put(uuid, 1);
                    }
                }
                plugin.getTrackerKeeper().getFlight().remove(uuid);
            }
        }
    }
}
