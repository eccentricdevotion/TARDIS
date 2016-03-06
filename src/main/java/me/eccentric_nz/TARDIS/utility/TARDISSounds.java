/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * The distinctive TARDIS sound effect - a cyclic wheezing, groaning noise - was
 * originally created in the BBC Radiophonic Workshop by Brian Hodgson. He
 * produced the effect by dragging a set of house keys along the strings of an
 * old, gutted piano. The resulting sound was recorded and electronically
 * processed with echo and reverb.
 *
 * @author eccentric_nz
 */
public class TARDISSounds {

    private static final float VOLUME = TARDIS.plugin.getConfig().getInt("preferences.sfx_volume") / 10.0F;

    /**
     * Plays a random TARDIS sound to players who are inside the TARDIS and
     * don't have SFX set to false.
     */
    public static void randomTARDISSound() {
        if (TARDIS.plugin.getConfig().getBoolean("allow.sfx") == true) {
            ResultSetTravellers rs = new ResultSetTravellers(TARDIS.plugin, null, true);
            if (rs.resultSet()) {
                List<UUID> data = rs.getData();
                for (UUID u : data) {
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("uuid", u.toString());
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(TARDIS.plugin, where);
                    boolean userSFX;
                    if (rsp.resultSet()) {
                        userSFX = rsp.isSfxOn();
                    } else {
                        userSFX = true;
                    }
                    final Player player = Bukkit.getServer().getPlayer(u);
                    if (player != null) {
                        if (userSFX) {
                            playTARDISSound(player.getLocation(), "tardis_hum");
                        }
                    }
                }
            }
        }
    }

    /**
     * Plays a TARDIS sound for the player and surrounding players at the
     * current location.
     *
     * @param l The location
     * @param s The sound to play
     */
    public static void playTARDISSound(Location l, String s) {
        try {
            l.getWorld().playSound(l, s, VOLUME, 1.0f);
        } catch (Exception ex) {
            // spawn an entity at the location - an exp orb will do
            Entity orb = l.getWorld().spawnEntity(l, EntityType.EXPERIENCE_ORB);
            for (Entity e : orb.getNearbyEntities(16.0d, 16.0d, 16.0d)) {
                if (e instanceof Player) {
                    Player pp = (Player) e;
                    pp.playSound(pp.getLocation(), s, VOLUME, 1.0f);
                }
            }
            // remove entity
            orb.remove();
        }
    }
}
