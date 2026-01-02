package me.eccentric_nz.TARDIS.schematic.setters;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.MannequinSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mannequin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class MannequinSetter {

    public static void setMannequins(JsonArray mannequins, World world, int x, int y, int z) {
        for (int i = 0; i < mannequins.size(); i++) {
            JsonObject mannequin = mannequins.get(i).getAsJsonObject();
            JsonObject rel = mannequin.get("rel_location").getAsJsonObject();
            int mx = rel.get("x").getAsInt();
            int my = rel.get("y").getAsInt();
            int mz = rel.get("z").getAsInt();
            Location ml = new Location(world, x + mx + 0.5d, y + my, z + mz + 0.5d);
            Mannequin m = (Mannequin) world.spawnEntity(ml, EntityType.MANNEQUIN);
            m.setRotation(mannequin.get("rotation").getAsFloat(), 0);
            m.setBodyYaw(mannequin.get("yaw").getAsFloat());
            String which = mannequin.get("type").getAsString();
            m.getPersistentDataContainer().set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.STRING, which);
            Skin skin = MannequinSkins.getByName.getOrDefault(which, MannequinSkins.ROMAN);
            m.setProfile(ResolvableProfile.resolvableProfile().name("").uuid(UUID.randomUUID()).addProperty(new ProfileProperty("textures", skin.value(), skin.signature())).build());
            m.setSilent(true);
            m.setAI(false);
            m.setImmovable(true);
            if (mannequin.has("hand")) {
                m.setMainHand(mannequin.get("hand").getAsString().equals("left") ? MainHand.LEFT : MainHand.RIGHT);
                m.getEquipment().setItemInMainHand(ItemStack.of(mannequin.get("item").getAsString().equals("IRON_SWORD") ? Material.IRON_SWORD : Material.IRON_SPEAR));
            }
        }
    }
}
