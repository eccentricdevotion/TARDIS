package me.eccentric_nz.TARDIS.regeneration;

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
            URL url = new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", id));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent())); // convert the input stream to a json element
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
