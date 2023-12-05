package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateDestinationTerminalForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TerminalAction {

    private final TARDIS plugin;

    public TerminalAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player, int id, Tardis tardis, TARDISCircuitChecker tcc) {
        if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id) || (!tardis.isHandbrake_on() && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) || plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
            return;
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            plugin.getTrackerKeeper().getHasRandomised().add(id);
        }
        if (tardis.getArtron_level() < plugin.getArtronConfig().getInt("travel")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
            return;
        }
        if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "INPUT_MISSING");
            return;
        }
        UUID playerUUID = player.getUniqueId();
        if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
            new FloodgateDestinationTerminalForm(plugin, playerUUID).send();
        } else {
            ItemStack[] items = new TARDISTerminalInventory(plugin).getTerminal();
            Inventory aec = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Destination Terminal");
            aec.setContents(items);
            player.openInventory(aec);
        }
    }
}
