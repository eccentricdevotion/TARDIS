package me.eccentric_nz.TARDIS.regeneration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseTracker;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SkinChangerSpigot {

    public static void set(Player player, String json) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getGameProfile();
        // convert the string to a json element
        JsonElement root = JsonParser.parseString(json);
        JsonObject skin = root.getAsJsonObject();
        String value = skin.get("value").getAsString();
        String signature = skin.get("signature").getAsString();
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", value, signature));
        // set the game profile
        SkinChanger.setPlayerProfile((CraftPlayer) player, profile);
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
    }

    public static void set(Player player, JsonObject properties) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getGameProfile();
        // get value and signature
        String value = properties.get("value").getAsString();
        String signature = properties.get("signature").getAsString();
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", value, signature));
        // set the game profile
        SkinChanger.setPlayerProfile((CraftPlayer) player, profile);
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
    }

    public static void remove(Player player) {
        GameProfile profile = ((CraftPlayer) player).getProfile();
        UUID uuid = player.getUniqueId();
        SkinFetcher getter = new SkinFetcher(TARDIS.plugin, uuid);
        getter.fetchAsync((hasResult, fetched) -> {
            if (hasResult) {
                JsonObject properties = fetched.getSkin();
                if (properties != null) {
                    String value = properties.get("value").getAsString();
                    String signature = properties.get("signature").getAsString();
                    profile.getProperties().removeAll("textures");
                    profile.getProperties().put("textures", new Property("textures", value, signature));
                    // set the game profile
                    SkinChanger.setPlayerProfile((CraftPlayer) player, profile);
                    TARDISDisguiseTracker.DISGUISED_AS_PLAYER.remove(uuid);
                }
            }
        });
    }
}
