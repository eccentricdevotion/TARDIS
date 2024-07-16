/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.admin;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.BlueprintType;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * TabCompleter for /tardisadmin
 */
public class TARDISAdminTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("arch", "armor_stand", "assemble", "condenser", "config", "convert_database", "create", "decharge", "delete", "disguise", "dispersed", "enter", "find", "list", "make_preset", "maze", "mvimport", "playercount", "prune", "prunelist", "purge", "purge_portals", "recharger", "region_flag", "reload", "repair", "revoke", "set_size", "spawn_abandoned", "undisguise", "update_plugins");
    private final ImmutableList<String> ASS_SUBS = ImmutableList.of("clear", "list");
    private final ImmutableList<String> COMPASS_SUBS = ImmutableList.of("NORTH", "EAST", "SOUTH", "WEST");
    private final ImmutableList<String> ENTITY_SUBS;
    private final ImmutableList<String> LIST_SUBS = ImmutableList.of("abandoned", "portals", "save", "preset_perms", "perms", "recipes", "blueprints");
    private final ImmutableList<String> PRESETS;
    private final ImmutableList<String> SEED_SUBS = ImmutableList.copyOf(Consoles.getBY_NAMES().keySet());
    private final ImmutableList<String> WORLD_SUBS;
    private final List<String> BLUEPRINT_SUBS = new ArrayList<>();
    private final List<String> BLUEPRINT_TYPE_SUBS = new ArrayList<>();
    private final List<String> MAT_SUBS = new ArrayList<>();

    public TARDISAdminTabComplete(TARDIS plugin) {
        List<String> tmpPresets = new ArrayList<>();
        for (ChameleonPreset p : ChameleonPreset.values()) {
            tmpPresets.add(p.toString());
        }
        PRESETS = ImmutableList.copyOf(tmpPresets);
        List<String> worlds = new ArrayList<>();
        plugin.getServer().getWorlds().forEach((w) -> worlds.add(w.getName()));
        WORLD_SUBS = ImmutableList.copyOf(worlds);
        List<String> tmpEntities = new ArrayList<>();
        for (EntityType e : EntityType.values()) {
            if (e.getEntityClass() != null && Creature.class.isAssignableFrom(e.getEntityClass())) {
                tmpEntities.add(e.toString());
            }
        }
        ENTITY_SUBS = ImmutableList.copyOf(tmpEntities);
        for (Permission b : plugin.getDescription().getPermissions()) {
            BLUEPRINT_SUBS.add(b.getName());
        }
        for (BlueprintType type : BlueprintType.values()) {
            BLUEPRINT_TYPE_SUBS.add(type.toString());
        }
        TARDISWalls.BLOCKS.forEach((m) -> MAT_SUBS.add(m.toString()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length == 1) {
            return partial(args[0], ROOT_SUBS);
        } else if (args.length == 2) {
            String sub = args[0].toLowerCase();
            if (sub.equals("assemble") || sub.equals("dispersed")) {
                return partial(lastArg, ASS_SUBS);
            }
            if (sub.equals("disguise")) {
                return partial(lastArg, ENTITY_SUBS);
            }
            if (sub.equals("list")) {
                return partial(lastArg, LIST_SUBS);
            }
            if (sub.equals("arch") || sub.equals("create") || sub.equals("delete") || sub.equals("enter") || sub.equals("purge") || sub.equals("repair") || sub.equals("revoke") || sub.equals("set_size") || sub.equals("undisguise")) {
                // return null to default to online player name matching
                return null;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("spawn_abandoned")) {
                return partial(lastArg, PRESETS);
            }
            if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("set_size")) {
                return partial(lastArg, SEED_SUBS);
            }
            if (args[0].equalsIgnoreCase("disguise")) {
                return null;
            }
            if (args[0].equalsIgnoreCase("revoke")) {
                return partial(lastArg, BLUEPRINT_SUBS);
            }
            if (args[0].equalsIgnoreCase("list") && args[1].equalsIgnoreCase("blueprints")) {
                return partial(lastArg, BLUEPRINT_TYPE_SUBS);
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("create")) {
                return partial(lastArg, MAT_SUBS);
            } else {
                return partial(lastArg, COMPASS_SUBS);
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("create")) {
                return partial(lastArg, MAT_SUBS);
            } else {
                return partial(lastArg, WORLD_SUBS);
            }
        }
        return ImmutableList.of();
    }
}
