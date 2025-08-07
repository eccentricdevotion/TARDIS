/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIPlayerPreferences;
import me.eccentric_nz.TARDIS.custommodels.GUIWeather;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
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
        ItemStack fli = ItemStack.of(Material.ELYTRA, 1);
        ItemMeta ght_im = fli.getItemMeta();
        ght_im.displayName(Component.text("Flight Mode"));
        String mode_value = FlightMode.getByMode().get(rsp.getFlightMode()).toString();
        ght_im.lore(List.of(Component.text(mode_value)));
        fli.setItemMeta(ght_im);
        stack[GUIPlayerPreferences.FLIGHT_MODE.getSlot()] = fli;
        // interior hum sound
        ItemStack hum = ItemStack.of(Material.BOWL, 1);
        ItemMeta hum_im = hum.getItemMeta();
        hum_im.displayName(Component.text("Interior Hum Sound"));
        String hum_value = (rsp.getHum().isEmpty()) ? "random" : rsp.getHum();
        hum_im.lore(List.of(Component.text(hum_value)));
        hum.setItemMeta(hum_im);
        stack[GUIPlayerPreferences.INTERIOR_HUM_SOUND.getSlot()] = hum;
        // handbrake
        Tardis tardis = null;
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid.toString());
        ResultSetTardis rst = new ResultSetTardis(plugin, wherep, "", false);
        if (rst.resultSet()) {
            tardis = rst.getTardis();
        }
        ItemStack hand = ItemStack.of(Material.LEVER, 1);
        ItemMeta brake = hand.getItemMeta();
        brake.displayName(Component.text("Handbrake"));
        brake.lore(List.of(Component.text(
                (tardis != null && tardis.isHandbrakeOn())
                        ? plugin.getLanguage().getString("SET_ON", "ON")
                        : plugin.getLanguage().getString("SET_OFF", "OFF"))
        ));
        hand.setItemMeta(brake);
        stack[GUIPlayerPreferences.HANDBRAKE.getSlot()] = hand;
        // map
        ItemStack tt = ItemStack.of(Material.MAP, 1);
        ItemMeta map = tt.getItemMeta();
        map.displayName(Component.text("TARDIS Map"));
        tt.setItemMeta(map);
        stack[GUIPlayerPreferences.TARDIS_MAP.getSlot()] = tt;
        if (TARDISPermission.hasPermission(player, "tardis.autonomous")) {
            // autonomous preferences
            ItemStack auto = ItemStack.of(Material.BOWL, 1);
            ItemMeta prefs = auto.getItemMeta();
            prefs.displayName(Component.text("Autonomous Preferences"));
            auto.setItemMeta(prefs);
            stack[GUIPlayerPreferences.AUTONOMOUS_PREFERENCES.getSlot()] = auto;
        }
        if (TARDISPermission.hasPermission(player, "tardis.farm")) {
            // farming preferences
            ItemStack farm = ItemStack.of(Material.BOWL, 1);
            ItemMeta ing = farm.getItemMeta();
            ing.displayName(Component.text("Farming Preferences"));
            farm.setItemMeta(ing);
            stack[GUIPlayerPreferences.FARMING_PREFERENCES.getSlot()] = farm;
        }
        // sonic configurator
        ItemStack sonic = ItemStack.of(Material.BOWL, 1);
        ItemMeta config = sonic.getItemMeta();
        config.displayName(Component.text("Sonic Configurator"));
        sonic.setItemMeta(config);
        stack[GUIPlayerPreferences.SONIC_CONFIGURATOR.getSlot()] = sonic;
        if (TARDISPermission.hasPermission(player, "tardis.particles")) {
            // particle preferences
            ItemStack part = ItemStack.of(Material.BOWL, 1);
            ItemMeta icles = part.getItemMeta();
            icles.displayName(Component.text("Materialisation Particles"));
            part.setItemMeta(icles);
            stack[GUIPlayerPreferences.PARTICLES.getSlot()] = part;
        }
        // general
        ItemStack gen = ItemStack.of(Material.NETHER_STAR, 1);
        ItemMeta eral = gen.getItemMeta();
        eral.displayName(Component.text("General Preferences Menu"));
        gen.setItemMeta(eral);
        stack[GUIPlayerPreferences.GENERAL_PREFERENCES_MENU.getSlot()] = gen;
        if (TARDISPermission.hasPermission(player, "tardis.admin")) {
            // admin
            ItemStack ad = ItemStack.of(Material.NETHER_STAR, 1);
            ItemMeta min = ad.getItemMeta();
            min.displayName(Component.text("Admin Config Menu"));
            ad.setItemMeta(min);
            stack[GUIPlayerPreferences.ADMIN_MENU.getSlot()] = ad;
        }
        // close
        ItemStack close = ItemStack.of(GUIWeather.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);
        stack[18] = close;
        return stack;
    }
}
