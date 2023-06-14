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
import java.util.logging.Level;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;

public class TARDISSpigotChecker implements Runnable {

    private final TARDIS plugin;

    public TARDISSpigotChecker(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // latest is always the recommended build - for new releases we should check the current server version
        String spigotVersion = plugin.getServer().getVersion();
        if (spigotVersion.contains("Spigot")) {
            JsonObject spigotBuild = fetchLatestSpigotBuild();
            if (spigotBuild == null || !spigotBuild.has("refs")) {
                // couldn't get Spigot info
                return;
            }
            int name = spigotBuild.getAsJsonPrimitive("name").getAsInt();
            // 3777 is the latest 1.20 build (as of 9-06-2023)
            String[] split = spigotVersion.split("-"); // something like '3777-Spigot-723951c-f3b2b22 (MC: 1.20)'
            int current = TARDISNumberParsers.parseInt(split[0]);
            if (name > current) {
                JsonObject refs = spigotBuild.get("refs").getAsJsonObject();
                // CraftBukkit version updates more often than Spigot...
                if (refs.has("CraftBukkit")) {
                    String craftBukkit = refs.get("CraftBukkit").getAsString().substring(0, 7);
                    String[] cb = split[3].split(" ");
                    if (craftBukkit.equals(cb[0])) {
                        // if new build number is same
                        return;
                    }
                    plugin.getLogger().log(Level.INFO, ChatColor.RED + "There is a new Spigot build! " + ChatColor.AQUA + "You should update so TARDIS doesn't bug out :)");
                }
            }
        }
    }

    /**
     * Fetches the latest build information from hub.spigotmc.org
     */
    private JsonObject fetchLatestSpigotBuild() {
        //
        try {
            URI uri = URI.create("https://hub.spigotmc.org/versions/latest.json");
            // Create a client, request and response
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonElement root = JsonParser.parseString((String) response.body());
            return root.getAsJsonObject();
        } catch (Exception ex) {
            plugin.debug("Failed to check for the latest build info from Spigot.");
        }
        return null;
    }
}
