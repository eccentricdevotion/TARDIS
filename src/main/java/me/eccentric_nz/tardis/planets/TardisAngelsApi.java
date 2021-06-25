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
package me.eccentric_nz.tardis.planets;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardisweepingangels.TardisWeepingAngelsPlugin;
import me.eccentric_nz.tardisweepingangels.TardisWeepingAngelsApi;
import org.bukkit.entity.Skeleton;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/**
 * @author eccentric_nz
 */
public class TardisAngelsApi {

    /**
     * Get TARDISWeepingAngelsAPI.
     *
     * @param plugin - a tardis plugin instance
     * @return the TARDISWeepingAngelsAPI
     */
    public static TardisWeepingAngelsApi getApi(TardisPlugin plugin) {
        Plugin p = plugin.getPluginManager().getPlugin("TARDISWeepingAngels");
        TardisWeepingAngelsPlugin twa = (TardisWeepingAngelsPlugin) p;
        assert twa != null;
        return twa.getWeepingAngelsApi();
    }

    public static boolean isDalek(Skeleton skeleton) {
        return skeleton.getPersistentDataContainer().has(TardisWeepingAngelsPlugin.DALEK, PersistentDataType.INTEGER);
    }
}
