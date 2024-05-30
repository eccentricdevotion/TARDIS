package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.SystemUpgrade;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerArtronLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.TARDISSystemTreeGUI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SystemTreeCommand {

    private final TARDIS plugin;

    public SystemTreeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean open(Player player) {
        String uuid = player.getUniqueId().toString();
        // get TARDIS player is in
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (!rst.resultSet()) {
            return true;
        }
        int id = rst.getTardis_id();
        // must be the owner of the TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        wheret.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_OWNER");
            return true;
        }
        // get player's artron energy level
        ResultSetPlayerArtronLevel rsp = new ResultSetPlayerArtronLevel(plugin, id, uuid);
        if (!rsp.resultset()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_TRAVEL_FIRST");
            return true;
        }
        SystemUpgrade current = rsp.getSystemUpgrade();
        ItemStack[] menu = new TARDISSystemTreeGUI(plugin, current).getInventory();
        Inventory upgrades = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS System Upgrades");
        upgrades.setContents(menu);
        player.openInventory(upgrades);
        return true;
    }
}
