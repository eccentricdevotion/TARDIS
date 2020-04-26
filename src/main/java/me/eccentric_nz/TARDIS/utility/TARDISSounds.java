/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The distinctive TARDIS sound effect - a cyclic wheezing, groaning noise - was originally created in the BBC
 * Radiophonic Workshop by Brian Hodgson. He produced the effect by dragging a set of house keys along the strings of an
 * old, gutted piano. The resulting sound was recorded and electronically processed with echo and reverb.
 *
 * @author eccentric_nz
 */
public class TARDISSounds {

    private static float VOLUME = TARDIS.plugin.getConfig().getInt("preferences.sfx_volume") / 10.0F;

    /**
     * Plays the interior hum sound upon TARDIS entry.
     *
     * @param p the player to play the sound to
     */
    public static void playTARDISHum(Player p) {
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(TARDIS.plugin, p.getUniqueId().toString());
        boolean userSFX;
        String hum;
        if (rsp.resultSet()) {
            userSFX = rsp.isSfxOn();
            hum = (rsp.getHum().isEmpty()) ? "tardis_hum" : "tardis_hum_" + rsp.getHum();
        } else {
            userSFX = true;
            hum = "tardis_hum";
        }
        if (userSFX) {
            playTARDISSound(p.getLocation(), hum);
        }
    }

    /**
     * Plays a TARDIS sound for the player and surrounding players at the current location.
     *
     * @param l      The location
     * @param s      The sound to play
     * @param volume The volume to play the sound at
     */
    public static void playTARDISSound(Location l, String s, float volume) {
        l.getWorld().playSound(l, s, VOLUME * volume, 1.0f);
    }

    /**
     * Plays a TARDIS sound for the player and surrounding players at the current location.
     *
     * @param l The location
     * @param s The sound to play
     */
    public static void playTARDISSound(Location l, String s) {
        l.getWorld().playSound(l, s, VOLUME, 1.0f);
    }

    /**
     * Plays a TARDIS sound for the specified player.
     *
     * @param p The player
     * @param s The sound to play
     */
    public static void playTARDISSound(Player p, String s) {
        TARDIS.plugin.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> p.playSound(p.getLocation(), s, VOLUME, 1.0f), 5L);
    }

    public static void setVolume(float VOLUME) {
        TARDISSounds.VOLUME = VOLUME;
    }
}
