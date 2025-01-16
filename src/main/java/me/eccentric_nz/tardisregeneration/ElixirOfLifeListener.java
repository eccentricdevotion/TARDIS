package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

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
            if (!TARDISPermission.hasPermission(player, "tardis.regenerate")) {
                // no permission
                return;
            }
            if (plugin.getTrackerKeeper().getJohnSmith().containsKey(player.getUniqueId())) {
                // not while chameleon arched
                return;
            }
            if (hasTotem(player)) {
                // totem of undying
                return;
            }
            // regenerate!
            new Regenerator().processPlayer(plugin, player);
        }
    }

    private boolean hasTotem(Player player) {
        ItemStack totem = player.getInventory().getItemInMainHand();
        return totem.getType() == Material.TOTEM_OF_UNDYING;
    }

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY) && player.isInvulnerable()) {
                event.setCancelled(true);
            }
        }
    }
}
