package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.ColourType;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.keys.ConsolePart;
import me.eccentric_nz.TARDIS.custommodels.keys.ConsoleVariant;
import me.eccentric_nz.TARDIS.database.ClearInteractions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConsoleLabel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInteractionsFromId;
import me.eccentric_nz.TARDIS.sonic.SonicLore;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ConsoleDestroyer {

    private final TARDIS plugin;

    public ConsoleDestroyer(TARDIS plugin) {
        this.plugin = plugin;
    }

    public ItemStack returnStack(String uuids, int id) {
        String colour = "";
        NamespacedKey model = null;
        ResultSetInteractionsFromId rs = new ResultSetInteractionsFromId(plugin, id);
        if (rs.resultSet()) {
            // interactions
            for (UUID u : rs.getUuids()) {
                Entity e = plugin.getServer().getEntity(u);
                if (e instanceof Interaction interaction) {
                    // check for sonic
                    ItemDisplay docked = TARDISDisplayItemUtils.getSonic(interaction);
                    if (docked != null) {
                        ItemStack sonic = docked.getItemStack();
                        // set the charge level in lore
                        SonicLore.setChargeLevel(sonic);
                        e.getWorld().dropItemNaturally(e.getLocation(), sonic);
                        docked.remove();
                    }
                    // item displays
                    UUID uuid = e.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
                    if (uuid != null) {
                        ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
                        if (display != null) {
                            display.remove();
                        }
                    }
                    // remove
                    e.remove();
                }
                if (e instanceof TextDisplay display) {
                    display.remove();
                }
            }
            // remove the centre block
            ResultSetConsoleLabel rsc = new ResultSetConsoleLabel(plugin, id);
            if (rsc.resultSet()) {
                Block centre = rsc.getLocation().getBlock();
                centre.setType(Material.AIR);
                removeTextDisplays(rsc.getLocation());
            }
            String[] split = uuids.split("~");
            for (String u : split) {
                try {
                    UUID uuid = UUID.fromString(u);
                    Entity e = plugin.getServer().getEntity(uuid);
                    if (e instanceof ItemDisplay display) {
                        // get colour
                        ItemStack is = display.getItemStack();
                        if (colour.isEmpty() && is != null && is.hasItemMeta()) {
                            ItemMeta im = is.getItemMeta();
                            model = im.getItemModel();
                            if (model == null && im.hasCustomModelData()) {
                                switch (im.getCustomModelData()) {
                                    case 1017, 2017 -> model = ConsolePart.CONSOLE_RUSTIC.getKey();
                                    case 1016, 2016 -> model = ConsolePart.CONSOLE_BROWN.getKey();
                                    case 1015, 2015 -> model = ConsolePart.CONSOLE_PINK.getKey();
                                    case 1014, 2014 -> model = ConsolePart.CONSOLE_MAGENTA.getKey();
                                    case 1013, 2013 -> model = ConsolePart.CONSOLE_PURPLE.getKey();
                                    case 1012, 2012 -> model = ConsolePart.CONSOLE_BLUE.getKey();
                                    case 1011, 2011 -> model = ConsolePart.CONSOLE_LIGHT_BLUE.getKey();
                                    case 1010, 2010 -> model = ConsolePart.CONSOLE_CYAN.getKey();
                                    case 1009, 2009 -> model = ConsolePart.CONSOLE_GREEN.getKey();
                                    case 1008, 2008 -> model = ConsolePart.CONSOLE_LIME.getKey();
                                    case 1007, 2007 -> model = ConsolePart.CONSOLE_YELLOW.getKey();
                                    case 1006, 2006 -> model = ConsolePart.CONSOLE_ORANGE.getKey();
                                    case 1005, 2005 -> model = ConsolePart.CONSOLE_RED.getKey();
                                    case 1004, 2004 -> model = ConsolePart.CONSOLE_WHITE.getKey();
                                    case 1003, 2003 -> model = ConsolePart.CONSOLE_BLACK.getKey();
                                    case 1002, 2002 -> model = ConsolePart.CONSOLE_GRAY.getKey();
                                    default -> model = ConsolePart.CONSOLE_LIGHT_GRAY.getKey();
                                }
                            }
                            colour = ColourType.COLOURS.getOrDefault(model, "LIGHT_GRAY");
                        }
                        // remove
                        display.remove();
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
            // remove database records
            new ClearInteractions(plugin).removeRecords(id);
            // build item stack
            boolean isRustic = model == ConsolePart.CONSOLE_RUSTIC.getKey();
            Material material = (isRustic) ? Material.WAXED_OXIDIZED_COPPER : Material.valueOf(colour + "_CONCRETE");
            NamespacedKey key;
            switch (material) {
                case WAXED_OXIDIZED_COPPER -> key = ConsoleVariant.CONSOLE_RUSTIC.getKey();
                case BROWN_CONCRETE -> key = ConsoleVariant.CONSOLE_BROWN.getKey();
                case PINK_CONCRETE -> key = ConsoleVariant.CONSOLE_PINK.getKey();
                case MAGENTA_CONCRETE -> key = ConsoleVariant.CONSOLE_MAGENTA.getKey();
                case PURPLE_CONCRETE -> key = ConsoleVariant.CONSOLE_PURPLE.getKey();
                case BLUE_CONCRETE -> key = ConsoleVariant.CONSOLE_BLUE.getKey();
                case LIGHT_BLUE_CONCRETE -> key = ConsoleVariant.CONSOLE_LIGHT_BLUE.getKey();
                case CYAN_CONCRETE -> key = ConsoleVariant.CONSOLE_CYAN.getKey();
                case GREEN_CONCRETE -> key = ConsoleVariant.CONSOLE_GREEN.getKey();
                case LIME_CONCRETE -> key = ConsoleVariant.CONSOLE_LIME.getKey();
                case YELLOW_CONCRETE -> key = ConsoleVariant.CONSOLE_YELLOW.getKey();
                case ORANGE_CONCRETE -> key = ConsoleVariant.CONSOLE_ORANGE.getKey();
                case RED_CONCRETE -> key = ConsoleVariant.CONSOLE_RED.getKey();
                case WHITE_CONCRETE -> key = ConsoleVariant.CONSOLE_WHITE.getKey();
                case BLACK_CONCRETE -> key = ConsoleVariant.CONSOLE_BLACK.getKey();
                case GRAY_CONCRETE -> key = ConsoleVariant.CONSOLE_GRAY.getKey();
                default -> key = ConsoleVariant.CONSOLE_LIGHT_GRAY.getKey();
            }
            ItemStack console = new ItemStack(material, 1);
            ItemMeta im = console.getItemMeta();
            String dn = ((isRustic) ? "Rustic" : TARDISStringUtils.capitalise(colour)) + " Console";
            im.setDisplayName(dn);
            im.setLore(List.of("Integration with interaction"));
            im.setItemModel(key);
            String which = model.getKey()
                    .replace("tardis/console_division_", "")
                    .replace("tardis/console_centre_", "")
                    .replace("tardis/console_", "");
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, which.toUpperCase(Locale.ROOT));
            console.setItemMeta(im);
            return console;
        }
        return null;
    }

    private void removeTextDisplays(Location centre) {
        Location spawn = centre.clone().add(0.5f, 0, 0.5f);
        for (Entity e : spawn.getWorld().getNearbyEntities(spawn, 4, 3, 4, (t) -> t.getType() == EntityType.TEXT_DISPLAY)) {
            if (e instanceof TextDisplay) {
                e.remove();
            }
        }
    }
}
