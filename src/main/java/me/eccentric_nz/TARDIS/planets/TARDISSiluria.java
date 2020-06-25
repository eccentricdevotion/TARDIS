/*
 * Copyright (C) 2020 eccentric_nz
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
import org.bukkit.command.CommandException;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TARDISSiluria {

    private final TARDIS plugin;

    public TARDISSiluria(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void createSilurianUnderworld() {
        String container = plugin.getServer().getWorldContainer().getAbsolutePath() + File.separator;
        try {
            TARDISFileCopier.copy(container + "Siluria.tar.gz", plugin.getResource("Siluria.tar.gz"), true);
            // decompress the archive
            File archive = new File(container + "Siluria.tar.gz");
            File destination = new File(container);
            Archiver archiver = ArchiverFactory.createArchiver(archive);
            archiver.extract(archive, destination);
            // set a random seed
            plugin.getTardisHelper().setRandomSeed("Siluria");
            archive.delete();
            // load world
//            WorldCreator.name("Siluria").type(WorldType.BUFFET).environment(Environment.NORMAL).seed(TARDISConstants.RANDOM.nextLong()).createWorld();
            WorldCreator.name("Siluria").environment(Environment.NORMAL).seed(TARDISConstants.RANDOM.nextLong()).createWorld();
            // add world to config
            plugin.getPlanetsConfig().set("planets.Siluria.time_travel", true);
            plugin.savePlanetsConfig();
        } catch (IOException | CommandException e) {
            plugin.getServer().getLogger().log(Level.SEVERE, "Could not copy Siluria world files to " + container + " {0}", e.getMessage());
        }
    }
}
