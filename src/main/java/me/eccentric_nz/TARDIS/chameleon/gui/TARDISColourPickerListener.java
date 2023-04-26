package me.eccentric_nz.TARDIS.chameleon.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetColour;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.List;

public class TARDISColourPickerListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISColourPickerListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Colour Picker")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 18 && slot < 54) {
                switch (slot) {
                    case 20 -> {
                        // red less
                        less(view, 10, 0, 0);
                        setRed(view);
                    }
                    case 24 -> {
                        // red more
                        more(view, 10, 0, 0);
                        setRed(view);
                    }
                    case 29 -> {
                        // green less
                        less(view, 0, 10, 0);
                        setGreen(view);
                    }
                    case 33 -> {
                        // green more
                        more(view, 0, 10, 0);
                        setGreen(view);
                    }
                    case 38 -> {
                        // blue less
                        less(view, 0, 0, 10);
                        setBlue(view);
                    }
                    case 42 -> {
                        // blue more
                        more(view, 0, 0, 10);
                        setBlue(view);
                    }
                    case 35 -> {
                        // get the player's tardis_id
                        ResultSetTardisID rst = new ResultSetTardisID(plugin);
                        if (rst.fromUUID(player.getUniqueId().toString())) {
                            int id = rst.getTardis_id();
                            // save the colour
                            Color color = getColour(view);
                            HashMap<String, Object> set = new HashMap<>();
                            set.put("red", color.getRed());
                            set.put("green", color.getGreen());
                            set.put("blue", color.getBlue());
                            // do they have an existing record?
                            ResultSetColour rsc = new ResultSetColour(plugin, id);
                            if (rsc.resultSet()) {
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("tardis_id", id);
                                plugin.getQueryFactory().doUpdate("colour", set, where);
                            } else {
                                set.put("tardis_id", id);
                                plugin.getQueryFactory().doInsert("colour", set);
                            }
                            TARDISMessage.send(player, "COLOUR_SET");
                        }
                        close(player);
                    }
                    case 53 -> {
                        // close
                        close(player);
                    }
                    default -> {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private void less(InventoryView view, int r, int g, int b) {
        ItemStack display = view.getItem(4);
        LeatherArmorMeta meta = (LeatherArmorMeta) display.getItemMeta();
        Color color = meta.getColor();
        int rr = color.getRed() - r;
        if (rr < 0) {
            rr = 0;
        }
        int gg = color.getGreen() - g;
        if (gg < 0) {
            gg = 0;
        }
        int bb = color.getBlue() - b;
        if (bb < 0) {
            bb = 0;
        }
        List<String> lore = meta.getLore();
        lore.set(0, "Red: " + rr);
        lore.set(1, "Green: " + gg);
        lore.set(2, "Blue: " + bb);
        meta.setLore(lore);
        meta.setColor(Color.fromRGB(rr, gg, bb));
        display.setItemMeta(meta);
    }

    private void more(InventoryView view, int r, int g, int b) {
        ItemStack display = view.getItem(4);
        LeatherArmorMeta meta = (LeatherArmorMeta) display.getItemMeta();
        Color color = meta.getColor();
        int rr = color.getRed() + r;
        if (rr > 255) {
            rr = 255;
        }
        int gg = color.getGreen() + g;
        if (gg > 255) {
            gg = 255;
        }
        int bb = color.getBlue() + b;
        if (bb > 255) {
            bb = 255;
        }
        List<String> lore = meta.getLore();
        lore.set(0, "Red: " + rr);
        lore.set(1, "Green: " + gg);
        lore.set(2, "Blue: " + bb);
        meta.setLore(lore);
        meta.setColor(Color.fromRGB(rr, gg, bb));
        display.setItemMeta(meta);
    }

    private void setRed(InventoryView view) {
        Color color = getColour(view);
        ItemStack red = view.getItem(22);
        LeatherArmorMeta meta = (LeatherArmorMeta) red.getItemMeta();
        meta.setColor(Color.fromRGB(color.getRed(), 0, 0));
        red.setItemMeta(meta);
    }

    private void setGreen(InventoryView view) {
        Color color = getColour(view);
        ItemStack green = view.getItem(31);
        LeatherArmorMeta meta = (LeatherArmorMeta) green.getItemMeta();
        meta.setColor(Color.fromRGB(0, color.getGreen(), 0));
        green.setItemMeta(meta);
    }

    private void setBlue(InventoryView view) {
        Color color = getColour(view);
        ItemStack blue = view.getItem(40);
        LeatherArmorMeta meta = (LeatherArmorMeta) blue.getItemMeta();
        meta.setColor(Color.fromRGB(0, 0, color.getBlue()));
        blue.setItemMeta(meta);
    }

    private Color getColour(InventoryView view) {
        ItemStack display = view.getItem(4);
        LeatherArmorMeta meta = (LeatherArmorMeta) display.getItemMeta();
        return meta.getColor();
    }
}
