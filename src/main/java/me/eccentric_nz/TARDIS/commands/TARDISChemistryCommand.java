/*
 * Copyright (C) 2021 eccentric_nz
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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.chemistry.block.ChemistryBlock;
import me.eccentric_nz.TARDIS.chemistry.block.RecipeData;
import me.eccentric_nz.TARDIS.chemistry.compound.CompoundCommand;
import me.eccentric_nz.TARDIS.chemistry.constructor.ConstructCommand;
import me.eccentric_nz.TARDIS.chemistry.creative.CreativeCommand;
import me.eccentric_nz.TARDIS.chemistry.formula.FormulaCommand;
import me.eccentric_nz.TARDIS.chemistry.lab.LabCommand;
import me.eccentric_nz.TARDIS.chemistry.product.ProductCommand;
import me.eccentric_nz.TARDIS.chemistry.reducer.ReduceCommand;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TARDISChemistryCommand implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> GUIS = Arrays.asList("creative", "construct", "compound", "reduce", "product", "lab");

    public TARDISChemistryCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardischemistry")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "Command can only be used by a player!");
                return true;
            }
            if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return true;
            }
            if (args[0].equalsIgnoreCase("formula") && TARDISPermission.hasPermission(player, "tardis.formula.show")) {
                return new FormulaCommand(plugin).show(player, args);
            } else if (args[0].equalsIgnoreCase("gui")) {
                if (!TARDISPermission.hasPermission(player, "tardis.chemistry.command")) {
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
                        return true;
                }
            } else if (args[0].equalsIgnoreCase("recipe")) {
                if (!TARDISPermission.hasPermission(sender, "tardis.help")) {
                    TARDISMessage.send(sender, "NO_PERMS");
                    return true;
                }
                String which = args[1].toLowerCase();
                if (!GUIS.contains(which)) {
                    TARDISMessage.message(player, "");
                    return false;
                }
                showBlockRecipe(player, which);
                return true;
            }
            return true;
        }
        return false;
    }

    private void showBlockRecipe(Player player, String which) {
        player.closeInventory();
        plugin.getTrackerKeeper().getRecipeViewers().add(player.getUniqueId());
        Material surround = switch (which) {
            case "creative" -> Material.DIAMOND;
            case "construct" -> Material.LAPIS_LAZULI;
            case "compound" -> Material.REDSTONE;
            case "reduce" -> Material.GOLD_NUGGET;
            case "product" -> Material.IRON_NUGGET;
            default -> // lab
                Material.COAL;
        };
        Inventory inv = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chemistry " + which + " recipe");
        ItemStack ingredient = new ItemStack(surround, 1);
        inv.setItem(0, ingredient);
        inv.setItem(1, ingredient);
        inv.setItem(2, ingredient);
        inv.setItem(9, ingredient);
        inv.setItem(10, new ItemStack(Material.CRAFTING_TABLE, 1));
        inv.setItem(11, ingredient);
        inv.setItem(18, ingredient);
        inv.setItem(19, ingredient);
        inv.setItem(20, ingredient);
        ItemStack result = new ItemStack(Material.RED_MUSHROOM_BLOCK, 1);
        ItemMeta im = result.getItemMeta();
        RecipeData data = ChemistryBlock.RECIPES.get(which);
        im.setDisplayName(data.getDisplayName());
        im.setLore(data.getLore());
        im.setCustomModelData(data.getCustomModelData());
        result.setItemMeta(im);
        inv.setItem(17, result);
        player.openInventory(inv);
    }
}
