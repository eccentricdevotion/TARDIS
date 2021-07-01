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
package me.eccentric_nz.tardis.api.event;

import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.desktop.TardisUpgradeData;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public final class TardisDesktopThemeEvent extends TardisEvent {

    private final TardisUpgradeData upgradeData;

    public TardisDesktopThemeEvent(Player player, Tardis tardis, TardisUpgradeData upgradeData) {
        super(player, tardis);
        this.upgradeData = upgradeData;
    }

    /**
     * Returns a desktop theme data object. You can use the {@link TardisUpgradeData} to determine the previous theme, the theme that the TARDIS is changing to, the wall and floor
     * block types using the object's getter methods.
     *
     * @return the desktop theme data object
     */
    public TardisUpgradeData getUpgradeData() {
        return upgradeData;
    }
}
