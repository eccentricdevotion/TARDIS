package me.eccentric_nz.TARDIS.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.BukkitUnwrapper;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.google.common.base.Objects;
import com.google.common.collect.MapMaker;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class TARDISKeyboardPacketListener implements Listener {

    private final TARDIS plugin;
    private static final ConcurrentMap<Player, Location> EDITING = new MapMaker().weakKeys().makeMap();
    // For accessing the underlying tile entity
    private FieldAccessor tileEntityAccessor;
    private FieldAccessor booleanAccessor;
    private FieldAccessor humanAccessor;

    public TARDISKeyboardPacketListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void startSignPackets() {
        ProtocolLibrary.getProtocolManager().getAsynchronousManager().registerAsyncHandler(
                new PacketAdapter(plugin, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                StructureModifier<BlockPosition> ints = event.getPacket().getBlockPositionModifier();
                Location loc = new Location(player.getWorld(), (double) ints.read(0).getX(), (double) ints.read(0).getY(), (double) ints.read(0).getZ());
                // Allow
                if (Objects.equal(EDITING.get(player), loc) && (loc.getBlock().getType().equals(Material.WALL_SIGN) || loc.getBlock().getType().equals(Material.SIGN_POST))) {
                    setEditingPlayer((Sign) loc.getBlock().getState(), player);
                }
            }
        }).syncStart();
    }

    private void setEditingPlayer(Sign sign, Player player) {
        // Extract a TileEntitySign from a CraftSign
        if (tileEntityAccessor == null) {
            Class<?> tileEntity = MinecraftReflection.getMinecraftClass("TileEntitySign");
            Class<?> humanClass = MinecraftReflection.getMinecraftClass("EntityHuman");
            tileEntityAccessor = Accessors.getFieldAccessor(sign.getClass(), tileEntity, true);
            booleanAccessor = Accessors.getFieldAccessor(tileEntity, boolean.class, true);
            humanAccessor = Accessors.getFieldAccessor(tileEntity, humanClass, true);
        }
        Object tileEntity = tileEntityAccessor.get(sign);
        booleanAccessor.set(tileEntity, true);
        humanAccessor.set(tileEntity, BukkitUnwrapper.getInstance().unwrapItem(player));
    }

    @EventHandler(ignoreCancelled = true)
    public void onKeyboardInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Block b = event.getClickedBlock();
        if (b != null && (b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN))) {
            Player player = event.getPlayer();
            String loc = event.getClickedBlock().getLocation().toString();
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("type", 7);
            where.put("location", loc);
            ResultSetControls rs = new ResultSetControls(plugin, where, false);
            if (rs.resultSet()) {
                TARDISCircuitChecker tcc = null;
                if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
                    tcc = new TARDISCircuitChecker(plugin, rs.getTardis_id());
                    tcc.getCircuits();
                }
                if (tcc != null && !tcc.hasInput()) {
                    TARDISMessage.send(player, "INPUT_MISSING");
                    return;
                }
                Sign sign = (Sign) b.getState();
                plugin.getTrackerKeeper().getSign().put(loc, sign);
                displaySignEditor(player, b);
            }
        }
    }

    /**
     * Display a sign editor for the given player.
     *
     * @param player - the player to send the packet to
     * @param sign - the sign to edit.
     */
    public static void displaySignEditor(Player player, Block sign) {
        if (!player.getWorld().equals(sign.getWorld())) {
            throw new IllegalArgumentException("Player and sign must be in the same world.");
        }
        PacketContainer editSignPacket = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR);

        // Permit this
        EDITING.put(player, sign.getLocation());
        try {
            BlockPosition bp = new BlockPosition(sign.getX(), sign.getY(), sign.getZ());
            editSignPacket.getBlockPositionModifier().write(0, bp);
        } catch (FieldAccessException e) {
            TARDIS.plugin.debug("Keyboard error: " + e.getMessage());
            return;
        }

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, editSignPacket);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet.", e);
        }
    }
}
