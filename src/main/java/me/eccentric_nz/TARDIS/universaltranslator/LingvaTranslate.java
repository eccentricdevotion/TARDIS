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
package me.eccentric_nz.TARDIS.universaltranslator;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LingvaTranslate {

    private static List<String> domains = Arrays.asList("lingva.ml", "translate.projectsegfau.lt", "translate.dr460nf1r3.org", "lingva.garudalinux.org");

    /**
     * Fetches a translation from a Lingva instance
     */
    public static String fetch(String from, String to, String message) {
        // get random domain
        String host = domains.get(ThreadLocalRandom.current().nextInt(domains.size()));
        try {
            // We're connecting to TARDIS's Jenkins REST api
            URL url = new URL("https://" + host + "/api/v1/" + from + "/" + to + "/" + message);
            // Create a connection
            URLConnection request = url.openConnection();
            request.setRequestProperty("User-Agent", "TARDISPlugin");
            request.connect();
            // Convert to a JSON object
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            String translation = root.getAsJsonObject().get("translation").getAsString();
            return translation;
        } catch (Exception ex) {
            TARDIS.plugin.debug("Failed to fetch a translation from " + host + ". " + ex.getMessage());
        }
        return "Translation failed :(";
    }
}
