package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.ArchiveUpdate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FloodgateDesktopThemeForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final String path = "https://raw.githubusercontent.com/eccentricdevotion/TARDIS-Resource-Pack/master/assets/tardis/textures/block/seed/%s.png";
    private final HashMap<String, String> blocks = new HashMap<>();

    public FloodgateDesktopThemeForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        blocks.put("ANCIENT", "sculk");
        blocks.put("ARS", "quartz");
        blocks.put("BIGGER", "gold");
        blocks.put("BONE", "waxed_oxidized_cut_copper");
        blocks.put("BUDGET", "iron");
        blocks.put("CAVE", "cave");
        blocks.put("COPPER", "warped_planks");
        blocks.put("CORAL", "netherwart");
        blocks.put("CURSED", "black_concrete");
        blocks.put("CUSTOM", "custom");
        blocks.put("DELTA", "crying_obsidian");
        blocks.put("DELUXE", "diamond");
        blocks.put("DIVISION", "pink_glazed");
        blocks.put("ELEVENTH", "emerald");
        blocks.put("ENDER", "purpur");
        blocks.put("FACTORY", "yellow_concrete_powder");
        blocks.put("FIFTEENTH", "ochre_froglight");
        blocks.put("FUGITIVE", "polished_deepslate");
        blocks.put("HOSPITAL", "white_concrete");
        blocks.put("LEGACY_BIGGER", "orange_glazed");
        blocks.put("LEGACY_DELUXE", "lime_glazed");
        blocks.put("LEGACY_ELEVENTH", "cyan_glazed");
        blocks.put("LEGACY_REDSTONE", "red_glazed");
        blocks.put("MASTER", "netherbrick");
        blocks.put("MECHANICAL", "polished_andesite");
        blocks.put("ORIGINAL", "packed_mud");
        blocks.put("PLANK", "plank");
        blocks.put("PYRAMID", "sandstone");
        blocks.put("REDSTONE", "redstone");
        blocks.put("ROTOR", "honeycomb_block");
        blocks.put("STEAMPUNK", "coal");
        blocks.put("THIRTEENTH", "orange_concrete");
        blocks.put("TOM", "lapis");
        blocks.put("TWELFTH", "prismarine");
        blocks.put("WAR", "white_terracotta");
        blocks.put("WEATHERED", "weathered");
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Upgrade Menu");
        for (Map.Entry<String, Schematic> a : Consoles.getBY_NAMES().entrySet()) {
            Material m = a.getValue().getSeedMaterial();
            if (!m.equals(Material.COBBLESTONE)) {
                builder.button(a.getKey(), FormImage.Type.URL, String.format(path, blocks.get(a.getKey())));
            }
        }
        builder.button("Archive Consoles", FormImage.Type.URL, "https://raw.githubusercontent.com/eccentricdevotion/TARDIS-Resource-Pack/master/assets/tardis/textures/item/gui/theme/archive.png");
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = response.clickedButton().text();
        if (label.equals("Archive Consoles")) {
            new FloodgateDesktopArchiveForm(plugin, uuid).send();
        } else {
            Schematic schm = Consoles.getBY_NAMES().get(label);
            if (schm != null) {
                Player player = plugin.getServer().getPlayer(uuid);
                // get permission based on choice
                String perm = schm.getPermission();
                if (TARDISPermission.hasPermission(player, "tardis." + perm)) {
                    // remember the upgrade choice
                    TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                    int upgrade = plugin.getArtronConfig().getInt("upgrades." + perm);
                    int needed = (tud.getPrevious().getPermission().equals(schm.getPermission())) ? upgrade / 2 : upgrade;
                    if (tud.getLevel() >= needed) {
                        tud.setSchematic(schm);
                        plugin.getTrackerKeeper().getUpgrades().put(player.getUniqueId(), tud);
                        if (tud.getPrevious().getPermission().equals("archive")) {
                            new ArchiveUpdate(plugin, player.getUniqueId().toString(), "ª°º").setInUse();
                        }
                        // open wall form
                        new FloodgateWallFloorForm(plugin, uuid, "Wall").send();
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_NO_ENERGY", label);
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_UPGRADE_CONSOLE");
                }
            }
        }
    }
}
