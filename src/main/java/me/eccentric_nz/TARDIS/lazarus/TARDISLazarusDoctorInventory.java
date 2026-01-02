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
package me.eccentric_nz.TARDIS.lazarus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.DoctorSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import me.eccentric_nz.TARDIS.skins.tv.PlayerHeadCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class TARDISLazarusDoctorInventory extends LazarusItems implements InventoryHolder, LazarusGUI {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISLazarusDoctorInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Genetic Skins", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        // 16 doctors
        if (PlayerHeadCache.DOCTORS.isEmpty()) {
            for (Skin doctor : DoctorSkins.DOCTORS) {
                ItemStack is = ItemStack.of(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                SkinUtils.getHeadProfile(doctor).thenAccept(playerProfile -> {
                    is.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(playerProfile));
                    String[] name = doctor.name().split(" - ");
                    im.displayName(Component.text(name[0]));
                    im.lore(List.of(Component.text(name[1])));
                    im.setPlayerProfile(playerProfile);
                    is.setItemMeta(im);
                    // cache the item stack
                    PlayerHeadCache.DOCTORS.add(is);
                });
                stacks[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.DOCTORS) {
                stacks[i] = is;
                i++;
            }
        }
        // add standard buttons
        addItems(plugin, stacks, 5);
        return stacks;
    }

    @Override
    public int getMaxSlot() {
        return DoctorSkins.DOCTORS.size();
    }
}
