/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chemistry.compound.CompoundCommand;
import me.eccentric_nz.TARDIS.chemistry.constructor.ConstructCommand;
import me.eccentric_nz.TARDIS.chemistry.creative.CreativeCommand;
import me.eccentric_nz.TARDIS.chemistry.formula.FormulaCommand;
import me.eccentric_nz.TARDIS.chemistry.lab.LabCommand;
import me.eccentric_nz.TARDIS.chemistry.product.ProductCommand;
import me.eccentric_nz.TARDIS.chemistry.reducer.ReduceCommand;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISChemistryCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISChemistryCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardischemistry")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "Command can only be used by a player!");
                return true;
            }
            // TODO formula first
            if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return true;
            }
            if (args[0].equalsIgnoreCase("formula") && player.hasPermission("tardis.formula.show")) {
                return new FormulaCommand(plugin).show(player, args);
            } else if (args[0].equalsIgnoreCase("gui")) {
                if (!player.hasPermission("tardis.chemistry.command")) {
                    TARDISMessage.send(player, "CHEMISTRY_CMD_PERM");
                    return true;
                }
                switch (args[1].toLowerCase()) {
                    case "creative":
                        if (args.length < 3) {
                            TARDISMessage.send(player, "TOO_FEW_ARGS");
                            return true;
                        }
                        return new CreativeCommand(plugin).open(player, args);
                    case "construct":
                        return new ConstructCommand(plugin).build(player);
                    case "compound":
                        return new CompoundCommand(plugin).create(player);
                    case "reduce":
                        return new ReduceCommand(plugin).use(player);
                    case "product":
                        return new ProductCommand(plugin).craft(player);
                    case "lab":
                        return new LabCommand(plugin).combine(player);
                    default:
                        break;
                }
            }
            return true;
        }
        return false;
    }
}
