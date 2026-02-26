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
package me.eccentric_nz.tardisweepingangels.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Locale;

public class AdminCommand {

    private final TARDIS plugin;
    private final HashMap<Monster, String> types = new HashMap<>();

    public AdminCommand(TARDIS plugin) {
        this.plugin = plugin;
        types.put(Monster.ANGEL_OF_LIBERTY, "angel_of_liberty");
        types.put(Monster.CYBERMAN, "cybermen");
        types.put(Monster.CYBERSHADE, "cybershades");
        types.put(Monster.DALEK, "daleks");
        types.put(Monster.EMPTY_CHILD, "empty_child");
        types.put(Monster.HATH, "hath");
        types.put(Monster.HEADLESS_MONK, "headless_monks");
        types.put(Monster.HEAVENLY_HOST, "heavenly_hosts");
        types.put(Monster.ICE_WARRIOR, "ice_warriors");
        types.put(Monster.JUDOON, "judoon");
        types.put(Monster.K9, "k9");
        types.put(Monster.MIRE, "the_mire");
        types.put(Monster.NIMON, "nimon");
        types.put(Monster.OMEGA, "omega");
        types.put(Monster.OOD, "ood");
        types.put(Monster.SATURNYNIAN, "saturnynians");
        types.put(Monster.SEA_DEVIL, "sea_devils");
        types.put(Monster.SILENT, "silence");
        types.put(Monster.SILURIAN, "silurians");
        types.put(Monster.SLITHEEN, "slitheen");
        types.put(Monster.SMILER, "smilers");
        types.put(Monster.SONTARAN, "sontarans");
        types.put(Monster.SUTEKH, "sutekh");
        types.put(Monster.THE_BEAST, "the_beast");
        types.put(Monster.TOCLAFANE, "toclafane");
        types.put(Monster.VAMPIRE_OF_VENICE, "vampires");
        types.put(Monster.VASHTA_NERADA, "vashta_nerada");
        types.put(Monster.WEEPING_ANGEL, "angels");
        types.put(Monster.ZYGON, "zygons");
    }

    public boolean set(CommandSender sender, String mon, World w, int m) {
        if (w == null) {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "COULD_NOT_FIND_WORLD");
        }
        String which = mon.toUpperCase(Locale.ROOT);
        Monster monster;
        String world = w.getName();
        try {
            monster = Monster.valueOf(which);
            plugin.getMonstersConfig().set(types.get(monster) + ".worlds." + world, m);
        } catch (IllegalArgumentException e) {
            if (which.equals("ALL")) {
                plugin.getMonstersConfig().set("angel_of_liberty.worlds." + world, m);
                plugin.getMonstersConfig().set("angels.worlds." + world, m);
                plugin.getMonstersConfig().set("cybermen.worlds." + world, m);
                plugin.getMonstersConfig().set("cybershades.worlds." + world, m);
                plugin.getMonstersConfig().set("daleks.worlds." + world, m);
                plugin.getMonstersConfig().set("empty_child.worlds." + world, m);
                plugin.getMonstersConfig().set("hath.worlds." + world, m);
                plugin.getMonstersConfig().set("headless_monks.worlds." + world, m);
                plugin.getMonstersConfig().set("heavenly_hosts.worlds." + world, m);
                plugin.getMonstersConfig().set("ice_warriors.worlds." + world, m);
                plugin.getMonstersConfig().set("judoon.worlds." + world, m);
                plugin.getMonstersConfig().set("k9.worlds." + world, true);
                plugin.getMonstersConfig().set("nimon.worlds." + world, m);
                plugin.getMonstersConfig().set("omega.worlds." + world, m);
                plugin.getMonstersConfig().set("ood.worlds." + world, true);
                plugin.getMonstersConfig().set("sea_devils.worlds." + world, m);
                plugin.getMonstersConfig().set("silence.worlds." + world, m);
                plugin.getMonstersConfig().set("silurians.worlds." + world, m);
                plugin.getMonstersConfig().set("slitheen.worlds." + world, m);
                plugin.getMonstersConfig().set("smilers.worlds." + world, m);
                plugin.getMonstersConfig().set("sontarans.worlds." + world, m);
                plugin.getMonstersConfig().set("sutekh.worlds." + world, m);
                plugin.getMonstersConfig().set("the_beast.worlds." + world, m);
                plugin.getMonstersConfig().set("the_mire.worlds." + world, m);
                plugin.getMonstersConfig().set("toclafane.worlds." + world, m);
                plugin.getMonstersConfig().set("vampires.worlds." + world, m);
                plugin.getMonstersConfig().set("vashta_nerada.worlds." + world, m);
                plugin.getMonstersConfig().set("zygons.worlds." + world, m);
            } else {
                plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_INVALID");
                return true;
            }
        }
        plugin.saveConfig();
        plugin.getMessenger().send(sender,TardisModule.MONSTERS, "WA_CONFIG");
        return true;
    }
}
