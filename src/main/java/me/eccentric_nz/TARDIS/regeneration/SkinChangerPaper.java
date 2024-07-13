package me.eccentric_nz.TARDIS.regeneration;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseTracker;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SkinChangerPaper {

    public static void set(Player player, String json) {
        PlayerProfile profile = player.getPlayerProfile();
        // load JSON
        JsonElement root = JsonParser.parseString(json);
        JsonObject skin = root.getAsJsonObject();
        String value = skin.get("value").getAsString();
        String signature = skin.get("signature").getAsString();
        profile.getProperties().removeIf(profileProperty -> profileProperty.getName().equals("textures"));
        profile.getProperties().add(new ProfileProperty("textures", value, signature));
        // set the game profile
        player.setPlayerProfile(profile);
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
    }

    public static void set(Player player, JsonObject properties) {
        PlayerProfile profile = player.getPlayerProfile();
        // get value and signature
        String value = properties.get("value").getAsString();
        String signature = properties.get("signature").getAsString();
        profile.getProperties().removeIf(profileProperty -> profileProperty.getName().equals("textures"));
        profile.getProperties().add(new ProfileProperty("textures", value, signature));
        // set the game profile
        player.setPlayerProfile(profile);
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
    }

    public static void remove(Player player) {
        PlayerProfile profile = player.getPlayerProfile();
        UUID uuid = player.getUniqueId();
        SkinFetcher getter = new SkinFetcher(TARDIS.plugin, uuid);
        getter.fetchAsync((hasResult, fetched) -> {
            if (hasResult) {
                JsonObject properties = fetched.getSkin();
                if (properties != null) {
                    String value = properties.get("value").getAsString();
                    String signature = properties.get("signature").getAsString();
                    profile.getProperties().removeIf(profileProperty -> profileProperty.getName().equals("textures"));
                    profile.getProperties().add(new ProfileProperty("textures", value, signature));
                    // reset the game profile
                    player.setPlayerProfile(profile);
                    TARDISDisguiseTracker.DISGUISED_AS_PLAYER.remove(uuid);
                }
            }
        });
    }
}
