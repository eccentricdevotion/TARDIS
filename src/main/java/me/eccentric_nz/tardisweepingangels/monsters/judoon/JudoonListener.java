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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Husk;
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

import java.util.UUID;

public class JudoonListener implements Listener {

    private final TARDIS plugin;

    public JudoonListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageJudoon(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Husk husk && event.getDamager() instanceof Player player) {
            if (husk.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID) && husk.getPersistentDataContainer().has(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER)) {
                event.setCancelled(true);
                player.playSound(husk.getLocation(), "judoon", 1.0f, 1.0f);
                if (!TARDISPermission.hasPermission(player, "tardisweepingangels.judoon")) {
                    return;
                }
                UUID judoonId = husk.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                if (player.getUniqueId().equals(judoonId)) {
                    int ammo = husk.getPersistentDataContainer().get(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER);
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
                                int remove = plugin.getMonstersConfig().getInt("judoon.ammunition") - ammo;
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
                                husk.getPersistentDataContainer().set(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER, (ammo + remove));
                                husk.setCustomName("Ammunition: " + (ammo + remove));
                                plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_RELOADED", remove);
                            }
                        }
                    } else {
                        // TODO toggle guard mode
                        // TODO add projectile firing to TWAJudoon - borrow target/bow code from Skeleton?
                        // TODO need models for firing gun while walking - alter TWAJudoon `aiStep()`
                        ItemStack arm = husk.getEquipment().getItemInMainHand();
                        ItemMeta im = arm.getItemMeta();
                        int cmd = im.getCustomModelData();
                        if (cmd == 4 && ammo > 0) {
                            if (!TARDISWeepingAngels.getPlayersWithGuards().contains(player.getUniqueId())) {
                                plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_ACTION");
                                // add to repeating task
                                TARDISWeepingAngels.getGuards().add(husk.getUniqueId());
                                TARDISWeepingAngels.getPlayersWithGuards().add(player.getUniqueId());
                            } else {
                                plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_GUARD");
                            }
                            // point weapon
                            cmd = 9;
                            husk.setCustomName("Ammunition: " + ammo);
                            husk.setCustomNameVisible(true);
                        } else {
                            // stand easy
                            cmd = 4;
                            husk.setCustomNameVisible(false);
                            plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_EASE");
                            // end guarding task
                            TARDISWeepingAngels.getGuards().remove(husk.getUniqueId());
                            TARDISWeepingAngels.getPlayersWithGuards().remove(player.getUniqueId());
                        }
                        im.setCustomModelData(cmd);
                        arm.setItemMeta(im);
                        husk.getEquipment().setItemInMainHand(arm);
                    }
                } else if (judoonId.equals(TARDISWeepingAngels.UNCLAIMED)) {
                    // claim the Judoon
                    husk.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, player.getUniqueId());
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_CLAIMED", "Judoon");
                }
            }
        }
    }
}
