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
        // left_down
        ItemStack ld = new ItemStack(SystemTree.LEFT_DOWN.getMaterial(), 1);
        ItemMeta eft = ld.getItemMeta();
        eft.setDisplayName(ChatColor.WHITE + "");
        eft.setCustomModelData(SystemTree.LEFT_DOWN.getCustomModelData());
        ld.setItemMeta(eft);
        stacks[0] = ld;
        // horizontal
        int[] horizontal = new int[]{1, 3, 5, 7};
        for (int h : horizontal) {
            ItemStack is = new ItemStack(SystemTree.H_LINE.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.WHITE + "");
            im.setCustomModelData(SystemTree.H_LINE.getCustomModelData());
            is.setItemMeta(im);
            stacks[h] = is;
        }
        // both_down
        int[] both_down = new int[]{2, 6};
        for (int d : both_down) {
            ItemStack is = new ItemStack(SystemTree.BOTH_DOWN.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.WHITE + "");
            im.setCustomModelData(SystemTree.BOTH_DOWN.getCustomModelData());
            is.setItemMeta(im);
            stacks[d] = is;
        }
        // right_down
        ItemStack rd = new ItemStack(SystemTree.RIGHT_DOWN.getMaterial(), 1);
        ItemMeta own = rd.getItemMeta();
        own.setDisplayName(ChatColor.WHITE + "");
        own.setCustomModelData(SystemTree.RIGHT_DOWN.getCustomModelData());
        rd.setItemMeta(own);
        stacks[8] = rd;
        // background
        ItemStack is = new ItemStack(SystemTree.BLANK.getMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "");
        im.setCustomModelData(SystemTree.BLANK.getCustomModelData());
        is.setItemMeta(im);
        stacks[10] = is;
        // vertical
        ItemStack vert = new ItemStack(SystemTree.VERTICAL.getMaterial(), 1);
        ItemMeta ical = vert.getItemMeta();
        ical.setDisplayName(ChatColor.WHITE + "");
        ical.setCustomModelData(SystemTree.VERTICAL.getCustomModelData());
        vert.setItemMeta(ical);
        stacks[13] = vert;
        // close
        ItemStack close = new ItemStack(SystemTree.CLOSE.getMaterial(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(SystemTree.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stacks[45] = close;
        return stacks;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }
}
