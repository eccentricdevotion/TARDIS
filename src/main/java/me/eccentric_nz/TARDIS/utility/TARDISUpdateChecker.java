/*
 * Copyright (C) 2022 eccentric_nz
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class TARDISUpdateChecker implements Runnable {

    private final TARDIS plugin;
    private final CommandSender sender;

    public TARDISUpdateChecker(TARDIS plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
    }

    @Override
    public void run() {
        if (!plugin.getGeneralKeeper().getPluginYAML().contains("build-number")) {
            // should never happen
            return;
        }
        String build = plugin.getGeneralKeeper().getPluginYAML().getString("build-number");
        if (build.contains(":")) {
            // local build, not a Jenkins build
            if (sender == null) {
                plugin.getLogger().log(Level.INFO, "Server is running a custom or dev version!");
            } else {
                sender.sendMessage(plugin.getPluginName() + "You are running a custom or dev version!");
            }
            return;
        }
        int buildNumber = Integer.parseInt(build);
        JsonObject lastBuild = fetchLatestJenkinsBuild();
        if (lastBuild == null || !lastBuild.has("id")) {
            // couldn't get Jenkins info
            if (sender == null) {
                plugin.getLogger().log(Level.WARNING, "Couldn't retrieve Jenkins info!");
            } else {
                sender.sendMessage(plugin.getPluginName() + "Couldn't retrieve Jenkins info!");
            }
            return;
        }
        int newBuildNumber = lastBuild.get("id").getAsInt();
        if (newBuildNumber < buildNumber) {
            // if new build number is older
            if (sender == null) {
                plugin.getLogger().log(Level.INFO, "Server is running a newer TARDIS version!");
            } else {
                sender.sendMessage(plugin.getPluginName() + "You are running a newer TARDIS version!");
            }
            return;
        }
        if (buildNumber == newBuildNumber) {
            // if new build number is same
            if (sender == null) {
                plugin.getLogger().log(Level.INFO, "Server is running the latest version!");
            } else {
                sender.sendMessage(plugin.getPluginName() + "You are running the latest version!");
            }
            return;
        }
        plugin.setUpdateFound(true);
        plugin.setBuildNumber(buildNumber);
        plugin.setUpdateNumber(newBuildNumber);
        if (sender == null) {
            plugin.getLogger().log(Level.INFO, String.format(TARDISMessage.JENKINS_UPDATE_READY, buildNumber, newBuildNumber));
            plugin.getLogger().log(Level.INFO, TARDISMessage.UPDATE_COMMAND);
        } else {
            sender.sendMessage(plugin.getPluginName() + "You are " + (newBuildNumber - buildNumber) + " builds behind! Type " + ChatColor.AQUA + "/tadmin update_plugins" + ChatColor.RESET + " to update!");
        }
    }

    /**
     * Fetches from jenkins, using the REST api the last snapshot build information
     */
    private JsonObject fetchLatestJenkinsBuild() {
        try {
            // We're connecting to TARDIS's Jenkins REST api
            URL url = new URL("http://tardisjenkins.duckdns.org:8080/job/TARDIS/lastSuccessfulBuild/api/json");
            // Create a connection
            URLConnection request = url.openConnection();
            request.setRequestProperty("User-Agent", "TARDISPlugin");
            request.connect();
            // Convert to a JSON object
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            return root.getAsJsonObject();
        } catch (Exception ex) {
            plugin.debug("Failed to check for a snapshot update on TARDIS Jenkins.");
        }
        return null;
    }
}
