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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author eccentric_nz
 */
public class TARDISTagListener implements Listener {

    private final TARDIS plugin;
    private final Calendar eggcal = Calendar.getInstance();

    public TARDISTagListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Informs the player that a game of tag in on. The date of the game can be set in tag.yml. It will also say happy
     * birthday to Doctor Who on the 23/11 of each year.
     * <p>
     * If no player is currently 'it' a random player is chosen (usually the first player to login).
     *
     * @param event a player joining a server
     */
    @EventHandler
    public void onPlayerTagJoin(PlayerJoinEvent event) {
        if (eggcal.after(plugin.getBeforeCal()) && eggcal.before(plugin.getAfterCal())) {
            if (eggcal.get(Calendar.MONTH) == Calendar.NOVEMBER && eggcal.get(Calendar.DATE) == 23) { // zero based month
                int age = ((eggcal.get(Calendar.YEAR)) - 1963);
                String ordinal = getOrdinal(age);
                event.getPlayer().sendMessage(plugin.getPluginName() + "Happy " + age + ordinal + " Birthday Doctor Who!");
            }
            event.getPlayer().sendMessage(plugin.getPluginName() + "Today, and today only, you can play 'Tag the OOD'!");
            event.getPlayer().sendMessage(plugin.getPluginName() + "To see tag stats (and who is currently 'it'), use the command " + ChatColor.AQUA + "/tardis tagtheood");
            if (plugin.getTagConfig().get("it").equals("")) {
                Player startit = getRandomPlayer();
                plugin.getServer().broadcastMessage(plugin.getPluginName() + startit.getName() + " is now the " + ChatColor.RED + "'OOD'!");
                setConfig(startit.getName());
                setConfig(System.currentTimeMillis());
            }
        }
    }

    /**
     * @param event a player leaving a server
     */
    @EventHandler
    public void onPlayerTagLeave(PlayerQuitEvent event) {
        if (eggcal.after(plugin.getBeforeCal()) && eggcal.before(plugin.getAfterCal())) {
            Player p = event.getPlayer();
            if (p.getName().equals(plugin.getTagConfig().getString("it"))) {
                // find a new player to make it
                Player newit = getRandomPlayer();
                if (TARDISPermission.hasPermission(p, "tardis.tag")) {
                    plugin.getServer().broadcastMessage(plugin.getPluginName() + newit.getName() + " is now the " + ChatColor.RED + "'OOD'!");
                }
                setConfig(newit.getName());
                long now = System.currentTimeMillis();
                long timewasit = now - plugin.getTagConfig().getLong("time");
                if (TARDISPermission.hasPermission(p, "tardis.tag")) {
                    plugin.getServer().broadcastMessage(plugin.getPluginName() + p + " was 'OOD' for " + getHoursMinutesSeconds(timewasit) + " seconds.");
                }
                setConfig(now);
                updateTagStats(p.getName(), timewasit);
            }
        }
    }

    /**
     * @param event a player right-clicking another player
     */
    @EventHandler(ignoreCancelled = true)
    public void onTagPlayerInteractTagPlayer(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player && eggcal.after(plugin.getBeforeCal()) && eggcal.before(plugin.getAfterCal())) {
            Player clicked = (Player) event.getRightClicked();
            String p = clicked.getName();
            if (clicked.getName().equals(plugin.getTagConfig().getString("it"))) {
                Player newit = event.getPlayer();
                if (TARDISPermission.hasPermission(newit, "tardis.tag")) {
                    plugin.getServer().broadcastMessage(plugin.getPluginName() + newit.getName() + " is now the " + ChatColor.RED + "'OOD'!");
                }
                setConfig(newit.getName());
                long now = System.currentTimeMillis();
                long timewasit = now - plugin.getTagConfig().getLong("time");
                if (TARDISPermission.hasPermission(newit, "tardis.tag")) {
                    plugin.getServer().broadcastMessage(plugin.getPluginName() + p + " was 'OOD' for " + getHoursMinutesSeconds(timewasit) + " seconds.");
                }
                setConfig(now);
                updateTagStats(p, timewasit);
            }
        }
    }

    /**
     * @return a random player
     */
    private Player getRandomPlayer() {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        int num = players.size();
        return players.get(TARDISConstants.RANDOM.nextInt(num));
    }

    /**
     * @param age the number of years old the birthday person is
     * @return an ordinal string (1st, 2nd, 3rd, 4th)
     */
    private String getOrdinal(int age) {
        switch (age % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * @param s the string value to set
     */
    private void setConfig(String s) {
        plugin.getTagConfig().set("it", s);
        try {
            plugin.getTagConfig().save(new File(plugin.getDataFolder(), "tag.yml"));
        } catch (IOException io) {
            plugin.debug("Could not save tag.yml, " + io);
        }
    }

    /**
     * @param l a long value to set
     */
    private void setConfig(long l) {
        plugin.getTagConfig().set("time", l);
        try {
            plugin.getTagConfig().save(new File(plugin.getDataFolder(), "tag.yml"));
        } catch (IOException io) {
            plugin.debug("Could not save tag.yml, " + io);
        }
    }

    /**
     * @param millis the number of milliseconds to convert to a string
     * @return a string in the form of '00h:00m:00s'
     */
    private String getHoursMinutesSeconds(long millis) {
        return String.format("%02dh:%02dm:%02ds", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    /**
     * @param p a player's name
     * @param t the player's score
     */
    private void updateTagStats(String p, long t) {
        HashMap<String, Object> set = new HashMap<>();
        set.put("player", p);
        set.put("time", t);
        plugin.getQueryFactory().doSyncInsert("tag", set);
    }
}
