package me.eccentric_nz.tardis.keyboard;

import me.eccentric_nz.tardis.TardisPlugin;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_16_R3.TileEntitySign;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
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
        entityPlayer.playerConnection.sendPacket(tileEntitySign.getUpdatePacket());
        tileEntitySign.isEditable = true;
        tileEntitySign.a(entityPlayer);
        PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(tileEntitySign.getPosition());
        entityPlayer.playerConnection.sendPacket(packet);
        SignInputHandler.injectNetty(player, TardisPlugin.plugin);
    }

    /**
     * Stops listening for sign updates.
     */
    public static void finishSignEditing(Player player) {
        SignInputHandler.ejectNetty(player);
    }
}
