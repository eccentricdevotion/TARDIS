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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIControlCentre;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.move.BlackWoolToggler;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class ControlInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final int id;
    private final Inventory inventory;

    public ControlInventory(TARDIS plugin, int id) {
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
        ItemStack random = ItemStack.of(GUIControlCentre.BUTTON_RANDOM.material(), 1);
        random.setData(DataComponentTypes.CUSTOM_NAME, Component.text()
                .append(Component.text("ran").decorate(TextDecoration.OBFUSCATED))
                .append(Component.text(plugin.getLanguage().getString("BUTTON_RANDOM", "Random Location")).decoration(TextDecoration.OBFUSCATED, false))
                .append(Component.text("dom").decorate(TextDecoration.OBFUSCATED))
                .build()
        );
        // Saves
        ItemStack save = ItemStack.of(GUIControlCentre.BUTTON_SAVES.material(), 1);
        save.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_SAVES", "Saved Locations")));
        save.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("load saves from this TARDIS")).build());
        // Own saves if in another player's TARDIS
        ItemStack own = ItemStack.of(GUIControlCentre.BUTTON_SAVES.material(), 1);
        own.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_SAVES", "Saved Locations")));
        own.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("Load my saves")).build());
        // back
        ItemStack fast = ItemStack.of(GUIControlCentre.BUTTON_BACK.material(), 1);
        fast.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_BACK", "Fast Return")));
        // areas
        ItemStack area = ItemStack.of(GUIControlCentre.BUTTON_AREAS.material(), 1);
        area.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_AREAS", "TARDIS Areas")));
        // destination terminal
        ItemStack terminal = ItemStack.of(GUIControlCentre.BUTTON_TERM.material(), 1);
        terminal.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_TERM", "Destination Terminal")));
        // space time throttle
        ItemStack space = ItemStack.of(GUIControlCentre.BUTTON_THROTTLE.material(), 1);
        space.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_THROTTLE", "Space Time Throttle")));
        String throttle = SpaceTimeThrottle.getByDelay().get(delay).toString();
        space.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(throttle)).build());
        /*
         * ***** INTERIOR *****
         */
        // architectural reconfiguration system
        ItemStack ars = ItemStack.of(GUIControlCentre.BUTTON_ARS.material(), 1);
        ars.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_ARS", "Architectural Reconfiguration System")));
        // desktop theme
        ItemStack upgrade = ItemStack.of(GUIControlCentre.BUTTON_THEME.material(), 1);
        upgrade.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_THEME", "Desktop Theme")));
        // power up/down
        ItemStack power = ItemStack.of(GUIControlCentre.BUTTON_POWER.material(), 1);
        power.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_POWER", "Power")));
        power.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(power_onoff)).build());
        power.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(!powered ? SwitchVariant.BUTTON_POWER_OFF.getFloats() : SwitchVariant.BUTTON_POWER_ON.getFloats())
                .build());
        // light
        ItemStack light = ItemStack.of(GUIControlCentre.BUTTON_LIGHTS.material(), 1);
        light.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LIGHTS", "Lights")));
        light.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("All the light things!")).build());
        // toggle wool / open or close display door
        ItemStack toggle = ItemStack.of(GUIControlCentre.BUTTON_TOGGLE.material(), 1);
