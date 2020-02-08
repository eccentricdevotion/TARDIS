package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.enumeration.ADAPTION;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TARDISChameleonControl {

    private final TARDIS plugin;

    public TARDISChameleonControl(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, int id, ADAPTION adapt, PRESET preset) {
        TARDISCircuitChecker tcc = null;
        if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
            tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
        }
        if (tcc != null && !tcc.hasChameleon()) {
            TARDISMessage.send(player, "CHAM_MISSING");
            return;
        }
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
            return;
        }
        if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
            TARDISMessage.send(player, "NOT_WHILE_DISPERSED");
            return;
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            TARDISMessage.send(player.getPlayer(), "NOT_IN_VORTEX");
            return;
        }
        // open Chameleon Circuit GUI
        ItemStack[] cc = new TARDISChameleonInventory(plugin, adapt, preset).getMenu();
        Inventory cc_gui = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Circuit");
        cc_gui.setContents(cc);
        player.openInventory(cc_gui);
    }
}
