/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The seemingly male Sontarans could be gene spliced to produce milk. Strax was very proud that he could produce
 * "magnificent quantities" of lactic fluid and offered to nurse Melody Pond.
 *
 * @author eccentric_nz
 */
public class MonsterInteractListener implements Listener {

    private final List<UUID> milkers = new ArrayList<>();
    private final TARDIS plugin;

    public MonsterInteractListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onMonsterInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!(entity instanceof org.bukkit.entity.Monster monster)) {
            return;
        }
        EntityEquipment ee = monster.getEquipment();
        ItemStack h = ee.getHelmet();
        if (!h.hasItemMeta()) {
            return;
        }
        ItemMeta im = h.getItemMeta();
        if (!im.hasDisplayName()) {
            return;
        }
        if (entity instanceof Zombie zombie) {
            if (h.getType().equals(Material.POTATO)) {
                if (im.getDisplayName().startsWith("Sontaran")) {
                    Player p = event.getPlayer();
                    ItemStack is = p.getInventory().getItemInMainHand();
                    if (is.getType().equals(Material.POTION)) {
                        PotionMeta potionMeta = (PotionMeta) is.getItemMeta();
                        if (potionMeta != null && potionMeta.getBasePotionType() != null && potionMeta.getBasePotionType().equals(PotionType.WEAKNESS)) {
                            // remove the potion
                            int a = p.getInventory().getItemInMainHand().getAmount();
                            int a2 = a - 1;
                            if (a2 > 0) {
                                p.getInventory().getItemInMainHand().setAmount(a2);
                            } else {
                                p.getInventory().removeItem(is);
                            }
                            // switch the armour to a butler uniform
                            Location l = zombie.getLocation();
                            zombie.remove();
                            PigZombie strax = (PigZombie) new MonsterSpawner().create(l, Monster.STRAX);
                            strax.setAngry(false);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                new Equipper(Monster.STRAX, strax, false).setHelmetAndInvisibility();
                                strax.setCustomName("Strax");
                                strax.getPersistentDataContainer().set(TARDISWeepingAngels.STRAX, PersistentDataType.INTEGER, Monster.STRAX.ordinal());
                                strax.getPersistentDataContainer().remove(TARDISWeepingAngels.SONTARAN);
                                plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(strax, EntityType.ZOMBIFIED_PIGLIN, Monster.STRAX, l));
                            }, 2L);
                        }
                    }
                    return;
                }
            }
            if (ee.getHelmet().getType().equals(Material.BAKED_POTATO)) {
                if (im.getDisplayName().startsWith("Strax")) {
                    Player p = event.getPlayer();
                    UUID uuid = p.getUniqueId();
                    ItemStack is = p.getInventory().getItemInMainHand();
                    if (is.getType().equals(Material.BUCKET)) {
                        if (!milkers.contains(uuid)) {
                            milkers.add(uuid);
                            p.playSound(zombie.getLocation(), "milk", 1.0f, 1.0f);
                            ItemStack milk = new ItemStack(Material.MILK_BUCKET);
                            ItemMeta m = milk.getItemMeta();
                            m.setDisplayName("Sontaran Lactic Fluid");
                            milk.setItemMeta(m);
                            p.getEquipment().setItemInMainHand(milk);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> milkers.remove(uuid), 3000L);
                        } else {
                            plugin.getMessenger().send(p, TardisModule.MONSTERS, "WA_STRAX");
                        }
                    } else if (event.getHand().equals(EquipmentSlot.HAND)) {
                        p.playSound(zombie.getLocation(), "strax", 1.0f, 1.0f);
                    }
                }
            }
        }
        if (((CraftEntity) entity).getHandle() instanceof TWAFollower follower) {
            if (event.getHand() != EquipmentSlot.HAND) {
                return;
            }
            // toggle following status
            PersistentDataContainer pdc = entity.getPersistentDataContainer();
            if (!pdc.has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                return;
            }
            Player player = event.getPlayer();
            // get current follower status
            if (pdc.has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                Follow.toggle(plugin, player, entity, "Ood", !follower.isFollowing());
            } else if (pdc.has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                Follow.toggle(plugin, player, entity, "Judoon", !follower.isFollowing());
            } else if (pdc.has(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                Follow.toggle(plugin, player, entity, "K9", !follower.isFollowing());
            }
        }
    }
}