//        CustomModelDataComponent twcomponent = gle.getCustomModelDataComponent();
        boolean open;
        if (displayDoor) {
            ResultSetDoorBlocks rsd = new ResultSetDoorBlocks(plugin, id);
            rsd.resultSet();
            open = TARDISStaticUtils.isDoorOpen(rsd.getInnerBlock());
            toggle.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_DOOR", "TARDIS door")));
            String currently = (open) ? plugin.getLanguage().getString("SET_OPEN", "OPEN") : plugin.getLanguage().getString("SET_CLOSED", "CLOSED");
            String clickTo = (open) ? "close" : "open";
            toggle.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                    Component.text("Currently " + currently),
                    Component.text("Click to " + clickTo)
            )));
            toggle.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                    .addFloats(!open ? SwitchVariant.DISPLAY_DOOR_CLOSED.getFloats() : SwitchVariant.DISPLAY_DOOR_OPEN.getFloats())
                    .build());
        } else {
            open = new BlackWoolToggler(plugin).isOpen(id);
            toggle.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_TOGGLE", "Toggle blocks behind door")));
            String toggle_openclosed = (open) ? plugin.getLanguage().getString("SET_OPEN", "OPEN") : plugin.getLanguage().getString("SET_CLOSED", "CLOSED");
            toggle.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(toggle_openclosed)).build());
            toggle.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                    .addFloats(!open ? SwitchVariant.BUTTON_TOGGLE_OFF.getFloats() : SwitchVariant.BUTTON_TOGGLE_ON.getFloats())
                    .build());
        }
        // tardis map
        ItemStack map = ItemStack.of(Material.MAP, 1);
        map.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_TARDIS_MAP", "TARDIS Map")));
        /*
         * ***** EXTERIOR *****
         */
        // chameleon circuit
        ItemStack chameleon = ItemStack.of(GUIControlCentre.BUTTON_CHAMELEON.material(), 1);
        chameleon.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_CHAMELEON", "Chameleon Circuit")));
        // siege
        ItemStack siege = ItemStack.of(GUIControlCentre.BUTTON_SIEGE.material(), 1);
        siege.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_SIEGE", "Siege Mode")));
        siege.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(siege_onoff)).build());
        siege.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(siege_onoff.equals(off) ? SwitchVariant.SIEGE_OFF.getFloats() : SwitchVariant.SIEGE_ON.getFloats())
                .build());
        // hide
        ItemStack hide = ItemStack.of(GUIControlCentre.BUTTON_HIDE.material(), 1);
        hide.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_HIDE", "Hide")));
        // rebuild
        ItemStack rebuild = ItemStack.of(GUIControlCentre.BUTTON_REBUILD.material(), 1);
        rebuild.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_REBUILD", "Rebuild")));
        // direction
        ItemStack dir = ItemStack.of(GUIControlCentre.BUTTON_DIRECTION.material(), 1);
        dir.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_DIRECTION", "Direction")));
        dir.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(direction)).build());
        // temporal
        ItemStack temporal = ItemStack.of(GUIControlCentre.BUTTON_TEMP.material(), 1);
        temporal.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_TEMP", "Temporal Locator")));
        /*
         * ***** INFORMATION *****
         */
        // artron levels
        ItemStack artron = ItemStack.of(GUIControlCentre.BUTTON_ARTRON.material(), 1);
        artron.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_ARTRON", "Artron Energy Levels")));
        // scanner
        ItemStack scanner = ItemStack.of(GUIControlCentre.BUTTON_SCANNER.material(), 1);
        scanner.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_SCANNER", "Scanner")));
        // TIS
        ItemStack info = ItemStack.of(GUIControlCentre.BUTTON_INFO.material(), 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_INFO", "TARDIS Information System")));
        // transmats
        ItemStack tranmats = ItemStack.of(GUIControlCentre.BUTTON_TRANSMAT.material(), 1);
        tranmats.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_TRANSMAT", "Transmat")));
        /*
         * ***** OTHER *****
         */
        // zero room
        ItemStack zero = ItemStack.of(GUIControlCentre.BUTTON_ZERO.material(), 1);
        zero.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_ZERO", "Zero Room transmat")));
        // player prefs
        ItemStack player = ItemStack.of(GUIControlCentre.BUTTON_PREFS.material(), 1);
        player.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_PREFS", "Player Preferences")));
        // companion menu
        ItemStack companion = ItemStack.of(GUIControlCentre.COMPANIONS_MENU.material(), 1);
        companion.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("COMPANIONS_MENU", "Companion Menu")));
        // system_upgrades
        ItemStack system = null;
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
            system = ItemStack.of(GUIControlCentre.BUTTON_SYSTEM_UPGRADES.material(), 1);
            system.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("SYS_MENU", "System Upgrades")));
        }
        // close
        ItemStack close = GUIItemFactory.close();

        return new ItemStack[]{
                random, null, ars, null, chameleon, null, artron, null, zero,
                save, null, upgrade, null, siege, null, scanner, null, player,
                own, null, power, null, hide, null, info, null, companion,
                fast, null, light, null, rebuild, null, tranmats, null, system,
                area, null, toggle, null, dir, null, null, null, null,
                terminal, null, map, null, temporal, null, space, null, close
        };
    }
}
