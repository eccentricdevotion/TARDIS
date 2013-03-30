/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands;

import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISBook;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command /tardisbook [book].
 *
 * On Alfava Metraxis in the 51st century, River had obtained a book about the
 * Weeping Angels. She gave it to the Doctor, who quickly skimmed through it.
 * The Doctor commented that it was slow in the middle and asked River if she
 * also hated the writer's girlfriend. He asked why there were no images, to
 * which River replied that the "image of an Angel is itself an Angel".
 *
 * @author eccentric_nz
 */
public class TARDISBookCommands implements CommandExecutor {

    private TARDIS plugin;
    HashMap<String, String> books;

    public TARDISBookCommands(TARDIS plugin) {
        this.plugin = plugin;
        this.books = new HashMap<String, String>();
        // filename, title
        this.books.put("lore", "Timelore: The Beginning");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisbook then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisbook")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " This command can only be run by a player");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(plugin.pluginName + "You need to specify a book name!");
                return false;
            }
            String bookname = args[0].toLowerCase(Locale.ENGLISH);
            if (!books.containsKey(bookname)) {
                sender.sendMessage(plugin.pluginName + "Could not find that book!");
                return true;
            }
            TARDISBook book = new TARDISBook(plugin);
            // title, author, filename, player
            book.writeBook(books.get(bookname), "Rassilon", bookname, player);
            return true;
        }
        return false;
    }
}
