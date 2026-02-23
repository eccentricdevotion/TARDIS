package me.eccentric_nz.TARDIS.commands.dev;

import com.google.common.collect.Multimaps;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.bStats.ARSRoomCounts;
import me.eccentric_nz.TARDIS.blueprints.BlueprintRoom;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetGames;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardisweepingangels.equip.MonsterArmour;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class DevelopmentUtility {

    public static void siege(TARDIS plugin, Player player) {
        ItemStack cube = player.getInventory().getItemInMainHand();
        ItemMeta im = cube.getItemMeta();
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, TARDISBlockDisplayItem.SIEGE_CUBE.getCustomModel().getKey());
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Time Lord: eccentric_nz"));
        lore.add(Component.text("ID: 1"));
        im.lore(lore);
        cube.setItemMeta(im);
        // track it
        plugin.getTrackerKeeper().getIsSiegeCube().add(1);
        // track the player as well
        plugin.getTrackerKeeper().getSiegeCarrying().put(player.getUniqueId(), 1);
    }

    public static void pong(TARDIS plugin, Player player) {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            ResultSetGames rsg = new ResultSetGames(plugin);
            if (rsg.fromId(rs.getTardisId())) {
                // get the pong_ids
                List<UUID> uuids = rsg.getPongUUIDs();
                for (Entity e : player.getLocation().getChunk().getEntities()) {
                    if (e instanceof TextDisplay display) {
                        plugin.debug(display.getUniqueId().toString());
                        if (uuids.contains(display.getUniqueId())) {
                            plugin.debug("found!");
                        }
                    }
                }
            }
        }
    }

    public static void pingPong(TARDIS plugin, Player player) {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            if (rs.fromUUID(player.getUniqueId().toString())) {
                ResultSetGames rsg = new ResultSetGames(plugin);
                if (rsg.fromId(rs.getTardisId())) {
                    String playerLocation = rsg.getPlayerLocation();
                    Location tp = TARDISStaticLocationGetters.getLocationFromBukkitString(playerLocation);
                    tp.setYaw(180f);
                    player.teleport(tp);
                }
            }
        }
    }

    public static void movePongDisplay(TARDIS plugin, Player player, double y) {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            ResultSetGames rsg = new ResultSetGames(plugin);
            if (rsg.fromId(rs.getTardisId())) {
                // get the pong_ids
                List<UUID> uuids = rsg.getPongUUIDs();
                for (UUID u : uuids) {
                    Entity e = player.getWorld().getEntity(u);
                    if (e instanceof TextDisplay display) {
                        display.teleport(display.getLocation().add(0, y, 0));
                    }
                }
            }
        }
    }

    public static void giveBlueprints(TARDIS plugin, Player player) {
        String uuid = player.getUniqueId().toString();
        for (BlueprintRoom bpr : BlueprintRoom.values()) {
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid);
            set.put("permission", bpr.getPermission());
            plugin.getQueryFactory().doInsert("blueprint", set);
        }
    }

    public static void dalek(TARDIS plugin, Player player) {
        Location eyeLocation = player.getTargetBlock(null, 16).getLocation();
        eyeLocation.add(0.5d, 1.25d, 0.5d);
        eyeLocation.setYaw(player.getLocation().getYaw() - 180.0f);
        Skeleton skeleton = (Skeleton) eyeLocation.getWorld().spawnEntity(eyeLocation, EntityType.SKELETON);
        EntityEquipment ee = skeleton.getEquipment();
        ItemStack head = ItemStack.of(Material.SLIME_BALL);
        ItemMeta him = head.getItemMeta();
        him.setItemModel(new NamespacedKey(plugin, "dalek_independent_head"));
        EquippableComponent component = him.getEquippable();
        component.setSlot(EquipmentSlot.HEAD);
        component.setAllowedEntities(EntityType.SKELETON);
        him.setEquippable(component);
        head.setItemMeta(him);
        ee.setHelmet(head);
        ItemStack body = ItemStack.of(Material.SLIME_BALL);
        ItemMeta bim = body.getItemMeta();
        bim.setItemModel(new NamespacedKey(plugin, "dalek_body"));
        body.setItemMeta(bim);
        ee.setItemInMainHand(body);
        PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
        skeleton.addPotionEffect(invisibility);
        PotionEffect resistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, false, false);
        skeleton.addPotionEffect(resistance);
    }

    public static void leather(Player player) {
        ItemStack is = ItemStack.of(Material.LEATHER_HORSE_ARMOR);
        LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
        im.setColor(Color.fromRGB(255, 0, 0));
        im.addItemFlags(ItemFlag.values());
        im.setAttributeModifiers(Multimaps.forMap(Map.of()));
        im.setEquippable(null);
        CustomModelDataComponent cmdc = im.getCustomModelDataComponent();
        List<String> strings = List.of("chameleon_tint");
        cmdc.setStrings(strings);
        im.setCustomModelDataComponent(cmdc);
        is.setItemMeta(im);
        player.getInventory().addItem(is);
    }

    public static void listPortals(TARDIS plugin, Player player) {
        // get open portals
        for (Map.Entry<Location, TARDISTeleportLocation> map : plugin.getTrackerKeeper().getPortals().entrySet()) {
            // only portals in police box worlds
            if (map.getKey().getWorld().getName().contains("TARDIS")) {
                continue;
            }
            if (map.getValue().isAbandoned()) {
                continue;
            }
            plugin.getMessenger().message(player, "tardisId => " + map.getValue().getTardisId());
        }
        plugin.getMessenger().message(player, "End of open portal list.");
    }

    public static void listPaintings(TARDIS plugin) {
        Registry<Art> variants = RegistryAccess.registryAccess().getRegistry(RegistryKey.PAINTING_VARIANT);
        for (Art a : variants) {
            try {
                plugin.debug(a + " " + variants.getKey(a).getKey());
            } catch (NoSuchElementException | NoSuchMethodError | NullPointerException ignored) {
            }
        }
    }

    public static void listStats(TARDIS plugin) {
        ARSRoomCounts arsRoomCounts = new ARSRoomCounts(plugin);
        for (Map.Entry<String, Integer> entry : arsRoomCounts.getRoomCounts().entrySet()) {
            plugin.debug(entry.getKey() + ": " + entry.getValue());
        }
        plugin.debug("Median per TARDIS: " + arsRoomCounts.getMedian());
    }

    public static void setArmour(Player player, String m, String s) {
        try {
            me.eccentric_nz.tardisweepingangels.utils.Monster monster = Monster.valueOf(m.toUpperCase(Locale.ROOT));
            EquipmentSlot slot = EquipmentSlot.valueOf(s.toUpperCase(Locale.ROOT));
            if (slot != EquipmentSlot.CHEST && slot != EquipmentSlot.LEGS) {
                return;
            }
            ItemStack a = MonsterArmour.makeEquippable(monster, slot);
            player.getInventory().addItem(a);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
