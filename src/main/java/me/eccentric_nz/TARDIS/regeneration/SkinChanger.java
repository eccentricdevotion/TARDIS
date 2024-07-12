package me.eccentric_nz.TARDIS.regeneration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISChameleonArchDisguiser;
import me.eccentric_nz.TARDIS.lazarus.disguise.TARDISDisguiseTracker;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
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
        try {
            // set GameProfile accessible
            Field gpField = net.minecraft.world.entity.player.Player.class.getDeclaredField("cD"); // cD = GameProfile
            gpField.setAccessible(true);
            gpField.set(entityPlayer, profile);
            gpField.setAccessible(false);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException ignored) {
            TARDIS.plugin.debug("Failed to set GameProfile");
        }
        // re-register the game profile for all players
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.canSee(player)) {
                p.showPlayer(TARDIS.plugin, player);
            }
        }
        // refresh misc player things AFTER sending game profile
        ClientboundPlayerInfoUpdatePacket playerInfoUpdatePacket = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, entityPlayer);
        ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(player.getEntityId());
        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(entityPlayer, 0, entityPlayer.blockPosition());
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            ServerPlayer ep = ((CraftPlayer) p).getHandle();
            if (ep != entityPlayer && p.getWorld() == player.getWorld() && p.canSee(player)) {
                ep.connection.send(playerInfoUpdatePacket);
                ep.connection.send(removeEntitiesPacket);
                ep.connection.send(addEntityPacket);
            }
        }
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
