package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.LightLevel;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonTemplate;
import me.eccentric_nz.TARDIS.custommodels.GUILights;
import me.eccentric_nz.TARDIS.custommodels.GUIParticle;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAllLightLevels;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISLightLevelsInventory {

    private final TARDIS plugin;
    private final ItemStack[] GUI;
    private String interior_level = "15";
    private String exterior_level = "15";
    private String console_level = "15";

    public TARDISLightLevelsInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        ResultSetAllLightLevels rs = new ResultSetAllLightLevels(plugin, id);
        if (rs.resultSet()) {
            interior_level = "" + LightLevel.interior_level[rs.getInteriorLevel()];
            exterior_level = "" + LightLevel.exterior_level[rs.getExteriorLevel()];
            console_level = "" + LightLevel.interior_level[rs.getConsoleLevel()];
        }
        this.GUI = getItemStack();
    }

    /**
     * Constructs an inventory for the TARDIS Lights GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // interior info
        ItemStack i_info = new ItemStack(GUILights.INTERIOR_INFO.material(), 1);
        ItemMeta iiim = i_info.getItemMeta();
        iiim.setDisplayName("Interior Lights");
        iiim.setLore(List.of("Set the light level", "of the interior lights"));
        iiim.setItemModel(GUILights.INTERIOR_INFO.key());
        i_info.setItemMeta(iiim);
        // interior
        ItemStack interior = new ItemStack(GUILights.INTERIOR.material(), 1);
        ItemMeta inim = interior.getItemMeta();
        inim.setDisplayName("Interior Lights");
        inim.setLore(List.of(interior_level));
        inim.setItemModel(GUILights.INTERIOR.key());
        interior.setItemMeta(inim);
        // exterior info
        ItemStack e_info = new ItemStack(GUILights.EXTERIOR_INFO.material(), 1);
        ItemMeta eiim = e_info.getItemMeta();
        eiim.setDisplayName("Exterior Lamp");
        eiim.setLore(List.of("Set the light level", "of the exterior lamp"));
        eiim.setItemModel(GUILights.EXTERIOR_INFO.key());
        e_info.setItemMeta(eiim);
        // exterior
        ItemStack exterior = new ItemStack(GUILights.EXTERIOR.material(), 1);
        ItemMeta exim = exterior.getItemMeta();
        exim.setDisplayName("Exterior Lamp");
        exim.setLore(List.of(exterior_level));
        exim.setItemModel(GUILights.EXTERIOR.key());
        exterior.setItemMeta(exim);
        // console info
        ItemStack c_info = new ItemStack(GUILights.CONSOLE_INFO.material(), 1);
        ItemMeta ciim = c_info.getItemMeta();
        ciim.setDisplayName("Console Lamp");
        ciim.setLore(List.of("Set the light level", "of the console lamp"));
        ciim.setItemModel(GUILights.CONSOLE_INFO.key());
        c_info.setItemMeta(ciim);
        // console
        ItemStack console = new ItemStack(GUILights.CONSOLE.material(), 1);
        ItemMeta lamp = console.getItemMeta();
        lamp.setDisplayName("Console Lamp");
        lamp.setLore(List.of(console_level));
        lamp.setItemModel(GUILights.CONSOLE.key());
        console.setItemMeta(lamp);
        // minus
        ItemStack minus = new ItemStack(GUIParticle.MINUS.material(), 1);
        ItemMeta mim = minus.getItemMeta();
        mim.setDisplayName(plugin.getLanguage().getString("BUTTON_LESS"));
        mim.setItemModel(GUIParticle.MINUS.key());
        minus.setItemMeta(mim);
        // plus
        ItemStack plus = new ItemStack(GUIParticle.PLUS.material(), 1);
        ItemMeta pim = plus.getItemMeta();
        pim.setDisplayName(plugin.getLanguage().getString("BUTTON_MORE"));
        pim.setItemModel(GUIParticle.PLUS.key());
        plus.setItemMeta(pim);
        // back button
        ItemStack back = new ItemStack(GUIChameleonTemplate.BACK_HELP.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.setDisplayName(plugin.getChameleonGuis().getString("BACK_HELP"));
        bk.setItemModel(GUIChameleonTemplate.BACK_HELP.key());
        back.setItemMeta(bk);
        // close
        ItemStack close = new ItemStack(GUILights.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        clim.setItemModel(GUILights.CLOSE.key());
        close.setItemMeta(clim);
        return new ItemStack[]{
                null, i_info, null, null, null, null, null, e_info, null,
                minus, interior, plus, null, null, null, minus, exterior, plus,
                null, null, null, null, c_info, null, null, null, null,
                null, null, null, minus, console, plus, null, null, null,
                null, null, null, null, null, null, null, null, null,
                back, null, null, null, null, null, null, null, close
        };
    }

    public ItemStack[] getGUI() {
        return GUI;
    }
}
