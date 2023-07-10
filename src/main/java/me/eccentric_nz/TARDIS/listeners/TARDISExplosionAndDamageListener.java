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
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCreeper;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Distronic explosives are powerful but unstable weapons, used on many worlds as components of explosive warheads
 * attached to missiles.
 *
 * @author eccentric_nz
 */
public class TARDISExplosionAndDamageListener implements Listener {

    private final TARDIS plugin;

    public TARDISExplosionAndDamageListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for explosions around the TARDIS Police Box. If the explosion affects any of the Police Box blocks, then
     * those blocks are removed from the effect of the explosion, there by protecting the Police box from damage.
     *
     * @param event an entity exploding
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
            return;
        }
        Location explode = event.getLocation();
        // check if the explosion is in a TARDIS world
        if ((explode.getWorld().getName().contains("TARDIS") || explode.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) && event.getEntity() instanceof Creeper) {
            event.setCancelled(true);
            // check it is not the Artron creeper
            String loc_chk = explode.getWorld().getName() + ":" + (explode.getBlockX() + 0.5f) + ":" + (explode.getBlockY() - 1) + ":" + (explode.getBlockZ() + 0.5f);
            if (!new ResultSetCreeper(plugin, loc_chk).resultSet()) {
                // create a new explosion that doesn't destroy blocks or set fire
                explode.getWorld().createExplosion(explode.getX(), explode.getY(), explode.getZ(), 4.0f, false, false);
            }
        } else {
            Environment env = explode.getWorld().getEnvironment();
            if ((env.equals(Environment.THE_END) && !plugin.getConfig().getBoolean("travel.the_end")) || (env.equals(Environment.NETHER) && !plugin.getConfig().getBoolean("travel.nether"))) {
                return;
            }
            for (String str : plugin.getGeneralKeeper().getProtectBlockMap().keySet()) {
                Location loc = TARDISStaticLocationGetters.getLocationFromBukkitString(str);
                if (loc != null) {
                    Block block = loc.getBlock();
                    // if the block is a TARDIS block then remove it
                    event.blockList().remove(block);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player && plugin.getTrackerKeeper().getFlyingReturnLocation().containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        } else if (event.getEntity() instanceof ItemFrame frame && event.getDamager() instanceof Player) {
            // check if it is a TARDIS Chameleon item frame
            String l = frame.getLocation().toString();
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", l);
            where.put("type", 27);
            ResultSetControls rs = new ResultSetControls(plugin, where, false);
            if (rs.resultSet()) {
                event.setCancelled(true);
            }
        }
        if (event.getCause() != DamageCause.ENTITY_EXPLOSION) {
            return;
        }
        String l = event.getDamager().getLocation().getWorld().getName();
        if (l.contains("TARDIS") || l.equals(plugin.getConfig().getString("creation.default_world_name"))) {
            event.setCancelled(true);
        }
    }
}
