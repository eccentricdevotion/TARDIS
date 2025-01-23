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
import me.eccentric_nz.TARDIS.custommodels.GUIParticle;
import me.eccentric_nz.TARDIS.custommodels.keys.ParticleItem;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetParticlePrefs;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
        sim.displayName(Component.text("Effect Shape"));
        sim.lore(List.of(
                Component.text("Choose a shape"),
                Component.text("from the options"),
                Component.text("on the right.")
        ));
        sim.setItemModel(GUIParticle.SHAPE_INFO.key());
        shape.setItemMeta(sim);
        stacks[GUIParticle.SHAPE_INFO.slot()] = shape;
        int i = 1;
        // shapes
        for (ParticleShape ps : ParticleShape.values()) {
            Material sm = (data.getShape() == ps) ? Material.LAPIS_ORE : GUIParticle.SHAPE.material();
            ItemStack pshape = new ItemStack(sm, 1);
            ItemMeta me = pshape.getItemMeta();
            me.displayName(Component.text(TARDISStringUtils.capitalise(ps.toString())));
            me.setItemModel(data.getShape() == ps ? ParticleItem.SHAPE_SELECTED.getKey() : GUIParticle.SHAPE.key());
            pshape.setItemMeta(me);
            stacks[i] = pshape;
            i++;
        }
        // effect
        ItemStack effect = new ItemStack(GUIParticle.EFFECT_INFO.material(), 1);
        ItemMeta eim = effect.getItemMeta();
        eim.displayName(Component.text("Effect Particle"));
        eim.lore(List.of(
                Component.text("Choose a particle"),
                Component.text("from the options"),
                Component.text("on the right.")
        ));
        eim.setItemModel(GUIParticle.EFFECT_INFO.key());
        effect.setItemMeta(eim);
        stacks[GUIParticle.EFFECT_INFO.slot()] = effect;
        i = 10;
        // effects
        for (ParticleEffect pe : ParticleEffect.values()) {
            Material pm = (data.getEffect() == pe) ? Material.REDSTONE_ORE : GUIParticle.EFFECT.material();
            ItemStack peffect = new ItemStack(pm, 1);
            ItemMeta pim = peffect.getItemMeta();
            pim.displayName(Component.text(TARDISStringUtils.capitalise(pe.toString())));
            pim.setItemModel(data.getEffect() == pe ? ParticleItem.EFFECT_SELECTED.getKey() : GUIParticle.EFFECT.key());
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
        ciim.displayName(Component.text("Effect Colour"));
        ciim.lore(List.of(
                Component.text("Only affects DUST"),
                Component.text("and EFFECT particles."),
                Component.text("Click below to cycle through"),
                Component.text("the 16 Minecraft colours.")
        ));
        ciim.setItemModel(GUIParticle.COLOUR_INFO.key());
        colour_info.setItemMeta(ciim);
        stacks[GUIParticle.COLOUR_INFO.slot()] = colour_info;
        // colour
        ItemStack colour = new ItemStack(GUIParticle.COLOUR.material(), 1);
        ItemMeta cim = colour.getItemMeta();
        cim.displayName(Component.text("Particle Colour"));
        // set to colour pref
        NamedTextColor textColor = ParticleColour.fromColor(data.getColour());
        String col = ParticleColour.toString(textColor);
        cim.lore(List.of(Component.text(textColor + col)));
        cim.setItemModel(GUIParticle.COLOUR.key());
        colour.setItemMeta(cim);
        stacks[GUIParticle.COLOUR.slot()] = colour;
        // block info
        ItemStack block_info = new ItemStack(GUIParticle.BLOCK_INFO.material(), 1);
        ItemMeta blim = block_info.getItemMeta();
        blim.displayName(Component.text("Effect Block"));
        blim.lore(List.of(
                Component.text("Only affects BLOCK particles."),
                Component.text("Click below to cycle"),
                Component.text("through different blocks.")
        ));
        blim.setItemModel(GUIParticle.BLOCK_INFO.key());
        block_info.setItemMeta(blim);
        stacks[GUIParticle.BLOCK_INFO.slot()] = block_info;
        // block
        ItemStack block = new ItemStack(GUIParticle.BLOCK.material(), 1);
        ItemMeta bim = block.getItemMeta();
        bim.displayName(Component.text("Block Type"));
        // set to material pref
        bim.lore(List.of(Component.text(data.getBlockData().getMaterial().toString())));
        bim.setItemModel(GUIParticle.BLOCK.key());
        block.setItemMeta(bim);
        stacks[GUIParticle.BLOCK.slot()] = block;
        // toggle on/off
        ItemStack toggle = new ItemStack(GUIParticle.TOGGLE.material(), 1);
        ItemMeta tim = toggle.getItemMeta();
        tim.displayName(Component.text("Particles Enabled"));
        tim.lore(List.of(Component.text(data.isOn() ? "ON" : "OFF")));
        NamespacedKey cmd = (data.isOn()) ? GUIParticle.TOGGLE.key() : SwitchVariant.BUTTON_TOGGLE_OFF.getKey();
        tim.setItemModel(cmd);
        toggle.setItemMeta(tim);
        stacks[GUIParticle.TOGGLE.slot()] = toggle;
        // test
        ItemStack test = new ItemStack(GUIParticle.TEST.material(), 1);
        ItemMeta xim = test.getItemMeta();
        xim.displayName(Component.text("Test"));
        xim.lore(List.of(
                Component.text("Display particles"),
                Component.text("around your TARDIS"),
                Component.text("with the current settings.")
        ));
        xim.setItemModel(GUIParticle.TEST.key());
        test.setItemMeta(xim);
        stacks[GUIParticle.TEST.slot()] = test;
        // density
        ItemStack density = new ItemStack(GUIParticle.DENSITY.material(), 1);
        ItemMeta dim = density.getItemMeta();
        dim.displayName(Component.text("Particle Density"));
        dim.lore(List.of(
                Component.text().color(NamedTextColor.AQUA).append(Component.text(data.getDensity())).build(),
                Component.text("Has no effect"),
                Component.text("on some shapes."),
                Component.text("Range: 8 - 32.")
        ));
        dim.setItemModel(GUIParticle.DENSITY.key());
        density.setItemMeta(dim);
        stacks[GUIParticle.DENSITY.slot()] = density;
        // speed
        ItemStack speed = new ItemStack(GUIParticle.SPEED.material(), 1);
        ItemMeta spim = speed.getItemMeta();
        spim.displayName(Component.text("Particle Speed"));
        spim.lore(List.of(
                Component.text().color(NamedTextColor.AQUA).append(Component.text(String.format("%.0f", data.getSpeed() * 10))).build(),
                Component.text("Range: 0 - 10.")
        ));
        spim.setItemModel(GUIParticle.SPEED.key());
        speed.setItemMeta(spim);
        stacks[GUIParticle.SPEED.slot()] = speed;
        // minus
        ItemStack minus = new ItemStack(GUIParticle.MINUS.material(), 1);
        ItemMeta mim = minus.getItemMeta();
        mim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LESS")));
        mim.setItemModel(GUIParticle.MINUS.key());
        minus.setItemMeta(mim);
        stacks[45] = minus;
        stacks[49] = minus;
        // plus
        ItemStack plus = new ItemStack(GUIParticle.PLUS.material(), 1);
        ItemMeta pim = plus.getItemMeta();
        pim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_MORE")));
        pim.setItemModel(GUIParticle.PLUS.key());
        plus.setItemMeta(pim);
        stacks[47] = plus;
        stacks[51] = plus;
        // close
        ItemStack close = new ItemStack(GUIParticle.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE")));
        clim.setItemModel(GUIParticle.CLOSE.key());
        close.setItemMeta(clim);
        stacks[GUIParticle.CLOSE.slot()] = close;
        return stacks;
    }

    public ItemStack[] getGUI() {
        return GUI;
    }
}
