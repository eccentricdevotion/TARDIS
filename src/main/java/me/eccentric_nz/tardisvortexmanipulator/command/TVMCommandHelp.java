/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

public class TVMCommandHelp {

    private final TARDIS plugin;

    public TVMCommandHelp(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean display(final CommandSender sender, String[] args) {
        if (!TARDISPermission.hasPermission(sender, "vm.teleport")) {
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        if (args.length == 1) {
            plugin.getMessenger().sendWithColour(sender, TardisModule.VORTEX_MANIPULATOR, "VM_HELP", "#55FFFF");
            plugin.getMessenger().messageWithColour(sender, "Crafting", "#55FF55");
            plugin.getMessenger().sendWithColours(sender, "/tardisrecipe vortex", "#AAAAAA", " - show the crafting recipe for the Vortex Manipulator.", "#FFFFFF");
            plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
            plugin.getMessenger().messageWithColour(sender, "Other help pages", "#55FF55");
            plugin.getMessenger().sendWithColours(sender, "/vmh gui", "#AAAAAA", " - view instructions on using the Vortex Manipulator GUI.", "#FFFFFF");
            plugin.getMessenger().sendWithColours(sender, "/vmh command", "#AAAAAA", " - view Vortex Manipulator commands.", "#FFFFFF");
            plugin.getMessenger().sendWithColours(sender, "/vmh tachyon", "#AAAAAA", " - view Tachyon energy usage for Vortex Manipulator functions.", "#FFFFFF");
            plugin.getMessenger().sendWithColours(sender, "/vmh message", "#AAAAAA", " - view information about Vortex messaging.", "#FFFFFF");
        }
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("command")) {
                plugin.getMessenger().sendWithColour(sender, TardisModule.VORTEX_MANIPULATOR, "VM_HELP", "#55FFFF");
                plugin.getMessenger().messageWithColour(sender, "Commands", "#55FF55");
                sender.sendMessage("All commands must be run while holding the Vortex Manipulator in your hand.");
                sender.sendMessage("You must have enough Tachyon energy to perform most Vortex Manipulator commands.");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "/vm", "#AAAAAA", " - perform a Vortex jump to a random location.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vm [world]", "#AAAAAA", " - perform a Vortex jump to a random location in the specified world.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vm [world] [x] [y] [z]", "#AAAAAA", " - perform a Vortex jump to specific coordinates.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vm go [save]", "#AAAAAA", " - perform a Vortex jump to a saved location.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vm gui", "#AAAAAA", " - open the Vortex Manipulator GUI.", "#FFFFFF");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "/vms", "#AAAAAA", " - show a page of saved locations.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vms [page]", "#AAAAAA", " - show specific page of saved locations.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vms [save name]", "#AAAAAA", " - save a location.", "#FFFFFF");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "/vmr [save name]", "#AAAAAA", " - remove a location.", "#FFFFFF");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "/vmb", "#AAAAAA", " - set a beacon signal at your current location.", "#FFFFFF");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "/vml", "#AAAAAA", " - scan for nearby mobs and players.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vml [player]", "#AAAAAA", " - scan a player's lifesigns.", "#FFFFFF");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "/vma [player]", "#AAAAAA", " - activate a Vortex Manipulator if it was given with the /tardisgive command (admin only).", "#FFFFFF");
            }
            if (args[1].equalsIgnoreCase("gui")) {
                plugin.getMessenger().sendWithColour(sender, TardisModule.VORTEX_MANIPULATOR, "VM_HELP", "#55FFFF");
                plugin.getMessenger().messageWithColour(sender, "GUI", "#55FF55");
                sender.sendMessage("Open the GUI by right-clicking AIR with the Vortex Manipulator.");
                sender.sendMessage("You must have enough Tachyon energy to perform most Vortex Manipulator functions - check the Tachyon Level item.");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "Text entry", "#AAAAAA", " - first click the button for the text you want to change, then use the keypad to enter a character, to move the text position use the 'next' and 'prev' buttons. Use the 'display' to check what you have typed.", "#FFFFFF");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "Random jump", "#AAAAAA", " - just click the 'GO' button.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Random jump in a specific world", "#AAAAAA", " - click the 'world' button, then use the keypad to enter the world name. Click the 'GO' button.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Jump to coordinates", "#AAAAAA", " - click the 'world' button, then use the keypad to enter the world name, repeat for the 'x', 'y', and 'z' buttons to enter coordinates. Click the 'GO' button.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Save the current location", "#AAAAAA", " - click the 'save' button, then use the keypad to enter a save name. Click the 'GO' button.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Load a saved location", "#AAAAAA", " - click the 'load' button, then choose a save (use the 'next' and 'prev' buttons to move between save pages). Click the 'GO' button.", "#FFFFFF");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "Read a message", "#AAAAAA", " - click the 'mail' button, then click a message (use the 'next' and 'prev' buttons to move between message pages). Click the 'read' button.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Delete a message", "#AAAAAA", " - click the 'mail' button, then click a message (use the 'next' and 'prev' buttons to move between message pages). Click the 'delete' button.", "#FFFFFF");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "Scan for lifesigns", "#AAAAAA", " - click the 'life' button, then click the 'GO' button.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Scan a player's lifesigns", "#AAAAAA", " - click the 'life' button, then use the keypad to enter a player's name. Click the 'GO' button.", "#FFFFFF");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "Set a beacon signal", "#AAAAAA", " - click the 'beacon' button.", "#FFFFFF");
            }
            if (args[1].equalsIgnoreCase("tachyon")) {
                plugin.getMessenger().sendWithColour(sender, TardisModule.VORTEX_MANIPULATOR, "VM_HELP", "#55FFFF");
                plugin.getMessenger().messageWithColour(sender, "Tachyon energy", "#55FF55");
                sender.sendMessage("You must have enough Tachyon energy to perform most functions - check the Tachyon Level in the Vortex Manipulator GUI.");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "Maximum charge level", "#AAAAAA", " - " + plugin.getVortexConfig().getInt("tachyon_use.max"), "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Recharge", "#AAAAAA", " - " + plugin.getVortexConfig().getInt("tachyon_use.recharge"), "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Recharge interval", "#AAAAAA", " - " + plugin.getVortexConfig().getInt("tachyon_use.recharge_interval") / 20 + " seconds", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Random travel", "#AAAAAA", " - " + plugin.getVortexConfig().getInt("tachyon_use.travel.random"), "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Random travel in a specific world", "#AAAAAA", " - " + plugin.getVortexConfig().getInt("tachyon_use.travel.world"), "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Coordinates travel", "#AAAAAA", " - " + plugin.getVortexConfig().getInt("tachyon_use.travel.coords"), "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Travel to a saved location", "#AAAAAA", " - " + plugin.getVortexConfig().getInt("tachyon_use.travel.saved"), "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Scan lifesigns", "#AAAAAA", " - " + plugin.getVortexConfig().getInt("tachyon_use.lifesigns"), "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Set a beacon signal", "#AAAAAA", " - " + plugin.getVortexConfig().getInt("tachyon_use.beacon"), "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "Send a message", "#AAAAAA", " - " + plugin.getVortexConfig().getInt("tachyon_use.message"), "#FFFFFF");
            }
            if (args[1].equalsIgnoreCase("message")) {
                plugin.getMessenger().sendWithColour(sender, TardisModule.VORTEX_MANIPULATOR, "VM_HELP", "#55FFFF");
                plugin.getMessenger().messageWithColour(sender, "Messaging", "#55FF55");
                sender.sendMessage("You can only message players who have a Vortex Manipulator.");
                plugin.getMessenger().messageWithColour(sender, "------------", "#55FF55");
                plugin.getMessenger().sendWithColours(sender, "/vmm msg [player] [message]", "#AAAAAA", " - send a message to the specified player.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vmm list in", "#AAAAAA", " - show the most recent page of received messages.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vmm list in [page]", "#AAAAAA", " - show a specific page of received messages.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vmm clear in", "#AAAAAA", " - clears all received messages.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vmm list out", "#AAAAAA", " - show the most recent page of sent messages.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vmm list out [page]", "#AAAAAA", " - show a specific page of sent messages.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vmm clear out", "#AAAAAA", " - clears all sent messages.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vmm read [#]", "#AAAAAA", " - read a specific message.", "#FFFFFF");
                plugin.getMessenger().sendWithColours(sender, "/vmm delete [#]", "#AAAAAA", " - deletes a specific message.", "#FFFFFF");
            }
        }
        return true;
    }
}
