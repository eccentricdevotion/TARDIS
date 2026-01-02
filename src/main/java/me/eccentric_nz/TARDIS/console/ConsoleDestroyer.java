/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.ColourType;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.keys.ConsolePart;
import me.eccentric_nz.TARDIS.database.ClearInteractions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConsoleLabel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInteractionsFromId;
import me.eccentric_nz.TARDIS.sonic.SonicLore;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
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
                        if (colour.isEmpty() && is.hasItemMeta()) {
                            ItemMeta im = is.getItemMeta();
                            model = im.getItemModel();
                            if (model == null && im.hasCustomModelDataComponent()) {
                                float convertedCMD = im.getCustomModelDataComponent().getFloats().getFirst();
                                if (convertedCMD == 1017.0f || convertedCMD == 2017.0f) {
                                    model = ConsolePart.CONSOLE_RUSTIC.getKey();
                                } else if (convertedCMD == 1016.0f || convertedCMD == 2016.0f) {
                                    model = ConsolePart.CONSOLE_BROWN.getKey();
                                } else if (convertedCMD == 1015.0f || convertedCMD == 2015.0f) {
                                    model = ConsolePart.CONSOLE_PINK.getKey();
                                } else if (convertedCMD == 1014.0f || convertedCMD == 2014.0f) {
                                    model = ConsolePart.CONSOLE_MAGENTA.getKey();
                                } else if (convertedCMD == 1013.0f || convertedCMD == 2013.0f) {
                                    model = ConsolePart.CONSOLE_PURPLE.getKey();
                                } else if (convertedCMD == 1012.0f || convertedCMD == 2012.0f) {
                                    model = ConsolePart.CONSOLE_BLUE.getKey();
                                } else if (convertedCMD == 1011.0f || convertedCMD == 2011.0f) {
                                    model = ConsolePart.CONSOLE_LIGHT_BLUE.getKey();
                                } else if (convertedCMD == 1010.0f || convertedCMD == 2010.0f) {
                                    model = ConsolePart.CONSOLE_CYAN.getKey();
                                } else if (convertedCMD == 1009.0f || convertedCMD == 2009.0f) {
                                    model = ConsolePart.CONSOLE_GREEN.getKey();
                                } else if (convertedCMD == 1008.0f || convertedCMD == 2008.0f) {
                                    model = ConsolePart.CONSOLE_LIME.getKey();
                                } else if (convertedCMD == 1007.0f || convertedCMD == 2007.0f) {
                                    model = ConsolePart.CONSOLE_YELLOW.getKey();
                                } else if (convertedCMD == 1006.0f || convertedCMD == 2006.0f) {
                                    model = ConsolePart.CONSOLE_ORANGE.getKey();
                                } else if (convertedCMD == 1005.0f || convertedCMD == 2005.0f) {
                                    model = ConsolePart.CONSOLE_RED.getKey();
                                } else if (convertedCMD == 1004.0f || convertedCMD == 2004.0f) {
                                    model = ConsolePart.CONSOLE_WHITE.getKey();
                                } else if (convertedCMD == 1003.0f || convertedCMD == 2003.0f) {
                                    model = ConsolePart.CONSOLE_BLACK.getKey();
                                } else if (convertedCMD == 1002.0f || convertedCMD == 2002.0f) {
                                    model = ConsolePart.CONSOLE_GRAY.getKey();
                                } else {
                                    model = ConsolePart.CONSOLE_LIGHT_GRAY.getKey();
                                }
                            } else {
                                plugin.debug("found model -> " + model.getKey());
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
            boolean isRustic = model != null
                    && (model.equals(ConsolePart.CONSOLE_RUSTIC.getKey())
                    || model.equals(ConsolePart.CONSOLE_CENTRE_RUSTIC.getKey())
                    || model.equals(ConsolePart.CONSOLE_DIVISION_RUSTIC.getKey())
            );
            Material material = (isRustic) ? Material.WAXED_OXIDIZED_COPPER : Material.valueOf(colour + "_CONCRETE");
            ItemStack console = ItemStack.of(material, 1);
            ItemMeta im = console.getItemMeta();
            String dn = ((isRustic) ? "Rustic" : TARDISStringUtils.capitalise(colour)) + " Console";
            im.displayName(ComponentUtils.toWhite(dn));
            im.lore(List.of(Component.text("Integration with interaction")));
            String which = model.getKey()
                    .replace("division_", "")
                    .replace("centre_", "");
            plugin.debug(which);
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, which);
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
