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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.mobfarming.TARDISHorse;
import me.eccentric_nz.TARDIS.mobfarming.TARDISLlama;
import me.eccentric_nz.TARDIS.mobfarming.TARDISMob;
import me.eccentric_nz.TARDIS.mobfarming.TARDISPig;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.LlamaInventory;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISRideableMobListener implements Listener {

    private final TARDIS plugin;
    private final List<EntityType> rideable = List.of(
            EntityType.CAMEL,
            EntityType.DONKEY,
            EntityType.HORSE,
            EntityType.LLAMA,
            EntityType.MULE,
            EntityType.PIG,
            EntityType.SKELETON_HORSE,
            EntityType.STRIDER,
            EntityType.ZOMBIE_HORSE
    );

    public TARDISRideableMobListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(EntityInteractEvent event) {
        Entity e = event.getEntity();
        EntityType type = e.getType();
        if (rideable.contains(type)) {
            Material m = event.getBlock().getType();
            Entity passenger = (!e.getPassengers().isEmpty()) ? e.getPassengers().getFirst() : null;
            if (passenger != null && m.equals(Material.OAK_PRESSURE_PLATE)) {
                if (passenger instanceof Player p) {
                    String pworld = p.getLocation().getWorld().getName();
                    HashMap<String, Object> wherep = new HashMap<>();
                    wherep.put("uuid", p.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wherep, false);
                    if (rst.resultSet() && pworld.contains("TARDIS")) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", id);
                        whered.put("door_type", 1);
                        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                        if (rsd.resultSet() && rsd.isLocked()) {
                            plugin.getMessenger().send(p, TardisModule.TARDIS, "DOOR_NEED_UNLOCK");
                            return;
                        }
                        // get spawn location
                        TARDISDoorLocation dl = TARDISDoorListener.getDoor(0, id);
                        Location l = dl.getL();
                        // set the horse's direction as you would for a player when exiting
                        switch (dl.getD()) {
                            case NORTH -> {
                                l.setZ(l.getZ() + 5);
                                l.setYaw(0.0f);
                            }
                            case WEST -> {
                                l.setX(l.getX() + 5);
                                l.setYaw(270.0f);
                            }
                            case SOUTH -> {
                                l.setZ(l.getZ() - 5);
                                l.setYaw(180.0f);
                            }
                            default -> {
                                l.setX(l.getX() - 5);
                                l.setYaw(90.0f);
                            }
                        }
                        TARDISHorse tmhor = null;
                        TARDISLlama tmlla = null;
                        TARDISPig tmpig = null;
                        TARDISHorse tmcamel = null;
                        TARDISMob mob = null;
                        switch (type) {
                            case DONKEY, HORSE, MULE, SKELETON_HORSE, ZOMBIE_HORSE -> {
                                AbstractHorse horse = (AbstractHorse) e;
                                // save horse
                                tmhor = new TARDISHorse();
                                tmhor.setType(type);
                                tmhor.setAge(horse.getTicksLived());
                                tmhor.setBaby(!horse.isAdult());
                                if (e.getType().equals(EntityType.HORSE)) {
                                    Horse hh = (Horse) e;
                                    tmhor.setHorseColour(hh.getColor());
                                    tmhor.setHorseStyle(hh.getStyle());
                                }
                                tmhor.setHorseVariant(e.getType());
                                tmhor.setName(ComponentUtils.stripColour(horse.customName()));
                                tmhor.setTamed(true);
                                if (horse instanceof ChestedHorse ch) {
                                    if (ch.isCarryingChest()) {
                                        tmhor.setHasChest(true);
                                    }
                                }
                                tmhor.setHorseInventory(horse.getInventory().getContents());
                                tmhor.setDomesticity(horse.getDomestication());
                                tmhor.setJumpStrength(horse.getJumpStrength());
                                double mh = horse.getAttribute(Attribute.MAX_HEALTH).getValue();
                                tmhor.setHorseHealth(mh);
                                tmhor.setHealth(horse.getHealth());
                                tmhor.setSpeed(horse.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue());
                            }
                            case LLAMA -> {
                                Llama llama = (Llama) e;
                                tmlla = new TARDISLlama();
                                tmlla.setType(type);
                                tmlla.setAge(llama.getAge());
                                tmlla.setBaby(!llama.isAdult());
                                double llmh = llama.getAttribute(Attribute.MAX_HEALTH).getValue();
                                tmlla.setHorseHealth(llmh);
                                tmlla.setHealth(llama.getHealth());
                                // get horse colour, style and variant
                                tmlla.setLlamacolor(llama.getColor());
                                tmlla.setStrength(llama.getStrength());
                                tmlla.setHorseVariant(EntityType.HORSE);
                                tmlla.setTamed(llama.isTamed());
                                if (llama.isCarryingChest()) {
                                    tmlla.setHasChest(true);
                                }
                                tmlla.setName(ComponentUtils.stripColour(llama.customName()));
                                tmlla.setHorseInventory(llama.getInventory().getContents());
                                tmlla.setDomesticity(llama.getDomestication());
                                tmlla.setJumpStrength(llama.getJumpStrength());
                                tmlla.setSpeed(llama.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue());
                                // check the leash
                                if (llama.isLeashed()) {
                                    Entity leash = llama.getLeashHolder();
                                    tmlla.setLeashed(true);
                                    if (leash instanceof LeashHitch) {
                                        leash.remove();
                                    }
                                }
                                LlamaInventory llinv = llama.getInventory();
                                tmlla.setDecor(llinv.getDecor());
                            }
                            case PIG -> {
                                Pig pig = (Pig) e;
                                tmpig = new TARDISPig();
                                tmpig.setType(type);
                                tmpig.setPigVariant(pig.getVariant());
                                tmpig.setAge(pig.getAge());
                                tmpig.setBaby(!pig.isAdult());
                                tmpig.setName(ComponentUtils.stripColour(e.customName()));
                                tmpig.setSaddled(pig.hasSaddle());
                            }
                            case STRIDER -> {
                                Strider strider = (Strider) e;
                                mob = new TARDISMob();
                                mob.setType(type);
                                mob.setAge(strider.getAge());
                                mob.setBaby(!strider.isAdult());
                                mob.setName(ComponentUtils.stripColour(e.customName()));
                            }
                            case CAMEL -> {
                                Camel camel = (Camel) e;
                                // save camel
                                tmcamel = new TARDISHorse();
                                tmcamel.setType(type);
                                tmcamel.setAge(camel.getTicksLived());
                                tmcamel.setBaby(!camel.isAdult());
                                tmcamel.setHorseVariant(e.getType());
                                tmcamel.setName(ComponentUtils.stripColour(camel.customName()));
                                tmcamel.setTamed(true);
                                tmcamel.setHorseInventory(camel.getInventory().getContents());
                                tmcamel.setDomesticity(camel.getDomestication());
                                tmcamel.setJumpStrength(camel.getJumpStrength());
                                double mh = camel.getAttribute(Attribute.MAX_HEALTH).getValue();
                                tmcamel.setHorseHealth(mh);
                                tmcamel.setHealth(camel.getHealth());
                                tmcamel.setSpeed(camel.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue());
                            }
                            default -> { }
                        }
                        // eject player
                        if (e.eject()) {
                            // remove ridden entity
                            e.remove();
                            World world = l.getWorld();
                            // load the chunk
                            while (!world.getChunkAt(l).isLoaded()) {
                                world.getChunkAt(l).load();
                            }
                            Entity ent = null;
                            if (tmhor != null) {
                                ent = world.spawnEntity(l, tmhor.getHorseVariant());
                                AbstractHorse equine = (AbstractHorse) ent;
                                equine.setAge(tmhor.getAge());
                                equine.setDomestication(tmhor.getDomesticity());
                                equine.setJumpStrength(tmhor.getJumpStrength());
                                String name = tmhor.getName();
                                if (!name.isEmpty()) {
                                    equine.customName(Component.text(name));
                                }
                                if (tmhor.hasChest()) {
                                    ChestedHorse chested = (ChestedHorse) equine;
                                    chested.setCarryingChest(true);
                                }
                                AttributeInstance attribute = equine.getAttribute(Attribute.MAX_HEALTH);
                                attribute.setBaseValue(tmhor.getHorseHealth());
                                equine.setHealth(tmhor.getHealth());
                                Inventory inv = equine.getInventory();
                                inv.setContents(tmhor.getHorseinventory());
                                if (tmhor.getHorseVariant().equals(EntityType.HORSE)) {
                                    Horse ee = (Horse) equine;
                                    ee.setColor(tmhor.getHorseColour());
                                    ee.setStyle(tmhor.getHorseStyle());
                                }
                                equine.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(tmhor.getSpeed());
                                equine.setTamed(true);
                                equine.setOwner(p);
                            } else if (tmlla != null) {
                                ent = world.spawnEntity(l, EntityType.LLAMA);
                                Llama llama = (Llama) ent;
                                llama.setColor(tmlla.getLlamacolor());
                                llama.setStrength(tmlla.getStrength());
                                llama.setAge(tmlla.getAge());
                                if (tmlla.isBaby()) {
                                    llama.setBaby();
                                }
                                AttributeInstance attribute = llama.getAttribute(Attribute.MAX_HEALTH);
                                attribute.setBaseValue(tmlla.getHorseHealth());
                                llama.setHealth(tmlla.getHealth());
                                String name = tmlla.getName();
                                if (!name.isEmpty()) {
                                    llama.customName(Component.text(name));
                                }
                                if (tmlla.isTamed()) {
                                    llama.setTamed(true);
                                    llama.setOwner(p);
                                }
                                llama.setDomestication(tmlla.getDomesticity());
                                llama.setJumpStrength(tmlla.getJumpStrength());
                                if (tmlla.hasChest()) {
                                    llama.setCarryingChest(true);
                                }
                                LlamaInventory inv = llama.getInventory();
                                inv.setContents(tmlla.getHorseinventory());
                                inv.setDecor(tmlla.getDecor());
                                llama.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(tmlla.getSpeed());
                                llama.setRemoveWhenFarAway(false);
                            } else if (tmpig != null) {
                                ent = world.spawnEntity(l, EntityType.PIG);
                                Pig pig = (Pig) ent;
                                pig.setVariant(tmpig.getPigVariant());
                                pig.setAge(tmpig.getAge());
                                if (tmpig.isBaby()) {
                                    pig.setBaby();
                                }
                                String name = e.getName();
                                if (!name.isEmpty()) {
                                    pig.customName(Component.text(name));
                                }
                                pig.setSaddle(true);
                                pig.setRemoveWhenFarAway(false);
                            } else if (tmcamel != null) {
                                ent = world.spawnEntity(l, EntityType.CAMEL);
                                Camel humped = (Camel) ent;
                                humped.setAge(tmcamel.getAge());
                                humped.setDomestication(tmcamel.getDomesticity());
                                humped.setJumpStrength(tmcamel.getJumpStrength());
                                String name = tmcamel.getName();
                                if (!name.isEmpty()) {
                                    humped.customName(Component.text(name));
                                }
                                AttributeInstance attribute = humped.getAttribute(Attribute.MAX_HEALTH);
                                attribute.setBaseValue(tmcamel.getHorseHealth());
                                humped.setHealth(tmcamel.getHealth());
                                Inventory inv = humped.getInventory();
                                inv.setContents(tmcamel.getHorseinventory());
                                humped.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(tmcamel.getSpeed());
                                humped.setTamed(true);
                                humped.setOwner(p);
                            } else if (mob != null) {
                                ent = world.spawnEntity(l, EntityType.STRIDER);
                                Strider strider = (Strider) ent;
                                strider.setAge(mob.getAge());
                                if (mob.isBaby()) {
                                    strider.setBaby();
                                }
                                String name = e.getName();
                                if (!name.isEmpty()) {
                                    strider.customName(Component.text(name));
                                }
                                strider.setRemoveWhenFarAway(false);
                            }
                            // teleport player and remove from travellers table
                            plugin.getGeneralKeeper().getDoorListener().movePlayer(p, l, true, p.getWorld(), false, 0, true, false);
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("uuid", p.getUniqueId().toString());
                            plugin.getQueryFactory().doDelete("travellers", where);
                            // set player as passenger
                            if (ent != null) {
                                Entity vehicle = ent;
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> vehicle.addPassenger(p), 10L);
                            }
                        }
                    }
                }
            }
        }
    }
}
