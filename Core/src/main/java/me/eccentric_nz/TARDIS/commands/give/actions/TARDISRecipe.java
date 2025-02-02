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
import me.eccentric_nz.TARDIS.commands.give.Give;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISGiveLister;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class TARDISRecipe {

    private final TARDIS plugin;

    public TARDISRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void grantMultiple(CommandSender sender, String[] args) {
        Player player = plugin.getServer().getPlayer(args[0]);
        if (player == null) { // player must be online
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
            return;
        }
        Set<NamespacedKey> keys = new HashSet<>();
        for (Map.Entry<String, String> map : Give.items.entrySet()) {
            if (!map.getValue().isEmpty()) {
                switch (map.getKey()) {
                    case "bow-tie" -> {
                        List<String> colours = List.of("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
                        colours.forEach((bt) -> {
                            NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
                            keys.add(nsk);
                        });
                    }
                    case "jelly-baby" -> {
                        List<String> flavours = List.of("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
                        flavours.forEach((jelly) -> {
                            NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
                            keys.add(nsk);
                        });
                    }
                    default -> {
                        NamespacedKey nsk = new NamespacedKey(plugin, map.getValue().replace(" ", "_").toLowerCase(Locale.ROOT));
                        keys.add(nsk);
                    }
                }
            }
        }
        player.discoverRecipes(keys);
    }

    public void grant(CommandSender sender, String[] args) {
        Player player = plugin.getServer().getPlayer(args[0]);
        if (player == null) { // player must be online
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
            return;
        }
        String item = args[2].toLowerCase(Locale.ROOT);
        if (!Give.items.containsKey(item)) {
            new TARDISGiveLister(plugin, sender).list();
            return;
        }
        Set<NamespacedKey> keys = new HashSet<>();
        switch (item) {
            case "bow-tie" -> {
                List<String> colours = List.of("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
                colours.forEach((bt) -> {
                    NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
                    keys.add(nsk);
                });
            }
            case "jelly-baby" -> {
                List<String> flavours = List.of("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
                flavours.forEach((jelly) -> {
                    NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
                    keys.add(nsk);
                });
            }
            default -> {
                NamespacedKey nsk = new NamespacedKey(plugin, Give.items.get(item).replace(" ", "_").toLowerCase(Locale.ROOT));
                keys.add(nsk);
            }
        }
        player.discoverRecipes(keys);
    }
}
