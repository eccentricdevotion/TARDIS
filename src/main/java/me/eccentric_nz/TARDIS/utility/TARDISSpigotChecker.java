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
package me.eccentric_nz.TARDIS.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TARDISSpigotChecker implements Runnable {

    private final TARDIS plugin;
    private final JsonParser jp;

    public TARDISSpigotChecker(TARDIS plugin) {
        this.plugin = plugin;
        jp = new JsonParser();
    }

    @Override
    public void run() {
        String spigotVersion = plugin.getServer().getVersion();
        if (spigotVersion.contains("Spigot")) {
            JsonObject spigotBuild = fetchLatestSpigotBuild();
            if (spigotBuild == null || !spigotBuild.has("refs")) {
                // couldn't get Spigot info
                return;
            }
            int name = spigotBuild.getAsJsonPrimitive("name").getAsInt();
            // 2855 is the latest 1.16.2 build (as of 11-08-2020)
            if (name > 2855) {
                JsonObject refs = spigotBuild.get("refs").getAsJsonObject();
                if (refs.has("Spigot")) {
                    String spigot = refs.get("Spigot").getAsString().substring(0, 7);
                    String[] split = spigotVersion.split("-"); // something like 'git-Spigot-2f5d615-d07a78b'
                    if (spigot.equals(split[2])) {
                        // if new build number is same
                        return;
                    }
                    plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + "There is a new Spigot build! " + ChatColor.AQUA + "You should update so TARDIS doesn't bug out :)");
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
            URL url = new URL("https://hub.spigotmc.org/versions/latest.json");
            URLConnection request = url.openConnection();
            request.connect();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            return root.getAsJsonObject();
        } catch (Exception ex) {
            plugin.debug("Failed to check for the latest build info from Spigot.");
        }
        return null;
    }
}
