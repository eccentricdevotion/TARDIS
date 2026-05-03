/*
 * Copyright (C) 2026 eccentric_nz
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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemContainerContents;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.keys.ArrowVariant;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.nms.TWAJudoon;
import me.eccentric_nz.tardisweepingangels.utils.ResetMonster;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class JudoonListener implements Listener {

    private final TARDIS plugin;

    public JudoonListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageJudoon(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Husk husk && event.getDamager() instanceof Player player) {
            if (husk.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID) && husk.getPersistentDataContainer().has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                event.setCancelled(true);
                player.playSound(husk.getLocation(), "judoon", 1.0f, 1.0f);
                if (!TARDISPermission.hasPermission(player, "tardisweepingangels.judoon")) {
                    return;
                }
                UUID judoonId = husk.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                Entity entity = ((CraftEntity) husk).getHandle();
                if (player.getUniqueId().equals(judoonId)) {
                    if (entity instanceof TWAJudoon judoon) {
                        int ammo = judoon.getAmmo();
                        if (Tag.SHULKER_BOXES.isTagged(player.getInventory().getItemInMainHand().getType())) {
                            // top up ammo
                            ItemStack box = player.getInventory().getItemInMainHand();
                            ItemContainerContents container = box.getData(DataComponentTypes.CONTAINER);
//                            List<ItemStack> inv = container.contents();
                            Inventory inv = Bukkit.createInventory(null, container.contents().size(), Component.text(""));
                            inv.setContents(container.contents().toArray(new ItemStack[0]));
                            if (inv.contains(Material.ARROW)) {
                                int a = inv.first(Material.ARROW);
                                ItemStack arrows = inv.getItem(a);
                                if (arrows.hasData(DataComponentTypes.ITEM_MODEL) && ArrowVariant.JUDOON_AMMO.getKey().equals(arrows.getData(DataComponentTypes.ITEM_MODEL))) {
                                    int remove = plugin.getMonstersConfig().getInt("judoon.ammunition") - ammo;
                                    if (arrows.getAmount() > remove) {
                                        arrows.setAmount(arrows.getAmount() - remove);
                                    } else {
                                        remove = arrows.getAmount();
                                        arrows = null;
                                    }
                                    inv.setItem(a, arrows);
                                    List<ItemStack> nonNullItems = Arrays.asList(inv.getContents()).stream()
                                            .filter(Objects::nonNull) // Remove any potential nulls
                                            .collect(Collectors.toList());
                                    box.setData(DataComponentTypes.CONTAINER, ItemContainerContents.containerContents()
                                            .addAll(nonNullItems)
                                            .build());
                                    judoon.setAmmo(ammo + remove);
                                    husk.customName(Component.text("Ammunition: " + (ammo + remove)));
                                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_RELOADED", remove);
                                }
                            }
                        } else {
                            // toggle guard mode
                            String message;
                            if (!judoon.isGuard()) {
                                if (!TARDISWeepingAngels.getPlayersWithGuards().contains(player.getUniqueId())) {
                                    // point weapon
                                    judoon.setGuard(true);
                                    message = "WA_ACTION";
                                    husk.customName(Component.text("Ammunition: " + ammo));
                                    husk.setCustomNameVisible(true);
                                    // add to repeating task
                                    TARDISWeepingAngels.getGuards().add(judoon.getUUID());
                                    TARDISWeepingAngels.getPlayersWithGuards().add(player.getUniqueId());
                                } else {
                                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_GUARD");
                                    return;
                                }
                            } else {
                                // stand easy
                                judoon.setGuard(false);
                                message = "WA_EASE";
                                husk.setCustomNameVisible(false);
                                // end guarding task
                                TARDISWeepingAngels.getGuards().remove(judoon.getUUID());
                                TARDISWeepingAngels.getPlayersWithGuards().remove(player.getUniqueId());
                            }
                            plugin.getMessenger().send(player, TardisModule.MONSTERS, message);
                        }
                    } else {
                        // reset because it had all the correct PDC
                        new ResetMonster(plugin, husk).reset();
                    }
                } else if (TARDISWeepingAngels.UNCLAIMED.equals(judoonId)) {
                    // claim the Judoon
                    UUID pid = player.getUniqueId();
                    husk.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, pid);
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_CLAIMED", "Judoon");
                    if (entity instanceof TWAJudoon judoon) {
                        judoon.setOwnerUUID(pid);
                    }
                }
            }
        }
    }
}
