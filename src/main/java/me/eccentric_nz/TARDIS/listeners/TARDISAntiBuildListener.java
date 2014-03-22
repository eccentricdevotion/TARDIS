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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAntiBuildListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> no_place = new ArrayList<Material>();

    public TARDISAntiBuildListener(TARDIS plugin) {
        this.plugin = plugin;
        this.no_place.add(Material.LAVA_BUCKET);
        this.no_place.add(Material.WATER_BUCKET);
        this.no_place.add(Material.PAINTING);
        this.no_place.add(Material.ITEM_FRAME);
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
            TARDISMessage.send(event.getPlayer(), tab.getTimelord() + " has turned off companion building!");
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
            TARDISMessage.send(event.getPlayer(), tab.getTimelord() + " has turned off companion building!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionPlace(PlayerInteractEvent event) {
        String name = event.getPlayer().getName();
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, name);
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getTrackAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        if (no_place.contains(event.getPlayer().getItemInHand().getType())) {
            event.setCancelled(true);
            TARDISMessage.send(event.getPlayer(), "Companion building has been turned off!");
        }
    }
}
