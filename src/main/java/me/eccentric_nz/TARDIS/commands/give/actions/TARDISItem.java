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
package me.eccentric_nz.TARDIS.commands.give.actions;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.give.Give;
import me.eccentric_nz.TARDIS.commands.give.TARDISDisplayBlockCommand;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardischemistry.lab.Lab;
import me.eccentric_nz.tardischemistry.lab.LabBuilder;
import me.eccentric_nz.tardischemistry.product.Product;
import me.eccentric_nz.tardischemistry.product.ProductBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TARDISItem {

    private final TARDIS plugin;
    private final List<String> CHEM_BLOCKS = List.of("Atomic Elements", "Chemical Compounds", "Lab Table", "Product Crafting", "Material Reducer", "Element Constructor");

    public TARDISItem(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean give(CommandSender sender, String item, int amount, Player player) {
        if (amount > 64) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_MAX");
            return true;
        }
        if ((item.equals("untempered-schism") || item.equals("elixir-of-life")) && !plugin.getConfig().getBoolean("modules.regeneration")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "RECIPE_REGENERATION");
            return true;
        }
        if ((item.equals("sonic-blaster") || item.equals("blaster-battery") || item.equals("landing-pad")) && !plugin.getConfig().getBoolean("modules.sonic_blaster")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "RECIPE_BLASTER");
            return true;
        }
        if (item.equals("k9") && !plugin.getConfig().getBoolean("modules.weeping_angels")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "RECIPE_K9");
            return true;
        }
        String item_to_give = Give.items.get(item);
        RecipeCategory category = RecipeCategory.ITEMS;
        try {
            RecipeItem recipeItem = RecipeItem.valueOf(TARDISStringUtils.toEnumUppercase(item_to_give));
            category = recipeItem.getCategory();
        } catch (IllegalArgumentException ignored) {
        }
        ItemStack result = null;
        ItemMeta im = null;
        if (category == RecipeCategory.CHEMISTRY) {
            if (!plugin.getConfig().getBoolean("modules.chemistry")) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "RECIPE_CHEMISTRY");
                return true;
            }
            String enumName;
            if (item.equalsIgnoreCase("balloon") || item.equalsIgnoreCase("glow-stick") || item.equalsIgnoreCase("sparkler")) {
                switch (item) {
                    case "balloon" -> enumName = "Red_Balloon";
                    case "glow-stick" -> enumName = "Light_Blue_Glow_Stick";
                    default -> enumName = "Green_Sparkler";
                }
            } else {
                enumName = item_to_give.replaceAll(" ", "_");
            }
            if (CHEM_BLOCKS.contains(item_to_give)) {
                result = plugin.getFigura().getShapedRecipes().get(item_to_give).getResult();
            } else {
                try {
                    Product product = Product.valueOf(enumName);
                    result = ProductBuilder.getProduct(product);
                } catch (IllegalArgumentException e) {
                    try {
                        Lab lab = Lab.valueOf(enumName);
                        result = LabBuilder.getLabProduct(lab);
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        } else {
            if (item.equals("vortex-manipulator") && !plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "RECIPE_VORTEX");
                return true;
            }
            if (item.equals("acid-bucket") || item.equals("rust-bucket")) {
                result = ItemStack.of((item.equals("acid-bucket") ? Material.WATER_BUCKET : Material.LAVA_BUCKET), 1);
                im = result.getItemMeta();
            } else if (item.equals("save-storage-disk") || item.equals("preset-storage-disk") || item.equals("biome-storage-disk") || item.equals("player-storage-disk") || item.equals("bowl-of-custard") || item.equals("jelly-baby") || item.equals("schematic-wand") || item.equals("judoon-ammunition")) {
                result = plugin.getIncomposita().getShapelessRecipes().get(item_to_give).getResult();
            } else if (Give.custom.contains(item)) {
                result = new TARDISDisplayBlockCommand(plugin).getStack(item);
            } else {
                result = plugin.getFigura().getShapedRecipes().get(item_to_give).getResult();
            }
            if (item.equals("vortex-manipulator")) {
                plugin.getMessenger().sendColouredCommand(sender, "GIVE_VORTEX", "/vm activate " + player.getName(), plugin);
            }
            if (item.equals("invisibility-circuit")) {
                // set the second line of lore
                im = result.getItemMeta();
                List<Component> lore = im.lore();
                Component uses = (plugin.getConfig().getString("circuits.uses.invisibility", "5").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                        ? Component.text("unlimited", NamedTextColor.YELLOW)
                        : Component.text(plugin.getConfig().getString("circuits.uses.invisibility", "5"), NamedTextColor.YELLOW);
                lore.set(1, uses);
                im.lore(lore);
            }
            if (item.equals("blank") || item.equals("save-disk") || item.equals("preset-disk") || item.equals("biome-disk") || item.equals("player-disk") || item.equals("blaster") || item.equals("control")) {
                im = result.getItemMeta();
                im.addItemFlags(ItemFlag.values());
                im.setAttributeModifiers(Multimaps.forMap(Map.of()));
            }
            if (item.equals("key") || item.equals("control")) {
                im = result.getItemMeta();
                im.getPersistentDataContainer().set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
                List<Component> lore = im.lore();
                if (lore == null) {
                    lore = new ArrayList<>();
                }
                String what = item.equals("key") ? "key" : "disk";
                lore.add(Component.text("This " + what + " belongs to", NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                lore.add(Component.text(player.getName(), NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                im.lore(lore);
            }
        }
        if (result != null) {
            if (im == null) {
                im = result.getItemMeta();
            }
            im.displayName(ComponentUtils.toWhite(Give.items.get(item)));
            result.setItemMeta(im);
            result.setAmount(amount);
            player.getInventory().addItem(result);
            player.updateInventory();
            plugin.getMessenger().send(player, TardisModule.TARDIS, "GIVE_ITEM", sender.getName(), amount + " " + item_to_give);
        }
        return true;
    }
}
