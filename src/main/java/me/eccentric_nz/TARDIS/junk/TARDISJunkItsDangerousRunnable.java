/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkItsDangerousRunnable implements Runnable {

    private final TARDIS plugin;
    private final Location l;
    int minX;
    int minZ;
    int maxX;
    int maxZ;

    public TARDISJunkItsDangerousRunnable(TARDIS plugin, Location l) {
        this.plugin = plugin;
        this.l = l;
        minX = this.l.getBlockX() - 2;
        minZ = this.l.getBlockZ() - 2;
        maxX = this.l.getBlockX() + 3;
        maxZ = this.l.getBlockZ() + 3;
    }

    @Override
    public void run() {
        // check if player is in Junk TARDIS effects zone
        List<UUID> remove = new ArrayList<UUID>();
        for (UUID uuid : plugin.getGeneralKeeper().getJunkTravellers()) {
            Player p = plugin.getServer().getPlayer(uuid);
            if (p.isOnline() && !isInside(p.getLocation())) {
                p.setHealth(0);
                remove.add(uuid);
            }
        }
        if (remove.size() > 0) {
            plugin.getGeneralKeeper().getJunkTravellers().removeAll(remove);
        }
    }

    public boolean isInside(Location loc) {
        return loc.getX() >= minX && loc.getX() <= maxX && loc.getZ() >= minZ && loc.getZ() <= maxZ;
    }
}
