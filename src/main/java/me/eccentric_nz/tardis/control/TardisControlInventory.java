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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.custommodeldata.GuiControlCentre;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.move.TardisBlackWoolToggler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisControlInventory {

    private final TardisPlugin plugin;
    private final UUID uuid;
    private final ItemStack[] controls;

    TardisControlInventory(TardisPlugin plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
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
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        String lightsOnOff = "";
        String siegeOnOff = "";
        String toggleOpenClosed = "";
        String poweredOnOff = "";
        String direction = "";
        String off = plugin.getLanguage().getString("SET_OFF");
        String on = plugin.getLanguage().getString("SET_ON");
        boolean open = false;
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            siegeOnOff = (tardis.isSiegeOn()) ? on : off;
            lightsOnOff = (tardis.isLightsOn()) ? on : off;
            open = new TardisBlackWoolToggler(plugin).isOpen(tardis.getTardisId());
            toggleOpenClosed = (open) ? plugin.getLanguage().getString("SET_OPEN") : plugin.getLanguage().getString("SET_CLOSED");
            poweredOnOff = (tardis.isPowered()) ? on : off;
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", tardis.getTardisId());
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wheret);
            if (rsc.resultSet()) {
                direction = rsc.getDirection().toString();
            }
        }
        /*
         * ***** TRAVEL *****
         */
        // random location
        ItemStack ran = new ItemStack(Material.BOWL, 1);
        ItemMeta dom = ran.getItemMeta();
        assert dom != null;
        dom.setDisplayName(ChatColor.MAGIC + "ran" + ChatColor.RESET + plugin.getLanguage().getString("BUTTON_RANDOM") + ChatColor.MAGIC + "dom");
        dom.setCustomModelData(GuiControlCentre.BUTTON_RANDOM.getCustomModelData());
        ran.setItemMeta(dom);
        // Saves
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta locs = save.getItemMeta();
        assert locs != null;
        locs.setDisplayName(plugin.getLanguage().getString("BUTTON_SAVES"));
        locs.setCustomModelData(GuiControlCentre.BUTTON_SAVES.getCustomModelData());
        save.setItemMeta(locs);
        // back
        ItemStack fast = new ItemStack(Material.BOWL, 1);
        ItemMeta ret = fast.getItemMeta();
        assert ret != null;
        ret.setDisplayName(plugin.getLanguage().getString("BUTTON_BACK"));
        ret.setCustomModelData(GuiControlCentre.BUTTON_BACK.getCustomModelData());
        fast.setItemMeta(ret);
        // areas
        ItemStack area = new ItemStack(Material.BOWL, 1);
        ItemMeta tar = area.getItemMeta();
        assert tar != null;
        tar.setDisplayName(plugin.getLanguage().getString("BUTTON_AREAS"));
        tar.setCustomModelData(GuiControlCentre.BUTTON_AREAS.getCustomModelData());
        area.setItemMeta(tar);
        // destination terminal
        ItemStack ter = new ItemStack(Material.BOWL, 1);
        ItemMeta min = ter.getItemMeta();
        assert min != null;
        min.setDisplayName(plugin.getLanguage().getString("BUTTON_TERM"));
        min.setCustomModelData(GuiControlCentre.BUTTON_TERM.getCustomModelData());
        ter.setItemMeta(min);
        /*
         * ***** INTERIOR *****
         */
        // architectural reconfiguration system
        ItemStack ars = new ItemStack(Material.BOWL, 1);
        ItemMeta but = ars.getItemMeta();
        assert but != null;
        but.setDisplayName(plugin.getLanguage().getString("BUTTON_ARS"));
        but.setCustomModelData(GuiControlCentre.BUTTON_ARS.getCustomModelData());
        ars.setItemMeta(but);
        // desktop theme
        ItemStack upg = new ItemStack(Material.BOWL, 1);
        ItemMeta rade = upg.getItemMeta();
        assert rade != null;
        rade.setDisplayName(plugin.getLanguage().getString("BUTTON_THEME"));
        rade.setCustomModelData(GuiControlCentre.BUTTON_THEME.getCustomModelData());
        upg.setItemMeta(rade);
        // power up/down
        ItemStack pow = new ItemStack(Material.REPEATER, 1);
        ItemMeta dwn = pow.getItemMeta();
        assert dwn != null;
        dwn.setDisplayName(plugin.getLanguage().getString("BUTTON_POWER"));
        dwn.setLore(Collections.singletonList(poweredOnOff));
        int pcmd = GuiControlCentre.BUTTON_POWER.getCustomModelData();
        assert poweredOnOff != null;
        if (poweredOnOff.equals(off)) {
            pcmd += 100;
        }
        dwn.setCustomModelData(pcmd);
        pow.setItemMeta(dwn);
        // light
        ItemStack lig = new ItemStack(Material.REPEATER, 1);
        ItemMeta swi = lig.getItemMeta();
        assert swi != null;
        swi.setDisplayName(plugin.getLanguage().getString("BUTTON_LIGHTS"));
        swi.setLore(Collections.singletonList(lightsOnOff));
        int lcmd = GuiControlCentre.BUTTON_LIGHTS.getCustomModelData();
        assert lightsOnOff != null;
        if (lightsOnOff.equals(off)) {
            lcmd += 100;
        }
        swi.setCustomModelData(lcmd);
        lig.setItemMeta(swi);
        // toggle wool
        ItemStack tog = new ItemStack(Material.REPEATER, 1);
        ItemMeta gle = tog.getItemMeta();
        assert gle != null;
        gle.setDisplayName(plugin.getLanguage().getString("BUTTON_TOGGLE"));
        gle.setLore(Collections.singletonList(toggleOpenClosed));
        int tcmd = GuiControlCentre.BUTTON_TOGGLE.getCustomModelData();
        if (!open) {
            tcmd += 100;
        }
        gle.setCustomModelData(tcmd);
        tog.setItemMeta(gle);
        // tardis map
        ItemStack map = new ItemStack(Material.MAP, 1);
        ItemMeta me = map.getItemMeta();
        assert me != null;
        me.setDisplayName(plugin.getLanguage().getString("BUTTON_TARDIS_MAP"));
        me.setCustomModelData(GuiControlCentre.BUTTON_TARDIS_MAP.getCustomModelData());
        map.setItemMeta(me);
        /*
         * ***** EXTERIOR *****
         */
        // chameleon circuit
        ItemStack cham = new ItemStack(Material.BOWL, 1);
        ItemMeta eleon = cham.getItemMeta();
        assert eleon != null;
        eleon.setDisplayName(plugin.getLanguage().getString("BUTTON_CHAMELEON"));
        eleon.setCustomModelData(GuiControlCentre.BUTTON_CHAMELEON.getCustomModelData());
        cham.setItemMeta(eleon);
        // siege
        ItemStack siege = new ItemStack(Material.REPEATER, 1);
        ItemMeta mode = siege.getItemMeta();
        assert mode != null;
        mode.setDisplayName(plugin.getLanguage().getString("BUTTON_SIEGE"));
        mode.setLore(Collections.singletonList(siegeOnOff));
        int scmd = GuiControlCentre.BUTTON_SIEGE.getCustomModelData();
        assert siegeOnOff != null;
        if (siegeOnOff.equals(off)) {
            scmd += 100;
        }
        mode.setCustomModelData(scmd);
        siege.setItemMeta(mode);
        // hide
        ItemStack hide = new ItemStack(Material.BOWL, 1);
        ItemMeta box = hide.getItemMeta();
        assert box != null;
        box.setDisplayName(plugin.getLanguage().getString("BUTTON_HIDE"));
        box.setCustomModelData(GuiControlCentre.BUTTON_HIDE.getCustomModelData());
        hide.setItemMeta(box);
        // rebuild
        ItemStack reb = new ItemStack(Material.BOWL, 1);
        ItemMeta uild = reb.getItemMeta();
        assert uild != null;
        uild.setDisplayName(plugin.getLanguage().getString("BUTTON_REBUILD"));
        uild.setCustomModelData(GuiControlCentre.BUTTON_REBUILD.getCustomModelData());
        reb.setItemMeta(uild);
        // direction
        ItemStack dir = new ItemStack(Material.BOWL, 1);
        ItemMeta ection = dir.getItemMeta();
        assert ection != null;
        ection.setDisplayName(plugin.getLanguage().getString("BUTTON_DIRECTION"));
        ection.setCustomModelData(GuiControlCentre.BUTTON_DIRECTION.getCustomModelData());
        ection.setLore(Collections.singletonList(direction));
        dir.setItemMeta(ection);
        // temporal
        ItemStack temp = new ItemStack(Material.BOWL, 1);
        ItemMeta oral = temp.getItemMeta();
        assert oral != null;
        oral.setDisplayName(plugin.getLanguage().getString("BUTTON_TEMP"));
        oral.setCustomModelData(GuiControlCentre.BUTTON_TEMP.getCustomModelData());
        temp.setItemMeta(oral);
        /*
         * ***** INFORMATION *****
         */
        // artron levels
        ItemStack art = new ItemStack(Material.BOWL, 1);
        ItemMeta ron = art.getItemMeta();
        assert ron != null;
        ron.setDisplayName(plugin.getLanguage().getString("BUTTON_ARTRON"));
        ron.setCustomModelData(GuiControlCentre.BUTTON_ARTRON.getCustomModelData());
        art.setItemMeta(ron);
        // scanner
        ItemStack scan = new ItemStack(Material.BOWL, 1);
        ItemMeta ner = scan.getItemMeta();
        assert ner != null;
        ner.setDisplayName(plugin.getLanguage().getString("BUTTON_SCANNER"));
        ner.setCustomModelData(GuiControlCentre.BUTTON_SCANNER.getCustomModelData());
        scan.setItemMeta(ner);
        // TIS
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta sys = info.getItemMeta();
        assert sys != null;
        sys.setDisplayName(plugin.getLanguage().getString("BUTTON_INFO"));
        sys.setCustomModelData(GuiControlCentre.BUTTON_INFO.getCustomModelData());
        info.setItemMeta(sys);
        // transmats
        ItemStack tran = new ItemStack(Material.BOWL, 1);
        ItemMeta smat = tran.getItemMeta();
        assert smat != null;
        smat.setDisplayName(plugin.getLanguage().getString("BUTTON_TRANSMAT"));
        smat.setCustomModelData(GuiControlCentre.BUTTON_TRANSMAT.getCustomModelData());
        tran.setItemMeta(smat);
        /*
         * ***** OTHER *****
         */
        // zero room
        ItemStack zero = new ItemStack(Material.BOWL, 1);
        ItemMeta room = zero.getItemMeta();
        assert room != null;
        room.setDisplayName(plugin.getLanguage().getString("BUTTON_ZERO"));
        room.setCustomModelData(GuiControlCentre.BUTTON_ZERO.getCustomModelData());
        zero.setItemMeta(room);
        // player prefs
        ItemStack player = new ItemStack(Material.BOWL, 1);
        ItemMeta prefs = player.getItemMeta();
        assert prefs != null;
        prefs.setDisplayName(plugin.getLanguage().getString("BUTTON_PREFS"));
        prefs.setCustomModelData(GuiControlCentre.BUTTON_PREFS.getCustomModelData());
        player.setItemMeta(prefs);
        // companion menu
        ItemStack companion = new ItemStack(Material.BOWL, 1);
        ItemMeta list = companion.getItemMeta();
        assert list != null;
        list.setDisplayName(plugin.getLanguage().getString("COMPANIONS_MENU"));
        list.setCustomModelData(GuiControlCentre.COMPANIONS_MENU.getCustomModelData());
        companion.setItemMeta(list);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        assert can != null;
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GuiControlCentre.BUTTON_CLOSE.getCustomModelData());
        close.setItemMeta(can);

        return new ItemStack[]{ran, null, ars, null, cham, null, art, null, zero, save, null, upg, null, siege, null, scan, null, player, fast, null, pow, null, hide, null, info, null, companion, area, null, lig, null, reb, null, tran, null, null, ter, null, tog, null, dir, null, null, null, null, null, null, map, null, temp, null, null, null, close};
    }

    public ItemStack[] getControls() {
        return controls;
    }
}
