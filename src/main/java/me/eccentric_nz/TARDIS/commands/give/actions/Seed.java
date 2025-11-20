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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemRegistry;
import me.eccentric_nz.TARDIS.customblocks.TARDISSeedDisplayItem;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Locale;

public class Seed {

    private final TARDIS plugin;

    public Seed(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(CommandSender sender, String[] args) {
        Player player = null;
        if (args[0].equals("@s") && sender instanceof Player) {
            player = (Player) sender;
        } else if (args[0].equals("@p")) {
            List<Entity> near = Bukkit.selectEntities(sender, "@p");
            if (!near.isEmpty() && near.getFirst() instanceof Player) {
                player = (Player) near.getFirst();
                if (player == null) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_NEARBY_PLAYER");
                    return;
                }
            }
        } else {
            player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                return;
            }
        }
        if (player != null) {
            String type = args[2].toUpperCase(Locale.ROOT);
            String wall = "ORANGE_WOOL";
            String floor = "LIGHT_GRAY_WOOL";
            if (args.length > 4) {
                try {
                    wall = Material.valueOf(args[3].toUpperCase(Locale.ROOT)).toString();
                    floor = Material.valueOf(args[4].toUpperCase(Locale.ROOT)).toString();
                } catch (IllegalArgumentException e) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "SEED_MAT_NOT_VALID");
                    return;
                }
            }
            if (Consoles.getBY_NAMES().containsKey(type)) {
                Schematic schm = Consoles.getBY_NAMES().get(type);
                ItemStack is;
                NamespacedKey model = TARDISSeedDisplayItem.CUSTOM.getCustomModel();
                if (schm.isCustom()) {
                    is = ItemStack.of(schm.getSeedMaterial(), 1);
                } else {
                    try {
                        TARDISDisplayItem tdi = TARDISDisplayItemRegistry.valueOf(type);
                        is = ItemStack.of(tdi.getMaterial(), 1);
                        model = tdi.getCustomModel();
                    } catch (IllegalArgumentException e) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SEED_NOT_VALID");
                        return;
                    }
                }
                ItemMeta im = is.getItemMeta();
                im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, model.getKey());
                // set display name
                im.displayName(ComponentUtils.toGold("TARDIS Seed Block"));
                im.lore(List.of(
                        Component.text(type),
                        Component.text("Walls: " + wall),
                        Component.text("Floors: " + floor),
                        Component.text("Chameleon: FACTORY")
                ));
                is.setItemMeta(im);
                player.getInventory().addItem(is);
                player.updateInventory();
                plugin.getMessenger().send(player, TardisModule.TARDIS, "GIVE_ITEM", sender.getName(), "a " + type + " seed block");
            }
        }
    }
}
