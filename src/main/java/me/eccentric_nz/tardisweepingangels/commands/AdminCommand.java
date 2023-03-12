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
package me.eccentric_nz.tardisweepingangels.commands;

import java.util.HashMap;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class AdminCommand {

    private final TARDISWeepingAngels plugin;
    private final HashMap<Monster, String> types = new HashMap<>();

    public AdminCommand(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        types.put(Monster.CYBERMAN, "cybermen");
        types.put(Monster.DALEK, "daleks");
        types.put(Monster.SEA_DEVIL, "sea_devils");
        types.put(Monster.EMPTY_CHILD, "empty_child");
        types.put(Monster.HATH, "hath");
        types.put(Monster.HEADLESS_MONK, "headless_monks");
        types.put(Monster.MIRE, "the_mire");
        types.put(Monster.ICE_WARRIOR, "ice_warriors");
        types.put(Monster.JUDOON, "judoon");
        types.put(Monster.K9, "k9");
        types.put(Monster.OOD, "ood");
        types.put(Monster.SILENT, "silence");
        types.put(Monster.SILURIAN, "silurians");
        types.put(Monster.SLITHEEN, "slitheen");
        types.put(Monster.SONTARAN, "sontarans");
        types.put(Monster.TOCLAFANE, "toclafane");
        types.put(Monster.VASHTA_NERADA, "vashta_nerada");
        types.put(Monster.WEEPING_ANGEL, "angels");
        types.put(Monster.ZYGON, "zygons");
    }

    public boolean set(CommandSender sender, String[] args) {
        if (args.length < 4) {
            return false;
        }
        World w = plugin.getServer().getWorld(args[2]);
        if (w == null) {
            sender.sendMessage(plugin.pluginName + "Could not find a world with that name!");
            return true;
        }
        int m;
        try {
            m = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.pluginName + "Last argument must be a number!");
            return false;
        }
        String which = args[1].toUpperCase();
        Monster monster;
        try {
            monster = Monster.valueOf(which);
            plugin.getConfig().set(types.get(monster) + ".worlds." + args[2], m);
        } catch (IllegalArgumentException e) {
            if (which.equals("all")) {
                plugin.getConfig().set("angels.worlds." + args[2], m);
                plugin.getConfig().set("cybermen.worlds." + args[2], m);
                plugin.getConfig().set("daleks.worlds." + args[2], m);
                plugin.getConfig().set("empty_child.worlds." + args[2], m);
                plugin.getConfig().set("hath.worlds." + args[2], m);
                plugin.getConfig().set("headless_monks.worlds." + args[2], m);
                plugin.getConfig().set("the_mire.worlds." + args[2], m);
                plugin.getConfig().set("slitheen.worlds." + args[2], m);
                plugin.getConfig().set("sea_devils.worlds." + args[2], m);
                plugin.getConfig().set("ice_warriors.worlds." + args[2], m);
                plugin.getConfig().set("silence.worlds." + args[2], m);
                plugin.getConfig().set("sontarans.worlds." + args[2], m);
                plugin.getConfig().set("ood.worlds." + args[2], true);
                plugin.getConfig().set("judoon.worlds." + args[2], m);
                plugin.getConfig().set("toclafane.worlds." + args[2], m);
                plugin.getConfig().set("k9.worlds." + args[2], true);
                plugin.getConfig().set("silurians.worlds." + args[2], m);
                plugin.getConfig().set("vashta_nerada.worlds." + args[2], m);
                plugin.getConfig().set("zygons.worlds." + args[2], m);
            } else {
                sender.sendMessage(plugin.pluginName + "Invalid monster type!");
                return true;
            }
        }
        plugin.saveConfig();
        sender.sendMessage(plugin.pluginName + "Config updated!");
        return true;
    }
}
