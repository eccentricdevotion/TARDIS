package me.eccentric_nz.TARDIS.commands.artron;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArtronCombine {

    public void meld(TARDIS plugin, Player player) {
        // check item in hand
        ItemStack is = ArtronUtility.hasCell(plugin, player);
        if (is != null) {
            int max = plugin.getArtronConfig().getInt("full_charge");
            ItemStack offhand = player.getInventory().getItemInOffHand();
            if (!offhand.hasData(DataComponentTypes.CUSTOM_NAME)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
                return;
            }
            if (offhand.getAmount() > 1) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_ONE");
                return;
            }
            if (!ComponentUtils.endsWith(offhand.getData(DataComponentTypes.CUSTOM_NAME), "Artron Storage Cell")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CELL_IN_HAND");
                return;
            }
            // get the artron levels of each storage cell
            int mainLevel = ArtronUtility.getLevel(is);
            int offLevel = ArtronUtility.getLevel(offhand);
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
            ArtronUtility.setLevel(is, combined, player, true);
            ArtronUtility.setLevel(offhand, remainder, player, false);
        }
    }
}
