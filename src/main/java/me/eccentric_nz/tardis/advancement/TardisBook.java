/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.advancement;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Book Monsters are creatures were made up entirely of books. They were known to ‘sleep’, and were capable of living
 * for several hundred years. They were hungry creatures which preferred to eat stories to people.
 *
 * @author eccentric_nz
 */
public class TardisBook {

    private final TardisPlugin plugin;

    public TardisBook(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Read text from a file and write it to a book. The book is then placed in the player's inventory.
     *
     * @param bookTitle The name of the book
     * @param author    Who wrote the book
     * @param name      The name of the text file
     * @param player    The player who will receive the book
     */
    public void writeBook(String bookTitle, String author, String name, Player player) {
        // read the file
        File file = new File(plugin.getDataFolder() + File.separator + "books" + File.separator + name + ".txt");
        StringBuilder fileContents = new StringBuilder((int) file.length());
        String bookString = "";
        String lineSeparator = System.getProperty("line.separator");
        try {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    fileContents.append(scanner.nextLine()).append(lineSeparator);
                }
                bookString = fileContents.toString();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            plugin.debug("Could not find file");
        }
        bookString = bookString.replaceAll("@p", player.getName());
        // two line breaks = new page
        List<String> pages = Arrays.asList(bookString.split(lineSeparator + lineSeparator));
        // make the book
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        String[] title = bookTitle.split(" - ");
        assert bookMeta != null;
        bookMeta.setTitle(title[0]);
        bookMeta.setAuthor(author);
        bookMeta.setPages(pages);
        book.setItemMeta(bookMeta);
        // put the book in the player's inventory
        Inventory inventory = player.getInventory();
        inventory.addItem(book);
        player.updateInventory();
        TardisMessage.send(player, "BOOK_RECEIVE", name);
    }
}
