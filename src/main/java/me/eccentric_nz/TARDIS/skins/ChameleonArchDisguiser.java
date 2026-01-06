/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.skins;

import com.mojang.authlib.GameProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseTracker;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;

public class ChameleonArchDisguiser {

    private final TARDIS plugin;
    private final Player player;
    private final Field gpField;

    public ChameleonArchDisguiser(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        try {
            gpField = net.minecraft.world.entity.player.Player.class.getDeclaredField("gameProfile"); // cy = gameProfile
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeSkin(String name) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        // remove player info
        for (Player p : Bukkit.getOnlinePlayers()) {
            ServerPlayer ep = ((CraftPlayer) p).getHandle();
            if (ep != entityPlayer && p.getWorld() == player.getWorld() && p.canSee(player)) {
                ep.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(entityPlayer.getUUID())));
            }
        }
        TARDISDisguiseTracker.ARCHED.put(player.getUniqueId(), player.getName());
        // set name
        try {
            GameProfile arch = new GameProfile(player.getUniqueId(), name);
            gpField.setAccessible(true);
            gpField.set(entityPlayer, arch);
            gpField.setAccessible(false);
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
            return;
        }
        // get a random skin
        Skin random = ArchSkins.DEFAULTS.get(TARDISConstants.RANDOM.nextInt(ArchSkins.DEFAULTS.size()));
        // set skin
        plugin.getSkinChanger().set(player, random);
    }

    public void resetSkin() {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        // set name
        for (Player p : Bukkit.getOnlinePlayers()) {
            ServerPlayer ep = ((CraftPlayer) p).getHandle();
            if (ep != entityPlayer && p.getWorld() == player.getWorld() && p.canSee(player)) {
                ep.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(entityPlayer.getUUID())));
            }
        }
        String oldName = TARDISDisguiseTracker.ARCHED.get(player.getUniqueId());
        if (oldName == null || oldName.isEmpty()) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.HELPER_WARNING, "Could not get backed up player name from tracker!");
            TARDISDisguiseTracker.ARCHED.remove(player.getUniqueId());
            return;
        }
        try {
            GameProfile arch = new GameProfile(player.getUniqueId(), oldName);
            Field nameField = arch.getClass().getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(arch, oldName);
            nameField.setAccessible(false);
            gpField.setAccessible(true);
            gpField.set(entityPlayer, arch);
            gpField.setAccessible(false);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            TARDISDisguiseTracker.ARCHED.remove(player.getUniqueId());
            return;
        }
        // reset skin
        plugin.getSkinChanger().remove(player);
        TARDISDisguiseTracker.ARCHED.remove(player.getUniqueId());
    }
}
