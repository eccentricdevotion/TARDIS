/**
 *
 * This file is borrowed from ASkyBlock. (https://github.com/tastybento/acidisland)
 *
 * ASkyBlock is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * ASkyBlock is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * ASkyBlock. If not, see <http://www.gnu.org/licenses/>.
 *
 * Adapted by eccentric_nz for the TARDIS plugin
 *
 */
package me.eccentric_nz.TARDIS.skaro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.move.TARDISMoveSession;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAcidWater implements Listener {

    private final TARDIS plugin;
    private final List<Player> burningPlayers = new ArrayList<Player>();

    public TARDISAcidWater(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent e) {
        burningPlayers.remove(e.getEntity());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        // Fast return if acid isn't being used
        if (!plugin.getConfig().getBoolean("planets.Skaro.enabled") || !plugin.getConfig().getBoolean("planets.Skaro.acid")) {
            return;
        }
        final Player player = e.getPlayer();
        final Location loc = player.getLocation(); // Grab Location

        /**
         * Copyright (c) 2011, The Multiverse Team All rights reserved. Check
         * the Player has actually moved a block to prevent unneeded
         * calculations... This is to prevent huge performance drops on high
         * player count servers.
         */
        TARDISMoveSession tms = plugin.getTrackerKeeper().getTARDISMoveSession(player);
        tms.setStaleLocation(loc);

        // If the location is stale, ie: the player isn't actually moving xyz coords, they're looking around
        if (tms.isStaleLocation()) {
            return;
        }
        // Fast checks
        if (player.isDead()) {
            return;
        }
        // Check that they are in the Skaro world
        if (!player.getWorld().getName().equalsIgnoreCase("Skaro")) {
            return;
        }
        // Return if players are immune
        if (player.hasPermission("tardis.bypass.acid")) {
            return;
        }
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        // Slow checks
        final Block block = loc.getBlock();
        final Block head = block.getRelative(BlockFace.UP);

        // If they are not in liquid, then return
        if (!block.isLiquid() && !head.isLiquid()) {
            return;
        }
        // only in configured biomes

        // Find out if they are at the bottom of the sea and if so bounce them back up
        if (loc.getBlockY() < 1) {
            final Vector v = new Vector(player.getVelocity().getX(), 1D, player.getVelocity().getZ());
            player.setVelocity(v);
        }
        // If they are already burning in acid then return
        if (burningPlayers.contains(player)) {
            return;
        }
        // Check if they are in water
        if (block.getType().equals(Material.STATIONARY_WATER) || block.getType().equals(Material.WATER) || head.getType().equals(Material.STATIONARY_WATER) || head.getType().equals(Material.WATER)) {
            //plugin.getLogger().info("DEBUG: head = " + head.getType() + " body = " + block.getType());
            // Check if player is in a boat
            Entity playersVehicle = player.getVehicle();
            if (playersVehicle != null) {
                // They are in a Vehicle
                if (playersVehicle.getType().equals(EntityType.BOAT)) {
                    // I'M ON A BOAT! I'M ON A BOAT! A %^&&* BOAT!
                    return;
                }
            }
            // Check if player has an active water potion or not
            Collection<PotionEffect> activePotions = player.getActivePotionEffects();
            for (PotionEffect s : activePotions) {
                // plugin.getLogger().info("Potion is : " + s.getType().toString());
                if (s.getType().equals(PotionEffectType.WATER_BREATHING)) {
                    // Safe!
                    //plugin.getLogger().info("DEBUG: Water breathing potion protection!");
                    return;
                }
            }
            // ACID!
            //plugin.getLogger().info("DEBUG: Acid!");
            // Put the player into the acid list
            burningPlayers.add(player);
            // This runnable continuously hurts the player even if they are not
            // moving but are in acid.
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isDead()) {
                        burningPlayers.remove(player);
                        this.cancel();
                    } else if ((player.getLocation().getBlock().isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid()) && player.getLocation().getWorld().getName().equalsIgnoreCase("Skaro")) {
                        // plugin.getLogger().info("Damage setting = " + Settings.acidDamage);
                        // plugin.getLogger().info("Damage to player = " + (Settings.general_acidDamage - Settings.general_acidDamage * getDamageReduced(player)));
                        // plugin.getLogger().info("Player health is " + player.getHealth());
                        // Apply additional potion effects
                        // plugin.getLogger().info("Potion damage " + Settings.acidDamageType.toString());
                        if (!plugin.getConfig().getStringList("planets.Skaro.acid_potions").isEmpty()) {
                            for (String t : plugin.getConfig().getStringList("planets.Skaro.acid_potions")) {
                                PotionEffectType pet = PotionEffectType.getByName(t);
                                // plugin.getLogger().info("Applying " + pet.toString());
                                // player.addPotionEffect(new PotionEffect(t, 20, amplifier));
                                if (pet != null && (pet.equals(PotionEffectType.BLINDNESS) || pet.equals(PotionEffectType.CONFUSION) || pet.equals(PotionEffectType.HUNGER) || pet.equals(PotionEffectType.SLOW) || pet.equals(PotionEffectType.SLOW_DIGGING) || pet.equals(PotionEffectType.WEAKNESS))) {
                                    player.addPotionEffect(new PotionEffect(pet, 600, 1));
                                } else {
                                    // Poison
                                    player.addPotionEffect(new PotionEffect(pet, 200, 1));
                                }
                            }
                        }
                        // double health = player.getHealth();
                        // Apply damage if there is any
                        double ad = plugin.getConfig().getDouble("planets.Skaro.acid_damage");
                        if (ad > 0d) {
                            double health = player.getHealth() - (ad - ad * getDamageReduced(player));
                            if (health < 0D) {
                                health = 0D;
                            } else if (health > 20D) {
                                health = 20D;
                            }
                            player.setHealth(health);
                            player.getWorld().playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 3F, 3F);
                        }
                    } else {
                        burningPlayers.remove(player);
                        // plugin.getLogger().info("Cancelled!");
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);
        }
    }

    /**
     * @param player
     * @return A double between 0.0 and 0.80 that reflects how much armor the
     * player has on. The higher the value, the more protection they have.
     */
    static public double getDamageReduced(Player player) {
        org.bukkit.inventory.PlayerInventory inv = player.getInventory();
        ItemStack boots = inv.getBoots();
        ItemStack helmet = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack pants = inv.getLeggings();
        double red = 0.0;
        if (helmet != null) {
            switch (helmet.getType()) {
                case LEATHER_HELMET:
                    red += 0.04;
                    break;
                case GOLD_HELMET:
                    red += 0.08;
                    break;
                case CHAINMAIL_HELMET:
                    red += 0.08;
                    break;
                case IRON_HELMET:
                    red += 0.08;
                    break;
                case DIAMOND_HELMET:
                    red += 0.12;
                    break;
                default:
                    break;
            }
        }
        if (boots != null) {
            switch (boots.getType()) {
                case LEATHER_BOOTS:
                    red += 0.04;
                    break;
                case GOLD_BOOTS:
                    red += 0.04;
                    break;
                case CHAINMAIL_BOOTS:
                    red += 0.04;
                    break;
                case IRON_BOOTS:
                    red += 0.08;
                    break;
                case DIAMOND_BOOTS:
                    red += 0.12;
                    break;
                default:
                    break;
            }
        }
        // Pants
        if (pants != null) {
            switch (pants.getType()) {
                case LEATHER_LEGGINGS:
                    red += 0.08;
                    break;
                case GOLD_LEGGINGS:
                    red += 0.12;
                    break;
                case CHAINMAIL_LEGGINGS:
                    red += 0.16;
                    break;
                case IRON_LEGGINGS:
                    red += 0.20;
                    break;
                case DIAMOND_LEGGINGS:
                    red += 0.24;
                    break;
                default:
                    break;
            }
        }
        // Chest plate
        if (chest != null) {
            switch (chest.getType()) {
                case LEATHER_CHESTPLATE:
                    red += 0.12;
                    break;
                case GOLD_CHESTPLATE:
                    red += 0.20;
                    break;
                case CHAINMAIL_CHESTPLATE:
                    red += 0.20;
                    break;
                case IRON_CHESTPLATE:
                    red += 0.24;
                    break;
                case DIAMOND_CHESTPLATE:
                    red += 0.32;
                    break;
                default:
                    break;
            }
        }
        return red;
    }
}
