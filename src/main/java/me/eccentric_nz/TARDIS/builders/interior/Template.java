package me.eccentric_nz.TARDIS.builders.interior;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Template {

    public String[][][] load(TARDIS plugin) {
        try {
            // get JSON
            InputStream stream = plugin.getResource("fade.json");
            if (stream != null) {
                String s = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                JsonObject obj = JsonParser.parseString(s).getAsJsonObject();
                JsonArray array = obj.get("input").getAsJsonArray();
                return new Gson().fromJson(array, String[][][].class);
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
