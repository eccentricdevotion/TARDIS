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
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class K9Listener implements Listener {

    private final TARDIS plugin;

    public K9Listener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onWolfTame(EntityTameEvent event) {
        LivingEntity ent = event.getEntity();
        if (!ent.getType().equals(EntityType.WOLF) || !plugin.getMonstersConfig().getBoolean("k9.by_taming")) {
            return;
        }
        if (event.getOwner() instanceof Player player) {
            if (!TARDISPermission.hasPermission(((Player) event.getOwner()), "tardisweepingangels.k9")) {
                return;
            }
            Location location = ent.getLocation();
            if (!plugin.getMonstersConfig().getBoolean("k9.worlds." + location.getWorld().getName())) {
                return;
            }
            Entity k9 = new MonsterSpawner().createFollower(location, new Follower(UUID.randomUUID(), player.getUniqueId(), Monster.K9)).getBukkitEntity();
            K9Equipment.set(player, k9, false);
            ent.remove();
            player.playSound(k9.getLocation(), "k9", 1.0f, 1.0f);
            plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(k9, EntityType.HUSK, Monster.K9, location));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageK9(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Husk k9 && event.getDamager() instanceof Player player) {
            if (k9.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID) && k9.getPersistentDataContainer().has(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                event.setCancelled(true);
                player.playSound(k9.getLocation(), "k9", 1.0f, 1.0f);
                UUID k9Id = k9.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                if (!TARDISPermission.hasPermission(player, "tardisweepingangels.k9")) {
                    return;
                }
                if (TARDISWeepingAngels.UNCLAIMED.equals(k9Id)) {
                    // claim this K9
                    k9.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, player.getUniqueId());
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_CLAIMED", "K-9");
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
                    Entity k9 = new MonsterSpawner().createFollower(location, new Follower(UUID.randomUUID(), player.getUniqueId(), Monster.K9)).getBukkitEntity();
                    K9Equipment.set(player, k9, false);
                    player.playSound(k9.getLocation(), "k9", 1.0f, 1.0f);
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(k9, EntityType.HUSK, Monster.K9, location));
                }
            }
        }
    }
}
