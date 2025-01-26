package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LazarusUtils {

    public static final Set<UUID> pagers = new HashSet<>();
    public static final List<String> twaHelmets = List.of("Dalek Head", "Davros Head", "K9 Head", "Toclafane");

    public static void openDoor(Block b) {
        b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
        b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
    }

    public static void twaOff(Player player) {
        UUID uuid = player.getUniqueId();
        if (SkinUtils.SKINNED.containsKey(uuid)) {
            // remove skin
            TARDIS.plugin.getSkinChanger().remove(player);
            Skin skin = SkinUtils.SKINNED.get(uuid);
            SkinUtils.removeExtras(player, skin);
            SkinUtils.SKINNED.remove(uuid);
        } else {
            ItemStack helmet = player.getInventory().getHelmet();
            if (helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasDisplayName()) {
                String metaName = helmet.getItemMeta().getDisplayName();
                if (twaHelmets.contains(metaName)) {
                    TARDIS.plugin.getServer().dispatchCommand(TARDIS.plugin.getConsole(), "twa disguise WEEPING_ANGEL off " + uuid);
                }
            }
        }
    }

    public static Skin skinForSlot(int slot) {
        Skin skin;
        if (slot >= 0 && slot < 16) {
            skin = DoctorSkins.DOCTORS.get(slot);
        } else if (slot >= 16 && slot < 31) {
            skin = CompanionSkins.COMPANIONS.get(slot - 16);
        } else {
            skin = CharacterSkins.LAZARUS_CHARACTERS.get(slot - 31);
        }
        return skin;
    }
}
