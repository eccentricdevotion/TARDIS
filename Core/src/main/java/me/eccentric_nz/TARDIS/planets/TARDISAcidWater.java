/*
  This file is borrowed from ASkyBlock. (https://github.com/tastybento/acidisland)
  <p>
  ASkyBlock is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
  License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
  version.
  <p>
  ASkyBlock is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
  details.
  <p>
  You should have received a copy of the GNU General Public License along with ASkyBlock. If not, see
  <http://www.gnu.org/licenses/>.
  <p>
  Adapted by eccentric_nz for the TARDIS plugin
 */
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Whoniverse;
import me.eccentric_nz.TARDIS.move.TARDISMoveSession;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISAcidWater implements Listener {

    private final TARDIS plugin;
    private final List<Player> burningPlayers = new ArrayList<>();

    public TARDISAcidWater(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (burningPlayers.contains(event.getEntity())) {
            String name = event.getEntity().getName();
            event.setDeathMessage(name + " was dissolved in acid");
        }
        burningPlayers.remove(event.getEntity());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {

        Player player = e.getPlayer();
        Location loc = player.getLocation();

        TARDISMoveSession tms = plugin.getTrackerKeeper().getTARDISMoveSession(player);

        // if the location is stale, ie: the player isn't actually moving xyz coords, they're looking around
        if (tms.isStaleLocation()) {
            return;
        }
        // fast checks
        if (player.isDead()) {
            return;
        }
        // check that they are in the Skaro world
        if (!player.getWorld().getName().equalsIgnoreCase("skaro")) {
            return;
        }
        // return if players are immune
        if (TARDISPermission.hasPermission(player, "tardis.acid.bypass")) {
            return;
        }
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        // slow checks
        Block block = loc.getBlock();
        Block head = block.getRelative(BlockFace.UP);

        // if they are not in liquid, then return
        if (!block.isLiquid() && !head.isLiquid()) {
            return;
        }
        // if they are already burning in acid then return
        if (burningPlayers.contains(player)) {
            return;
        }
        // check if they are in water
        if (block.getType().equals(Material.WATER) || head.getType().equals(Material.WATER)) {
            // check if player is in a boat
            Entity playersVehicle = player.getVehicle();
            if (playersVehicle != null) {
                // they are in a Vehicle
                if (isBoat(playersVehicle.getType())) {
                    // I'M ON A BOAT! I'M ON A BOAT! A %^&&* BOAT!
                    return;
                }
            }
            // check if player has an active water potion or not
            Collection<PotionEffect> activePotions = player.getActivePotionEffects();
            for (PotionEffect s : activePotions) {
                if (s.getType().equals(PotionEffectType.WATER_BREATHING)) {
                    // Safe!
                    return;
                }
            }
            // ACID!
            // put the player into the acid list
            burningPlayers.add(player);
            // this runnable continuously hurts the player even if they are not moving but are in acid.
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isDead()) {
                        burningPlayers.remove(player);
                        cancel();
                    } else if ((player.getLocation().getBlock().isLiquid() || player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid()) && player.getLocation().getWorld().getName().equalsIgnoreCase("skaro")) {
                        // apply additional potion effects
                        if (!plugin.getPlanetsConfig().getStringList("planets.skaro.acid_potions").isEmpty()) {
                            plugin.getPlanetsConfig().getStringList("planets.skaro.acid_potions").forEach((t) -> {
                                PotionEffectType pet = PotionEffectType.getByName(t);
                                if (pet != null) {
                                    if (pet.equals(PotionEffectType.BLINDNESS) || pet.equals(PotionEffectType.NAUSEA) || pet.equals(PotionEffectType.HUNGER) || pet.equals(PotionEffectType.SLOWNESS) || pet.equals(PotionEffectType.MINING_FATIGUE) || pet.equals(PotionEffectType.WEAKNESS)) {
                                        player.addPotionEffect(new PotionEffect(pet, 200, 1));
                                    } else {
                                        // poison
                                        player.addPotionEffect(new PotionEffect(pet, 50, 1));
                                    }
                                }
                            });
                        }
                        // apply damage if there is any
                        double ad = plugin.getPlanetsConfig().getDouble("planets.skaro.acid_damage");
                        if (ad > 0d) {
                            double damage = (ad - ad * getDamageReduced(player)) / 2.5d;
                            player.damage(damage, DamageSource.builder(DamageType.GENERIC).build());
                            player.getWorld().playSound(loc, Sound.ENTITY_CREEPER_PRIMED, 3F, 3F);
                        }
                    } else {
                        burningPlayers.remove(player);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);
        }
    }

    /**
     * @param player The player to get damage for
     * @return A double between 0.0 and 0.80 that reflects how much armor the player has on. The higher the value, the
     * more protection they have.
     */
    private double getDamageReduced(Player player) {
        PlayerInventory inv = player.getInventory();
        ItemStack boots = inv.getBoots();
        ItemStack helmet = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack pants = inv.getLeggings();
        double reduction = 0.0;
        if (helmet != null) {
            switch (helmet.getType()) {
                case TURTLE_HELMET -> reduction += 0.02;
                case LEATHER_HELMET -> reduction += 0.04;
                case GOLDEN_HELMET -> reduction += 0.06;
                case CHAINMAIL_HELMET -> reduction += 0.08;
                case IRON_HELMET -> reduction += 0.10;
                case DIAMOND_HELMET -> reduction += 0.12;
                case NETHERITE_HELMET -> reduction += 0.14;
                default -> {
                }
            }
        }
        if (boots != null) {
            switch (boots.getType()) {
                case LEATHER_BOOTS -> reduction += 0.04;
                case GOLDEN_BOOTS -> reduction += 0.06;
                case CHAINMAIL_BOOTS -> reduction += 0.08;
                case IRON_BOOTS -> reduction += 0.10;
                case DIAMOND_BOOTS -> reduction += 0.12;
                case NETHERITE_BOOTS -> reduction += 0.14;
                default -> {
                }
            }
        }
        // Pants
        if (pants != null) {
            switch (pants.getType()) {
                case LEATHER_LEGGINGS -> reduction += 0.08;
                case GOLDEN_LEGGINGS -> reduction += 0.12;
                case CHAINMAIL_LEGGINGS -> reduction += 0.16;
                case IRON_LEGGINGS -> reduction += 0.20;
                case DIAMOND_LEGGINGS -> reduction += 0.24;
                case NETHERITE_LEGGINGS -> reduction += 0.28;
                default -> {
                }
            }
        }
        // Chest plate
        if (chest != null) {
            switch (chest.getType()) {
                case LEATHER_CHESTPLATE -> reduction += 0.12;
                case GOLDEN_CHESTPLATE -> reduction += 0.16;
                case CHAINMAIL_CHESTPLATE -> reduction += 0.20;
                case IRON_CHESTPLATE -> reduction += 0.24;
                case DIAMOND_CHESTPLATE -> reduction += 0.28;
                case NETHERITE_CHESTPLATE -> reduction += 0.32;
                default -> {
                }
            }
        }
        return reduction;
    }

    @EventHandler
    public void onFillAcidBucket(PlayerBucketFillEvent event) {
        Player p = event.getPlayer();
        if (!p.getWorld().getName().equalsIgnoreCase("skaro")) {
            return;
        }
        Material type = event.getBlockClicked().getType();
        ItemStack bucket = event.getItemStack();
        ItemMeta im = bucket.getItemMeta();
        if (type.equals(Material.WATER)) {
            im.setDisplayName("Acid Bucket");
            im.setItemModel(Whoniverse.ACID_BUCKET.getKey());
        }
        if (type.equals(Material.LAVA)) {
            im.setDisplayName("Rust Bucket");
            im.setItemModel(Whoniverse.RUST_BUCKET.getKey());
        }
        bucket.setItemMeta(im);
        p.updateInventory();
    }

    private boolean isBoat(EntityType type) {
        switch (type) {
            case ACACIA_BOAT, BIRCH_BOAT, CHERRY_BOAT,
                 DARK_OAK_BOAT, JUNGLE_BOAT, MANGROVE_BOAT,
                 OAK_BOAT, PALE_OAK_BOAT, SPRUCE_BOAT -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
