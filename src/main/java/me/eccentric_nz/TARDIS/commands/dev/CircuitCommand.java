/*
 * Copyright (C) 2026 eccentric_nz
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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.GlowstoneCircuit;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CircuitCommand {

    private final TARDIS plugin;
    private final List<String> circuits = new ArrayList<>();

    public CircuitCommand(TARDIS plugin) {
        this.plugin = plugin;
        for (DiskCircuit dc : DiskCircuit.values()) {
            if (dc.getMaterial() == Material.GLOWSTONE_DUST) {
                circuits.add(dc.getName());
            }
        }
    }

    public void give(CommandSender sender) {
        if (sender instanceof Player player) {
            // get a random circuit
            String c = circuits.get(TARDISConstants.RANDOM.nextInt(circuits.size()));
            ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(c);
            ItemStack result = recipe.getResult();
            // set the second line of lore
            List<Component> lore = new ArrayList<>(result.getData(DataComponentTypes.LORE).lines());
            Component uses = Component.text(plugin.getConfig().getInt("circuits.uses.invisibility") - 3, NamedTextColor.YELLOW);
            lore.set(1, uses);
            result.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
            player.getInventory().addItem(result);
        }
    }

    public void all(CommandSender sender) {
        for (CircuitVariant circuit : CircuitVariant.values()) {
            ItemStack is = ItemStack.of(Material.GLOWSTONE_DUST);
            try {
                String c = circuit.toString();
                if (c.contains("DAMAGED")) {
                    c = circuit.toString().replace("_DAMAGED", "");
                }
                GlowstoneCircuit gc = GlowstoneCircuit.valueOf(c);
                is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                        .addFloats(circuit.getFloats())
                        .addString(gc.getDisplayName())
                        .build());
                is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(gc.getDisplayName()));
                if (gc.getDisplayName().contains("TARDIS")) {
                    Component uses = (plugin.getConfig().getString("circuits.uses.chameleon", "25").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                            ? Component.text("unlimited", NamedTextColor.YELLOW)
                            : Component.text(plugin.getConfig().getString("circuits.uses.chameleon", "25"), NamedTextColor.YELLOW);
                    is.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                            Component.text("Uses left"),
                            uses
                    )));
                }
                Player player = (Player) sender;
                HashMap<Integer, ItemStack> items = player.getInventory().addItem(is);
                if (!items.isEmpty()) {
                    for (ItemStack item : items.values()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid GlowstoneCircuit " + circuit);
            }
        }
    }
}
