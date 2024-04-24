/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.disguise;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;

import java.lang.reflect.Field;
import java.util.Arrays;

public class TARDISChameleonArchDisguiser {

    private final TARDIS plugin;
    private final org.bukkit.entity.Player player;
    private final String archSkin = "eyJ0aW1lc3RhbXAiOjE1NjY3OTQ5MTM4MjMsInByb2ZpbGVJZCI6ImY3NjdhNmIyMDgzZTQwMzhhM2ViMTAxNmI3Yjk3NDNjIiwicHJvZmlsZU5hbWUiOiJSb2JiaWUiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FkZWIyMjQzMDBmMmYxNjEyYWM0ZmQ4NWQwNjBlYzljMmY4NDNkYmRlZjMyNGVkMzgzZDRjMGM2ZjgxNGVjNGUifX19";
    private final String archSignature = "Spsq7rec2UB8x7SBBjDQ/hguC8Nt5XvyGUmw58Z7ZXVBbVqVLjMXMGD9MY25dy9HuWC1sMO1svrz35lJlsZArwI2Mm1c1LXTTYffrWGu7DLS2ONVDH26Cp5DB/buVLU/FTdyqB6OcZGU6zliU7sHlLBVcgv7FdS8Enoq9k9CQXUYacEyTDTQJGKgV3FDow7jDcLOQYdFTVaZsImOqhadhEmmQUZyGWtZqbKI+diIrFbUBNtIiMz14Lk3f3u7z8OQG/cabIEiKUvHZz6yCtt8LCQimHe9oEPPREQYH2ztOp3vrOli+6zvnu8GOspB4tWs8zIZZ86FQbD1PNdodrrYcG0vEKQ5zX8fuCDCPngiz5GbvJzku4SleHcx3iAdP9FeUie46ILKlBIUyvnjzBmubQVhBDB9jA+RcrYMpLz/EBU5kpAanz1M7BECsdWLqtrLD7tvYxsRfGdUmOlDT2TcpDmNeQHD0kgS8WBIXZhGa7FnnC/J1UXfNJSHKbnZ6AIfTplCnhZTI4BNGzwNdiM1rPoLE5HpRJ+2fyQn3bD6jssvZuI7S9UW1gvDfiaMG10RPiFVntjkWQVrZZ6qXbMqF1S6FbYvTTJOnjtrYqeXcLSqjCPPkcTHCn/2igl1V0D3A+NukxEFJLCXcpcW0Zx4bzQ0AWdNqcU1bHU9G3GN0QE=";

    public TARDISChameleonArchDisguiser(TARDIS plugin, org.bukkit.entity.Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void changeSkin(String name) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        // set name
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            ServerPlayer ep = ((CraftPlayer) p).getHandle();
            if (ep != entityPlayer && p.getWorld() == player.getWorld() && p.canSee(player)) {
                ep.connection.send(new ClientboundPlayerInfoRemovePacket(Arrays.asList(entityPlayer.getUUID())));
            }
        }
        TARDISDisguiseTracker.ARCHED.put(player.getUniqueId(), new TARDISDisguiseTracker.ProfileData(entityPlayer.getGameProfile().getProperties(), player.getName()));
        try {
            GameProfile arch = new GameProfile(player.getUniqueId(), name);
            arch.getProperties().removeAll("textures");
            arch.getProperties().put("textures", new Property("textures", archSkin, archSignature));
            Field gpField = Player.class.getDeclaredField("cq"); // cr = GameProfile
            gpField.setAccessible(true);
            gpField.set(entityPlayer, arch);
            gpField.setAccessible(false);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            TARDISDisguiseTracker.ARCHED.remove(player.getUniqueId());
            return;
        }
        ClientboundPlayerInfoUpdatePacket packetPlayOutPlayerInfo = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, entityPlayer); // a = ADD_PLAYER
        ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(player.getEntityId());
        ClientboundAddEntityPacket packetPlayOutNamedEntitySpawn = new ClientboundAddEntityPacket(entityPlayer);
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            ServerPlayer ep = ((CraftPlayer) p).getHandle();
            if (ep != entityPlayer && p.getWorld() == player.getWorld() && p.canSee(player)) {
                ep.connection.send(packetPlayOutPlayerInfo);
                ep.connection.send(packetPlayOutEntityDestroy);
                ep.connection.send(packetPlayOutNamedEntitySpawn);
            }
        }
    }

    public void resetSkin() {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        // set name
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            ServerPlayer ep = ((CraftPlayer) p).getHandle();
            if (ep != entityPlayer && p.getWorld() == player.getWorld() && p.canSee(player)) {
                ep.connection.send(new ClientboundPlayerInfoRemovePacket(Arrays.asList(entityPlayer.getUUID())));
            }
        }
        TARDISDisguiseTracker.ProfileData map = TARDISDisguiseTracker.ARCHED.get(player.getUniqueId());
        if (map == null) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER_WARNING, "Could not get backed up profile data from tracker!");
            return;
        }
        PropertyMap properties = map.getProperties();
        String oldName = map.getName();
        try {
            GameProfile arch = new GameProfile(player.getUniqueId(), oldName);
            Field nameField = arch.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(arch, oldName);
            nameField.setAccessible(false);
            arch.getProperties().putAll(properties);
            Field gpField = Player.class.getDeclaredField("cq"); // cr = GameProfile
            gpField.setAccessible(true);
            gpField.set(entityPlayer, arch);
            gpField.setAccessible(false);
            arch.getProperties().put("textures", new Property("textures", archSkin, archSignature));
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            TARDISDisguiseTracker.ARCHED.remove(player.getUniqueId());
            return;
        }
        ClientboundPlayerInfoUpdatePacket packetPlayOutPlayerInfo = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, entityPlayer);
        ClientboundRemoveEntitiesPacket packetPlayOutEntityDestroy = new ClientboundRemoveEntitiesPacket(player.getEntityId());
        ClientboundAddEntityPacket packetPlayOutNamedEntitySpawn = new ClientboundAddEntityPacket(entityPlayer);
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            ServerPlayer ep = ((CraftPlayer) p).getHandle();
            if (ep != entityPlayer && p.getWorld() == player.getWorld() && p.canSee(player)) {
                ep.connection.send(packetPlayOutPlayerInfo);
                ep.connection.send(packetPlayOutEntityDestroy);
                ep.connection.send(packetPlayOutNamedEntitySpawn);
            }
        }
        TARDISDisguiseTracker.ARCHED.remove(player.getUniqueId());
    }
}
