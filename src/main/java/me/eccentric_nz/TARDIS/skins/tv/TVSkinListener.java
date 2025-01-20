package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUITelevision;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.handles.wiki.WikiLink;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.skins.*;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class TVSkinListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final List<String> titles = List.of(
            NamedTextColor.DARK_RED + "Doctor Skins",
            NamedTextColor.DARK_RED + "Companion Skins",
            NamedTextColor.DARK_RED + "Character Skins",
            NamedTextColor.DARK_RED + "Monster Skins",
            NamedTextColor.DARK_RED + "Cyberman Skins"
    );

    public TVSkinListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onSkinMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (!titles.contains(title)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 35) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        switch (slot) {
            case 29 -> toggleDownloads(view);
            case 31 -> {
                // remove skin
                plugin.getSkinChanger().remove(player);
                Skin skin = SkinUtils.SKINNED.get(uuid);
                SkinUtils.removeExtras(player, skin);
                SkinUtils.SKINNED.remove(uuid);
            }
            case 33 -> {
                // back
                ItemStack[] items = new TVInventory().getMenu();
                Inventory tvinv = plugin.getServer().createInventory(player, 36, NamedTextColor.DARK_RED + "TARDIS Television");
                tvinv.setContents(items);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(tvinv), 2L);
            }
            case 35 -> close(player); // close
            default -> {
                if (SkinUtils.SKINNED.containsKey(uuid)) {
                    // remove the skin items
                    SkinUtils.removeExtras(player, SkinUtils.SKINNED.get(uuid));
                }
                Skin skin = ArchSkins.ARI;
                String which = TARDISStaticUtils.stripColor(title).split(" ")[0];
                switch (which) {
                    case "Doctor" -> skin = DoctorSkins.DOCTORS.get(slot);
                    case "Companion" -> skin = CompanionSkins.COMPANIONS.get(slot);
                    case "Character" -> skin = CharacterSkins.CHARACTERS.get(slot);
                    case "Monster" -> skin = MonsterSkins.MONSTERS.get(slot);
                    case "Cyberman" -> skin = CyberSkins.VARIANTS.get(slot);
                }
                if (isDownload(view)) {
                    plugin.getMessenger().sendWikiLink(player, new WikiLink("Download " + skin.name() + " skin file", skin.url(), true));
                } else {
                    plugin.getSkinChanger().set(player, skin);
                    SkinUtils.setExtras(player, skin);
                    SkinUtils.SKINNED.put(uuid, skin);
                }
                close(player);
            }
        }
    }

    private void toggleDownloads(InventoryView view) {
        // get item in download slot
        ItemStack is = view.getItem(GUITelevision.DOWNLOAD.slot());
        if (is != null && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            NamespacedKey key = (im.hasItemModel() && SwitchVariant.DOWNLOAD_OFF.getKey().equals(im.getItemModel()))
                    ? SwitchVariant.DOWNLOAD_ON.getKey(): SwitchVariant.DOWNLOAD_OFF.getKey();
            im.setItemModel(key);
            is.setItemMeta(im);
            view.setItem(GUITelevision.DOWNLOAD.slot(), is);
        }
    }

    private boolean isDownload(InventoryView view) {
        // get item in download slot
        ItemStack is = view.getItem(GUITelevision.DOWNLOAD.slot());
        if (is != null && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            return im.hasItemModel() && SwitchVariant.DOWNLOAD_ON.getKey().equals(im.getItemModel());
        }
        return false;
    }
}
