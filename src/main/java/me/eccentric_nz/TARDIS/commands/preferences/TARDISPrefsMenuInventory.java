/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIPlayerPreferences;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import me.eccentric_nz.TARDIS.enumeration.HADS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * The Administrator of Solos is the Earth Empire's civilian overseer for that planet.
 *
 * @author eccentric_nz
 */
public class TARDISPrefsMenuInventory {

    private final TARDIS plugin;
    private final UUID uuid;
    private final ItemStack[] menu;

    public TARDISPrefsMenuInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */

    private ItemStack[] getItemStack() {
        // get player prefs
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        List<Boolean> values = new ArrayList<>();
        if (!rsp.resultSet()) {
            // make a new record
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("player_prefs", set);
            // get the new record
            rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            rsp.resultSet();
        }
        values.add(rsp.isAutoOn());
        values.add(rsp.isAutoSiegeOn());
        values.add(rsp.isAutoRescueOn());
        values.add(rsp.isBeaconOn());
        values.add(rsp.isCloseGUIOn());
        values.add(rsp.isDND());
        values.add(rsp.isEpsOn());
        values.add(rsp.isHadsOn());
        values.add(rsp.getHadsType().equals(HADS.DISPERSAL));
        values.add(rsp.isQuotesOn());
        values.add(rsp.isRendererOn());
        values.add(rsp.isSfxOn());
        values.add(rsp.isSubmarineOn());
        values.add(rsp.isTextureOn());
        values.add(rsp.isBuildOn());
        values.add(rsp.isWoolLightsOn());
        values.add(rsp.isSignOn());
        values.add(rsp.isTravelbarOn());
        values.add(rsp.isFarmOn());
        values.add(rsp.isTelepathyOn());
        // get TARDIS preset
        Tardis tardis = null;
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid.toString());
        ResultSetTardis rst = new ResultSetTardis(plugin, wherep, "", false, 0);
        boolean hasTARDIS = rst.resultSet();
        if (hasTARDIS) {
            tardis = rst.getTardis();
            values.add(rst.getTardis().getPreset().equals(PRESET.JUNK_MODE)); // junk mode
        } else {
            values.add(false);
        }
        values.add(rsp.isAutoPowerUp());
        values.add(plugin.getTrackerKeeper().getActiveForceFields().containsKey(uuid));
        values.add(rsp.isLanternsOn());
        values.add(rsp.isMinecartOn());
        values.add(rsp.isEasyDifficulty());
        if (plugin.isWorldGuardOnServer()) {
            String chunk = rst.getTardis().getChunk();
            String[] split = chunk.split(":");
            World world = plugin.getServer().getWorld(split[0]);
            values.add(!plugin.getWorldGuardUtils().queryContainers(world, plugin.getServer().getPlayer(uuid).getName()));
        } else {
            values.add(false);
        }
        // make a stack
        ItemStack[] stack = new ItemStack[36];
        for (GUIPlayerPreferences pref : GUIPlayerPreferences.values()) {
            if (pref.getMaterial() == Material.REPEATER) {
                ItemStack is = new ItemStack(pref.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(pref.getName());
                int cmd = pref.getCustomModelData();
                boolean v;
                if (pref == GUIPlayerPreferences.JUNK_TARDIS) {
                    v = (tardis != null && tardis.getPreset().equals(PRESET.JUNK_MODE));
                } else {
                    v = values.get(pref.getSlot());
                }
                im.setCustomModelData(v ? cmd : cmd + 100);
                if (pref == GUIPlayerPreferences.HADS_TYPE) {
                    im.setLore(Collections.singletonList(v ? "DISPERSAL" : "DISPLACEMENT"));
                } else {
                    im.setLore(Collections.singletonList(v ? plugin.getLanguage().getString("SET_ON") : plugin.getLanguage().getString("SET_OFF")));
                }
                is.setItemMeta(im);
                stack[pref.getSlot()] = is;
            }
        }
        if (!plugin.isWorldGuardOnServer()) {
            stack[GUIPlayerPreferences.LOCK_CONTAINERS.getSlot()] = null;
        }
        // flight mode
        ItemStack fli = new ItemStack(Material.ELYTRA, 1);
        ItemMeta ght_im = fli.getItemMeta();
        ght_im.setDisplayName("Flight Mode");
        String mode_value = FlightMode.getByMode().get(rsp.getFlightMode()).toString();
        ght_im.setLore(Collections.singletonList(mode_value));
        ght_im.setCustomModelData(GUIPlayerPreferences.FLIGHT_MODE.getCustomModelData());
        fli.setItemMeta(ght_im);
        stack[GUIPlayerPreferences.FLIGHT_MODE.getSlot()] = fli;
        // interior hum sound
        ItemStack hum = new ItemStack(Material.BOWL, 1);
        ItemMeta hum_im = hum.getItemMeta();
        hum_im.setDisplayName("Interior Hum Sound");
        String hum_value = (rsp.getHum().isEmpty()) ? "random" : rsp.getHum();
        hum_im.setLore(Collections.singletonList(hum_value));
        hum_im.setCustomModelData(GUIPlayerPreferences.INTERIOR_HUM_SOUND.getCustomModelData());
        hum.setItemMeta(hum_im);
        stack[GUIPlayerPreferences.INTERIOR_HUM_SOUND.getSlot()] = hum;
        // handbrake
        ItemStack hand = new ItemStack(Material.LEVER, 1);
        ItemMeta brake = hand.getItemMeta();
        brake.setDisplayName("Handbrake");
        brake.setLore(Collections.singletonList((tardis != null && tardis.isHandbrake_on()) ? plugin.getLanguage().getString("SET_ON") : plugin.getLanguage().getString("SET_OFF")));
        brake.setCustomModelData(GUIPlayerPreferences.HANDBRAKE.getCustomModelData());
        hand.setItemMeta(brake);
        stack[GUIPlayerPreferences.HANDBRAKE.getSlot()] = hand;
        // map
        ItemStack tt = new ItemStack(Material.MAP, 1);
        ItemMeta map = tt.getItemMeta();
        map.setDisplayName("TARDIS Map");
        map.setCustomModelData(GUIPlayerPreferences.TARDIS_MAP.getCustomModelData());
        tt.setItemMeta(map);
        stack[GUIPlayerPreferences.TARDIS_MAP.getSlot()] = tt;
        // autonomous prefs
        ItemStack auto = new ItemStack(Material.BOWL, 1);
        ItemMeta prefs = auto.getItemMeta();
        prefs.setDisplayName("Autonomous Preferences");
        prefs.setCustomModelData(GUIPlayerPreferences.AUTONOMOUS_PREFERENCES.getCustomModelData());
        auto.setItemMeta(prefs);
        stack[GUIPlayerPreferences.AUTONOMOUS_PREFERENCES.getSlot()] = auto;
        // sonic configurator
        ItemStack sonic = new ItemStack(Material.BOWL, 1);
        ItemMeta config = sonic.getItemMeta();
        config.setDisplayName("Sonic Configurator");
        config.setCustomModelData(GUIPlayerPreferences.SONIC_CONFIGURATOR.getCustomModelData());
        sonic.setItemMeta(config);
        stack[GUIPlayerPreferences.SONIC_CONFIGURATOR.getSlot()] = sonic;
        if (plugin.getServer().getPlayer(uuid).hasPermission("tardis.admin")) {
            // admin
            ItemStack ad = new ItemStack(Material.NETHER_STAR, 1);
            ItemMeta min = ad.getItemMeta();
            min.setDisplayName("Admin Config Menu");
            min.setCustomModelData(GUIPlayerPreferences.ADMIN_MENU.getCustomModelData());
            ad.setItemMeta(min);
            stack[GUIPlayerPreferences.ADMIN_MENU.getSlot()] = ad;
        }
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
