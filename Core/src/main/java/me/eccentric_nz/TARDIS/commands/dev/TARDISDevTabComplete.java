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
package me.eccentric_nz.TARDIS.commands.dev;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.particles.ParticleEffect;
import me.eccentric_nz.TARDIS.particles.ParticleShape;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ItemDisplay;

import java.util.ArrayList;
import java.util.List;

/**
 * TabCompleter for /tardisdev
 */
public class TARDISDevTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of(
            "add_regions", "advancements",
            "biome", "box", "brushable",
            "chunks", "chunky", "circuit",
            "debug", "dismount", "displayitem",
            "effect",
            "frame", "furnace",
            "gravity",
            "interaction",
            "label", "list",
            "nms",
            "plurals",
            "recipe", "regen", "reset",
            "screen", "skin", "snapshot", "stats", "systree",
            "tis", "tree", "trim",
            "zero"
    );
    private final ImmutableList<String> LIST_SUBS = ImmutableList.of("preset_perms", "perms", "recipes", "blueprints", "commands", "block_colours", "change", "consoles");
    private final ImmutableList<String> SNAPSHOT_SUBS = ImmutableList.of("in", "out", "c");
    private final ImmutableList<String> STATE_SUBS = ImmutableList.of("closed", "open", "stained", "glass", "fly");
    private final ImmutableList<String> FRAME_SUBS = ImmutableList.of("lock", "unlock");
    private final ImmutableList<String> SCREEN_SUBS = ImmutableList.of("forward", "backward", "left", "right");
    private final ImmutableList<String> DEBUG_SUBS = ImmutableList.of("create", "update", "enter", "exit");
    private final ImmutableList<String> DISPLAY_SUBS = ImmutableList.of("add", "remove", "place", "break", "convert", "chunk", "block", "console");
    private final List<String> STONE_SUBS = new ArrayList<>();
    private final List<String> MAT_SUBS = new ArrayList<>();
    private final List<String> TRANSFORM_SUBS = new ArrayList<>();
    private final List<String> ITEM_SUBS = new ArrayList<>();
    private final List<String> PRESET_SUBS = new ArrayList<>();
    private final List<String> MONSTER_SUBS = new ArrayList<>();
    private final List<String> EFFECT_SUBS = new ArrayList<>();
    private final List<String> SHAPE_SUBS = new ArrayList<>();
    private final ImmutableList<String> RECIPE_SUBS = ImmutableList.of("shaped", "shapeless", "chest", "chemistry", "custom");

    public TARDISDevTabComplete(TARDIS plugin) {
        plugin.getTardisHelper().getTreeMaterials().forEach((m) -> MAT_SUBS.add(m.toString()));
        for (TARDISDisplayItem d : TARDISDisplayItem.values()) {
            STONE_SUBS.add(d.getName());
        }
        for (ItemDisplay.ItemDisplayTransform t : ItemDisplay.ItemDisplayTransform.values()) {
            TRANSFORM_SUBS.add(t.toString());
        }
        for (Material m : Material.values()) {
            if (m.isItem()) {
                ITEM_SUBS.add(m.toString());
            }
        }
        for (ChameleonPreset c : ChameleonPreset.values()) {
            if (c.usesArmourStand()) {
                PRESET_SUBS.add(c.toString());
            }
        }
        for (Monster m : Monster.values()) {
            MONSTER_SUBS.add(m.toString());
        }
        for (ParticleEffect e : ParticleEffect.values()) {
            EFFECT_SUBS.add(e.toString());
        }
        for (ParticleShape s : ParticleShape.values()) {
            SHAPE_SUBS.add(s.toString());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        String sub = args[0];
        switch (args.length) {
            case 1 -> {
                return partial(args[0], ROOT_SUBS);
            }
            case 2 -> {
                if (sub.equals("list")) {
                    return partial(lastArg, LIST_SUBS);
                }
                if (sub.equals("box")) {
                    return partial(lastArg, PRESET_SUBS);
                }
                if (sub.equals("debug")) {
                    return partial(lastArg, DEBUG_SUBS);
                }
                if (sub.equals("nms")) {
                    return partial(lastArg, MONSTER_SUBS);
                }
                if (sub.equals("tree")) {
                    return partial(lastArg, MAT_SUBS);
                }
                if (sub.equals("snapshot")) {
                    return partial(lastArg, SNAPSHOT_SUBS);
                }
                if (sub.equals("displayitem")) {
                    return partial(lastArg, DISPLAY_SUBS);
                }
                if (sub.equals("frame")) {
                    return partial(lastArg, FRAME_SUBS);
                }
                if (sub.equals("recipe")) {
                    return partial(lastArg, RECIPE_SUBS);
                }
                if (sub.equals("effect")) {
                    return partial(lastArg, SHAPE_SUBS);
                }
                if (sub.equals("screen")) {
                    return partial(lastArg, SCREEN_SUBS);
                }
            }
            case 3 -> {
                if (sub.equals("box")) {
                    return partial(lastArg, STATE_SUBS);
                }
                if (sub.equals("effect")) {
                    return partial(lastArg, EFFECT_SUBS);
                }
            }
            case 4 -> {
                if (sub.equals("displayitem")) {
                    return partial(lastArg, TRANSFORM_SUBS);
                }
            }
            default -> {
                return switch (args[1]) {
                    case "place" -> partial(lastArg, STONE_SUBS);
                    case "add" -> partial(lastArg, ITEM_SUBS);
                    default -> partial(lastArg, MAT_SUBS);
                };
            }
        }
        return ImmutableList.of();
    }
}
