package me.eccentric_nz.tardis.keyboard;

import me.eccentric_nz.tardis.TardisPlugin;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.level.block.entity.TileEntitySign;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TardisKeyboardGuiController {

    /**
     * Opens a sign editing GUI.
     *
     * @param player the player to open the GUI for
     * @param sign   the sign block that is being edited
     */
    public static void openSignGui(Player player, Sign sign) {
        Location location = sign.getLocation();
        TileEntitySign tileEntitySign = (TileEntitySign) ((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle().getTileEntity(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        EntityPlayer entityPlayer = ((CraftPlayer) Objects.requireNonNull(player.getPlayer())).getHandle();
        assert tileEntitySign != null;
        entityPlayer.b.sendPacket(tileEntitySign.getUpdatePacket()); // b = playerConnection
        tileEntitySign.f = true; // f = isEditable
        tileEntitySign.a(entityPlayer);
        PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(tileEntitySign.getPosition());
        entityPlayer.b.sendPacket(packet); // b = playerConnection
        SignInputHandler.injectNetty(player, TardisPlugin.plugin);
    }

    /**
     * Stops listening for sign updates.
     */
    public static void finishSignEditing(Player player) {
        SignInputHandler.ejectNetty(player);
    }
}
