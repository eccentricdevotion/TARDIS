/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.utility;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.control.TardisScanner;
import me.eccentric_nz.tardis.disguise.Fox;
import me.eccentric_nz.tardis.disguise.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisEntityTracker {

    private final TardisPlugin plugin;

    public TardisEntityTracker(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public void addNpcs(Location exterior, Location interior, UUID uuid) {
        List<Entity> ents = TardisScanner.getNearbyEntities(exterior, 6);
        List<UUID> npcids = new ArrayList<>();
        for (Entity e : ents) {
            if (e instanceof LivingEntity) {
                Location location = e.getLocation();
                // determine relative location
                double relx = location.getX() - exterior.getX();
                double rely = location.getY() - exterior.getY();
                double relz = location.getZ() - exterior.getZ();
                double adjx = interior.getX() + relx;
                double adjy = interior.getY() + rely;
                double adjz = interior.getZ() + relz;
                Location l = new Location(interior.getWorld(), adjx, adjy, adjz);
                l.setYaw(location.getYaw());
                l.setPitch(location.getPitch());
                // create NPC
                plugin.setTardisSpawn(true);
                ArmorStand stand = (ArmorStand) Objects.requireNonNull(interior.getWorld()).spawnEntity(l, EntityType.ARMOR_STAND);
                npcids.add(stand.getUniqueId());
                Object[] options = null;
                switch (e.getType()) {
                    case CAT:
                        options = new Object[]{((Cat) e).getCatType(), ((Tameable) e).isTamed(), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case PANDA:
                        options = new Object[]{Gene.getFromPandaGene(((Panda) e).getMainGene()), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case DONKEY:
                    case MULE:
                        options = new Object[]{((ChestedHorse) e).isCarryingChest(), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case PIG:
                        options = new Object[]{((Pig) e).hasSaddle(), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case CREEPER:
                        options = new Object[]{((Creeper) e).isPowered()};
                        break;
                    case ENDERMAN:
                        options = new Object[]{((Enderman) e).getCarriedBlock() != null};
                        break;
                    case SHEEP:
                        options = new Object[]{((Sheep) e).getColor(), ((Tameable) e).isTamed(), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case WOLF:
                        options = new Object[]{((Wolf) e).getCollarColor(), ((Tameable) e).isTamed(), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case HORSE:
                        options = new Object[]{((Horse) e).getColor(), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case LLAMA:
                        options = new Object[]{((Llama) e).getColor(), ((Llama) e).getInventory().getDecor() != null, Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case OCELOT:
                        options = new Object[]{((Tameable) e).isTamed(), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case PARROT:
                        options = new Object[]{((Parrot) e).getVariant(), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case RABBIT:
                        options = new Object[]{((Rabbit) e).getRabbitType(), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case VILLAGER:
                        options = new Object[]{Profession.getFromVillagerProfession(((Villager) e).getProfession()), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case ZOMBIE_VILLAGER:
                        options = new Object[]{Profession.getFromVillagerProfession(Objects.requireNonNull(((ZombieVillager) e).getVillagerProfession())), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case SLIME:
                    case MAGMA_CUBE:
                        options = new Object[]{((Slime) e).getSize()};
                        break;
                    case COW:
                    case TURTLE:
                    case ZOMBIE:
                    case BEE:
                        options = new Object[]{Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case SNOWMAN:
                        options = new Object[]{((Snowman) e).isDerp()};
                        break;
                    case PUFFERFISH:
                        options = new Object[]{((PufferFish) e).getPuffState()};
                        break;
                    case TROPICAL_FISH:
                        options = new Object[]{((TropicalFish) e).getPattern()};
                        break;
                    case MUSHROOM_COW:
                        options = new Object[]{Mooshroom.getFromMushroomCowType(((MushroomCow) e).getVariant()), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    case FOX:
                        options = new Object[]{Fox.getFromFoxType(((org.bukkit.entity.Fox) e).getFoxType()), Age.getFromBoolean(!((Ageable) e).isAdult())};
                        break;
                    default:
                        break;
                }
                TardisArmorStandDisguiser.disguiseArmourStand(stand, e.getType(), options);
            }
        }
        if (npcids.size() > 0) {
            plugin.getTrackerKeeper().getRenderedNpcs().put(uuid, npcids);
        }
    }

    public void removeNpcs(UUID uuid) {
        plugin.getTrackerKeeper().getRenderedNpcs().get(uuid).forEach((i) -> {
            Entity npc = Bukkit.getEntity(i);
            if (npc != null) {
                TardisArmorStandDisguiser.undisguiseArmourStand((ArmorStand) npc);
                npc.remove();
            }
        });
        plugin.getTrackerKeeper().getRenderedNpcs().remove(uuid);
    }
}
