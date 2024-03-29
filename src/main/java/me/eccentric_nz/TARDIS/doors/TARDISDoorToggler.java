/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISDoorToggler {

    private final TARDIS plugin;
    private final Block block;
    private final Player player;
    private final boolean minecart;
    private final boolean open;
    private final int id;

    public TARDISDoorToggler(TARDIS plugin, Block block, Player player, boolean minecart, boolean open, int id) {
        this.plugin = plugin;
        this.block = block;
        this.player = player;
        this.minecart = minecart;
        this.open = open;
        this.id = id;
    }

    /**
     * Toggle the door open and closed.
     */
    public void toggleDoors() {
        UUID uuid = player.getUniqueId();
        if (open) {
            new DoorCloserAction(plugin, uuid, id).closeDoors();
        } else {
            new DoorOpenerAction(plugin, uuid, id).openDoors();
        }
        playDoorSound(player, open, block.getLocation(), minecart);
    }

    /**
     * Toggle the door open and closed without playing the door sound.
     */
    public void toggleDoorsWithoutSound(boolean classic) {
        UUID uuid = player.getUniqueId();
        if (open) {
            new DoorCloserAction(plugin, uuid, id).closeDoors();
        } else {
            new DoorOpenerAction(plugin, uuid, id).openDoors();
        }
        if (!classic) {
            playDoorSound(player, open, block.getLocation(), minecart);
        }
    }

    /**
     * Plays a door sound when the iron door is clicked.
     *
     * @param player   a player to play the sound for
     * @param open     which sound to play, open (true), close (false)
     * @param location a location to play the sound at
     * @param minecart whether to play the custom sound (false) or the Minecraft one (true)
     */
    private void playDoorSound(Player player, boolean open, Location location, boolean minecart) {
        if (open) {
            if (!minecart) {
                TARDISSounds.playTARDISSound(location, "tardis_door_close");
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.0F, 1.0F);
            }
        } else if (!minecart) {
            TARDISSounds.playTARDISSound(location, "tardis_door_open");
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1.0F, 1.0F);
        }
    }
}
