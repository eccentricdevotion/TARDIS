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

import com.google.common.io.MoreFiles;
import me.eccentric_nz.TARDIS.TARDIS;

import java.io.File;
import java.io.IOException;

/**
 * @author eccentric_nz
 */
public class LegacyDataPack {

    private final TARDIS plugin;

    public LegacyDataPack(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void remove() {
        // get server's main world folder
        // is there a world container?
        File container = plugin.getServer().getWorldContainer();
        String s_world = plugin.getServer().getWorlds().getFirst().getName();
        String dataPacksTardis = container.getAbsolutePath() + File.separator + s_world + File.separator + "datapacks" + File.separator + "tardis";
        File tardisOldDir = new File(dataPacksTardis);
        // check if the directory exists
        if (tardisOldDir.exists()) {
            // delete directory and files as we now load the datapack from the TARDIS.jar file
            try {
                MoreFiles.deleteRecursively(tardisOldDir.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
