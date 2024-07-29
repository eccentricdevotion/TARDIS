package me.eccentric_nz.tardisregeneration;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRegenerations;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class TARDISRegenerationCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("add", "remove", "block");

    public TARDISRegenerationCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisregeneration")) {
            // need at least 2 arguments
            if (args.length < 2) {
                plugin.getMessenger().send(sender, TardisModule.REGENERATION, "TOO_FEW_ARGS");
                return true;
            }
            if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("add")) {
                if (!sender.hasPermission("tardis.admin")) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_ADMIN");
                    return true;
                }
                if (args.length < 3) {
                    plugin.getMessenger().send(sender, TardisModule.REGENERATION, "TOO_FEW_ARGS");
                    return true;
                }
                Player p = plugin.getServer().getPlayer(args[1]);
                if (p == null) {
                    plugin.getMessenger().send(sender, TardisModule.REGENERATION, "COULD_NOT_FIND_NAME");
                    return true;
                }
                String uuid = p.getUniqueId().toString();
                int amount = TARDISNumberParsers.parseInt(args[2]);
                int max = plugin.getRegenerationConfig().getInt("regenerations", 15);
                if (amount < 0 || amount > max) {
                    plugin.getMessenger().send(sender, TardisModule.REGENERATION, "ARG_REGENERATION");
                    return true;
                }
                // get current regenerations
                ResultSetRegenerations rsr = new ResultSetRegenerations(plugin);
                if (rsr.fromUUID(uuid)) {
                    int setA = rsr.getCount() + (args[0].equalsIgnoreCase("add") ? amount : -amount);
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
                    plugin.getMessenger().send(sender, TardisModule.REGENERATION, "REGENERATION_SET", p.getName(), setA);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("block")) {
                if (sender instanceof Player player) {
                    String tf = args[1].toLowerCase();
                    if (!tf.equals("on") && !tf.equals("off")) {
                        plugin.getMessenger().send(player, TardisModule.REGENERATION, "PREF_ON_OFF", "regeneration block");
                        return true;
                    }
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("regen_block_on", tf.equals("on") ? 1 : 0);
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("uuid", player.getUniqueId().toString());
                    plugin.getQueryFactory().doUpdate("player_prefs", set, where);
                    plugin.getMessenger().send(player, TardisModule.REGENERATION, "REGENERATION_BLOCK", tf);
                    return true;
                } else {
                    plugin.getMessenger().send(sender, TardisModule.REGENERATION, "CMD_NO_CONSOLE");
                    return true;
                }
            }
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return partial(args[0], ROOT_SUBS);
        }
        return ImmutableList.of();
    }
}
