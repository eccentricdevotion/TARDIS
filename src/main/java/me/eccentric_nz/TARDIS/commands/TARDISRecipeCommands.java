/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

/**
 * A Time Control Unit is a golden sphere about the size of a Cricket ball. It
 * is stored in the Secondary Control Room. All TARDISes have one of these
 * devices, which can be used to remotely control a TARDIS by broadcasting
 * Stattenheim signals that travel along the time contours in the Space/Time
 * Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISRecipeCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<String>();

    public TARDISRecipeCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("remote"); // Stattenheim Remote
        firstArgs.add("locator"); // TARDIS Locator
        firstArgs.add("l-circuit"); // Locator Circuit
        firstArgs.add("m-circuit"); // Materialisation Circuit
        firstArgs.add("s-circuit"); // Stattenheim Circuit
        firstArgs.add("c-circuit"); // Chameleon Circuit
        firstArgs.add("sonic"); // Sonic Screwdriver
        firstArgs.add("blank"); // Blank Storage Disk
        firstArgs.add("save-disk"); // Blank Save Storage Disk
        firstArgs.add("preset-disk"); // Blank Preset Storage Disk
        firstArgs.add("biome-disk"); // Blank Biome Storage Disk
        firstArgs.add("player-disk"); // Blank Player Disk
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisrecipe")) {
            if (!sender.hasPermission("tardis.use")) {
                sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                return false;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + "You must be a player to run this command!");
                return false;
            }
            if (args.length < 1) {
                sender.sendMessage(plugin.pluginName + "Too few command arguments!");
                return false;
            }
            if (!firstArgs.contains(args[0].toLowerCase(Locale.ENGLISH))) {
                sender.sendMessage(plugin.pluginName + "That is not a valid recipe name! Try one of: remote|locator|l-circuit|m-circuit|s-circuit|c-circuit|sonic|blank|save-disk|preset-disk|biome-disk|player-disk");
                return true;
            }
            if (args[0].equalsIgnoreCase("remote")) {
                showShapedRecipe(player, "Stattenheim Remote");
                return true;
            }
            if (args[0].equalsIgnoreCase("locator")) {
                showShapedRecipe(player, "TARDIS Locator");
                return true;
            }
            if (args[0].equalsIgnoreCase("l-circuit")) {
                this.showShapedRecipe(player, "TARDIS Locator Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("m-circuit")) {
                this.showShapedRecipe(player, "TARDIS Materialisation Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("s-circuit")) {
                showShapedRecipe(player, "TARDIS Stattenheim Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("c-circuit")) {
                showShapedRecipe(player, "TARDIS Chameleon Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("sonic")) {
                this.showShapedRecipe(player, "Sonic Screwdriver");
                return true;
            }
            if (args[0].equalsIgnoreCase("blank")) {
                this.showShapedRecipe(player, "Blank Storage Disk");
                return true;
            }
            if (args[0].equalsIgnoreCase("save-disk")) {
                this.showShapelessRecipe(player, "Save Storage Disk");
                return true;
            }
            if (args[0].equalsIgnoreCase("preset-disk")) {
                this.showShapelessRecipe(player, "Blank Preset Storage Disk");
                return true;
            }
            if (args[0].equalsIgnoreCase("biome-disk")) {
                this.showShapelessRecipe(player, "Blank Biome Storage Disk");
                return true;
            }
            if (args[0].equalsIgnoreCase("player-disk")) {
                this.showShapelessRecipe(player, "Player Storage Disk");
                return true;
            }
        }
        return false;
    }

    public void showShapedRecipe(Player p, String str) {
        ShapedRecipe recipe = plugin.figura.getShapedRecipes().get(str);
        p.closeInventory();
        plugin.trackRecipeView.add(p.getName());
        final InventoryView view = p.openWorkbench(null, true);
        final String[] recipeShape = recipe.getShape();
        final Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
        for (int j = 0; j < recipeShape.length; j++) {
            for (int k = 0; k < recipeShape[j].length(); k++) {
                final ItemStack item = ingredientMap.get(recipeShape[j].toCharArray()[k]);
                if (item == null) {
                    continue;
                }
                item.setAmount(0);
                view.getTopInventory().setItem(j * 3 + k + 1, item);
            }
        }
    }

    public void showShapelessRecipe(Player player, String str) {
        ShapelessRecipe recipe = plugin.incomposita.getShapelessRecipes().get(str);
        final List<ItemStack> ingredients = recipe.getIngredientList();
        plugin.trackRecipeView.add(player.getName());
        final InventoryView view = player.openWorkbench(null, true);
        for (int i = 0; i < ingredients.size(); i++) {
            view.setItem(i + 1, ingredients.get(i));
        }
    }
}
