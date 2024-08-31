package me.eccentric_nz.TARDIS.rooms.debug;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
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
import me.eccentric_nz.tardisweepingangels.equip.ArmourStandEquipment;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.UUID;

public class DebugPopulator {

    private final TARDIS plugin;
    private final World world;
    private final int rx = -1552;
    private final int rz = -9744;

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
        int x = -2;
        int z = 2;
        for (ShopItem item : ShopItem.values()) {
            // 78 shaped, 20 shapeless
            if (item.getRecipeType() == ShopItemRecipe.SHAPED || item.getRecipeType() == ShopItemRecipe.SHAPELESS) {
                Location location = new Location(world, rx + x, 65, rz + z);
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
                // labels
                TextDisplay text = (TextDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.65d, 0.5d), EntityType.TEXT_DISPLAY);
                text.setAlignment(TextDisplay.TextAlignment.CENTER);
                text.setText(item.getDisplayName());
                text.setTransformation(new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, TARDISConstants.VECTOR_QUARTER, TARDISConstants.AXIS_ANGLE_ZERO));
                text.setBillboard(Display.Billboard.VERTICAL);
                // loop x z 24 x 24 blocks with empty blocks between
                x -= 2;
                if (x < -26) {
                    x = -2;
                    z += 2;
                }
            }
        }
    }

    public void blocks() {
        int x = 2;
        int z = 2;
        for (TARDISDisplayItem tdi : TARDISDisplayItem.values()) {
            // 122 blocks - surgery room x-ray
            if (tdi != TARDISDisplayItem.NONE && tdi != TARDISDisplayItem.PANDORICA && tdi != TARDISDisplayItem.UNTEMPERED_SCHISM && tdi.getCustomModelData() != -1 && !tdi.toString().contains("DOOR")) {
                Location location = new Location(world, rx + x, 65, rz + z);
                // set display item at location
                TARDISDisplayItemUtils.set(tdi, world, rx + x, 65, rz + z);
                //labels
                TextDisplay text = (TextDisplay) location.getWorld().spawnEntity(location.clone().add(0.5d, 1.65d, 0.5d), EntityType.TEXT_DISPLAY);
                text.setAlignment(TextDisplay.TextAlignment.CENTER);
                text.setText(tdi.getDisplayName());
                text.setTransformation(new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, TARDISConstants.VECTOR_QUARTER, TARDISConstants.AXIS_ANGLE_ZERO));
                text.setBillboard(Display.Billboard.VERTICAL);
                // loop x z - spaced over 24 x 24 with empty blocks between
                x += 2;
                if (x > 24) {
                    x = 2;
                    z += 2;
                }
            }
        }
    }

    public void monsters() {
        int x = 3;
        int z = -3;
        for (Monster monster : Monster.values()) {
            if (monster != Monster.FLYER) {
                if (monster != Monster.DALEK) {
                    Location location = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand stand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
                    // equip armour stands
                    new ArmourStandEquipment().setStandEquipment(stand, monster, (monster == Monster.EMPTY_CHILD));
                    x += 3;
                    if (x > 24) {
                        x = 3;
                        z -= 3;
                    }
                }
                if (monster == Monster.HEADLESS_MONK) {
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    new ArmourStandEquipment().setStandEquipment(as, monster, false);
                    // set helmet to sword version
                    setHelmet(as, 405);
                    x += 3;
                    if (x > 24) {
                        x = 3;
                        z -= 3;
                    }
                }
                if (monster == Monster.MIRE || monster == Monster.SLITHEEN) {
                    // set no helmet!
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    new ArmourStandEquipment().setStandEquipment(as, monster, false);
                    // set helmet to sword version
                    setHelmet(as, 5);
                    x += 3;
                    if (x > 24) {
                        x = 3;
                        z -= 3;
                    }
                }
                if (monster == Monster.CLOCKWORK_DROID) {
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    new ArmourStandEquipment().setStandEquipment(as, monster, false);
                    // set female
                    setHelmet(as, 7);
                    x += 3;
                    if (x > 24) {
                        x = 3;
                        z -= 3;
                    }
                }
                if (monster == Monster.DALEK) {
                    for (int c = 0; c < 17; c++) {
                        Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                        ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                        new ArmourStandEquipment().setStandEquipment(as, monster, false);
                        // set helmet to sword version
                        setHelmet(as, 10000005 + c);
                        x += 3;
                        if (x > 24) {
                            x = 3;
                            z -= 3;
                        }
                    }
                }
                if (monster == Monster.OOD) {
                    for (int c = 411; c < 436; c += 6) {
                        Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                        ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                        new ArmourStandEquipment().setStandEquipment(as, monster, false);
                        // set colour and red eye variants
                        setHelmet(as, c);
                        x += 3;
                        if (x > 24) {
                            x = 3;
                            z -= 3;
                        }
                    }
                }
            }
        }
    }

    private void setHelmet(ArmorStand as, int cmd) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            EntityEquipment ee = as.getEquipment();
            ItemStack head = ee.getHelmet();
            ItemMeta meta = head.getItemMeta();
            meta.setCustomModelData(cmd);
            head.setItemMeta(meta);
            ee.setHelmet(head);
        }, 2L);
    }

    public void chameleon() {
        // all 4 states need 3x3 with empty blocks between
        // ~ 22
        int x = -4;
        int r = 0;
        for (ChameleonPreset preset : ChameleonPreset.values()) {
            if (preset.usesArmourStand() && preset != ChameleonPreset.ITEM) {
                int cmd = 1001;
                for (int z = -4 - r; z > -17 - r; z -= 4) {
                    Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                    ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                    EntityEquipment ee = as.getEquipment();
                    Material dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, -1, true);
                    ItemStack head = new ItemStack(dye, 1);
                    ItemMeta meta = head.getItemMeta();
                    meta.setCustomModelData(cmd);
                    head.setItemMeta(meta);
                    ee.setHelmet(head);
                    as.setInvisible(true);
                    cmd++;
                }
                x -= 4;
                if (x < -49) {
                    x = -4;
                    r += 16;
                }
            }
        }
        for (String c : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
            // custom models
            int cmd = 1001;
            for (int z = -4 - r; z > -17 - r; z -= 4) {
                Location loc = new Location(world, rx + x + 0.5d, 65, rz + z + 0.5d);
                ArmorStand as = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
                EntityEquipment ee = as.getEquipment();
                Material material = Material.valueOf(plugin.getCustomModelConfig().getString("models." + c + ".item"));
                ItemStack head = new ItemStack(material, 1);
                ItemMeta meta = head.getItemMeta();
                meta.setCustomModelData(cmd);
                head.setItemMeta(meta);
                ee.setHelmet(head);
                as.setInvisible(true);
                cmd++;
            }
            x -= 4;
            if (x < -49) {
                x = -4;
                r += 16;
            }
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
