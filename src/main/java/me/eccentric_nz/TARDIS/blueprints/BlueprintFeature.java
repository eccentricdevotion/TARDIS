/*
 * Copyright (C) 2023 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.blueprints;

public enum BlueprintFeature {

    ABANDON("tardis.abandon"),
    ACID_BYPASS("tardis.acid.bypass"),
    ADVANCED("tardis.advanced"),
    ARCHITECTURAL("tardis.architectural"),
    ARCHIVE("tardis.archive"),
    ATMOSPHERIC("tardis.atmospheric"),
    AUTONOMOUS("tardis.autonomous"),
    BACKDOOR("tardis.backdoor"),
    CHAMELEON_ARCH("tardis.chameleonarch"),
    CHEMISTRY_BREW("tardis.chemistry.brew"),
    CHEMISTRY_COMMAND("tardis.chemistry.command"),
    CHEMISTRY_CREATIVE("tardis.chemistry.creative"),
    CHEMISTRY_CURE("tardis.chemistry.cure"),
    CHEMISTRY_GLUE("tardis.chemistry.glue"),
    COMPOUND_CREATE("tardis.compound.create"),
    CONSTRUCT_BUILD("tardis.construct.build"),
    DIFFICULTY("tardis.difficulty"),
    EJECT("tardis.eject"),
    FARM("tardis.farm"),
    FILTER("tardis.filter"),
    FORCEFIELD("tardis.forcefield"),
    FORMULA_SHOW("tardis.formula.show"),
    FURNACE("tardis.furnace"),
    GRAVITY("tardis.gravity"),
    HANDLES("tardis.handles"),
    LAB_COMBINE("tardis.lab.combine"),
    LAZARUS("tardis.lazarus"),
    PAPER_BAG("tardis.paper_bag"),
    REDUCER_USE("tardis.reducer.use"),
    REMOTE("tardis.remote"),
    REPAIR("tardis.repair"),
    RIFT("tardis.rift"),
    SCANNER_MAP("tardis.scanner.map"),
    STORAGE("tardis.storage"),
    STORE("tardis.store"),
    TEMPORAL("tardis.temporal"),
    TEXTURE("tardis.texture"),
    TRANSLATE("tardis.translate"),
    TRANSMAT("tardis.transmat"),
    TWELFTH("tardis.twelfth"),
    UPDATE("tardis.update"),
    UPGRADE("tardis.upgrade"),
    VAULT("tardis.vault"),
    WEATHER_CLEAR("tardis.weather.clear"),
    WEATHER_RAIN("tardis.weather.rain"),
    WEATHER_THUNDER("tardis.weather.thunder"),
    NONE("tardis.use");

    private final String permission;

    BlueprintFeature(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
