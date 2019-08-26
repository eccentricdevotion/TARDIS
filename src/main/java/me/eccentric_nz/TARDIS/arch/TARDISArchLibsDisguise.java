package me.eccentric_nz.TARDIS.arch;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.entity.Player;

public class TARDISArchLibsDisguise {

    public static void disguise(Player player, String name) {
        PlayerDisguise playerDisguise = new PlayerDisguise(name);
        playerDisguise.setHideHeldItemFromSelf(false);
        playerDisguise.setViewSelfDisguise(false);
        DisguiseAPI.disguiseToAll(player, playerDisguise);
    }

    public static void undisguise(Player player) {
        if (DisguiseAPI.isDisguised(player)) {
            DisguiseAPI.undisguiseToAll(player);
        }
    }
}
