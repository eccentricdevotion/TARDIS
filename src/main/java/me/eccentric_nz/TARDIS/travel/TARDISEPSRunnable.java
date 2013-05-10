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
package me.eccentric_nz.TARDIS.travel;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.thinking.EnterSightBehavior;
import de.kumpelblase2.remoteentities.api.thinking.InteractBehavior;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireLookAtNearest;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Emergency Program One was a feature of the Doctor's TARDIS designed to return
 * a companion to a designated place in case of extreme emergency.
 *
 * @author eccentric_nz
 */
public class TARDISEPSRunnable implements Runnable {

    private TARDIS plugin;
    private Player p;
    private String message;

    public TARDISEPSRunnable(TARDIS plugin, String message, Player p) {
        this.plugin = plugin;
        this.p = p;
        this.message = message;
    }

    @Override
    public void run() {
        // get player location
        Location loc = p.getLocation().getWorld().getSpawnLocation();
        loc.setX(loc.getX() + 0.5);
        loc.setZ(loc.getZ() + 0.5);
        // get player's yaw so we can make the NPC face the player
        float yaw = p.getLocation().getYaw() + 180F;
        // create NPC
        try {
            final RemoteEntity npc = plugin.npcManager.createNamedEntity(RemoteEntityType.Human, loc, p.getName());
            // set some behaviours
            npc.setYaw(yaw);
            npc.setStationary(true);
            npc.getMind().addMovementDesire(new DesireLookAtNearest(npc, Player.class, 8F), 1);
            npc.getMind().addBehaviour(new EnterSightBehavior(npc) {
                @Override
                public void onEnterSight(Player inPlayer) {
                    inPlayer.sendMessage(ChatColor.RED + "[Emergency Program One] " + ChatColor.RESET + message);
                    inPlayer.sendMessage(ChatColor.RED + "[Emergency Program One] " + ChatColor.RESET + "Right-click me to make me go away.");
                }
            });
            npc.getMind().addBehaviour(new InteractBehavior(npc) {
                @Override
                public void onInteract(Player inPlayer) {
                    // remove npc
                    inPlayer.sendMessage(ChatColor.RED + "[Emergency Program One] " + ChatColor.RESET + "Bye!");
                    plugin.npcManager.removeEntity(npc.getID());
                }
            });
        } catch (Exception e) {
            plugin.debug(e);
        }
    }
}
