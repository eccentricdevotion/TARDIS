package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.chameleon.utils.PandoricaOpens;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmer;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.mobfarming.TARDISPetsAndFollowers;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicSound;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DoorToggleAction extends TARDISDoorListener {

    public DoorToggleAction(TARDIS plugin) {
        super(plugin);
    }

    public boolean openClose(int id, Player player, ArmorStand stand) {
        EntityEquipment ee = stand.getEquipment();
        ItemStack dye = ee.getHelmet();
        if (dye != null && (TARDISConstants.DYES.contains(dye.getType()) || plugin.getUtils().isCustomModel(dye)) && dye.hasItemMeta()) {
            ItemMeta dim = dye.getItemMeta();
            if (dim.hasItemModel()) {
                String model = dim.getItemModel().getKey();
                if ((model.contains("_open") || model.contains("_closed")) && TARDISPermission.hasPermission(player, "tardis.enter")) {
                    UUID uuid = player.getUniqueId();
                    // get TARDIS from location
                    Location location = stand.getLocation();
                    String doorloc = TARDISStaticLocationGetters.makeLocationStr(location);
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("door_location", doorloc);
                    where.put("door_type", 0);
                    ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                    if (rsd.resultSet()) {
                        boolean closed = model.contains("_closed");
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                            plugin.getMessenger().sendStatus(player, "SIEGE_NO_EXIT");
                            return true;
                        }
                        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
                            return true;
                        }
                        // handbrake must be on
                        HashMap<String, Object> tid = new HashMap<>();
                        tid.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false, 2);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            if (!tardis.isHandbrakeOn()) {
                                plugin.getMessenger().sendStatus(player, "HANDBRAKE_ENGAGE");
                                return true;
                            }
                            // must be Time Lord or companion
                            ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                            if (rsc.getCompanions().contains(uuid) || tardis.isAbandoned()) {
                                if (!rsd.isLocked()) {
                                    if (closed) {
                                        // get key material
                                        ResultSetPlayerPrefs rspref = new ResultSetPlayerPrefs(plugin, uuid.toString());
                                        String key;
                                        boolean willFarm = false;
                                        boolean canPowerUp = false;
                                        if (rspref.resultSet()) {
                                            key = (!rspref.getKey().isEmpty()) ? rspref.getKey() : plugin.getConfig().getString("preferences.key");
                                            willFarm = rspref.isFarmOn();
                                            if (rspref.isAutoPowerUp() && plugin.getConfig().getBoolean("allow.power_down")) {
                                                // check TARDIS is not abandoned
                                                canPowerUp = !tardis.isAbandoned();
                                            }
                                        } else {
                                            key = plugin.getConfig().getString("preferences.key");
                                        }
                                        Material m = Material.valueOf(key);
                                        ItemStack hand = player.getInventory().getItemInMainHand();
                                        if (hand.getType().equals(m) || plugin.getConfig().getBoolean("preferences.any_key")) {
                                            if (player.isSneaking()) {
                                                // tp to the interior
                                                // get INNER TARDIS location
                                                TARDISDoorLocation idl = getDoor(1, id);
                                                Location tardis_loc = idl.getL();
                                                World cw = idl.getW();
                                                COMPASS innerD = idl.getD();
                                                COMPASS d = rsd.getDoor_direction();
                                                COMPASS pd = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                                                World playerWorld = location.getWorld();
                                                // check for entities near the police box
                                                TARDISPetsAndFollowers petsAndFollowers = null;
                                                if (plugin.getConfig().getBoolean("allow.mob_farming") && TARDISPermission.hasPermission(player, "tardis.farm") && !plugin.getTrackerKeeper().getFarming().contains(uuid) && willFarm) {
                                                    plugin.getTrackerKeeper().getFarming().add(uuid);
                                                    TARDISFarmer tf = new TARDISFarmer(plugin);
                                                    petsAndFollowers = tf.farmAnimals(location, d, id, player.getPlayer(), tardis_loc.getWorld().getName(), playerWorld.getName());
                                                }
                                                // if WorldGuard is on the server check for TARDIS region protection and add admin as member
                                                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard") && TARDISPermission.hasPermission(player, "tardis.skeletonkey")) {
                                                    plugin.getWorldGuardUtils().addMemberToRegion(cw, tardis.getOwner(), player.getUniqueId());
                                                }
                                                // enter TARDIS!
                                                cw.getChunkAt(tardis_loc).load();
                                                tardis_loc.setPitch(player.getLocation().getPitch());
                                                // get inner door direction, so we can adjust yaw if necessary
                                                float yaw = player.getLocation().getYaw();
                                                if (!innerD.equals(pd)) {
                                                    yaw += adjustYaw(pd, innerD);
                                                }
                                                tardis_loc.setYaw(yaw);
                                                movePlayer(player, tardis_loc, false, playerWorld, rspref.isQuotesOn(), 1, rspref.isMinecartOn(), false);
                                                if (petsAndFollowers != null) {
                                                    if (!petsAndFollowers.getPets().isEmpty()) {
                                                        movePets(petsAndFollowers.getPets(), tardis_loc, player, d, true);
                                                    }
                                                    if (!petsAndFollowers.getFollowers().isEmpty()) {
                                                        new TARDISFollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), tardis_loc, player, d, true);
                                                    }
                                                }
                                                if (canPowerUp && !tardis.isPoweredOn() && !tardis.isAbandoned()) {
                                                    // power up the TARDIS
                                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISPowerButton(plugin, id, player, tardis.getPreset(), false, tardis.isHidden(), tardis.isLightsOn(), player.getLocation(), tardis.getArtronLevel(), tardis.getSchematic().getLights()).clickButton(), 20L);
                                                }
                                                // put player into travellers table
                                                // remove them first as they may have exited incorrectly, and we only want them listed once
                                                removeTraveller(uuid);
                                                HashMap<String, Object> set = new HashMap<>();
                                                set.put("tardis_id", id);
                                                set.put("uuid", uuid.toString());
                                                plugin.getQueryFactory().doSyncInsert("travellers", set);
                                            } else {
                                                // create portal & open inner door
                                                new TARDISInnerDoorOpener(plugin, uuid, id).openDoor(!plugin.getUtils().inTARDISWorld(player));
                                                if (dye.getType() == Material.ENDER_PEARL) {
                                                    // animate pandorica opening
                                                    new PandoricaOpens(plugin).animate(stand, true);
                                                } else {
                                                    switch (dye.getType()) {
                                                        case CYAN_STAINED_GLASS_PANE -> dim.setItemModel(ChameleonVariant.TENNANT_OPEN.getKey());
                                                        case GRAY_STAINED_GLASS_PANE -> dim.setItemModel(ChameleonVariant.WEEPING_ANGEL_OPEN.getKey());
                                                        case WHITE_DYE -> dim.setItemModel(ChameleonVariant.WHITE_OPEN.getKey());
                                                        case ORANGE_DYE -> dim.setItemModel(ChameleonVariant.ORANGE_OPEN.getKey());
                                                        case MAGENTA_DYE -> dim.setItemModel(ChameleonVariant.MAGENTA_OPEN.getKey());
                                                        case LIGHT_BLUE_DYE -> dim.setItemModel(ChameleonVariant.LIGHT_BLUE_OPEN.getKey());
                                                        case YELLOW_DYE -> dim.setItemModel(ChameleonVariant.YELLOW_OPEN.getKey());
                                                        case LIME_DYE -> dim.setItemModel(ChameleonVariant.LIME_OPEN.getKey());
                                                        case PINK_DYE -> dim.setItemModel(ChameleonVariant.PINK_OPEN.getKey());
                                                        case GRAY_DYE -> dim.setItemModel(ChameleonVariant.GRAY_OPEN.getKey());
                                                        case LIGHT_GRAY_DYE -> dim.setItemModel(ChameleonVariant.LIGHT_GRAY_OPEN.getKey());
                                                        case CYAN_DYE -> dim.setItemModel(ChameleonVariant.CYAN_OPEN.getKey());
                                                        case PURPLE_DYE -> dim.setItemModel(ChameleonVariant.PURPLE_OPEN.getKey());
                                                        case BLUE_DYE -> dim.setItemModel(ChameleonVariant.BLUE_OPEN.getKey());
                                                        case BROWN_DYE -> dim.setItemModel(ChameleonVariant.BROWN_OPEN.getKey());
                                                        case GREEN_DYE -> dim.setItemModel(ChameleonVariant.GREEN_OPEN.getKey());
                                                        case RED_DYE -> dim.setItemModel(ChameleonVariant.RED_OPEN.getKey());
                                                        case BLACK_DYE -> dim.setItemModel(ChameleonVariant.BLACK_OPEN.getKey());
                                                        case LEATHER_HORSE_ARMOR -> dim.setItemModel(ColouredVariant.TINTED_OPEN.getKey());
                                                        default -> {
                                                            // get the custom model config
                                                            NamespacedKey c = plugin.getUtils().getCustomModel(dye.getType(), "_open");
                                                            if (c != null) {
                                                                dim.setItemModel(Door.getOpenModel(dye.getType()));
                                                            }
                                                        }
                                                    }
                                                    dye.setItemMeta(dim);
                                                    ee.setHelmet(dye, true);
                                                }
                                            }
                                            TARDISSounds.playDoorSound(true, location);
                                        } else if (TARDISStaticUtils.isSonic(hand) && TARDISMaterials.dyes.contains(dye.getType()) && tardis.getUuid().equals(uuid)) {
                                            ItemMeta im = hand.getItemMeta();
                                            List<String> lore = im.getLore();
                                            if (TARDISPermission.hasPermission(player, "tardis.sonic.paint") && lore != null && lore.contains("Painter Upgrade")) {
                                                // check for dye in slot
                                                PlayerInventory inv = player.getInventory();
                                                ItemStack colour = inv.getItem(8);
                                                if (colour == null || !TARDISMaterials.dyes.contains(colour.getType())) {
                                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_DYE");
                                                    return true;
                                                }
                                                // dye = item frame item
                                                if (dye.getType() == colour.getType()) {
                                                    // same colour - do nothing
                                                    return true;
                                                }
                                                long now = System.currentTimeMillis();
                                                TARDISSonicSound.playSonicSound(plugin, player, now, 600L, "sonic_short");
                                                dye.setType(colour.getType());
                                                ee.setHelmet(dye, true);
                                                // remove one dye
                                                int a = colour.getAmount();
                                                int a2 = a - 1;
                                                if (a2 > 0) {
                                                    inv.getItem(8).setAmount(a2);
                                                } else {
                                                    inv.setItem(8, null);
                                                }
                                                player.updateInventory();
                                                // update database
                                                HashMap<String, Object> set = new HashMap<>();
                                                String c = "POLICE_BOX_" + colour.getType().toString().replace("_DYE", "");
                                                set.put("chameleon_preset", c);
                                                set.put("chameleon_demat", c);
                                                HashMap<String, Object> wheret = new HashMap<>();
                                                wheret.put("tardis_id", tardis.getTardisId());
                                                plugin.getQueryFactory().doUpdate("tardis", set, wheret);
                                            }
                                        }
                                    } else {
                                        if (tardis.isAbandoned()) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDONED_DOOR");
                                            return true;
                                        }
                                        // close portal & inner door
                                        new TARDISInnerDoorCloser(plugin, uuid, id).closeDoor(!plugin.getUtils().inTARDISWorld(player));
                                        if (dye.getType() == Material.ENDER_PEARL) {
                                            new PandoricaOpens(plugin).animate(stand, false);
                                        } else {
                                            switch (dye.getType()) {
                                                case CYAN_STAINED_GLASS_PANE -> dim.setItemModel(ChameleonVariant.TENNANT_CLOSED.getKey());
                                                case GRAY_STAINED_GLASS_PANE -> dim.setItemModel(ChameleonVariant.WEEPING_ANGEL_CLOSED.getKey());
                                                case WHITE_DYE -> dim.setItemModel(ChameleonVariant.WHITE_CLOSED.getKey());
                                                case ORANGE_DYE -> dim.setItemModel(ChameleonVariant.ORANGE_CLOSED.getKey());
                                                case MAGENTA_DYE -> dim.setItemModel(ChameleonVariant.MAGENTA_CLOSED.getKey());
                                                case LIGHT_BLUE_DYE -> dim.setItemModel(ChameleonVariant.LIGHT_BLUE_CLOSED.getKey());
                                                case YELLOW_DYE -> dim.setItemModel(ChameleonVariant.YELLOW_CLOSED.getKey());
                                                case LIME_DYE -> dim.setItemModel(ChameleonVariant.LIME_CLOSED.getKey());
                                                case PINK_DYE -> dim.setItemModel(ChameleonVariant.PINK_CLOSED.getKey());
                                                case GRAY_DYE -> dim.setItemModel(ChameleonVariant.GRAY_CLOSED.getKey());
                                                case LIGHT_GRAY_DYE -> dim.setItemModel(ChameleonVariant.LIGHT_GRAY_CLOSED.getKey());
                                                case CYAN_DYE -> dim.setItemModel(ChameleonVariant.CYAN_CLOSED.getKey());
                                                case PURPLE_DYE -> dim.setItemModel(ChameleonVariant.PURPLE_CLOSED.getKey());
                                                case BLUE_DYE -> dim.setItemModel(ChameleonVariant.BLUE_CLOSED.getKey());
                                                case BROWN_DYE -> dim.setItemModel(ChameleonVariant.BROWN_CLOSED.getKey());
                                                case GREEN_DYE -> dim.setItemModel(ChameleonVariant.GREEN_CLOSED.getKey());
                                                case RED_DYE -> dim.setItemModel(ChameleonVariant.RED_CLOSED.getKey());
                                                case BLACK_DYE -> dim.setItemModel(ChameleonVariant.BLACK_CLOSED.getKey());
                                                case LEATHER_HORSE_ARMOR -> dim.setItemModel(ColouredVariant.TINTED_CLOSED.getKey());
                                                default -> {
                                                    // get the custom model config
                                                    NamespacedKey c = plugin.getUtils().getCustomModel(dye.getType(), "_closed");
                                                    if (c != null) {
                                                        dim.setItemModel(Door.getOpenModel(dye.getType()));
                                                    }
                                                }
                                            }
                                            dye.setItemMeta(dim);
                                            ee.setHelmet(dye, true);
                                            TARDISSounds.playDoorSound(false, location);
                                        }
                                    }
                                } else if (!tardis.getUuid().equals(uuid)) {
                                    plugin.getMessenger().sendStatus(player, "DOOR_DEADLOCKED");
                                } else {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DOOR_NEED_UNLOCK");
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
