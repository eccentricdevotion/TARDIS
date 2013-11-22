/*
 * Copyright (C) 2013 eccentric_nz
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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTagListener implements Listener {

    private final TARDIS plugin;
    Random rand = new Random();

    public TARDISTagListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param event a player joining a server
     */
    @EventHandler
    public void onPlayerTagJoin(PlayerJoinEvent event) {
        Calendar eggcal = Calendar.getInstance();
        plugin.debug("Before: " + plugin.getBeforeCal().getTime().toString());
        plugin.debug("Now: " + eggcal.getTime().toString());
        plugin.debug("After: " + plugin.getAfterCal().getTime().toString());
        if (eggcal.after(plugin.getBeforeCal()) && eggcal.before(plugin.getAfterCal())) {
            plugin.debug("In the zone!");
            int age = ((Calendar.getInstance().get(Calendar.YEAR)) - 1963);
            String ordinal = getOrdinal(age);

            event.getPlayer().sendMessage(plugin.pluginName + "Happy " + age + ordinal + " Birthday Doctor Who!");
            event.getPlayer().sendMessage(plugin.pluginName + "Today, and today only, you can play 'Tag the OOD'!");
            event.getPlayer().sendMessage(plugin.pluginName + "To see tag stats (and who is currently 'it'), use the command " + ChatColor.AQUA + "/tardis tagtheood");
            if (plugin.getTagConfig().get("it").equals("")) {
                Player startit = getRandomPlayer();
                plugin.getServer().broadcastMessage(plugin.pluginName + startit.getName() + " is now the " + ChatColor.RED + "'OOD'!");
                setConfig(startit.getName());
                setConfig(System.currentTimeMillis());
            }
        }
    }

    /**
     *
     * @param event a player leaving a server
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        System.out.println("Someone left...");
        Calendar eggcal = Calendar.getInstance();
        if (eggcal.after(plugin.getBeforeCal()) && eggcal.before(plugin.getAfterCal())) {
            String p = event.getPlayer().getName();
            if (p.equals(plugin.getTagConfig().getString("it"))) {
                // find a new player to make it
                Player newit = getRandomPlayer();
                plugin.getServer().broadcastMessage(plugin.pluginName + newit.getName() + " is now the " + ChatColor.RED + "'OOD'!");
                setConfig(newit.getName());
                long now = System.currentTimeMillis();
                long timewasit = now - plugin.getTagConfig().getLong("time");
                plugin.getServer().broadcastMessage(plugin.pluginName + p + " was 'OOD' for " + getHoursMinutesSeconds(timewasit) + " seconds.");
                setConfig(now);
                updateTagStats(p, timewasit);
            }
        }
    }

    /**
     *
     * @param event a player right-clicking another player
     */
    @EventHandler
    public void onPlayerInteractPlayer(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            System.out.println("Someone right-clicked a player...");
            Player clicked = (Player) event.getRightClicked();
            String p = clicked.getName();
            if (clicked.getName().equals(plugin.getTagConfig().getString("it"))) {
                Player newit = event.getPlayer();
                plugin.getServer().broadcastMessage(plugin.pluginName + newit.getName() + " is now the " + ChatColor.RED + "'OOD'!");
                setConfig(newit.getName());
                long now = System.currentTimeMillis();
                long timewasit = now - plugin.getTagConfig().getLong("time");
                plugin.getServer().broadcastMessage(plugin.pluginName + p + " was 'OOD' for " + getHoursMinutesSeconds(timewasit) + " seconds.");
                setConfig(now);
                updateTagStats(p, timewasit);
            }
        }
    }

    /**
     *
     * @return a random player
     */
    private Player getRandomPlayer() {
        Player[] players = plugin.getServer().getOnlinePlayers();
        int num = players.length;
        return players[rand.nextInt(num)];
    }

    /**
     *
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
     *
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
     *
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
     *
     * @param millis the number of milliseconds to convert to a string
     * @return a string in the form of '00h:00m:00s'
     */
    private String getHoursMinutesSeconds(long millis) {
        return String.format("%02dh:%02dm:%02ds",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    /**
     *
     * @param p a player's name
     * @param t the player's score
     */
    private void updateTagStats(String p, long t) {
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("player", p);
        set.put("time", t);
        new QueryFactory(plugin).doSyncInsert("tag", set);
    }
}
