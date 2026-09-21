package me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Utility class to make alphabetic banners.
 * <p>
 * To make a red getBanner(base) with white letters:
 * Use:
 * DyeColor base = `DyeColor.RED`
 * DyeColor letter = `DyeColor.WHITE`
 */
public class Letters {

    public static ItemStack A(DyeColor base, DyeColor letter) {
        ItemStack a = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_MIDDLE));
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(base, PatternType.BORDER));
        a.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return a;
    }

    public static ItemStack B(DyeColor base, DyeColor letter) {
        ItemStack b = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(base, PatternType.CURLY_BORDER));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_MIDDLE));
        bm.add(new Pattern(base, PatternType.BORDER));
        b.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return b;
    }

    public static ItemStack C(DyeColor base, DyeColor letter) {
        ItemStack c = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(base, PatternType.STRIPE_MIDDLE));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(base, PatternType.BORDER));
        c.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return c;
    }

    public static ItemStack D(DyeColor base, DyeColor letter) {
        ItemStack d = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(base, PatternType.CURLY_BORDER));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(base, PatternType.BORDER));
        d.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return d;
    }

    public static ItemStack E(DyeColor base, DyeColor letter) {
        ItemStack e = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(letter, PatternType.STRIPE_MIDDLE));
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(base, PatternType.BORDER));
        e.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return e;
    }

    public static ItemStack F(DyeColor base, DyeColor letter) {
        ItemStack f = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_MIDDLE));
        bm.add(new Pattern(base, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(base, PatternType.BORDER));
        f.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return f;
    }

    public static ItemStack G(DyeColor base, DyeColor letter) {
        ItemStack g = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(base, PatternType.HALF_HORIZONTAL));
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(base, PatternType.BORDER));
        g.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return g;
    }

    public static ItemStack H(DyeColor base, DyeColor letter) {
        ItemStack h = ItemStack.of(getBanner(letter));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(base, PatternType.STRIPE_TOP));
        bm.add(new Pattern(base, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(base, PatternType.BORDER));
        h.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return h;
    }

    public static ItemStack I(DyeColor base, DyeColor letter) {
        ItemStack i = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_CENTER));
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(base, PatternType.BORDER));
        i.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return i;
    }

    public static ItemStack J(DyeColor base, DyeColor letter) {
        ItemStack j = ItemStack.of(getBanner(letter));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(base, PatternType.HALF_HORIZONTAL));
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(base, PatternType.BORDER));
        j.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return j;
    }

    public static ItemStack K(DyeColor base, DyeColor letter) {
        ItemStack k = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_DOWNRIGHT));
        bm.add(new Pattern(base, PatternType.HALF_HORIZONTAL));
        bm.add(new Pattern(letter, PatternType.STRIPE_DOWNLEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(base, PatternType.BORDER));
        k.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return k;
    }

    public static ItemStack L(DyeColor base, DyeColor letter) {
        ItemStack l = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(base, PatternType.BORDER));
        l.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return l;
    }

    public static ItemStack M(DyeColor base, DyeColor letter) {
        ItemStack m = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.TRIANGLE_TOP));
        bm.add(new Pattern(base, PatternType.TRIANGLES_TOP));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(base, PatternType.BORDER));
        m.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return m;
    }

    public static ItemStack N(DyeColor base, DyeColor letter) {
        ItemStack n = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(base, PatternType.TRIANGLE_TOP));
        bm.add(new Pattern(letter, PatternType.STRIPE_DOWNRIGHT));
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(base, PatternType.BORDER));
        n.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return n;
    }

    public static ItemStack O(DyeColor base, DyeColor letter) {
        ItemStack o = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(base, PatternType.BORDER));
        o.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return o;
    }

    public static ItemStack P(DyeColor base, DyeColor letter) {
        ItemStack p = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(base, PatternType.HALF_HORIZONTAL_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_MIDDLE));
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(base, PatternType.BORDER));
        p.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return p;
    }

    public static ItemStack Q(DyeColor base, DyeColor letter) {
        ItemStack q = ItemStack.of(getBanner(letter));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(base, PatternType.RHOMBUS));
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.SQUARE_BOTTOM_RIGHT));
        bm.add(new Pattern(base, PatternType.BORDER));
        q.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return q;
    }

    public static ItemStack R(DyeColor base, DyeColor letter) {
        ItemStack r = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.HALF_HORIZONTAL));
        bm.add(new Pattern(base, PatternType.STRIPE_CENTER));
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_DOWNRIGHT));
        bm.add(new Pattern(base, PatternType.BORDER));
        r.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return r;
    }

    public static ItemStack S(DyeColor base, DyeColor letter) {
        ItemStack s = ItemStack.of(getBanner(letter));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(base, PatternType.RHOMBUS));
        bm.add(new Pattern(base, PatternType.STRIPE_MIDDLE));
        bm.add(new Pattern(letter, PatternType.STRIPE_DOWNRIGHT));
        bm.add(new Pattern(base, PatternType.BORDER));
        s.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return s;
    }

    public static ItemStack T(DyeColor base, DyeColor letter) {
        ItemStack t = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(letter, PatternType.STRIPE_CENTER));
        bm.add(new Pattern(base, PatternType.BORDER));
        t.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return t;
    }

    public static ItemStack U(DyeColor base, DyeColor letter) {
        ItemStack u = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(base, PatternType.BORDER));
        u.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return u;
    }

    public static ItemStack V(DyeColor base, DyeColor letter) {
        ItemStack v = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_DOWNLEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(base, PatternType.TRIANGLE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_DOWNLEFT));
        bm.add(new Pattern(base, PatternType.BORDER));
        v.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return v;
    }

    public static ItemStack W(DyeColor base, DyeColor letter) {
        ItemStack w = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.TRIANGLE_BOTTOM));
        bm.add(new Pattern(base, PatternType.TRIANGLES_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(base, PatternType.BORDER));
        w.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return w;
    }

    public static ItemStack X(DyeColor base, DyeColor letter) {
        ItemStack x = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.CROSS));
        bm.add(new Pattern(base, PatternType.BORDER));
        x.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return x;
    }

    public static ItemStack Y(DyeColor base, DyeColor letter) {
        ItemStack y = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_DOWNRIGHT));
        bm.add(new Pattern(base, PatternType.HALF_HORIZONTAL_BOTTOM));
        bm.add(new Pattern(letter, PatternType.STRIPE_DOWNLEFT));
        bm.add(new Pattern(base, PatternType.BORDER));
        y.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return y;
    }

    public static ItemStack Z(DyeColor base, DyeColor letter) {
        ItemStack z = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_TOP));
        bm.add(new Pattern(letter, PatternType.STRIPE_DOWNLEFT));
        bm.add(new Pattern(letter, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(base, PatternType.BORDER));
        z.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return z;
    }

    public static ItemStack exclamation(DyeColor base, DyeColor letter) {
        ItemStack ex = ItemStack.of(getBanner(base));
        List<Pattern> bm = new ArrayList<>();
        bm.add(new Pattern(letter, PatternType.STRIPE_CENTER));
        bm.add(new Pattern(base, PatternType.STRIPE_BOTTOM));
        bm.add(new Pattern(letter, PatternType.TRIANGLES_BOTTOM));
        bm.add(new Pattern(base, PatternType.STRIPE_LEFT));
        bm.add(new Pattern(base, PatternType.STRIPE_RIGHT));
        bm.add(new Pattern(base, PatternType.BORDER));
        ex.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()
                .addAll(bm)
                .build());
        return ex;
    }

    public static void giveAll(Player player) {
        player.performCommand("/give @p minecraft:cyan_shulker_box[container=[{slot:0,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_right,color:white},{pattern:stripe_left,color:white},{pattern:stripe_middle,color:white},{pattern:stripe_top,color:white},{pattern:border,color:cyan}],custom_name:\"Letter A\"},count:16}},{slot:1,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_right,color:white},{pattern:stripe_bottom,color:white},{pattern:stripe_top,color:white},{pattern:curly_border,color:cyan},{pattern:stripe_left,color:white},{pattern:stripe_middle,color:white},{pattern:border,color:cyan}],custom_name:\"Letter B\"},count:16}},{slot:2,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_top,color:white},{pattern:stripe_bottom,color:white},{pattern:stripe_right,color:white},{pattern:stripe_middle,color:cyan},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter C\"},count:16}},{slot:3,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_right,color:white},{pattern:stripe_bottom,color:white},{pattern:stripe_top,color:white},{pattern:curly_border,color:cyan},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter D\"},count:16}},{slot:4,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_left,color:white},{pattern:stripe_top,color:white},{pattern:stripe_middle,color:white},{pattern:stripe_bottom,color:white},{pattern:border,color:cyan}],custom_name:\"Letter E\"},count:16}},{slot:5,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_middle,color:white},{pattern:stripe_right,color:cyan},{pattern:stripe_top,color:white},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter F\"},count:16}},{slot:6,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_right,color:white},{pattern:half_horizontal,color:cyan},{pattern:stripe_bottom,color:white},{pattern:stripe_left,color:white},{pattern:stripe_top,color:white},{pattern:border,color:cyan}],custom_name:\"Letter G\"},count:16}},{slot:7,item:{id:white_banner,components:{banner_patterns:[{pattern:stripe_top,color:cyan},{pattern:stripe_bottom,color:cyan},{pattern:stripe_left,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter H\"},count:16}},{slot:8,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_center,color:white},{pattern:stripe_top,color:white},{pattern:stripe_bottom,color:white},{pattern:border,color:cyan}],custom_name:\"Letter I\"},count:16}},{slot:9,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_left,color:white},{pattern:half_horizontal,color:cyan},{pattern:stripe_bottom,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter J\"},count:16}},{slot:10,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_downright,color:white},{pattern:half_horizontal,color:cyan},{pattern:stripe_downleft,color:white},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter K\"},count:16}},{slot:11,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_bottom,color:white},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter L\"},count:16}},{slot:12,item:{id:cyan_banner,components:{banner_patterns:[{pattern:triangle_top,color:white},{pattern:triangles_top,color:cyan},{pattern:stripe_left,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter M\"},count:16}},{slot:13,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_left,color:white},{pattern:triangle_top,color:cyan},{pattern:stripe_downright,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter N\"},count:16}},{slot:14,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_left,color:white},{pattern:stripe_right,color:white},{pattern:stripe_bottom,color:white},{pattern:stripe_top,color:white},{pattern:border,color:cyan}],custom_name:\"Letter O\"},count:16}},{slot:15,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_right,color:white},{pattern:half_horizontal_bottom,color:cyan},{pattern:stripe_middle,color:white},{pattern:stripe_top,color:white},{pattern:stripe_left,color:white},{pattern:border,color:cyan}],custom_name:\"Letter P\"},count:16}},{slot:16,item:{id:white_banner,components:{banner_patterns:[{pattern:rhombus,color:cyan},{pattern:stripe_right,color:white},{pattern:stripe_left,color:white},{pattern:square_bottom_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter Q\"},count:16}},{slot:17,item:{id:cyan_banner,components:{banner_patterns:[{pattern:half_horizontal,color:white},{pattern:stripe_center,color:cyan},{pattern:stripe_top,color:white},{pattern:stripe_left,color:white},{pattern:stripe_downright,color:white},{pattern:border,color:cyan}],custom_name:\"Letter R\"},count:16}},{slot:18,item:{id:white_banner,components:{banner_patterns:[{pattern:rhombus,color:cyan},{pattern:stripe_middle,color:cyan},{pattern:stripe_downright,color:white},{pattern:border,color:cyan}],custom_name:\"Letter S\"},count:16}},{slot:19,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_top,color:white},{pattern:stripe_center,color:white},{pattern:border,color:cyan}],custom_name:\"Letter T\"},count:16}},{slot:20,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_bottom,color:white},{pattern:stripe_left,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter U\"},count:16}},{slot:21,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_downleft,color:white},{pattern:stripe_left,color:white},{pattern:triangle_bottom,color:cyan},{pattern:stripe_downleft,color:white},{pattern:border,color:cyan}],custom_name:\"Letter V\"},count:16}},{slot:22,item:{id:cyan_banner,components:{banner_patterns:[{pattern:triangle_bottom,color:white},{pattern:triangles_bottom,color:cyan},{pattern:stripe_left,color:white},{pattern:stripe_right,color:white},{pattern:border,color:cyan}],custom_name:\"Letter W\"},count:16}},{slot:23,item:{id:cyan_banner,components:{banner_patterns:[{pattern:cross,color:white},{pattern:border,color:cyan}],custom_name:\"Letter X\"},count:16}},{slot:24,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_downright,color:white},{pattern:half_horizontal_bottom,color:cyan},{pattern:stripe_downleft,color:white},{pattern:border,color:cyan}],custom_name:\"Letter Y\"},count:16}},{slot:25,item:{id:cyan_banner,components:{banner_patterns:[{pattern:stripe_top,color:white},{pattern:stripe_downleft,color:white},{pattern:stripe_bottom,color:white},{pattern:border,color:cyan}],custom_name:\"Letter Z\"},count:16}}]]");
    }

    public static void makeCode(Player player) {
        char[] upper = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        char[] lower = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        Registry<PatternType> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN);
        // get shulker box player is targeting
        Block b = player.getTargetBlock(null, 4);
        if (b.getType() == Material.CYAN_SHULKER_BOX) {
            ShulkerBox box = (ShulkerBox) b.getState();
            Inventory inventory = box.getInventory();
            int i = 0;
            for (ItemStack is : inventory.getContents()) {
                if (is != null && (is.getType() == Material.CYAN_BANNER || is.getType() == Material.WHITE_BANNER)) {
                    TARDIS.plugin.debug("public static ItemStack " + upper[i] + "(DyeColor base, DyeColor letter) {");
                    TARDIS.plugin.debug("ItemStack " + lower[i] + " = ItemStack.of(getBanner(base));");
                    TARDIS.plugin.debug("List<Pattern> bm = new ArrayList<>();");
                    BannerPatternLayers bm = is.getData(DataComponentTypes.BANNER_PATTERNS);
                    for (Pattern p : bm.patterns()) {
                        String type = registry.getKey(p.getPattern()).getKey().toUpperCase(Locale.ROOT);
                        TARDIS.plugin.debug("bm.add(new Pattern(DyeColor." + p.getColor() + ", PatternType." + type + "));");
                    }
                    TARDIS.plugin.debug(lower[i] + ".setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers()");
                    TARDIS.plugin.debug("        .addAll(bm)");
                    TARDIS.plugin.debug(".build());");
                    TARDIS.plugin.debug("return " + lower[i] + ";");
                    TARDIS.plugin.debug("}");
                    TARDIS.plugin.debug(" ");
                    i++;
                }
            }
        }
    }

    private static Material getBanner(DyeColor colour) {
        return Material.valueOf(colour + "_BANNER");
    }
}
