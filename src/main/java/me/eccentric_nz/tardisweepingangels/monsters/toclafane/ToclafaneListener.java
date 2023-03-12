/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.monsters.toclafane;

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.WorldGuardChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ToclafaneListener implements Listener {

    private final TARDISWeepingAngels plugin;
    private final List<Material> drops = new ArrayList<>();

    public ToclafaneListener(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        plugin.getConfig().getStringList("toclafane.drops").forEach((d) -> {
            drops.add(Material.valueOf(d));
        });
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageToclafane(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if (entity instanceof ArmorStand stand && damager instanceof Player player) {
            if (stand.getPersistentDataContainer().has(TARDISWeepingAngels.TOCLAFANE, PersistentDataType.INTEGER)) {
                event.setCancelled(true);
                int maxHealth = (stand.getLocation().getWorld().getDifficulty().ordinal() * 6) + 1;
                int health = stand.getPersistentDataContainer().get(TARDISWeepingAngels.TOCLAFANE, PersistentDataType.INTEGER);
                if (health == maxHealth) {
                    // get the bee and make it angry
                    Bee bee = (Bee) stand.getVehicle();
                    if (bee == null) {
                        bee = (Bee) stand.getLocation().getWorld().spawnEntity(stand.getLocation(), EntityType.BEE);
                        PotionEffect p = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false);
                        bee.addPotionEffect(p);
                        bee.addPassenger(stand);
                    }
                    EntityEquipment ee = stand.getEquipment();
                    if (ee != null) {
                        ItemStack head = ee.getHelmet();
                        ItemMeta im = head.getItemMeta();
                        player.playSound(stand.getLocation(), "toclafane", 1.0f, 1.0f);
                        im.setCustomModelData(2);
                        head.setItemMeta(im);
                        ee.setHelmet(head);
                        bee.setHasStung(false);
                        bee.setHealth(bee.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                        bee.setAnger(500);
                        bee.setTarget(player);
                        bee.setSilent(true);
                        stand.getPersistentDataContainer().set(TARDISWeepingAngels.TOCLAFANE, PersistentDataType.INTEGER, maxHealth - 1);
                    }
                } else {
                    player.playSound(stand.getLocation(), "dalek_hit", 1.0f, 1.0f);
                    health--;
                    if (health == 0) {
                        Location location = stand.getLocation();
                        // kill the toclafane
                        if (stand.getVehicle() != null) {
                            Entity bee = stand.getVehicle();
                            if (bee instanceof Bee) {
                                stand.remove();
                                bee.remove();
                            }
                        } else {
                            stand.remove();
                        }
                        boolean destroy;
                        if (plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
                            destroy = (plugin.getConfig().getBoolean("toclafane.destroy_blocks")) && WorldGuardChecker.canExplode(location);
                        } else {
                            destroy = (plugin.getConfig().getBoolean("toclafane.destroy_blocks"));
                        }
                        // explode
                        location.getWorld().createExplosion(location, 2.0f, false, destroy);
                        // give drops
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            ItemStack stack = new ItemStack(drops.get(TARDISWeepingAngels.random.nextInt(drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                            location.getWorld().dropItemNaturally(location, stack);
                        }, 3L);
                    } else {
                        stand.getPersistentDataContainer().set(TARDISWeepingAngels.TOCLAFANE, PersistentDataType.INTEGER, health);
                    }
                }
            }
        } else if (entity instanceof Bee bee) {
            if (entity.getPassengers().size() > 0) {
                Entity passenger = entity.getPassengers().get(0);
                if (passenger instanceof ArmorStand && passenger.getPersistentDataContainer().has(TARDISWeepingAngels.TOCLAFANE, PersistentDataType.INTEGER)) {
                    bee.setHasStung(false);
                }
            }
        } else if (entity instanceof Player player && damager instanceof Bee bee) {
            if (damager.getPassengers().size() > 0) {
                Entity passenger = damager.getPassengers().get(0);
                if (passenger instanceof ArmorStand && passenger.getPersistentDataContainer().has(TARDISWeepingAngels.TOCLAFANE, PersistentDataType.INTEGER)) {
                    bee.setHasStung(false);
                    bee.setHealth(bee.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    bee.setTarget(player);
                }
            }
        }
    }

    @EventHandler
    public void onBeeTargetEvent(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Bee bee) {
            if (bee.getTarget() instanceof Player || bee.getAnger() >= 0) {
                if (bee.getPassengers().size() > 0) {
                    Entity passenger = bee.getPassengers().get(0);
                    if (passenger instanceof ArmorStand && passenger.getPersistentDataContainer().has(TARDISWeepingAngels.TOCLAFANE, PersistentDataType.INTEGER)) {
                        bee.setHasStung(false);
                    }
                }
            }
        }
    }
}
