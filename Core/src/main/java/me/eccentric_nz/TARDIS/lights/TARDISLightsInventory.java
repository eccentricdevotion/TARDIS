package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.LightLevel;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodeldata.GUILights;
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

import java.util.HashMap;
import java.util.List;

public class TARDISLightsInventory {

    private final TARDIS plugin;
    private final int id;
    private final ItemStack[] GUI;

    public TARDISLightsInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
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
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
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
        sim.setCustomModelData(GUILights.LIGHT_INFO.customModelData());
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
            lim.setCustomModelData(tl.getOn().getCustomModelData());
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
        blim.setCustomModelData(GUILights.BLOCK_INFO.customModelData());
        block_info.setItemMeta(blim);
        stacks[GUILights.BLOCK_INFO.slot()] = block_info;
        // 28 block button
        ItemStack block_button = new ItemStack(GUILights.BLOCK_BUTTON.material(), 1);
        ItemMeta bbim = block_button.getItemMeta();
        bbim.setDisplayName("Block Type");
        bbim.setCustomModelData(GUILights.BLOCK_BUTTON.customModelData());
        block_button.setItemMeta(bbim);
        stacks[GUILights.BLOCK_BUTTON.slot()] = block_button;
        // 29 populate with block choice if available
        ItemStack block = new ItemStack(material, 1);
        stacks[29] = block;
        // 35 change lights
        ItemStack chan = new ItemStack(GUILights.CHANGE_LIGHTS.material(), 1);
        ItemMeta geim = chan.getItemMeta();
        geim.setDisplayName("Change Lights");
        geim.setCustomModelData(GUILights.CHANGE_LIGHTS.customModelData());
        chan.setItemMeta(geim);
        stacks[GUILights.CHANGE_LIGHTS.slot()] = chan;
        // 45 light switch
        ItemStack lig = new ItemStack(GUILights.BUTTON_LIGHTS.material(), 1);
        ItemMeta swi = lig.getItemMeta();
        swi.setDisplayName(plugin.getLanguage().getString("BUTTON_LIGHTS"));
        swi.setLore(List.of(lights_onoff));
        int lcmd = GUILights.BUTTON_LIGHTS.customModelData();
        if (lights_onoff.equals(off)) {
            lcmd += 100;
        }
        swi.setCustomModelData(lcmd);
        lig.setItemMeta(swi);
        stacks[GUILights.BUTTON_LIGHTS.slot()] = lig;
        // 47 light levels page
        ItemStack llis = new ItemStack(GUILights.BUTTON_LIGHT_LEVELS.material(), 1);
        ItemMeta llim = llis.getItemMeta();
        llim.setDisplayName("Light Levels");
        llim.setCustomModelData(GUILights.BUTTON_LIGHT_LEVELS.customModelData());
        llis.setItemMeta(llim);
        stacks[GUILights.BUTTON_LIGHT_LEVELS.slot()] = llis;
        // 49 light sequence button
        ItemStack sequence = new ItemStack(GUILights.BUTTON_LIGHT_SEQUENCE.material(), 1);
        ItemMeta qim = sequence.getItemMeta();
        qim.setDisplayName("Run Light Sequence");
        qim.setCustomModelData(GUILights.BUTTON_LIGHT_SEQUENCE.customModelData());
        sequence.setItemMeta(qim);
        stacks[GUILights.BUTTON_LIGHT_SEQUENCE.slot()] = sequence;
        // 51 edit light sequence page?
        ItemStack edit = new ItemStack(GUILights.EDIT_LIGHT_SEQUENCE.material(), 1);
        ItemMeta eim = edit.getItemMeta();
        eim.setDisplayName("Edit Light Sequence");
        eim.setCustomModelData(GUILights.EDIT_LIGHT_SEQUENCE.customModelData());
        edit.setItemMeta(eim);
        stacks[GUILights.EDIT_LIGHT_SEQUENCE.slot()] = edit;
        // close
        ItemStack close = new ItemStack(GUILights.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        clim.setCustomModelData(GUILights.CLOSE.customModelData());
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
