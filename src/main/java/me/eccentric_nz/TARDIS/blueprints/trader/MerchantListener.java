package me.eccentric_nz.TARDIS.blueprints.trader;

import io.papermc.paper.event.player.PlayerPurchaseEvent;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class MerchantListener implements Listener {

    private final TARDIS plugin;

    public MerchantListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTrade(PlayerPurchaseEvent event) {
        Player player = event.getPlayer();
        // find the mannequin - entity interaction range is maximum 5 blocks
        for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
            if (entity instanceof Mannequin mannequin) {
                if (mannequin.getPersistentDataContainer().has(plugin.getTradesKey(), PersistentDataType.INTEGER)) {
                    int count = mannequin.getPersistentDataContainer().getOrDefault(plugin.getTradesKey(), PersistentDataType.INTEGER, 0);
                    mannequin.getPersistentDataContainer().set(plugin.getTradesKey(), PersistentDataType.INTEGER, count + 1);
                }
            }
        }
    }
}
