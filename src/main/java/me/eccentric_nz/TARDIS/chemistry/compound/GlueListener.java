/*
 * Copyright (C) 2021 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.chemistry.compound;

import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.PistonHead;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GlueListener implements Listener {

    @EventHandler
    public void onGlueUse(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && EquipmentSlot.HAND.equals(event.getHand())) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (TARDISPermission.hasPermission(player, "tardis.chemistry.glue") && block != null && block.getType().equals(Material.PISTON)) {
                ItemStack is = event.getItem();
                if (is != null && is.getType().equals(Material.GLASS_BOTTLE) && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName() && im.getDisplayName().equals("Glue") && im.hasCustomModelData() && im.getCustomModelData() == 10000011) {
                        player.playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1.0f, 1.0f);
                        // switch piston to sticky piston
                        Piston blockData = (Piston) block.getBlockData();
                        Piston piston = (Piston) Material.STICKY_PISTON.createBlockData();
                        piston.setExtended(blockData.isExtended());
                        piston.setFacing(blockData.getFacing());
                        if (blockData.isExtended()) {
                            // get the PistonHead
                            Block head = block.getRelative(blockData.getFacing());
                            PistonHead sticky = (PistonHead) head.getBlockData();
                            sticky.setType(PistonHead.Type.STICKY);
                            head.setBlockData(sticky);
                        }
                        block.setBlockData(piston);
                        // remove glue
                        int amount = is.getAmount() - 1;
                        player.getInventory().getItemInMainHand().setAmount(amount);
                        player.updateInventory();
                    }
                }
            }
        }
    }
}
