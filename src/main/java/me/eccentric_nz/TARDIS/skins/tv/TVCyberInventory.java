package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.skins.CyberSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

public class TVCyberInventory extends TVGUI {

    private final ItemStack[] menu;

    public TVCyberInventory() {
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Monster Skins GUI.
     *
     * @return an Array of item stacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[36];
        int i = 0;
        if (PlayerHeadCache.CYBERS.isEmpty()) {
            for (Skin variant : CyberSkins.VARIANTS) {
                ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                PlayerProfile profile = SkinUtils.getHeadProfile(variant);
                im.setOwnerProfile(profile);
                im.setDisplayName(variant.name());
                is.setItemMeta(im);
                // cache the item stack
                PlayerHeadCache.CYBERS.add(is);
                stack[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.CYBERS) {
                stack[i] = is;
                i++;
            }
        }
        addDefaults(stack);
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
