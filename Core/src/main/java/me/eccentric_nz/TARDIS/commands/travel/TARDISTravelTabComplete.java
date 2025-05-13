/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.travel;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

/**
 * TabCompleter for /tardistravel
 */
public class TARDISTravelTabComplete extends TARDISCompleter implements TabCompleter {

    private final List<String> ROOT_SUBS = new ArrayList<>();
    private final Set<String> BIOME_SUBS = new HashSet<>();
    private final List<String> AREA_SUBS = new ArrayList<>();
    private final List<String> STRUCTURE_SUBS;

    public TARDISTravelTabComplete(TARDIS plugin) {
        for (Biome bi : Registry.BIOME) {
            if (!bi.equals(Biome.THE_VOID)) {
                BIOME_SUBS.add(bi.toString());
            }
        }
        if (TARDIS.plugin.getServer().getPluginManager().isPluginEnabled("TerraformGenerator")) {
            BIOME_SUBS.addAll(new TerraBiomes().get());
        }
        ROOT_SUBS.addAll(List.of("home", "biome", "save", "dest", "area", "back", "player", "cave", "village", "structure", "random", "cancel", "costs", "stop"));
        ROOT_SUBS.addAll(plugin.getTardisAPI().getWorlds());
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
        if (rsa.resultSet()) {
            AREA_SUBS.addAll(rsa.getNames());
        }
        STRUCTURE_SUBS = List.of("PILLAGER_OUTPOST", "MINESHAFT", "MINESHAFT_MESA", "MANSION", "JUNGLE_PYRAMID",
                "DESERT_PYRAMID", "IGLOO", "SHIPWRECK", "SHIPWRECK_BEACHED", "SWAMP_HUT", "STRONGHOLD", "MONUMENT",
                "OCEAN_RUIN_COLD", "OCEAN_RUIN_WARM", "FORTRESS", "NETHER_FOSSIL", "END_CITY", "BURIED_TREASURE",
                "BASTION_REMNANT", "VILLAGE_PLAINS", "VILLAGE_DESERT", "VILLAGE_SAVANNA", "VILLAGE_SNOWY", "VILLAGE_TAIGA",
                "RUINED_PORTAL", "RUINED_PORTAL_DESERT", "RUINED_PORTAL_JUNGLE", "RUINED_PORTAL_SWAMP", "RUINED_PORTAL_MOUNTAIN",
                "RUINED_PORTAL_OCEAN", "RUINED_PORTAL_NETHER", "ANCIENT_CITY", "TRAIL_RUINS");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        // Remember that we can return null to default to online player name matching
        String lastArg = args[args.length - 1];
        if (args.length == 1) {
            List<String> part = partial(args[0], ROOT_SUBS);
            return (!part.isEmpty()) ? part : null;
        } else if (args.length == 2) {
            String sub = args[0].toLowerCase(Locale.ROOT);
            if (sub.equals("area")) {
                return partial(lastArg, AREA_SUBS);
            }
            if (sub.equals("biome")) {
                return partial(lastArg, BIOME_SUBS);
            }
            if (sub.equals("village") || sub.equals("structure")) {
                return partial(lastArg, STRUCTURE_SUBS);
            }
            if (sub.equals("player")) {
                return null;
            }
        }
        return ImmutableList.of();
    }
}
