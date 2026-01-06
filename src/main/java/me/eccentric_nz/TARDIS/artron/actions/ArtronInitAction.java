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
package me.eccentric_nz.TARDIS.artron.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.artron.ArtronAbandoned;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ArtronInitAction {

    private final TARDIS plugin;

    public ArtronInitAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void powerUp(Location location, Tardis tardis, Player player, int id) {
        // kickstart the TARDIS Artron Energy Capacitor
        TARDISSounds.playTARDISSound(location, "power_up");
        if (tardis.isAbandoned()) {
            // transfer ownership to the player who clicked
            new ArtronAbandoned(plugin).claim(player, id, location, tardis);
        }
        // get locations from database
        String creeper = tardis.getCreeper();
        if (!creeper.isEmpty() && !creeper.equals(":")) {
            World w = location.getWorld();
            Location cl = TARDISStaticLocationGetters.getLocationFromDB(creeper);
            plugin.setTardisSpawn(true);
            Entity e = w.spawnEntity(cl.add(0.0d, 1.0d, 0.0d), EntityType.CREEPER);
            Creeper c = (Creeper) e;
            c.setPowered(true);
            c.setRemoveWhenFarAway(false);
            if (tardis.getSchematic().hasBeacon()) {
                String beacon = tardis.getBeacon();
                Block bl = TARDISStaticLocationGetters.getLocationFromDB(beacon).getBlock();
                bl.setBlockData(TARDISConstants.GLASS);
            }
        }
        // set the capacitor to 50% charge
        HashMap<String, Object> set = new HashMap<>();
        int half = Math.round(plugin.getArtronConfig().getInt("full_charge") / 2.0F);
        set.put("artron_level", half);
        set.put("tardis_init", 1);
        set.put("powered_on", 1);
        HashMap<String, Object> whereid = new HashMap<>();
        whereid.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", set, whereid);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_INIT");
    }
}
