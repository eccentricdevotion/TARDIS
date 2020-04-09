package me.eccentric_nz.TARDIS.chemistry.product;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BalloonListener implements Listener {

    private final TARDIS plugin;

    public BalloonListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerHoldBalloon(PlayerItemHeldEvent event) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Player player = event.getPlayer();
            int factor = -1;
            if (isBalloon(player.getInventory().getItemInMainHand())) {
                factor += 1;
            }
            if (isBalloon(player.getInventory().getItemInOffHand())) {
                factor += 1;
            }
            removeJumpBoost(player);
            if (factor > -1) {
                PotionEffect potionEffect = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, factor);
                player.addPotionEffect(potionEffect, true);
            }
        }, 1L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropBalloon(PlayerDropItemEvent event) {
        if (isBalloon(event.getItemDrop().getItemStack())) {
            Player player = event.getPlayer();
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (!isBalloon(player.getInventory().getItemInMainHand())) {
                    removeJumpBoost(player);
                }
            }, 1L);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupBalloon(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                Player player = (Player) event.getEntity();
                int factor = -1;
                if (isBalloon(event.getItem().getItemStack())) {
                    if (isBalloon(player.getInventory().getItemInMainHand())) {
                        factor += 1;
                    }
                    if (isBalloon(player.getInventory().getItemInOffHand())) {
                        factor += 1;
                    }
                    removeJumpBoost(player);
                    if (factor > -1) {
                        PotionEffect potionEffect = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, factor);
                        player.addPotionEffect(potionEffect, true);
                    }
                }
            }, 1L);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMoveBalloon(InventoryClickEvent event) {
        if (isBalloon(event.getCursor()) || isBalloon(event.getCurrentItem()) || event.getClick() == ClickType.NUMBER_KEY) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                Player player = (Player) event.getWhoClicked();
                int factor = 1;
                if (!isBalloon(player.getInventory().getItemInMainHand())) {
                    factor -= 1;
                }
                if (!isBalloon(player.getInventory().getItemInOffHand())) {
                    factor -= 1;
                }
                if (factor > -1) {
                    PotionEffect potionEffect = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, factor);
                    player.addPotionEffect(potionEffect, true);
                } else {
                    removeJumpBoost(player);
                }
            }, 1L);
        }
    }

    private boolean isBalloon(ItemStack is) {
        return is != null && is.getType().equals(Material.CORNFLOWER) && is.hasItemMeta() && is.getItemMeta().hasCustomModelData() && isInDataRange(is.getItemMeta().getCustomModelData());
    }

    private boolean isInDataRange(int custom) {
        return (custom > 10000018 && custom < 10000035);
    }

    private void removeJumpBoost(Player player) {
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.JUMP);
            if (potionEffect.getDuration() > 150000000) {
                player.removePotionEffect(PotionEffectType.JUMP);
            }
        }
    }
}
