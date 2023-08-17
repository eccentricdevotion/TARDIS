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
package me.eccentric_nz.tardisweepingangels.monsters.k9;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class K9Listener implements Listener {

    private final TARDIS plugin;

    public K9Listener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onWolfTame(EntityTameEvent event) {
        if (event.getOwner() instanceof Player player) {
            if (!TARDISPermission.hasPermission(((Player) event.getOwner()), "tardisweepingangels.k9")) {
                return;
            }
            LivingEntity ent = event.getEntity();
            if (ent.getType().equals(EntityType.WOLF) && plugin.getMonstersConfig().getBoolean("k9.by_taming")) {
                Location location = ent.getLocation();
                if (!plugin.getMonstersConfig().getBoolean("k9.worlds." + location.getWorld().getName())) {
                    return;
                }
                Entity k9 = (Entity) new MonsterSpawner().createFollower(location, new Follower(UUID.randomUUID(), player.getUniqueId(), Monster.K9));
                K9Equipment.set(player, k9, false);
                ent.remove();
                player.playSound(k9.getLocation(), "k9", 1.0f, 1.0f);
                plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(k9, EntityType.ARMOR_STAND, Monster.K9, location));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onK9Interact(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!TARDISPermission.hasPermission(player, "tardisweepingangels.k9")) {
            return;
        }
        Entity k9 = event.getRightClicked();
        if (k9.getType().equals(EntityType.HUSK) && k9.getPersistentDataContainer().has(TARDISWeepingAngels.K9, PersistentDataType.INTEGER)) {
            if (k9.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                UUID uuid = player.getUniqueId();
                UUID k9Id = k9.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                if (uuid.equals(k9Id)) {
                    player.playSound(k9.getLocation(), "k9", 1.0f, 1.0f);
                    TWAFollower follower = (TWAFollower) ((CraftEntity) k9).getHandle();
                    // toggle following
                    follower.setFollowing(!follower.isFollowing());
                } else {
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_NOT_YOURS", "K9");
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onK9BoneEgg(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack is = event.getItem();
            if (is != null && is.getType().equals(Material.BONE) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName() && im.getDisplayName().equals("K9") && im.hasCustomModelData()) {
                    event.setCancelled(true);
                    Player player = event.getPlayer();
                    Location location = event.getClickedBlock().getLocation().add(0.5d, 1.0d, 0.5d);
                    if (!plugin.getMonstersConfig().getBoolean("k9.worlds." + location.getWorld().getName())) {
                        plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_SPAWN");
                        return;
                    }
                    // remove bone form inventory
                    if (is.getAmount() == 1) {
                        player.getInventory().setItemInMainHand(null);
                    } else {
                        is.setAmount(is.getAmount() - 1);
                        player.getInventory().setItemInMainHand(is);
                    }
                    // spawn a K9 instead
                    Entity k9 = (Entity) new MonsterSpawner().createFollower(location, new Follower(UUID.randomUUID(), player.getUniqueId(), Monster.K9));
                    K9Equipment.set(player, k9, false);
                    player.playSound(k9.getLocation(), "k9", 1.0f, 1.0f);
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(k9, EntityType.ARMOR_STAND, Monster.K9, location));
                }
            }
        }
    }
}
