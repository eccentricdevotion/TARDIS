package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.*;
import me.eccentric_nz.TARDIS.database.ResultSetProgramFromEvent;
import me.eccentric_nz.TARDIS.database.data.Program;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TARDISHandlesEventListener implements Listener {

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
     * Callback to get data asynchronously from the database.
     *
     * @param <T> The Object type we want to return
     */
    interface Callback<T> {

        void execute(T response);
    }

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
                        case ARTRON:
                            processor.processArtronCommand(i + 1);
                            return;
                        case DO:
                            processor.processCommand(i + 1);
                            return;
                    }
                }
                i++;
            }
        }
    };

    /**
     * Retrieve a Program asynchronously from the database
     */
    private void getProgram(String uuid, String event) {
        if (TARDIS.plugin.getConfig().getBoolean("allow.handles")) {
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
}
