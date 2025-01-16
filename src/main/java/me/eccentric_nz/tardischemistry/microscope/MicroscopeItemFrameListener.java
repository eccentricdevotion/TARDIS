package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class MicroscopeItemFrameListener implements Listener {

    private final TARDIS plugin;

    MicroscopeItemFrameListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteractEntity(PlayerInteractAtEntityEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Entity entity = event.getRightClicked();
        if (entity instanceof ItemFrame frame) {
            if (!frame.getPersistentDataContainer().has(plugin.getMicroscopeKey(), PersistentDataType.INTEGER)) {
                return;
            }
            event.setCancelled(true);
            frame.setRotation(getPreviousRotation(frame.getRotation()));
            Player player = event.getPlayer();
            if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
                return;
            }
            // get the item in the frame
            ItemStack dye = frame.getItem();
            if (dye.getType().isAir()) {
                return;
            }
            if (!LabEquipment.getByMaterial().containsKey(dye.getType())) {
                return;
            }
            ItemStack is = player.getInventory().getItemInMainHand();
            ItemMeta im = is.getItemMeta();
            LabEquipment equipment = LabEquipment.getByMaterial().get(dye.getType());
            switch (equipment) {
                case SLIDE_RACK -> {
                    // if hand contains a slide return it
                    if (MicroscopeUtils.hasItemInHand(is, Material.GLASS, plugin)) {
                        MicroscopeUtils.reduceInHand(player);
                    } else {
                        // open the slide GUI
                        ItemStack[] slides = new SlideInventory(plugin).getItems();
                        Inventory inventory = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Slides");
                        inventory.setContents(slides);
                        player.openInventory(inventory);
                    }
                }
                case MICROSCOPE -> {
                    // microscope
                    String key;
                    if (MicroscopeUtils.hasItemInHand(is, Material.GLASS, plugin)) {
                        // set microscope slide
                        key = im.getPersistentDataContainer().get(plugin.getMicroscopeKey(), PersistentDataType.STRING);
                        frame.getPersistentDataContainer().set(plugin.getMicroscopeKey(), PersistentDataType.STRING, key);
                    } else {
                        // view current slide
                        key = frame.getPersistentDataContainer().get(plugin.getMicroscopeKey(), PersistentDataType.STRING);
                    }
                    // remember item in hand for restoration
                    MicroscopeUtils.STORED_STACKS.put(player.getUniqueId(), is);
                    // set item in hand
                    ItemStack slide = new ItemStack(Material.GLASS, 1);
                    ItemMeta slideMeta = slide.getItemMeta();
                    String[] split = key.split("/");
                    slideMeta.setDisplayName(TARDISStringUtils.sentenceCase(split[1]));
                    slideMeta.setItemModel(new NamespacedKey(plugin, key));
                    slide.setItemMeta(slideMeta);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.getInventory().setItemInMainHand(slide), 1L);
                }
                case COMPUTER_MONITOR -> {
                    // computer storage
                    if (MicroscopeUtils.hasItemInHand(is, Material.LIME_STAINED_GLASS, plugin)) {
                        MicroscopeUtils.reduceInHand(player);
                    } else {
                        // open computer GUI
                        ItemStack[] screens = new ComputerInventory(plugin).getItems();
                        Inventory inventory = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Computer Storage");
                        inventory.setContents(screens);
                        player.openInventory(inventory);
                    }
                }
                case ELECTRON_MICROSCOPE -> {
                    // electron microscope
                    String key;
                    if (MicroscopeUtils.hasItemInHand(is, Material.LIME_STAINED_GLASS, plugin)) {
                        // set microscope screen
                        key = im.getPersistentDataContainer().get(plugin.getMicroscopeKey(), PersistentDataType.STRING);
                        frame.getPersistentDataContainer().set(plugin.getMicroscopeKey(), PersistentDataType.STRING, key);
                    } else {
                        // view current slide
                        key = frame.getPersistentDataContainer().get(plugin.getMicroscopeKey(), PersistentDataType.STRING);
                    }
                    // remember item in hand for restoration
                    MicroscopeUtils.STORED_STACKS.put(player.getUniqueId(), is);
                    // set item in hand
                    ItemStack screen = new ItemStack(Material.LIME_STAINED_GLASS, 1);
                    ItemMeta screenMeta = screen.getItemMeta();
                    String[] split = key.split("/");
                    screenMeta.setDisplayName(TARDISStringUtils.sentenceCase(split[1]));
                    screenMeta.setItemModel(new NamespacedKey(plugin, key));
                    screen.setItemMeta(screenMeta);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.getInventory().setItemInMainHand(screen), 1L);
                }
                case FILING_CABINET -> {
                    // filing cabinet
                    if (MicroscopeUtils.hasItemInHand(is, Material.GRAY_STAINED_GLASS, plugin)) {
                        MicroscopeUtils.reduceInHand(player);
                    } else {
                        // open filing cabinet GUI
                        ItemStack[] helmets = new FileCabinetInventory(plugin).getItems();
                        Inventory inventory = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Map Cabinet");
                        inventory.setContents(helmets);
                        player.openInventory(inventory);
                    }
                }
                default -> {
                    // telescope
                    String key;
                    if (MicroscopeUtils.hasItemInHand(is, Material.GRAY_STAINED_GLASS, plugin)) {
                        // set microscope screen
                        key = im.getPersistentDataContainer().get(plugin.getMicroscopeKey(), PersistentDataType.STRING);
                        frame.getPersistentDataContainer().set(plugin.getMicroscopeKey(), PersistentDataType.STRING, key);
                    } else {
                        // view current slide
                        key = frame.getPersistentDataContainer().get(plugin.getMicroscopeKey(), PersistentDataType.STRING);
                    }
                    // remember item in hand for restoration
                    MicroscopeUtils.STORED_STACKS.put(player.getUniqueId(), is);
                    // set item in hand
                    ItemStack helmet = new ItemStack(Material.GRAY_STAINED_GLASS, 1);
                    ItemMeta helmetMeta = helmet.getItemMeta();
                    String[] split = key.split("/");
                    helmetMeta.setDisplayName(TARDISStringUtils.sentenceCase(split[1]));
                    helmetMeta.setItemModel(new NamespacedKey(plugin, key));
                    helmet.setItemMeta(helmetMeta);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.getInventory().setItemInMainHand(helmet), 1L);
                }
            }
        }
    }

    private Rotation getPreviousRotation(Rotation rotation) {
        int max = Rotation.values().length - 1;
        int ord = rotation.ordinal() - 1;
        if (ord < 0) {
            ord = max;
        }
        return Rotation.values()[ord];
    }
}
