package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ConsoleDestroyer;
import me.eccentric_nz.TARDIS.console.ControlMonitor;
import me.eccentric_nz.TARDIS.console.models.ColourType;
import me.eccentric_nz.TARDIS.console.models.ConsoleColourChanger;
import me.eccentric_nz.TARDIS.control.actions.ControlMenuAction;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Locale;

public class ScreenInteraction {

    private final TARDIS plugin;

    public ScreenInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void display(int id, Interaction interaction, boolean coords, Player player) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        // if shift-click change display else open Control Menu GUI
        if (player.isSneaking()) {
            // get the text display
            TextDisplay display = getTextDisplay(interaction.getLocation(), coords, id);
            if (display != null) {
                display.setRotation(Location.normalizeYaw(120), -7.5f);
                new ControlMonitor(plugin).update(id, display.getUniqueId(), coords);
            }
        } else {
            ItemStack hand = player.getInventory().getItemInMainHand();
            Material material = hand.getType();
            if (Tag.CONCRETE_POWDER.isTagged(material) || material == Material.COPPER_INGOT) {
                int amount = hand.getAmount();
                if (amount < 6) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CONSOLE_COLOUR_AMOUNT");
                    return;
                }
                // get colour
                String colour = (material == Material.COPPER_INGOT) ? "rustic" : material.toString().toLowerCase(Locale.ROOT).replace("_concrete_powder", "");
                // get the UUIDs
                String uuids = interaction.getPersistentDataContainer().get(plugin.getUnaryKey(), PersistentDataType.STRING);
                if (uuids != null) {
                    // change the colour of the console
                    if (new ConsoleColourChanger(plugin, interaction.getLocation(), uuids, colour).paint()) {
                        // take the concrete powder
                        if (amount < 7) {
                            player.getInventory().setItemInMainHand(null);
                        } else {
                            hand.setAmount(amount - 6);
                            player.getInventory().setItemInMainHand(hand);
                        }
                    }
                }
            } else if (Tag.ITEMS_PICKAXES.isTagged(hand.getType())) {
                String uuids = interaction.getPersistentDataContainer().get(plugin.getUnaryKey(), PersistentDataType.STRING);
                if (uuids != null) {
                    // remove the console
                    ItemStack console = new ConsoleDestroyer(plugin).returnStack(uuids, id);
                    // return a console block of the correct colour
                    if (console != null) {
                        interaction.getWorld().dropItemNaturally(interaction.getLocation(), console);
                    }
                }
            } else {
                // open control menu
                new ControlMenuAction(plugin).openGUI(player, id);
            }
        }
    }

    private TextDisplay getTextDisplay(Location location, boolean coords, int id) {
        TextDisplay textDisplay = null;
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1.0d, 1.0d, 1.0d, (e) -> e.getType() == EntityType.TEXT_DISPLAY)) {
            textDisplay = (TextDisplay) entity;
            break;
        }
        if (textDisplay == null) {
            Location adjusted = location.clone();
            // from middle of interaction!
            Vector vector = coords ? new Vector(-0.05d, 0.5d, -0.4d) : new Vector(-0.25d, 0.5d, 0.125d);
            adjusted.add(vector);
            plugin.setTardisSpawn(true);
            textDisplay = (TextDisplay) location.getWorld().spawnEntity(adjusted, EntityType.TEXT_DISPLAY);
            textDisplay.getPersistentDataContainer().set(plugin.getInteractionUuidKey(), PersistentDataType.BOOLEAN, true);
            // insert record into database
            HashMap<String, Object> data = new HashMap<>();
            data.put("tardis_id", id);
            data.put("uuid", textDisplay.getUniqueId().toString());
            data.put("control", "SCREEN");
            data.put("state", 0);
            plugin.getQueryFactory().doInsert("interactions", data);
        }
        return textDisplay;
    }
}
