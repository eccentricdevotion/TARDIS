package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.give.Give;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Knowledge {

    private final TARDIS plugin;

    public Knowledge(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void giveAll(CommandSender sender, Player player) {
        ItemStack book = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        KnowledgeBookMeta kbm = (KnowledgeBookMeta) book.getItemMeta();
        for (Map.Entry<String, String> map : Give.items.entrySet()) {
            if (!map.getValue().isEmpty()) {
                switch (map.getKey()) {
                    case "bow-tie" -> {
                        List<String> colours = Arrays.asList("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
                        colours.forEach((bt) -> {
                            NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
                            kbm.addRecipe(nsk);
                        });
                    }
                    case "jelly-baby" -> {
                        List<String> flavours = Arrays.asList("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
                        flavours.forEach((jelly) -> {
                            NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
                            kbm.addRecipe(nsk);
                        });
                    }
                    default -> {
                        NamespacedKey nsk = new NamespacedKey(plugin, map.getValue().replace(" ", "_").toLowerCase(Locale.ROOT));
                        kbm.addRecipe(nsk);
                    }
                }
            }
        }
        book.setItemMeta(kbm);
        player.getInventory().addItem(book);
        player.updateInventory();
        plugin.getMessenger().send(player, TardisModule.TARDIS, "GIVE_KNOWLEDGE", sender.getName(), "all TARDIS recipes");
    }

    public void give(CommandSender sender, String item, Player player) {
        String item_to_give = (item.endsWith("_seed")) ? item : Give.items.get(item);
        ItemStack book = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        KnowledgeBookMeta kbm = (KnowledgeBookMeta) book.getItemMeta();
        String message = item_to_give;
        switch (item) {
            case "bow-tie" -> {
                List<String> colours = Arrays.asList("white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "grey", "light_grey", "cyan", "purple", "blue", "brown", "green", "red", "black");
                colours.forEach((bt) -> {
                    NamespacedKey nsk = new NamespacedKey(plugin, bt + "_bow_tie");
                    kbm.addRecipe(nsk);
                });
                message = "Bow Ties";
            }
            case "jelly-baby" -> {
                List<String> flavours = Arrays.asList("vanilla", "orange", "watermelon", "bubblegum", "lemon", "lime", "strawberry", "earl_grey", "vodka", "island_punch", "grape", "blueberry", "cappuccino", "apple", "raspberry", "licorice");
                flavours.forEach((jelly) -> {
                    NamespacedKey nsk = new NamespacedKey(plugin, jelly + "_jelly_baby");
                    kbm.addRecipe(nsk);
                });
                message = "Jelly Babies";
            }
            default -> {
                NamespacedKey nsk = new NamespacedKey(plugin, item_to_give.replace(" ", "_").toLowerCase(Locale.ROOT));
                kbm.addRecipe(nsk);
            }
        }
        book.setItemMeta(kbm);
        player.getInventory().addItem(book);
        player.updateInventory();
        plugin.getMessenger().send(player, TardisModule.TARDIS, "GIVE_KNOWLEDGE", sender.getName(), message);
    }
}
