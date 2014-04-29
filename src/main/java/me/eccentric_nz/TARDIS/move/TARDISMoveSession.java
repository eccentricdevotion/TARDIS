/*
 * Copyright (c) 2011, The Multiverse Team All rights reserved.

 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of the The Multiverse Team nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission. THIS SOFTWARE
 * IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package me.eccentric_nz.TARDIS.move;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMoveSession {

    private boolean staleLocation;
    private Location loc;

    public TARDISMoveSession(Player p) {
        this.setLocation(p.getLocation());
    }

    public void setStaleLocation(boolean active) {
        this.staleLocation = active;
    }

    public boolean isStaleLocation() {
        return this.staleLocation;
    }

    public void setStaleLocation(Location loc) {

        // If the player has not moved, they have a stale location
        if (this.getLocation().getBlockX() == loc.getBlockX() && this.getLocation().getBlockY() == loc.getBlockY() && this.getLocation().getBlockZ() == loc.getBlockZ()) {
            this.setStaleLocation(true);
        } else {
            // Update the Players Session to the new Location.
            this.setLocation(loc);
            // The location is no longer stale.
            this.setStaleLocation(false);
        }
    }

    private void setLocation(Location loc) {
        this.loc = loc;
    }

    public Location getLocation() {
        return this.loc;
    }
}
