/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIControlCentre;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISControlInventory {

    private final TARDIS plugin;
    private final int id;
    private final ItemStack[] controls;

    public TARDISControlInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        controls = getItemStack();
    }

    /**
     * Constructs an inventory for the Control Centre GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        // get tardis options
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        String lights_onoff = "";
        String siege_onoff = "";
        String toggle_openclosed = "";
        String power_onoff = "";
        String direction = "";
        String off = plugin.getLanguage().getString("SET_OFF");
        String on = plugin.getLanguage().getString("SET_ON");
        int delay = 1;
        boolean open = false;
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            siege_onoff = (tardis.isSiegeOn()) ? on : off;
            lights_onoff = (tardis.isLightsOn()) ? on : off;
            open = new TARDISBlackWoolToggler(plugin).isOpen(tardis.getTardisId());
            toggle_openclosed = (open) ? plugin.getLanguage().getString("SET_OPEN") : plugin.getLanguage().getString("SET_CLOSED");
            power_onoff = (tardis.isPoweredOn()) ? on : off;
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, tardis.getTardisId());
            if (rsc.resultSet()) {
                direction = rsc.getDirection().toString();
            }
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, rs.getTardis().getUuid().toString());
            if (rsp.resultSet()) {
                delay = rsp.getThrottle();
            }
        }
        /*
         * ***** TRAVEL *****
         */
        // random location
        ItemStack ran = new ItemStack(GUIControlCentre.BUTTON_RANDOM.material(), 1);
        ItemMeta dom = ran.getItemMeta();
        dom.setDisplayName(ChatColor.MAGIC + "ran" + ChatColor.RESET + plugin.getLanguage().getString("BUTTON_RANDOM") + ChatColor.MAGIC + "dom");
        dom.setCustomModelData(GUIControlCentre.BUTTON_RANDOM.customModelData());
        ran.setItemMeta(dom);
        // Saves
        ItemStack save = new ItemStack(GUIControlCentre.BUTTON_SAVES.material(), 1);
        ItemMeta locs = save.getItemMeta();
        locs.setDisplayName(plugin.getLanguage().getString("BUTTON_SAVES"));
        locs.setLore(List.of("load saves from this TARDIS"));
        locs.setCustomModelData(GUIControlCentre.BUTTON_SAVES.customModelData());
        save.setItemMeta(locs);
        // Own saves if in another player's TARDIS
        ItemStack own = new ItemStack(GUIControlCentre.BUTTON_SAVES.material(), 1);
        ItemMeta saves = own.getItemMeta();
        saves.setDisplayName(plugin.getLanguage().getString("BUTTON_SAVES"));
        saves.setLore(List.of("Load my saves"));
        saves.setCustomModelData(GUIControlCentre.BUTTON_SAVES.customModelData());
        own.setItemMeta(saves);
        // back
        ItemStack fast = new ItemStack(GUIControlCentre.BUTTON_BACK.material(), 1);
        ItemMeta ret = fast.getItemMeta();
        ret.setDisplayName(plugin.getLanguage().getString("BUTTON_BACK"));
        ret.setCustomModelData(GUIControlCentre.BUTTON_BACK.customModelData());
        fast.setItemMeta(ret);
        // areas
        ItemStack area = new ItemStack(GUIControlCentre.BUTTON_AREAS.material(), 1);
        ItemMeta tar = area.getItemMeta();
        tar.setDisplayName(plugin.getLanguage().getString("BUTTON_AREAS"));
        tar.setCustomModelData(GUIControlCentre.BUTTON_AREAS.customModelData());
        area.setItemMeta(tar);
        // destination terminal
        ItemStack ter = new ItemStack(GUIControlCentre.BUTTON_TERM.material(), 1);
        ItemMeta min = ter.getItemMeta();
        min.setDisplayName(plugin.getLanguage().getString("BUTTON_TERM"));
        min.setCustomModelData(GUIControlCentre.BUTTON_TERM.customModelData());
        ter.setItemMeta(min);
        // space time throttle
        ItemStack thro = new ItemStack(GUIControlCentre.BUTTON_THROTTLE.material(), 1);
        ItemMeta ttle = thro.getItemMeta();
        ttle.setDisplayName(plugin.getLanguage().getString("BUTTON_THROTTLE"));
        String throttle = SpaceTimeThrottle.getByDelay().get(delay).toString();
        ttle.setLore(List.of(throttle));
        ttle.setCustomModelData(GUIControlCentre.BUTTON_THROTTLE.customModelData());
        thro.setItemMeta(ttle);
        /*
         * ***** INTERIOR *****
         */
        // architectural reconfiguration system
        ItemStack ars = new ItemStack(GUIControlCentre.BUTTON_ARS.material(), 1);
        ItemMeta but = ars.getItemMeta();
        but.setDisplayName(plugin.getLanguage().getString("BUTTON_ARS"));
        but.setCustomModelData(GUIControlCentre.BUTTON_ARS.customModelData());
        ars.setItemMeta(but);
        // desktop theme
        ItemStack upg = new ItemStack(GUIControlCentre.BUTTON_THEME.material(), 1);
        ItemMeta rade = upg.getItemMeta();
        rade.setDisplayName(plugin.getLanguage().getString("BUTTON_THEME"));
        rade.setCustomModelData(GUIControlCentre.BUTTON_THEME.customModelData());
        upg.setItemMeta(rade);
        // power up/down
        ItemStack pow = new ItemStack(GUIControlCentre.BUTTON_POWER.material(), 1);
        ItemMeta dwn = pow.getItemMeta();
        dwn.setDisplayName(plugin.getLanguage().getString("BUTTON_POWER"));
        dwn.setLore(List.of(power_onoff));
        int pcmd = GUIControlCentre.BUTTON_POWER.customModelData();
        if (power_onoff.equals(off)) {
            pcmd += 100;
        }
        dwn.setCustomModelData(pcmd);
        pow.setItemMeta(dwn);
        // light
        ItemStack lig = new ItemStack(GUIControlCentre.BUTTON_LIGHTS.material(), 1);
        ItemMeta swi = lig.getItemMeta();
        swi.setDisplayName(plugin.getLanguage().getString("BUTTON_LIGHTS"));
        swi.setLore(List.of(lights_onoff));
        int lcmd = GUIControlCentre.BUTTON_LIGHTS.customModelData();
        if (lights_onoff.equals(off)) {
            lcmd += 100;
        }
        swi.setCustomModelData(lcmd);
        lig.setItemMeta(swi);
        // toggle wool
        ItemStack tog = new ItemStack(GUIControlCentre.BUTTON_TOGGLE.material(), 1);
        ItemMeta gle = tog.getItemMeta();
        gle.setDisplayName(plugin.getLanguage().getString("BUTTON_TOGGLE"));
        gle.setLore(List.of(toggle_openclosed));
        int tcmd = GUIControlCentre.BUTTON_TOGGLE.customModelData();
        if (!open) {
            tcmd += 100;
        }
        gle.setCustomModelData(tcmd);
        tog.setItemMeta(gle);
        // tardis map
        ItemStack map = new ItemStack(Material.MAP, 1);
        ItemMeta me = map.getItemMeta();
        me.setDisplayName(plugin.getLanguage().getString("BUTTON_TARDIS_MAP"));
        me.setCustomModelData(GUIControlCentre.BUTTON_TARDIS_MAP.customModelData());
        map.setItemMeta(me);
        /*
         * ***** EXTERIOR *****
         */
        // chameleon circuit
        ItemStack cham = new ItemStack(GUIControlCentre.BUTTON_CHAMELEON.material(), 1);
        ItemMeta eleon = cham.getItemMeta();
        eleon.setDisplayName(plugin.getLanguage().getString("BUTTON_CHAMELEON"));
        eleon.setCustomModelData(GUIControlCentre.BUTTON_CHAMELEON.customModelData());
        cham.setItemMeta(eleon);
        // siege
        ItemStack siege = new ItemStack(GUIControlCentre.BUTTON_SIEGE.material(), 1);
        ItemMeta mode = siege.getItemMeta();
        mode.setDisplayName(plugin.getLanguage().getString("BUTTON_SIEGE"));
        mode.setLore(List.of(siege_onoff));
        int scmd = GUIControlCentre.BUTTON_SIEGE.customModelData();
        if (siege_onoff.equals(off)) {
            scmd += 100;
        }
        mode.setCustomModelData(scmd);
        siege.setItemMeta(mode);
        // hide
        ItemStack hide = new ItemStack(GUIControlCentre.BUTTON_HIDE.material(), 1);
        ItemMeta box = hide.getItemMeta();
        box.setDisplayName(plugin.getLanguage().getString("BUTTON_HIDE"));
        box.setCustomModelData(GUIControlCentre.BUTTON_HIDE.customModelData());
        hide.setItemMeta(box);
        // rebuild
        ItemStack reb = new ItemStack(GUIControlCentre.BUTTON_REBUILD.material(), 1);
        ItemMeta uild = reb.getItemMeta();
        uild.setDisplayName(plugin.getLanguage().getString("BUTTON_REBUILD"));
        uild.setCustomModelData(GUIControlCentre.BUTTON_REBUILD.customModelData());
        reb.setItemMeta(uild);
        // direction
        ItemStack dir = new ItemStack(GUIControlCentre.BUTTON_DIRECTION.material(), 1);
        ItemMeta ection = dir.getItemMeta();
        ection.setDisplayName(plugin.getLanguage().getString("BUTTON_DIRECTION"));
        ection.setCustomModelData(GUIControlCentre.BUTTON_DIRECTION.customModelData());
        ection.setLore(List.of(direction));
        dir.setItemMeta(ection);
        // temporal
        ItemStack temp = new ItemStack(GUIControlCentre.BUTTON_TEMP.material(), 1);
        ItemMeta oral = temp.getItemMeta();
        oral.setDisplayName(plugin.getLanguage().getString("BUTTON_TEMP"));
        oral.setCustomModelData(GUIControlCentre.BUTTON_TEMP.customModelData());
        temp.setItemMeta(oral);
        /*
         * ***** INFORMATION *****
         */
        // artron levels
        ItemStack art = new ItemStack(GUIControlCentre.BUTTON_ARTRON.material(), 1);
        ItemMeta ron = art.getItemMeta();
        ron.setDisplayName(plugin.getLanguage().getString("BUTTON_ARTRON"));
        ron.setCustomModelData(GUIControlCentre.BUTTON_ARTRON.customModelData());
        art.setItemMeta(ron);
        // scanner
        ItemStack scan = new ItemStack(GUIControlCentre.BUTTON_SCANNER.material(), 1);
        ItemMeta ner = scan.getItemMeta();
        ner.setDisplayName(plugin.getLanguage().getString("BUTTON_SCANNER"));
        ner.setCustomModelData(GUIControlCentre.BUTTON_SCANNER.customModelData());
        scan.setItemMeta(ner);
        // TIS
        ItemStack info = new ItemStack(GUIControlCentre.BUTTON_INFO.material(), 1);
        ItemMeta sys = info.getItemMeta();
        sys.setDisplayName(plugin.getLanguage().getString("BUTTON_INFO"));
        sys.setCustomModelData(GUIControlCentre.BUTTON_INFO.customModelData());
        info.setItemMeta(sys);
        // transmats
        ItemStack tran = new ItemStack(GUIControlCentre.BUTTON_TRANSMAT.material(), 1);
        ItemMeta smat = tran.getItemMeta();
        smat.setDisplayName(plugin.getLanguage().getString("BUTTON_TRANSMAT"));
        smat.setCustomModelData(GUIControlCentre.BUTTON_TRANSMAT.customModelData());
        tran.setItemMeta(smat);
        /*
         * ***** OTHER *****
         */
        // zero room
        ItemStack zero = new ItemStack(GUIControlCentre.BUTTON_ZERO.material(), 1);
        ItemMeta room = zero.getItemMeta();
        room.setDisplayName(plugin.getLanguage().getString("BUTTON_ZERO"));
        room.setCustomModelData(GUIControlCentre.BUTTON_ZERO.customModelData());
        zero.setItemMeta(room);
        // player prefs
        ItemStack player = new ItemStack(GUIControlCentre.BUTTON_PREFS.material(), 1);
        ItemMeta prefs = player.getItemMeta();
        prefs.setDisplayName(plugin.getLanguage().getString("BUTTON_PREFS"));
        prefs.setCustomModelData(GUIControlCentre.BUTTON_PREFS.customModelData());
        player.setItemMeta(prefs);
        // companion menu
        ItemStack companion = new ItemStack(GUIControlCentre.COMPANIONS_MENU.material(), 1);
        ItemMeta list = companion.getItemMeta();
        list.setDisplayName(plugin.getLanguage().getString("COMPANIONS_MENU"));
        list.setCustomModelData(GUIControlCentre.COMPANIONS_MENU.customModelData());
        companion.setItemMeta(list);
        // system_upgrades
        ItemStack system = null;
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
            system = new ItemStack(GUIControlCentre.BUTTON_SYSTEM_UPGRADES.material(), 1);
            ItemMeta upgrades = system.getItemMeta();
            upgrades.setDisplayName(plugin.getLanguage().getString("SYS_MENU"));
            upgrades.setCustomModelData(GUIControlCentre.BUTTON_SYSTEM_UPGRADES.customModelData());
            system.setItemMeta(upgrades);
        }
        // close
        ItemStack close = new ItemStack(GUIControlCentre.BUTTON_CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GUIControlCentre.BUTTON_CLOSE.customModelData());
        close.setItemMeta(can);

        return new ItemStack[]{
            ran, null, ars, null, cham, null, art, null, zero,
            save, null, upg, null, siege, null, scan, null, player,
            own, null, pow, null, hide, null, info, null, companion,
            fast, null, lig, null, reb, null, tran, null, system,
            area, null, tog, null, dir, null, null, null, null,
            ter, null, map, null, temp, null, thro, null, close
        };
    }

    public ItemStack[] getControls() {
        return controls;
    }
}
