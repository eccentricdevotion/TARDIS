package me.eccentric_nz.TARDIS.regeneration;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseTracker;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SkinChangerPaper {

    public static void set(Player player, String json) {
        PlayerProfile profile = player.getPlayerProfile();
        // load JSON
        JsonElement root = JsonParser.parseString(json);
        JsonObject skin = root.getAsJsonObject();
        String texture = skin.get("value").getAsString();
        String signature = skin.get("signature").getAsString();
        profile.getProperties().removeIf(profileProperty -> profileProperty.getName().equals("textures"));
        profile.getProperties().add(new ProfileProperty("textures", texture, signature));
        player.setPlayerProfile(profile);
        SkinChanger.REGENERATED.put(player.getUniqueId(), new TARDISDisguiseTracker.ProfileData(((CraftPlayer) player).getHandle().getGameProfile().getProperties(), player.getName()));
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player && player.getWorld() == p.getWorld()) {
                p.hidePlayer(player);
                p.showPlayer(player);
            }
        }
    }
}
