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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
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
        firstArgs.add("remote"); // Stattenheim Remote
        firstArgs.add("locator"); // TARDIS Locator
        firstArgs.add("l-circuit"); // Locator Circuit
        firstArgs.add("m-circuit"); // Materialisation Circuit
        firstArgs.add("s-circuit"); // Stattenheim Circuit
        firstArgs.add("sonic"); // Sonic Screwdriver
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
                sender.sendMessage(plugin.pluginName + "That is not a valid recipe name! Try one of: remote|locator|l-circuit|m-circuit|s-circuit");
                return false;
            }
            ItemStack obs = new ItemStack(Material.OBSIDIAN, 1);
            ItemStack red = new ItemStack(Material.REDSTONE, 1);
            ItemStack qtz = new ItemStack(Material.QUARTZ, 1);
            ItemStack ing = new ItemStack(Material.IRON_INGOT, 1);
            ItemStack dio = new ItemStack(Material.DIODE, 1);
            ItemStack sti = new ItemStack(Material.STICK, 1);
            ItemStack lap = new ItemStack(Material.INK_SACK, 1, (short) 4);
            if (args[0].equalsIgnoreCase("remote")) {
                plugin.trackRecipeView.add(player.getName());
                InventoryView view = player.openWorkbench(null, true);
                view.getTopInventory().setItem(1, obs);
                view.getTopInventory().setItem(2, new ItemStack(Material.STONE_BUTTON, 1));
                view.getTopInventory().setItem(3, obs);
                view.getTopInventory().setItem(4, obs);
                if (plugin.getConfig().getString("difficulty").equalsIgnoreCase("hard")) {
                    ItemStack circuit = new ItemStack(Material.MAP, 1, (short) 1963);
                    ItemMeta im = circuit.getItemMeta();
                    im.setDisplayName("TARDIS Stattenheim Circuit");
                    im.setLore(Arrays.asList(new String[]{"/tardisrecipe s-circuit"}));
                    circuit.setItemMeta(im);
                    view.getTopInventory().setItem(5, circuit);
                } else {
                    view.getTopInventory().setItem(5, lap);
                }
                view.getTopInventory().setItem(6, obs);
                view.getTopInventory().setItem(7, red);
                view.getTopInventory().setItem(8, red);
                view.getTopInventory().setItem(9, red);
                return true;
            }
            if (args[0].equalsIgnoreCase("locator")) {
                //recipe.shape("OIO", "ICI", "OIO");
                plugin.trackRecipeView.add(player.getName());
                InventoryView view = player.openWorkbench(null, true);
                view.getTopInventory().setItem(1, obs);
                view.getTopInventory().setItem(2, ing);
                view.getTopInventory().setItem(3, obs);
                view.getTopInventory().setItem(4, ing);
                ItemStack circuit = new ItemStack(Material.MAP, 1, (short) 1965);
                ItemMeta im = circuit.getItemMeta();
                im.setDisplayName("TARDIS Locator Circuit");
                im.setLore(Arrays.asList(new String[]{"/tardisrecipe l-circuit"}));
                circuit.setItemMeta(im);
                view.getTopInventory().setItem(5, circuit);
                view.getTopInventory().setItem(6, ing);
                view.getTopInventory().setItem(7, obs);
                view.getTopInventory().setItem(8, ing);
                view.getTopInventory().setItem(9, obs);
                return true;
            }
            if (args[0].equalsIgnoreCase("l-circuit")) {
                //recipe.shape("RQR", "RIR", "DRL");
                plugin.trackRecipeView.add(player.getName());
                InventoryView view = player.openWorkbench(null, true);
                view.getTopInventory().setItem(1, red);
                view.getTopInventory().setItem(2, qtz);
                view.getTopInventory().setItem(3, red);
                view.getTopInventory().setItem(4, red);
                view.getTopInventory().setItem(5, ing);
                view.getTopInventory().setItem(6, red);
                view.getTopInventory().setItem(7, dio);
                view.getTopInventory().setItem(8, red);
                view.getTopInventory().setItem(9, lap);
                return true;
            }
            if (args[0].equalsIgnoreCase("m-circuit")) {
                // recipe.shape("IDI", "DLD", "QRQ");
                plugin.trackRecipeView.add(player.getName());
                InventoryView view = player.openWorkbench(null, true);
                if (plugin.getConfig().getString("difficulty").equalsIgnoreCase("hard")) {
                    ing = new ItemStack(Material.EYE_OF_ENDER, 1);
                }
                view.getTopInventory().setItem(1, ing);
                view.getTopInventory().setItem(2, dio);
                view.getTopInventory().setItem(3, ing);
                view.getTopInventory().setItem(4, dio);
                view.getTopInventory().setItem(5, lap);
                view.getTopInventory().setItem(6, dio);
                view.getTopInventory().setItem(7, qtz);
                view.getTopInventory().setItem(8, red);
                view.getTopInventory().setItem(9, qtz);
                return true;
            }
            if (args[0].equalsIgnoreCase("s-circuit")) {
                //recipe.shape("AAA", "LRM", "QQQ");
                plugin.trackRecipeView.add(player.getName());
                InventoryView view = player.openWorkbench(null, true);
                ItemStack lcircuit = new ItemStack(Material.MAP, 1, (short) 1965);
                ItemMeta lim = lcircuit.getItemMeta();
                lim.setDisplayName("TARDIS Locator Circuit");
                lim.setLore(Arrays.asList(new String[]{"/tardisrecipe l-circuit"}));
                lcircuit.setItemMeta(lim);
                view.getTopInventory().setItem(4, lcircuit);
                view.getTopInventory().setItem(5, red);
                ItemStack mcircuit = new ItemStack(Material.MAP, 1, (short) 1964);
                ItemMeta mim = mcircuit.getItemMeta();
                mim.setDisplayName("TARDIS Materialisation Circuit");
                mim.setLore(Arrays.asList(new String[]{"/tardisrecipe m-circuit"}));
                mcircuit.setItemMeta(mim);
                view.getTopInventory().setItem(6, mcircuit);
                view.getTopInventory().setItem(7, qtz);
                view.getTopInventory().setItem(8, qtz);
                view.getTopInventory().setItem(9, qtz);
                return true;
            }
            if (args[0].equalsIgnoreCase("sonic")) {
                // recipe.shape(" R ", " S ", " S ");
                plugin.trackRecipeView.add(player.getName());
                InventoryView view = player.openWorkbench(null, true);
                view.getTopInventory().setItem(2, red);
                view.getTopInventory().setItem(5, sti);
                view.getTopInventory().setItem(8, sti);
                return true;
            }
        }
        return false;
    }
}
