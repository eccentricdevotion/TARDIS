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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.destroyers;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.builders.TardisInteriorPositioning;
import me.eccentric_nz.tardis.builders.TardisTipsData;
import me.eccentric_nz.tardis.enumeration.Schematic;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Collections;
import java.util.HashMap;

/**
 * Destroys the inner TARDIS.
 * <p>
 * If a TARDIS landed in the same space and time as another TARDIS, a time ram could occur, destroying both TARDISes,
 * their occupants and even cause a black hole that would tear a hole in the universe
 *
 * @author eccentric_nz
 */
public class TardisDestroyerInner {

    private final TardisPlugin plugin;

    public TardisDestroyerInner(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Destroys the inside of the TARDIS.
     *
     * @param schematic the name of the schematic file to use can be DEFAULT, BIGGER or DELUXE.
     * @param id        the unique key of the record for this TARDIS in the database.
     * @param w         the world where the TARDIS is to be built.
     * @param slot      the TIPS slot number
     */
    public void destroyInner(Schematic schematic, int id, World w, int slot) {
        // destroy TARDIS
        if (!plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world")) {
            plugin.debug(TardisPlugin.plugin.getLanguage().getString("CONFIG_CREATION_WORLD"));
            return;
        }
        Location wgl;
        TardisInteriorPositioning tips = new TardisInteriorPositioning(plugin);
        TardisTipsData coords;
        if (schematic.getPermission().equals("junk")) {
            coords = tips.getTipsJunkData();
        } else {
            coords = tips.getTipsData(slot);
        }
        tips.reclaimChunks(w, id);
        wgl = new Location(w, coords.getMinX(), 64, coords.getMinZ());
        // remove blocks saved to blocks table (iron/gold/diamond/emerald)
        HashMap<String, Object> where = new HashMap<>();
        where.put("police_box", 0);
        plugin.getQueryFactory().doDelete("blocks", where);
        // remove from protectBlockMap - remove(id) would only remove the first one
        plugin.getGeneralKeeper().getProtectBlockMap().values().removeAll(Collections.singleton(id));
        if (plugin.isWorldGuardOnServer()) {
            plugin.getWorldGuardUtils().removeRegion(wgl);
        }
    }
}
