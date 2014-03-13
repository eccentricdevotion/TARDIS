/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The Administrator of Solos is the Earth Empire's civilian overseer for that
 * planet.
 *
 * @author eccentric_nz
 */
public class TARDISPrefsMenuInventory {

    private final TARDIS plugin;
    private final String player;
    private final ItemStack[] menu;

    public TARDISPrefsMenuInventory(TARDIS plugin, String player) {
        this.plugin = plugin;
        this.player = player;
        this.menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    @SuppressWarnings("deprecation")
    private ItemStack[] getItemStack() {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", player);
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
        List<ItemStack> options = new ArrayList<ItemStack>();
        if (rsp.resultSet()) {
            // Autonomous
            ItemStack auto = new ItemStack(TARDISConstants.GUI_ITEMS.get(0), 1);
            ItemMeta a_im = auto.getItemMeta();
            a_im.setDisplayName("Autonomous");
            String a_value = (rsp.isAutoOn()) ? "ON" : "OFF";
            a_im.setLore(Arrays.asList(new String[]{a_value}));
            auto.setItemMeta(a_im);
            options.add(auto);
            // Beacon
            ItemStack beacon = new ItemStack(TARDISConstants.GUI_ITEMS.get(1), 1);
            ItemMeta b_im = beacon.getItemMeta();
            b_im.setDisplayName("Beacon");
            String b_value = (rsp.isBeaconOn()) ? "ON" : "OFF";
            b_im.setLore(Arrays.asList(new String[]{b_value}));
            beacon.setItemMeta(b_im);
            options.add(beacon);
            // DND
            ItemStack dnd = new ItemStack(TARDISConstants.GUI_ITEMS.get(2), 1);
            ItemMeta d_im = dnd.getItemMeta();
            d_im.setDisplayName("Do Not Disturb");
            String d_value = (rsp.isDND()) ? "ON" : "OFF";
            d_im.setLore(Arrays.asList(new String[]{d_value}));
            dnd.setItemMeta(d_im);
            options.add(dnd);
            // eps
            ItemStack eps = new ItemStack(TARDISConstants.GUI_ITEMS.get(3), 1);
            ItemMeta e_im = eps.getItemMeta();
            e_im.setDisplayName("Emergency Program One");
            String e_value = (rsp.isEpsOn()) ? "ON" : "OFF";
            e_im.setLore(Arrays.asList(new String[]{e_value}));
            eps.setItemMeta(e_im);
            options.add(eps);
            // hads
            ItemStack hads = new ItemStack(TARDISConstants.GUI_ITEMS.get(4), 1);
            ItemMeta h_im = hads.getItemMeta();
            h_im.setDisplayName("Hostile Action Displacement System");
            String h_value = (rsp.isHadsOn()) ? "ON" : "OFF";
            h_im.setLore(Arrays.asList(new String[]{h_value}));
            hads.setItemMeta(h_im);
            options.add(hads);
            // minecart
            ItemStack mine = new ItemStack(TARDISConstants.GUI_ITEMS.get(5), 1);
            ItemMeta m_im = mine.getItemMeta();
            m_im.setDisplayName("Minecart Sounds");
            String m_value = (rsp.isMinecartOn()) ? "ON" : "OFF";
            m_im.setLore(Arrays.asList(new String[]{m_value}));
            mine.setItemMeta(m_im);
            options.add(mine);
            // platform
            ItemStack plat = new ItemStack(TARDISConstants.GUI_ITEMS.get(6), 1);
            ItemMeta p_im = plat.getItemMeta();
            p_im.setDisplayName("Safety Platform");
            String p_value = (rsp.isPlatformOn()) ? "ON" : "OFF";
            p_im.setLore(Arrays.asList(new String[]{p_value}));
            plat.setItemMeta(p_im);
            options.add(plat);
            // quotes
            ItemStack quotes = new ItemStack(TARDISConstants.GUI_ITEMS.get(7), 1);
            ItemMeta q_im = quotes.getItemMeta();
            q_im.setDisplayName("Who Quotes");
            String q_value = (rsp.isQuotesOn()) ? "ON" : "OFF";
            q_im.setLore(Arrays.asList(new String[]{q_value}));
            quotes.setItemMeta(q_im);
            options.add(quotes);
            // Renderer
            ItemStack renderer = new ItemStack(TARDISConstants.GUI_ITEMS.get(8), 1);
            ItemMeta r_im = renderer.getItemMeta();
            r_im.setDisplayName("Exterior Rendering Room");
            String i_value = (rsp.isRendererOn()) ? "ON" : "OFF";
            r_im.setLore(Arrays.asList(new String[]{i_value}));
            renderer.setItemMeta(r_im);
            options.add(renderer);
            // sfx
            ItemStack sfx = new ItemStack(TARDISConstants.GUI_ITEMS.get(9), 1);
            ItemMeta s_im = sfx.getItemMeta();
            s_im.setDisplayName("Interior SFX");
            String s_value = (rsp.isSfxOn()) ? "ON" : "OFF";
            s_im.setLore(Arrays.asList(new String[]{s_value}));
            sfx.setItemMeta(s_im);
            options.add(sfx);
            // submarine
            ItemStack sub = new ItemStack(TARDISConstants.GUI_ITEMS.get(10), 1);
            ItemMeta u_im = sub.getItemMeta();
            u_im.setDisplayName("Submarine Mode");
            String u_value = (rsp.isSubmarineOn()) ? "ON" : "OFF";
            u_im.setLore(Arrays.asList(new String[]{u_value}));
            sub.setItemMeta(u_im);
            options.add(sub);
            // texture
            ItemStack tex = new ItemStack(TARDISConstants.GUI_ITEMS.get(11), 1);
            ItemMeta t_im = tex.getItemMeta();
            t_im.setDisplayName("Resource Pack Switching");
            String t_value = (rsp.isTextureOn()) ? "ON" : "OFF";
            t_im.setLore(Arrays.asList(new String[]{t_value}));
            tex.setItemMeta(t_im);
            options.add(tex);
        }
        ItemStack[] stack = new ItemStack[18];
        for (int s = 0; s < 18; s++) {
            if (s < options.size()) {
                stack[s] = options.get(s);
            } else {
                stack[s] = null;
            }
        }
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
