package me.eccentric_nz.TARDIS.schematic.getters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ArmourStandGetter {

    public static JsonObject getJson(ArmorStand stand, int minx, int miny, int minz) {
        Location location = stand.getLocation();
        JsonObject object = new JsonObject();
        JsonObject loc = new JsonObject();
        loc.addProperty("x", location.getX() - minx);
        loc.addProperty("y", location.getY() - miny);
        loc.addProperty("z", location.getZ() - minz);
        object.add("rel_location", loc);
        object.addProperty("facing", stand.getFacing().toString());
        object.addProperty("invisible", stand.isVisible());
        object.addProperty("gravity", stand.hasGravity());
        // add rotations - x,y,z degrees Rotations.ofDegrees(x,y,z)
        JsonArray rotations = new JsonArray();
        JsonArray headRotations = new JsonArray();
        headRotations.add(stand.getHeadRotations().x());
        headRotations.add(stand.getHeadRotations().y());
        headRotations.add(stand.getHeadRotations().z());
        rotations.add(headRotations);
        JsonArray bodyRotations = new JsonArray();
        bodyRotations.add(stand.getBodyRotations().x());
        bodyRotations.add(stand.getBodyRotations().y());
        bodyRotations.add(stand.getBodyRotations().z());
        rotations.add(bodyRotations);
        JsonArray leftArmRotations = new JsonArray();
        leftArmRotations.add(stand.getLeftArmRotations().x());
        leftArmRotations.add(stand.getLeftArmRotations().y());
        leftArmRotations.add(stand.getLeftArmRotations().z());
        rotations.add(leftArmRotations);
        JsonArray rightArmRotations = new JsonArray();
        rightArmRotations.add(stand.getRightArmRotations().x());
        rightArmRotations.add(stand.getRightArmRotations().y());
        rightArmRotations.add(stand.getRightArmRotations().z());
        rotations.add(rightArmRotations);
        JsonArray leftLegRotations = new JsonArray();
        leftLegRotations.add(stand.getLeftLegRotations().x());
        leftLegRotations.add(stand.getLeftLegRotations().y());
        leftLegRotations.add(stand.getLeftLegRotations().z());
        rotations.add(leftLegRotations);
        JsonArray rightLegRotations = new JsonArray();
        rightLegRotations.add(stand.getRightLegRotations().x());
        rightLegRotations.add(stand.getRightLegRotations().y());
        rightLegRotations.add(stand.getRightLegRotations().z());
        rotations.add(rightLegRotations);
        object.add("rotations", rotations);
        // get helmet
        JsonObject head = new JsonObject();
        EntityEquipment equipment = stand.getEquipment();
        ItemStack helmet = equipment.getHelmet();
        if (helmet != null && helmet.hasItemMeta()) {
            ItemMeta im = helmet.getItemMeta();
            if (im.hasItemModel()) {
                head.addProperty("model", im.getItemModel().toString());
            }
            if (im instanceof SkullMeta skullMeta) {
                skullMeta.getPlayerProfile().getProperties().stream().findFirst().ifPresent(property -> head.addProperty("skull", property.getValue()));
            }
            head.addProperty("material", helmet.getType().toString());
            object.add("head", head);
        }
        // get chestplate
        ItemStack chestplate = equipment.getChestplate();
        if (chestplate != null) {
            object.add("chest", ItemStackGetter.getJson(chestplate));
        }
        // get leggings
        ItemStack leggings = equipment.getLeggings();
        if (leggings != null) {
            object.add("leggings", ItemStackGetter.getJson(leggings));
        }
        // get boots
        ItemStack boots = equipment.getBoots();
        if (boots != null) {
            object.add("boots", ItemStackGetter.getJson(boots));
        }
        // get main hand
        ItemStack mainhand = equipment.getItemInMainHand();
        object.add("mainhand", ItemStackGetter.getJson(mainhand));
        // get off hand
        ItemStack offhand = equipment.getItemInOffHand();
        object.add("offhand", ItemStackGetter.getJson(offhand));
        return object;
    }
}
