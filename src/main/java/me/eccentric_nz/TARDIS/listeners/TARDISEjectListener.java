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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.mobfarming.types.TARDISLlama;
import me.eccentric_nz.TARDIS.move.DoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
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
            plugin.getMessenger().send(player, TardisModule.TARDIS, "EJECT_WORLD");
            return;
        }
        Entity ent = event.getRightClicked();
        // only living entities
        if (!(ent instanceof LivingEntity)) {
            return;
        }
        // get the exit location
        TARDISDoorLocation dl = DoorListener.getDoor(0, plugin.getTrackerKeeper().getEjecting().get(uuid));
        Location l = dl.getL();
        // set the entity's direction as you would for a player when exiting
        switch (dl.getD()) {
            case NORTH -> {
                l.setZ(l.getZ() + 2.5f);
                l.setYaw(0.0f);
            }
            case WEST -> {
                l.setX(l.getX() + 2.5f);
                l.setYaw(270.0f);
            }
            case SOUTH -> {
                l.setZ(l.getZ() - 2.5f);
                l.setYaw(180.0f);
            }
            default -> {
                l.setX(l.getX() - 2.5f);
                l.setYaw(90.0f);
            }
        }
        switch (ent.getType()) {
            // can't eject OPs or TARDIS admins
            case PLAYER -> {
                Player p = (Player) ent;
                if (p.isOp() || TARDISPermission.hasPermission(p, "tardis.admin")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "EJECT_PLAYER");
                    return;
                }
                // check the clicked player is in a TARDIS world
                if (!plugin.getUtils().inTARDISWorld(p)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "EJECT_WORLD");
                    return;
                }
                // teleport player and remove from travellers table
                plugin.getGeneralKeeper().getDoorListener().movePlayer(p, l, true, p.getWorld(), false, 0, true, false);
                plugin.getMessenger().send(p, TardisModule.TARDIS, "EJECT_MESSAGE", player.getName());
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", p.getUniqueId().toString());
                plugin.getQueryFactory().doDelete("travellers", where);
            }
            case ALLAY -> {
                // cancel the event so the Allay doesn't lose its held item
                event.setCancelled(true);
                Allay a = (Allay) ent;
                Allay allay = (Allay) l.getWorld().spawnEntity(l, EntityType.ALLAY);
                allay.setTicksLived(a.getTicksLived());
                Component allayname = ent.customName();
                if (allayname != null) {
                    allay.customName(allayname);
                }
                allay.setCanDuplicate(a.canDuplicate());
                allay.setDuplicationCooldown(a.getDuplicationCooldown());
                allay.setMemory(MemoryKey.LIKED_PLAYER, a.getMemory(MemoryKey.LIKED_PLAYER));
                allay.getInventory().setContents(a.getInventory().getContents());
                ent.remove();
            }
            case BEE -> {
                Bee b = (Bee) ent;
                Bee bee = (Bee) l.getWorld().spawnEntity(l, EntityType.BEE);
                bee.setTicksLived(b.getTicksLived());
                if (!b.isAdult()) {
                    bee.setBaby();
                }
                Component beename = ent.customName();
                if (beename != null) {
                    bee.customName(beename);
                }
                bee.setHasStung(b.hasStung());
                bee.setHasNectar(b.hasNectar());
                bee.setAnger(b.getAnger());
                ent.remove();
            }
            case CHICKEN -> {
                Chicken k = (Chicken) ent;
                Chicken chicken = (Chicken) l.getWorld().spawnEntity(l, EntityType.CHICKEN);
                chicken.setTicksLived(k.getTicksLived());
                if ((!k.isAdult())) {
                    chicken.setBaby();
                }
                Component chickname = ent.customName();
                if (chickname != null) {
                    chicken.customName(chickname);
                }
                ent.remove();
            }
            case CAMEL -> {
                Camel camel = (Camel) ent;
                Camel hump = (Camel) l.getWorld().spawnEntity(l, EntityType.CAMEL);
                hump.setAge(camel.getAge());
                if (!camel.isAdult()) {
                    hump.setBaby();
                }
                hump.getInventory().setContents(camel.getInventory().getContents());
                Component camelname = ent.customName();
                if (camelname != null) {
                    hump.customName(camelname);
                }
                hump.setDomestication(camel.getDomestication());
                ent.remove();
            }
            case COW -> {
                Cow c = (Cow) ent;
                Cow cow = (Cow) l.getWorld().spawnEntity(l, EntityType.COW);
                cow.setTicksLived(c.getTicksLived());
                if ((!c.isAdult())) {
                    cow.setBaby();
                }
                Component cowname = ent.customName();
                if (cowname != null) {
                    cow.customName(cowname);
                }
                ent.remove();
            }
            case DONKEY, HORSE, MULE, SKELETON_HORSE, ZOMBIE_HORSE -> plugin.getMessenger().send(player, TardisModule.TARDIS, "EJECT_HORSE");
            case LLAMA -> {
                event.setCancelled(true);
                Llama ll = (Llama) ent;
                TARDISLlama tmlla = new TARDISLlama();
                tmlla.setAge(ll.getAge());
                tmlla.setBaby(!ll.isAdult());
                double mh = ll.getAttribute(Attribute.MAX_HEALTH).getValue();
                tmlla.setHorseHealth(mh);
                tmlla.setHealth(ll.getHealth());
                // get horse colour, style and variant
                tmlla.setLlamacolor(ll.getColor());
                tmlla.setStrength(ll.getStrength());
                tmlla.setHorseVariant(EntityType.HORSE);
                tmlla.setTamed(ll.isTamed());
                if (ll.isCarryingChest()) {
                    tmlla.setHasChest(true);
                }
                tmlla.setName(ComponentUtils.stripColour(ll.customName()));
                tmlla.setHorseInventory(ll.getInventory().getContents());
                tmlla.setDomesticity(ll.getDomestication());
                tmlla.setJumpStrength(ll.getJumpStrength());
                tmlla.setSpeed(ll.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue());
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
                AttributeInstance attribute = llama.getAttribute(Attribute.MAX_HEALTH);
                attribute.setBaseValue(tmlla.getHorseHealth());
                String name = tmlla.getName();
                if (name != null && !name.isEmpty()) {
                    llama.customName(Component.text(name));
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
                    ItemStack leash = ItemStack.of(Material.LEAD, 1);
                    pinv.addItem(leash);
                    player.updateInventory();
                }
                llama.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(tmlla.getSpeed());
                ent.remove();
            }
            case MOOSHROOM -> {
                MushroomCow m = (MushroomCow) ent;
                MushroomCow mush = (MushroomCow) l.getWorld().spawnEntity(l, EntityType.MOOSHROOM);
                mush.setTicksLived(m.getTicksLived());
                if ((!m.isAdult())) {
                    mush.setBaby();
                }
                mush.setVariant(m.getVariant());
                Component mushname = ent.customName();
                if (mushname != null) {
                    mush.customName(mushname);
                }
                ent.remove();
            }
            case PANDA -> {
                Panda inner_panda = (Panda) ent;
                Panda outer_panda = (Panda) l.getWorld().spawnEntity(l, EntityType.PANDA);
                outer_panda.setTicksLived(inner_panda.getTicksLived());
                if (!inner_panda.isAdult()) {
                    outer_panda.setBaby();
                }
                outer_panda.setMainGene(inner_panda.getMainGene());
                outer_panda.setHiddenGene(inner_panda.getHiddenGene());
                Component panda_name = ent.customName();
                if (panda_name != null) {
                    outer_panda.customName(panda_name);
                }
                ent.remove();
            }
            case PARROT -> {
                Parrot inner_parrot = (Parrot) ent;
                Parrot outer_parrot = (Parrot) l.getWorld().spawnEntity(l, EntityType.PARROT);
                outer_parrot.setTicksLived(inner_parrot.getTicksLived());
                Component parrot_name = ent.customName();
                if (parrot_name != null) {
                    outer_parrot.customName(parrot_name);
                }
                outer_parrot.setVariant(inner_parrot.getVariant());
                ent.remove();
            }
            case PIG -> {
                Pig g = (Pig) ent;
                // eject any passengers
                g.eject();
                Pig pig = (Pig) l.getWorld().spawnEntity(l, EntityType.PIG);
                pig.setTicksLived(g.getTicksLived());
                if ((!g.isAdult())) {
                    pig.setBaby();
                }
                Component pigname = ent.customName();
                if (pigname != null) {
                    pig.customName(pigname);
                }
                if (g.hasSaddle()) {
                    pig.setSaddle(true);
                }
                ent.remove();
            }
            case POLAR_BEAR -> {
                PolarBear polar = (PolarBear) ent;
                PolarBear bear = (PolarBear) l.getWorld().spawnEntity(l, EntityType.POLAR_BEAR);
                bear.setTicksLived(polar.getTicksLived());
                if ((!polar.isAdult())) {
                    bear.setBaby();
                }
                Component bearname = ent.customName();
                if (bearname != null) {
                    bear.customName(bearname);
                }
                ent.remove();
            }
            case SHEEP -> {
                Sheep s = (Sheep) ent;
                Sheep sheep = (Sheep) l.getWorld().spawnEntity(l, EntityType.SHEEP);
                sheep.setTicksLived(s.getTicksLived());
                if ((!s.isAdult())) {
                    sheep.setBaby();
                }
                Component sheepname = ent.customName();
                if (sheepname != null) {
                    sheep.customName(sheepname);
                }
                sheep.setColor(s.getColor());
                ent.remove();
            }
            case SNIFFER -> {
                Sniffer s = (Sniffer) ent;
                Sniffer sniffer = (Sniffer) l.getWorld().spawnEntity(l, EntityType.SNIFFER);
                sniffer.setTicksLived(s.getTicksLived());
                if ((!s.isAdult())) {
                    sniffer.setBaby();
                }
                Component sniffername = ent.customName();
                if (sniffername != null) {
                    sniffer.customName(sniffername);
                }
                ent.remove();
            }
            case STRIDER -> {
                Strider s = (Strider) ent;
                Strider strider = (Strider) l.getWorld().spawnEntity(l, EntityType.STRIDER);
                strider.setTicksLived(s.getTicksLived());
                if ((!s.isAdult())) {
                    strider.setBaby();
                }
                Component stridername = ent.customName();
                if (stridername != null) {
                    strider.customName(stridername);
                }
                ent.remove();
            }
            case RABBIT -> {
                Rabbit r = (Rabbit) ent;
                Rabbit bunny = (Rabbit) l.getWorld().spawnEntity(l, EntityType.RABBIT);
                bunny.setTicksLived(r.getTicksLived());
                if ((!r.isAdult())) {
                    bunny.setBaby();
                }
                Component rabbitname = ent.customName();
                if (rabbitname != null) {
                    bunny.customName(rabbitname);
                }
                bunny.setRabbitType(r.getRabbitType());
                ent.remove();
            }
            case WOLF -> {
                Tameable wtamed = (Tameable) ent;
                if (wtamed.isTamed() && wtamed.getOwner().getUniqueId().equals(player.getUniqueId())) {
                    Wolf w = (Wolf) ent;
                    Wolf wolf = (Wolf) l.getWorld().spawnEntity(l, EntityType.WOLF);
                    wolf.setTicksLived(w.getTicksLived());
                    if ((!w.isAdult())) {
                        wolf.setBaby();
                    }
                    Component wolfname = ent.customName();
                    if (wolfname != null) {
                        wolf.customName(wolfname);
                    }
                    wolf.setSitting(w.isSitting());
                    wolf.setCollarColor(w.getCollarColor());
                    double health = Math.min(w.getHealth(), 8D);
                    wolf.setHealth(health);
                    ent.remove();
                }
            }
            case CAT -> {
                Tameable otamed = (Tameable) ent;
                if (otamed.isTamed() && otamed.getOwner().getUniqueId().equals(player.getUniqueId())) {
                    Cat o = (Cat) ent;
                    Cat cat = (Cat) l.getWorld().spawnEntity(l, EntityType.CAT);
                    cat.setTicksLived(o.getTicksLived());
                    if ((!o.isAdult())) {
                        cat.setBaby();
                    }
                    Component catname = ent.customName();
                    if (catname != null) {
                        cat.customName(catname);
                    }
                    cat.setSitting(o.isSitting());
                    cat.setCatType(o.getCatType());
                    cat.setCollarColor(o.getCollarColor());
                    double health = Math.min(o.getHealth(), 8D);
                    cat.setHealth(health);
                    ent.remove();
                }
            }
            case VILLAGER -> {
                event.setCancelled(true);
                Villager v = (Villager) ent;
                Villager villager = (Villager) l.getWorld().spawnEntity(l, EntityType.VILLAGER);
                villager.setProfession(v.getProfession());
                villager.setAge(v.getTicksLived());
                if (!v.isAdult()) {
                    villager.setBaby();
                }
                villager.setHealth(v.getHealth());
                villager.setRecipes(v.getRecipes());
                villager.setVillagerType(v.getVillagerType());
                villager.setVillagerExperience(v.getVillagerExperience());
                villager.setVillagerLevel(v.getVillagerLevel());
                plugin.getTardisHelper().setReputation(villager, uuid, plugin.getTardisHelper().getReputation(v, uuid));
                Component vilname = ent.customName();
                if (vilname != null) {
                    villager.customName(vilname);
                }
                ent.remove();
            }
            case FROG -> {
                event.setCancelled(true);
                Frog f = (Frog) ent;
                Frog frog = (Frog) l.getWorld().spawnEntity(l, EntityType.FROG);
                frog.setVariant(f.getVariant());
                frog.setAge(f.getTicksLived());
                if (!f.isAdult()) {
                    frog.setBaby();
                }
                frog.setHealth(f.getHealth());
                Component frogname = ent.customName();
                if (frogname != null) {
                    frog.customName(frogname);
                }
                ent.remove();
            }
            case AXOLOTL -> {
                event.setCancelled(true);
                Axolotl a = (Axolotl) ent;
                Axolotl axolotl = (Axolotl) l.getWorld().spawnEntity(l, EntityType.AXOLOTL);
                axolotl.setVariant(a.getVariant());
                axolotl.setAge(a.getTicksLived());
                if (!a.isAdult()) {
                    axolotl.setBaby();
                }
                axolotl.setHealth(a.getHealth());
                Component axname = ent.customName();
                if (axname != null) {
                    axolotl.customName(axname);
                }
                ent.remove();
            }
            default -> plugin.getMessenger().send(player, TardisModule.TARDIS, "EJECT_NOT_VALID");
        }
        // stop tracking player
        plugin.getTrackerKeeper().getEjecting().remove(uuid);
    }
}
