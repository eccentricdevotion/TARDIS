/*
 * Copyright (C) 2023 eccentric_nz
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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

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
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Server is running a custom or dev version!");
            } else {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "You are running a custom or dev version!");
            }
            return;
        }
        int buildNumber = Integer.parseInt(build);
        JsonObject lastBuild = fetchLatestJenkinsBuild();
        if (lastBuild == null || !lastBuild.has("id")) {
            // couldn't get Jenkins info
            if (sender == null) {
                plugin.getMessenger().sendWithColour(plugin.getConsole(), TardisModule.TARDIS, "Couldn't retrieve Jenkins info!", "#FF5555");
            } else {
                plugin.getMessenger().sendWithColour(sender, TardisModule.TARDIS, "Couldn't retrieve Jenkins info!", "#FF5555");
            }
            return;
        }
        int newBuildNumber = lastBuild.get("id").getAsInt();
        if (newBuildNumber < buildNumber) {
            // if new build number is older
            if (sender == null) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Server is running a newer TARDIS version!");
            } else {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "You are running a newer TARDIS version!");
            }
            return;
        }
        if (buildNumber == newBuildNumber) {
            // if new build number is same
            if (sender == null) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Server is running the latest version!");
            } else {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "You are running the latest version!");
            }
            return;
        }
        plugin.setUpdateFound(true);
        plugin.setBuildNumber(buildNumber);
        plugin.setUpdateNumber(newBuildNumber);
        if (sender == null) {
            plugin.getMessenger().sendJenkinsUpdateReady(plugin.getConsole(), buildNumber, newBuildNumber);
            plugin.getMessenger().sendUpdateCommand(plugin.getConsole());
        } else {
            plugin.getMessenger().sendBuildsBehind(sender, (newBuildNumber - buildNumber));
        }
    }

    /**
     * Fetches from jenkins, using the REST api the last snapshot build
     * information
     */
    private JsonObject fetchLatestJenkinsBuild() {
        try {
            // We're connecting to TARDIS's Jenkins REST api
            URI uri = URI.create("http://tardisjenkins.duckdns.org:8080/job/TARDIS/lastSuccessfulBuild/api/json");
            // Create a client, request and response
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("User-Agent", "TARDISPlugin")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonElement root = JsonParser.parseString((String) response.body());
            return root.getAsJsonObject();
        } catch (Exception ex) {
            plugin.debug("Failed to check for a snapshot update on TARDIS Jenkins.");
        }
        return null;
    }
}
