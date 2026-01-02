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
package me.eccentric_nz.TARDIS.destroyers;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.interior.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.interior.TARDISTIPSData;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.utility.protection.TARDISProtectionRemover;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Destroys the inner TARDIS.
 * <p>
 * If a TARDIS landed in the same space and time as another TARDIS, a time ram could occur, destroying both TARDISes,
 * their occupants and even cause a black hole that would tear a hole in the universe
 *
 * @author eccentric_nz
 */
public class TARDISDestroyerInner {

    private final TARDIS plugin;

    public TARDISDestroyerInner(TARDIS plugin) {
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
            plugin.debug(TARDIS.plugin.getLanguage().getString("CONFIG_CREATION_WORLD"));
            return;
        }
        TARDISInteriorPostioning tips = new TARDISInteriorPostioning(plugin);
        tips.reclaimChunks(w, id, schematic);
        // remove blocks saved to blocks table and remove the entries from the protection map
        new TARDISProtectionRemover(plugin).cleanInteriorBlocks(id);
        if (plugin.isWorldGuardOnServer()) {
            TARDISTIPSData coords;
            if (schematic.getPermission().equals("junk")) {
                coords = tips.getTIPSJunkData(slot);
            } else {
                coords = tips.getTIPSData(slot);
            }
            plugin.getWorldGuardUtils().removeRegion(new Location(w, coords.getMinX(), 64, coords.getMinZ()));
        }
    }
}
