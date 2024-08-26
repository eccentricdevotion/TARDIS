package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class MicroscopeSlotChangeListener implements Listener {

    private final TARDIS plugin;

    MicroscopeSlotChangeListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.getInventory().setItemInMainHand(MicroscopeUtils.STORED_STACKS.get(player.getUniqueId()));
                MicroscopeUtils.STORED_STACKS.remove(player.getUniqueId());
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.getInventory().setItemInMainHand(MicroscopeUtils.STORED_STACKS.get(player.getUniqueId()));
                MicroscopeUtils.STORED_STACKS.remove(player.getUniqueId());
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
            player.getInventory().setItemInMainHand(MicroscopeUtils.STORED_STACKS.get(player.getUniqueId()));
            MicroscopeUtils.STORED_STACKS.remove(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.getInventory().setItemInMainHand(MicroscopeUtils.STORED_STACKS.get(player.getUniqueId()));
                MicroscopeUtils.STORED_STACKS.remove(player.getUniqueId());
            }, 1L);
        }
    }
}
