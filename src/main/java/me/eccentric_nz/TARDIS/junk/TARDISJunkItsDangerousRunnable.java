/*
 * Copyright (C) 2021 eccentric_nz
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
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISJunkItsDangerousRunnable implements Runnable {

    private final TARDIS plugin;
    private final Block t;
    private final int minX;
    private final int minZ;
    private final int maxX;
    private final int maxZ;
    private final Lightable lightable = (Lightable) Material.REDSTONE_TORCH.createBlockData();
    private int c = 0;

    TARDISJunkItsDangerousRunnable(TARDIS plugin, Location l) {
        this.plugin = plugin;
        t = l.clone().add(0.0d, 2.0d, -1.0d).getBlock();
        minX = l.getBlockX() - 3;
        minZ = l.getBlockZ() - 2;
        maxX = l.getBlockX() + 3;
        maxZ = l.getBlockZ() + 4;
    }

    @Override
    public void run() {
        lightable.setLit(c % 5 >= 3);
        t.setBlockData(lightable);
        // check if player is in Junk TARDIS effects zone
        List<UUID> remove = new ArrayList<>();
        plugin.getGeneralKeeper().getJunkTravellers().forEach((uuid) -> {
            Player p = plugin.getServer().getPlayer(uuid);
            if (p != null && p.isOnline() && !isInside(p.getLocation())) {
                p.setHealth(0);
                remove.add(uuid);
            }
        });
        if (remove.size() > 0) {
            remove.forEach(plugin.getGeneralKeeper().getJunkTravellers()::remove);
        }
        c++;
    }

    private boolean isInside(Location loc) {
        return loc.getX() >= minX && loc.getX() <= maxX && loc.getZ() >= minZ && loc.getZ() <= maxZ;
    }
}
