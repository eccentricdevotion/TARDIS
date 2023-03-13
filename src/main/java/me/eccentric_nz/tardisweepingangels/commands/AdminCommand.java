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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class AdminCommand {

    private final TARDIS plugin;
    private final HashMap<Monster, String> types = new HashMap<>();

    public AdminCommand(TARDIS plugin) {
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
            sender.sendMessage(plugin.getPluginName() + "Could not find a world with that name!");
            return true;
        }
        int m;
        try {
            m = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getPluginName() + "Last argument must be a number!");
            return false;
        }
        String which = args[1].toUpperCase();
        Monster monster;
        try {
            monster = Monster.valueOf(which);
            plugin.getMonstersConfig().set(types.get(monster) + ".worlds." + args[2], m);
        } catch (IllegalArgumentException e) {
            if (which.equals("all")) {
                plugin.getMonstersConfig().set("angels.worlds." + args[2], m);
                plugin.getMonstersConfig().set("cybermen.worlds." + args[2], m);
                plugin.getMonstersConfig().set("daleks.worlds." + args[2], m);
                plugin.getMonstersConfig().set("empty_child.worlds." + args[2], m);
                plugin.getMonstersConfig().set("hath.worlds." + args[2], m);
                plugin.getMonstersConfig().set("headless_monks.worlds." + args[2], m);
                plugin.getMonstersConfig().set("the_mire.worlds." + args[2], m);
                plugin.getMonstersConfig().set("slitheen.worlds." + args[2], m);
                plugin.getMonstersConfig().set("sea_devils.worlds." + args[2], m);
                plugin.getMonstersConfig().set("ice_warriors.worlds." + args[2], m);
                plugin.getMonstersConfig().set("silence.worlds." + args[2], m);
                plugin.getMonstersConfig().set("sontarans.worlds." + args[2], m);
                plugin.getMonstersConfig().set("ood.worlds." + args[2], true);
                plugin.getMonstersConfig().set("judoon.worlds." + args[2], m);
                plugin.getMonstersConfig().set("toclafane.worlds." + args[2], m);
                plugin.getMonstersConfig().set("k9.worlds." + args[2], true);
                plugin.getMonstersConfig().set("silurians.worlds." + args[2], m);
                plugin.getMonstersConfig().set("vashta_nerada.worlds." + args[2], m);
                plugin.getMonstersConfig().set("zygons.worlds." + args[2], m);
            } else {
                sender.sendMessage(plugin.getPluginName() + "Invalid monster type!");
                return true;
            }
        }
        plugin.saveConfig();
        sender.sendMessage(plugin.getPluginName() + "Monsters config updated!");
        return true;
    }
}
