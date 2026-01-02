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
package me.eccentric_nz.TARDIS.listeners;

import com.destroystokyo.paper.MaterialTags;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAntiBuild;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISAntiBuild;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISAntiBuildListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> no_place = new ArrayList<>();
    private final List<Material> allow_interact = new ArrayList<>();

    public TARDISAntiBuildListener(TARDIS plugin) {
        this.plugin = plugin;
        no_place.add(Material.CHEST_MINECART);
        no_place.add(Material.EGG);
        no_place.add(Material.FLINT_AND_STEEL);
        no_place.add(Material.FURNACE_MINECART);
        no_place.add(Material.HOPPER_MINECART);
        no_place.add(Material.ITEM_FRAME);
        no_place.add(Material.MINECART);
        no_place.add(Material.PAINTING);
        no_place.add(Material.SHEARS);
        no_place.add(Material.TNT_MINECART);
        no_place.addAll(MaterialTags.BUCKETS.getValues());
        no_place.addAll(MaterialTags.SPAWN_EGGS.getValues());
        no_place.addAll(Tag.ITEMS_BOATS.getValues());
        allow_interact.add(Material.COMPARATOR);
        allow_interact.add(Material.LEVER);
        allow_interact.add(Material.REPEATER);
        allow_interact.addAll(Tag.BUTTONS.getValues());
        allow_interact.addAll(Tag.DOORS.getValues());
        allow_interact.addAll(Tag.FENCE_GATES.getValues());
        allow_interact.addAll(Tag.PRESSURE_PLATES.getValues());
        allow_interact.addAll(Tag.TRAPDOORS.getValues());
    }

    @EventHandler
    public void onFlowerPotInteract(PlayerFlowerPotManipulateEvent event) {
        Player p = event.getPlayer();
        if (!plugin.getUtils().inTARDISWorld(p)) {
            return;
        }
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getUniqueId());
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        event.setCancelled(true);
        plugin.getMessenger().send(p, TardisModule.TARDIS, "ANTIBUILD");
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionBuild(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        if (!plugin.getUtils().inTARDISWorld(p)) {
            return;
        }
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getUniqueId());
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        Vector v = event.getBlock().getLocation().toVector();
        TARDISAntiBuild tab = plugin.getTrackerKeeper().getAntiBuild().get(rs.getTardis_id());
        if (v.isInAABB(tab.getMin(), tab.getMax())) {
            event.setCancelled(true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "ANTIBUILD_TIMELORD", tab.getTimelord());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        if (!plugin.getUtils().inTARDISWorld(p)) {
            return;
        }
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getUniqueId());
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        Vector v = event.getBlock().getLocation().toVector();
        TARDISAntiBuild tab = plugin.getTrackerKeeper().getAntiBuild().get(rs.getTardis_id());
        if (v.isInAABB(tab.getMin(), tab.getMax())) {
            event.setCancelled(true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "ANTIBUILD_TIMELORD", tab.getTimelord());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionPlace(PlayerInteractEvent event) {
        // ignore non-hand click actions
        if (event.getAction().equals(Action.PHYSICAL)) {
            return;
        }
        Player p = event.getPlayer();
        if (!plugin.getUtils().inTARDISWorld(p)) {
            return;
        }
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getUniqueId());
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        EquipmentSlot hand = event.getHand();
        if (hand == null) {
            return;
        }
        ItemStack t = (hand.equals(EquipmentSlot.HAND)) ? p.getInventory().getItemInMainHand() : p.getInventory().getItemInOffHand();
        Material m = t.getType();
        if ((hand.equals(EquipmentSlot.HAND) && no_place.contains(m)) || (hand.equals(EquipmentSlot.OFF_HAND) && no_place.contains(m)) && !allow_interact.contains(event.getClickedBlock().getType())) {
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "ANTIBUILD");
        }
//        if (event.getClickedBlock().getType().equals(Material.FLOWER_POT) && (hand.equals(EquipmentSlot.HAND) && no_flower_pot.contains(m)) || (hand.equals(EquipmentSlot.OFF_HAND) && no_flower_pot.contains(m))) {
//            event.setUseItemInHand(Result.DENY);
//            event.setCancelled(true);
//            Block b = event.getClickedBlock();
//            b.getState().update();
//            for (BlockFace f : plugin.getGeneralKeeper().getFaces()) {
//                if (b.getRelative(f).getType().isAir()) {
//                    b.getRelative(f).setBlockData(TARDISConstants.GLASS, true);
//                    b.getRelative(f).setBlockData(TARDISConstants.AIR, true);
//                    break;
//                }
//            }
//            plugin.getMessenger().send(p, TardisModule.TARDIS, "ANTIBUILD");
//        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionEntityClick(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        if (!plugin.getUtils().inTARDISWorld(p)) {
            return;
        }
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getUniqueId());
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        Vector v = event.getRightClicked().getLocation().toVector();
        TARDISAntiBuild tab = plugin.getTrackerKeeper().getAntiBuild().get(rs.getTardis_id());
        if (v.isInAABB(tab.getMin(), tab.getMax())) {
            event.setCancelled(true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "ANTIBUILD_TIMELORD", tab.getTimelord());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player p) {
            if (!plugin.getUtils().inTARDISWorld(p)) {
                return;
            }
            ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getUniqueId());
            if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
                return;
            }
            Vector v = event.getEntity().getLocation().toVector();
            TARDISAntiBuild tab = plugin.getTrackerKeeper().getAntiBuild().get(rs.getTardis_id());
            if (v.isInAABB(tab.getMin(), tab.getMax())) {
                event.setCancelled(true);
                plugin.getMessenger().send(p, TardisModule.TARDIS, "ANTIBUILD_TIMELORD", tab.getTimelord());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionBreakHanging(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player p) {
            if (!plugin.getUtils().inTARDISWorld(p)) {
                return;
            }
            ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getUniqueId());
            if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
                return;
            }
            Vector v = event.getEntity().getLocation().toVector();
            TARDISAntiBuild tab = plugin.getTrackerKeeper().getAntiBuild().get(rs.getTardis_id());
            if (v.isInAABB(tab.getMin(), tab.getMax())) {
                event.setCancelled(true);
                plugin.getMessenger().send(p, TardisModule.TARDIS, "ANTIBUILD_TIMELORD", tab.getTimelord());
            }
        }
    }
}
