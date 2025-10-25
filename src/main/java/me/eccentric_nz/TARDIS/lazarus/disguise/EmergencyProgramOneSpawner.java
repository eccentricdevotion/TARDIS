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
package me.eccentric_nz.TARDIS.lazarus.disguise;

import com.destroystokyo.paper.SkinParts;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.SkinFetcher;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;

import java.util.UUID;

public class EmergencyProgramOneSpawner {

    private final Player player;
    private final Location location;
    private Mannequin npc = null;

    public EmergencyProgramOneSpawner(Player player, Location location) {
        this.player = player;
        this.location = location;
    }

    public void create() {
        SkinFetcher getter = new SkinFetcher(TARDIS.plugin, player.getUniqueId());
        getter.fetchAsync((hasResult, fetched) -> {
            if (hasResult) {
                JsonObject properties = fetched.getSkin();
                if (properties != null) {
                    GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
                    // set the skin to the player's
                    String value = properties.get("value").getAsString();
                    String signature = properties.get("signature").getAsString();
                    gameProfile.properties().removeAll("textures");
                    gameProfile.properties().put("textures", new Property("textures", value, signature));
                    ResolvableProfile profile = ResolvableProfile.resolvableProfile(player.getPlayerProfile());
                    World world = location.getWorld();
                    npc = (Mannequin) world.spawnEntity(location, EntityType.MANNEQUIN);
                    npc.setHealth(20.0d);
                    npc.setProfile(profile);
                    npc.setSkinParts(SkinParts.allParts());
                    npc.setImmovable(true);
                    npc.setInvulnerable(true);
                    // set NPC equipment
                    EntityEquipment equipment = npc.getEquipment();
                    equipment.setBoots(player.getInventory().getBoots());
                    equipment.setLeggings(player.getInventory().getLeggings());
                    equipment.setChestplate(player.getInventory().getChestplate());
                    equipment.setHelmet(player.getInventory().getHelmet());
                    equipment.setItemInMainHand(player.getInventory().getItemInMainHand());
                    equipment.setItemInOffHand(player.getInventory().getItemInOffHand());
                }
            }
        });
    }

    public Mannequin getMannequin() {
        return npc;
    }
}
