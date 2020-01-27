/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.achievement;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
public class TARDISBook {

    private final TARDIS plugin;

    public TARDISBook(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Read text from a file and write it to a book. The book is then placed in the player's inventory.
     *
     * @param title_reward The name of the book
     * @param author       Who wrote the book
     * @param name         The name of the text file
     * @param p            The player who will receive the book
     */
    public void writeBook(String title_reward, String author, String name, Player p) {
        // read the file
        File file = new File(plugin.getDataFolder() + File.separator + "books" + File.separator + name + ".txt");
        StringBuilder fileContents = new StringBuilder((int) file.length());
        String book_str = "";
        String ls = System.getProperty("line.separator");
        try {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    fileContents.append(scanner.nextLine()).append(ls);
                }
                book_str = fileContents.toString();
            }
        } catch (FileNotFoundException f) {
            plugin.debug("Could not find file");
        }
        book_str = book_str.replaceAll("@p", p.getName());
        // two line breaks = new page
        List<String> pages = Arrays.asList(book_str.split(ls + ls));
        // make the book
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        String[] title = title_reward.split(" - ");
        meta.setTitle(title[0]);
        meta.setAuthor(author);
        meta.setPages(pages);
        book.setItemMeta(meta);
        // put the book in the player's inventory
        Inventory inv = p.getInventory();
        inv.addItem(book);
        p.updateInventory();
        TARDISMessage.send(p, "BOOK_RECEIVE", name);
    }
}
