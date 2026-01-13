package me.eccentric_nz.TARDIS.schematic.setters;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class ArmourStandSetter {

    public static void setStands(JsonArray stands, World world, int x, int y, int z) {
        for (int i = 0; i < stands.size(); i++) {
            JsonObject stand = stands.get(i).getAsJsonObject();
            JsonObject rel = stand.get("rel_location").getAsJsonObject();
            float asx = rel.get("x").getAsFloat();
            float asy = rel.get("y").getAsFloat();
            float asz = rel.get("z").getAsFloat();
            COMPASS facing = COMPASS.valueOf(BlockFace.valueOf(stand.get("facing").getAsString()).getOppositeFace().toString());
            Location asl = new Location(world, x + asx, y + asy, z + asz);
            ArmorStand as = (ArmorStand) world.spawnEntity(asl, EntityType.ARMOR_STAND);
            as.setRotation(facing.getYaw(), 0);
            if (stand.has("invisible")) {
                as.setVisible(stand.get("invisible").getAsBoolean());
            }
            if (stand.has("gravity")) {
                as.setGravity(stand.get("gravity").getAsBoolean());
            }
            if (stand.has("head")) {
                JsonObject head = stand.get("head").getAsJsonObject();
                Material material = Material.valueOf(head.get("material").getAsString());
                ItemStack is = ItemStack.of(material);
                ItemMeta im = is.getItemMeta();
                if (head.has("model")) {
                    NamespacedKey nsk = NamespacedKey.fromString(head.get("model").getAsString());
                    im.setItemModel(nsk);
                }
                if (head.has("skull")) {
                    SkullMeta skull = (SkullMeta) im;
                    PlayerProfile skullProfile = TARDIS.plugin.getServer().createProfile(UUID.randomUUID());
                    skullProfile.setProperty(new ProfileProperty("textures", head.get("skull").getAsString(), null));
                    skull.setPlayerProfile(skullProfile);
                    is.setItemMeta(skull);
                } else {
                    is.setItemMeta(im);
                }
                as.getEquipment().setHelmet(is);
            }
        }
    }
}
