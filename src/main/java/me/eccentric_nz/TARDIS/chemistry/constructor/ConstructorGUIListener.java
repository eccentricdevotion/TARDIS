package me.eccentric_nz.TARDIS.chemistry.constructor;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chemistry.element.Element;
import me.eccentric_nz.TARDIS.chemistry.element.ElementBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConstructorGUIListener implements Listener {

    private final TARDIS plugin;

    public ConstructorGUIListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onElementMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Element constructor")) {
            Player p = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                switch (slot) {
                    case 4:
                        event.setCancelled(true);
                        int pminus = getCount(view, 0);
                        if (pminus > 0) {
                            setCount(view, pminus - 1, 0);
                            setElement(view);
                        }
                        break;
                    case 5:
                        event.setCancelled(true);
                        int pplus = getCount(view, 0);
                        if (pplus < 118) {
                            setCount(view, pplus + 1, 0);
                            setElement(view);
                        }
                        break;
                    case 13:
                        event.setCancelled(true);
                        int nminus = getCount(view, 9);
                        if (nminus > 0) {
                            setCount(view, nminus - 1, 9);
                            setElement(view);
                        }
                        break;
                    case 14:
                        event.setCancelled(true);
                        int nplus = getCount(view, 9);
                        if (nplus < 176) {
                            setCount(view, nplus + 1, 9);
                            setElement(view);
                        }
                        break;
                    case 22:
                        event.setCancelled(true);
                        int eminus = getCount(view, 18);
                        if (eminus > 0) {
                            setCount(view, eminus - 1, 18);
                            setElement(view);
                        }
                        break;
                    case 23:
                        event.setCancelled(true);
                        int eplus = getCount(view, 18);
                        if (eplus < 118) {
                            setCount(view, eplus + 1, 18);
                            setElement(view);
                        }
                        break;
                    case 16:
                        event.setCancelled(true);
                        // get clicked ItemStack
                        ItemStack choice = view.getItem(16).clone();
                        choice.setAmount(event.getClick().equals(ClickType.SHIFT_LEFT) ? 64 : 1);
                        // add ItemStack to inventory if there is room
                        p.getInventory().addItem(choice);
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            } else {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private int getCount(InventoryView view, int offset) {
        int oneInt, tenInt = 0, hundredInt = 0;
        ItemStack ones = view.getItem(2 + offset);
        ItemMeta oneMeta = ones.getItemMeta();
        oneInt = Integer.parseInt(oneMeta.getDisplayName());
        ItemStack tens = view.getItem(1 + offset);
        if (tens != null) {
            ItemMeta tenMeta = tens.getItemMeta();
            tenInt = Integer.parseInt(tenMeta.getDisplayName()) * 10;
        }
        ItemStack hundreds = view.getItem(0 + offset);
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
        ItemStack ones = view.getItem(2 + offset);
        ItemMeta oneMeta = ones.getItemMeta();
        oneMeta.setDisplayName("" + oneInt);
        oneMeta.setCustomModelData(26 + oneInt);
        ones.setItemMeta(oneMeta);
        ItemStack tens = view.getItem(1 + offset);
        if (tenInt > 0) {
            if (tens == null) {
                tens = new ItemStack(Material.PAPER, 1);
            }
            ItemMeta tenMeta = tens.getItemMeta();
            tenMeta.setDisplayName("" + tenInt);
            tenMeta.setCustomModelData(26 + tenInt);
            tens.setItemMeta(tenMeta);
            view.setItem(1 + offset, tens);
        } else {
            view.setItem(1 + offset, null);
        }
        ItemStack hundreds = view.getItem(0 + offset);
        if (hundredInt > 0) {
            if (hundreds == null) {
                hundreds = new ItemStack(Material.PAPER, 1);
            }
            ItemMeta hundredMeta = hundreds.getItemMeta();
            hundredMeta.setDisplayName("" + hundredInt);
            hundredMeta.setCustomModelData(26 + hundredInt);
            hundreds.setItemMeta(hundredMeta);
            view.setItem(0 + offset, hundreds);
        } else {
            view.setItem(0 + offset, null);
        }
    }

    private void setElement(InventoryView view) {
        int protons = getCount(view, 0);
        int neutrons = getCount(view, 9);
        int electrons = getCount(view, 18);
        for (Element element : Element.values()) {
            if (protons == element.getAtomicNumber() && neutrons == element.getNeutrons() && electrons == element.getAtomicNumber()) {
                ItemStack is = ElementBuilder.getElement(element);
                view.setItem(16, is);
                return;
            }
        }
        view.setItem(16, null);
    }
}
