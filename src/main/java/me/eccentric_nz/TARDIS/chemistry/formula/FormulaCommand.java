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
package me.eccentric_nz.TARDIS.chemistry.formula;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chemistry.compound.Compound;
import me.eccentric_nz.TARDIS.chemistry.lab.Lab;
import me.eccentric_nz.TARDIS.chemistry.product.Product;
import org.bukkit.entity.Player;

public class FormulaCommand {

    private final TARDIS plugin;

    public FormulaCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean show(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(plugin.getPluginName() + "You need to specify the compound or product you want the formula for! Try using tab complete...");
            return false;
        }
        // do stuff
        try {
            Compound compound = Compound.valueOf(args[1]);
            new FormulaViewer(plugin, player).getCompoundFormula(compound);
            return true;
        } catch (IllegalArgumentException ce) {
            try {
                Product product = Product.valueOf(args[1]);
                new FormulaViewer(plugin, player).getProductFormula(product);
                return true;
            } catch (IllegalArgumentException pe) {
                try {
                    Lab lab = Lab.valueOf(args[1]);
                    new FormulaViewer(plugin, player).getLabFormula(lab);
                    return true;
                } catch (IllegalArgumentException le) {
                    player.sendMessage(plugin.getPluginName() + "Could not find a formula for '" + args[1] + "' make sure you typed it correctly.");
                    return true;
                }
            }
        }
    }
}
