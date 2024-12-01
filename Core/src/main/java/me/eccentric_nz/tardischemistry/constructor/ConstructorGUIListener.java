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
package me.eccentric_nz.tardischemistry.constructor;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.HandlesVariant;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.tardischemistry.element.Element;
import me.eccentric_nz.tardischemistry.element.ElementBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConstructorGUIListener extends TARDISMenuListener {

    public ConstructorGUIListener(TARDIS plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onElementMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "Element constructor")) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 26) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        switch (slot) {
            case 5 -> {
                event.setCancelled(true);
                int pminus = getCount(view, 0);
                if (pminus > 0) {
                    setCount(view, pminus - 1, 0);
                    setElement(view);
                }
            }
            case 6 -> {
                event.setCancelled(true);
                int pplus = getCount(view, 0);
                if (pplus < 118) {
                    setCount(view, pplus + 1, 0);
                    setElement(view);
                }
            }
            case 14 -> {
                event.setCancelled(true);
                int nminus = getCount(view, 9);
                if (nminus > 0) {
                    setCount(view, nminus - 1, 9);
                    setElement(view);
                }
            }
            case 15 -> {
                event.setCancelled(true);
                int nplus = getCount(view, 9);
                if (nplus < 177) {
                    setCount(view, nplus + 1, 9);
                    setElement(view);
                }
            }
            case 23 -> {
                event.setCancelled(true);
                int eminus = getCount(view, 18);
                if (eminus > 0) {
                    setCount(view, eminus - 1, 18);
                    setElement(view);
                }
            }
            case 24 -> {
                event.setCancelled(true);
                int eplus = getCount(view, 18);
                if (eplus < 118) {
                    setCount(view, eplus + 1, 18);
                    setElement(view);
                }
            }
            case 17 -> {
                event.setCancelled(true);
                if (view.getItem(17) == null) {
                    return;
                }
                // get clicked ItemStack
                ItemStack choice = view.getItem(17).clone();
                choice.setAmount(event.getClick().equals(ClickType.SHIFT_LEFT) ? 64 : 1);
                // add ItemStack to inventory if there is room
                p.getInventory().addItem(choice);
            }
            default -> event.setCancelled(true);
        }
    }

    private int getCount(InventoryView view, int offset) {
        int oneInt, tenInt = 0, hundredInt = 0;
        ItemStack ones = view.getItem(3 + offset);
        ItemMeta oneMeta = ones.getItemMeta();
        oneInt = Integer.parseInt(oneMeta.getDisplayName());
        ItemStack tens = view.getItem(2 + offset);
        if (tens != null) {
            ItemMeta tenMeta = tens.getItemMeta();
            tenInt = Integer.parseInt(tenMeta.getDisplayName()) * 10;
        }
        ItemStack hundreds = view.getItem(1 + offset);
        if (hundreds != null) {
            ItemMeta hundredMeta = hundreds.getItemMeta();
            hundredInt = Integer.parseInt(hundredMeta.getDisplayName()) * 100;
        }
        return oneInt + tenInt + hundredInt;
    }

    private void setCount(InventoryView view, int amount, int offset) {
        int oneInt = amount % 10;
        int tenInt = (amount / 10) % 10;
        int hundredInt = (amount / 100) % 10;
        ItemStack ones = view.getItem(3 + offset);
        ItemMeta oneMeta = ones.getItemMeta();
        oneMeta.setDisplayName("" + oneInt);
        oneMeta.setItemModel(HandlesVariant.values()[25 + oneInt].getKey());
        ones.setItemMeta(oneMeta);
        ItemStack tens = view.getItem(2 + offset);
        if (tenInt > 0 || tenInt == 0 && hundredInt > 0) {
            if (tens == null) {
                tens = new ItemStack(Material.PAPER, 1);
            }
            ItemMeta tenMeta = tens.getItemMeta();
            tenMeta.setDisplayName("" + tenInt);
            tenMeta.setItemModel(HandlesVariant.values()[25 + tenInt].getKey());
            tens.setItemMeta(tenMeta);
            view.setItem(2 + offset, tens);
        } else {
            view.setItem(2 + offset, null);
        }
        ItemStack hundreds = view.getItem(1 + offset);
        if (hundredInt > 0) {
            if (hundreds == null) {
                hundreds = new ItemStack(Material.PAPER, 1);
            }
            ItemMeta hundredMeta = hundreds.getItemMeta();
            hundredMeta.setDisplayName("" + hundredInt);
            hundredMeta.setItemModel(HandlesVariant.values()[25 + hundredInt].getKey());
            hundreds.setItemMeta(hundredMeta);
            view.setItem(1 + offset, hundreds);
        } else {
            view.setItem(1 + offset, null);
        }
    }

    private void setElement(InventoryView view) {
        int protons = getCount(view, 0);
        int neutrons = getCount(view, 9);
        int electrons = getCount(view, 18);
        for (Element element : Element.values()) {
            if (protons == element.getAtomicNumber() && neutrons == element.getNeutrons() && electrons == element.getAtomicNumber()) {
                ItemStack is = ElementBuilder.getElement(element);
                view.setItem(17, is);
                return;
            }
        }
        view.setItem(17, null);
    }
}
