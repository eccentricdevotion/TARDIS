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
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISJunkItsDangerousRunnable implements Runnable {

    private final TARDIS plugin;
    private final Location l;
    private final Block t;
    int minX;
    int minZ;
    int maxX;
    int maxZ;
    int c = 0;

    public TARDISJunkItsDangerousRunnable(TARDIS plugin, Location l) {
        this.plugin = plugin;
        this.l = l;
        t = l.clone().add(0.0d, 2.0d, -1.0d).getBlock();
        minX = this.l.getBlockX() - 3;
        minZ = this.l.getBlockZ() - 2;
        maxX = this.l.getBlockX() + 3;
        maxZ = this.l.getBlockZ() + 4;
    }

    @Override
    public void run() {
        if (c % 5 < 3) {
            t.setType(Material.REDSTONE_TORCH);
        }
        // check if player is in Junk TARDIS effects zone
        List<UUID> remove = new ArrayList<>();
        plugin.getGeneralKeeper().getJunkTravellers().forEach((uuid) -> {
            Player p = plugin.getServer().getPlayer(uuid);
            if (p.isOnline() && !isInside(p.getLocation())) {
                p.setHealth(0);
                remove.add(uuid);
            }
        });
        if (remove.size() > 0) {
            plugin.getGeneralKeeper().getJunkTravellers().removeAll(remove);
        }
        c++;
    }

    public boolean isInside(Location loc) {
        return loc.getX() >= minX && loc.getX() <= maxX && loc.getZ() >= minZ && loc.getZ() <= maxZ;
    }
}
