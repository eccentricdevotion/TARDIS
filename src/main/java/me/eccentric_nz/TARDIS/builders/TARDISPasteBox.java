/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import java.io.File;
import java.io.IOException;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.World;

/**
 * The Box of Tantalus was a place where the Fifth Doctor and his companion went
 * and faced invisible monsters.
 *
 * @author eccentric_nz
 */
public class TARDISPasteBox {

    private final TARDIS plugin;
    CuboidClipboard cc;

    public TARDISPasteBox(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void loadBoxes() {
        String basePath = plugin.getDataFolder() + File.separator + "schematics" + File.separator + "boxes" + File.separator;
        String boxPath = "box.schematic";
        File boxSchematic = new File(basePath + boxPath);
        try {
            cc = SchematicFormat.MCEDIT.load(boxSchematic);
        } catch (IOException io) {
            plugin.debug("Schematic read error: " + io);
        } catch (DataException de) {
            plugin.debug("Schematic data error: " + de);
        }
    }

    private void paste(World world, CuboidClipboard clip, Vector origin) throws DataException, IOException, MaxChangedBlocksException {
        EditSession es = new EditSession(new BukkitWorld(world), 20000);
        clip.paste(es, origin, true);
    }
}
