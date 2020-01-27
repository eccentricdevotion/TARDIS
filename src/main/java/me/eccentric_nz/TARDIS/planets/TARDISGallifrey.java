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
import org.bukkit.WorldType;
import org.bukkit.command.CommandException;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 * <p>
 * Gallifrey is the homeworld of the Time Lords. It is believed to have been destroyed in the Last Great Time War but
 * was later discovered to be frozen in a pocket universe by the first thirteen incarnations of the Doctor, surviving
 * the Time War. The literal translation of Gallifrey is "They that walk in the shadows"
 */
public class TARDISGallifrey {

    private final TARDIS plugin;

    public TARDISGallifrey(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void createTimeLordWorld() {
        String container = plugin.getServer().getWorldContainer().getAbsolutePath() + File.separator;
        try {
            TARDISFileCopier.copy(container + "Gallifrey.tar.gz", plugin.getResource("Gallifrey.tar.gz"), true);
            // decompress the archive
            File archive = new File(container + "Gallifrey.tar.gz");
            File destination = new File(container);
            Archiver archiver = ArchiverFactory.createArchiver(archive);
            archiver.extract(archive, destination);
            // set a random seed
            plugin.getTardisHelper().setRandomSeed("Gallifrey");
            archive.delete();
            // load world
            WorldCreator.name("Gallifrey").type(WorldType.BUFFET).environment(Environment.NORMAL).seed(TARDISConstants.RANDOM.nextLong()).createWorld();
            // add world to config
            plugin.getPlanetsConfig().set("planets.Gallifrey.time_travel", true);
            plugin.savePlanetsConfig();
        } catch (IOException | CommandException e) {
            plugin.getServer().getLogger().log(Level.SEVERE, "Could not copy Gallifrey world files to " + container + " {0}", e.getMessage());
        }
    }
}
