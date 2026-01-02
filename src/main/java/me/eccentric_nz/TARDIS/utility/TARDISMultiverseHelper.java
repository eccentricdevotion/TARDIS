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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.LoadedMultiverseWorld;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.external.vavr.control.Option;

/**
 * @author eccentric_nz
 */
public class TARDISMultiverseHelper {

    MultiverseCoreApi coreApi = MultiverseCoreApi.get();

    public String getAlias(World world) {
        Option<MultiverseWorld> option = coreApi.getWorldManager().getWorld(world);
        if (option.isDefined()) {
            MultiverseWorld mvw = option.get();
            return mvw.getAlias();
        } else {
            return world.getName();
        }
    }

    public String getAlias(String world) {
        if (Bukkit.getWorld(world) != null) {
            Option<MultiverseWorld> option = coreApi.getWorldManager().getWorld(world);
            if (option.isDefined()) {
                MultiverseWorld mvw = option.get();
                return mvw.getAlias();
            } else {
                return world;
            }
        }
        return world;
    }

    public boolean isWorldSurvival(World world) {
        GameMode gm;
        Option<MultiverseWorld> option = coreApi.getWorldManager().getWorld(world);
        if (option.isDefined()) {
            MultiverseWorld mvw = option.get();
            gm = mvw.getGameMode();
        } else {
            gm = GameMode.SURVIVAL;
        }
        return (gm.equals(GameMode.SURVIVAL));
    }

    public World getWorld(String w) {
        Option<LoadedMultiverseWorld> option = coreApi.getWorldManager().getLoadedWorld(w);
        if (option.isDefined()) {
            LoadedMultiverseWorld mvw = option.get();
            return (mvw.getBukkitWorld().isDefined()) ? mvw.getBukkitWorld().get() : Bukkit.getServer().getWorld(w);
        }
        return Bukkit.getServer().getWorld(w);
    }

    public void importWorlds(CommandSender sender) {
        new TARDISMultiverseImporter(TARDIS.plugin, sender).transfer();
    }
}
