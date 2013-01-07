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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.thirdparty.Version;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * Checks BukkitDev for a newer version of the plugin.
 *
 * @author eccentric_nz
 */
public class TARDISUpdateChecker {

    private final TARDIS plugin;
    private URL tardisURL;

    public TARDISUpdateChecker(TARDIS plugin) {
        this.plugin = plugin;
        try {
            this.tardisURL = new URL("http://www.thenosefairy.co.nz/tardis.txt");
        } catch (Exception e) {
            plugin.debug("Could create new URL! " + e.getMessage());
        }
    }

    /**
     * Checks for the latest version.
     */
    public void checkVersion(Player p) {
        if (exists(tardisURL)) {
            FileConfiguration pluginYml = YamlConfiguration.loadConfiguration(plugin.pm.getPlugin("TARDIS").getResource("plugin.yml"));
            String verStr = pluginYml.getString("version");
            if (verStr.contains("SNAPSHOT")) {
                verStr = verStr.substring(0, verStr.length() - 9);
            }
            String latest = URLReader(tardisURL);
            Version thisversion = new Version(verStr);
            Version latestversion = new Version(latest);
            int compare = thisversion.compareTo(latestversion);
            String message;
            switch (compare) {
                case -1:
                    // lower
                    message = plugin.pluginName + "There is a new version (" + ChatColor.LIGHT_PURPLE + latest + ChatColor.RESET + ") of TARDIS available! Get it at " + pluginYml.getString("website");
                    break;
                case 1:
                    // higher
                    message = plugin.pluginName + "The latest version is " + ChatColor.LIGHT_PURPLE + latest + ChatColor.RESET + ", you must be running a custom or dev version of TARDIS";
                    break;
                default:
                    // equal
                    message = plugin.pluginName + "Well done, you are running the latest version of TARDIS!";
                    break;
            }
            if (p != null) {
                p.sendMessage(message);
            } else {
                plugin.console.sendMessage(message);
            }
        }
    }

    /**
     * Checks whether a a url is valid.
     *
     * @param url the URL to check.
     */
    public boolean exists(URL url) {
        try {
            url.openStream();
        } catch (Exception e) {
            plugin.console.sendMessage("Could not open URL! " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Reads the latest version strings from thenosefairy.co.nz website. Returns
     * an array where [0] is the current stable version number and [1] is the
     * latest dev version number.
     *
     * @param url the URL to read from. This will always be
     * http://thenosefairy.co.nz/tardis.php
     */
    public String URLReader(URL url) {
        String version = "";
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            version = in.readLine();
            in.close();
        } catch (IOException ex) {
            plugin.debug("" + ex);
        }
        return version;
    }
}
