package me.eccentric_nz.TARDIS.skins;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.*;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R2.profile.CraftPlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

public class SkinUtils {

    public static final HashMap<UUID, Skin> SKINNED = new HashMap<>();
    private static final UUID uuid = UUID.fromString("622bb234-0a3e-46d7-9e1d-ed1f03c76011");

    public static PlayerProfile getHeadProfile(Skin skin) {
        GameProfile profile = new GameProfile(uuid, "TARDIS_Skin");
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", skin.value(), skin.signature()));
        PlayerProfile playerProfile = new CraftPlayerProfile(profile);
        PlayerTextures textures = playerProfile.getTextures();
//        PlayerTextures.SkinModel model = (skin.slim()) ? PlayerTextures.SkinModel.SLIM : PlayerTextures.SkinModel.CLASSIC;
        try {
            textures.setSkin(new URL(skin.url()), PlayerTextures.SkinModel.CLASSIC);
        } catch (MalformedURLException e) {
            TARDIS.plugin.debug("Bad URL: " + skin.url());
        }
        return playerProfile;
    }

    public static boolean isAlexSkin(Player player) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        GameProfile profile = sp.getGameProfile();
        String base64 = profile.getProperties().get("textures").iterator().next().value();
        String decodedValue = new String(Base64.getDecoder().decode(base64));
        JsonObject json = JsonParser.parseString(decodedValue).getAsJsonObject();
        JsonObject skinObject = json.getAsJsonObject("textures").getAsJsonObject("SKIN");
        if (!skinObject.has("metadata")) {
            return false;
        }
        if (!skinObject.getAsJsonObject("metadata").has("model")) {
            return false;
        }
        String model = skinObject.getAsJsonObject("metadata").get("model").getAsString();
        return model.equals("slim");
    }

    public static void setSkinModel(Player player, boolean slim) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getGameProfile();
        String base64 = profile.getProperties().get("textures").iterator().next().value();
        String decodedValue = new String(Base64.getDecoder().decode(base64));
        JsonObject json = JsonParser.parseString(decodedValue).getAsJsonObject();
        JsonObject textures = json.getAsJsonObject("textures");
        JsonObject skin = textures.get("SKIN").getAsJsonObject();
        if (slim) {
            JsonObject metadata = new JsonObject();
            metadata.addProperty("model", "slim");
            // add metadata property
            skin.add("metadata", metadata);
        } else {
            skin.remove("metadata");
            // remove metadata object
            // probably don't need to set this when skin is removed as the SkinChanger#remove implementation will reset the textures
        }
        textures.add("SKIN", skin);
        json.add("textures", textures);
        String value = json.toString();
        TARDIS.plugin.debug(value);
        String encoded = Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
        // will the signature change if a new property is added to the textures object?
        String signature = textures.get("signature").getAsString();
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", encoded, signature));
        // set profile back to player
        ProfileChanger.setPlayerProfile(((CraftPlayer) player), profile);
    }

    public static void debug(Player player) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getGameProfile();
        String base64 = profile.getProperties().get("textures").iterator().next().value();
        String decodedValue = new String(Base64.getDecoder().decode(base64));
        TARDIS.plugin.debug(decodedValue);
        /*
        {
          "timestamp" : 1721299105129,
          "profileId" : "01aadb9932ea4e5ba1df50b46d3f82e3",
          "profileName" : "thenosefairy",
          "signatureRequired" : true,
          "textures" : {
            "SKIN" : {
              "url" : "http://textures.minecraft.net/texture/45e729c38eb2872e979652f707ec156da0b9615ee3c87832829d7ca0ebdb7f92",
              "metadata" : {
                "model" : "slim"
              }
            }
          }
        }
        */
    }

    public static void setExtras(Player player, Skin skin) {
        Material material = Material.LEATHER;
        NamespacedKey key;
        switch (skin.name()) {
            case "Ace" -> key = Leather.ACE_PONYTAIL.getKey();
            case "Angel of Liberty" -> {
                key = Leather.ANGEL_OF_LIBERTY_CROWN.getKey();
                // + 5 torch
                ItemStack torch = new ItemStack(Material.TORCH, 1);
                ItemMeta tim = torch.getItemMeta();
                tim.setDisplayName("Liberty Torch");
                tim.setItemModel(Torch.ANGEL_OF_LIBERTY_TORCH.getKey());
                torch.setItemMeta(tim);
                setOrSwapItem(torch, player, EquipmentSlot.HAND);
            }
            case "Bannakaffalatta" -> {
                material = Material.NETHER_WART;
                key = NetherWart.BANNAKAFFALATTA_SPIKES.getKey();
            }
            case "Brigadier Lethbridge-Stewart" -> key = Leather.BRIGADIER_LETHBRIDGE_STEWART_HAT.getKey();
            case "Cyberman" -> {
                material = Material.IRON_INGOT;
                key = IronIngot.CYBERMAN_FEATURES.getKey();
                // + 7 weapon
                ItemStack weapon = new ItemStack(material, 1);
                ItemMeta cwim = weapon.getItemMeta();
                cwim.setDisplayName("Cyber Weapon");
                cwim.setItemModel(IronIngot.CYBER_WEAPON.getKey());
                weapon.setItemMeta(cwim);
                setOrSwapItem(weapon, player, EquipmentSlot.HAND);
            }
            case "Cybershade" -> key = Leather.CYBERSHADE_EARS.getKey();
            case "Dalek Sec" -> {
                material = Material.MANGROVE_PROPAGULE;
                key = MangrovePropagule.DALEK_SEC_TENTACLES.getKey();
            }
            case "Empty Child" -> {
                material = Material.SUGAR;
                key = Sugar.EMPTY_CHILD_MASK.getKey();
                // set generic scale
                player.getAttribute(Attribute.SCALE).setBaseValue(0.5d);
            }
            case "Hath" -> {
                material = Material.PUFFERFISH;
                key = Pufferfish.HATH_FEATURES.getKey();
            }
            case "Ice Warrior" -> {
                material = Material.SNOWBALL;
                key = Snowball.ICE_WARRIOR_CREST.getKey();
            }
            case "Impossible Astronaut" -> {
                material = Material.ORANGE_STAINED_GLASS_PANE;
                key = OrangeStainedGlassPane.IMPOSSIBLE_ASTRONAUT_PACK.getKey();
            }
            case "Jenny Flint" -> {
                key = null;
                // 17 off-hand katana
                ItemStack katana = new ItemStack(material, 1);
                ItemMeta kim = katana.getItemMeta();
                kim.setDisplayName("Katana");
                kim.setItemModel(Leather.JENNY_FLINT_KATANA.getKey());
                katana.setItemMeta(kim);
                setOrSwapItem(katana, player, EquipmentSlot.OFF_HAND);
            }
            case "Jo Grant" -> key = Leather.JO_GRANT_HAIR.getKey();
            case "Judoon" -> {
                material = Material.YELLOW_DYE;
                key = YellowDye.JUDOON_SNOUT.getKey();
            }
            case "Martha Jones" -> key = Leather.MARTHA_JONES_HAIR.getKey();
            case "Mire" -> {
                material = Material.NETHERITE_SCRAP;
                key = NetheriteScrap.MIRE_HELMET.getKey();
                // + 7, 8 left, right arms
                ItemStack leftArm = new ItemStack(material, 1);
                ItemMeta laim = leftArm.getItemMeta();
                laim.setDisplayName(skin.name());
                laim.setItemModel(NetheriteScrap.MIRE_LEFT_ARM.getKey());
                leftArm.setItemMeta(laim);
                ItemStack rightArm = new ItemStack(material, 1);
                ItemMeta raim = rightArm.getItemMeta();
                raim.setDisplayName(skin.name());
                laim.setItemModel(NetheriteScrap.MIRE_RIGHT_ARM.getKey());
                rightArm.setItemMeta(raim);
                setOrSwapItem(leftArm, player, EquipmentSlot.OFF_HAND);
                setOrSwapItem(rightArm, player, EquipmentSlot.HAND);
            }
            case "Omega" -> key = Leather.OMEGA_FRILL.getKey();
            case "Ood" -> {
                material = Material.ROTTEN_FLESH;
                key = RottenFlesh.OOD_FEATURES.getKey();
            }
            case "Racnoss" -> key = Leather.RACNOSS_FEATURES.getKey();
            case "Scarecrow" -> {
                material = Material.WHEAT;
                key = Wheat.SCARECROW_EARS.getKey();
            }
            case "Sea Devil" -> {
                material = Material.KELP;
                key = Kelp.SEA_DEVIL_EARS.getKey();
            }
            case "Silence" -> {
                material = Material.END_STONE;
                key = EndStone.SILENCE_SIDE_HEAD.getKey();
            }
            case "Silurian" -> {
                material = Material.FEATHER;
                key = Feather.SILURIAN_CREST.getKey();
            }
            case "Slitheen" -> {
                material = Material.TURTLE_EGG;
                key = TurtleEgg.SLITHEEN_HEAD_SKIN.getKey();
                // + 7, 8 left, right claws
                ItemStack leftClaw = new ItemStack(material, 1);
                ItemMeta lhim = leftClaw.getItemMeta();
                lhim.setDisplayName(skin.name());
                lhim.setItemModel(TurtleEgg.SLITHEEN_CLAW_LEFT.getKey());
                leftClaw.setItemMeta(lhim);
                ItemStack rightClaw = new ItemStack(material, 1);
                ItemMeta rhim = rightClaw.getItemMeta();
                rhim.setDisplayName(skin.name());
                rhim.setItemModel(TurtleEgg.SLITHEEN_CLAW_RIGHT.getKey());
                rightClaw.setItemMeta(rhim);
                setOrSwapItem(leftClaw, player, EquipmentSlot.OFF_HAND);
                setOrSwapItem(rightClaw, player, EquipmentSlot.HAND);
            }
            case "Sontaran" -> {
                material = Material.POTATO;
                key = Potato.SONTARAN_EARS.getKey();
            }
            case "Strax" -> {
                material = Material.POTATO;
                key = Potato.STRAX_EARS.getKey();
            }
            case "Sutekh" -> key = Leather.SUTEKH_FEATURES.getKey();
            case "Sycorax" -> key = Leather.SYCORAX_CAPE.getKey();
            case "Tegan" -> key = Leather.TEGAN_HAT.getKey();
            case "The Beast" -> key = Leather.THE_BEAST_HORNS.getKey();
            case "Vampire of Venice" -> {
                material = Material.COD;
                key = Cod.VAMPIRE_OF_VENICE_FAN.getKey();
            }
            case "Weeping Angel" -> {
                material = Material.BRICK;
                key = Brick.WEEPING_ANGEL_WINGS.getKey();
            }
            case "Zygon" -> {
                material = Material.PAINTING;
                key = Painting.ZYGON_CREST.getKey();
            }
            default -> {
                // return
                return;
            }
        }
        if (!skin.name().equals("Jenny Flint")) {
            ItemStack head = new ItemStack(material, 1);
            ItemMeta im = head.getItemMeta();
            im.setDisplayName(skin.name());
            if (key != null) {
                im.setItemModel(key);
            }
            im.getPersistentDataContainer().set(TARDIS.plugin.getTimeLordUuidKey(), PersistentDataType.BOOLEAN, true);
            head.setItemMeta(im);
            setOrSwapItem(head, player, EquipmentSlot.HEAD);
        }
    }

    public static void setOrSwapItem(ItemStack item, Player player, EquipmentSlot slot) {
        PlayerInventory inventory = player.getInventory();
        ItemStack current = inventory.getItem(slot);
        if (current != null && !current.getType().isAir()) {
            HashMap<Integer, ItemStack> remainder = inventory.addItem(current);
            if (!remainder.isEmpty()) {
                player.getWorld().dropItem(player.getLocation(), current);
            }
        }
        inventory.setItem(slot, item);
        player.updateInventory();
    }

    public static String serializeSkin(Skin skin) {
        Gson gson = new Gson();
        return gson.toJson(skin);
    }

    public static Skin deserializeSkin(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Skin.class);
    }

    public static void removeExtras(Player player, Skin skin) {
        switch (skin.name()) {
            case "Angel of Liberty", "Cyberman" -> {
                // head & main hand
                player.getInventory().setItem(EquipmentSlot.HEAD, null);
                player.getInventory().setItem(EquipmentSlot.HAND, null);
            }
            case "Jenny Flint" -> {
                // off-hand
                player.getInventory().setItem(EquipmentSlot.OFF_HAND, null);
            }
            case "Empty Child" -> {
                // head & reset generic scale
                player.getInventory().setItem(EquipmentSlot.HEAD, null);
                player.getAttribute(Attribute.SCALE).setBaseValue(1.0d);
            }
            case "Mire", "Slitheen" -> {
                // head & both hands
                player.getInventory().setItem(EquipmentSlot.HEAD, null);
                player.getInventory().setItem(EquipmentSlot.HAND, null);
                player.getInventory().setItem(EquipmentSlot.OFF_HAND, null);
            }
            case "Ace", "Bannakaffalatta", "Brigadier Lethbridge-Stewart", "Dalek Sec", "Hath", "Ice Warrior",
                 "Impossible Astronaut", "Jo Grant", "Judoon", "Martha Jones", "Omega", "Ood", "Scarecrow",
                 "Sea Devil", "Silence", "Silurian", "Sontaran", "Strax", "Sutekh", "Sycorax", "Tegan",
                 "The Beast", "Vampire of Venice", "Weeping Angel", "Zygon" -> {
                // just head
                player.getInventory().setItem(EquipmentSlot.HEAD, null);
            }
            default -> {
                // nothing to remove
            }
        }
    }
}
