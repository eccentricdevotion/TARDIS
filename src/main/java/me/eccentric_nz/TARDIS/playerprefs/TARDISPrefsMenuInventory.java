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
package me.eccentric_nz.TARDIS.playerprefs;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUIPlayerPreferences;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * The Administrator of Solos is the Earth Empire's civilian overseer for that planet.
 *
 * @author eccentric_nz
 */
public class TARDISPrefsMenuInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Inventory inventory;

    public TARDISPrefsMenuInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("Player Prefs Menu", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */

    private ItemStack[] getItemStack() {
        // get player preferences
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        if (!rsp.resultSet()) {
            // make a new record
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("player_prefs", set);
            // get the new record
            rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            rsp.resultSet();
        }
        Player player = plugin.getServer().getPlayer(uuid);
        ItemStack[] stack = new ItemStack[27];
        // flight mode
        ItemStack flight = ItemStack.of(Material.ELYTRA, 1);
        flight.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Flight Mode"));
        String mode_value = FlightMode.getByMode().get(rsp.getFlightMode()).toString();
        flight.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(mode_value)).build());
        stack[GUIPlayerPreferences.FLIGHT_MODE.getSlot()] = flight;
        // interior hum sound
        ItemStack hum = ItemStack.of(Material.BOWL, 1);
        hum.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Interior Hum Sound"));
        String hum_value = (rsp.getHum().isEmpty()) ? "random" : rsp.getHum();
        hum.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(hum_value)).build());
        stack[GUIPlayerPreferences.INTERIOR_HUM_SOUND.getSlot()] = hum;
        // handbrake
        Tardis tardis = null;
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid.toString());
        ResultSetTardis rst = new ResultSetTardis(plugin, wherep, "", false);
        if (rst.resultSet()) {
            tardis = rst.getTardis();
        }
        ItemStack handbrake = ItemStack.of(Material.LEVER, 1);
        handbrake.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Handbrake"));
        handbrake.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(
                (tardis != null && tardis.isHandbrakeOn())
                        ? plugin.getLanguage().getString("SET_ON", "ON")
                        : plugin.getLanguage().getString("SET_OFF", "OFF"))
        ).build());
        stack[GUIPlayerPreferences.HANDBRAKE.getSlot()] = handbrake;
        // map
        ItemStack map = ItemStack.of(Material.MAP, 1);
        map.setData(DataComponentTypes.CUSTOM_NAME, Component.text("TARDIS Map"));
        stack[GUIPlayerPreferences.TARDIS_MAP.getSlot()] = map;
        if (TARDISPermission.hasPermission(player, "tardis.autonomous")) {
            // autonomous preferences
            ItemStack auto = ItemStack.of(Material.BOWL, 1);
            auto.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Autonomous Preferences"));
            stack[GUIPlayerPreferences.AUTONOMOUS_PREFERENCES.getSlot()] = auto;
        }
        if (TARDISPermission.hasPermission(player, "tardis.farm")) {
            // farming preferences
            ItemStack farm = ItemStack.of(Material.BOWL, 1);
            farm.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Farming Preferences"));
            stack[GUIPlayerPreferences.FARMING_PREFERENCES.getSlot()] = farm;
        }
        // sonic configurator
        ItemStack sonic = ItemStack.of(Material.BOWL, 1);
        sonic.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Sonic Configurator"));
        stack[GUIPlayerPreferences.SONIC_CONFIGURATOR.getSlot()] = sonic;
        if (TARDISPermission.hasPermission(player, "tardis.particles")) {
            // particle preferences
            ItemStack particles = ItemStack.of(Material.BOWL, 1);
            particles.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Materialisation Particles"));
            stack[GUIPlayerPreferences.PARTICLES.getSlot()] = particles;
        }
        // general
        ItemStack general = ItemStack.of(Material.NETHER_STAR, 1);
        general.setData(DataComponentTypes.CUSTOM_NAME, Component.text("General Preferences Menu"));
        stack[GUIPlayerPreferences.GENERAL_PREFERENCES_MENU.getSlot()] = general;
        if (TARDISPermission.hasPermission(player, "tardis.admin")) {
            // admin
            ItemStack admin = ItemStack.of(Material.NETHER_STAR, 1);
            admin.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Admin Config Menu"));
            stack[GUIPlayerPreferences.ADMIN_MENU.getSlot()] = admin;
        }
        // close
        stack[18] = GUIItemFactory.close();
        return stack;
    }
}
