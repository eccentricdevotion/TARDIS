package me.eccentric_nz.TARDIS.arch;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

public class TARDISArchDisguise {

    public static void disguise(Player player, String name) {
        TARDIS.plugin.getTardisHelper().disguise(player, name);
    }

    public static void undisguise(Player player) {
        TARDIS.plugin.getTardisHelper().reset(player);
    }
}
