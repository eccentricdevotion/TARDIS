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
package me.eccentric_nz.TARDIS.utility;

import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class HumPrefs {

    private final Player player;
    private Boolean sfx;
    private String hum;

    HumPrefs(Player player, Boolean sfx, String hum) {
        this.player = player;
        this.sfx = sfx;
        this.hum = hum;
    }

    public Player getPlayer() {
        return player;
    }

    public Boolean getSfx() {
        return sfx;
    }

    public void setSfx(Boolean sfx) {
        this.sfx = sfx;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }
}
