/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.travel.TARDISHorse;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.tardishorsespeed.TardisHorseSpeed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISHorseListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> barding = new ArrayList<Material>();

    public TARDISHorseListener(TARDIS plugin) {
        this.plugin = plugin;
        this.barding.add(Material.IRON_BARDING);
        this.barding.add(Material.GOLD_BARDING);
        this.barding.add(Material.DIAMOND_BARDING);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(EntityInteractEvent event) {
        Entity e = event.getEntity();
        if (e instanceof Horse) {
            Horse h = (Horse) e;
            Material m = event.getBlock().getType();
            Entity passenger = h.getPassenger();
            if (passenger != null && m.equals(Material.WOOD_PLATE)) {
                if (passenger instanceof Player) {
                    final Player p = (Player) passenger;
                    String pworld = p.getLocation().getWorld().getName();
                    HashMap<String, Object> wherep = new HashMap<String, Object>();
                    wherep.put("player", p.getName());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wherep, false);
                    if (rst.resultSet() && pworld.contains("TARDIS")) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("tardis_id", id);
                        whered.put("door_type", 1);
                        ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                        if (rsd.resultSet() && rsd.isLocked()) {
                            TARDISMessage.send(p, plugin.getPluginName() + "You need to unlock the door!");
                            return;
                        }
                        // get spawn location
                        TARDISDoorLocation dl = plugin.getGeneralKeeper().getDoorListener().getDoor(0, id);
                        Location l = dl.getL();
                        switch (dl.getD()) {
                            case NORTH:
                                l.setZ(l.getZ() + 5);
                                break;
                            case WEST:
                                l.setX(l.getX() + 5);
                                break;
                            case SOUTH:
                                l.setZ(l.getZ() - 5);
                                break;
                            default:
                                l.setX(l.getX() - 5);
                                break;
                        }
                        // save horse
                        TARDISHorse tmhor = new TARDISHorse();
                        tmhor.setAge(h.getTicksLived());
                        tmhor.setBaby(!h.isAdult());
                        tmhor.setHorseColour(h.getColor());
                        tmhor.setHorseStyle(h.getStyle());
                        tmhor.setHorseVariant(h.getVariant());
                        tmhor.setTamed(true);
                        if (h.isCarryingChest()) {
                            tmhor.setHasChest(true);
                        }
                        tmhor.setHorseInventory(h.getInventory().getContents());
                        tmhor.setDomesticity(h.getDomestication());
                        tmhor.setJumpStrength(h.getJumpStrength());
                        tmhor.setHealth(h.getHealth());
                        if (plugin.getPM().isPluginEnabled("TardisHorseSpeed")) {
                            TardisHorseSpeed ths = (TardisHorseSpeed) plugin.getPM().getPlugin("TardisHorseSpeed");
                            double speed = ths.getHorseSpeed(h);
                            tmhor.setSpeed(speed);
                        }
                        // eject player
                        // if (p.leaveVehicle()) {
                        if (h.eject()) {
                            // remove horse
                            h.remove();
                            // respawn horse
                            World world = l.getWorld();
                            // load the chunk
                            while (!world.getChunkAt(l).isLoaded()) {
                                world.getChunkAt(l).load();
                            }
                            Entity ent = world.spawnEntity(l, EntityType.HORSE);
                            final Horse equine = (Horse) ent;
                            equine.setAge(tmhor.getAge());
                            equine.setVariant(tmhor.getHorseVariant());
                            equine.setColor(tmhor.getHorseColour());
                            equine.setStyle(tmhor.getHorseStyle());
                            equine.setDomestication(tmhor.getDomesticity());
                            equine.setJumpStrength(tmhor.getJumpStrength());
                            if (tmhor.hasChest()) {
                                equine.setCarryingChest(true);
                            }
                            equine.setHealth(tmhor.getHealth());
                            Inventory inv = equine.getInventory();
                            inv.setContents(tmhor.getHorseinventory());
                            if (inv.contains(Material.SADDLE)) {
                                int saddle_slot = inv.first(Material.SADDLE);
                                ItemStack saddle = inv.getItem(saddle_slot);
                                equine.getInventory().setSaddle(saddle);
                            }
                            for (Material mat : barding) {
                                if (inv.contains(mat)) {
                                    int armour_slot = inv.first(mat);
                                    ItemStack bard = inv.getItem(armour_slot);
                                    equine.getInventory().setArmor(bard);
                                }
                            }
                            if (plugin.getPM().isPluginEnabled("TardisHorseSpeed")) {
                                TardisHorseSpeed ths = (TardisHorseSpeed) plugin.getPM().getPlugin("TardisHorseSpeed");
                                ths.setHorseSpeed(equine, tmhor.getSpeed());
                            }
                            Tameable tamed = (Tameable) equine;
                            tamed.setTamed(true);
                            tamed.setOwner(p);

                            // teleport player and remove from travellers table
                            plugin.getGeneralKeeper().getDoorListener().movePlayer(p, l, true, p.getWorld(), false, 0, true);
                            HashMap<String, Object> where = new HashMap<String, Object>();
                            where.put("player", p.getName());
                            new QueryFactory(plugin).doDelete("travellers", where);
                            // set player as passenger
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    equine.setPassenger(p);
                                }
                            }, 10L);
                        }
                    }
                }
            }
        }
    }
}
