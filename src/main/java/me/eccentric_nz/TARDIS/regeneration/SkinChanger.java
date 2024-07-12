package me.eccentric_nz.TARDIS.regeneration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISChameleonArchDisguiser;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseTracker;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.CommonPlayerSpawnInfo;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class SkinChanger {

    public static final HashMap<UUID, TARDISDisguiseTracker.ProfileData> REGENERATED = new HashMap<>();

    public static void set(Player player, String json) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        GameProfile profile = entityPlayer.getGameProfile();
        REGENERATED.put(player.getUniqueId(), new TARDISDisguiseTracker.ProfileData(profile.getProperties(), player.getName()));
        // convert the string to a json element
        JsonElement root = JsonParser.parseString(json);
        JsonObject skin = root.getAsJsonObject();
        String texture = skin.get("value").getAsString();
        String signature = skin.get("signature").getAsString();
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", texture, signature));
        // first unregister the player for all players with the OLD game profile
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.canSee(player)) {
                p.hidePlayer(TARDIS.plugin, player);
            }
        }
        // set the game profile here, we should have unregistered the entity via iterating all player entities above.
//        try {
//            // set GameProfile accessible
//            Field gpField = net.minecraft.world.entity.player.Player.class.getDeclaredField("cD"); // cD = GameProfile
//            gpField.setAccessible(true);
//            gpField.set(entityPlayer, profile);
//            gpField.setAccessible(false);
//        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException ignored) {
//            TARDIS.plugin.debug("Failed to set GameProfile");
//        }
        ((CraftPlayer) player).getHandle().getGameProfile().getProperties().clear();
        ((CraftPlayer) player).getHandle().getGameProfile().getProperties().putAll(profile.getProperties());
        // re-register the game profile for all players
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.canSee(player)) {
                p.showPlayer(TARDIS.plugin, player);
            }
        }
        // refresh misc player things AFTER sending game profile
//        ClientboundPlayerInfoUpdatePacket playerInfoUpdatePacket = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, entityPlayer);
//        ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(player.getEntityId());
//        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(entityPlayer, 0, entityPlayer.blockPosition());
        // respawn info
        CommonPlayerSpawnInfo spawnInfo = new CommonPlayerSpawnInfo(
                entityPlayer.serverLevel().dimensionTypeRegistration(),
                entityPlayer.serverLevel().dimension(),
                0L,
                entityPlayer.gameMode.getGameModeForPlayer(),
                entityPlayer.gameMode.getGameModeForPlayer(),
                false,
                false,
                Optional.empty(),
                0
        );
        // respawn packet
        ClientboundRespawnPacket respawnPacket = new ClientboundRespawnPacket(spawnInfo, (byte) 0x02);
        // pos packet
        Location location = player.getLocation();
        ClientboundPlayerPositionPacket positionPacket = new ClientboundPlayerPositionPacket(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), Collections.emptySet(), 0);
        // send packets
        entityPlayer.connection.send(respawnPacket);
        entityPlayer.connection.send(positionPacket);
        DedicatedPlayerList dedicatedPlayerList = ((CraftServer) Bukkit.getServer()).getHandle();
        // send level info
        dedicatedPlayerList.sendLevelInfo(entityPlayer, entityPlayer.serverLevel());
        // send all player info
        dedicatedPlayerList.sendAllPlayerInfo(entityPlayer);
//        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
//            ServerPlayer ep = ((CraftPlayer) p).getHandle();
//            if (ep != entityPlayer && p.getWorld() == player.getWorld() && p.canSee(player)) {
//                ep.connection.send(playerInfoUpdatePacket);
//                ep.connection.send(removeEntitiesPacket);
//                ep.connection.send(addEntityPacket);
//            }
//        }
        player.updateInventory();
        TARDISDisguiseTracker.DISGUISED_AS_PLAYER.add(player.getUniqueId());
    }

    public static void remove(Player player) {
        new TARDISChameleonArchDisguiser(TARDIS.plugin, player).resetSkin(REGENERATED);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player && player.getWorld() == p.getWorld()) {
                p.hidePlayer(TARDIS.plugin, player);
                p.showPlayer(TARDIS.plugin, player);
            }
        }
    }
}
