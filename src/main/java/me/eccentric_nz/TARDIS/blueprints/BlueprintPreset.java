package me.eccentric_nz.TARDIS.blueprints;

public enum BlueprintPreset {

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
    NEW("tardis.preset.new"),
    OLD("tardis.preset.old"),
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
    WELL("tardis.preset.well"),
    WINDMILL("tardis.preset.windmill"),
    YELLOW("tardis.preset.yellow")

//    REBUILD("tardis.rebuild"),

    ;

    private final String permission;

    BlueprintPreset(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
