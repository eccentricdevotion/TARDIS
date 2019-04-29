/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.files.TARDISFileCopier;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandException;
import org.bukkit.plugin.Plugin;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * The Time Vortex is the dimension through which all time travellers pass. The Vortex was built by the Time Lords as a
 * transdimensional spiral that connected all points in space and time.
 *
 * @author eccentric_nz
 */
public class TARDISSkaro {

    private final TARDIS plugin;

    public TARDISSkaro(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void createDalekWorld() {
        String container = plugin.getServer().getWorldContainer().getAbsolutePath() + File.separator;
        try {
            TARDISFileCopier.copy(container + "Skaro.tar.gz", plugin.getResource("Skaro.tar.gz"), true);
            // decompress the archive
            File archive = new File(container + "Skaro.tar.gz");
            File destination = new File(container);
            Archiver archiver = ArchiverFactory.createArchiver(archive);
            archiver.extract(archive, destination);
            // set a random seed
            plugin.getTardisHelper().setRandomSeed("Skaro");
            archive.delete();
            // load world
            WorldCreator.name("Skaro").type(WorldType.BUFFET).environment(Environment.NORMAL).seed(TARDISConstants.RANDOM.nextLong()).createWorld();
            // add world to config
            plugin.getConfig().set("worlds.Skaro", true);
            plugin.saveConfig();
            // make sure TARDISWeepingAngels can re-disguise Daleks in the Skaro world
            Plugin twa = plugin.getPM().getPlugin("TARDISWeepingAngels");
            if (twa != null) {
                twa.getConfig().set("daleks.worlds.Skaro", 500);
                twa.saveConfig();
            }
        } catch (IOException | CommandException e) {
            plugin.getServer().getLogger().log(Level.SEVERE, "Could not copy Skaro world files to " + container + " {0}", e.getMessage());
        }
    }
}
