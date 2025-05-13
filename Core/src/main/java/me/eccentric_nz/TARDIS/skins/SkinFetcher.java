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
package me.eccentric_nz.TARDIS.skins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

public class SkinFetcher {

    private final TARDIS plugin;
    private final UUID uuid;
    private JsonObject skin;

    public SkinFetcher(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void fetchAsync(final SkinCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            boolean hasResult = fetch();
            // go back to the tick loop
            Bukkit.getScheduler().runTask(plugin, () -> {
                // call the callback with the result
                callback.onDone(hasResult, this);
            });
        });
    }

    public boolean fetch() {
        String id = uuid.toString().replace("-", "");
        try {
            URL url = URI.create(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", id)).toURL();
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                // convert the input stream to a json element
                JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
                JsonObject rootObj = root.getAsJsonObject();
                JsonArray jsonArray = rootObj.getAsJsonArray("properties");
                skin = jsonArray.get(0).getAsJsonObject();
                return true;
            } else {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER_WARNING, "Connection could not be opened (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
            }
        } catch (IOException ignored) {
        }
        return false;
    }

    public JsonObject getSkin() {
        return skin;
    }

    public interface SkinCallback {

        void onDone(boolean hasResult, SkinFetcher fetcher);
    }
}
