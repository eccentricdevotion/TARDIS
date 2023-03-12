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
package me.eccentric_nz.tardisweepingangels.monsters.judoon;

import java.util.UUID;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class JudoonListener implements Listener {

    private final TARDISWeepingAngels plugin;

    public JudoonListener(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageJudoon(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ArmorStand stand && event.getDamager() instanceof Player player) {
            if (stand.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID) && stand.getPersistentDataContainer().has(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER)) {
                event.setCancelled(true);
                player.playSound(stand.getLocation(), "judoon", 1.0f, 1.0f);
                if (!player.hasPermission("tardisweepingangels.judoon")) {
                    return;
                }
                UUID judoonId = stand.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                if (player.getUniqueId().equals(judoonId)) {
                    int ammo = stand.getPersistentDataContainer().get(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER);
                    if (Tag.SHULKER_BOXES.isTagged(player.getInventory().getItemInMainHand().getType())) {
                        // top up ammo
                        ItemStack box = player.getInventory().getItemInMainHand();
                        BlockStateMeta bsm = (BlockStateMeta) box.getItemMeta();
                        ShulkerBox shulkerBox = (ShulkerBox) bsm.getBlockState();
                        Inventory inv = shulkerBox.getInventory();
                        if (inv.contains(Material.ARROW)) {
                            int a = inv.first(Material.ARROW);
                            ItemStack arrows = inv.getItem(a);
                            ItemMeta aim = arrows.getItemMeta();
                            if (aim.hasCustomModelData() && aim.getCustomModelData() == 13) {
                                int remove = plugin.getConfig().getInt("judoon.ammunition") - ammo;
                                if (arrows.getAmount() > remove) {
                                    arrows.setAmount(arrows.getAmount() - remove);
                                } else {
                                    remove = arrows.getAmount();
                                    arrows = null;
                                }
                                inv.setItem(a, arrows);
                                shulkerBox.update();
                                bsm.setBlockState(shulkerBox);
                                box.setItemMeta(bsm);
                                stand.getPersistentDataContainer().set(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER, (ammo + remove));
                                stand.setCustomName("Ammunition: " + (ammo + remove));
                                player.sendMessage(plugin.pluginName + "You reloaded " + remove + " Judoon ammunition.");
                            }
                        }
                    } else {
                        ItemStack arm = stand.getEquipment().getItemInMainHand();
                        ItemMeta im = arm.getItemMeta();
                        int cmd = im.getCustomModelData();
                        if (cmd == 4 && ammo > 0) {
                            if (!plugin.getPlayersWithGuards().contains(player.getUniqueId())) {
                                player.sendMessage(plugin.pluginName + "Judoon ready for action.");
                                // add to repeating task
                                plugin.getGuards().add(stand.getUniqueId());
                                plugin.getPlayersWithGuards().add(player.getUniqueId());
                            } else {
                                player.sendMessage(plugin.pluginName + "You already have a Judoon guard!");
                            }
                            // point weapon
                            cmd = 9;
                            stand.setCustomName("Ammunition: " + ammo);
                            stand.setCustomNameVisible(true);
                        } else {
                            // stand easy
                            cmd = 4;
                            stand.setCustomNameVisible(false);
                            player.sendMessage(plugin.pluginName + "Judoon standing at ease.");
                            // end guarding task
                            plugin.getGuards().remove(stand.getUniqueId());
                            plugin.getPlayersWithGuards().remove(player.getUniqueId());
                        }
                        im.setCustomModelData(cmd);
                        arm.setItemMeta(im);
                        stand.getEquipment().setItemInMainHand(arm);
                    }
                } else if (judoonId.equals(TARDISWeepingAngels.UNCLAIMED)) {
                    // claim the Judoon
                    stand.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, player.getUniqueId());
                    player.sendMessage(TARDISWeepingAngels.plugin.pluginName + "You have claimed this Judoon!");
                }
            }
        }
    }
}
