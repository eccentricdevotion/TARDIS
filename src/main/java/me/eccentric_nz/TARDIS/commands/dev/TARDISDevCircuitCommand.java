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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TARDISDevCircuitCommand {

    private final TARDIS plugin;
    private final List<String> circuits = new ArrayList<>();

    public TARDISDevCircuitCommand(TARDIS plugin) {
        this.plugin = plugin;
        for (DiskCircuit dc : DiskCircuit.values()) {
            if (dc.getMaterial() == Material.GLOWSTONE_DUST) {
                circuits.add(dc.getName());
            }
        }
    }

    public boolean give(CommandSender sender) {
        if (sender instanceof Player player) {
            // get a random circuit
            String c = circuits.get(TARDISConstants.RANDOM.nextInt(circuits.size()));
            ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(c);
            ItemStack result = recipe.getResult();
            // set the second line of lore
            ItemMeta im = result.getItemMeta();
            List<Component> lore = im.lore();
            Component uses = Component.text(plugin.getConfig().getInt("circuits.uses.invisibility") - 3, NamedTextColor.YELLOW);
            lore.set(1, uses);
            im.lore(lore);
            result.setItemMeta(im);
            player.getInventory().addItem(result);
        }
        return true;
    }
}
