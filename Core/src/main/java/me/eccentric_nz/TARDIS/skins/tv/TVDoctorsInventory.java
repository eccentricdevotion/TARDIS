package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.skins.DoctorSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.List;

public class TVDoctorsInventory extends TVGUI {

    private final ItemStack[] menu;

    public TVDoctorsInventory() {
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Doctor Skins GUI.
     *
     * @return an Array of item stacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[36];
        int i = 0;
        if (PlayerHeadCache.DOCTORS.isEmpty()) {
            for (Skin doctor : DoctorSkins.DOCTORS) {
                ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                PlayerProfile profile = SkinUtils.getHeadProfile(doctor);
                im.setOwnerProfile(profile);
                String[] name = doctor.name().split(" - ");
                im.setDisplayName(name[0]);
                im.setLore(List.of(name[1]));
                is.setItemMeta(im);
                // cache the item stack
                PlayerHeadCache.DOCTORS.add(is);
                stack[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.DOCTORS) {
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
