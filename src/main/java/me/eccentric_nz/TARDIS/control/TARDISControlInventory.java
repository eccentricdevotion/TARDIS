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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIControlCentre;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISControlInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final int id;
    private final Inventory inventory;

    public TARDISControlInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Control Menu", NamedTextColor.DARK_RED));
        this.inventory.setContents(getControls());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Control Centre GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getControls() {

        // get tardis options
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        String siege_onoff = "";
        String power_onoff = "";
        String direction = "";
        String off = plugin.getLanguage().getString("SET_OFF", "OFF");
        String on = plugin.getLanguage().getString("SET_ON", "ON");
        int delay = 1;
        boolean powered = true;
        boolean displayDoor = false;
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            siege_onoff = (tardis.isSiegeOn()) ? on : off;
            power_onoff = (tardis.isPoweredOn()) ? on : off;
            powered = tardis.isPoweredOn();
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (rsc.resultSet()) {
                direction = rsc.getCurrent().direction().toString();
            }
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, tardis.getUuid().toString());
            if (rsp.resultSet()) {
                delay = rsp.getThrottle();
                displayDoor = rsp.isOpenDisplayDoorOn();
            }
        }
        /*
         * ***** TRAVEL *****
         */
        // random location
        ItemStack ran = ItemStack.of(GUIControlCentre.BUTTON_RANDOM.material(), 1);
        ItemMeta dom = ran.getItemMeta();
        dom.displayName(Component.text()
                .append(Component.text("ran").decorate(TextDecoration.OBFUSCATED))
                .append(Component.text(plugin.getLanguage().getString("BUTTON_RANDOM", "Random Location")).decoration(TextDecoration.OBFUSCATED, false))
                .append(Component.text("dom").decorate(TextDecoration.OBFUSCATED))
                .build()
        );
        ran.setItemMeta(dom);
        // Saves
        ItemStack save = ItemStack.of(GUIControlCentre.BUTTON_SAVES.material(), 1);
        ItemMeta locs = save.getItemMeta();
        locs.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SAVES", "Saved Locations")));
        locs.lore(List.of(Component.text("load saves from this TARDIS")));
        save.setItemMeta(locs);
        // Own saves if in another player's TARDIS
        ItemStack own = ItemStack.of(GUIControlCentre.BUTTON_SAVES.material(), 1);
        ItemMeta saves = own.getItemMeta();
        saves.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SAVES", "Saved Locations")));
        saves.lore(List.of(Component.text("Load my saves")));
        own.setItemMeta(saves);
        // back
        ItemStack fast = ItemStack.of(GUIControlCentre.BUTTON_BACK.material(), 1);
        ItemMeta ret = fast.getItemMeta();
        ret.displayName(Component.text(plugin.getLanguage().getString("BUTTON_BACK", "Fast Return")));
        fast.setItemMeta(ret);
        // areas
        ItemStack area = ItemStack.of(GUIControlCentre.BUTTON_AREAS.material(), 1);
        ItemMeta tar = area.getItemMeta();
        tar.displayName(Component.text(plugin.getLanguage().getString("BUTTON_AREAS", "TARDIS Areas")));
        area.setItemMeta(tar);
        // destination terminal
        ItemStack ter = ItemStack.of(GUIControlCentre.BUTTON_TERM.material(), 1);
        ItemMeta min = ter.getItemMeta();
        min.displayName(Component.text(plugin.getLanguage().getString("BUTTON_TERM", "Destination Terminal")));
        ter.setItemMeta(min);
        // space time throttle
        ItemStack thro = ItemStack.of(GUIControlCentre.BUTTON_THROTTLE.material(), 1);
        ItemMeta ttle = thro.getItemMeta();
        ttle.displayName(Component.text(plugin.getLanguage().getString("BUTTON_THROTTLE", "Space Time Throttle")));
        String throttle = SpaceTimeThrottle.getByDelay().get(delay).toString();
        ttle.lore(List.of(Component.text(throttle)));
        thro.setItemMeta(ttle);
        /*
         * ***** INTERIOR *****
         */
        // architectural reconfiguration system
        ItemStack ars = ItemStack.of(GUIControlCentre.BUTTON_ARS.material(), 1);
        ItemMeta but = ars.getItemMeta();
        but.displayName(Component.text(plugin.getLanguage().getString("BUTTON_ARS", "Architectural Reconfiguration System")));
        ars.setItemMeta(but);
        // desktop theme
        ItemStack upg = ItemStack.of(GUIControlCentre.BUTTON_THEME.material(), 1);
        ItemMeta rade = upg.getItemMeta();
        rade.displayName(Component.text(plugin.getLanguage().getString("BUTTON_THEME", "Desktop Theme")));
        upg.setItemMeta(rade);
        // power up/down
        ItemStack pow = ItemStack.of(GUIControlCentre.BUTTON_POWER.material(), 1);
        ItemMeta dwn = pow.getItemMeta();
        dwn.displayName(Component.text(plugin.getLanguage().getString("BUTTON_POWER", "Power")));
        dwn.lore(List.of(Component.text(power_onoff)));
        CustomModelDataComponent pdcomponent = dwn.getCustomModelDataComponent();
        pdcomponent.setFloats(!powered ? SwitchVariant.BUTTON_POWER_OFF.getFloats() : SwitchVariant.BUTTON_POWER_ON.getFloats());
        dwn.setCustomModelDataComponent(pdcomponent);
        pow.setItemMeta(dwn);
        // light
        ItemStack lig = ItemStack.of(GUIControlCentre.BUTTON_LIGHTS.material(), 1);
        ItemMeta swi = lig.getItemMeta();
        swi.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LIGHTS", "Lights")));
        swi.lore(List.of(Component.text("All the light things!")));
        lig.setItemMeta(swi);
        // toggle wool / open or close display door
        ItemStack tog = ItemStack.of(GUIControlCentre.BUTTON_TOGGLE.material(), 1);
        ItemMeta gle = tog.getItemMeta();
        CustomModelDataComponent twcomponent = gle.getCustomModelDataComponent();
        boolean open;
        if (displayDoor) {
            ResultSetDoorBlocks rsd = new ResultSetDoorBlocks(plugin, id);
            rsd.resultSet();
            open = TARDISStaticUtils.isDoorOpen(rsd.getInnerBlock());
            gle.displayName(Component.text(plugin.getLanguage().getString("BUTTON_DOOR", "TARDIS door")));
            String currently = (open) ? plugin.getLanguage().getString("SET_OPEN", "OPEN") : plugin.getLanguage().getString("SET_CLOSED", "CLOSED");
            String clickTo = (open) ? "close" : "open";
            gle.lore(List.of(Component.text("Currently " + currently), Component.text("Click to " + clickTo)));
            twcomponent.setFloats(!open ? SwitchVariant.DISPLAY_DOOR_CLOSED.getFloats() : SwitchVariant.DISPLAY_DOOR_OPEN.getFloats());
        } else {
            open = new TARDISBlackWoolToggler(plugin).isOpen(id);
            gle.displayName(Component.text(plugin.getLanguage().getString("BUTTON_TOGGLE", "Toggle blocks behind door")));
            String toggle_openclosed = (open) ? plugin.getLanguage().getString("SET_OPEN", "OPEN") : plugin.getLanguage().getString("SET_CLOSED", "CLOSED");
            gle.lore(List.of(Component.text(toggle_openclosed)));
            twcomponent.setFloats(!open ? SwitchVariant.BUTTON_TOGGLE_OFF.getFloats() : SwitchVariant.BUTTON_TOGGLE_ON.getFloats());
        }
        gle.setCustomModelDataComponent(twcomponent);
        tog.setItemMeta(gle);
        // tardis map
        ItemStack map = ItemStack.of(Material.MAP, 1);
        ItemMeta me = map.getItemMeta();
        me.displayName(Component.text(plugin.getLanguage().getString("BUTTON_TARDIS_MAP", "TARDIS Map")));
        map.setItemMeta(me);
        /*
         * ***** EXTERIOR *****
         */
        // chameleon circuit
        ItemStack cham = ItemStack.of(GUIControlCentre.BUTTON_CHAMELEON.material(), 1);
        ItemMeta eleon = cham.getItemMeta();
        eleon.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CHAMELEON", "Chameleon Circuit")));
        cham.setItemMeta(eleon);
        // siege
        ItemStack siege = ItemStack.of(GUIControlCentre.BUTTON_SIEGE.material(), 1);
        ItemMeta mode = siege.getItemMeta();
        mode.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SIEGE", "Siege Mode")));
        mode.lore(List.of(Component.text(siege_onoff)));
        CustomModelDataComponent smcomponent = mode.getCustomModelDataComponent();
        smcomponent.setFloats(siege_onoff.equals(off) ? SwitchVariant.SIEGE_OFF.getFloats() : SwitchVariant.SIEGE_ON.getFloats());
        mode.setCustomModelDataComponent(smcomponent);
        siege.setItemMeta(mode);
        // hide
        ItemStack hide = ItemStack.of(GUIControlCentre.BUTTON_HIDE.material(), 1);
        ItemMeta box = hide.getItemMeta();
        box.displayName(Component.text(plugin.getLanguage().getString("BUTTON_HIDE", "Hide")));
        hide.setItemMeta(box);
        // rebuild
        ItemStack reb = ItemStack.of(GUIControlCentre.BUTTON_REBUILD.material(), 1);
        ItemMeta uild = reb.getItemMeta();
        uild.displayName(Component.text(plugin.getLanguage().getString("BUTTON_REBUILD", "Rebuild")));
        reb.setItemMeta(uild);
        // direction
        ItemStack dir = ItemStack.of(GUIControlCentre.BUTTON_DIRECTION.material(), 1);
        ItemMeta ection = dir.getItemMeta();
        ection.displayName(Component.text(plugin.getLanguage().getString("BUTTON_DIRECTION", "Direction")));
        ection.lore(List.of(Component.text(direction)));
        dir.setItemMeta(ection);
        // temporal
        ItemStack temp = ItemStack.of(GUIControlCentre.BUTTON_TEMP.material(), 1);
        ItemMeta oral = temp.getItemMeta();
        oral.displayName(Component.text(plugin.getLanguage().getString("BUTTON_TEMP", "Temporal Locator")));
        temp.setItemMeta(oral);
        /*
         * ***** INFORMATION *****
         */
        // artron levels
        ItemStack art = ItemStack.of(GUIControlCentre.BUTTON_ARTRON.material(), 1);
        ItemMeta ron = art.getItemMeta();
        ron.displayName(Component.text(plugin.getLanguage().getString("BUTTON_ARTRON", "Artron Energy Levels")));
        art.setItemMeta(ron);
        // scanner
        ItemStack scan = ItemStack.of(GUIControlCentre.BUTTON_SCANNER.material(), 1);
        ItemMeta ner = scan.getItemMeta();
        ner.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCANNER", "Scanner")));
        scan.setItemMeta(ner);
        // TIS
        ItemStack info = ItemStack.of(GUIControlCentre.BUTTON_INFO.material(), 1);
        ItemMeta sys = info.getItemMeta();
        sys.displayName(Component.text(plugin.getLanguage().getString("BUTTON_INFO", "TARDIS Information System")));
        info.setItemMeta(sys);
        // transmats
        ItemStack tran = ItemStack.of(GUIControlCentre.BUTTON_TRANSMAT.material(), 1);
        ItemMeta smat = tran.getItemMeta();
        smat.displayName(Component.text(plugin.getLanguage().getString("BUTTON_TRANSMAT", "Transmat")));
        tran.setItemMeta(smat);
        /*
         * ***** OTHER *****
         */
        // zero room
        ItemStack zero = ItemStack.of(GUIControlCentre.BUTTON_ZERO.material(), 1);
        ItemMeta room = zero.getItemMeta();
        room.displayName(Component.text(plugin.getLanguage().getString("BUTTON_ZERO", "Zero Room transmat")));
        zero.setItemMeta(room);
        // player prefs
        ItemStack player = ItemStack.of(GUIControlCentre.BUTTON_PREFS.material(), 1);
        ItemMeta prefs = player.getItemMeta();
        prefs.displayName(Component.text(plugin.getLanguage().getString("BUTTON_PREFS", "Player Preferences")));
        player.setItemMeta(prefs);
        // companion menu
        ItemStack companion = ItemStack.of(GUIControlCentre.COMPANIONS_MENU.material(), 1);
        ItemMeta list = companion.getItemMeta();
        list.displayName(Component.text(plugin.getLanguage().getString("COMPANIONS_MENU", "Companion Menu")));
        companion.setItemMeta(list);
        // system_upgrades
        ItemStack system = null;
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
            system = ItemStack.of(GUIControlCentre.BUTTON_SYSTEM_UPGRADES.material(), 1);
            ItemMeta upgrades = system.getItemMeta();
            upgrades.displayName(Component.text(plugin.getLanguage().getString("SYS_MENU", "System Upgrades")));
            system.setItemMeta(upgrades);
        }
        // close
        ItemStack close = ItemStack.of(GUIControlCentre.BUTTON_CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
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
}
