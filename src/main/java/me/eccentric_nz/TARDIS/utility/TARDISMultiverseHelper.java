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
package me.eccentric_nz.TARDIS.utility;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * @author eccentric_nz
 */
public class TARDISMultiverseHelper {

    private final MultiverseCore mvc;

    public TARDISMultiverseHelper(Plugin mvplugin) {
        mvc = (MultiverseCore) mvplugin;
    }

    public String getAlias(World world) {
        MultiverseWorld mvw = mvc.getMVWorldManager().getMVWorld(world);
        return (mvw != null) ? mvw.getAlias() : world.getName();
    }

    public String getAlias(String world) {
        if (Bukkit.getWorld(world) != null) {
            MultiverseWorld mvw = mvc.getMVWorldManager().getMVWorld(world);
            return (mvw != null) ? mvw.getAlias() : world;
        } else {
            return world;
        }
    }

    public boolean isWorldSurvival(World world) {
        MultiverseWorld mvw = mvc.getMVWorldManager().getMVWorld(world);
        GameMode gm = (mvw != null) ? mvw.getGameMode() : GameMode.SURVIVAL;
        return (gm.equals(GameMode.SURVIVAL));
    }

    public World getWorld(String w) {
        MultiverseWorld mvw = mvc.getMVWorldManager().getMVWorld(w);
        return (mvw != null) ? mvw.getCBWorld() : Bukkit.getServer().getWorld(w);
    }

    public void importWorlds(CommandSender sender) {
        new TARDISMultiverseImporter(TARDIS.plugin, mvc, sender).transfer();
    }
}
