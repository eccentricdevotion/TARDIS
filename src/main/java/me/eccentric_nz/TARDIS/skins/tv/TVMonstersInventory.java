package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.skins.MonsterSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

public class TVMonstersInventory extends TVGUI {

    private final ItemStack[] menu;

    public TVMonstersInventory() {
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
        if (PlayerHeadCache.MONSTERS.isEmpty()) {
            for (Skin monster : MonsterSkins.MONSTERS) {
                ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                PlayerProfile profile = SkinUtils.getHeadProfile(monster);
                im.setOwnerProfile(profile);
                im.setDisplayName(monster.name());
                is.setItemMeta(im);
                // cache the item stack
                PlayerHeadCache.MONSTERS.add(is);
                stack[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.MONSTERS) {
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
