package me.eccentric_nz.TARDIS.handles;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class TARDISHandlesPlaceholder {

    public static String getSubstituted(String s, Player player) {
        return PlaceholderAPI.setPlaceholders(player, s);
    }
}
