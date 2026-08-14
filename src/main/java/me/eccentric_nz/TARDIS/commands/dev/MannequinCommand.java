package me.eccentric_nz.TARDIS.commands.dev;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.skins.MannequinSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.tardischemistry.product.Product;
import me.eccentric_nz.tardischemistry.product.ProductBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
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
            mannequin.getEquipment().setItemInMainHand(ItemStack.of(TARDISConstants.RANDOM.nextBoolean() ? Material.IRON_SWORD : Material.IRON_SPEAR));
        }
        if (which.equals("balloon")) {
            mannequin.setMainHand(TARDISConstants.RANDOM.nextBoolean() ? MainHand.LEFT : MainHand.RIGHT);
            // make a balloon
            List<Product> balloons = List.of(
                    Product.White_Balloon, Product.Orange_Balloon, Product.Magenta_Balloon, Product.Light_Blue_Balloon,
                    Product.Yellow_Balloon, Product.Lime_Balloon, Product.Pink_Balloon, Product.Gray_Balloon,
                    Product.Light_Gray_Balloon, Product.Cyan_Balloon, Product.Purple_Balloon, Product.Blue_Balloon,
                    Product.Brown_Balloon, Product.Green_Balloon, Product.Red_Balloon, Product.Black_Balloon
            );
            ItemStack balloon = ProductBuilder.getProduct(balloons.get(TARDISConstants.RANDOM.nextInt(16)));
            mannequin.getEquipment().setItemInMainHand(balloon);
        }
    }
}
