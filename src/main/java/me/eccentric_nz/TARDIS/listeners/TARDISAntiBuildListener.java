/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetAntiBuild;
import me.eccentric_nz.TARDIS.utility.TARDISAntiBuild;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAntiBuildListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> no_place = new ArrayList<Material>();
    private final List<Material> allow_interact = new ArrayList<Material>();
    private final List<Material> no_flower_pot = new ArrayList<Material>();

    public TARDISAntiBuildListener(TARDIS plugin) {
        this.plugin = plugin;
        this.no_place.add(Material.BOAT);
        this.no_place.add(Material.BUCKET);
        this.no_place.add(Material.EXPLOSIVE_MINECART);
        this.no_place.add(Material.FLINT_AND_STEEL);
        this.no_place.add(Material.HOPPER_MINECART);
        this.no_place.add(Material.ITEM_FRAME);
        this.no_place.add(Material.LAVA_BUCKET);
        this.no_place.add(Material.MINECART);
        this.no_place.add(Material.MONSTER_EGG);
        this.no_place.add(Material.PAINTING);
        this.no_place.add(Material.POWERED_MINECART);
        this.no_place.add(Material.SHEARS);
        this.no_place.add(Material.STORAGE_MINECART);
        this.no_place.add(Material.WATER_BUCKET);
        this.allow_interact.add(Material.DIODE_BLOCK_OFF);
        this.allow_interact.add(Material.DIODE_BLOCK_ON);
        this.allow_interact.add(Material.IRON_DOOR);
        this.allow_interact.add(Material.LEVER);
        this.allow_interact.add(Material.REDSTONE_COMPARATOR_OFF);
        this.allow_interact.add(Material.REDSTONE_COMPARATOR_ON);
        this.allow_interact.add(Material.STONE_BUTTON);
        this.allow_interact.add(Material.STONE_PLATE);
        this.allow_interact.add(Material.WOODEN_DOOR);
        this.allow_interact.add(Material.WOOD_BUTTON);
        this.allow_interact.add(Material.WOOD_PLATE);
        this.no_flower_pot.add(Material.RED_ROSE);
        this.no_flower_pot.add(Material.YELLOW_FLOWER);
        this.no_flower_pot.add(Material.SAPLING);
        this.no_flower_pot.add(Material.RED_MUSHROOM);
        this.no_flower_pot.add(Material.BROWN_MUSHROOM);
        this.no_flower_pot.add(Material.CACTUS);
        this.no_flower_pot.add(Material.LONG_GRASS);
        this.no_flower_pot.add(Material.DEAD_BUSH);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionBuild(BlockPlaceEvent event) {
        String name = event.getPlayer().getName();
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, name);
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getTrackAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        Vector v = event.getBlock().getLocation().toVector();
        TARDISAntiBuild tab = plugin.getTrackerKeeper().getTrackAntiBuild().get(rs.getTardis_id());
        if (v.isInAABB(tab.getMin(), tab.getMax())) {
            event.setCancelled(true);
            TARDISMessage.send(event.getPlayer(), plugin.getPluginName() + tab.getTimelord() + " has turned off companion building!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionBreak(BlockBreakEvent event) {
        String name = event.getPlayer().getName();
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, name);
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getTrackAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        Vector v = event.getBlock().getLocation().toVector();
        TARDISAntiBuild tab = plugin.getTrackerKeeper().getTrackAntiBuild().get(rs.getTardis_id());
        if (v.isInAABB(tab.getMin(), tab.getMax())) {
            event.setCancelled(true);
            TARDISMessage.send(event.getPlayer(), plugin.getPluginName() + tab.getTimelord() + " has turned off companion building!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionPlace(PlayerInteractEvent event) {
        String name = event.getPlayer().getName();
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, name);
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getTrackAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        if (no_place.contains(event.getPlayer().getItemInHand().getType()) && !allow_interact.contains(event.getClickedBlock().getType())) {
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            TARDISMessage.send(event.getPlayer(), plugin.getPluginName() + "Companion building has been turned off!");
        }
        if (event.getClickedBlock().getType().equals(Material.FLOWER_POT) && no_flower_pot.contains(event.getPlayer().getItemInHand().getType())) {
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            TARDISMessage.send(event.getPlayer(), plugin.getPluginName() + "Companion planting has been turned off!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionEntityClick(PlayerInteractEntityEvent event) {
        String name = event.getPlayer().getName();
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, name);
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getTrackAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        Vector v = event.getRightClicked().getLocation().toVector();
        TARDISAntiBuild tab = plugin.getTrackerKeeper().getTrackAntiBuild().get(rs.getTardis_id());
        if (v.isInAABB(tab.getMin(), tab.getMax())) {
            event.setCancelled(true);
            TARDISMessage.send(event.getPlayer(), plugin.getPluginName() + tab.getTimelord() + " has turned off entity interaction for companions!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getName());
            if (!rs.resultSet() || !plugin.getTrackerKeeper().getTrackAntiBuild().containsKey(rs.getTardis_id())) {
                return;
            }
            Vector v = event.getEntity().getLocation().toVector();
            TARDISAntiBuild tab = plugin.getTrackerKeeper().getTrackAntiBuild().get(rs.getTardis_id());
            if (v.isInAABB(tab.getMin(), tab.getMax())) {
                event.setCancelled(true);
                TARDISMessage.send(p, plugin.getPluginName() + tab.getTimelord() + " has turned off entity damage for companions!");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionPaint(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            Player p = (Player) event.getRemover();
            ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getName());
            if (!rs.resultSet() || !plugin.getTrackerKeeper().getTrackAntiBuild().containsKey(rs.getTardis_id())) {
                return;
            }
            Vector v = event.getEntity().getLocation().toVector();
            TARDISAntiBuild tab = plugin.getTrackerKeeper().getTrackAntiBuild().get(rs.getTardis_id());
            if (v.isInAABB(tab.getMin(), tab.getMax())) {
                event.setCancelled(true);
                TARDISMessage.send(p, plugin.getPluginName() + tab.getTimelord() + " has turned off painting breaking for companions!");
            }
        }
    }
}
