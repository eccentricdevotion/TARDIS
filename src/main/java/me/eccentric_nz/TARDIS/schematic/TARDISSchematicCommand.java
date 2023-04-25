/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic;

import java.io.File;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.schematic.actions.*;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISSchematicCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISSchematicCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisschematic")) {
            if (args.length < 1) {
                TARDISMessage.send(sender, "TOO_FEW_ARGS");
                return true;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.send(sender, "CMD_PLAYER");
                return true;
            }
            if (!player.hasPermission("tardis.admin")) {
                TARDISMessage.send(sender, "CMD_ADMIN");
                return true;
            }
            if (args[0].equalsIgnoreCase("paste")) {
                boolean noAir = (args.length == 2 && args[1].equalsIgnoreCase("no_air"));
                SchematicPaster paster = new SchematicPaster(plugin, player, !noAir);
                int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, paster, 1L, 3L);
                paster.setTask(task);
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("pastecsv")) {
                Location eyeLocation = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation();
                String path = plugin.getDataFolder() + File.separator + "user_schematics" + File.separator + "legacy_budget.csv";
                File csv = new File(path);
                if (csv.exists()) {
                    CSVPaster paster = new CSVPaster(plugin);
                    paster.buildLegacy(paster.arrayFromCSV(csv), eyeLocation);
                } else {
                    TARDISMessage.message(player, "Nice try, but it looks like you don't know what this command is for...");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("clear")) {
                return new SchematicClear().act(plugin, player);
            }
            if (args[0].equalsIgnoreCase("remove")) {
                return new SchematicRemoveLights().act(plugin, player);
            }
            if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                TARDISMessage.send(player, "SCHM_NAME");
                return true;
            }
            if (args[0].equalsIgnoreCase("save")) {
                return new SchematicSave().act(plugin, player, args[1]);
            }
            if (args[0].equalsIgnoreCase("replace")) {
                return new SchematicReplace().act(plugin, player, args);
            }
            if (args[0].equalsIgnoreCase("convert")) {
                return new SchematicConvert().act(plugin, player, args);
            }
            if (args[0].equalsIgnoreCase("load")) {
                return new SchematicLoad().act(plugin, player, args);
            }
        }
        return false;
    }
}
