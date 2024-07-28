package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class ElixirOfLifeListener implements Listener {

    private final TARDIS plugin;

    public ElixirOfLifeListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrinkElixir(PlayerItemConsumeEvent event) {
        ItemStack goblet = event.getItem();
        if (!ElixirOfLife.is(goblet)) {
            return;
        }
        new Regenerator().processPlayer(plugin, event.getPlayer());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!plugin.getConfig().getBoolean("modules.regeneration")) {
            // module not enabled
            return;
        }
        if (event.getEntity() instanceof Player player) {
            if (player.getHealth() - event.getDamage() > 0.0D) {
                // not dying yet
                return;
            }
            if (!TARDISPermission.hasPermission(player, "tardis.regeneration")) {
                // no permission
                return;
            }
            if (plugin.getTrackerKeeper().getJohnSmith().containsKey(player.getUniqueId())) {
                // not while chameleon arched
                return;
            }
            // regenerate!
            new Regenerator().processPlayer(plugin, player);
        }
    }
}
