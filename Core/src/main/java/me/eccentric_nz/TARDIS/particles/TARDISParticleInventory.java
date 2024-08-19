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
            data = new ParticleData(ParticleEffect.ASH, ParticleShape.RANDOM, 16, 0, "WHITE", "STONE", false);
        }
        // shape
        ItemStack shape = new ItemStack(GUIParticle.SHAPE_INFO.material(), 1);
        ItemMeta sim = shape.getItemMeta();
        sim.setDisplayName("Effect Shape");
        sim.setLore(List.of("Choose a shape", "from the options", "on the right."));
        sim.setCustomModelData(GUIParticle.SHAPE_INFO.customModelData());
        shape.setItemMeta(sim);
        stacks[GUIParticle.SHAPE_INFO.slot()] = shape;
        int i = 1;
        // shapes
        for (ParticleShape ps : ParticleShape.values()) {
            Material sm = (data.getShape() == ps) ? Material.LAPIS_ORE : GUIParticle.SHAPE.material();
            ItemStack pshape = new ItemStack(sm, 1);
            ItemMeta me = pshape.getItemMeta();
            me.setDisplayName(TARDISStringUtils.capitalise(ps.toString()));
            me.setCustomModelData(GUIParticle.SHAPE.customModelData());
            pshape.setItemMeta(me);
            stacks[i] = pshape;
            i++;
        }
        // effect
        ItemStack effect = new ItemStack(GUIParticle.EFFECT_INFO.material(), 1);
        ItemMeta eim = effect.getItemMeta();
        eim.setDisplayName("Effect Particle");
        eim.setLore(List.of("Choose a particle", "from the options", "on the right."));
        eim.setCustomModelData(GUIParticle.EFFECT_INFO.customModelData());
        effect.setItemMeta(eim);
        stacks[GUIParticle.EFFECT_INFO.slot()] = effect;
        i = 10;
        // effects
        for (ParticleEffect pe : ParticleEffect.values()) {
            Material pm = (data.getEffect() == pe) ? Material.REDSTONE_ORE : GUIParticle.EFFECT.material();
            ItemStack peffect = new ItemStack(pm, 1);
            ItemMeta pim = peffect.getItemMeta();
            pim.setDisplayName(TARDISStringUtils.capitalise(pe.toString()));
            pim.setCustomModelData(GUIParticle.EFFECT.customModelData());
            peffect.setItemMeta(pim);
            stacks[i] = peffect;
            if (i % 9 == 7) {
                i += 3;
            } else {
                i++;
            }
        }
        // colour info
        ItemStack colour_info = new ItemStack(GUIParticle.COLOUR_INFO.material(), 1);
        ItemMeta ciim = colour_info.getItemMeta();
        ciim.setDisplayName("Effect Colour");
        ciim.setLore(List.of("Only affects DUST", "and EFFECT particles.", "Click below to cycle through", "the 16 Minecraft colours."));
        ciim.setCustomModelData(GUIParticle.COLOUR_INFO.customModelData());
        colour_info.setItemMeta(ciim);
        stacks[GUIParticle.COLOUR_INFO.slot()] = colour_info;
        // colour
        ItemStack colour = new ItemStack(GUIParticle.COLOUR.material(), 1);
        ItemMeta cim = colour.getItemMeta();
        cim.setDisplayName("Particle Colour");
        // set to colour pref
        ChatColor chatColor = ParticleColour.fromColor(data.getColour());
        String col = ParticleColour.toString(chatColor);
        cim.setLore(List.of(chatColor + col));
        cim.setCustomModelData(GUIParticle.COLOUR.customModelData());
        colour.setItemMeta(cim);
        stacks[GUIParticle.COLOUR.slot()] = colour;
        // block info
        ItemStack block_info = new ItemStack(GUIParticle.BLOCK_INFO.material(), 1);
        ItemMeta blim = block_info.getItemMeta();
        blim.setDisplayName("Effect Block");
        blim.setLore(List.of("Only affects BLOCK particles.", "Click below to cycle", "through different blocks."));
        blim.setCustomModelData(GUIParticle.BLOCK_INFO.customModelData());
        block_info.setItemMeta(blim);
        stacks[GUIParticle.BLOCK_INFO.slot()] = block_info;
        // block
        ItemStack block = new ItemStack(GUIParticle.BLOCK.material(), 1);
        ItemMeta bim = block.getItemMeta();
        bim.setDisplayName("Block Type");
        // set to material pref
        bim.setLore(List.of(data.getBlockData().getMaterial().toString()));
        bim.setCustomModelData(GUIParticle.BLOCK.customModelData());
        block.setItemMeta(bim);
        stacks[GUIParticle.BLOCK.slot()] = block;
        // toggle on/off
        ItemStack toggle = new ItemStack(GUIParticle.TOGGLE.material(), 1);
        ItemMeta tim = toggle.getItemMeta();
        tim.setDisplayName("Particles Enabled");
        tim.setLore(List.of(data.isOn() ? "ON" : "OFF"));
        int cmd = (data.isOn()) ? GUIParticle.TOGGLE.customModelData() : 119;
        tim.setCustomModelData(cmd);
        toggle.setItemMeta(tim);
        stacks[GUIParticle.TOGGLE.slot()] = toggle;
        // test
        ItemStack test = new ItemStack(GUIParticle.TEST.material(), 1);
        ItemMeta xim = test.getItemMeta();
        xim.setDisplayName("Test");
        xim.setLore(List.of("Display particles", "around your TARDIS", "with the current settings."));
        xim.setCustomModelData(GUIParticle.TEST.customModelData());
        test.setItemMeta(xim);
        stacks[GUIParticle.TEST.slot()] = test;
        // density
        ItemStack density = new ItemStack(GUIParticle.DENSITY.material(), 1);
        ItemMeta dim = density.getItemMeta();
        dim.setDisplayName("Particle Density");
        dim.setLore(List.of(ChatColor.AQUA + "" + data.getDensity(), "Has no effect", "on some shapes.", "Range: 8 - 32."));
        dim.setCustomModelData(GUIParticle.DENSITY.customModelData());
        density.setItemMeta(dim);
        stacks[GUIParticle.DENSITY.slot()] = density;
        // speed
        ItemStack speed = new ItemStack(GUIParticle.SPEED.material(), 1);
        ItemMeta spim = speed.getItemMeta();
        spim.setDisplayName("Particle Speed");
        spim.setLore(List.of(ChatColor.AQUA + String.format("%.0f", data.getSpeed() * 10), "Range: 0 - 10."));
        spim.setCustomModelData(GUIParticle.SPEED.customModelData());
        speed.setItemMeta(spim);
        stacks[GUIParticle.SPEED.slot()] = speed;
        // minus
        ItemStack minus = new ItemStack(GUIParticle.MINUS.material(), 1);
        ItemMeta mim = minus.getItemMeta();
        mim.setDisplayName(plugin.getLanguage().getString("BUTTON_LESS"));
        mim.setCustomModelData(GUIParticle.MINUS.customModelData());
        minus.setItemMeta(mim);
        stacks[45] = minus;
        stacks[49] = minus;
        // plus
        ItemStack plus = new ItemStack(GUIParticle.PLUS.material(), 1);
        ItemMeta pim = plus.getItemMeta();
        pim.setDisplayName(plugin.getLanguage().getString("BUTTON_MORE"));
        pim.setCustomModelData(GUIParticle.PLUS.customModelData());
        plus.setItemMeta(pim);
        stacks[47] = plus;
        stacks[51] = plus;
        // close
        ItemStack close = new ItemStack(GUIParticle.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        clim.setCustomModelData(GUIParticle.CLOSE.customModelData());
        close.setItemMeta(clim);
        stacks[GUIParticle.CLOSE.slot()] = close;
        return stacks;
    }

    public ItemStack[] getGUI() {
        return GUI;
    }
}
