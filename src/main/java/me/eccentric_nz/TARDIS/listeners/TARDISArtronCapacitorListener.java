/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetEntities;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISArtronCapacitorListener implements Listener {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISArtronCapacitorListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCapacitorInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
                plugin.debug("Right-clicked while sneaking");
                // only proceed if they are clicking a glass block!
                if (blockType == Material.OBSIDIAN) {
                    plugin.debug("The material was obsidian");
                    Material item = player.getItemInHand().getType();
                    if (item.equals(Material.NETHER_STAR)) {
                        // give TARDIS full charge
                    }
                    if (item.equals(Material.valueOf(plugin.getConfig().getString("key")))) {
                        plugin.debug("Item in hand was TARDIS key");
                        // kickstart the TARDIS Artron Energy Capacitor
                        Location l = block.getRelative(BlockFace.DOWN, 2).getLocation();
                        Entity e = l.getWorld().spawnEntity(l, EntityType.CREEPER);
                        String uuid = e.getUniqueId().toString();
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("uuid", uuid);
                        QueryFactory qf = new QueryFactory(plugin);
                        qf.doInsert("entities", set);
                        Creeper c = (Creeper) e;
                        c.setPowered(true);
                    } else {
                        // transfer player artron energy into
                    }
                }
            }
        }
    }

    @EventHandler
    public void onReadySteadyExplode(ExplosionPrimeEvent e) {
        Entity ent = e.getEntity();
        List<String> ids = getUUIDs();
        if (ids.contains(ent.getUniqueId().toString())) {
            e.setCancelled(true);
        }
    }

    private List<String> getUUIDs() {
        List<String> list = new ArrayList<String>();
        ResultSetEntities rs = new ResultSetEntities(plugin, true);
        if (rs.resultSet()) {
            ArrayList<HashMap<String, String>> data = rs.getData();
            for (HashMap<String, String> map : data) {
                list.add(map.get("uuid"));
            }
        }
        return list;
    }
}