package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRegenerations;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RegenerationUtility {

    public static void set(TARDIS plugin, CommandSender sender, Player player, int amount, boolean add) {
        if (!sender.hasPermission("tardis.admin")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_ADMIN");
            return;
        }
        String uuid = player.getUniqueId().toString();
        int max = plugin.getRegenerationConfig().getInt("regenerations", 15);
        if (amount < 0 || amount > max) {
            plugin.getMessenger().send(sender, TardisModule.REGENERATION, "ARG_REGENERATION");
            return;
        }
        // get current regenerations
        ResultSetRegenerations rsr = new ResultSetRegenerations(plugin);
        if (rsr.fromUUID(uuid)) {
            int setA = rsr.getCount() + (add ? amount : -amount);
            if (setA < 0) {
                setA = 0;
            }
            if (setA > max) {
                setA = max;
            }
            HashMap<String, Object> set = new HashMap<>();
            set.put("regenerations", setA);
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid);
            plugin.getQueryFactory().doUpdate("player_prefs", set, where);
            plugin.getMessenger().send(sender, TardisModule.REGENERATION, "REGENERATION_SET", player.getName(), setA);
        }
    }

    public static void block(TARDIS plugin, Player player, String o) {
        if (!o.equals("on") && !o.equals("off")) {
            plugin.getMessenger().send(player, TardisModule.REGENERATION, "PREF_ON_OFF", "regeneration block");
            return;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("regen_block_on", o.equals("on") ? 1 : 0);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        plugin.getQueryFactory().doUpdate("player_prefs", set, where);
        plugin.getMessenger().send(player, TardisModule.REGENERATION, "REGENERATION_BLOCK", o);
    }
}
