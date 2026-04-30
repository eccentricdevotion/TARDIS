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
package me.eccentric_nz.TARDIS.particles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUIParticle;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetParticlePrefs;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

/**
 * Retro-genitor particles are a type of radiation that can stop Time Lords from regenerating. They can prevent an
 * individual from travelling in time without a time capsule. Proximity to someone who is infused with retro-genitor
 * particles can cause their eyes to turn dark periodically.
 *
 * @author eccentric_nz
 */
public class ParticleInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final String uuid;
    private final Inventory inventory;

    public ParticleInventory(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Particle Preferences", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
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
        ItemStack shape = ItemStack.of(GUIParticle.SHAPE_INFO.material(), 1);
        shape.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Effect Shape"));
        shape.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Choose a shape"),
                Component.text("from the options"),
                Component.text("on the right.")
        )));
        stacks[GUIParticle.SHAPE_INFO.slot()] = shape;
        int i = 1;
        // shapes
        for (ParticleShape ps : ParticleShape.values()) {
            Material sm = (data.getShape() == ps) ? Material.LAPIS_ORE : GUIParticle.SHAPE.material();
            ItemStack pshape = ItemStack.of(sm, 1);
            pshape.setData(DataComponentTypes.CUSTOM_NAME, Component.text(TARDISStringUtils.capitalise(ps.toString())));
            stacks[i] = pshape;
            i++;
        }
        // effect
        ItemStack effect = ItemStack.of(GUIParticle.EFFECT_INFO.material(), 1);
        effect.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Effect Particle"));
        effect.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Choose a particle"),
                Component.text("from the options"),
                Component.text("on the right.")
        )));
        stacks[GUIParticle.EFFECT_INFO.slot()] = effect;
        i = 10;
        // effects
        for (ParticleEffect pe : ParticleEffect.values()) {
            if (pe != ParticleEffect.LEAVES) {
                Material pm = (data.getEffect() == pe) ? Material.REDSTONE_ORE : GUIParticle.EFFECT.material();
                ItemStack peffect = ItemStack.of(pm, 1);
                peffect.setData(DataComponentTypes.CUSTOM_NAME, Component.text(TARDISStringUtils.capitalise(pe.toString())));
                stacks[i] = peffect;
                if (i % 9 == 7) {
                    i += 3;
                } else {
                    i++;
                }
            }
        }
        // leaves effect (special position)
        Material lm = (data.getEffect() == ParticleEffect.LEAVES) ? Material.REDSTONE_ORE : GUIParticle.EFFECT.material();
        ItemStack leaves = ItemStack.of(lm, 1);
        leaves.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Leaves"));
        stacks[27] = leaves;
        // colour info
        ItemStack colour_info = ItemStack.of(GUIParticle.COLOUR_INFO.material(), 1);
        colour_info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Effect Colour"));
        colour_info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Only affects DUST"),
                Component.text("and EFFECT particles."),
                Component.text("Click below to cycle through"),
                Component.text("the 16 Minecraft colours.")
        )));
        stacks[GUIParticle.COLOUR_INFO.slot()] = colour_info;
        // colour
        ItemStack colour = ItemStack.of(GUIParticle.COLOUR.material(), 1);
        colour.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Particle Colour"));
        // set to colour pref
        NamedTextColor chatColor = ParticleColour.fromColor(data.getColour());
        String col = ParticleColour.toString(chatColor);
        colour.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(col, chatColor)).build());
        stacks[GUIParticle.COLOUR.slot()] = colour;
        // block info
        ItemStack block_info = ItemStack.of(GUIParticle.BLOCK_INFO.material(), 1);
        block_info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Effect Block"));
        block_info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Only affects BLOCK particles."),
                Component.text("Click below to cycle"),
                Component.text("through different blocks.")
        )));
        stacks[GUIParticle.BLOCK_INFO.slot()] = block_info;
        // block
        ItemStack block = ItemStack.of(GUIParticle.BLOCK.material(), 1);
        block.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Block Type"));
        // set to material pref
        block.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(data.getBlockData().getMaterial().toString())).build());
        stacks[GUIParticle.BLOCK.slot()] = block;
        // toggle on/off
        ItemStack toggle = ItemStack.of(GUIParticle.TOGGLE.material(), 1);
        toggle.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Particles Enabled"));
        toggle.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(data.isOn() ? "ON" : "OFF")).build());
        toggle.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(data.isOn() ? SwitchVariant.BUTTON_TOGGLE_ON.getFloats() : SwitchVariant.BUTTON_TOGGLE_OFF.getFloats())
                .build());
        stacks[GUIParticle.TOGGLE.slot()] = toggle;
        // test
        ItemStack test = ItemStack.of(GUIParticle.TEST.material(), 1);
        test.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Test"));
        test.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Component.text("Display particles"),
                Component.text("around your TARDIS"),
                Component.text("with the current settings.")
        )));
        stacks[GUIParticle.TEST.slot()] = test;
        // density
        ItemStack density = ItemStack.of(GUIParticle.DENSITY.material(), 1);
        density.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Particle Density"));
        density.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text(data.getDensity(), NamedTextColor.AQUA),
                Component.text("Has no effect"), Component.text("on some shapes."),
                Component.text("Range: 8 - 32.")
        )));
        stacks[GUIParticle.DENSITY.slot()] = density;
        // speed
        ItemStack speed = ItemStack.of(GUIParticle.SPEED.material(), 1);
        speed.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Particle Speed"));
        speed.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text(String.format("%.0f", data.getSpeed() * 10), NamedTextColor.AQUA),
                Component.text("Range: 0 - 10.")
        )));
        stacks[GUIParticle.SPEED.slot()] = speed;
        // minus
        ItemStack minus = ItemStack.of(GUIParticle.MINUS.material(), 1);
        minus.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LESS", "Less")));
        stacks[45] = minus;
        stacks[49] = minus;
        // plus
        ItemStack plus = ItemStack.of(GUIParticle.PLUS.material(), 1);
        plus.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MORE", "More")));
        stacks[47] = plus;
        stacks[51] = plus;
        // close
        stacks[GUIParticle.CLOSE.slot()] = GUIItemFactory.close();
        ;
        return stacks;
    }
}
