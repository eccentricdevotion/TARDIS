/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.rooms.happy;

import io.papermc.paper.entity.Leashable;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.mobfarming.HappyGhastUtils;
import me.eccentric_nz.TARDIS.mobfarming.TARDISBoat;
import me.eccentric_nz.TARDIS.mobfarming.TARDISHappyGhast;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.List;

public class HappyGhastRelease {

    private final TARDIS plugin;

    public HappyGhastRelease(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void undock(Block block, int id, Player player) {
        // only if unpowering the lever
        if (block.getBlockData() instanceof Switch lever && !lever.isPowered()) {
            return;
        }
        // find the happy ghast
        for (Entity e : block.getWorld().getNearbyEntities(block.getLocation(), 10, 10, 10)) {
            if (e instanceof HappyGhast ghast) {
                TARDISHappyGhast happy = new TARDISHappyGhast();
                happy.setAge(ghast.getAge());
                happy.setBaby(!ghast.isAdult());
                happy.setName(ComponentUtils.stripColour(ghast.customName()));
                happy.setHealth(ghast.getHealth());
                // get harness
                happy.setHarness(ghast.getEquipment().getItem(EquipmentSlot.BODY));
                // get/save home location
                happy.setHome(ghast.getMemory(MemoryKey.HOME));
                // get/save leashed boats
                Leashable leashed = HappyGhastUtils.getLeashed(ghast);
                if (leashed instanceof Boat boat) {
                    TARDISBoat tb = new TARDISBoat();
                    tb.setType(boat.getType());
                    if (boat instanceof ChestBoat chested) {
                        tb.setItems(chested.getInventory().getContents());
                    }
                    happy.setBoat(tb);
                    boat.remove();
                }
                if (leashed != null) {
                    leashed.setLeashHolder(null);
                }
                // get passengers
                List<Entity> passengers = ghast.getPassengers();
                // get the exit location
                TARDISDoorLocation dl = TARDISDoorListener.getDoor(0, id);
                Location l = dl.getL();
                // set the entity's direction as you would for a player when exiting
                switch (dl.getD()) {
                    case NORTH -> {
                        l.setZ(l.getZ() + 4.5f);
                        l.setYaw(0.0f);
                    }
                    case WEST -> {
                        l.setX(l.getX() + 4.5f);
                        l.setYaw(270.0f);
                    }
                    case SOUTH -> {
                        l.setZ(l.getZ() - 4.5f);
                        l.setYaw(180.0f);
                    }
                    default -> {
                        l.setX(l.getX() - 4.5f);
                        l.setYaw(90.0f);
                    }
                }
                // spawn ghast outside the TARDIS
                HappyGhast skies = (HappyGhast) l.getWorld().spawnEntity(l, EntityType.HAPPY_GHAST);
                skies.setAge(happy.getAge());
                if (happy.isBaby()) {
                    skies.setBaby();
                }
                if (happy.getHarness() != null) {
                    skies.getEquipment().setItem(EquipmentSlot.BODY, happy.getHarness());
                }
                skies.setHealth(happy.getHealth());
                String name = happy.getName();
                if (name != null && !name.isEmpty()) {
                    skies.customName(Component.text(name));
                }
                // teleport passengers
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    for (Entity p : passengers) {
                        ghast.removePassenger(p);
                        p.teleport(skies.getLocation().clone().add(0, 4, 0));
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> ghast.addPassenger(p), 2L);
                        // remove player from travellers table
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("uuid", p.getUniqueId().toString());
                        plugin.getQueryFactory().doDelete("travellers", where);
                    }
                }, 3L);
                // remove ghast
                ghast.remove();
                if (happy.getBoat() != null) {
                    TARDISBoat tb = happy.getBoat();
                    Entity boat = l.getWorld().spawnEntity(l, tb.getType());
                    if (boat instanceof ChestBoat chested) {
                        chested.getInventory().setContents(tb.getItems());
                    }
                    Leashable leashable = (Leashable) boat;
                    leashable.setLeashHolder(skies);
                }
                skies.setMemory(MemoryKey.HOME, happy.getHome());
                skies.setRemoveWhenFarAway(false);
            }
        }
    }
}
