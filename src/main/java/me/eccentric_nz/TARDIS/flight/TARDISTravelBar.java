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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISTravelBar {

    private final TARDIS plugin;
    private int taskID;
    private static final BarFlag[] EMPTY_ARRAY = new BarFlag[0];

    TARDISTravelBar(TARDIS plugin) {
        this.plugin = plugin;
    }

    void showTravelRemaining(Player player, long duration, boolean takeoff) {

        String title = (takeoff) ? plugin.getLanguage().getString("TRAVEL_BAR_TAKEOFF") : plugin.getLanguage().getString("TRAVEL_BAR_LAND");
        BossBar bb = Bukkit.createBossBar(title, BarColor.WHITE, BarStyle.SOLID, EMPTY_ARRAY);
        bb.setProgress(0);
        bb.addPlayer(player);
        bb.setVisible(true);
        double millis = duration * 50.0d;
        long start = System.currentTimeMillis();
        double end = start + millis;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            long now = System.currentTimeMillis();
            if (now < end) {
                double progress = 1 - (end - now) / millis;
                bb.setProgress(progress);
            } else {
                bb.setProgress(1);
                bb.setVisible(false);
                bb.removeAll();
                Bukkit.getScheduler().cancelTask(taskID);
                taskID = 0;
            }
        }, 1L, 1L);
    }
}
