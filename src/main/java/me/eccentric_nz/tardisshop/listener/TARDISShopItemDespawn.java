package me.eccentric_nz.tardisshop.listener;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.persistence.PersistentDataType;

public class TARDISShopItemDespawn implements Listener {

    private final TARDIS plugin;

    public TARDISShopItemDespawn(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShopItemDespawn(ItemDespawnEvent event) {
        if (event.getEntity().getPersistentDataContainer().has(plugin.getShopSettings().getItemKey(), PersistentDataType.INTEGER)) {
            event.setCancelled(true);
        }
    }
}
