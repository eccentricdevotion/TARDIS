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
package me.eccentric_nz.TARDIS.commands.dev.wiki;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistrySet;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.*;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.tardisweepingangels.monsters.cybermen.CyberType;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.logging.Level;

public class MonstersBuilder {

    private final TARDIS plugin;

    public MonstersBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void place(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        // fill chests with every monster item
        int chests = ((Monster.values().length * 7) / 27); // account for cybermen variants, ood variants and clockwork droids x2
        Location location = player.getLocation().add(0, 2, 0);
        // place some chests
        for (int i = 0; i < chests; i++) {
            location.getBlock().getRelative(BlockFace.EAST, i).setType(Material.CHEST);
        }
        int count = 0;
        int chestNum = 0;
        Chest chest = (Chest) location.getBlock().getState();
        for (Monster monster : Monster.values()) {
            plugin.getLogger().log(Level.INFO, monster.toString());
            if (count == 27) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
            }
            Material material = monster.getMaterial();
            NamespacedKey headModel = monster.getHeadModel();
            if (headModel != null) {
                // head
                ItemStack head = new ItemStack(material, 1);
                NamespacedKey armour = monster == Monster.OOD ? ArmourVariant.OOD_BLACK.getKey() : monster.getArmourKey();
                // get head variant (CLOCKWORK_DROIDS, CYBERMEN)
                if (monster == Monster.DAVROS) {
                    headModel = DavrosVariant.DAVROS.getKey();
                }
                head.setData(DataComponentTypes.ITEM_MODEL, headModel);
                Equippable.Builder equippable = Equippable.equippable(EquipmentSlot.HEAD);
                equippable.damageOnHurt(false);
                equippable.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                        TypedKey.create(RegistryKey.ENTITY_TYPE, monster.getEntityType().getKey()),
                        TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
                ));
                if (monster.equals(Monster.EMPTY_CHILD)) {
                    equippable.cameraOverlay(EmptyChildVariant.EMPTY_CHILD_OVERLAY.getKey());
                }
                head.setData(DataComponentTypes.EQUIPPABLE, equippable.build());
                String name = switch (monster) {
                    case HEADLESS_MONK -> "Headless Monk Hood";
                    case MIRE -> "Mire Helmet";
                    default -> monster.getName() + " Head";
                };
                head.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(name));
                chest.getBlockInventory().addItem(head);
                // chest
                ItemStack body = ItemStack.of(monster.getMaterial());
                body.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.CHESTPLATE.getKey());
                body.setData(DataComponentTypes.CUSTOM_NAME, Component.text(monster.getName() + " Chestplate"));
                if (armour != null) {
                    body.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                            .addString(armour.value() + "_chestplate")
                            .build()
                    );
                } else {
                    plugin.debug("Armour variant " + monster.getName() + " has no armour");
                }
                Equippable.Builder bodyBuilder = Equippable.equippable(EquipmentSlot.CHEST);
                bodyBuilder.damageOnHurt(false);
                bodyBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                        TypedKey.create(RegistryKey.ENTITY_TYPE, monster.getEntityType().getKey()),
                        TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
                ));
                bodyBuilder.assetId(armour);
                body.setData(DataComponentTypes.EQUIPPABLE, bodyBuilder.build());
                body.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
                chest.getBlockInventory().addItem(body);
                // leggings
                ItemStack legs = ItemStack.of(monster.getMaterial());
                legs.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.LEGGINGS.getKey());
                legs.setData(DataComponentTypes.CUSTOM_NAME, Component.text(monster.getName() + " Leggings"));
                if (armour != null) {
                    legs.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                            .addString(armour.value() + "_leggings")
                            .build()
                    );
                }
                Equippable.Builder legsBuilder = Equippable.equippable(EquipmentSlot.LEGS);
                legsBuilder.damageOnHurt(false);
                legsBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                        TypedKey.create(RegistryKey.ENTITY_TYPE, monster.getEntityType().getKey()),
                        TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
                ));
                legsBuilder.assetId(armour);
                legs.setData(DataComponentTypes.EQUIPPABLE, legsBuilder.build());
                legs.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
                chest.getBlockInventory().addItem(legs);
                count += 3;
            }
        }
        // cyber variants
        for (Map.Entry<NamespacedKey, NamespacedKey> variant : CyberType.CYBER_HEADS.entrySet()) {
            if (variant.getValue().equals(ArmourVariant.CYBERMAN.getKey())) {
                continue;
            }
            if (count == 27) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
            }
            Material material = Monster.CYBERMAN.getMaterial();
            // head
            ItemStack head = new ItemStack(material, 1);
            NamespacedKey armour = variant.getKey();
            head.setData(DataComponentTypes.ITEM_MODEL, variant.getValue());
            Equippable.Builder equippable = Equippable.equippable(EquipmentSlot.HEAD);
            equippable.damageOnHurt(false);
            equippable.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                    TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.CYBERMAN.getEntityType().getKey()),
                    TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
            ));
            head.setData(DataComponentTypes.EQUIPPABLE, equippable.build());
            head.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Cyberman Head"));
            chest.getBlockInventory().addItem(head);
            // chest
            ItemStack body = ItemStack.of(material);
            body.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.CHESTPLATE.getKey());
            body.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Cyberman Chestplate"));
            body.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                    .addString(armour.value() + "_chestplate")
                    .build()
            );
            Equippable.Builder bodyBuilder = Equippable.equippable(EquipmentSlot.CHEST);
            bodyBuilder.damageOnHurt(false);
            bodyBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                    TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.CYBERMAN.getEntityType().getKey()),
                    TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
            ));
            bodyBuilder.assetId(armour);
            body.setData(DataComponentTypes.EQUIPPABLE, bodyBuilder.build());
            body.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
            chest.getBlockInventory().addItem(body);
            // leggings
            ItemStack legs = ItemStack.of(material);
            legs.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.LEGGINGS.getKey());
            legs.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Cyberman Leggings"));
            body.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                    .addString(armour.value() + "_leggings")
                    .build()
            );
            Equippable.Builder legsBuilder = Equippable.equippable(EquipmentSlot.LEGS);
            legsBuilder.damageOnHurt(false);
            legsBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                    TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.CYBERMAN.getEntityType().getKey()),
                    TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
            ));
            legsBuilder.assetId(armour);
            legs.setData(DataComponentTypes.EQUIPPABLE, legsBuilder.build());
            legs.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
            chest.getBlockInventory().addItem(legs);
            count += 3;
            if (count == 27) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
            }
        }
        if (count == 27) {
            // get next chest
            chestNum++;
            count = 0;
            chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
        }
        // female clockwork droid
        Material material = Monster.CLOCKWORK_DROID.getMaterial();
        // head
        ItemStack head = new ItemStack(material, 1);
        NamespacedKey armour = ArmourVariant.CLOCKWORK_DROID_FEMALE.getKey();
        head.setData(DataComponentTypes.ITEM_MODEL, DroidVariant.CLOCKWORK_DROID_FEMALE_HEAD.getKey());
        Equippable.Builder equippable = Equippable.equippable(EquipmentSlot.HEAD);
        equippable.damageOnHurt(false);
        equippable.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.CYBERMAN.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        head.setData(DataComponentTypes.EQUIPPABLE, equippable.build());
        head.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Clockwork Droid Head"));
        chest.getBlockInventory().addItem(head);
        count++;
        if (count == 27) {
            // get next chest
            chestNum++;
            count = 0;
            chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
        }
        // chest
        ItemStack body = ItemStack.of(material);
        body.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.CHESTPLATE.getKey());
        body.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Clockwork Droid Chestplate"));
        body.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addString(armour.value() + "_chestplate")
                .build()
        );
        Equippable.Builder bodyBuilder = Equippable.equippable(EquipmentSlot.CHEST);
        bodyBuilder.damageOnHurt(false);
        bodyBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.CLOCKWORK_DROID.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        bodyBuilder.assetId(armour);
        body.setData(DataComponentTypes.EQUIPPABLE, bodyBuilder.build());
        body.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
        chest.getBlockInventory().addItem(body);
        count++;
        if (count == 27) {
            // get next chest
            chestNum++;
            count = 0;
            chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
        }
        // leggings
        ItemStack legs = ItemStack.of(material);
        legs.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.LEGGINGS.getKey());
        legs.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Clockwork Droid Leggings"));
        legs.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addString(armour.value() + "_leggings")
                .build()
        );
        Equippable.Builder legsBuilder = Equippable.equippable(EquipmentSlot.LEGS);
        legsBuilder.damageOnHurt(false);
        legsBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.CLOCKWORK_DROID.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        legsBuilder.assetId(armour);
        legs.setData(DataComponentTypes.EQUIPPABLE, legsBuilder.build());
        legs.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
        chest.getBlockInventory().addItem(legs);
        count++;
        if (count == 27) {
            // get next chest
            chestNum++;
            count = 0;
            chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
        }
        // ood
        material = Monster.OOD.getMaterial();
        // red eye head
        ItemStack oodHead = new ItemStack(material, 1);
        oodHead.setData(DataComponentTypes.ITEM_MODEL, OodVariant.OOD_REDEYE_HEAD.getKey());
        Equippable.Builder oodEquippable = Equippable.equippable(EquipmentSlot.HEAD);
        oodEquippable.damageOnHurt(false);
        oodEquippable.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.OOD.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        oodHead.setData(DataComponentTypes.EQUIPPABLE, oodEquippable.build());
        oodHead.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Ood Head"));
        chest.getBlockInventory().addItem(oodHead);
        count++;
        if (count == 27) {
            // get next chest
            chestNum++;
            count = 0;
            chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
        }
        // blue chest
        ItemStack blueBody = ItemStack.of(material);
        blueBody.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.CHESTPLATE.getKey());
        blueBody.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Ood Chestplate"));
        body.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addString(ArmourVariant.OOD_BLUE.getKey().value() + "_chestplate")
                .build()
        );
        Equippable.Builder blueBodyBuilder = Equippable.equippable(EquipmentSlot.CHEST);
        blueBodyBuilder.damageOnHurt(false);
        blueBodyBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.OOD.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        blueBodyBuilder.assetId(ArmourVariant.OOD_BLUE.getKey());
        blueBody.setData(DataComponentTypes.EQUIPPABLE, blueBodyBuilder.build());
        blueBody.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
        chest.getBlockInventory().addItem(blueBody);
        count++;
        if (count == 27) {
            // get next chest
            chestNum++;
            count = 0;
            chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
        }
        // blue leggings
        ItemStack blueLegs = ItemStack.of(material);
        blueLegs.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.LEGGINGS.getKey());
        blueLegs.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Ood Leggings"));
        legs.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addString(ArmourVariant.OOD_BLUE.getKey().value() + "_leggings")
                .build()
        );
        Equippable.Builder blueLegsBuilder = Equippable.equippable(EquipmentSlot.LEGS);
        blueLegsBuilder.damageOnHurt(false);
        blueLegsBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.OOD.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        blueLegsBuilder.assetId(ArmourVariant.OOD_BLUE.getKey());
        blueLegs.setData(DataComponentTypes.EQUIPPABLE, blueLegsBuilder.build());
        blueLegs.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
        chest.getBlockInventory().addItem(blueLegs);
        count++;
        if (count == 27) {
            // get next chest
            chestNum++;
            count = 0;
            chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
        }
        // brown chest
        ItemStack brownBody = ItemStack.of(material);
        brownBody.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.CHESTPLATE.getKey());
        brownBody.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Ood Chestplate"));
        body.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addString(ArmourVariant.OOD_BROWN.getKey().value() + "_chestplate")
                .build()
        );
        Equippable.Builder brownBodyBuilder = Equippable.equippable(EquipmentSlot.CHEST);
        brownBodyBuilder.damageOnHurt(false);
        brownBodyBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.OOD.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        brownBodyBuilder.assetId(ArmourVariant.OOD_BROWN.getKey());
        brownBody.setData(DataComponentTypes.EQUIPPABLE, brownBodyBuilder.build());
        brownBody.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
        chest.getBlockInventory().addItem(brownBody);
        count++;
        if (count == 27) {
            // get next chest
            chestNum++;
            chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
        }
        // brown leggings
        ItemStack brownLegs = ItemStack.of(material);
        brownLegs.setData(DataComponentTypes.ITEM_MODEL, ArmourVariant.LEGGINGS.getKey());
        brownLegs.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Ood Leggings"));
        legs.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addString(ArmourVariant.OOD_BROWN.getKey().value() + "_leggings")
                .build()
        );
        Equippable.Builder brownLegsBuilder = Equippable.equippable(EquipmentSlot.LEGS);
        brownLegsBuilder.damageOnHurt(false);
        brownLegsBuilder.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.OOD.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        brownLegsBuilder.assetId(ArmourVariant.OOD_BROWN.getKey());
        brownLegs.setData(DataComponentTypes.EQUIPPABLE, brownLegsBuilder.build());
        brownLegs.editPersistentDataContainer(pdc -> pdc.set(TARDIS.plugin.getHeadBlockKey(), PersistentDataType.INTEGER, 99));
        chest.getBlockInventory().addItem(brownLegs);
        count++;
        if (count == 27) {
            // get next chest
            chestNum++;
            chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
        }
        // k9
        ItemStack k9 = ItemStack.of(Material.BONE);
        k9.setData(DataComponentTypes.CUSTOM_NAME, Component.text("K9 Head"));
        k9.setData(DataComponentTypes.ITEM_MODEL, K9Variant.K9.getKey());
        chest.getBlockInventory().addItem(k9);
        // toclafane
        ItemStack toclafane = ItemStack.of(Material.GUNPOWDER);
        toclafane.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Toclafane"));
        toclafane.setData(DataComponentTypes.ITEM_MODEL, ToclafaneVariant.TOCLAFANE.getKey());
        chest.getBlockInventory().addItem(toclafane);
        // toclafane attack
        ItemStack toclafaneAttack = ItemStack.of(Material.GUNPOWDER);
        toclafaneAttack.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Toclafane"));
        toclafaneAttack.setData(DataComponentTypes.ITEM_MODEL, ToclafaneVariant.TOCLAFANE_ATTACK.getKey());
        chest.getBlockInventory().addItem(toclafaneAttack);
        // weeping angel head
        ItemStack waHead = new ItemStack(Monster.WEEPING_ANGEL.getMaterial(), 1);
        waHead.setData(DataComponentTypes.ITEM_MODEL, WeepingAngelVariant.WEEPING_ANGEL_HEAD.getKey());
        Equippable.Builder waEquippable = Equippable.equippable(EquipmentSlot.HEAD);
        waEquippable.damageOnHurt(false);
        waEquippable.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.WEEPING_ANGEL.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        waHead.setData(DataComponentTypes.EQUIPPABLE, waEquippable.build());
        waHead.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Weeping Angel Head"));
        chest.getBlockInventory().addItem(waHead);
        // saturnynian head
        ItemStack satHead = new ItemStack(Monster.SATURNYNIAN.getMaterial(), 1);
        satHead.setData(DataComponentTypes.ITEM_MODEL, VampireOfVeniceVariant.SATURNYNIAN_HEAD.getKey());
        Equippable.Builder satEquippable = Equippable.equippable(EquipmentSlot.HEAD);
        satEquippable.damageOnHurt(false);
        satEquippable.allowedEntities(RegistrySet.keySet(RegistryKey.ENTITY_TYPE,
                TypedKey.create(RegistryKey.ENTITY_TYPE, Monster.SATURNYNIAN.getEntityType().getKey()),
                TypedKey.create(RegistryKey.ENTITY_TYPE, EntityType.PLAYER.getKey())
        ));
        satHead.setData(DataComponentTypes.EQUIPPABLE, satEquippable.build());
        satHead.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Saturnynian Head"));
        chest.getBlockInventory().addItem(satHead);
        // monster weapons and hands etc
        ItemStack hand;
        ItemStack offhand;
        // ANGEL_OF_LIBERTY torch
        hand = ItemStack.of(Material.TORCH);
        hand.setData(DataComponentTypes.ITEM_MODEL, Features.ANGEL_OF_LIBERTY_TORCH.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        // CLOCKWORK_DROID keys
        hand = ItemStack.of(Material.GOLD_NUGGET);
        hand.setData(DataComponentTypes.ITEM_MODEL, DroidVariant.CLOCKWORK_DROID_KEY.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        offhand = ItemStack.of(Material.GOLD_NUGGET);
        offhand.setData(DataComponentTypes.ITEM_MODEL, DroidVariant.CLOCKWORK_DROID_FEMALE_KEY.getKey());
        chest.getBlockInventory().addItem(offhand.clone());
        // CYBERMAN weapon
        hand = ItemStack.of(Material.IRON_NUGGET);
        hand.setData(DataComponentTypes.ITEM_MODEL, CybermanVariant.CYBER_WEAPON.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        // WOOD_CYBERMAN weapon
        hand = ItemStack.of(Material.SPRUCE_BUTTON);
        hand.setData(DataComponentTypes.ITEM_MODEL, CybermanVariant.WOOD_CYBER_WEAPON.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        // CYBERMAN_INVASION arms
        hand = ItemStack.of(Material.IRON_NUGGET);
        hand.setData(DataComponentTypes.ITEM_MODEL, CybermanVariant.CYBERMAN_INVASION_ARM.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        offhand = ItemStack.of(Material.IRON_NUGGET);
        offhand.setData(DataComponentTypes.ITEM_MODEL, CybermanVariant.CYBERMAN_INVASION_ARM.getKey());
        chest.getBlockInventory().addItem(offhand.clone());
        // CYBER_LORD, CYBERMAN_RISE, CYBERMAN_MOONBASE arms
        hand = ItemStack.of(Material.IRON_NUGGET);
        hand.setData(DataComponentTypes.ITEM_MODEL, CybermanVariant.CYBERMAN_RISE_ARM.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        offhand = ItemStack.of(Material.IRON_NUGGET);
        offhand.setData(DataComponentTypes.ITEM_MODEL, CybermanVariant.CYBERMAN_RISE_ARM.getKey());
        chest.getBlockInventory().addItem(offhand.clone());
        // HATH weapon
        hand = ItemStack.of(Monster.HATH.getMaterial());
        hand.setData(DataComponentTypes.ITEM_MODEL, HathVariant.HATH_WEAPON.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        // HEADLESS_MONK sword
        hand = ItemStack.of(Material.GOLDEN_SWORD);
        hand.setData(DataComponentTypes.ITEM_MODEL, MonkVariant.HEADLESS_MONK_SWORD.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        // ICE_WARRIOR dagger
        hand = ItemStack.of(Material.IRON_SWORD);
        hand.setData(DataComponentTypes.ITEM_MODEL, IceWarriorVariant.ICE_WARRIOR_DAGGER.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        // JUDOON weapon
        hand = ItemStack.of(Material.END_ROD);
        hand.setData(DataComponentTypes.ITEM_MODEL, JudoonVariant.JUDOON_WEAPON_RESTING.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        // MIRE both hands/arms
        hand = ItemStack.of(Material.NETHERITE_SCRAP);
        hand.setData(DataComponentTypes.ITEM_MODEL, MireVariant.MIRE_RIGHT_ARM.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        offhand = ItemStack.of(Material.NETHERITE_SCRAP);
        offhand.setData(DataComponentTypes.ITEM_MODEL, MireVariant.MIRE_LEFT_ARM.getKey());
        chest.getBlockInventory().addItem(offhand.clone());
        // SEA_DEVIL invisible trident
        hand = ItemStack.of(Material.TRIDENT, 1);
        hand.setData(DataComponentTypes.ITEM_MODEL, DalekVariant.DALEK_BOW.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        chestNum++;
        chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
        // SILENT both hands
        hand = ItemStack.of(Material.END_STONE_BRICK_SLAB);
        hand.setData(DataComponentTypes.ITEM_MODEL, SilentVariant.SILENCE_HAND.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        offhand = ItemStack.of(Material.END_STONE_BRICK_SLAB);
        offhand.setData(DataComponentTypes.ITEM_MODEL, SilentVariant.SILENCE_OFFHAND.getKey());
        chest.getBlockInventory().addItem(offhand.clone());
        // SILURIAN gun
        hand = ItemStack.of(Material.BOW, 1);
        hand.setData(DataComponentTypes.ITEM_MODEL, SilurianVariant.SILURIAN_GUN.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        // SLITHEEN both hands
        hand = ItemStack.of(Material.TURTLE_EGG);
        hand.setData(DataComponentTypes.ITEM_MODEL, SlitheenVariant.SLITHEEN_CLAW_RIGHT.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        offhand = ItemStack.of(Material.TURTLE_EGG);
        offhand.setData(DataComponentTypes.ITEM_MODEL, SlitheenVariant.SLITHEEN_CLAW_LEFT.getKey());
        chest.getBlockInventory().addItem(offhand.clone());
        // SONTARAN weapon
        hand = ItemStack.of(Material.END_ROD);
        hand.setData(DataComponentTypes.ITEM_MODEL, SontaranVariant.SONTARAN_WEAPON.getKey());
        chest.getBlockInventory().addItem(hand.clone());
        // dalek variants
        for (DalekVariant variant : DalekVariant.values()) {
            if (variant.getKey().getKey().contains("bow") || variant.getKey().getKey().contains("head")
                    || variant.getKey().getKey().contains("button") || variant.getKey().getKey().contains("overlay")) {
                continue;
            }
            ItemStack dalek = ItemStack.of(Material.SLIME_BALL);
            dalek.setData(DataComponentTypes.ITEM_MODEL, variant.getKey());
            chest.getBlockInventory().addItem(dalek);
        }
    }
}
