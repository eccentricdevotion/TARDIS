package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemRegistry;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.flight.vehicle.InterpolatedAnimation;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisshop.ShopItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;

import java.util.Locale;

public class DisplayItemUtility {

    public static void add(Player player, String m, String t) {
        Block block = player.getTargetBlock(null, 8);
        Material material;
        ItemDisplay.ItemDisplayTransform transform = ItemDisplay.ItemDisplayTransform.GROUND;
        try {
            material = Material.valueOf(m);
            if (!t.isEmpty()) {
                transform = ItemDisplay.ItemDisplayTransform.valueOf(t);
            }
        } catch (IllegalArgumentException e) {
            material = Material.DIAMOND_AXE;
        }
        ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
        ItemStack is = ItemStack.of(material);
        display.setItemStack(is);
        display.setItemDisplayTransform(transform);
        display.setBillboard(Display.Billboard.VERTICAL);
        display.setInvulnerable(true);
    }

    public static void shop(Player player, String s, boolean label) {
        Block block = player.getTargetBlock(null, 8);
        try {
            ShopItem shopItem = ShopItem.valueOf(s.toUpperCase(Locale.ROOT));
            ItemDisplay.ItemDisplayTransform transform = ItemDisplay.ItemDisplayTransform.GROUND;
            ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
            ItemStack is = ItemStack.of(shopItem.getMaterial());
            ItemMeta im = is.getItemMeta();
            im.setItemModel(shopItem.getModel());
            is.setItemMeta(im);
            display.setItemStack(is);
            display.setItemDisplayTransform(transform);
            display.setBillboard(Display.Billboard.VERTICAL);
            display.setInvulnerable(true);
            if (label) {
                TextDisplay text = (TextDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 1.75d, 0.5d), EntityType.TEXT_DISPLAY);
                text.setAlignment(TextDisplay.TextAlignment.CENTER);
                text.text(Component.text(TARDISStringUtils.capitalise(shopItem.getDisplayName()) + ", Cost: 25.00"));
                text.setTransformation(new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, TARDISConstants.VECTOR_QUARTER, TARDISConstants.AXIS_ANGLE_ZERO));
                text.setBillboard(Display.Billboard.VERTICAL);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    public static void animate(TARDIS plugin, Player player, boolean b) {
        if (player.getPassengers().isEmpty()) {
            if (!b) {
                ItemStack box = ItemStack.of(Material.BLUE_DYE, 1);
                ItemMeta im = box.getItemMeta();
                im.setItemModel(ChameleonVariant.BLUE_CLOSED.getKey());
                box.setItemMeta(im);
                ItemDisplay display = VehicleUtility.getItemDisplay(player, box, 1.75f);
                int period = 40;
                plugin.getTrackerKeeper().setAnimateTask(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new InterpolatedAnimation(display, period), 5, period));
            } else {
                Bee bee = (Bee) player.getWorld().spawnEntity(player.getLocation(), EntityType.BEE);
                bee.setAI(false);
                player.addPassenger(bee);
            }
        } else {
            for (Entity e : player.getPassengers()) {
                e.eject();
                e.remove();
            }
            plugin.getServer().getScheduler().cancelTask(plugin.getTrackerKeeper().getAnimateTask());
        }
    }

    public static void door(TARDIS plugin, Player player) {
        // get the item display the player is looking at
        Location observerPos = player.getEyeLocation();
        RayTraceResult result = observerPos.getWorld().rayTraceEntities(observerPos, observerPos.getDirection(), 16.0d, (s) -> s.getType() == EntityType.ITEM_DISPLAY);
        if (result != null) {
            ItemDisplay display = (ItemDisplay) result.getHitEntity();
            if (display != null) {
                ItemStack is = display.getItemStack();
                plugin.getMessenger().message(player, is.getType().toString());
            }
        }
    }

    public static void remove(Player player) {
        Block block = player.getTargetBlock(null, 8);
        BoundingBox box = new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX() + 1, block.getY() + 2.5, block.getZ() + 1);
        for (Entity e : block.getWorld().getNearbyEntities(box)) {
            if (e instanceof ItemDisplay || e instanceof TextDisplay || e instanceof Interaction) {
                e.remove();
            }
            // set the block to air
            block.setType(Material.AIR);
        }
    }

    public static void place(TARDIS plugin, Player player, String item) {
        Block block = player.getTargetBlock(null, 8);
        TARDISDisplayItem tdi = TARDISDisplayItemRegistry.getBY_NAME().get(item);
        if (tdi != null) {
            ItemStack is = ItemStack.of(tdi.getMaterial());
            ItemMeta im = is.getItemMeta();
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
            im.setItemModel(tdi.getCustomModel());
            im.displayName(Component.text(TARDISStringUtils.capitalise(item)));
            is.setItemMeta(im);
            Block up = block.getRelative(BlockFace.UP);
            if (tdi.isClosedDoor() || tdi.isLight()) {
                // also set an interaction entity
                Interaction interaction = (Interaction) block.getWorld().spawnEntity(up.getLocation().clone().add(0.5d, 0, 0.5d), EntityType.INTERACTION);
                interaction.setResponsive(true);
                interaction.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, tdi.getCustomModel().getKey());
                interaction.setPersistent(true);
                if (tdi.isLight()) {
                    Levelled light = TARDISConstants.LIGHT;
                    int level = (tdi.isLit()) ? 15 : 0;
                    light.setLevel(level);
                    up.setBlockData(light);
                }
                if (tdi.isClosedDoor()) {
                    // set size
                    interaction.setInteractionHeight(2.0f);
                    interaction.setInteractionWidth(1.0f);
                }
            } else {
                up.setType((tdi == TARDISBlockDisplayItem.ARTRON_FURNACE) ? Material.FURNACE : Material.BARRIER);
            }
            double ay = (tdi.isClosedDoor()) ? 0.0d : 0.5d;
            ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(up.getLocation().add(0.5d, ay, 0.5d), EntityType.ITEM_DISPLAY);
            display.setItemStack(is);
            display.setPersistent(true);
            display.setInvulnerable(true);
            if (tdi.isClosedDoor() || tdi == TARDISBlockDisplayItem.UNTEMPERED_SCHISM) {
                display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
            }
            if (tdi.isPipe() || tdi.getMaterial() == Material.AMETHYST_SHARD) {
                display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            }
            if (tdi == TARDISBlockDisplayItem.ARTRON_FURNACE) {
                display.setBrightness(new Display.Brightness(15, 15));
            }
        }
    }

    public static void breakDisplay(Player player) {
        Block block = player.getTargetBlock(null, 8);
        if (block.getType().equals(Material.BARRIER)) {
            for (Entity e : block.getWorld().getNearbyEntities(block.getBoundingBox().expand(0.1d))) {
                if (e instanceof ItemDisplay display) {
                    ItemStack is = display.getItemStack();
                    block.getWorld().dropItemNaturally(block.getLocation(), is);
                    e.remove();
                }
                if (e instanceof Interaction) {
                    e.remove();
                }
            }
            block.setType(Material.AIR);
        }
    }

    public static void console(TARDIS plugin, Player player, String c) {
        String colour = c.toLowerCase(Locale.ROOT);
        if (!TARDISConstants.COLOURS.contains(colour)) {
            plugin.getMessenger().message(player, "Must be a valid console type!");
            return;
        }
        // get TARDIS id
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        String uuid = player.getUniqueId().toString();
        if (rs.fromUUID(uuid)) {
            Block block = player.getTargetBlock(null, 8);
            new ConsoleBuilder(plugin).create(block, colour, rs.getTardisId(), uuid);
        }
    }
}
