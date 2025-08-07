package me.eccentric_nz.TARDIS.commands.dev;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Room;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class TARDISDevRoomsCommand {

    private final TARDIS plugin;

    public TARDISDevRoomsCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean build(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            // get players current location
            Location location = player.getLocation();
            long delay = 0;
            // /tdev rooms [room]...
            for (int i = 1; i < args.length; i++) {
                try {
                    Room r = Room.valueOf(args[i].toUpperCase(Locale.ROOT));
                    JsonObject json = TARDISSchematicGZip.getObject(plugin, "rooms", r.toString().toLowerCase(Locale.ROOT), false);
                    Location l = location.clone().add(20 * i, 10, 0);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // paste schematic
                        TARDISDevRoomPaster paster = new TARDISDevRoomPaster(plugin, player, json, l);
                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, paster, 1L, 3L);
                        paster.setTask(task);
                    }, delay);
                } catch (IllegalArgumentException e) {
                    plugin.debug("Invalid room [" + args[i] + "] in room list!");
                }
                delay += 10;
            }
            return true;
        }
        return false;
    }
}
