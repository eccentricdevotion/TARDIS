package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TARDISUpdateBlocks {

    public static void showOptions(Player player, Updateable updateable) {
        TARDISMessage.message(player, TARDIS.plugin.getPluginName() + ChatColor.AQUA + "'" + updateable.getName() + "'" + ChatColor.RESET + " valid blocks:");
        for (Material m : updateable.getMaterialChoice().getChoices()) {
            String s = m.toString();
            if (s.equals("SPAWNER")) {
                TARDISMessage.message(player, "   ANY BLOCK");
            } else {
                TARDISMessage.message(player, "  - " + TARDISStringUtils.capitalise(s));
            }
        }
    }
}
