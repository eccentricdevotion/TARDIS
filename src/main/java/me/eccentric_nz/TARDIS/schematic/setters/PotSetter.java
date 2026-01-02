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
package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.DecoratedPot;

/**
 * @author eccentric_nz
 */
public class PotSetter {

    public static void decorate(TARDIS plugin, JsonObject pot, Block block) {
        try {
            DecoratedPot decorated = (DecoratedPot) block.getState();
            decorated.setSherd(DecoratedPot.Side.BACK, Material.valueOf(pot.get("BACK").getAsString()));
            decorated.setSherd(DecoratedPot.Side.FRONT, Material.valueOf(pot.get("FRONT").getAsString()));
            decorated.setSherd(DecoratedPot.Side.LEFT, Material.valueOf(pot.get("LEFT").getAsString()));
            decorated.setSherd(DecoratedPot.Side.RIGHT, Material.valueOf(pot.get("RIGHT").getAsString()));
            if (pot.has("cracked") && block.getBlockData() instanceof org.bukkit.block.data.type.DecoratedPot dp) {
                dp.setCracked(pot.get("cracked").getAsBoolean());
                block.setBlockData(dp);
            }
            decorated.update();
        } catch (IllegalArgumentException ex) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, "Could not decorate pot sides!");
        }
    }
}
