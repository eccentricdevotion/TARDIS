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
package me.eccentric_nz.TARDIS.listeners;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISCraftListener implements Listener {

    private final TARDIS plugin;

    public TARDISCraftListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftTARDISItem(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (recipe != null) {
            ItemStack is = recipe.getResult();
            CraftingInventory ci = event.getInventory();
            if (is.hasData(DataComponentTypes.CUSTOM_NAME)) {
                String dn = ComponentUtils.stripColour(is.getData(DataComponentTypes.CUSTOM_NAME));
                if (dn.equals("TARDIS Seed Block")) {
                    List<Component> lore = new ArrayList<>(is.getData(DataComponentTypes.LORE).lines());
                    lore.add(Component.text("Walls: " + ci.getItem(6).getType()));
                    lore.add(Component.text("Floors: " + ci.getItem(9).getType()));
                    lore.add(Component.text("Chameleon: FACTORY"));
                    is.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
                    ci.setResult(is);
                } else if (is.getType().equals(Material.GLOWSTONE_DUST)) {
                    if (DiskCircuit.getCircuitNames().contains(dn)) {
                        // which circuit is it?
                        String[] split = dn.split(" ");
                        String which = split[1].toLowerCase(Locale.ROOT);
                        // set the second line of lore
                        List<Component> lore;
                        Component uses = (plugin.getConfig().getString("circuits.uses." + which, "20").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                                ? Component.text("unlimited", NamedTextColor.YELLOW)
                                : Component.text(plugin.getConfig().getString("circuits.uses." + which, "20"), NamedTextColor.YELLOW);
                        if (ComponentUtils.hasLore(is)) {
                            lore = new ArrayList<>(is.getData(DataComponentTypes.LORE).lines());
                            lore.set(1, uses);
                        } else {
                            lore = List.of(Component.text("Uses left"), uses);
                        }
                        is.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
                        ci.setResult(is);
                    }
                } else if (is.getType().equals(Material.IRON_SWORD) && dn.endsWith("Rust Plague Sword")) {
                    // enchant the result
                    is.addEnchantment(Enchantment.SMITE, 2);
                    ci.setResult(is);
                } else if (is.getType().equals(Material.LEATHER_HELMET) && dn.endsWith("3-D Glasses") || dn.endsWith("TARDIS Communicator")) {
                    is.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                            .color(Color.WHITE)
                            .build());
                    ci.setResult(is);
                } else if (dn.contains("Key") || dn.contains("Authorised Control")) {
                    HumanEntity human = event.getView().getPlayer();
                    if (human instanceof Player) {
                        is.editPersistentDataContainer(pdc -> pdc.set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), human.getUniqueId()));
                        List<Component> lore = new ArrayList<>();
                        String what = dn.contains("Key") ? "key" : "disk";
                        lore.add(Component.text("This " + what + " belongs to", NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                        lore.add(Component.text(human.getName(), NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                        is.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
                        ci.setResult(is);
                    }
                } else if (dn.startsWith("Door ")) {
                    // add custom block key to PDC
                    is.editPersistentDataContainer(pdc -> pdc.set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 10000));
                    ci.setResult(is);
                } else if (dn.contains("Stattenheim")) {
                    int uses = plugin.getConfig().getInt("circuits.uses.stattenheim", 15);
                    is.editPersistentDataContainer(pdc -> pdc.set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, uses > 0 ? uses : 1000));
                    ci.setResult(is);
                } else if (is.getType() == Material.GLASS && dn.endsWith("Variable Light")) {
                    // set the lore to the material in the centre
                    Material variable = ci.getItem(5).getType();
                    is.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(variable.toString())).build());
                    ci.setResult(is);
                }
            }
        }
    }
}
