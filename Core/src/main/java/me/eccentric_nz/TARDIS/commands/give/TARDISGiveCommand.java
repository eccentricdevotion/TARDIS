/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.give;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.give.actions.*;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISGiveLister;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISGiveCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISGiveCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisgive then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisgive")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                    new TARDISGiveLister(plugin, sender).list();
                    return true;
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("list_more")) {
                    new TARDISGiveLister(plugin, sender).listMore();
                    return true;
                }
                if (args.length < 3) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                    plugin.getMessenger().message(sender, "/tardisgive [player] [item] [amount]");
                    return true;
                }
                String item = args[1].toLowerCase(Locale.ROOT);
                if (!Give.items.containsKey(item)) {
                    new TARDISGiveLister(plugin, sender).list();
                    return true;
                }
                if (item.equals("kit")) {
                    Player p = plugin.getServer().getPlayer(args[0]);
                    if (p == null) { // player must be online
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    if (!plugin.getKitsConfig().contains("kits." + args[2])) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_KIT");
                        return true;
                    }
                    plugin.getKitsConfig().getStringList("kits." + args[2]).forEach((k) -> new Kit(plugin).give(k, p));
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "GIVE_KIT", sender.getName(), args[2]);
                    return true;
                }
                if (item.equals("blueprint")) {
                    String blueprint = args[2].toUpperCase(Locale.ROOT);
                    if (TARDISGiveTabComplete.getBlueprints().contains(blueprint)) {
                        new TARDISBlueprint(plugin).give(sender, args, blueprint);
                    } else {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_BLUEPRINT");
                    }
                    return true;
                }
                if (item.equals("recipes")) {
                    if (args[2].equalsIgnoreCase("all")) {
                        new TARDISRecipe(plugin).grantMultiple(sender, args);
                    } else {
                        new TARDISRecipe(plugin).grant(sender, args);
                    }
                    return true;
                }
                if (item.equals("seed")) {
                    String seed = args[2].toUpperCase(Locale.ROOT);
                    if (Consoles.getBY_NAMES().containsKey(seed) && !seed.equals("SMALL") && !seed.equals("MEDIUM") && !seed.equals("TALL") && !seed.equals("ARCHIVE")) {
                        if (args.length > 3 && args[3].equalsIgnoreCase("knowledge")) {
                            Player sp = plugin.getServer().getPlayer(args[0]);
                            if (sp == null) { // player must be online
                                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                                return true;
                            }
                            new Knowledge(plugin).give(sender, seed.toLowerCase(Locale.ROOT) + "_seed", sp);
                        } else {
                            new Seed(plugin).give(sender, args);
                        }
                    } else {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_SEED");
                    }
                    return true;
                }
                if (item.equals("system-upgrade")) {
                    return new SystemUpgrades(plugin).give(sender, args[0], args[2]);
                }
                if (item.equals("tachyon")) {
                    new Tachyon(plugin).give(sender, args[0], args[2]);
                    return true;
                }
                int amount;
                switch (args[2]) {
                    case "full" -> amount = plugin.getArtronConfig().getInt("full_charge");
                    case "empty" -> amount = 0;
                    case "knowledge" -> amount = 1;
                    default -> {
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException nfe) {
                            plugin.getMessenger().sendColouredCommand(sender, "ARG_GIVE", "/tardisgive [player] [item] [amount]", plugin);
                            return true;
                        }
                    }
                }
                if (item.equals("artron")) {
                    if (Bukkit.getOfflinePlayer(args[0]).getName() == null) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    new Artron(plugin).give(sender, args[0], amount, args.length > 3 && args[3].equals("timelord"));
                    return true;
                } else {
                    Player player = null;
                    if (args[0].equals("@s") && sender instanceof Player) {
                        player = (Player) sender;
                    } else if (args[0].equals("@p")) {
                        List<Entity> near = Bukkit.selectEntities(sender, "@p");
                        if (!near.isEmpty() && near.getFirst() instanceof Player) {
                            player = (Player) near.getFirst();
                            if (player == null) {
                                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_NEARBY_PLAYER");
                                return true;
                            }
                        }
                    } else {
                        player = plugin.getServer().getPlayer(args[0]);
                    }
                    if (player == null) { // player must be online
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("artron-storage-cell") && args.length == 4 && args[3].equalsIgnoreCase("full")) {
                        new FuelCell(plugin).give(sender, amount, player);
                        return true;
                    } else if (args[2].equals("knowledge")) {
                        if (item.equalsIgnoreCase("all")) {
                            new Knowledge(plugin).giveAll(sender, player);
                        } else {
                            new Knowledge(plugin).give(sender, item, player);
                        }
                        return true;
                    } else if (!args[2].endsWith("_seed")) {
                        return new TARDISItem(plugin).give(sender, item, amount, player);
                    }
                }
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERMS");
                return true;
            }
        }
        return false;
    }
}
