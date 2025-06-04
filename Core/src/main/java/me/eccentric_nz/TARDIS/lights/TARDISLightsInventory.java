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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISLightsInventory {

    private final TARDIS plugin;
    private final int id;
    private final UUID uuid;
    private final ItemStack[] GUI;

    public TARDISLightsInventory(TARDIS plugin, int id, UUID uuid) {
        this.plugin = plugin;
        this.id = id;
        this.uuid = uuid;
        this.GUI = getItemStack();
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
        ItemStack lights = new ItemStack(GUILights.LIGHT_INFO.material(), 1);
        ItemMeta sim = lights.getItemMeta();
        sim.setDisplayName("Light Type");
        sim.setLore(List.of("Choose a light", "from the options", "on the right."));
        lights.setItemMeta(sim);
        stacks[GUILights.LIGHT_INFO.slot()] = lights;
        int i = 1;
        for (TardisLight tl : TardisLight.values()) {
            ItemStack light = new ItemStack(tl.getOn().getMaterial(), 1);
            ItemMeta lim = light.getItemMeta();
            lim.setDisplayName(TARDISStringUtils.capitalise(tl.toString()));
            if (lightPref == tl) {
                lim.setLore(List.of("Current light"));
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
        ItemStack block_info = new ItemStack(GUILights.BLOCK_INFO.material(), 1);
        ItemMeta blim = block_info.getItemMeta();
        blim.setDisplayName("Variable Light Block");
        blim.setLore(List.of("Only applies to variable lights.", "Click the button to the right", "to open the blocks menu."));
        block_info.setItemMeta(blim);
        stacks[GUILights.BLOCK_INFO.slot()] = block_info;
        // 28 block button
        ItemStack block_button = new ItemStack(GUILights.BLOCK_BUTTON.material(), 1);
        ItemMeta bbim = block_button.getItemMeta();
        bbim.setDisplayName("Block Type");
        block_button.setItemMeta(bbim);
        stacks[GUILights.BLOCK_BUTTON.slot()] = block_button;
        // 29 populate with block choice if available
        ItemStack block = new ItemStack(material, 1);
        stacks[29] = block;
        // 34 change info
        ItemStack ch_info = new ItemStack(GUILights.CHANGE_INFO.material(), 1);
        ItemMeta chim = ch_info.getItemMeta();
        chim.setDisplayName("Change your light type");
        chim.setLore(List.of("Select a light from above,", "if the light is variable", "also select a block type.", "Click the button to start."));
        ch_info.setItemMeta(chim);
        stacks[GUILights.CHANGE_INFO.slot()] = ch_info;
        // 35 change lights
        ItemStack chan = new ItemStack(GUILights.CHANGE_LIGHTS.material(), 1);
        ItemMeta geim = chan.getItemMeta();
        geim.setDisplayName("Change Lights");
        chan.setItemMeta(geim);
        stacks[GUILights.CHANGE_LIGHTS.slot()] = chan;
        // 41 convert lights info
        ItemStack c_info = new ItemStack(GUILights.CONVERT_INFO.material(), 1);
        ItemMeta ciim = c_info.getItemMeta();
        ciim.setDisplayName("Convert blocks to lights");
        ciim.setLore(List.of("Click the button to the", "right to select a block", "type to convert."));
        c_info.setItemMeta(ciim);
        stacks[GUILights.CONVERT_INFO.slot()] = c_info;
        // 42 light emitting selection
        ItemStack emitting = new ItemStack(GUILights.SELECT_LIGHT.material(), 1);
        ItemMeta emim = emitting.getItemMeta();
        emim.setDisplayName("Select block to convert");
        emitting.setItemMeta(emim);
        stacks[GUILights.SELECT_LIGHT.slot()] = emitting;
        // 43 light emitting choice
        Material lightEmitting = Material.REDSTONE_LAMP;
        if (Sequences.CONVERTERS.containsKey(uuid)) {
            lightEmitting = Material.valueOf(Sequences.CONVERTERS.get(uuid));
        }
        ItemStack lit = new ItemStack(lightEmitting, 1);
        stacks[43] = lit;
        // 44 convert lights
        ItemStack con = new ItemStack(GUILights.CONVERT_LIGHTS.material(), 1);
        ItemMeta vert = con.getItemMeta();
        vert.setDisplayName("Convert blocks to lights");
        vert.setLore(List.of("Will change the block", "type to the left to", "the TARDIS light you", "have selected above."));
        con.setItemMeta(vert);
        stacks[GUILights.CONVERT_LIGHTS.slot()] = con;
        // 45 light switch
        ItemStack lig = new ItemStack(GUILights.LIGHT_SWITCH.material(), 1);
        ItemMeta swi = lig.getItemMeta();
        swi.setDisplayName(plugin.getLanguage().getString("BUTTON_LIGHTS"));
        swi.setLore(List.of(lights_onoff));
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
        ItemStack llis = new ItemStack(GUILights.BUTTON_LIGHT_LEVELS.material(), 1);
        ItemMeta llim = llis.getItemMeta();
        llim.setDisplayName("Light Levels");
        llis.setItemMeta(llim);
        stacks[GUILights.BUTTON_LIGHT_LEVELS.slot()] = llis;
        // 49 light sequence button
        ItemStack sequence = new ItemStack(GUILights.BUTTON_LIGHT_SEQUENCE.material(), 1);
        ItemMeta qim = sequence.getItemMeta();
        qim.setDisplayName("Run Light Sequence");
        sequence.setItemMeta(qim);
        stacks[GUILights.BUTTON_LIGHT_SEQUENCE.slot()] = sequence;
        // 51 edit light sequence page?
        ItemStack edit = new ItemStack(GUILights.EDIT_LIGHT_SEQUENCE.material(), 1);
        ItemMeta eim = edit.getItemMeta();
        eim.setDisplayName("Edit Light Sequence");
        edit.setItemMeta(eim);
        stacks[GUILights.EDIT_LIGHT_SEQUENCE.slot()] = edit;
        // close
        ItemStack close = new ItemStack(GUILights.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
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
            Block block = rsl.getData().getFirst();
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

    public ItemStack[] getGUI() {
        return GUI;
    }
}
