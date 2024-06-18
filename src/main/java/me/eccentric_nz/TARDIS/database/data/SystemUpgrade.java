package me.eccentric_nz.TARDIS.database.data;

import me.eccentric_nz.TARDIS.upgrades.SystemTree;

import java.util.HashMap;

public class SystemUpgrade {

    private final int artronLevel;
    private final HashMap<SystemTree, Boolean> upgrades = new HashMap<>();

    public SystemUpgrade(int artronLevel, boolean architecture, boolean chameleon, boolean rooms, boolean desktop, boolean navigation, boolean saves, boolean distance1, boolean distance2, boolean distance3, boolean telepathic, boolean interDimension, boolean flight, boolean tools, boolean locator, boolean biomeReader, boolean forceField, boolean monitor, boolean stattenheimRemote) {
        this.artronLevel = artronLevel;
        this.upgrades.put(SystemTree.UPGRADE_TREE, true);
        this.upgrades.put(SystemTree.ARCHITECTURE, architecture);
        this.upgrades.put(SystemTree.CHAMELEON_CIRCUIT, chameleon);
        this.upgrades.put(SystemTree.ROOM_GROWING, rooms);
        this.upgrades.put(SystemTree.DESKTOP_THEME, desktop);
        this.upgrades.put(SystemTree.NAVIGATION, navigation);
        this.upgrades.put(SystemTree.SAVES, saves);
        this.upgrades.put(SystemTree.DISTANCE_1, distance1);
        this.upgrades.put(SystemTree.DISTANCE_2, distance2);
        this.upgrades.put(SystemTree.DISTANCE_3 , distance3);
        this.upgrades.put(SystemTree.TELEPATHIC_CIRCUIT , telepathic);
        this.upgrades.put(SystemTree.INTER_DIMENSIONAL_TRAVEL, interDimension);
        this.upgrades.put(SystemTree.EXTERIOR_FLIGHT, flight);
        this.upgrades.put(SystemTree.TOOLS, tools);
        this.upgrades.put(SystemTree.TARDIS_LOCATOR, locator);
        this.upgrades.put(SystemTree.BIOME_READER, biomeReader);
        this.upgrades.put(SystemTree.FORCE_FIELD, forceField);
        this.upgrades.put(SystemTree.MONITOR, monitor);
        this.upgrades.put(SystemTree.STATTENHEIM_REMOTE, stattenheimRemote);
    }

    public int getArtronLevel() {
        return artronLevel;
    }

    public HashMap<SystemTree, Boolean> getUpgrades() {
        return upgrades;
    }
}
