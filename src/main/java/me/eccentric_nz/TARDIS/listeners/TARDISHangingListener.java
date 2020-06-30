package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;

public class TARDISHangingListener implements Listener {

    private final TARDIS plugin;

    public TARDISHangingListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingBreak(HangingBreakEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ItemFrame) {
            if (plugin.getGeneralKeeper().getProtectBlockMap().containsKey(event.getEntity().getLocation().getBlock().getLocation().toString()) || plugin.getGeneralKeeper().getTimeRotors().contains(entity.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
