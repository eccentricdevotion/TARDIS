package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.SystemUpgrade;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TARDISSystemTreeGUI {

    private final TARDIS plugin;
    private final SystemUpgrade sysData;
    private final ItemStack[] inventory;

    public TARDISSystemTreeGUI(TARDIS plugin, SystemUpgrade sysData) {
        this.plugin = plugin;
        this.sysData = sysData;
        inventory = getItemStacks();
    }

    /**
     * Constructs an inventory for the TARDIS Upgrades GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStacks() {
        ItemStack[] stacks = new ItemStack[54];
        for (SystemTree g : SystemTree.values()) {
            if (g.getSlot() != -1) {
                ItemStack is = new ItemStack(g.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                String prefix = (g.getBranch().equals("branch")) ? ChatColor.GOLD + "" + ChatColor.ITALIC : "";
                im.setDisplayName(prefix + g.getName());
                List<String> lore = new ArrayList<>(g.getLore());
                boolean has = sysData.getUpgrades().get(g);
                if (!has) {
                    String cost;
                    if (g.getBranch().equals("branch")) {
                        cost = plugin.getSystemUpgradesConfig().getString("branch");
                    } else {
                        cost = plugin.getSystemUpgradesConfig().getString(g.getBranch() + "." + g.toString().toLowerCase());
                    }
                    lore.add(ChatColor.AQUA + "" + ChatColor.ITALIC + "Cost: " + cost);
                } else if (g != SystemTree.UPGRADE_TREE) {
                    lore.add(ChatColor.GREEN + "" + ChatColor.ITALIC + "Unlocked");
                } else {
                    // add players current Artron level to UPGRADE_TREE
                    lore.add(ChatColor.AQUA + "" + ChatColor.ITALIC + "Artron Level: " + sysData.getArtronLevel());
                }
                im.setLore(lore);
                // does the player have this system upgrade?
                int cmd = (has) ? g.getCustomModelData() + 1000 : g.getCustomModelData();
                im.setCustomModelData(cmd);
                is.setItemMeta(im);
                stacks[g.getSlot()] = is;
            }
        }
        // horizontal
        int[] horizontal = new int[]{10, 12, 14, 15};
        for (int h : horizontal) {
            ItemStack is = new ItemStack(SystemTree.H_LINE.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.RESET + "");
            im.setCustomModelData(SystemTree.H_LINE.getCustomModelData());
            is.setItemMeta(im);
            stacks[h] = is;
        }
        // t_right
        int[] t_right = new int[]{18, 25, 27, 34, 43};
        for (int r : t_right) {
            ItemStack is = new ItemStack(SystemTree.T_RIGHT.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.RESET + "");
            im.setCustomModelData(SystemTree.T_RIGHT.getCustomModelData());
            is.setItemMeta(im);
            stacks[r] = is;
        }
        // t_up
        ItemStack ist = new ItemStack(SystemTree.T_UP.getMaterial(), 1);
        ItemMeta up = ist.getItemMeta();
        up.setDisplayName(ChatColor.RESET + "");
        up.setCustomModelData(SystemTree.T_UP.getCustomModelData());
        ist.setItemMeta(up);
        stacks[11] = ist;
        // down_right
        int[] down_right = new int[]{36, 49, 52};
        for (int d : down_right) {
            ItemStack is = new ItemStack(SystemTree.D_RIGHT.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.RESET + "");
            im.setCustomModelData(SystemTree.D_RIGHT.getCustomModelData());
            is.setItemMeta(im);
            stacks[d] = is;
        }
        // cross
        int[] cross = new int[]{22, 31, 40, 43};
        for (int c : cross) {
            ItemStack cr = new ItemStack(SystemTree.CROSS.getMaterial(), 1);
            ItemMeta oss = cr.getItemMeta();
            oss.setDisplayName(ChatColor.RESET + "");
            oss.setCustomModelData(SystemTree.CROSS.getCustomModelData());
            cr.setItemMeta(oss);
            stacks[c] = cr;
        }
        // background
        ItemStack is = new ItemStack(SystemTree.BLANK.getMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.RESET + "");
        im.setCustomModelData(SystemTree.BLANK.getCustomModelData());
        is.setItemMeta(im);
        stacks[0] = is;
        // close
        ItemStack close = new ItemStack(SystemTree.CLOSE.getMaterial(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(SystemTree.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stacks[8] = close;
        return stacks;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }
}
