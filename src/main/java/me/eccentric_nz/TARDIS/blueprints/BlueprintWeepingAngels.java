/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software"), you can redistribute it and/or modify
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
 * along with this program. If not, see <http"),//www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.blueprints;

public enum BlueprintWeepingAngels {

    BUILD_ANGEL("tardisweepingangels.build.angel"),
    BUILD_JUDOON("tardisweepingangels.build.judoon"),
    BUILD_K9("tardisweepingangels.build.k9"),
    BUILD__OOD("tardisweepingangels.build.ood"),
    COUNT("tardisweepingangels.count"),
    DISGUISE("tardisweepingangels.disguise"),
    FOLLOW("tardisweepingangels.follow"),
    K9("tardisweepingangels.k9"),
    KILL("tardisweepingangels.kill"),
    REMOVE_JUDOON("tardisweepingangels.remove.judoon"),
    REMOVE_K9("tardisweepingangels.remove.k9"),
    REMOVE_OOD("tardisweepingangels.remove.ood"),
    SPAWN_ANGEL("tardisweepingangels.spawn.angel"),
    SPAWN_BEAST("tardisweepingangels.spawn.beast"),
    SPAWN_CLOCKWORK("tardisweepingangels.spawn.clockwork"),
    SPAWN_CYBERMAN("tardisweepingangels.spawn.cyberman"),
    SPAWN_CYBERSHADE("tardisweepingangels.spawn.cybershade"),
    SPAWN_DALEK("tardisweepingangels.spawn.dalek"),
    SPAWN_DALEK_SEC("tardisweepingangels.spawn.dalek_sec"),
    SPAWN_DAVROS("tardisweepingangels.spawn.davros"),
    SPAWN_EMPTY("tardisweepingangels.spawn.empty"),
    SPAWN_HATH("tardisweepingangels.spawn.hath"),
    SPAWN_ICE("tardisweepingangels.spawn.ice"),
    SPAWN_JUDOON("tardisweepingangels.spawn.judoon"),
    SPAWN_K9("tardisweepingangels.spawn.k9"),
    SPAWN_LIBERTY("tardisweepingangels.spawn.liberty"),
    SPAWN_MIRE("tardisweepingangels.spawn.mire"),
    SPAWN_MONK("tardisweepingangels.spawn.monk"),
    SPAWN_OMEGA("tardisweepingangels.spawn.omega"),
    SPAWN_OOD("tardisweepingangels.spawn.ood"),
    SPAWN_OSSIFIED("tardisweepingangels.spawn.ossified"),
    SPAWN_RACNOSS("tardisweepingangels.spawn.racnoss"),
    SPAWN_SCARECROW("tardisweepingangels.spawn.scarecrow"),
    SPAWN_SEA_DEVIL("tardisweepingangels.spawn.sea_devil"),
    SPAWN_SILENT("tardisweepingangels.spawn.silent"),
    SPAWN_SILURIAN("tardisweepingangels.spawn.silurian"),
    SPAWN_SLITHEEN("tardisweepingangels.spawn.slitheen"),
    SPAWN_SMILER("tardisweepingangels.spawn.smiler"),
    SPAWN_SONTARAN("tardisweepingangels.spawn.sontaran"),
    SPAWN_STRAX("tardisweepingangels.spawn.strax"),
    SPAWN_SUTEKH("tardisweepingangels.spawn.sutekh"),
    SPAWN_SYCORAX("tardisweepingangels.spawn.sycorax"),
    SPAWN_TOCLAFANE("tardisweepingangels.spawn.toclafane"),
    SPAWN_VAMPIRE("tardisweepingangels.spawn.vampire"),
    SPAWN_VASHTA("tardisweepingangels.spawn.vashta"),
    SPAWN_ZYGON("tardisweepingangels.spawn.zygon"),
    USE("weeping_angels.use");

    private final String permission;

    BlueprintWeepingAngels(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
