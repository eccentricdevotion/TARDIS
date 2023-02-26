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
package me.eccentric_nz.TARDIS.universaltranslator;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LingvaTranslate {

    private static List<String> domains = Arrays.asList("lingva.ml", "translate.dr460nf1r3.org", "lingva.garudalinux.org");

    /**
     * Fetches a translation from a Lingva instance
     */
    public static String fetch(String from, String to, String message) {
        // get random domain
        String host = domains.get(ThreadLocalRandom.current().nextInt(domains.size()));
        try {
            String encoded = message.replace(" ", "%20");
            // We're connecting to TARDIS's Jenkins REST api
            URI uri = URI.create("https://" + host + "/api/v1/" + from + "/" + to + "/" + encoded);
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
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonElement root = JsonParser.parseString((String) response.body());
            String translation = root.getAsJsonObject().get("translation").getAsString();
            return translation;
        } catch (Exception ex) {
            TARDIS.plugin.debug("Failed to fetch a translation from " + host + ". " + ex.getMessage());
        }
        return "Translation failed :(";
    }
}
