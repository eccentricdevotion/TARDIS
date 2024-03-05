package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.dev.wiki.*;
import org.bukkit.command.CommandSender;

public class TARDISWikiRecipeCommand {

    private final TARDIS plugin;

    public TARDISWikiRecipeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean write(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        if (args[1].equalsIgnoreCase("chest")) {
            return new ChestBuilder(plugin).place(sender);
        }
        if (args[1].equalsIgnoreCase("shaped")) {
            return new ShapedPageBuilder(plugin).compile();
        } else if (args[1].equalsIgnoreCase("shapeless")) {
            return new ShapelessPageBuilder(plugin).compile();
        } else if (args[1].equalsIgnoreCase("chemistry")) {
            return new ChemistryPageBuilder(plugin).compile();
        } else if (args[1].equalsIgnoreCase("custom")) {
            return new CustomPageBuilder(plugin).compile();
        }
        return true;
    }
}
