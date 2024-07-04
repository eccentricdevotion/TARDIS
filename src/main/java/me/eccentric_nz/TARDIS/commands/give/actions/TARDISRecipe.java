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
                        List<String> colours = Arrays.asList("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
                        colours.forEach((bt) -> {
                            NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
                            keys.add(nsk);
                        });
                    }
                    case "jelly-baby" -> {
                        List<String> flavours = Arrays.asList("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
                        flavours.forEach((jelly) -> {
                            NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
                            keys.add(nsk);
                        });
                    }
                    default -> {
                        NamespacedKey nsk = new NamespacedKey(plugin, map.getValue().replace(" ", "_").toLowerCase(Locale.ENGLISH));
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
                List<String> colours = Arrays.asList("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
                colours.forEach((bt) -> {
                    NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
                    keys.add(nsk);
                });
            }
            case "jelly-baby" -> {
                List<String> flavours = Arrays.asList("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
                flavours.forEach((jelly) -> {
                    NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
                    keys.add(nsk);
                });
            }
            default -> {
                NamespacedKey nsk = new NamespacedKey(plugin, Give.items.get(item).replace(" ", "_").toLowerCase(Locale.ENGLISH));
                keys.add(nsk);
            }
        }
        player.discoverRecipes(keys);
    }
}
