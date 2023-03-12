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
package me.eccentric_nz.tardisweepingangels.death;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

public class PlayerDeath implements Listener {

    private final TARDISWeepingAngels plugin;

    public PlayerDeath(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        EntityDamageEvent damage = event.getEntity().getLastDamageCause();
        if (damage != null) {
            if (damage instanceof EntityDamageByEntityEvent damageByEntity) {
                Entity attacker = damageByEntity.getDamager();
                if (damage.getCause().equals(DamageCause.ENTITY_ATTACK)) {
                    PersistentDataContainer pdc = attacker.getPersistentDataContainer();
                    String name = event.getEntity().getName();
                    if (attacker instanceof Zombie) {
                        if (pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)) {
                            String what_happened = (plugin.getConfig().getBoolean("cybermen.can_upgrade")) ? "upgraded" : "slain";
                            event.setDeathMessage(name + " was " + what_happened + " by a Cyberman");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by an Empty Child");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.ZYGON, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by a Zygon");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by a Slitheen");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by a Sontaran");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.VASHTA, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was eaten by a Vashta Nerada");
                            return;
                        }
                    }
                    if (attacker instanceof Guardian) {
                        Entity silent = attacker.getVehicle();
                        if (silent != null && silent.getType().equals(EntityType.SKELETON)) {
                            event.setDeathMessage(name + " was slain by a Silent");
                            return;
                        }
                    }
                    if (attacker instanceof Bee) {
                        if (!attacker.getPassengers().isEmpty()) {
                            Entity passenger = attacker.getPassengers().get(0);
                            if (passenger != null && passenger.getType().equals(EntityType.ARMOR_STAND)) {
                                event.setDeathMessage(name + " was slain by a Toclafane");
                                return;
                            }
                        }
                    }
                    if (attacker instanceof PigZombie) {
                        if (pdc.has(TARDISWeepingAngels.DALEK_SEC, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by Dalek Sec");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.DAVROS, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was exterminated by Davros");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by an Ice Warrior");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.STRAX, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by a very angry Sontaran butler called Strax");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.HATH, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was killed by a Hath blaster rifle");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by a Racnoss");
                            return;
                        }
                    }
                    if (attacker instanceof Skeleton) {
                        if (pdc.has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by a Weeping Angel");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by a Silurian");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by a Headless Monk");
                            return;
                        }
                        if (pdc.has(TARDISWeepingAngels.MIRE, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by the Mire");
                            return;
                        }
                        if (!attacker.getPassengers().isEmpty()) {
                            Entity passenger = attacker.getPassengers().get(0);
                            if (passenger != null && passenger.getType().equals(EntityType.GUARDIAN)) {
                                event.setDeathMessage(name + " was slain by a Silent");
                                return;
                            }
                        }
                    }
                    if (attacker instanceof Drowned && pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)) {
                        event.setDeathMessage(name + " was slain by a Sea Devil");
                        return;
                    }
                    if (attacker instanceof PiglinBrute && pdc.has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)) {
                        event.setDeathMessage(name + " was slain by a Racnoss");
                        return;
                    }
                }
                if (attacker.getType().equals(EntityType.ARROW)) {
                    Projectile arrow = (Arrow) attacker;
                    ProjectileSource source = arrow.getShooter();
                    if (source instanceof Skeleton skeleton) {
                        PersistentDataContainer spdc = skeleton.getPersistentDataContainer();
                        String name = event.getEntity().getName();
                        if (spdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by a Dalek");
                            return;
                        }
                        if (spdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                            event.setDeathMessage(name + " was slain by a Silurian");
                        }
                    }
                }
            } else if (damage.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
                if (event.getDeathMessage().contains("Bee")) {
                    event.setDeathMessage(event.getEntity().getName() + " was slain by a Toclafane");
                }
            }
        }
    }
}
