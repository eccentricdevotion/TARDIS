package me.eccentric_nz.TARDIS.rooms.debug;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.doors.Door;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.rotors.Rotor;
import me.eccentric_nz.tardisshop.ShopItem;
import me.eccentric_nz.tardisshop.ShopItemRecipe;
import me.eccentric_nz.tardisshop.TARDISShopItemSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.UUID;

public class DebugPopulator {

    private final TARDIS plugin;
    private final World world;

    public DebugPopulator(TARDIS plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    public void createBase() {
        // TIPS slot -50
        TARDISTIPSData tipsData = new TARDISInteriorPostioning(plugin).getTIPSData(-50);
        int x = tipsData.getCentreX();
        int z = tipsData.getCentreZ();
        plugin.debug("Debug Preview spawn => x" + x + ", y65, z" + z);
        HashMap<String, Object> set = new HashMap<>();
        set.put("tardis_id", -50);
        set.put("name", "debug_preview");
        set.put("world", world.getName());
        set.put("x", x + 0.5d);
        set.put("y", 65);
        set.put("z", z + 0.5d);
        plugin.getQueryFactory().doInsert("transmats", set);
        for (int r = -100; r < 100; r++) {
            for (int c = -100; c < 100; c++) {
                world.getBlockAt(x + r, 64, z + c).setType(Material.BLACK_CONCRETE);
                if (r % 2 == 0 && c % 2 == 0) {
                    world.getBlockAt(x + r, 70, z + c).setBlockData(TARDISConstants.LIGHT_DIV);
                }
            }
        }
    }

    public void items() {
        TARDISShopItemSpawner spawner = new TARDISShopItemSpawner(plugin);
        for (ShopItem item : ShopItem.values()) {
            if (item.getRecipeType() == ShopItemRecipe.SHAPED || item.getRecipeType() == ShopItemRecipe.SHAPELESS) {
                // 78 shaped, 20 shapeless
                // TODO loop x z 20 x 20 blocks with empty blocks between
                Location location = new Location(world, 0,0,0);
                // set block at location
                location.getBlock().setType(Material.WHITE_CONCRETE);
                ItemStack is = new ItemStack(item.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.setCustomModelData(item.getCustomModelData());
                im.setDisplayName(item.getDisplayName());
                im.getPersistentDataContainer().set(plugin.getShopSettings().getItemKey(), PersistentDataType.INTEGER, 10001);
                is.setItemMeta(im);
                ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
                display.setItemStack(is);
                display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
                display.setBillboard(Display.Billboard.VERTICAL);
                display.setInvulnerable(true);
                TextDisplay text = (TextDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.65d, 0.5d), EntityType.TEXT_DISPLAY);
                text.setAlignment(TextDisplay.TextAlignment.CENTER);
                text.setText(item.getDisplayName());
                text.setTransformation(new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, TARDISConstants.VECTOR_QUARTER, TARDISConstants.AXIS_ANGLE_ZERO));
                text.setBillboard(Display.Billboard.VERTICAL);
            }
        }
    }

    public void blocks() {
        for (TARDISDisplayItem tdi : TARDISDisplayItem.values()) {
            if (tdi != TARDISDisplayItem.NONE && tdi.getCustomModelData() != -1 && !tdi.toString().contains("DOOR")) {
                // 122 blocks - surgery room x-ray
                // TODO loop x z - spaced over 24 x 24 with empty blocks between
                Location location = new Location(world, 0,0,0);
                // set display item at location
                TARDISDisplayItemUtils.set(tdi, world, 0,0,0);
            }
        }
    }

    public void monsters() {
        for (Monster monster : Monster.values()) {
            // equip armour stands
        }
    }

    public void chameleon() {
        // all 4 states need 3x3 with empty blocks between
        // ~ 22
        for (ChameleonPreset preset : ChameleonPreset.values()) {
            if (preset.usesArmourStand() && preset != ChameleonPreset.ITEM) {

            }
        }
        for (String c : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
            // custom models
        }
    }

    public void rotors() {
        for (Rotor rotor : Rotor.byCustomModelData.values()) {
            // set item frame
            // set item
            // lock
            // invisible
        }
    }

    public void doors() {
        // + open states
        for (Door door : Door.byName.values()) {

        }
    }

    public void consoles() {
        Block block = world.getBlockAt(0, 0, 0);
        for (int i = 1; i < 18; i++) {
            new ConsoleBuilder(plugin).create(block, i, -1, UUID.randomUUID().toString());
        }
    }
}
