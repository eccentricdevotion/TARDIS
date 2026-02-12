/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility.update;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.io.StringReader;
import java.lang.module.ModuleDescriptor;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;

public class TARDISUpdateChecker implements Runnable {

    private final TARDIS plugin;
    private final CommandSender sender;

    public TARDISUpdateChecker(TARDIS plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
    }

    @Override
    public void run() {
        if (!plugin.getGeneralKeeper().getPluginYAML().contains("version")) {
            // should never happen
            return;
        }
        String build = plugin.getGeneralKeeper().getPluginYAML().getString("version");
        if (build.contains("local")) {
            // local build, not a GitHub build
            if (sender == null) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Server is running a custom or dev version!");
            } else {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "You are running a custom or dev version!");
            }
            return;
        }
        String commit = build.split("-b")[1];
        JsonObject lastBuild = fetchLatestCommit();
        // TARDIS-6.3.10-b9a2e689d1.jar - 9 chars
        if (lastBuild == null || !lastBuild.has("sha")) {
            // couldn't get GitHub info
            if (sender == null) {
                plugin.getMessenger().sendWithColour(plugin.getConsole(), TardisModule.TARDIS, "Couldn't retrieve GitHub commit info!", "#FF5555");
            } else {
                plugin.getMessenger().sendWithColour(sender, TardisModule.TARDIS, "Couldn't retrieve GitHub commit info!", "#FF5555");
            }
            return;
        }
        String latestCommit = lastBuild.get("sha").getAsString().substring(0, 9);
        if (commit.equals(latestCommit)) {
            // if latest commit short sha is same
            if (sender == null) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Server is running the latest version!");
            } else {
                plugin.getMessenger().message(sender, TardisModule.TARDIS, "You are running the latest version!");
            }
            return;
        }
        // check if server version matches tardis version
        String currentServer = getLastestServerVersion();
        if (!currentServer.isEmpty()) {
            ModuleDescriptor.Version updateVersion = ModuleDescriptor.Version.parse(currentServer);
            ModuleDescriptor.Version serverVersion = ModuleDescriptor.Version.parse(plugin.getServerStr());
            if (updateVersion.compareTo(serverVersion) > 0) {
                String message = "A new version is available, but requires a server update to " + currentServer;
                if (sender == null) {
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, message);
                } else {
                    plugin.getMessenger().message(sender, TardisModule.TARDIS, message);
                }
                return;
            }
        }
        UpdateTracker.setUpdateFound(true);
        UpdateTracker.setCurrentCommit(commit);
        UpdateTracker.setLatestCommit(latestCommit);
        if (sender == null) {
            plugin.getMessenger().sendGitHubUpdateReady(plugin.getConsole(), commit, latestCommit);
            if (plugin.getConfig().getBoolean("preferences.update.auto_download")) {
                new UpdateTARDISPlugins(plugin).fetchFromGitHub(plugin.getConsole());
            } else {
                plugin.getMessenger().sendUpdateCommand(plugin.getConsole());
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "", latestCommit);
        }
    }

    /**
     * Fetches from GitHub, using the REST api the last snapshot build
     * information
     */
    private JsonObject fetchLatestCommit() {
        try {
            // We're connecting to GitHub's REST api
            URI uri = URI.create("https://api.github.com/repos/eccentricdevotion/TARDIS/commits");
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
            JsonElement root = JsonParser.parseString(response.body());
            return root.getAsJsonArray().get(0).getAsJsonObject();
        } catch (Exception ex) {
            plugin.debug("Failed to check for a commit update on TARDIS GitHub.");
        }
        return null;
    }

    private String getLastestServerVersion() {
        try {
            // We're connecting to GitHub
            URI uri = URI.create("https://raw.githubusercontent.com/eccentricdevotion/TARDIS/master/gradle.properties");
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
            StringReader reader = new StringReader(response.body());
            Properties props = new Properties();
            props.load(reader);
            reader.close();
            return props.getProperty("paperVersion");
        } catch (IOException  | InterruptedException e) {
            return "1.21.11";
        }
    }
}
