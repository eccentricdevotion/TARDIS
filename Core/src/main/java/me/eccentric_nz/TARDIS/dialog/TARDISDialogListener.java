package me.eccentric_nz.TARDIS.dialog;

import com.google.gson.JsonElement;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCustomClickEvent;

public class TARDISDialogListener implements Listener {

    private final TARDIS plugin;

    public TARDISDialogListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDialogClick(PlayerCustomClickEvent event) {
        JsonElement json = event.getData();
        plugin.debug(event.getId());
        plugin.debug(json.toString());
    }
}
