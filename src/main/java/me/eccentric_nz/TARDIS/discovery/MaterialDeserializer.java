package me.eccentric_nz.TARDIS.discovery;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.bukkit.Material;

import java.lang.reflect.Type;

public class MaterialDeserializer implements JsonDeserializer<Material> {
    @Override
    public Material deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Material.valueOf(json.getAsString());
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            return Material.AIR;
        }
    }
}

