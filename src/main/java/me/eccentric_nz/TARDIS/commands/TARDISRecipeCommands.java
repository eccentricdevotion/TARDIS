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
import me.eccentric_nz.TARDIS.enumeration.MAP;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

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
        firstArgs.add("a-circuit"); // Admin Circuit
        firstArgs.add("bio-circuit"); // Bio-scanner Circuit
        firstArgs.add("biome-disk"); // Blank Biome Storage Disk
        firstArgs.add("blank"); // Blank Storage Disk
        firstArgs.add("c-circuit"); // Chameleon Circuit
        firstArgs.add("cell"); // Artron Energy Cell
        firstArgs.add("d-circuit"); // Diamond Circuit
        firstArgs.add("e-circuit"); // Emerald Circuit
        firstArgs.add("filter"); // Perception Filter
        firstArgs.add("key"); // TARDIS key
        firstArgs.add("l-circuit"); // Locator Circuit
        firstArgs.add("locator"); // TARDIS Locator
        firstArgs.add("m-circuit"); // Materialisation Circuit
        firstArgs.add("oscillator"); // Sonic Oscillator
        firstArgs.add("p-circuit"); // Perception Circuit
        firstArgs.add("player-disk"); // Blank Player Disk
        firstArgs.add("preset-disk"); // Blank Preset Storage Disk
        firstArgs.add("r-circuit"); // Redstone Circuit
        firstArgs.add("remote"); // Stattenheim Remote
        firstArgs.add("s-circuit"); // Stattenheim Circuit
        firstArgs.add("save-disk"); // Blank Save Storage Disk
        firstArgs.add("sonic"); // Sonic Screwdriver
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisrecipe")) {
            if (!sender.hasPermission("tardis.create")) {
                sender.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
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
            if (args[0].equalsIgnoreCase("key")) {
                this.showShapedRecipe(player, "TARDIS Key");
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
            if (args[0].equalsIgnoreCase("cell")) {
                this.showShapedRecipe(player, "Artron Storage Cell");
                return true;
            }
            if (args[0].equalsIgnoreCase("sonic")) {
                this.showShapedRecipe(player, "Sonic Screwdriver");
                return true;
            }
            if (args[0].equalsIgnoreCase("oscillator")) {
                this.showShapedRecipe(player, "Sonic Oscillator");
                return true;
            }
            if (args[0].equalsIgnoreCase("a-circuit")) {
                this.showShapedRecipe(player, "Server Admin Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("bio-circuit")) {
                this.showShapedRecipe(player, "Bio-scanner Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("r-circuit")) {
                this.showShapedRecipe(player, "Redstone Activator Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("d-circuit")) {
                this.showShapedRecipe(player, "Diamond Disruptor Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("e-circuit")) {
                this.showShapedRecipe(player, "Emerald Environment Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("p-circuit")) {
                this.showShapedRecipe(player, "Perception Circuit");
                return true;
            }
            if (args[0].equalsIgnoreCase("filter")) {
                this.showShapedRecipe(player, "Perception Filter");
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
                if (item.getType().equals(Material.MAP)) {
                    ItemMeta im = item.getItemMeta();
                    im.setDisplayName(getDisplayName(item.getData().getData()));
                    item.setItemMeta(im);
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
            if (ingredients.get(i).getType().equals(Material.MAP)) {
                ItemMeta im = ingredients.get(i).getItemMeta();
                im.setDisplayName(getDisplayName(ingredients.get(i).getData().getData()));
                ingredients.get(i).setItemMeta(im);
            }
            view.setItem(i + 1, ingredients.get(i));
        }
    }

    private String getDisplayName(byte data) {
        MAP map = MAP.getMap(data);
        if (map != null) {
            return map.getDisplayName();
        } else {
            return "Map #" + data;
        }
    }
}
