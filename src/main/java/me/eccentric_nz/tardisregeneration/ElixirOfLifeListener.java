/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
