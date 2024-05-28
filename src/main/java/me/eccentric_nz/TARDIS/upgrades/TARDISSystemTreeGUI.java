package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUISystemTree;
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
        for (GUISystemTree g : GUISystemTree.values()) {
            if (g.getSlot() != -1) {
                ItemStack is = new ItemStack(g.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                String prefix = (g.getBranch().equals("branch")) ? ChatColor.GOLD + "" : "";
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
                } else if (g != GUISystemTree.UPGRADE_TREE) {
                    lore.add(ChatColor.GOLD + "Unlocked");
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
            ItemStack is = new ItemStack(GUISystemTree.H_LINE.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.RESET + "");
            im.setCustomModelData(GUISystemTree.H_LINE.getCustomModelData());
            is.setItemMeta(im);
            stacks[h] = is;
        }
        // t_right
        int[] t_right = new int[]{18, 22, 25, 27, 31, 34, 40, 43};
        for (int r : t_right) {
            ItemStack is = new ItemStack(GUISystemTree.T_RIGHT.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.RESET + "");
            im.setCustomModelData(GUISystemTree.T_RIGHT.getCustomModelData());
            is.setItemMeta(im);
            stacks[r] = is;
        }
        // t_up
        int[] t_up = new int[]{11, 49};
        for (int t : t_up) {
            ItemStack is = new ItemStack(GUISystemTree.T_UP.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.RESET + "");
            im.setCustomModelData(GUISystemTree.T_UP.getCustomModelData());
            is.setItemMeta(im);
            stacks[t] = is;
        }
        // down_right
        int[] down_right = new int[]{36, 52};
        for (int d : down_right) {
            ItemStack is = new ItemStack(GUISystemTree.D_RIGHT.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(ChatColor.RESET + "");
            im.setCustomModelData(GUISystemTree.D_RIGHT.getCustomModelData());
            is.setItemMeta(im);
            stacks[d] = is;
        }
        // background
        ItemStack is = new ItemStack(GUISystemTree.BLANK.getMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.RESET + "");
        im.setCustomModelData(GUISystemTree.BLANK.getCustomModelData());
        is.setItemMeta(im);
        stacks[0] = is;
        // close
        ItemStack close = new ItemStack(GUISystemTree.CLOSE.getMaterial(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(GUISystemTree.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stacks[8] = close;
        return stacks;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }
}
