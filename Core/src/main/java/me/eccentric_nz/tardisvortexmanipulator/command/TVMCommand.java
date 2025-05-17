/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class TVMCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TVMCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vm")) {
            // help
            if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
                return new TVMCommandHelp(plugin).display(sender, args);
            }
            // activate
            if (args[0].equalsIgnoreCase("activate")) {
                return new TVMCommandActivate(plugin).process(sender, args);
            }
            // give
            if (args[0].equalsIgnoreCase("give")) {
                return new TVMCommandGive(plugin).process(sender, args);
            }
            // must be a player
            if (sender instanceof Player player) {
                if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
                    plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
                    return true;
                }
                ItemStack is = player.getInventory().getItemInMainHand();
                if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().endsWith("Vortex Manipulator")) {
                    switch (args[0].toLowerCase(Locale.ROOT)) {
                        case "gui" -> {
                            return new TVMCommandGUI(plugin).open(player);
                        }
                        case "go" -> {
                            if (args.length == 5 && args[0].equalsIgnoreCase("go")) {
                                return new TVMCommandCoords(plugin).execute(player, args);
                            } else {
                                return new TVMCommandGo(plugin).execute(player, args);
                            }
                        }
                        case "beacon" -> {
                            return new TVMCommandBeacon(plugin).process(player);
                        }
                        case "lifesigns" -> {
                            return new TVMCommandLifesigns(plugin).scan(player, args);
                        }
                        case "message" -> {
                            return new TVMCommandMessage(plugin).process(player, args);
                        }
                        case "remove" -> {
                            return new TVMCommandRemove(plugin).process(player, args);
                        }
                        case "save" -> {
                            return new TVMCommandSave(plugin).process(player, args);
                        }
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_HAND");
                    return true;
                }
            } else {
                plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "CMD_PLAYER");
                return true;
            }
        }
        return false;
    }
}
