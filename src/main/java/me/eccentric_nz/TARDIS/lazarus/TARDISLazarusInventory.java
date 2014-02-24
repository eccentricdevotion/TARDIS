/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.lazarus;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The Genetic Manipulation Device was invented by Professor Richard Lazarus.
 * The machine would turn anyone inside decades younger, but the process
 * contained one side effect: genes that evolution rejected and left dormant
 * would be unlocked, transforming the human into a giant skeletal scorpion-like
 * beast that fed off the lifeforce of living creatures.
 *
 * @author eccentric_nz
 */
public class TARDISLazarusInventory {

    private final ItemStack[] terminal;
    LinkedHashMap<String, Short> disguises = new LinkedHashMap<String, Short>();

    public TARDISLazarusInventory() {
        disguises.put("BAT", (short) 65);
        disguises.put("BLAZE", (short) 61);
        disguises.put("CAVE_SPIDER", (short) 59);
        disguises.put("CHICKEN", (short) 93);
        disguises.put("COW", (short) 92);
        disguises.put("CREEPER", (short) 50);
        disguises.put("ENDERMAN", (short) 58);
        disguises.put("HORSE", (short) 100);
        disguises.put("DONKEY", (short) 100);
        disguises.put("MULE", (short) 100);
        disguises.put("SKELETON_HORSE", (short) 100);
        disguises.put("UNDEAD_HORSE", (short) 100);
        disguises.put("MAGMA_CUBE", (short) 62);
        disguises.put("MUSHROOM_COW", (short) 96);
        disguises.put("OCELOT", (short) 98);
        disguises.put("PIG", (short) 90);
        disguises.put("PIG_ZOMBIE", (short) 57);
        disguises.put("SHEEP", (short) 91);
        disguises.put("SILVERFISH", (short) 60);
        disguises.put("SKELETON", (short) 51);
        disguises.put("WITHER_SKELETON", (short) 51);
        disguises.put("SLIME", (short) 55);
        disguises.put("SPIDER", (short) 52);
        disguises.put("SQUID", (short) 94);
        disguises.put("VILLAGER", (short) 120);
        disguises.put("WITCH", (short) 66);
        disguises.put("WOLF", (short) 95);
        disguises.put("ZOMBIE", (short) 54);
        disguises.put("ZOMBIE_VILLAGER", (short) 54);
        this.terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Temporal Locator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] eggs = new ItemStack[54];
        int i = 0;
        for (Map.Entry<String, Short> map : disguises.entrySet()) {
            ItemStack egg = new ItemStack(Material.MONSTER_EGG, 1, map.getValue());
            ItemMeta ime = egg.getItemMeta();
            ime.setDisplayName(map.getKey());
            egg.setItemMeta(ime);
            eggs[i] = egg;
            i++;
        }
        // put iron golem
        ItemStack iron = new ItemStack(Material.IRON_BLOCK, 1);
        ItemMeta golem = iron.getItemMeta();
        golem.setDisplayName("IRON_GOLEM");
        iron.setItemMeta(golem);
        eggs[i] = iron;
        i++;
        // put snowman
        ItemStack snow = new ItemStack(Material.SNOW_BALL, 1);
        ItemMeta man = snow.getItemMeta();
        man.setDisplayName("SNOWMAN");
        snow.setItemMeta(man);
        eggs[i] = snow;
        i++;
        // put wither
        ItemStack wit = new ItemStack(Material.SKULL_ITEM, 1, (byte) 1);
        ItemMeta her = wit.getItemMeta();
        her.setDisplayName("WITHER");
        wit.setItemMeta(her);
        eggs[i] = wit;
        i++;
        // if TARDISWeepingAngels is enabled angels, cybermen and ice warriors will be available
        ItemStack weep = new ItemStack(Material.SMOOTH_BRICK, 1, (short) 2);
        ItemMeta ing = weep.getItemMeta();
        ing.setDisplayName("WEEPING ANGEL");
        weep.setItemMeta(ing);
        eggs[i] = weep;
        i++;
        ItemStack cyber = new ItemStack(Material.IRON_INGOT, 1);
        ItemMeta men = cyber.getItemMeta();
        men.setDisplayName("CYBERMAN");
        cyber.setItemMeta(men);
        eggs[i] = cyber;
        i++;
        ItemStack ice = new ItemStack(Material.ICE, 1);
        ItemMeta war = ice.getItemMeta();
        war.setDisplayName("ICE WARRIOR");
        ice.setItemMeta(war);
        eggs[i] = ice;
        // add options
        ItemStack the = new ItemStack(Material.REDSTONE_COMPARATOR, 1);
        ItemMeta master = the.getItemMeta();
        master.setDisplayName("The Master's reverse polarity button");
        master.setLore(Arrays.asList(new String[]{"OFF"}));
        the.setItemMeta(master);
        eggs[37] = the;
        ItemStack adult = new ItemStack(Material.HOPPER, 1);
        ItemMeta baby = adult.getItemMeta();
        baby.setDisplayName("Age");
        baby.setLore(Arrays.asList(new String[]{"ADULT"}));
        adult.setItemMeta(baby);
        eggs[39] = adult;
        ItemStack typ = new ItemStack(Material.INK_SACK, 1, (byte) 6);
        ItemMeta col = typ.getItemMeta();
        col.setDisplayName("Type/Colour");
        col.setLore(Arrays.asList(new String[]{"WHITE"}));
        typ.setItemMeta(col);
        eggs[41] = typ;
        ItemStack tamed = new ItemStack(Material.LEASH, 1, (byte) 6);
        ItemMeta tf = tamed.getItemMeta();
        tf.setDisplayName("Tamed/Flying/Blazing/Powered/Agressive");
        tf.setLore(Arrays.asList(new String[]{"FALSE"}));
        tamed.setItemMeta(tf);
        eggs[43] = tamed;
        // add buttons
        ItemStack rem = new ItemStack(Material.DAYLIGHT_DETECTOR, 1);
        ItemMeta ove = rem.getItemMeta();
        ove.setDisplayName("Restore my original genetic material");
        rem.setItemMeta(ove);
        eggs[47] = rem;
        // set
        ItemStack s = new ItemStack(Material.ANVIL, 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName("Modify my genetic material");
        s.setItemMeta(sim);
        eggs[49] = s;
        ItemStack can = new ItemStack(Material.OBSIDIAN, 1);
        ItemMeta cel = can.getItemMeta();
        cel.setDisplayName("Cancel");
        can.setItemMeta(cel);
        eggs[51] = can;

        return eggs;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
