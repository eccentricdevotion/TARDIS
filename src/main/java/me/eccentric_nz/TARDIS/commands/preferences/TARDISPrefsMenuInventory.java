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
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import org.bukkit.Material;
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
    private final UUID uuid;
    private final ItemStack[] menu;

    public TARDISPrefsMenuInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    @SuppressWarnings("deprecation")
    private ItemStack[] getItemStack() {
        // map
        ItemStack tt = new ItemStack(Material.MAP, 1);
        ItemMeta map = tt.getItemMeta();
        map.setDisplayName("TARDIS Map");
        tt.setItemMeta(map);
        // get player prefs
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
        List<ItemStack> options = new ArrayList<ItemStack>();
        if (rsp.resultSet()) {
            // Autonomous
            ItemStack auto = new ItemStack(Material.DIODE, 1);
            ItemMeta a_im = auto.getItemMeta();
            a_im.setDisplayName("Autonomous");
            String a_value = (rsp.isAutoOn()) ? "ON" : "OFF";
            a_im.setLore(Arrays.asList(a_value));
            auto.setItemMeta(a_im);
            options.add(auto);
            // Beacon
            ItemStack beacon = new ItemStack(Material.DIODE, 1);
            ItemMeta b_im = beacon.getItemMeta();
            b_im.setDisplayName("Beacon");
            String b_value = (rsp.isBeaconOn()) ? "ON" : "OFF";
            b_im.setLore(Arrays.asList(b_value));
            beacon.setItemMeta(b_im);
            options.add(beacon);
            // DND
            ItemStack dnd = new ItemStack(Material.DIODE, 1);
            ItemMeta d_im = dnd.getItemMeta();
            d_im.setDisplayName("Do Not Disturb");
            String d_value = (rsp.isDND()) ? "ON" : "OFF";
            d_im.setLore(Arrays.asList(d_value));
            dnd.setItemMeta(d_im);
            options.add(dnd);
            // eps
            ItemStack eps = new ItemStack(Material.DIODE, 1);
            ItemMeta e_im = eps.getItemMeta();
            e_im.setDisplayName("Emergency Program One");
            String e_value = (rsp.isEpsOn()) ? "ON" : "OFF";
            e_im.setLore(Arrays.asList(e_value));
            eps.setItemMeta(e_im);
            options.add(eps);
            // hads
            ItemStack hads = new ItemStack(Material.DIODE, 1);
            ItemMeta h_im = hads.getItemMeta();
            h_im.setDisplayName("Hostile Action Displacement System");
            String h_value = (rsp.isHadsOn()) ? "ON" : "OFF";
            h_im.setLore(Arrays.asList(h_value));
            hads.setItemMeta(h_im);
            options.add(hads);
            // minecart
            ItemStack mine = new ItemStack(Material.DIODE, 1);
            ItemMeta m_im = mine.getItemMeta();
            m_im.setDisplayName("Minecart Sounds");
            String m_value = (rsp.isMinecartOn()) ? "ON" : "OFF";
            m_im.setLore(Arrays.asList(m_value));
            mine.setItemMeta(m_im);
            options.add(mine);
            // quotes
            ItemStack quotes = new ItemStack(Material.DIODE, 1);
            ItemMeta q_im = quotes.getItemMeta();
            q_im.setDisplayName("Who Quotes");
            String q_value = (rsp.isQuotesOn()) ? "ON" : "OFF";
            q_im.setLore(Arrays.asList(q_value));
            quotes.setItemMeta(q_im);
            options.add(quotes);
            // Renderer
            ItemStack renderer = new ItemStack(Material.DIODE, 1);
            ItemMeta r_im = renderer.getItemMeta();
            r_im.setDisplayName("Exterior Rendering Room");
            String i_value = (rsp.isRendererOn()) ? "ON" : "OFF";
            r_im.setLore(Arrays.asList(i_value));
            renderer.setItemMeta(r_im);
            options.add(renderer);
            // sfx
            ItemStack sfx = new ItemStack(Material.DIODE, 1);
            ItemMeta s_im = sfx.getItemMeta();
            s_im.setDisplayName("Interior SFX");
            String s_value = (rsp.isSfxOn()) ? "ON" : "OFF";
            s_im.setLore(Arrays.asList(s_value));
            sfx.setItemMeta(s_im);
            options.add(sfx);
            // submarine
            ItemStack sub = new ItemStack(Material.DIODE, 1);
            ItemMeta u_im = sub.getItemMeta();
            u_im.setDisplayName("Submarine Mode");
            String u_value = (rsp.isSubmarineOn()) ? "ON" : "OFF";
            u_im.setLore(Arrays.asList(u_value));
            sub.setItemMeta(u_im);
            options.add(sub);
            // texture
            ItemStack tex = new ItemStack(Material.DIODE, 1);
            ItemMeta t_im = tex.getItemMeta();
            t_im.setDisplayName("Resource Pack Switching");
            String t_value = (rsp.isTextureOn()) ? "ON" : "OFF";
            t_im.setLore(Arrays.asList(t_value));
            tex.setItemMeta(t_im);
            options.add(tex);
            // build
            ItemStack anti = new ItemStack(Material.DIODE, 1);
            ItemMeta build = anti.getItemMeta();
            build.setDisplayName("Companion Build");
            String ab_value = (rsp.isBuildOn()) ? "ON" : "OFF";
            build.setLore(Arrays.asList(ab_value));
            anti.setItemMeta(build);
            options.add(anti);
            // wool_lights
            ItemStack wool = new ItemStack(Material.DIODE, 1);
            ItemMeta lights = wool.getItemMeta();
            lights.setDisplayName("Wool For Lights Off");
            String wl_value = (rsp.isWoolLightsOn()) ? "ON" : "OFF";
            lights.setLore(Arrays.asList(wl_value));
            wool.setItemMeta(lights);
            options.add(wool);
            // connected textures
            ItemStack ctm = new ItemStack(Material.DIODE, 1);
            ItemMeta ctm_im = ctm.getItemMeta();
            ctm_im.setDisplayName("Connected Textures");
            String ctm_value = (rsp.isCtmOn()) ? "ON" : "OFF";
            ctm_im.setLore(Arrays.asList(ctm_value));
            ctm.setItemMeta(ctm_im);
            options.add(ctm);
            // preset sign
            ItemStack pre = new ItemStack(Material.DIODE, 1);
            ItemMeta sign = pre.getItemMeta();
            sign.setDisplayName("Preset Sign");
            String pre_value = (rsp.isSignOn()) ? "ON" : "OFF";
            sign.setLore(Arrays.asList(pre_value));
            pre.setItemMeta(sign);
            options.add(pre);
        }
        ItemStack[] stack = new ItemStack[18];
        for (int s = 0; s < 16; s++) {
            if (s < options.size()) {
                stack[s] = options.get(s);
            } else {
                stack[s] = null;
            }
        }
        if (plugin.getServer().getPlayer(uuid).hasPermission("tardis.admin")) {
            stack[16] = tt;
            // admin
            ItemStack ad = new ItemStack(Material.NETHER_STAR, 1);
            ItemMeta min = ad.getItemMeta();
            min.setDisplayName("Admin Menu");
            ad.setItemMeta(min);
            stack[17] = ad;
        } else {
            stack[16] = null;
            stack[17] = tt;
        }
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
