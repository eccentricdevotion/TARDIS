package me.eccentric_nz.TARDIS.commands.dev;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.skins.MannequinSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class MannequinCommand {

    public void equip(Player player, String which) {
        Skin skin = MannequinSkins.getByName.getOrDefault(which, MannequinSkins.ROMAN);
        // get the block player is looking at
        Location observerPos = player.getTargetBlock(null, 8).getLocation().add(0.5, 1, 0.5);
        Mannequin mannequin = (Mannequin) observerPos.getWorld().spawnEntity(observerPos, EntityType.MANNEQUIN);
        float yaw = player.getYaw() + 180f;
        mannequin.setRotation(yaw, 0f);
        mannequin.setBodyYaw(yaw);
        mannequin.getPersistentDataContainer().set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.STRING, which);
        mannequin.setProfile(ResolvableProfile.resolvableProfile().name("").uuid(UUID.randomUUID()).addProperty(new ProfileProperty("textures", skin.value(), skin.signature())).build());
        mannequin.setSilent(true);
        mannequin.setAI(false);
        mannequin.setImmovable(true);
        if (which.equals("roman") && TARDISConstants.RANDOM.nextBoolean()) {
            mannequin.setMainHand(TARDISConstants.RANDOM.nextBoolean() ? MainHand.LEFT : MainHand.RIGHT);
            mannequin.getEquipment().setItemInMainHand(new ItemStack(TARDISConstants.RANDOM.nextBoolean() ? Material.IRON_SWORD : Material.IRON_SPEAR));
        }
    }
}
