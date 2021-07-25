/*
 * Copyright (C) 2021 eccentric_nz
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

public enum BlueprintPreset {

    ADAPTIVE("tardis.preset.adaptive"),
    ANDESITE("tardis.preset.andesite"),
    ANGEL("tardis.preset.angel"),
    APPERTURE("tardis.preset.apperture"),
    CAKE("tardis.preset.cake"),
    CANDY("tardis.preset.candy"),
    CHALICE("tardis.preset.chalice"),
    CHORUS("tardis.preset.chorus"),
    CREEPY("tardis.preset.creepy"),
    CUSTOM("tardis.preset.custom"),
    DESERT("tardis.preset.desert"),
    DIORITE("tardis.preset.diorite"),
    DUCK("tardis.preset.duck"),
    FENCE("tardis.preset.fence"),
    FLOWER("tardis.preset.flower"),
    GAZEBO("tardis.preset.gazebo"),
    GRANITE("tardis.preset.granite"),
    GRAVESTONE("tardis.preset.gravestone"),
    HELIX("tardis.preset.helix"),
    JAIL("tardis.preset.jail"),
    JUNGLE("tardis.preset.jungle"),
    LAMP("tardis.preset.lamp"),
    LIBRARY("tardis.preset.library"),
    LIGHTHOUSE("tardis.preset.lighthouse"),
    MINESHAFT("tardis.preset.mineshaft"),
    NETHER("tardis.preset.nether"),
    PANDORICA("tardis.preset.pandorica"),
    PARTY("tardis.preset.party"),
    PEANUT("tardis.preset.peanut"),
    PINE("tardis.preset.pine"),
    POLICE_BOX_BLACK("tardis.preset.police_box_black"),
    POLICE_BOX_BLUE("tardis.preset.police_box_blue"),
    POLICE_BOX_BROWN("tardis.preset.police_box_brown"),
    POLICE_BOX_CYAN("tardis.preset.police_box_cyan"),
    POLICE_BOX_GRAY("tardis.preset.police_box_gray"),
    POLICE_BOX_GREEN("tardis.preset.police_box_green"),
    POLICE_BOX_LIGHT_BLUE("tardis.preset.police_box_light_blue"),
    POLICE_BOX_LIGHT_GRAY("tardis.preset.police_box_light_gray"),
    POLICE_BOX_LIME("tardis.preset.police_box_lime"),
    POLICE_BOX_MAGENTA("tardis.preset.police_box_magenta"),
    POLICE_BOX_ORANGE("tardis.preset.police_box_orange"),
    POLICE_BOX_PINK("tardis.preset.police_box_pink"),
    POLICE_BOX_PURPLE("tardis.preset.police_box_purple"),
    POLICE_BOX_RED("tardis.preset.police_box_red"),
    POLICE_BOX_WHITE("tardis.preset.police_box_white"),
    POLICE_BOX_YELLOW("tardis.preset.police_box_yellow"),
    PORTAL("tardis.preset.portal"),
    PRISMARINE("tardis.preset.prismarine"),
    PUNKED("tardis.preset.punked"),
    ROBOT("tardis.preset.robot"),
    SHROOM("tardis.preset.shroom"),
    SNOWMAN("tardis.preset.snowman"),
    STONE("tardis.preset.stone"),
    SUBMERGED("tardis.preset.submerged"),
    SWAMP("tardis.preset.swamp"),
    TELEPHONE("tardis.preset.telephone"),
    TOILET("tardis.preset.toilet"),
    TOPSYTURVEY("tardis.preset.topsyturvey"),
    TORCH("tardis.preset.torch"),
    VILLAGE("tardis.preset.village"),
    WEEPING_ANGEL("tardis.preset.weeping_angel"),
    WELL("tardis.preset.well"),
    WINDMILL("tardis.preset.windmill"),
    YELLOW("tardis.preset.yellow");

    private final String permission;

    BlueprintPreset(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
