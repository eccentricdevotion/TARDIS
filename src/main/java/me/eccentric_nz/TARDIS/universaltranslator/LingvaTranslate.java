/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.universaltranslator;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class LingvaTranslate {

    private static final List<String> domains = List.of("lingva.lunar.icu", "translate.projectsegfau.lt", "translate.dr460nf1r3.org", "translate.plausibility.cloud", "lingva.ml");
    private final TARDIS plugin;
    private final String from, to, message;
    private String translated;

    public LingvaTranslate(TARDIS plugin, String from, String to, String message) {
        this.plugin = plugin;
        this.from = from;
        this.to = to;
        this.message = message;
    }

    /**
     * Pings an HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in
     * the 200-399 range.
     *
     * @param url The HTTP URL to be pinged.
     * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
     * given timeout, otherwise <code>false</code>.
     */
    private static boolean pingURL(String url) {
//        url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }

    public void fetchAsync(final TranslateCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            boolean hasResult = fetch();
            // go back to the tick loop
            Bukkit.getScheduler().runTask(plugin, () -> {
                // call the callback with the result
                callback.onDone(hasResult, this);
            });
        });
    }

    /**
     * Fetches a translation from a Lingva instance
     */
    public boolean fetch() {
        try {
            String encoded = message.replace(" ", "%20");
            for (String host : domains) {
                // ping the host
                if (!pingURL("http://" + host)) {
                    continue;
                }
                // We're connecting to a random Lingva host's REST api
                URI uri = URI.create("https://" + host + "/api/v1/" + from + "/" + to + "/" + encoded);
                // Create a client, request and response
                HttpResponse<String> response;
                try (HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(10)).build()) {
                    HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).header("User-Agent", "TARDISPlugin").build();
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                }
                JsonElement root = JsonParser.parseString(response.body());
                translated = root.getAsJsonObject().get("translation").getAsString();
                return true;
            }
        } catch (Exception ex) {
            plugin.debug("Failed to fetch a translation from Lingva hosts" + ex.getMessage());
        }
        translated = "Translation failed :(";
        return false;
    }

    public String getTranslated() {
        return translated;
    }

    public interface TranslateCallback {

        void onDone(boolean hasResult, LingvaTranslate translated);
    }
}
