package me.eccentric_nz.TARDIS.rooms.loader;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.ARS.ARSMapData;
import me.eccentric_nz.TARDIS.ARS.ARSMethods;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ChunkLoaderGUIListener extends ARSMethods implements Listener {

    public ChunkLoaderGUIListener(TARDIS plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLoaderGUIClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!(event.getInventory().getHolder(false) instanceof ChunkLoaderInventory)) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();
        UUID uuid;
        uuid = TARDISSudoTracker.SUDOERS.getOrDefault(playerUUID, playerUUID);
        ids.put(playerUUID, getTardisId(uuid.toString()));
        int slot = event.getRawSlot();
        if (slot != 10 && slot != 45 && !hasLoadedMap.contains(playerUUID)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_LOAD");
            return;
        }
        if (slot < 0 || slot > 53) {
            return;
        }
        switch (slot) {
            case 1, 9, 11, 19 -> moveMap(playerUUID, view, slot); // up, left, right, down
            // TODO
            // load map will need to check if a room has a ticket and display the correct icon,
            // which means getting the actual position in the grid from the chunk coordinates
            case 10 -> loadMap(view, playerUUID, true); // load map
            case 45 -> close(player); // close
            case 27, 28, 29 -> {
                // change levels
                if (map_data.containsKey(playerUUID)) {
                    switchLevel(view, slot, playerUUID);
                    ARSMapData md = map_data.get(playerUUID);
                    setMap(md.getY(), md.getE(), md.getS(), playerUUID, view);
                    setLore(view, slot, null);
                } else {
                    setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
                }
            }
            case 47 -> {
                // TODO
                // process the grid
                // for each ticket
                // get chunk coordinates
                // add chunk coordinate records to database
                // add chunk tickets for each chunk
                // track tardis id to remove Artron per cycle
                // other TODO
                // remove chunk tickets if player is offline
                // remove chunk tickets when server shuts down
                // add chunk tickets when player joins
            }
            case 49 -> {
                // TODO
                // clear all chunk tickets for this TARDIS
            }
            default -> {
                if (map_data.containsKey(playerUUID)) {
                    ARSMapData md = map_data.get(playerUUID);
                    ItemStack is = view.getItem(slot);
                    if (is != null) {
                        String dn = ComponentUtils.stripColour(is.getData(DataComponentTypes.CUSTOM_NAME));
                        // don't allow selection if it has no room in it
                        if (!dn.equals("Empty slot")) {
                            if (is.getType() == Material.NAME_TAG) {
                                // TODO
                                // if room already has a ticket - remove it, restoring the appropriate room icon
                                // remove the chunk from the temp grid
                            } else {
                                // otherwise change the item stack to a ticket (NAME_TAG) keeping the room name
                                // store the chunk in the temp grid for processing - no changes are made to the actual ARS stored data
                            }
                        }
                    }
                } else {
                    setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
                }
            }
        }
    }
}
