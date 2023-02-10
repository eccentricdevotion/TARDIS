package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
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
        blocks.put("CAVE", "cave");
        blocks.put("STEAMPUNK", "coal");
        blocks.put("CUSTOM", "custom");
        blocks.put("DELUXE", "diamond");
        blocks.put("ELEVENTH", "emerald");
        blocks.put("BIGGER", "gold");
        blocks.put("ROTOR", "honeycomb_block");
        blocks.put("BUDGET", "iron");
        blocks.put("TOM", "lapis");
        blocks.put("LEGACY_ELEVENTH", "cyan_glazed");
        blocks.put("LEGACY_BUDGET", "light_gray_glazed");
        blocks.put("LEGACY_DELUXE", "lime_glazed");
        blocks.put("MASTER", "netherbrick");
        blocks.put("CORAL", "netherwart");
        blocks.put("THIRTEENTH", "orange_concrete");
        blocks.put("LEGACY_BIGGER", "orange_glazed");
        blocks.put("ORIGINAL", "packed_mud");
        blocks.put("DIVISION", "pink_glazed");
        blocks.put("PLANK", "plank");
        blocks.put("MECHANICAL", "polished_andesite");
        blocks.put("FUGITIVE", "polished_deepslate");
        blocks.put("TWELFTH", "prismarine");
        blocks.put("ENDER", "purpur");
        blocks.put("ARS", "quartz");
        blocks.put("LEGACY_REDSTONE", "red_glazed");
        blocks.put("REDSTONE", "redstone");
        blocks.put("PYRAMID", "sandstone");
        blocks.put("ANCIENT", "sculk");
        blocks.put("COPPER", "warped_planks");
        blocks.put("WEATHERED", "weathered");
        blocks.put("WAR", "white_terracotta");
        blocks.put("FACTORY", "yellow_concrete_powder");
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
        builder.validResultHandler(response -> handleResponse(response));
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = response.clickedButton().text();
        Schematic schm = Consoles.getBY_NAMES().get(label);
        if (schm != null) {
            Player p = plugin.getServer().getPlayer(uuid);
            // get permission based on choice
            String perm = schm.getPermission();
            if (TARDISPermission.hasPermission(p, "tardis." + perm)) {
                // remember the upgrade choice
                TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                int upgrade = plugin.getArtronConfig().getInt("upgrades." + perm);
                int needed = (tud.getPrevious().getPermission().equals(schm.getPermission())) ? upgrade / 2 : upgrade;
                if (tud.getLevel() >= needed) {
                    tud.setSchematic(schm);
                    plugin.getTrackerKeeper().getUpgrades().put(p.getUniqueId(), tud);
                    if (tud.getPrevious().getPermission().equals("archive")) {
                        new ArchiveUpdate(plugin, p.getUniqueId().toString(), "ª°º").setInUse();
                    }
                    // open wall form
                    new FloodgateWallFloorForm(plugin, uuid, "Wall").send();
                }
            } else {
                TARDISMessage.send(p, "NO_PERM_UPGRADE_CONSOLE");
            }
        }
    }
}
