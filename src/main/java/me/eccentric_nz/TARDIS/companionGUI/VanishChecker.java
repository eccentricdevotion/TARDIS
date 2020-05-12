package me.eccentric_nz.TARDIS.companionGUI;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class VanishChecker {

    public static boolean canSee(Player player, Player other) {
        return player.canSee(other) && !isVanished(other);
    }

    private static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) {
                return true;
            }
        }
        return false;
    }
}
