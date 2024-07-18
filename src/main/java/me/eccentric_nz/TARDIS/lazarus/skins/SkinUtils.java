package me.eccentric_nz.TARDIS.lazarus.skins;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Base64;

public class SkinUtils {

    public static boolean isAlexSkin(Player player) {
        ServerPlayer sp = ((CraftPlayer)player).getHandle();
        GameProfile profile = sp.getGameProfile();
        String base64 = profile.getProperties().get("textures").iterator().next().value();
        String decodedValue = new String(Base64.getDecoder().decode(base64));
        JsonObject json = JsonParser.parseString(decodedValue).getAsJsonObject();
        JsonObject skinObject = json.getAsJsonObject("textures").getAsJsonObject("SKIN");
        if (!skinObject.has("metadata")) {
            return false;
        }
        if (!skinObject.getAsJsonObject("metadata").has("model")) {
            return false;
        }
        String model = skinObject.getAsJsonObject("metadata").get("model").getAsString();
        return model.equals("slim");
    }

    public static void setSkinModel(Player player, boolean slim) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getGameProfile();
        String base64 = profile.getProperties().get("textures").iterator().next().value();
        String decodedValue = new String(Base64.getDecoder().decode(base64));
        JsonObject json = JsonParser.parseString(decodedValue).getAsJsonObject();
        JsonObject textures = json.getAsJsonObject("textures");
        if (slim) {
            JsonObject metadata = new JsonObject();
            metadata.addProperty("model", "slim");
            JsonObject skin = textures.get("SKIN").getAsJsonObject();
            // add metadata property
            skin.add("metadata", metadata);
            String value = skin.getAsString();
            // TODO base64-encode value?
            // will the signature change if a new property is added to the textures object?
            String signature = textures.get("signature").getAsString();
            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", new Property("textures", value, signature));
        } else {
            // remove metadata object
            // probably don't need to set this when skin is removed as the SkinChanger#remove implementation will reset the textures
        }
    }

    public static void debug(Player player) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getGameProfile();
        String base64 = profile.getProperties().get("textures").iterator().next().value();
        String decodedValue = new String(Base64.getDecoder().decode(base64));
        TARDIS.plugin.debug(decodedValue);
        /*
        {
          "timestamp" : 1721299105129,
          "profileId" : "01aadb9932ea4e5ba1df50b46d3f82e3",
          "profileName" : "thenosefairy",
          "signatureRequired" : true,
          "textures" : {
            "SKIN" : {
              "url" : "http://textures.minecraft.net/texture/45e729c38eb2872e979652f707ec156da0b9615ee3c87832829d7ca0ebdb7f92",
              "metadata" : {
                "model" : "slim"
              }
            }
          }
        }
         */
    }
}
