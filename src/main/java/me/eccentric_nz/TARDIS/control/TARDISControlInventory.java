/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISControlInventory {

    private final TARDIS plugin;
    private final UUID uuid;
    private final ItemStack[] controls;

    public TARDISControlInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.controls = getItemStack();
    }

    /**
     * Constructs an inventory for the Temporal Locator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        // get player prefs
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        String lights_onoff = "";
        String siege_onoff = "";
        String toggle_openclosed = "";
        String power_onoff = "";
        String direction = "";
        String off = plugin.getLanguage().getString("SET_OFF");
        String on = plugin.getLanguage().getString("SET_ON");
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            siege_onoff = (tardis.isSiege_on()) ? on : off;
            lights_onoff = (tardis.isLights_on()) ? on : off;
            boolean open = new TARDISBlackWoolToggler(plugin).isOpen(tardis.getTardis_id());
            toggle_openclosed = (open) ? plugin.getLanguage().getString("SET_OPEN") : plugin.getLanguage().getString("SET_CLOSED");
            power_onoff = (tardis.isPowered_on()) ? on : off;
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("tardis_id", tardis.getTardis_id());
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wheret);
            if (rsc.resultSet()) {
                direction = rsc.getDirection().toString();
            }
        }
        /**
         * ***** TRAVEL *****
         */
        // random location
        ItemStack ran = new ItemStack(Material.BOWL, 1);
        ItemMeta dom = ran.getItemMeta();
        dom.setDisplayName(ChatColor.MAGIC + "ran" + ChatColor.RESET + plugin.getLanguage().getString("BUTTON_RANDOM") + ChatColor.MAGIC + "dom");
        ran.setItemMeta(dom);
        // Saves
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta locs = save.getItemMeta();
        locs.setDisplayName(plugin.getLanguage().getString("BUTTON_SAVES"));
        save.setItemMeta(locs);
        // back
        ItemStack fast = new ItemStack(Material.BOWL, 1);
        ItemMeta ret = fast.getItemMeta();
        ret.setDisplayName(plugin.getLanguage().getString("BUTTON_BACK"));
        fast.setItemMeta(ret);
        // areas
        ItemStack area = new ItemStack(Material.BOWL, 1);
        ItemMeta tar = area.getItemMeta();
        tar.setDisplayName(plugin.getLanguage().getString("BUTTON_AREAS"));
        area.setItemMeta(tar);
        // destination terminal
        ItemStack ter = new ItemStack(Material.BOWL, 1);
        ItemMeta min = ter.getItemMeta();
        min.setDisplayName(plugin.getLanguage().getString("BUTTON_TERM"));
        ter.setItemMeta(min);
        /**
         * ***** INTERIOR *****
         */
        // architectural reconfiguration system
        ItemStack ars = new ItemStack(Material.BOWL, 1);
        ItemMeta but = ars.getItemMeta();
        but.setDisplayName(plugin.getLanguage().getString("BUTTON_ARS"));
        ars.setItemMeta(but);
        // desktop theme
        ItemStack upg = new ItemStack(Material.BOWL, 1);
        ItemMeta rade = upg.getItemMeta();
        rade.setDisplayName(plugin.getLanguage().getString("BUTTON_THEME"));
        upg.setItemMeta(rade);
        // power up/down
        ItemStack pow = new ItemStack(Material.DIODE, 1);
        ItemMeta dwn = pow.getItemMeta();
        dwn.setDisplayName(plugin.getLanguage().getString("BUTTON_POWER"));
        dwn.setLore(Arrays.asList(power_onoff));
        pow.setItemMeta(dwn);
        // light
        ItemStack lig = new ItemStack(Material.DIODE, 1);
        ItemMeta swi = lig.getItemMeta();
        swi.setDisplayName(plugin.getLanguage().getString("BUTTON_LIGHTS"));
        swi.setLore(Arrays.asList(lights_onoff));
        lig.setItemMeta(swi);
        // toggle wool
        ItemStack tog = new ItemStack(Material.DIODE, 1);
        ItemMeta gle = tog.getItemMeta();
        gle.setDisplayName(plugin.getLanguage().getString("BUTTON_TOGGLE"));
        gle.setLore(Arrays.asList(toggle_openclosed));
        tog.setItemMeta(gle);
        // tardis map
        ItemStack map = new ItemStack(Material.BOWL, 1);
        ItemMeta me = map.getItemMeta();
        me.setDisplayName(plugin.getLanguage().getString("BUTTON_TARDIS_MAP"));
        map.setItemMeta(me);
        /**
         * ***** EXTERIOR *****
         */
        // chameleon circuit
        ItemStack cham = new ItemStack(Material.BOWL, 1);
        ItemMeta eleon = cham.getItemMeta();
        eleon.setDisplayName(plugin.getLanguage().getString("BUTTON_CHAMELEON"));
        cham.setItemMeta(eleon);
        // siege
        ItemStack siege = new ItemStack(Material.DIODE, 1);
        ItemMeta mode = siege.getItemMeta();
        mode.setDisplayName(plugin.getLanguage().getString("BUTTON_SIEGE"));
        mode.setLore(Arrays.asList(siege_onoff));
        siege.setItemMeta(mode);
        // hide
        ItemStack hide = new ItemStack(Material.BOWL, 1);
        ItemMeta box = hide.getItemMeta();
        box.setDisplayName(plugin.getLanguage().getString("BUTTON_HIDE"));
        hide.setItemMeta(box);
        // rebuild
        ItemStack reb = new ItemStack(Material.BOWL, 1);
        ItemMeta uild = reb.getItemMeta();
        uild.setDisplayName(plugin.getLanguage().getString("BUTTON_REBUILD"));
        reb.setItemMeta(uild);
        // direction
        ItemStack dir = new ItemStack(Material.BOWL, 1);
        ItemMeta ection = dir.getItemMeta();
        ection.setDisplayName(plugin.getLanguage().getString("BUTTON_DIRECTION"));
        ection.setLore(Arrays.asList(direction));
        dir.setItemMeta(ection);
        // temporal
        ItemStack temp = new ItemStack(Material.BOWL, 1);
        ItemMeta oral = temp.getItemMeta();
        oral.setDisplayName(plugin.getLanguage().getString("BUTTON_TEMP"));
        temp.setItemMeta(oral);
        /**
         * ***** INFORMATION *****
         */
        // artron levels
        ItemStack art = new ItemStack(Material.BOWL, 1);
        ItemMeta ron = art.getItemMeta();
        ron.setDisplayName(plugin.getLanguage().getString("BUTTON_ARTRON"));
        art.setItemMeta(ron);
        // scanner
        ItemStack scan = new ItemStack(Material.BOWL, 1);
        ItemMeta ner = scan.getItemMeta();
        ner.setDisplayName(plugin.getLanguage().getString("BUTTON_SCANNER"));
        scan.setItemMeta(ner);
        // TIS
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta sys = info.getItemMeta();
        sys.setDisplayName(plugin.getLanguage().getString("BUTTON_INFO"));
        info.setItemMeta(sys);
        /**
         * ***** OTHER *****
         */
        // zero room
        ItemStack zero = new ItemStack(Material.BOWL, 1);
        ItemMeta room = zero.getItemMeta();
        room.setDisplayName(plugin.getLanguage().getString("BUTTON_ZERO"));
        zero.setItemMeta(room);
        // player prefs
        ItemStack player = new ItemStack(Material.BOWL, 1);
        ItemMeta prefs = player.getItemMeta();
        prefs.setDisplayName(plugin.getLanguage().getString("BUTTON_PREFS"));
        player.setItemMeta(prefs);
        // companion menu
        ItemStack companion = new ItemStack(Material.BOWL, 1);
        ItemMeta list = companion.getItemMeta();
        list.setDisplayName(plugin.getLanguage().getString("COMPANIONS_MENU"));
        companion.setItemMeta(list);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(can);

        ItemStack[] is = {
            ran, null, ars, null, cham, null, art, null, zero,
            save, null, upg, null, siege, null, scan, null, player,
            fast, null, pow, null, hide, null, info, null, companion,
            area, null, lig, null, reb, null, null, null, null,
            ter, null, tog, null, dir, null, null, null, null,
            null, null, map, null, temp, null, null, null, close
        };
        return is;
    }

    public ItemStack[] getControls() {
        return controls;
    }
}
