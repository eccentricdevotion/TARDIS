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
package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIParticle;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetParticlePrefs;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * Retro-genitor particles are a type of radiation that can stop Time Lords from regenerating. They can prevent an
 * individual from travelling in time without a time capsule. Proximity to someone who is infused with retro-genitor
 * particles can cause their eyes to turn dark periodically.
 *
 * @author eccentric_nz
 */
public class TARDISParticleInventory {

    private final ItemStack[] GUI;
    private final TARDIS plugin;
    private final String uuid;

    public TARDISParticleInventory(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        GUI = getItemStack();
    }

    /**
     * Constructs an inventory for the Particle Preferences GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        // get particle prefs
        ParticleData data;
        ResultSetParticlePrefs rs = new ResultSetParticlePrefs(plugin);
        if (rs.fromUUID(uuid)) {
            data = rs.getData();
        } else {
            // make a record
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid);
            plugin.getQueryFactory().doSyncInsert("particle_prefs", set);
            data = new ParticleData(ParticleEffect.ASH, ParticleShape.RANDOM, 16, 0, false);
        }
        // shape
        ItemStack shape = new ItemStack(GUIParticle.SHAPE_INFO.getMaterial(), 1);
        ItemMeta sim = shape.getItemMeta();
        sim.setDisplayName("Effect Shape");
        sim.setLore(List.of("Choose a shape", "from the options", "on the right."));
        sim.setCustomModelData(GUIParticle.SHAPE_INFO.getCustomModelData());
        shape.setItemMeta(sim);
        stacks[GUIParticle.SHAPE_INFO.getSlot()] = shape;
        int i = 2;
        // shapes
        for (ParticleShape ps : ParticleShape.values()) {
            Material sm = (data.getShape() == ps) ? Material.LAPIS_ORE : GUIParticle.SHAPE.getMaterial();
            ItemStack pshape = new ItemStack(sm, 1);
            ItemMeta me = pshape.getItemMeta();
            me.setDisplayName(TARDISStringUtils.capitalise(ps.toString()));
            me.setCustomModelData(GUIParticle.SHAPE.getCustomModelData());
            pshape.setItemMeta(me);
            stacks[i] = pshape;
            i++;
        }
        // effect
        ItemStack effect = new ItemStack(GUIParticle.EFFECT_INFO.getMaterial(), 1);
        ItemMeta eim = effect.getItemMeta();
        eim.setDisplayName("Effect Particle");
        eim.setLore(List.of("Choose a particle", "from the options", "on the right."));
        eim.setCustomModelData(GUIParticle.EFFECT_INFO.getCustomModelData());
        effect.setItemMeta(eim);
        stacks[GUIParticle.EFFECT_INFO.getSlot()] = effect;
        i = 11;
        // effects
        for (ParticleEffect pe : ParticleEffect.values()) {
            Material pm = (data.getEffect() == pe) ? Material.REDSTONE_ORE : GUIParticle.EFFECT.getMaterial();
            ItemStack peffect = new ItemStack(pm, 1);
            ItemMeta pim = peffect.getItemMeta();
            pim.setDisplayName(TARDISStringUtils.capitalise(pe.toString()));
            pim.setCustomModelData(GUIParticle.EFFECT.getCustomModelData());
            peffect.setItemMeta(pim);
            stacks[i] = peffect;
            if (i % 9 == 7) {
                i += 4;
            } else {
                i++;
            }
        }
        // toggle on/off
        ItemStack toggle = new ItemStack(GUIParticle.TOGGLE.getMaterial(), 1);
        ItemMeta tim = toggle.getItemMeta();
        tim.setDisplayName("Particles Enabled");
        tim.setLore(List.of(data.isOn() ? "ON" : "OFF"));
        int cmd = (data.isOn()) ? GUIParticle.TOGGLE.getCustomModelData() : 119;
        tim.setCustomModelData(cmd);
        toggle.setItemMeta(tim);
        stacks[GUIParticle.TOGGLE.getSlot()] = toggle;
        // test
        ItemStack test = new ItemStack(GUIParticle.TEST.getMaterial(), 1);
        ItemMeta xim = test.getItemMeta();
        xim.setDisplayName("Test");
        xim.setLore(List.of("Display particles", "around your TARDIS", "with the current settings."));
        xim.setCustomModelData(GUIParticle.TEST.getCustomModelData());
        test.setItemMeta(xim);
        stacks[GUIParticle.TEST.getSlot()] = test;
        // density
        ItemStack density = new ItemStack(GUIParticle.DENSITY.getMaterial(), 1);
        ItemMeta dim = density.getItemMeta();
        dim.setDisplayName("Particle Density");
        dim.setLore(List.of(ChatColor.AQUA + "" + data.getDensity(), "Has no effect", "on some shapes.", "Range: 8 - 32."));
        dim.setCustomModelData(GUIParticle.DENSITY.getCustomModelData());
        density.setItemMeta(dim);
        stacks[GUIParticle.DENSITY.getSlot()] = density;
        // speed
        ItemStack speed = new ItemStack(GUIParticle.SPEED.getMaterial(), 1);
        ItemMeta spim = speed.getItemMeta();
        spim.setDisplayName("Particle Speed");
        spim.setLore(List.of(ChatColor.AQUA + "" + String.format("%.0f", data.getSpeed() * 10), "Range: 0 - 10."));
        spim.setCustomModelData(GUIParticle.SPEED.getCustomModelData());
        speed.setItemMeta(spim);
        stacks[GUIParticle.SPEED.getSlot()] = speed;
        // minus
        ItemStack minus = new ItemStack(GUIParticle.MINUS.getMaterial(), 1);
        ItemMeta mim = minus.getItemMeta();
        mim.setDisplayName(plugin.getLanguage().getString("BUTTON_LESS"));
        mim.setCustomModelData(GUIParticle.MINUS.getCustomModelData());
        minus.setItemMeta(mim);
        stacks[45] = minus;
        stacks[49] = minus;
        // plus
        ItemStack plus = new ItemStack(GUIParticle.PLUS.getMaterial(), 1);
        ItemMeta pim = plus.getItemMeta();
        pim.setDisplayName(plugin.getLanguage().getString("BUTTON_MORE"));
        pim.setCustomModelData(GUIParticle.PLUS.getCustomModelData());
        plus.setItemMeta(pim);
        stacks[47] = plus;
        stacks[51] = plus;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta cim = close.getItemMeta();
        cim.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        cim.setCustomModelData(GUIParticle.CLOSE.getCustomModelData());
        close.setItemMeta(cim);
        stacks[53] = close;
        return stacks;
    }

    public ItemStack[] getGUI() {
        return GUI;
    }
}
