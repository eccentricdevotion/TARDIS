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
package me.eccentric_nz.TARDIS.handles;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.*;
import me.eccentric_nz.TARDIS.database.data.Program;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetProgramFromEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TARDISHandlesEventListener implements Listener {

    /**
     * Process the program
     */
    private final Callback<Program> programCallback = (Program program) -> {
        Player player = Bukkit.getPlayer(UUID.fromString(program.getUuid()));
        if (player != null && player.isOnline()) {
            ItemStack[] stack = program.getInventory();
            int i = 0;
            for (ItemStack is : stack) {
                // find the ARTRON / DO
                if (is != null) {
                    TARDISHandlesBlock thb = TARDISHandlesBlock.BY_NAME.get(is.getItemMeta().getDisplayName());
                    TARDISHandlesProcessor processor = new TARDISHandlesProcessor(TARDIS.plugin, program, player, program.getProgram_id());
                    switch (thb) {
                        case ARTRON -> {
                            processor.processArtronCommand(i + 1);
                            return;
                        }
                        case DO -> {
                            processor.processCommand(i + 1);
                            return;
                        }
                        default -> {
                        }
                    }
                }
                i++;
            }
        }
    };

    public void onHandlesArtron(TARDISArtronEvent event) {
        getProgram(event.getPlayer().getUniqueId().toString(), "ARTRON");
    }

    public void onHandlesSiegeOff(TARDISSiegeOffEvent event) {
        getProgram(event.getPlayer().getUniqueId().toString(), "SIEGE_OFF");
    }

    public void onHandlesSiegeOn(TARDISSiegeEvent event) {
        getProgram(event.getPlayer().getUniqueId().toString(), "SIEGE_ON");
    }

    public void onHandlesMaterialise(TARDISMaterialisationEvent event) {
        getProgram(event.getPlayer().getUniqueId().toString(), "MATERIALISE");
    }

    public void onHandlesDematerialise(TARDISDematerialisationEvent event) {
        getProgram(event.getPlayer().getUniqueId().toString(), "DEMATERIALISE");
    }

    public void onHandlesEnter(TARDISEnterEvent event) {
        getProgram(event.getPlayer().getUniqueId().toString(), "ENTER");
    }

    public void onHandlesExit(TARDISExitEvent event) {
        getProgram(event.getPlayer().getUniqueId().toString(), "EXIT");
    }

    public void onHandlesHADS(TARDISHADSEvent event) {
        getProgram(event.getPlayer().getUniqueId().toString(), "HADS");
    }

    public void onHandlesLogout(PlayerQuitEvent event) {
        getProgram(event.getPlayer().getUniqueId().toString(), "LOG_OUT");
    }

    public void onHandlesTimeLordDeath(PlayerDeathEvent event) {
        getProgram(event.getEntity().getUniqueId().toString(), "DEATH");
    }

    /**
     * Retrieve a Program asynchronously from the database
     */
    private void getProgram(String uuid, String event) {
        if (TARDIS.plugin.getHandlesConfig().getBoolean("enabled")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    ResultSetProgramFromEvent rs = new ResultSetProgramFromEvent(TARDIS.plugin, uuid, event);
                    if (rs.resultSet()) {
                        programCallback.execute(rs.getProgram());
                    }
                }
            }.runTaskAsynchronously(TARDIS.plugin);
        }
    }

    /**
     * Callback to get data asynchronously from the database.
     *
     * @param <T> The Object type we want to return
     */
    interface Callback<T> {

        void execute(T response);
    }
}
