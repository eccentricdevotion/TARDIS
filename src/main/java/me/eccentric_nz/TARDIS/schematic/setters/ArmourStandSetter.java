package me.eccentric_nz.TARDIS.schematic.setters;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.papermc.paper.math.Rotations;
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
            // pose
            if (stand.has("rotations")) {
                JsonArray rotations = stand.get("rotations").getAsJsonArray();
                // head
                JsonArray head = rotations.get(0).getAsJsonArray();
                as.setHeadRotations(Rotations.ofDegrees(head.get(0).getAsDouble(), head.get(1).getAsDouble(), head.get(2).getAsDouble()));
                // body
                JsonArray body = rotations.get(1).getAsJsonArray();
                as.setBodyRotations(Rotations.ofDegrees(body.get(0).getAsDouble(), body.get(1).getAsDouble(), body.get(2).getAsDouble()));
                // left arm
                JsonArray leftArm = rotations.get(2).getAsJsonArray();
                as.setLeftArmRotations(Rotations.ofDegrees(leftArm.get(0).getAsDouble(), leftArm.get(1).getAsDouble(), leftArm.get(2).getAsDouble()));
                // right arm
                JsonArray rightArm = rotations.get(3).getAsJsonArray();
                as.setRightArmRotations(Rotations.ofDegrees(rightArm.get(0).getAsDouble(), rightArm.get(1).getAsDouble(), rightArm.get(2).getAsDouble()));
                // left leg
                JsonArray leftLeg = rotations.get(4).getAsJsonArray();
                as.setLeftLegRotations(Rotations.ofDegrees(leftLeg.get(0).getAsDouble(), leftLeg.get(1).getAsDouble(), leftLeg.get(2).getAsDouble()));
                // right leg
                JsonArray rightLeg = rotations.get(5).getAsJsonArray();
                as.setRightLegRotations(Rotations.ofDegrees(rightLeg.get(0).getAsDouble(), rightLeg.get(1).getAsDouble(), rightLeg.get(2).getAsDouble()));
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
            if (stand.has("chest")) {
                JsonObject chest = stand.get("chest").getAsJsonObject();
                ItemStack c = ItemStackSetter.build(chest);
                as.getEquipment().setChestplate(c);
            }
            if (stand.has("leggings")) {
                JsonObject leggings = stand.get("leggings").getAsJsonObject();
                ItemStack l = ItemStackSetter.build(leggings);
                as.getEquipment().setLeggings(l);
            }
            if (stand.has("boots")) {
                JsonObject boots = stand.get("boots").getAsJsonObject();
                ItemStack c = ItemStackSetter.build(boots);
                as.getEquipment().setBoots(c);
            }
            if  (stand.has("mainhand")) {
                JsonObject mainhand = stand.get("mainhand").getAsJsonObject();
                ItemStack c = ItemStackSetter.build(mainhand);
                as.getEquipment().setItemInMainHand(c);
            }
            if (stand.has("offhand")) {
                JsonObject offhand = stand.get("offhand").getAsJsonObject();
                ItemStack c = ItemStackSetter.build(offhand);
                as.getEquipment().setItemInOffHand(c);
            }
        }
    }
}
