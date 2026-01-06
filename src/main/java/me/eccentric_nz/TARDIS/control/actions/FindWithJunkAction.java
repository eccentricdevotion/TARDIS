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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.junk.JunkControlListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FindWithJunkAction {

    private final TARDIS plugin;

    public FindWithJunkAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void getNearbyChunkLocation(int id, Player player) {
        // set destination to a surrounding chunk where the player's TARDIS is located
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (rst.fromUUID(player.getUniqueId().toString())) {
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, rst.getTardisId());
            if (rsc.resultSet() && rsc.getCurrent().location().getWorld() != null) {
                Chunk chunk = getRandomSurroundingChunk(rsc.getCurrent().location().getChunk());
                // get a random location in the chunk
                int x = TARDISConstants.RANDOM.nextInt(16) + (chunk.getX() * 16);
                int z = TARDISConstants.RANDOM.nextInt(16) + (chunk.getZ() * 16);
                Sign sign = JunkControlListener.getDestinationSign(id);
                sign.getSide(Side.FRONT).line(1, Component.text(chunk.getWorld().getName()));
                sign.getSide(Side.FRONT).line(2, Component.text(x));
                sign.getSide(Side.FRONT).line(3, Component.text(z));
                sign.update();
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "EXTERMINATE_NONE");
        }
    }

    private Chunk getRandomSurroundingChunk(Chunk chunk) {
        // get a random block face
        BlockFace face = plugin.getGeneralKeeper().getSurrounding().get(TARDISConstants.RANDOM.nextInt(plugin.getGeneralKeeper().getSurrounding().size()));
        Vector vector = face.getDirection();
        double cx = (vector.getX() < 0) ? Math.floor(vector.getX()) : Math.ceil(vector.getX());
        double cz = (vector.getZ() < 0) ? Math.floor(vector.getZ()) : Math.ceil(vector.getZ());
        int x = (int) (chunk.getX() + cx);
        int z = (int) (chunk.getZ() + cz);
        return chunk.getWorld().getChunkAt(x, z);
    }
}
