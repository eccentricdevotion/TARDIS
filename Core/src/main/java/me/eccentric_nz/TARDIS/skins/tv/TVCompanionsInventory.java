package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUITelevision;
import me.eccentric_nz.TARDIS.skins.CompanionSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

public class TVCompanionsInventory {

    private final ItemStack[] menu;

    public TVCompanionsInventory() {
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Companion Skins GUI.
     *
     * @return an Array of item stacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        int i = 0;
        if (PlayerHeadCache.COMPANIONS.isEmpty()) {
            for (Skin companion : CompanionSkins.COMPANIONS) {
                ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                PlayerProfile profile = SkinUtils.getHeadProfile(companion);
                im.setOwnerProfile(profile);
                im.setDisplayName(companion.name());
                is.setItemMeta(im);
                // cache the item stack
                PlayerHeadCache.COMPANIONS.add(is);
                stack[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.COMPANIONS) {
                stack[i] = is;
                i++;
            }
        }
        // back
        ItemStack back = new ItemStack(GUITelevision.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.setDisplayName("Back");
        but.setCustomModelData(GUITelevision.BACK.customModelData());
        back.setItemMeta(but);
        stack[GUITelevision.BACK.slot()] = back;
        // close
        ItemStack close = new ItemStack(GUITelevision.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(TARDIS.plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(GUITelevision.CLOSE.customModelData());
        close.setItemMeta(close_im);
        stack[GUITelevision.CLOSE.slot()] = close;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
