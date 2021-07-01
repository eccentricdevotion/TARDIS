package me.eccentric_nz.tardis.utility;

import org.bukkit.Bukkit;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class TardisFloodgate {

    public static boolean isBedrockPlayer(UUID uuid) {
        return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
    }

    public static boolean isFloodgateEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("floodgate");
    }

    public static String sanitisePlayerName(String name) {
        String replace = "^" + FloodgateApi.getInstance().getPlayerPrefix();
        return name.replaceFirst(replace, "");
    }
}
