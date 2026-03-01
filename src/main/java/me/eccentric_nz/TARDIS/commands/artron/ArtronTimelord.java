package me.eccentric_nz.TARDIS.commands.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArtronTimelord {

    public void transfer(TARDIS plugin, Player player, int amount) {
        // check item in hand
        ItemStack is = ArtronUtility.hasCell(plugin, player);
        if (is != null) {
            String playerUUID = player.getUniqueId().toString();
            ResultSetPlayerPrefs rs = new ResultSetPlayerPrefs(plugin, playerUUID);
            if (!rs.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return;
            }
            int current_level = rs.getArtronLevel();
            if (current_level - amount < 0) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NOT_ENOUGH");
                return;
            }
            ArtronUtility.chargeCell(plugin, is, player, amount, "player_prefs");
        }
    }
}
