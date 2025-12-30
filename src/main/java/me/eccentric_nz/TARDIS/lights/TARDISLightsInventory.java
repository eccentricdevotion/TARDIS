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
package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.utility.LightLevel;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.GUILights;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISLightsInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final int id;
    private final UUID uuid;
    private final Inventory inventory;

    public TARDISLightsInventory(TARDIS plugin, int id, UUID uuid) {
        this.plugin = plugin;
        this.id = id;
        this.uuid = uuid;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Lights", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the TARDIS Lights GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // get tardis options
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        String off = plugin.getLanguage().getString("SET_OFF", "OFF");
        String on = plugin.getLanguage().getString("SET_ON", "ON");
        String lights_onoff = on;
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            lights_onoff = (tardis.isLightsOn()) ? on : off;
        }
        ItemStack[] stacks = new ItemStack[54];
        // get current light preferences - create record if none exists
        TardisLight lightPref;
        Material material;
        ResultSetLightPrefs rslp = new ResultSetLightPrefs(plugin);
        if (rslp.fromID(id)) {
            lightPref = rslp.getLight();
            material = rslp.getMaterial();
        } else {
            // try to get current light
            lightPref = getCurrentLight(id);
            material = Material.BONE_BLOCK;
            String levels = getCurrentLevels(id);
            // insert a record
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            set.put("light", lightPref.toString());
            set.put("material", "BONE_BLOCK");
            set.put("levels", levels);
            plugin.getQueryFactory().doSyncInsert("light_prefs", set);
        }
        // 0 lights info
        ItemStack lights = ItemStack.of(GUILights.LIGHT_INFO.material(), 1);
        ItemMeta sim = lights.getItemMeta();
        sim.displayName(Component.text("Light Type"));
        sim.lore(List.of(
                Component.text("Choose a light"),
                Component.text("from the options"),
                Component.text("on the right.")
        ));
        lights.setItemMeta(sim);
        stacks[GUILights.LIGHT_INFO.slot()] = lights;
        int i = 1;
        for (TardisLight tl : TardisLight.values()) {
            ItemStack light = ItemStack.of(tl.getOn().getMaterial(), 1);
            ItemMeta lim = light.getItemMeta();
            lim.displayName(Component.text(TARDISStringUtils.capitalise(tl.toString())));
            if (lightPref == tl) {
                lim.lore(List.of(Component.text("Current light")));
            }
            light.setItemMeta(lim);
            stacks[i] = light;
            if (i % 9 == 8) {
                i += 2;
            } else {
                i++;
            }
        }
        // 27 block info
        ItemStack block_info = ItemStack.of(GUILights.BLOCK_INFO.material(), 1);
        ItemMeta blim = block_info.getItemMeta();
        blim.displayName(Component.text("Variable Light Block"));
        blim.lore(List.of(
                Component.text("Only applies to variable lights."),
                Component.text("Click the button to the right"),
                Component.text("to open the blocks menu.")
        ));
        block_info.setItemMeta(blim);
        stacks[GUILights.BLOCK_INFO.slot()] = block_info;
        // 28 block button
        ItemStack block_button = ItemStack.of(GUILights.BLOCK_BUTTON.material(), 1);
        ItemMeta bbim = block_button.getItemMeta();
        bbim.displayName(Component.text("Block Type"));
        block_button.setItemMeta(bbim);
        stacks[GUILights.BLOCK_BUTTON.slot()] = block_button;
        // 29 populate with block choice if available
        ItemStack block = ItemStack.of(material, 1);
        stacks[29] = block;
        // 34 change info
        ItemStack ch_info = ItemStack.of(GUILights.CHANGE_INFO.material(), 1);
        ItemMeta chim = ch_info.getItemMeta();
        chim.displayName(Component.text("Change your light type"));
        chim.lore(List.of(
                Component.text("Select a light from above,"),
                Component.text("if the light is variable"),
                Component.text("also select a block type."),
                Component.text("Click the button to start.")
        ));
        ch_info.setItemMeta(chim);
        stacks[GUILights.CHANGE_INFO.slot()] = ch_info;
        // 35 change lights
        ItemStack chan = ItemStack.of(GUILights.CHANGE_LIGHTS.material(), 1);
        ItemMeta geim = chan.getItemMeta();
        geim.displayName(Component.text("Change Lights"));
        chan.setItemMeta(geim);
        stacks[GUILights.CHANGE_LIGHTS.slot()] = chan;
        // 41 convert lights info
        ItemStack c_info = ItemStack.of(GUILights.CONVERT_INFO.material(), 1);
        ItemMeta ciim = c_info.getItemMeta();
        ciim.displayName(Component.text("Convert blocks to lights"));
        ciim.lore(List.of(
                Component.text("Click the button to the"),
                Component.text("right to select a block"),
                Component.text("type to convert.")
        ));
        c_info.setItemMeta(ciim);
        stacks[GUILights.CONVERT_INFO.slot()] = c_info;
        // 42 light emitting selection
        ItemStack emitting = ItemStack.of(GUILights.SELECT_LIGHT.material(), 1);
        ItemMeta emim = emitting.getItemMeta();
        emim.displayName(Component.text("Select block to convert"));
        emitting.setItemMeta(emim);
        stacks[GUILights.SELECT_LIGHT.slot()] = emitting;
        // 43 light emitting choice
        Material lightEmitting = Material.REDSTONE_LAMP;
        if (Sequences.CONVERTERS.containsKey(uuid)) {
            lightEmitting = Material.valueOf(Sequences.CONVERTERS.get(uuid));
        }
        ItemStack lit = ItemStack.of(lightEmitting, 1);
        stacks[43] = lit;
        // 44 convert lights
        ItemStack con = ItemStack.of(GUILights.CONVERT_LIGHTS.material(), 1);
        ItemMeta vert = con.getItemMeta();
        vert.displayName(Component.text("Convert blocks to lights"));
        vert.lore(List.of(
                Component.text("Will change the block"),
                Component.text("type to the left to"),
                Component.text("the TARDIS light you"),
                Component.text("have selected above.")
        ));
        con.setItemMeta(vert);
        stacks[GUILights.CONVERT_LIGHTS.slot()] = con;
        // 45 light switch
        ItemStack lig = ItemStack.of(GUILights.LIGHT_SWITCH.material(), 1);
        ItemMeta swi = lig.getItemMeta();
        swi.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LIGHTS")));
        swi.lore(List.of(Component.text(lights_onoff)));
        CustomModelDataComponent lscomponent = swi.getCustomModelDataComponent();
        List<Float> floats = lights_onoff.equals(off) ? SwitchVariant.BUTTON_LIGHTS_OFF.getFloats() : SwitchVariant.BUTTON_LIGHTS_ON.getFloats();
        lscomponent.setFloats(floats);
        swi.setCustomModelDataComponent(lscomponent);
        CustomModelDataComponent lcomponent = swi.getCustomModelDataComponent();
        lcomponent.setFloats(floats);
        swi.setCustomModelDataComponent(lcomponent);
        lig.setItemMeta(swi);
        stacks[GUILights.LIGHT_SWITCH.slot()] = lig;
        // 47 light levels page
        ItemStack llis = ItemStack.of(GUILights.BUTTON_LIGHT_LEVELS.material(), 1);
        ItemMeta llim = llis.getItemMeta();
        llim.displayName(Component.text("Light Levels"));
        llis.setItemMeta(llim);
        stacks[GUILights.BUTTON_LIGHT_LEVELS.slot()] = llis;
        // 49 light sequence button
        ItemStack sequence = ItemStack.of(GUILights.BUTTON_LIGHT_SEQUENCE.material(), 1);
        ItemMeta qim = sequence.getItemMeta();
        qim.displayName(Component.text("Run Light Sequence"));
        sequence.setItemMeta(qim);
        stacks[GUILights.BUTTON_LIGHT_SEQUENCE.slot()] = sequence;
        // 51 edit light sequence page?
        ItemStack edit = ItemStack.of(GUILights.EDIT_LIGHT_SEQUENCE.material(), 1);
        ItemMeta eim = edit.getItemMeta();
        eim.displayName(Component.text("Edit Light Sequence"));
        edit.setItemMeta(eim);
        stacks[GUILights.EDIT_LIGHT_SEQUENCE.slot()] = edit;
        // close
        ItemStack close = ItemStack.of(GUILights.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(clim);
        stacks[GUILights.CLOSE.slot()] = close;
        return stacks;
    }

    private String getCurrentLevels(int id) {
        ResultSetLightLevel rsl = new ResultSetLightLevel(plugin);
        if (rsl.fromTypeAndID(50, id)) {
            int level = LightLevel.interior_level[rsl.getLevel()];
            return String.format("%s:%s:%s:%s:%s:%s:%s:%s:%s", level, level, level, level, level, level, level, level, level);
        }
        return "15:15:15:15:15:15:15:15:15";
    }

    private TardisLight getCurrentLight(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetLamps rsl = new ResultSetLamps(plugin, where, true);
        if (rsl.resultSet()) {
            Block block = rsl.getData().getFirst().block();
            ItemDisplay display = TARDISDisplayItemUtils.get(block);
            if (display != null) {
                TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
                if (tdi != null && tdi.isLight()) {
                    return TardisLight.getFromDisplayItem(tdi);
                }
            }
        }
        return TardisLight.TENTH;
    }
}
