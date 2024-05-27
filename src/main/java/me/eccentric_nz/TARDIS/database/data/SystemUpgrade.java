package me.eccentric_nz.TARDIS.database.data;

import me.eccentric_nz.TARDIS.custommodeldata.GUISystemTree;

import java.util.HashMap;

public class SystemUpgrade {

    private final int artronLevel;
    private final HashMap<GUISystemTree, Boolean> upgrades = new HashMap<>();

    public SystemUpgrade(int artronLevel, boolean architecture, boolean chameleon, boolean rooms, boolean desktop, boolean navigation, boolean saves, boolean distance1, boolean distance2, boolean distance3, boolean interDimension, boolean tools, boolean locator, boolean biomeReader, boolean forceField, boolean stattenheimRemote) {
        this.artronLevel = artronLevel;
        this.upgrades.put(GUISystemTree.UPGRADE_TREE, true);
        this.upgrades.put(GUISystemTree.ARCHITECTURE, architecture);
        this.upgrades.put(GUISystemTree.CHAMELEON_CIRCUIT, chameleon);
        this.upgrades.put(GUISystemTree.ROOM_GROWING, rooms);
        this.upgrades.put(GUISystemTree.DESKTOP_THEME, desktop);
        this.upgrades.put(GUISystemTree.NAVIGATION, navigation);
        this.upgrades.put(GUISystemTree.SAVES, saves);
        this.upgrades.put(GUISystemTree.DISTANCE_1, distance1);
        this.upgrades.put(GUISystemTree.DISTANCE_2, distance2);
        this.upgrades.put(GUISystemTree.DISTANCE_3 , distance3);
        this.upgrades.put(GUISystemTree.INTER_DIMENSIONAL_TRAVEL, interDimension);
        this.upgrades.put(GUISystemTree.TOOLS, tools);
        this.upgrades.put(GUISystemTree.TARDIS_LOCATOR, locator);
        this.upgrades.put(GUISystemTree.BIOME_READER, biomeReader);
        this.upgrades.put(GUISystemTree.FORCE_FIELD, forceField);
        this.upgrades.put(GUISystemTree.STATTENHEIM_REMOTE, stattenheimRemote);
    }

    public int getArtronLevel() {
        return artronLevel;
    }

    public HashMap<GUISystemTree, Boolean> getUpgrades() {
        return upgrades;
    }
}
