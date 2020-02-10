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

import com.google.gson.Gson;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class TARDISUpdateChecker implements Runnable {

    private final TARDIS plugin;

    public TARDISUpdateChecker(TARDIS plugin) {
        this.plugin = plugin;
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
            return;
        }
        int buildNumber = Integer.parseInt(build);
        Map<String, Object> lastBuild = fetchLatestJenkinsBuild();
        if (lastBuild == null || !lastBuild.containsKey("id")) {
            // couldn't get Jenkins info
            return;
        }
        int newBuildNumber = Integer.parseInt((String) lastBuild.get("id"));
        if (newBuildNumber <= buildNumber) {
            // if new build number is same or older
            return;
        }
        plugin.setUpdateFound(true);
        plugin.setBuildNumber(buildNumber);
        plugin.setUpdateNumber(newBuildNumber);
        plugin.getConsole().sendMessage(plugin.getPluginName() + String.format(TARDISMessage.JENKINS_UPDATE_READY, buildNumber, newBuildNumber));
    }

    /**
     * Fetches from jenkins, using the REST api the last snapshot build information
     */
    private Map<String, Object> fetchLatestJenkinsBuild() {
        try {
            // We're connecting to TARDIS's Jenkins REST api
            URL url = new URL("http://tardisjenkins.duckdns.org:8080/job/TARDIS/lastSuccessfulBuild/api/json");
            // Creating a connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            Map<String, Object> jsonObject;

            // Get the input stream, what we receive
            try (InputStream input = con.getInputStream()) {
                // Read it to string
                String json = IOUtils.toString(input, "utf-8");
                jsonObject = new Gson().fromJson(json, Map.class);
            }
            return jsonObject;
        } catch (Exception ex) {
            plugin.debug("Failed to check for a snapshot update on TARDIS Jenkins.");
        }
        return null;
    }
}
