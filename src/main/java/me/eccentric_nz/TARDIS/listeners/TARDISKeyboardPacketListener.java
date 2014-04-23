package me.eccentric_nz.TARDIS.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.BukkitUnwrapper;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.google.common.base.Objects;
import com.google.common.collect.MapMaker;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class TARDISKeyboardPacketListener implements Listener {

    private final TARDIS plugin;
    private final ConcurrentMap<Player, Location> editing = new MapMaker().weakKeys().makeMap();
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

                        StructureModifier<Integer> ints = event.getPacket().getIntegers();
                        Location loc = new Location(player.getWorld(), ints.read(0), ints.read(1), ints.read(2));

                        // Allow
                        if (Objects.equal(editing.get(player), loc)) {
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
                if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                    tcc = new TARDISCircuitChecker(plugin, rs.getTardis_id());
                    tcc.getCircuits();
                }
                if (tcc != null && !tcc.hasInput()) {
                    TARDISMessage.send(player, plugin.getPluginName() + "The Input Circuit is missing from the console!");
                    return;
                }
                Sign sign = (Sign) b.getState();
                plugin.getTrackerKeeper().getTrackSign().put(loc, sign);
                displaySignEditor(player, b);
            }
        }
    }

    /**
     * Display a sign editor for the given player.
     *
     * @param sign - the sign to edit.
     */
    private void displaySignEditor(Player player, Block sign) {
        if (!player.getWorld().equals(sign.getWorld())) {
            throw new IllegalArgumentException("Player and sign must be in the same world.");
        }
        PacketContainer editSignPacket = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_ENTITY);

        // Permit this
        editing.put(player, sign.getLocation());
        editSignPacket.getIntegers().write(0, sign.getX()).write(1, sign.getY()).write(2, sign.getZ());

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, editSignPacket);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet.", e);
        }
    }
}
