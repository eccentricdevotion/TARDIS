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
package me.eccentric_nz.TARDIS.utility;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.Coord;
import com.palmergames.bukkit.towny.object.TownyWorld;
import com.palmergames.bukkit.towny.object.WorldCoord;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISTownyChecker {

    private TARDIS plugin;
    private Towny towny;

    public TARDISTownyChecker(TARDIS plugin) {
        this.plugin = plugin;
        if (plugin.townyOnServer) {
            towny = (Towny) plugin.getServer().getPluginManager().getPlugin("Towny");
        }
    }

    /**
     * Checks whether a location is in 'wilderness'... ie NOT in a Towny town.
     *
     * @param l the location instance to check.
     */
    public boolean isWilderness(Player p, Location l) {
        boolean bool = true;
        if (towny != null) {
            try {
                String name = l.getWorld().getName();
                TownyWorld w = new TownyWorld(name);
                if (w.hasTowns()) {
                    Coord cord = Coord.parseCoord(l.getBlockX(), l.getBlockZ());
                    WorldCoord wcoord = new WorldCoord(name, cord);
                    if (wcoord.getTownBlock().hasResident() && (!wcoord.getTownBlock().getResident().getPermissions().outsiderBuild && !wcoord.getTownBlock().getResident().getPermissions().allyBuild && !wcoord.getTownBlock().getResident().getPermissions().residentBuild)) {
                        bool = false;
                    }
                }
            } catch (Exception e) {
                plugin.debug("Towny checker error " + e.getMessage());
            }
        }
        return bool;
    }
}