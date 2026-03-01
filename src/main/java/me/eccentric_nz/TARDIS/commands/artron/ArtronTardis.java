package me.eccentric_nz.TARDIS.commands.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArtronTardis {

    public void transfer(TARDIS plugin, Player player, int amount) {
        // check item in hand
        ItemStack is = ArtronUtility.hasCell(plugin, player);
        if (is != null) {
            String playerUUID = player.getUniqueId().toString();
            ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
            if (!rs.fromUUID(playerUUID)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return;
            }
            int current_level = rs.getArtronLevel();
            if (current_level - amount < plugin.getArtronConfig().getInt("comehere")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NO_TRANSFER");
                return;
            }
            ArtronUtility.chargeCell(plugin, is, player, amount, "player_prefs");
        }
    }
}
