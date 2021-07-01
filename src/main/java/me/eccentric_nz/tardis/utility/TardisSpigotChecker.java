/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.ChatColor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TardisSpigotChecker implements Runnable {

    private final TardisPlugin plugin;

    public TardisSpigotChecker(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // latest is always the recommended build - for new releases we should check the current server version
        String spigotVersion = plugin.getServer().getVersion();
        if (spigotVersion.contains("Spigot")) {
            JsonObject spigotBuild = fetch1dot17SpigotBuild();
            if (spigotBuild == null || !spigotBuild.has("refs")) {
                // couldn't get Spigot info
                return;
            }
            int name = spigotBuild.getAsJsonPrimitive("name").getAsInt();
            // 3121 is the latest 1.17 build (as of 12-06-2021)
            String[] split = spigotVersion.split("-"); // something like '3118-Spigot-66f9d3c-27835bd (MC: 1.17)'
            int current = TardisNumberParsers.parseInt(split[0]);
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
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            return root.getAsJsonObject();
        } catch (Exception exception) {
            plugin.debug("Failed to check for the latest build info from Spigot.");
        }
        return null;
    }

    /**
     * Fetches the latest build information from hub.spigotmc.org
     */
    private JsonObject fetch1dot17SpigotBuild() {
        //
        try {
            URL url = new URL("https://hub.spigotmc.org/versions/1.17.json");
            URLConnection request = url.openConnection();
            request.connect();
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            return root.getAsJsonObject();
        } catch (Exception exception) {
            plugin.debug("Failed to check for the latest build info from Spigot.");
        }
        return null;
    }
}
