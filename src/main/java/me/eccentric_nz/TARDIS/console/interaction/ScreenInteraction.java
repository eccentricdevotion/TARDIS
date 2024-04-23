package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ControlMonitor;
import me.eccentric_nz.TARDIS.console.models.ColourType;
import me.eccentric_nz.TARDIS.console.models.ConcoleColourChanger;
import me.eccentric_nz.TARDIS.control.actions.ControlMenuAction;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class ScreenInteraction {

    private final TARDIS plugin;

    public ScreenInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void display(int id, Interaction interaction, boolean coords, Player player) {
        // if shift-click change display else open Control Menu GUI
        if (player.isSneaking()) {
            // get the text display
            TextDisplay display = getTextDisplay(interaction.getLocation(), coords);
            if (display != null) {
                display.setRotation(Location.normalizeYaw(300), -10f);
                new ControlMonitor(plugin).update(id, display.getUniqueId(), coords);
            }
        } else {
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (Tag.CONCRETE_POWDER.isTagged(hand.getType())) {
                int amount = hand.getAmount();
                if (amount < 6) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CONSOLE_COLOUR_AMOUNT");
                    return;
                }
                // get colour
                int colour = ColourType.LOOKUP.getOrDefault(hand.getType(), 1);
                // get the UUIDs
                String uuids = interaction.getPersistentDataContainer().get(plugin.getUnaryKey(), PersistentDataType.STRING);
                if (uuids != null) {
                    // change the colour of the console
                    if (new ConcoleColourChanger(plugin, interaction.getLocation(), uuids, colour).paint()) {
                        // take the concrete powder
                        if (amount < 7) {
                            player.getInventory().setItemInMainHand(null);
                        } else {
                            hand.setAmount(amount - 6);
                            player.getInventory().setItemInMainHand(hand);
                        }
                    }
                }
            } else {
                // open control menu
                new ControlMenuAction(plugin).openGUI(player, id);
            }
        }
    }

    private TextDisplay getTextDisplay(Location location, boolean coords) {
        TextDisplay textDisplay = null;
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1.0d, 1.0d, 1.0d, (e) -> e.getType() == EntityType.TEXT_DISPLAY)) {
            textDisplay = (TextDisplay) entity;
            break;
        }
        if (textDisplay == null) {
            Location adjusted = location.clone();
            Vector vector = coords ? new Vector(0.0d, 0.5d, 0.35d) : new Vector(0.32d, 0.5d, -0.225d);
            adjusted.add(vector);
            textDisplay = (TextDisplay) location.getWorld().spawnEntity(adjusted, EntityType.TEXT_DISPLAY);
        }
        return textDisplay;
    }
}
