package me.eccentric_nz.TARDIS.chemistry.formula;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chemistry.compound.Compound;
import me.eccentric_nz.TARDIS.chemistry.lab.Lab;
import me.eccentric_nz.TARDIS.chemistry.product.Product;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FormulaCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final List<String> ROOT_SUBS = new ArrayList<>();

    public FormulaCommand(TARDIS plugin) {
        this.plugin = plugin;
        for (Compound compound : Compound.values()) {
            ROOT_SUBS.add(compound.toString());
        }
        for (Product product : Product.values()) {
            ROOT_SUBS.add(product.toString());
        }
        for (Lab lab : Lab.values()) {
            ROOT_SUBS.add(lab.toString());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("formula")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "Command can only be used by a player!");
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage(plugin.getPluginName() + "You need to specify the compound or product you want the formula for! Try using tab complete...");
                return false;
            }
            // do stuff
            try {
                Compound compound = Compound.valueOf(args[0]);
                new FormulaViewer(plugin, player).getCompoundFormula(compound);
                return true;
            } catch (IllegalArgumentException ce) {
                try {
                    Product product = Product.valueOf(args[0]);
                    new FormulaViewer(plugin, player).getProductFormula(product);
                    return true;
                } catch (IllegalArgumentException pe) {
                    try {
                        Lab lab = Lab.valueOf(args[0]);
                        new FormulaViewer(plugin, player).getLabFormula(lab);
                        return true;
                    } catch (IllegalArgumentException le) {
                        sender.sendMessage(plugin.getPluginName() + "Could not find a formula for '" + args[0] + "' make sure you typed it correctly.");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Remember that we can return null to default to online player name matching
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        }
        return ImmutableList.of();
    }
}
