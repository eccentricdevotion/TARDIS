/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.mobfarming.TARDISHorse;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISHorseListener implements Listener {

    private final TARDIS plugin;

    public TARDISHorseListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(EntityInteractEvent event) {
        Entity e = event.getEntity();
        if (e instanceof AbstractHorse && !(e instanceof Llama)) {
            AbstractHorse h = (AbstractHorse) e;
            Material m = event.getBlock().getType();
            Entity passenger = (h.getPassengers().size() > 0) ? h.getPassengers().get(0) : null;
            if (passenger != null && m.equals(Material.OAK_PRESSURE_PLATE)) {
                if (passenger instanceof Player) {
                    Player p = (Player) passenger;
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
                            TARDISMessage.send(p, "DOOR_UNLOCK");
                            return;
                        }
                        // get spawn location
                        TARDISDoorLocation dl = plugin.getGeneralKeeper().getDoorListener().getDoor(0, id);
                        Location l = dl.getL();
                        // set the horse's direction as you would for a player when exiting
                        switch (dl.getD()) {
                            case NORTH:
                                l.setZ(l.getZ() + 5);
                                l.setYaw(0.0f);
                                break;
                            case WEST:
                                l.setX(l.getX() + 5);
                                l.setYaw(270.0f);
                                break;
                            case SOUTH:
                                l.setZ(l.getZ() - 5);
                                l.setYaw(180.0f);
                                break;
                            default:
                                l.setX(l.getX() - 5);
                                l.setYaw(90.0f);
                                break;
                        }
                        // save horse
                        TARDISHorse tmhor = new TARDISHorse();
                        tmhor.setAge(h.getTicksLived());
                        tmhor.setBaby(!h.isAdult());
                        if (e.getType().equals(EntityType.HORSE)) {
                            Horse hh = (Horse) e;
                            tmhor.setHorseColour(hh.getColor());
                            tmhor.setHorseStyle(hh.getStyle());
                        }
                        tmhor.setHorseVariant(e.getType());
                        tmhor.setName(h.getCustomName());
                        tmhor.setTamed(true);
                        if (h instanceof ChestedHorse) {
                            ChestedHorse ch = (ChestedHorse) h;
                            if (ch.isCarryingChest()) {
                                tmhor.setHasChest(true);
                            }
                        }
                        tmhor.setHorseInventory(h.getInventory().getContents());
                        tmhor.setDomesticity(h.getDomestication());
                        tmhor.setJumpStrength(h.getJumpStrength());
                        double mh = h.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                        tmhor.setHorseHealth(mh);
                        tmhor.setHealth(h.getHealth());
                        if (plugin.isHelperOnServer()) {
                            tmhor.setSpeed(plugin.getTardisHelper().getHorseSpeed(h));
                        }
                        // eject player
                        if (h.eject()) {
                            // remove horse
                            h.remove();
                            // respawn horse
                            World world = l.getWorld();
                            // load the chunk
                            while (!world.getChunkAt(l).isLoaded()) {
                                world.getChunkAt(l).load();
                            }
                            Entity ent = world.spawnEntity(l, tmhor.getHorseVariant());
                            AbstractHorse equine = (AbstractHorse) ent;
                            equine.setAge(tmhor.getAge());
                            equine.setDomestication(tmhor.getDomesticity());
                            equine.setJumpStrength(tmhor.getJumpStrength());
                            String name = tmhor.getName();
                            if (name != null && !name.isEmpty()) {
                                equine.setCustomName(name);
                            }
                            if (tmhor.hasChest()) {
                                ChestedHorse chested = (ChestedHorse) equine;
                                chested.setCarryingChest(true);
                            }
                            AttributeInstance attribute = equine.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                            attribute.setBaseValue(tmhor.getHorseHealth());
                            equine.setHealth(tmhor.getHealth());
                            Inventory inv = equine.getInventory();
                            inv.setContents(tmhor.getHorseinventory());
                            if (tmhor.getHorseVariant().equals(EntityType.HORSE)) {
                                Horse ee = (Horse) equine;
                                ee.setColor(tmhor.getHorseColour());
                                ee.setStyle(tmhor.getHorseStyle());
                            }
                            if (plugin.isHelperOnServer()) {
                                plugin.getTardisHelper().setHorseSpeed(equine, tmhor.getSpeed());
                            }
                            equine.setTamed(true);
                            equine.setOwner(p);

                            // teleport player and remove from travellers table
                            plugin.getGeneralKeeper().getDoorListener().movePlayer(p, l, true, p.getWorld(), false, 0, true);
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("uuid", p.getUniqueId().toString());
                            new QueryFactory(plugin).doDelete("travellers", where);
                            // set player as passenger
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> equine.addPassenger(p), 10L);
                        }
                    }
                }
            }
        }
    }
}
