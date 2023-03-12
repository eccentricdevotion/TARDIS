package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TVMCommandHelp implements CommandExecutor {

    private final TARDISVortexManipulator plugin;

    public TVMCommandHelp(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vmh")) {
            if (!sender.hasPermission("vm.teleport")) {
                sender.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(plugin.getPluginName() + ChatColor.AQUA + "Vortex Manipulator help:");
                sender.sendMessage(ChatColor.GREEN + "Crafting");
                sender.sendMessage(ChatColor.GRAY + "/tardisrecipe vortex" + ChatColor.RESET + " - show the crafting recipe for the Vortex Manipulator.");
                sender.sendMessage(ChatColor.GREEN + "------------");
                sender.sendMessage(ChatColor.GREEN + "Other help pages");
                sender.sendMessage(ChatColor.GRAY + "/vmh gui" + ChatColor.RESET + " - view instructions on using the Vortex Manipulator GUI.");
                sender.sendMessage(ChatColor.GRAY + "/vmh command" + ChatColor.RESET + " - view Vortex Manipulator commands.");
                sender.sendMessage(ChatColor.GRAY + "/vmh tachyon" + ChatColor.RESET + " - view Tachyon energy usage for Vortex Manipulator functions.");
                sender.sendMessage(ChatColor.GRAY + "/vmh message" + ChatColor.RESET + " - view information about Vortex messaging.");
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("command")) {
                    sender.sendMessage(plugin.getPluginName() + ChatColor.AQUA + "Vortex Manipulator help:");
                    sender.sendMessage(ChatColor.GREEN + "Commands");
                    sender.sendMessage("All commands must be run while holding the Vortex Manipulator in your hand.");
                    sender.sendMessage("You must have enough Tachyon energy to perform most Vortex Manipulator commands.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "/vm" + ChatColor.RESET + " - perform a Vortex jump to a random location.");
                    sender.sendMessage(ChatColor.GRAY + "/vm [world]" + ChatColor.RESET + " - perform a Vortex jump to a random location in the specified world.");
                    sender.sendMessage(ChatColor.GRAY + "/vm [world] [x] [y] [z]" + ChatColor.RESET + " - perform a Vortex jump to specific coordinates.");
                    sender.sendMessage(ChatColor.GRAY + "/vm go [save]" + ChatColor.RESET + " - perform a Vortex jump to a saved location.");
                    sender.sendMessage(ChatColor.GRAY + "/vm gui" + ChatColor.RESET + " - open the Vortex Manipulator GUI.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "/vms" + ChatColor.RESET + " - show a page of saved locations.");
                    sender.sendMessage(ChatColor.GRAY + "/vms [page]" + ChatColor.RESET + " - show specific page of saved locations.");
                    sender.sendMessage(ChatColor.GRAY + "/vms [save name]" + ChatColor.RESET + " - save a location.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "/vmr [save name]" + ChatColor.RESET + " - remove a location.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "/vmb" + ChatColor.RESET + " - set a beacon signal at your current location.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "/vml" + ChatColor.RESET + " - scan for nearby mobs and players.");
                    sender.sendMessage(ChatColor.GRAY + "/vml [player]" + ChatColor.RESET + " - scan a player's lifesigns.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "/vma [player]" + ChatColor.RESET + " - activate a Vortex Manipulator if it was given with the /tardisgive command (admin only).");
                }
                if (args[0].equalsIgnoreCase("gui")) {
                    sender.sendMessage(plugin.getPluginName() + ChatColor.AQUA + "Vortex Manipulator help:");
                    sender.sendMessage(ChatColor.GREEN + "GUI");
                    sender.sendMessage("Open the GUI by right-clicking AIR with the Vortex Manipulator.");
                    sender.sendMessage("You must have enough Tachyon energy to perform most Vortex Manipulator functions - check the Tachyon Level item.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "Text entry" + ChatColor.RESET + " - first click the button for the text you want to change, then use the keypad to enter a character, to move the text position use the 'next' and 'prev' buttons. Use the 'display' to check what you have typed.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "Random jump" + ChatColor.RESET + " - just click the 'GO' button.");
                    sender.sendMessage(ChatColor.GRAY + "Random jump in a specific world" + ChatColor.RESET + " - click the 'world' button, then use the keypad to enter the world name. Click the 'GO' button.");
                    sender.sendMessage(ChatColor.GRAY + "Jump to coordinates" + ChatColor.RESET + " - click the 'world' button, then use the keypad to enter the world name, repeat for the 'x', 'y', and 'z' buttons to enter coordinates. Click the 'GO' button.");
                    sender.sendMessage(ChatColor.GRAY + "Save the current location" + ChatColor.RESET + " - click the 'save' button, then use the keypad to enter a save name. Click the 'GO' button.");
                    sender.sendMessage(ChatColor.GRAY + "Load a saved location" + ChatColor.RESET + " - click the 'load' button, then choose a save (use the 'next' and 'prev' buttons to move between save pages). Click the 'GO' button.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "Read a message" + ChatColor.RESET + " - click the 'mail' button, then click a message (use the 'next' and 'prev' buttons to move between message pages). Click the 'read' button.");
                    sender.sendMessage(ChatColor.GRAY + "Delete a message" + ChatColor.RESET + " - click the 'mail' button, then click a message (use the 'next' and 'prev' buttons to move between message pages). Click the 'delete' button.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "Scan for lifesigns" + ChatColor.RESET + " - click the 'life' button, then click the 'GO' button.");
                    sender.sendMessage(ChatColor.GRAY + "Scan a player's lifesigns" + ChatColor.RESET + " - click the 'life' button, then use the keypad to enter a player's name. Click the 'GO' button.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "Set a beacon signal" + ChatColor.RESET + " - click the 'beacon' button.");
                }
                if (args[0].equalsIgnoreCase("tachyon")) {
                    sender.sendMessage(plugin.getPluginName() + ChatColor.AQUA + "Vortex Manipulator help:");
                    sender.sendMessage(ChatColor.GREEN + "Tachyon energy");
                    sender.sendMessage("You must have enough Tachyon energy to perform most functions - check the Tachyon Level in the Vortex Manipulator GUI.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "Maximum charge level" + ChatColor.RESET + " - " + plugin.getConfig().getInt("tachyon_use.max"));
                    sender.sendMessage(ChatColor.GRAY + "Recharge" + ChatColor.RESET + " - " + plugin.getConfig().getInt("tachyon_use.recharge"));
                    sender.sendMessage(ChatColor.GRAY + "Recharge interval" + ChatColor.RESET + " - " + plugin.getConfig().getInt("tachyon_use.recharge_interval") / 20 + " seconds");
                    sender.sendMessage(ChatColor.GRAY + "Random travel" + ChatColor.RESET + " - " + plugin.getConfig().getInt("tachyon_use.travel.random"));
                    sender.sendMessage(ChatColor.GRAY + "Random travel in a specific world" + ChatColor.RESET + " - " + plugin.getConfig().getInt("tachyon_use.travel.world"));
                    sender.sendMessage(ChatColor.GRAY + "Coordinates travel" + ChatColor.RESET + " - " + plugin.getConfig().getInt("tachyon_use.travel.coords"));
                    sender.sendMessage(ChatColor.GRAY + "Travel to a saved location" + ChatColor.RESET + " - " + plugin.getConfig().getInt("tachyon_use.travel.saved"));
                    sender.sendMessage(ChatColor.GRAY + "Scan lifesigns" + ChatColor.RESET + " - " + plugin.getConfig().getInt("tachyon_use.lifesigns"));
                    sender.sendMessage(ChatColor.GRAY + "Set a beacon signal" + ChatColor.RESET + " - " + plugin.getConfig().getInt("tachyon_use.beacon"));
                    sender.sendMessage(ChatColor.GRAY + "Send a message" + ChatColor.RESET + " - " + plugin.getConfig().getInt("tachyon_use.message"));
                }
                if (args[0].equalsIgnoreCase("message")) {
                    sender.sendMessage(plugin.getPluginName() + ChatColor.AQUA + "Vortex Manipulator help:");
                    sender.sendMessage(ChatColor.GREEN + "Messaging");
                    sender.sendMessage("You can only message players who have a Vortex Manipulator.");
                    sender.sendMessage(ChatColor.GREEN + "------------");
                    sender.sendMessage(ChatColor.GRAY + "/vmm msg [player] [message]" + ChatColor.RESET + " - send a message to the specified player.");
                    sender.sendMessage(ChatColor.GRAY + "/vmm list in" + ChatColor.RESET + " - show the most recent page of received messages.");
                    sender.sendMessage(ChatColor.GRAY + "/vmm list in [page]" + ChatColor.RESET + " - show a specific page of received messages.");
                    sender.sendMessage(ChatColor.GRAY + "/vmm clear in" + ChatColor.RESET + " - clears all received messages.");
                    sender.sendMessage(ChatColor.GRAY + "/vmm list out" + ChatColor.RESET + " - show the most recent page of sent messages.");
                    sender.sendMessage(ChatColor.GRAY + "/vmm list out [page]" + ChatColor.RESET + " - show a specific page of sent messages.");
                    sender.sendMessage(ChatColor.GRAY + "/vmm clear out" + ChatColor.RESET + " - clears all sent messages.");
                    sender.sendMessage(ChatColor.GRAY + "/vmm read [#]" + ChatColor.RESET + " - read a specific message.");
                    sender.sendMessage(ChatColor.GRAY + "/vmm delete [#]" + ChatColor.RESET + " - deletes a specific message.");
                }
            }
            return true;
        }
        return false;
    }
}
