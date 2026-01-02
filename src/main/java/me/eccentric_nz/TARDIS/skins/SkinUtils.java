/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.skins;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.CybermanVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.Features;
import me.eccentric_nz.TARDIS.custommodels.keys.MireVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SlitheenVariant;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SkinUtils {

    public static final HashMap<UUID, Skin> SKINNED = new HashMap<>();
    private static final UUID uuid = UUID.fromString("622bb234-0a3e-46d7-9e1d-ed1f03c76011");

    public static CompletableFuture<PlayerProfile> getHeadProfile(Skin skin) {
        ResolvableProfile profile = ResolvableProfile.resolvableProfile()
                .uuid(uuid)
                .name("TARDIS_Skin")
                .addProperty(new ProfileProperty("textures", skin.value(), skin.signature()))
                .build();
        CompletableFuture<PlayerProfile> futureProfile = profile.resolve();
        return futureProfile.thenApply(playerProfile -> {
            playerProfile.getProperties().removeIf(profileProperty -> profileProperty.getName().equals("textures"));
            playerProfile.getProperties().add(new ProfileProperty("textures", skin.value(), skin.signature()));
//        PlayerTextures.SkinModel model = (skin.slim()) ? PlayerTextures.SkinModel.SLIM : PlayerTextures.SkinModel.CLASSIC;
            return playerProfile;
        });
    }

    public static boolean isAlexSkin(Player player) {
        ServerPlayer sp = ((CraftPlayer) player).getHandle();
        GameProfile profile = sp.getGameProfile();
        String base64 = profile.properties().get("textures").iterator().next().value();
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
        String base64 = profile.properties().get("textures").iterator().next().value();
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
        profile.properties().removeAll("textures");
        profile.properties().put("textures", new Property("textures", encoded, signature));
        // set profile back to player
        ProfileChanger.setPlayerProfile(((CraftPlayer) player), profile);
    }

    public static void debug(Player player) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getGameProfile();
        String base64 = profile.properties().get("textures").iterator().next().value();
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
            case "Ace" -> key = Features.ACE_PONYTAIL.getKey();
            case "Angel of Liberty" -> {
                key = Features.ANGEL_OF_LIBERTY_CROWN.getKey();
                // + 5 torch
                ItemStack torch = ItemStack.of(Material.TORCH, 1);
                ItemMeta tim = torch.getItemMeta();
                tim.displayName(Component.text("Liberty Torch"));
                tim.setItemModel(Features.ANGEL_OF_LIBERTY_TORCH.getKey());
                torch.setItemMeta(tim);
                setOrSwapItem(torch, player, EquipmentSlot.HAND);
            }
            case "Bannakaffalatta" -> {
                material = Material.NETHER_WART;
                key = Features.BANNAKAFFALATTA_SPIKES.getKey();
            }
            case "Brigadier Lethbridge-Stewart" -> key = Features.BRIGADIER_LETHBRIDGE_STEWART_HAT.getKey();
            case "Cyberman" -> {
                material = Material.IRON_INGOT;
                key = Features.CYBERMAN_FEATURES.getKey();
                // + 7 weapon
                ItemStack weapon = ItemStack.of(material, 1);
                ItemMeta cwim = weapon.getItemMeta();
                cwim.displayName(Component.text("Cyber Weapon"));
                cwim.setItemModel(CybermanVariant.CYBER_WEAPON.getKey());
                weapon.setItemMeta(cwim);
                setOrSwapItem(weapon, player, EquipmentSlot.HAND);
            }
            case "Wooden Cyberman" -> {
                material = Material.SPRUCE_BUTTON;
                key = Features.WOOD_CYBERMAN_FEATURES.getKey();
                // + weapon
                ItemStack weapon = ItemStack.of(material, 1);
                ItemMeta cwim = weapon.getItemMeta();
                cwim.displayName(Component.text("Wood Cyber Weapon"));
                cwim.setItemModel(CybermanVariant.WOOD_CYBER_WEAPON.getKey());
                weapon.setItemMeta(cwim);
                setOrSwapItem(weapon, player, EquipmentSlot.HAND);
            }
            case "Black Cyberman" -> {
                material = Material.IRON_INGOT;
                key = Features.BLACK_CYBERMAN_FEATURES.getKey();
            }
            case "Invasion Cyberman" -> {
                material = Material.IRON_INGOT;
                key = Features.CYBERMAN_INVASION_FEATURES.getKey();
                // + arm decor
                ItemStack arm = ItemStack.of(material, 1);
                ItemMeta cwim = arm.getItemMeta();
                cwim.displayName(Component.text("Cyber Arm"));
                cwim.setItemModel(CybermanVariant.CYBERMAN_INVASION_ARM.getKey());
                arm.setItemMeta(cwim);
                setOrSwapItem(arm, player, EquipmentSlot.HAND);
                setOrSwapItem(arm, player, EquipmentSlot.OFF_HAND);
            }
            case "Rise of the Cyberman", "Cyber Lord", "Moonbase Cyberman" -> {
                material = Material.IRON_INGOT;
                key = switch (skin.name()) {
                    case "Rise of the Cyberman" -> Features.CYBERMAN_RISE_FEATURES.getKey();
                    case "Moonbase Cyberman" -> Features.CYBERMAN_MOONBASE_FEATURES.getKey();
                    default -> Features.CYBER_LORD_FEATURES.getKey();
                };
                // + arm decor
                ItemStack arm = ItemStack.of(material, 1);
                ItemMeta cwim = arm.getItemMeta();
                cwim.displayName(Component.text("Cyber Arm"));
                cwim.setItemModel(CybermanVariant.CYBERMAN_RISE_ARM.getKey());
                arm.setItemMeta(cwim);
                setOrSwapItem(arm, player, EquipmentSlot.HAND);
                setOrSwapItem(arm, player, EquipmentSlot.OFF_HAND);
            }
            case "Tenth Planet Cyberman" -> {
                material = Material.IRON_INGOT;
                key = Features.CYBERMAN_TENTH_PLANET_FEATURES.getKey();
            }
            case "Earthshock Cyberman" -> {
                material = Material.IRON_INGOT;
                key = Features.CYBERMAN_EARTHSHOCK_FEATURES.getKey();
            }
            case "Cybershade" -> key = Features.CYBERSHADE_EARS.getKey();
            case "Dalek Sec" -> {
                material = Material.MANGROVE_PROPAGULE;
                key = Features.DALEK_SEC_TENTACLES.getKey();
            }
            case "Empty Child" -> {
                material = Material.SUGAR;
                key = Features.EMPTY_CHILD_MASK.getKey();
                // set generic scale
                player.getAttribute(Attribute.SCALE).setBaseValue(0.5d);
            }
            case "Hath" -> {
                material = Material.PUFFERFISH;
                key = Features.HATH_FEATURES.getKey();
            }
            case "Heavenly Host" -> {
                material = Material.GOLD_INGOT;
                key = Features.HEAVENLY_HOST_FEATURES.getKey();
            }
            case "Ice Warrior" -> {
                material = Material.SNOWBALL;
                key = Features.ICE_WARRIOR_CREST.getKey();
            }
            case "Impossible Astronaut" -> {
                material = Material.ORANGE_STAINED_GLASS_PANE;
                key = Features.IMPOSSIBLE_ASTRONAUT_PACK.getKey();
            }
            case "Jenny Flint" -> {
                key = null;
                // 17 off-hand katana
                ItemStack katana = ItemStack.of(material, 1);
                ItemMeta kim = katana.getItemMeta();
                kim.displayName(Component.text("Katana"));
                kim.setItemModel(Features.JENNY_FLINT_KATANA.getKey());
                katana.setItemMeta(kim);
                setOrSwapItem(katana, player, EquipmentSlot.OFF_HAND);
            }
            case "Jo Grant" -> key = Features.JO_GRANT_HAIR.getKey();
            case "Judoon" -> {
                material = Material.YELLOW_DYE;
                key = Features.JUDOON_SNOUT.getKey();
            }
            case "Martha Jones" -> key = Features.MARTHA_JONES_HAIR.getKey();
            case "Melanie Bush" -> {
                key = Features.MELANIE_BUSH_HAIR.getKey();
                ItemStack leftArm = ItemStack.of(material, 1);
                ItemMeta laim = leftArm.getItemMeta();
                laim.displayName(Component.text(skin.name()));
                laim.setItemModel(Features.MELANIE_BUSH_ARM_LEFT.getKey());
                leftArm.setItemMeta(laim);
                ItemStack rightArm = ItemStack.of(material, 1);
                ItemMeta raim = rightArm.getItemMeta();
                raim.displayName(Component.text(skin.name()));
                raim.setItemModel(Features.MELANIE_BUSH_ARM_RIGHT.getKey());
                rightArm.setItemMeta(raim);
                setOrSwapItem(leftArm, player, EquipmentSlot.OFF_HAND);
                setOrSwapItem(rightArm, player, EquipmentSlot.HAND);
            }
            case "Mire" -> {
                material = Material.NETHERITE_SCRAP;
                key = Features.MIRE_HELMET.getKey();
                // + 7, 8 left, right arms
                ItemStack leftArm = ItemStack.of(material, 1);
                ItemMeta laim = leftArm.getItemMeta();
                laim.displayName(Component.text(skin.name()));
                laim.setItemModel(MireVariant.MIRE_LEFT_ARM.getKey());
                leftArm.setItemMeta(laim);
                ItemStack rightArm = ItemStack.of(material, 1);
                ItemMeta raim = rightArm.getItemMeta();
                raim.displayName(Component.text(skin.name()));
                raim.setItemModel(MireVariant.MIRE_RIGHT_ARM.getKey());
                rightArm.setItemMeta(raim);
                setOrSwapItem(leftArm, player, EquipmentSlot.OFF_HAND);
                setOrSwapItem(rightArm, player, EquipmentSlot.HAND);
            }
            case "Nimon" -> {
                material = Material.GOAT_HORN;
                key = Features.NIMON_HORNS.getKey();
            }
            case "Omega" -> key = Features.OMEGA_FRILL.getKey();
            case "Ood" -> {
                material = Material.ROTTEN_FLESH;
                key = Features.OOD_FEATURES.getKey();
            }
            case "Racnoss" -> key = Features.RACNOSS_FEATURES.getKey();
            case "Saturnynian" -> {
                material = Material.COD;
                key = Features.SATURNYNIAN_ARMS.getKey();
            }
            case "Scarecrow" -> {
                material = Material.WHEAT;
                key = Features.SCARECROW_EARS.getKey();
            }
            case "Sea Devil" -> {
                material = Material.KELP;
                key = Features.SEA_DEVIL_EARS.getKey();
            }
            case "Silence" -> {
                material = Material.END_STONE;
                key = Features.SILENCE_SIDE_HEAD.getKey();
            }
            case "Silurian" -> {
                material = Material.FEATHER;
                key = Features.SILURIAN_CREST.getKey();
            }
            case "Slitheen" -> {
                material = Material.TURTLE_EGG;
                key = Features.SLITHEEN_HEAD.getKey();
                // + 7, 8 left, right claws
                ItemStack leftClaw = ItemStack.of(material, 1);
                ItemMeta lhim = leftClaw.getItemMeta();
                lhim.displayName(Component.text(skin.name()));
                lhim.setItemModel(SlitheenVariant.SLITHEEN_CLAW_LEFT.getKey());
                leftClaw.setItemMeta(lhim);
                ItemStack rightClaw = ItemStack.of(material, 1);
                ItemMeta rhim = rightClaw.getItemMeta();
                rhim.displayName(Component.text(skin.name()));
                rhim.setItemModel(SlitheenVariant.SLITHEEN_CLAW_RIGHT.getKey());
                rightClaw.setItemMeta(rhim);
                setOrSwapItem(leftClaw, player, EquipmentSlot.OFF_HAND);
                setOrSwapItem(rightClaw, player, EquipmentSlot.HAND);
            }
            case "Sontaran" -> {
                material = Material.POTATO;
                key = Features.SONTARAN_EARS.getKey();
            }
            case "Strax" -> {
                material = Material.POTATO;
                key = Features.STRAX_EARS.getKey();
            }
            case "Sutekh" -> key = Features.SUTEKH_FEATURES.getKey();
            case "Sycorax" -> key = Features.SYCORAX_CAPE.getKey();
            case "Roman Rory" -> key = Features.ROMAN_RORY_CAPE.getKey();
            case "Tegan" -> key = Features.TEGAN_HAT.getKey();
            case "The Beast" -> key = Features.THE_BEAST_HORNS.getKey();
            case "Vampire of Venice" -> {
                material = Material.COD;
                key = Features.VAMPIRE_OF_VENICE_FAN.getKey();
            }
            case "Weeping Angel" -> {
                material = Material.BRICK;
                key = Features.WEEPING_ANGEL_WINGS.getKey();
            }
            case "Zygon" -> {
                material = Material.PAINTING;
                key = Features.ZYGON_CREST.getKey();
            }
            default -> {
                // return
                return;
            }
        }
        if (!skin.name().equals("Jenny Flint")) {
            ItemStack head = ItemStack.of(material, 1);
            ItemMeta im = head.getItemMeta();
            im.displayName(Component.text(skin.name()));
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
        if (!current.getType().isAir()) {
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
            case "Angel of Liberty", "Cyberman", "Wooden Cyberman" -> {
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
            case "Mire", "Slitheen", "Rise of the Cyberman", "Cyber Lord", "Moonbase Cyberman", "Invasion Cyberman",
                 "Melanie Bush" -> {
                // head & both hands
                player.getInventory().setItem(EquipmentSlot.HEAD, null);
                player.getInventory().setItem(EquipmentSlot.HAND, null);
                player.getInventory().setItem(EquipmentSlot.OFF_HAND, null);
            }
            case "Ace", "Bannakaffalatta", "Brigadier Lethbridge-Stewart", "Black Cyberman", "Tenth Planet Cyberman",
                 "Earthshock Cyberman", "Cybershade", "Dalek Sec", "Hath", "Ice Warrior",
                 "Impossible Astronaut", "Jo Grant", "Judoon", "Martha Jones", "Omega", "Ood", "Racnoss", "Scarecrow",
                 "Saturnynian", "Sea Devil", "Silence", "Silurian", "Sontaran", "Strax", "Sutekh", "Sycorax", "Tegan",
                 "The Beast", "Vampire of Venice", "Weeping Angel", "Zygon", "Nimon", "Roamn Rory", "Heavenly Host" -> {
                // just head
                player.getInventory().setItem(EquipmentSlot.HEAD, null);
            }
            default -> {
                // nothing to remove
            }
        }
    }
}
