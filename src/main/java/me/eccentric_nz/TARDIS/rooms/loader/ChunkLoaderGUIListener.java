package me.eccentric_nz.TARDIS.rooms.loader;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.UUID;

public class ChunkLoaderGUIListener extends TicketMethods implements Listener {

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
        UUID uuid = TARDISSudoTracker.SUDOERS.getOrDefault(playerUUID, playerUUID);
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
            // up, left, right, down
            case 1, 9, 11, 19 -> moveMap(playerUUID, view, slot);
            case 10 -> loadTickets(view, playerUUID, true); // load map
            case 45 -> close(player); // close
            case 27, 28, 29 -> {
                // change levels
                if (ticketData.containsKey(playerUUID)) {
                    switchLevel(view, slot, playerUUID);
                    TicketData md = ticketData.get(playerUUID);
                    setMap(md.getY(), md.getE(), md.getS(), playerUUID, view);
                    setLore(view, slot, null);
                } else {
                    setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
                }
            }
            case 47 -> processTickets(playerUUID, player); // process the grid for tickets
            case 49 -> clearTickets(playerUUID, player); // clear all chunk tickets for this TARDIS
            default -> {
                if (ticketData.containsKey(playerUUID)) {
                    ItemStack is = view.getItem(slot);
                    if (is != null) {
                        String dn = ComponentUtils.stripColour(is.getData(DataComponentTypes.CUSTOM_NAME));
                        // don't allow selection if it has no room in it
                        if (!dn.equals("Empty slot")) {
                            ItemStack clicked;
                            if (is.getType() == Material.NAME_TAG) {
                                // if room already has a ticket - remove it, restoring the appropriate room icon
                                // remove the chunk from the temp grid
                                String name  = ComponentUtils.stripColour(is.getData(DataComponentTypes.CUSTOM_NAME));
                                TARDISARS ars = TARDISARS.valueOf(name.toUpperCase(Locale.ROOT));
                                // set back to room material
                                clicked = is.withType(Material.valueOf(ars.getMaterial()));
                                clicked.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("Not loaded")).build());
                            } else {
                                // otherwise change the item stack to a ticket (NAME_TAG) keeping the room name
                                // store the chunk in the temp grid for processing - no changes are made to the actual ARS stored data
                                clicked = is.withType(Material.NAME_TAG);
                                clicked.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("Loaded" )).build());
                            }
                            view.setItem(slot, clicked);
                            updateTickets(playerUUID, slot, clicked);
                        }
                    }
                } else {
                    setLore(view, slot, plugin.getLanguage().getString("ARS_LOAD", "You need to load the map first!"));
                }
            }
        }
    }
}
