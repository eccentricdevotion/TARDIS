/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetSounds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

/**
 * The distinctive TARDIS sound effect - a cyclic wheezing, groaning noise - was originally created in the BBC
 * Radiophonic Workshop by Brian Hodgson. He produced the effect by dragging a set of house keys along the strings of an
 * old, gutted piano. The resulting sound was recorded and electronically processed with echo and reverb.
 *
 * @author eccentric_nz
 */
public class TARDISHumSounds {

    /**
     * Callback to get data asynchronously from the database.
     *
     * @param <T> The Object type we want to return
     */
    interface Callback<T> {

        void execute(T response);
    }

    private final Callback<List<UUID>> callback = (List<UUID> data) -> {
        // Do whatever with the data
        data.forEach((u) -> {
            Player player = Bukkit.getServer().getPlayer(u);
            if (player != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Prefs p = new Prefs(player, true, "tardis_hum");
                        // All the MySQL stuff and what not
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(TARDIS.plugin, u.toString());
                        if (rsp.resultSet()) {
                            p.setSfx(rsp.isSfxOn());
                            p.setHum((rsp.getHum().isEmpty()) ? "tardis_hum" : "tardis_hum_" + rsp.getHum());
                        }
                        if (p.getSfx()) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    prefs.execute(p);
                                }
                            }.runTask(TARDIS.plugin);
                        }
                    }
                }.runTaskAsynchronously(TARDIS.plugin);
            }
        });
    };

    private final Callback<Prefs> prefs = (Prefs pref) -> TARDISSounds.playTARDISSound(pref.getPlayer().getLocation(), pref.getHum());

    /**
     * Plays an interior hum sound to players who are inside the TARDIS and don't have SFX set to false.
     */
    public void playTARDISHum() {
        if (TARDIS.plugin.getConfig().getBoolean("allow.sfx")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    // All the MySQL stuff and what not
                    ResultSetSounds rs = new ResultSetSounds(TARDIS.plugin);
                    if (rs.resultSet()) {
                        List<UUID> data = rs.getData();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                callback.execute(data);
                            }
                        }.runTask(TARDIS.plugin);
                    }
                }
            }.runTaskAsynchronously(TARDIS.plugin);
        }
    }
}
