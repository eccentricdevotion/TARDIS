package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeUpdate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class SystemUpgrades {

    private final TARDIS plugin;

    public SystemUpgrades(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean give(CommandSender sender, String player, String upgrade) {
        if (!plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "SYS_DISABLED");
            return true;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        if (offlinePlayer.getName() == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_FOUND");
            return true;
        }
        String uuid = offlinePlayer.getUniqueId().toString();
        // get player's TARDIS id
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (!rst.fromUUID(uuid)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NO_TARDIS");
            return true;
        }
        if (upgrade.equalsIgnoreCase("all")) {
            for (SystemTree st : SystemTree.values()) {
                if (st.getSlot() != -1 && st != SystemTree.UPGRADE_TREE) {
                    new SystemUpgradeUpdate(plugin).set(uuid, rst.getTardisId(), st);
                }
            }
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "SYS_SUCCESS", "full TARDIS");
        } else {
            SystemTree systemTree;
            try {
                systemTree = SystemTree.valueOf(upgrade);
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "SYS_INVALID");
                return true;
            }
            // update system upgrade record
            new SystemUpgradeUpdate(plugin).set(uuid, rst.getTardisId(), systemTree);
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "SYS_SUCCESS", systemTree.getName());
        }
        return true;
    }
}
