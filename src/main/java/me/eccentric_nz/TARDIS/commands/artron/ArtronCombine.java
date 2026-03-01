package me.eccentric_nz.TARDIS.commands.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArtronCombine {

    public void meld(TARDIS plugin, Player player) {
        // check item in hand
        ItemStack is = ArtronUtility.hasCell(plugin, player);
        if (is != null) {
            ItemMeta im = is.getItemMeta();
            int max = plugin.getArtronConfig().getInt("full_charge");
            ItemStack offhand = player.getInventory().getItemInOffHand();
            if (!offhand.hasItemMeta()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
                return;
            }
            if (offhand.getAmount() > 1) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_ONE");
                return;
            }
            ItemMeta offMeta = offhand.getItemMeta();
            if (!offMeta.hasDisplayName() || !ComponentUtils.endsWith(offMeta.displayName(), "Artron Storage Cell")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
                return;
            }
            // get the artron levels of each storage cell
            int mainLevel = ArtronUtility.getLevel(im);
            int offLevel = ArtronUtility.getLevel(offMeta);
            if (mainLevel <= 0 || offLevel <= 0) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_NOT_CHARGED");
                return;
            }
            int combined = mainLevel + offLevel;
            int remainder = 0;
            if (combined > max) {
                remainder = combined - max;
                combined = max;
            }
            ArtronUtility.setLevel(is, im, combined, player, true);
            ArtronUtility.setLevel(offhand, offMeta, remainder, player, false);
        }
    }
}
