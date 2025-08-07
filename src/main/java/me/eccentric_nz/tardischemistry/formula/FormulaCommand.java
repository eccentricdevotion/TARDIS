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
package me.eccentric_nz.tardischemistry.formula;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischemistry.compound.Compound;
import me.eccentric_nz.tardischemistry.lab.Lab;
import me.eccentric_nz.tardischemistry.product.Product;
import org.bukkit.entity.Player;

public class FormulaCommand {

    private final TARDIS plugin;

    public FormulaCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean show(Player player, String[] args) {
        if (args.length < 2) {
            plugin.getMessenger().message(player, "You need to specify the compound or product you want the formula for! Try using tab complete...");
            return false;
        }
        // do stuff
        FormulaViewer holder = new FormulaViewer(plugin, player);
        try {
            Compound compound = Compound.valueOf(args[1]);
            holder.getCompoundFormula(compound);
            player.openInventory(holder.getInventory());
            return true;
        } catch (IllegalArgumentException ce) {
            try {
                Product product = Product.valueOf(args[1]);
                holder.getProductFormula(product);
                player.openInventory(holder.getInventory());
                return true;
            } catch (IllegalArgumentException pe) {
                try {
                    Lab lab = Lab.valueOf(args[1]);
                    holder.getLabFormula(lab);
                    player.openInventory(holder.getInventory());
                    return true;
                } catch (IllegalArgumentException le) {
                    plugin.getMessenger().message(player, "Could not find a formula for '" + args[1] + "' make sure you typed it correctly.");
                    return true;
                }
            }
        }
    }
}
