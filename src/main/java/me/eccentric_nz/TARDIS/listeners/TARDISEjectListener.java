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
import me.eccentric_nz.TARDIS.mobfarming.TARDISLlama;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LlamaInventory;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISEjectListener implements Listener {

    private final TARDIS plugin;

    public TARDISEjectListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!plugin.getTrackerKeeper().getEjecting().containsKey(uuid)) {
            return;
        }
        // check they are still in the TARDIS world - they could have exited after running the command
        if (!plugin.getUtils().inTARDISWorld(player)) {
            TARDISMessage.send(player, "EJECT_WORLD");
            return;
        }
        Entity ent = event.getRightClicked();
        // only living entities
        if (!(ent instanceof LivingEntity)) {
            return;
        }
        // get the exit location
        TARDISDoorLocation dl = plugin.getGeneralKeeper().getDoorListener().getDoor(0, plugin.getTrackerKeeper().getEjecting().get(uuid));
        Location l = dl.getL();
        // set the entity's direction as you would for a player when exiting
        switch (dl.getD()) {
            case NORTH:
                l.setZ(l.getZ() + 2.5f);
                l.setYaw(0.0f);
                break;
            case WEST:
                l.setX(l.getX() + 2.5f);
                l.setYaw(270.0f);
                break;
            case SOUTH:
                l.setZ(l.getZ() - 2.5f);
                l.setYaw(180.0f);
                break;
            default:
                l.setX(l.getX() - 2.5f);
                l.setYaw(90.0f);
                break;
        }
        switch (ent.getType()) {
            // can't eject OPs or TARDIS admins
            case PLAYER:
                Player p = (Player) ent;
                if (p.isOp() || p.hasPermission("tardis.admin")) {
                    TARDISMessage.send(player, "EJECT_PLAYER");
                    return;
                }
                // check the clicked player is in a TARDIS world
                if (!plugin.getUtils().inTARDISWorld(p)) {
                    TARDISMessage.send(player, "EJECT_WORLD");
                    return;
                }
                // teleport player and remove from travellers table
                plugin.getGeneralKeeper().getDoorListener().movePlayer(p, l, true, p.getWorld(), false, 0, true);
                TARDISMessage.send(p, "EJECT_MESSAGE", player.getName());
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", p.getUniqueId().toString());
                new QueryFactory(plugin).doDelete("travellers", where);
                break;
            case CHICKEN:
                Chicken k = (Chicken) ent;
                Chicken chicken = (Chicken) l.getWorld().spawnEntity(l, EntityType.CHICKEN);
                chicken.setTicksLived(k.getTicksLived());
                if ((!k.isAdult())) {
                    chicken.setBaby();
                }
                String chickname = ent.getCustomName();
                if (chickname != null && !chickname.isEmpty()) {
                    chicken.setCustomName(chickname);
                }
                ent.remove();
                break;
            case COW:
                Cow c = (Cow) ent;
                Cow cow = (Cow) l.getWorld().spawnEntity(l, EntityType.COW);
                cow.setTicksLived(c.getTicksLived());
                if ((!c.isAdult())) {
                    cow.setBaby();
                }
                String cowname = ent.getCustomName();
                if (cowname != null && !cowname.isEmpty()) {
                    cow.setCustomName(cowname);
                }
                ent.remove();
                break;
            case DONKEY:
            case HORSE:
            case MULE:
            case SKELETON_HORSE:
            case ZOMBIE_HORSE:
                TARDISMessage.send(player, "EJECT_HORSE");
                break;
            case LLAMA:
                event.setCancelled(true);
                Llama ll = (Llama) ent;
                TARDISLlama tmlla = new TARDISLlama();
                tmlla.setAge(ll.getAge());
                tmlla.setBaby(!ll.isAdult());
                double mh = ll.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                tmlla.setHorseHealth(mh);
                tmlla.setHealth(ll.getHealth());
                // get horse colour, style and variant
                tmlla.setLlamacolor(ll.getColor());
                tmlla.setStrength(ll.getStrength());
                tmlla.setHorseVariant(EntityType.HORSE);
                if (ll.isTamed()) {
                    tmlla.setTamed(true);
                } else {
                    tmlla.setTamed(false);
                }
                if (ll.isCarryingChest()) {
                    tmlla.setHasChest(true);
                }
                tmlla.setName(ll.getCustomName());
                tmlla.setHorseInventory(ll.getInventory().getContents());
                tmlla.setDomesticity(ll.getDomestication());
                tmlla.setJumpStrength(ll.getJumpStrength());
                tmlla.setSpeed(ll.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
                // check the leash
                if (ll.isLeashed()) {
                    Entity leash = ll.getLeashHolder();
                    tmlla.setLeashed(true);
                    if (leash instanceof LeashHitch) {
                        leash.remove();
                    }
                }
                LlamaInventory llinv = ll.getInventory();
                tmlla.setDecor(llinv.getDecor());
                Llama llama = (Llama) l.getWorld().spawnEntity(l, EntityType.LLAMA);
                llama.setColor(tmlla.getLlamacolor());
                llama.setStrength(tmlla.getStrength());
                llama.setAge(tmlla.getAge());
                if (tmlla.isBaby()) {
                    llama.setBaby();
                }
                AttributeInstance attribute = llama.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                attribute.setBaseValue(tmlla.getHorseHealth());
                String name = tmlla.getName();
                if (name != null && !name.isEmpty()) {
                    llama.setCustomName(name);
                }
                if (tmlla.isTamed()) {
                    llama.setTamed(true);
                    llama.setOwner(player);
                }
                llama.setDomestication(tmlla.getDomesticity());
                llama.setJumpStrength(tmlla.getJumpStrength());
                if (tmlla.hasChest()) {
                    llama.setCarryingChest(true);
                }
                LlamaInventory inv = llama.getInventory();
                inv.setContents(tmlla.getHorseinventory());
                inv.setDecor(tmlla.getDecor());
                if (tmlla.isLeashed()) {
                    Inventory pinv = player.getInventory();
                    ItemStack leash = new ItemStack(Material.LEAD, 1);
                    pinv.addItem(leash);
                    player.updateInventory();
                }
                llama.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(tmlla.getSpeed());
                ent.remove();
                break;
            case MUSHROOM_COW:
                MushroomCow m = (MushroomCow) ent;
                MushroomCow mush = (MushroomCow) l.getWorld().spawnEntity(l, EntityType.MUSHROOM_COW);
                mush.setTicksLived(m.getTicksLived());
                if ((!m.isAdult())) {
                    mush.setBaby();
                }
                String mushname = ent.getCustomName();
                if (mushname != null && !mushname.isEmpty()) {
                    mush.setCustomName(mushname);
                }
                ent.remove();
                break;
            case PIG:
                Pig g = (Pig) ent;
                // eject any passengers
                g.eject();
                Pig pig = (Pig) l.getWorld().spawnEntity(l, EntityType.PIG);
                pig.setTicksLived(g.getTicksLived());
                if ((!g.isAdult())) {
                    pig.setBaby();
                }
                String pigname = ent.getCustomName();
                if (pigname != null && !pigname.isEmpty()) {
                    pig.setCustomName(pigname);
                }
                if (g.hasSaddle()) {
                    pig.setSaddle(true);
                }
                ent.remove();
                break;
            case POLAR_BEAR:
                PolarBear b = (PolarBear) ent;
                PolarBear polarbear = (PolarBear) l.getWorld().spawnEntity(l, EntityType.POLAR_BEAR);
                polarbear.setTicksLived(b.getTicksLived());
                if ((!b.isAdult())) {
                    polarbear.setBaby();
                }
                String bearname = ent.getCustomName();
                if (bearname != null && !bearname.isEmpty()) {
                    polarbear.setCustomName(bearname);
                }
                ent.remove();
                break;
            case SHEEP:
                Sheep s = (Sheep) ent;
                Sheep sheep = (Sheep) l.getWorld().spawnEntity(l, EntityType.SHEEP);
                sheep.setTicksLived(s.getTicksLived());
                if ((!s.isAdult())) {
                    sheep.setBaby();
                }
                String sheepname = ent.getCustomName();
                if (sheepname != null && !sheepname.isEmpty()) {
                    sheep.setCustomName(sheepname);
                }
                sheep.setColor(s.getColor());
                ent.remove();
                break;
            case RABBIT:
                Rabbit r = (Rabbit) ent;
                Rabbit bunny = (Rabbit) l.getWorld().spawnEntity(l, EntityType.RABBIT);
                bunny.setTicksLived(r.getTicksLived());
                if ((!r.isAdult())) {
                    bunny.setBaby();
                }
                String rabbitname = ent.getCustomName();
                if (rabbitname != null && !rabbitname.isEmpty()) {
                    bunny.setCustomName(rabbitname);
                }
                bunny.setRabbitType(r.getRabbitType());
                ent.remove();
                break;
            case WOLF:
                Tameable wtamed = (Tameable) ent;
                if (wtamed.isTamed() && wtamed.getOwner().getUniqueId().equals(player.getUniqueId())) {
                    Wolf w = (Wolf) ent;
                    Wolf wolf = (Wolf) l.getWorld().spawnEntity(l, EntityType.WOLF);
                    wolf.setTicksLived(w.getTicksLived());
                    if ((!w.isAdult())) {
                        wolf.setBaby();
                    }
                    String wolfname = ent.getCustomName();
                    if (wolfname != null && !wolfname.isEmpty()) {
                        wolf.setCustomName(wolfname);
                    }
                    wolf.setSitting(w.isSitting());
                    wolf.setCollarColor(w.getCollarColor());
                    double health = (w.getHealth() > 8D) ? 8D : w.getHealth();
                    wolf.setHealth(health);
                    ent.remove();
                }
                break;
            case OCELOT:
                Tameable otamed = (Tameable) ent;
                if (otamed.isTamed() && otamed.getOwner().getUniqueId().equals(player.getUniqueId())) {
                    Ocelot o = (Ocelot) ent;
                    Ocelot cat = (Ocelot) l.getWorld().spawnEntity(l, EntityType.OCELOT);
                    cat.setTicksLived(o.getTicksLived());
                    if ((!o.isAdult())) {
                        cat.setBaby();
                    }
                    String catname = ent.getCustomName();
                    if (catname != null && !catname.isEmpty()) {
                        cat.setCustomName(catname);
                    }
                    cat.setSitting(o.isSitting());
                    cat.setCatType(o.getCatType());
                    double health = (o.getHealth() > 8D) ? 8D : o.getHealth();
                    cat.setHealth(health);
                    ent.remove();
                }
                break;
            case VILLAGER:
                event.setCancelled(true);
                Villager v = (Villager) ent;
                Villager villager = (Villager) l.getWorld().spawnEntity(l, EntityType.VILLAGER);
                villager.setProfession(v.getProfession());
                villager.setAge(v.getTicksLived());
                villager.setHealth(v.getHealth());
                villager.setRecipes(v.getRecipes());
                villager.setRiches(v.getRiches());
                villager.setCareer(v.getCareer());
                if ((!((Villager) ent).isAdult())) {
                    villager.setBaby();
                }
                String vilname = ent.getCustomName();
                if (vilname != null && !vilname.isEmpty()) {
                    villager.setCustomName(vilname);
                }
                if (plugin.isHelperOnServer()) {
                    plugin.getTardisHelper().setVillagerCareerLevel(villager, plugin.getTardisHelper().getVillagerCareerLevel(v));
                    plugin.getTardisHelper().setVillagerWilling(villager, plugin.getTardisHelper().getVillagerWilling(v));
                }
                ent.remove();
                break;
            default:
                TARDISMessage.send(player, "EJECT_NOT_VALID");
                break;
        }
        // stop tracking player
        plugin.getTrackerKeeper().getEjecting().remove(uuid);
    }
}
