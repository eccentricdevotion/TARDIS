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
package me.eccentric_nz.tardischemistry;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.tardischemistry.compound.Compound;
import me.eccentric_nz.tardischemistry.lab.Lab;
import me.eccentric_nz.tardischemistry.product.Product;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TARDISChemistryTabComplete extends TARDISCompleter implements TabCompleter {

    private final List<String> ROOT_SUBS = Arrays.asList("gui", "formula", "recipe");
    private final List<String> GUI_SUBS = Arrays.asList("creative", "construct", "compound", "reduce", "product", "lab");
    private final List<String> FORMULA_SUBS = new ArrayList<>();
    private final List<String> CREATIVE_SUBS = Arrays.asList("elements", "compounds", "products", "lab");

    public TARDISChemistryTabComplete() {
        for (Compound compound : Compound.values()) {
            FORMULA_SUBS.add(compound.toString());
        }
        for (Product product : Product.values()) {
            FORMULA_SUBS.add(product.toString());
        }
        for (Lab lab : Lab.values()) {
            FORMULA_SUBS.add(lab.toString());
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length == 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0].toLowerCase();
            if (sub.equals("gui") || sub.equals("recipe")) {
                return partial(lastArg, GUI_SUBS);
            }
            if (sub.equals("formula")) {
                return partial(lastArg, FORMULA_SUBS);
            }
        } else if (args.length == 3) {
            return partial(lastArg, CREATIVE_SUBS);
        }
        return ImmutableList.of();
    }
}
